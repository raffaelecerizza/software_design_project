import os
import cv2

# Percorso della cartella "train"
train_folder = 'G:/Datasets/Dataset_Oxford_augmented/valid'

# Dimensioni desiderate per le immagini
target_size = (224, 224)

# Itera su tutte le immagini nelle cartelle e sottocartelle di "train"
for root, dirs, files in os.walk(train_folder):
    for file in files:
        if file.endswith('.jpg') or file.endswith('.jpeg') or file.endswith('.png'):
            # Percorso dell'immagine
            image_path = os.path.join(root, file)

            # Carica l'immagine
            image = cv2.imread(image_path)

            # Ridimensiona l'immagine alle dimensioni desiderate
            resized_image = cv2.resize(image, target_size)

            # Sovrascrive l'immagine originale con l'immagine ridimensionata e pre-elaborata
            cv2.imwrite(image_path, resized_image)

print("Ridimensionamento delle immagini completato.")
