from tensorflow.keras.preprocessing.image import ImageDataGenerator
import os
import numpy as np
from PIL import Image

def augment_train_set(dataset_path, output_path):
    train_dir = dataset_path
    output_train_dir = output_path

    # Crea un generatore di immagini con le specifiche indicate
    gen = ImageDataGenerator(
        rescale=1. / 255,
        rotation_range=50,
        zoom_range=[0.75, 1.25],
        width_shift_range=0.1,
        height_shift_range=0.1,
        horizontal_flip=True
    )

    for class_folder in os.listdir(train_dir):
        class_path = os.path.join(train_dir, class_folder)
        output_class_path = os.path.join(output_train_dir, class_folder)
        os.makedirs(output_class_path, exist_ok=True)

        # Calcola il numero di immagini esistenti per la classe
        existing_images_count = len(os.listdir(class_path))

        # Calcola il numero di immagini da generare per raggiungere il numero massimo
        images_to_generate = 206 - existing_images_count

        if images_to_generate > 0:
            image_files = os.listdir(class_path)

            # Prendi casualmente delle immagini esistenti per generare nuove immagini
            for i in range(images_to_generate):
                image_file = image_files[i % existing_images_count]
                image_path = os.path.join(class_path, image_file)
                output_image_path = os.path.join(output_class_path, f"augmented_{i}.jpg")

                # Carica l'immagine come array numpy
                img = np.array(Image.open(image_path))

                # Aggiungi una dimensione per conformarsi alle dimensioni richieste dal generatore
                img = np.expand_dims(img, axis=0)

                # Genera e salva l'immagine con data augmentation
                augmented_img = next(gen.flow(img, batch_size=1, save_to_dir=output_class_path, save_prefix='augmented'))
                augmented_img = np.squeeze(augmented_img, axis=0)
                augmented_img = Image.fromarray(np.uint8(augmented_img))

                # Salva l'immagine
                augmented_img.save(output_image_path)

    print("Data augmentation completata.")

dataset_path = 'train/'
output_path = '/content/gdrive/MyDrive/Oxford_102/train_augmented/'
augment_train_set(dataset_path, output_path)
