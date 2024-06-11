package jigl.image.ops.levelOps.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import org.junit.Test;

public class ClipTest {

 private static final int TEST_IMAGE_WIDTH = 10;
 private static final int TEST_IMAGE_HEIGHT = 10;

 @Test
 public void testClipIntInt() {
 Image inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 inputImage.set(0, 0, (byte) 100);
 inputImage.set(0, 1, (byte) 200);
 inputImage.set(1, 0, (byte) 50);
 inputImage.set(1, 1, (byte) 150);

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply(inputImage);

 byte[] expectedOutput = new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 expectedOutput[0] = 50;
 expectedOutput[1] = 50;
 expectedOutput[2] = 50;
 expectedOutput[3] = 150;
 expectedOutput[4] = 150;
 expectedOutput[5] = 150;
 expectedOutput[6] = 150;
 expectedOutput[7] = 150;
 expectedOutput[8] = 150;
 expectedOutput[9] = 150;
 // ... and so on for the rest of the pixels

 byte[] actualOutput = new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 outputImage.getPixels(actualOutput, 0, TEST_IMAGE_WIDTH, 0, 0, TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);

 assertArrayEquals("The output image doesn't match the expected one", expectedOutput, actualOutput);
 }

 @Test
 public void testClipFloatFloat() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 inputImage.set(0, 0, 100.0f);
 inputImage.set(0, 1, 200.0f);
 inputImage.set(1, 0, 50.0f);
 inputImage.set(1, 1, 150.0f);

 Clip clip = new Clip(0.5f, 1.5f);
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);

 float[] expectedOutput = new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 expectedOutput[0] = 0.5f;
 expectedOutput[1] = 0.5f;
 expectedOutput[2] = 0.5f;
 expectedOutput[3] = 1.5f;
 expectedOutput[4] = 1.5f;
 expectedOutput[5] = 1.5f;
 expectedOutput[6] = 1.5f;
 expectedOutput[7] = 1.5f;
 expectedOutput[8] = 1.5f;
 expectedOutput[9] = 1.5f;
 // ... and so on for the rest of the pixels

 float[] actualOutput = new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 outputImage.getPixels(actualOutput, 0, TEST_IMAGE_WIDTH, 0, 0, TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);

 assertArrayEquals("The output image doesn't match the expected one", expectedOutput, actualOutput, 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipNullImage() {
 Clip clip = new Clip(50, 150);
 GrayImage inputImage = null;
 try {
 clip.apply(inputImage);
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipInvalidMin() {
 Clip clip = new Clip(-1, 50);
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 try {
 clip.apply(inputImage);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipInvalidMax() {
 Clip clip = new Clip(50, -1);
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 try {
 clip.apply(inputImage);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipNullROI() {
 Clip clip = new Clip(50, 150);
 try {
 clip.apply(new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT), null);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected exception
 }
 }

 @Test
 public void testClipIntIntWithROI() {
 Image inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 inputImage.set(0, 0, (byte) 100);
 inputImage.set(0, 1, (byte) 200);
 inputImage.set(1, 0, (byte) 50);
 inputImage.set(1, 1, (byte) 150);

 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply(inputImage, roi);

 byte[] expectedOutput = new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 expectedOutput[1] = 50;
 expectedOutput[2] = 50;
 expectedOutput[3] = 150;
 expectedOutput[4] = 150;
 expectedOutput[5] = 150;
 // ... and so on for the rest of the pixels in the ROI

 byte[] actualOutput = new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 outputImage.getPixels(actualOutput, 0, TEST_IMAGE_WIDTH, 0, 0, TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);

 byte[] roiPixels = new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 inputImage.getROIPixels(roi, roiPixels, 0, 0, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);

 assertArrayEquals("The output image doesn't match the expected one for the ROI", expectedOutput, actualOutput, 0, roiPixels);
 }

 @Test
 public void testClipFloatFloatWithROI() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 inputImage.set(0, 0, 100.0f);
 inputImage.set(0, 1, 200.0f);
 inputImage.set(1, 0, 50.0f);
 inputImage.set(1, 1, 150.0f);

 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);

 Clip clip = new Clip(50.0f, 150.0f);
 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage, roi);

 float[] expectedOutput = new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 expectedOutput[1] = 50.0f;
 expectedOutput[2] = 50.0f;
 expectedOutput[3] = 150.0f;
 expectedOutput[4] = 150.0f;
 expectedOutput[5] = 150.0f;
 // ... and so on for the rest of the pixels in the ROI

 float[] actualOutput = new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 outputImage.getPixels(actualOutput, 0, TEST_IMAGE_WIDTH, 0, 0, TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);

 float[] roiPixels = new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT];
 inputImage.getROIPixels(roi, roiPixels, 0, 0, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);

 assertArrayEquals("The output image doesn't match the expected one for the ROI", expectedOutput, actualOutput, 0.001f, roiPixels);
 }
}