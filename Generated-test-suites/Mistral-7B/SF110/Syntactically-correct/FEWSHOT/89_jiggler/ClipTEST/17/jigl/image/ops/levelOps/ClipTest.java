package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage inputImage = new BufferedImageGray(3, 3, DataBuffer.TYPE_BYTE);
        inputImage.setPixel(0, 0, (byte) 128);
        inputImage.setPixel(0, 1, (byte) 255);
        inputImage.setPixel(0, 2, (byte) 64);
        inputImage.setPixel(1, 0, (byte) 255);
        inputImage.setPixel(1, 1, (byte) 128);
        inputImage.setPixel(1, 2, (byte) 255);
        inputImage.setPixel(2, 0, (byte) 64);
        inputImage.setPixel(2, 1, (byte) 128);
        inputImage.setPixel(2, 2, (byte) 128);

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(0, 128);
        Image outputImage = clip.apply(inputImage, roi);

        byte[] expectedOutput = {(byte) 128, (byte) 128};
        byte[] output = new byte[outputImage.X() * outputImage.Y()];
        outputImage.getPixels(0, 0, outputImage.X(), outputImage.Y(), 0, 0);
        assertArrayEquals(expectedOutput, output, 0, 2);

        // Test with negative values
        clip = new Clip(256, -1);
        outputImage = clip.apply(inputImage, roi);
        assertNull(outputImage);

        // Test with max value
        clip = new Clip(Integer.MAX_VALUE, Integer.MAX_VALUE);
        outputImage = clip.apply(inputImage, roi);
        assertNotNull(outputImage);
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = new BufferedImageRealGray(3, 3, DataBuffer.TYPE_FLOAT);
        inputImage.setPixel(0, 0, 128.0f);
        inputImage.setPixel(0, 1, 255.0f);
        inputImage.setPixel(0, 2, 64.0f);
        inputImage.setPixel(1, 0, 255.0f);
        inputImage.setPixel(1, 1, 128.0f);
        inputImage.setPixel(1, 2, 255.0f);
        inputImage.setPixel(2, 0, 64.0f);
        inputImage.setPixel(2, 1, 128.0f);
        inputImage.setPixel(2, 2, 128.0f);

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(0.0f, 128.0f);
        Image outputImage = clip.apply(inputImage, roi);

        float[] expectedOutput = {128.0f, 128.0f};
        float[] outputData = new float[outputImage.X() * outputImage.Y()];
        outputImage.getPixels(0, 0, outputImage.X(), outputImage.Y(), 0, 0, outputData);
        assertArrayEquals(expectedOutput, outputData, 0, 2);

        // Test with negative values
        clip = new Clip(256.0f, -1.0f);
        outputImage = clip.apply(inputImage, roi);
        assertNull(outputImage);

        // Test with max value
        clip = new Clip(Float.MAX_VALUE, Float.MAX_VALUE);
        outputImage = clip.apply(inputImage, roi);
        assertNotNull(outputImage);
    }

    @Test
    public void testClipApplyColorImage() {
        ColorImage inputImage = new BufferedImageColor(3, 3, new RGBColor(255, 255, 255), DataBuffer.TYPE_BYTE);
        inputImage.setPixel(0, 0, new RGB(128, 128, 128));
        inputImage.setPixel(0, 1, new RGB(255, 255, 255));
        inputImage.setPixel(0, 2, new RGB(64, 64, 64));
        inputImage.setPixel(1, 0, new RGB(255, 255, 255));
        inputImage.setPixel(1, 1, new RGB(128, 128, 128));
        inputImage.setPixel(1, 2, new RGB(255, 255, 255));
        inputImage.setPixel(2, 0, new RGB(64, 64, 64));
        inputImage.setPixel(2, 1, new RGB(128, 128, 128));
        inputImage.setPixel(2, 2, new RGB(128, 128, 128));

        ROI roi = new ROI(1, 1, 2, 2);

        Clip clip = new Clip(0, 128);
        Image outputImage = clip.apply(inputImage, roi);

        RGB expectedOutput[] = new RGB[] {new RGB(128, 128, 128), new RGB(128, 128, 128)};
        RGB output[] = new RGB[outputImage.X() * outputImage.Y()];
        outputImage.getPixels(0, 0, outputImage.X(), outputImage.Y(), 0, 0, output);
        assertArrayEquals(expectedOutput, output, 0, 2);
    }

    @Test
    public void testClipMain() {
        try {
            String inputFile = "test_image.jpg";
            String outputFile = "test_output.jpg";
            String command = "java jigl.image.ops.levelOps.Clip 0 128 test_image.jpg test_output.jpg";
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "./target/classes:./libs/*", "jigl.image.ops.levelOps.Clip", "0", "128", inputFile, outputFile);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                throw new RuntimeException("Test failed with exit code " + exitValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}