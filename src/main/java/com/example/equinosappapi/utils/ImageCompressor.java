package com.example.equinosappapi.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageCompressor {

    private static final double COMPRESSION_FACTOR = 0.5;

    private static final int MAX_WIDTH = 500; // px



    public byte[] compressImage(byte[] bytes) throws IOException {
        // long originalSize = bytes.length;
        // System.out.println("Original size = " + originalSize);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage originalImage = ImageIO.read(inputStream);

        int targetWidth = MAX_WIDTH;  // Width
        int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth())); // Maintain aspect ratio

        //int targetWidth = (int) (originalImage.getWidth() * COMPRESSION_FACTOR);
        //int targetHeight = (int) (originalImage.getHeight() * COMPRESSION_FACTOR);

        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(outputImage, "jpeg", outputStream);
//        byte[] compressedImage = outputStream.toByteArray();
//        long compressedSize = compressedImage.length;
//        System.out.println("Compressed size = " + compressedSize);
        return outputStream.toByteArray();
    }
}
