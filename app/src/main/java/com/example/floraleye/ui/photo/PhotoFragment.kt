package com.example.floraleye.ui.photo

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.floraleye.R
import com.example.floraleye.databinding.FragmentPhotoBinding
import com.example.floraleye.ml.ModelFlowers
import com.example.floraleye.ml.ModelGeneral
import com.example.floraleye.repositories.PhotoRepository
import com.example.floraleye.utils.Constants
import com.example.floraleye.utils.PhotoUtils
import com.example.floraleye.viewmodels.PhotoViewModel
import com.example.floraleye.viewmodels.PhotoViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.label.Category
import java.io.IOException


/**
 * Fragment per la gestione dell'acquisizione di una immagine.
 */
class PhotoFragment : Fragment() {

    private lateinit var mBinding: FragmentPhotoBinding

    private lateinit var photoViewModel: PhotoViewModel

    private val choosePhotoResultLauncher =
        registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d(TAG, "pickMediaResultLauncher: Selected URI: $uri")
                photoViewModel.setImageByUri(uri)
                PhotoUtils.setAllTextsInvisible(mBinding)
            } else {
                Log.d(TAG, "pickMediaResultLauncher: No media selected.")
                mBinding.photoProgressBar.visibility = View.INVISIBLE
            }
        }

    @Suppress("DEPRECATION")
    private val takePhotoResultLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result != null) {
                val image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.extras?.getParcelable("data", Bitmap::class.java)
                } else {
                    result.data?.extras?.getParcelable("data")
                }
                if (image != null) {
                    setImageByBitmap(image)
                    PhotoUtils.setAllTextsInvisible(mBinding)
                } else {
                    Log.d(TAG, "takePhotoResultLauncher: image is null.")
                    mBinding.photoProgressBar.visibility = View.INVISIBLE
                }
            } else {
                Log.d(TAG, "takePhotoResultLauncher: result is null.")
                mBinding.photoProgressBar.visibility = View.INVISIBLE
            }
        }

    companion object {

        private val TAG: String = PhotoFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPhotoBinding.inflate(inflater, container, false)

        PhotoUtils.restoreInstanceState(mBinding, savedInstanceState)

        photoViewModel = ViewModelProvider(
            this,
            PhotoViewModelFactory(
                requireActivity().application,
                PhotoRepository()
            )
        )[PhotoViewModel::class.java]

        mBinding.choosePhotoButton.setOnClickListener {
            onChoosePhotoButtonClicked()
        }

        mBinding.identifyButton.setOnClickListener {
            classifyImage(getCurrentDisplayedImage())
        }

        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        photoViewModel.getImage().observe(this) { image ->
            if (image == null) {
                Log.d(TAG, "photoViewModel image observer: image is null.")
            } else {
                Glide.with(requireContext())
                    .load(image)
                    .placeholder(R.drawable.ic_baseline_image_placeholder_208dp)
                    .into(mBinding.photoImageView)
            }
            mBinding.photoProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        photoViewModel.getImage().removeObservers(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        photoViewModel.getImage().removeObservers(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (this::mBinding.isInitialized)
            PhotoUtils.saveInstanceState(mBinding, outState)
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per settare l'immagine mostrata.
     * @param image immagine da mostrare
     */
    fun setImageByBitmap(image: Bitmap) {
        photoViewModel.setImage(image)
    }

    /**
     * Metodo utilizzato, soprattutto in fase di test, per settare una immagine tramite uri.
     * @param uri identificatore univoco dell'immagine
     */
    fun setImageByUri(uri: Uri) {
        photoViewModel.setImageByUri(uri)
    }

    /**
     * Metodo utilizzato, sopratutto in fase di test, per ottenere l'immagine attualmente
     * mostrata.
     * @return immagine attualmente mostrata in formato Bitmap
     */
    fun getCurrentDisplayedImage(): Bitmap {
        return mBinding.photoImageView.drawToBitmap(Bitmap.Config.ARGB_8888)
    }

    private fun onChoosePhotoButtonClicked() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.str_choose_photo)
            .setMessage(R.string.str_choose_photo_message)
            .setNeutralButton(R.string.str_cancel, null)
            .setPositiveButton(R.string.str_take_photo) { _, _ ->
                takePhotoResultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
            .setNegativeButton(R.string.str_load_photo) { _, _ ->
                choosePhotoResultLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }
            .setCancelable(true)
            .show()
    }

    private fun classifyImage(image: Bitmap) {
        try {
            val imageProcessor = ImageProcessor.Builder()
                .add(ResizeOp(Constants.IMAGE_SIZE, Constants.IMAGE_SIZE,
                    ResizeOp.ResizeMethod.BILINEAR))
                .build()

            val tensorImage = TensorImage(DataType.UINT8)
            tensorImage.load(image)
            val processedImage = imageProcessor.process(tensorImage)

            val modelGeneral = ModelGeneral.newInstance(requireContext())

            val isImageFlower = PhotoUtils.isImageFlower(processedImage, modelGeneral)
            if (isImageFlower) {
                classifyFlower(processedImage)
            } else {
                mBinding.textViewFirstResult.text = getString(R.string.str_not_flower)
                PhotoUtils.setTextsNotFlower(mBinding)
            }
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
    }

    private fun classifyFlower(tensorImage: TensorImage) {
        val flowerNames: MutableList<String> = ArrayList()
        val flowerScores: MutableList<String> = ArrayList()

        val modelFlowers = ModelFlowers.newInstance(requireContext())

        val outputs = modelFlowers.process(tensorImage)
        val scores: List<Category> = outputs.scoreAsCategoryList

        val sortedScores = scores.sortedWith(compareByDescending { it.score })

        val numTopScores = Constants.NUM_TOP_SCORES
        for (i in 0 until numTopScores.coerceAtMost(scores.size)) {
            val category = sortedScores[i]
            val index = category.label.toInt() - 1
            val score = category.score
            val flowerName = PhotoUtils.getFlowerNames(requireContext())[index]
            flowerNames.add(flowerName)
            flowerScores.add(PhotoUtils.convertToPercentage(score))
        }

        mBinding.textViewFirstResult.text = flowerNames[0]
        mBinding.textViewFirstConfidence.text = getString(R.string.str_confidence, flowerScores[0])
        mBinding.textViewSecondResult.text = flowerNames[1]
        mBinding.textViewSecondConfidence.text = getString(R.string.str_confidence, flowerScores[1])
        mBinding.textViewThirdResult.text = flowerNames[2]
        mBinding.textViewThirdConfidence.text = getString(R.string.str_confidence, flowerScores[2])
        PhotoUtils.setAllTextsVisible(mBinding)

        modelFlowers.close()
    }

}
