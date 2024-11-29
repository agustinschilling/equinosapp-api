package com.example.equinosappapi.services;

import com.example.equinosappapi.utils.ImageCompressor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class ImageService {

    private final String BASE_DIRECTORY;
    private static final String COMPRESSED_PREFIX = "compressed_";

    public ImageService(@Value("${image.base-directory}") String baseDirectory) {
        this.BASE_DIRECTORY = baseDirectory;
        System.out.println("Ruta absoluta del directorio base: " + Paths.get(BASE_DIRECTORY).toAbsolutePath());

        // Create the base directory if it doesn't exist
        createDirectoryIfNotExists(Paths.get(BASE_DIRECTORY));
        createDirectoryIfNotExists(Paths.get(BASE_DIRECTORY, "users"));
        createDirectoryIfNotExists(Paths.get(BASE_DIRECTORY, "horses"));
        createDirectoryIfNotExists(Paths.get(BASE_DIRECTORY, "analysis"));
    }

    // Method to create a directory if it doesn't exist
    private void createDirectoryIfNotExists(Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not create directory: " + path, e);
            }
        }
    }

    // Method to save an image to the specified directory in normal size and compressed
    public String saveImage(byte[] fileBytes, String extension, String subDirectory) throws IOException {
        // Ensure the directory exists or create it if it doesn't
        createDirectoryIfNotExists(Paths.get(BASE_DIRECTORY, subDirectory));

        // Generate a unique filename using UUID and preserve the extension
        String uniqueFileName = UUID.randomUUID() + extension;
        Path imagePath = Paths.get(BASE_DIRECTORY, subDirectory, uniqueFileName);

        Files.write(imagePath, fileBytes);

        ImageCompressor compressor = new ImageCompressor();
        byte[] compressedImage = compressor.compressImage(fileBytes);

        Path compressedImagePath = Paths.get(BASE_DIRECTORY, subDirectory, COMPRESSED_PREFIX + uniqueFileName);

        Files.write(compressedImagePath, compressedImage);

        // Return the path of the saved file
        return uniqueFileName;
    }


    // Method to retrieve an image from the specified path
    public Resource getImage(String imagePath) {
        try {
            Path path = Paths.get(BASE_DIRECTORY, imagePath);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read image at path: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving image from path: " + imagePath, e);
        }
    }

    // Method to delete an image
    public void deleteImage(String imagePath) {
        try {
            Path path = Paths.get(BASE_DIRECTORY, imagePath);

            if (Files.exists(path)) {
                Files.delete(path);
            } else {
                throw new RuntimeException("Image does not exist at path: " + imagePath);
            }

            // Delete the compressed image if it exists
            Path compressedImagePath = Paths.get(path.getParent().toString(), COMPRESSED_PREFIX + path.getFileName().toString());

            if (Files.exists(compressedImagePath)) {
                Files.delete(compressedImagePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting image from path: " + imagePath, e);
        }
    }

    public static String getImageExtension(String originalFileName) {
        String extension;
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } else {
            extension = ".png";
        }
        return extension;
    }
}