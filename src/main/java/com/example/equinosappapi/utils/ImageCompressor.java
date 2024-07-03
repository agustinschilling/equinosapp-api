package com.example.equinosappapi.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCompressor {

    public byte[] comprimirImagen(byte[] bytes) throws IOException {
        long originalSize = bytes.length;
        System.out.println("Original size = " + originalSize);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage originalImage = ImageIO.read(inputStream);

        int targetWidth = 100;  // Ancho objetivo
        int targetHeight = (int) (originalImage.getHeight() * (100.0 / originalImage.getWidth())); // Mantener la relación de aspecto

        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpeg", outputStream);
        byte[] compressedImage = outputStream.toByteArray();
        long compressedSize = compressedImage.length;
        System.out.println("Compressed size = " + compressedSize);
        return outputStream.toByteArray();
    }

}
