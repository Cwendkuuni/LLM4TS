package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

import org.junit.Test;

public class ClipTest {

 private static final int IMG_WIDTH = 10;
 private static final int IMG_HEIGHT = 5;

 @Test
 public void testClipIntInt() {
 GrayImage inputImage = new GrayImage(IMG_WIDTH, IMG_HEIGHT, new ByteType());
 // Set some values for testing
 for (int y = 0; y < IMG_HEIGHT; ++y) {
 for (int x = 0; x < IMG_WIDTH; ++x) {
 inputImage.set(x, y, (byte) (255 * (x + y) / (IMG_WIDTH + IMG_HEIGHT)));
 }
 }

 Clip clip = new Clip(50, 150);
 GrayImage outputImage = (GrayImage) clip.apply(inputImage);

 // Test typical use case
 for (int y = 0; y < IMG_HEIGHT; ++y) {
 for (int x = 0; x < IMG_WIDTH; ++x) {
 int expected = Math.max(0, Math.min(255, (int) (255 * (x + y) / (IMG_WIDTH + IMG_HEIGHT))));
 int actual = inputImage.get(x, y).intValue();
 assertEquals("Expected value for clip test with int bounds: ", expected, actual);
 assertEquals("Expected value for clipped pixel: ", expected, outputImage.get(x, y).intValue());
 }
 }

 // Test edge cases
 for (int y = 0; y < IMG_HEIGHT; ++y) {
 for (int x = 0; x < IMG_WIDTH; ++x) {
 int value = inputImage.get(x, y).intValue();
 if (value > 150) {
 // Value is greater than max
 int expected = 150;
 assertEquals("Expected value for clipped pixel with max value: ", expected, outputImage.get(x, y).intValue());
 } else if (value < 50) {
 // Value is less than min
 int expected = 50;
 assertEquals("Expected value for clipped pixel with min value: ", expected, outputImage.get(x, y).intValue());
 } else {
 // Value is within bounds
 int expected = value;
 assertEquals("Expected value for clipped pixel within bounds: ", expected, outputImage.get(x, y).intValue());
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidBounds() {
 // Test error scenario with invalid bounds
 Clip clip = new Clip(-10, 150);
 try {
 clip.apply(inputImage);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test exception message
 String expectedMsg = "Invalid bounds for clip operation.";
 String actualMsg = e.getMessage();
 assertEquals("Expected error message for clip test with invalid bounds: ", expectedMsg, actualMsg);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithNullImage() {
 // Test error scenario with null image
 Clip clip = new Clip(50, 150);
 try {
 clip.apply(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test exception message
 String expectedMsg = "Image can't be null.";
 String actualMsg = e.getMessage();
 assertEquals("Expected error message for clip test with null image: ", expectedMsg, actualMsg);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithNullROI() {
 // Test error scenario with null ROI
 Clip clip = new Clip(50, 150);
 GrayImage inputImageROI = new GrayImage(1, 1, new ByteType());
 try {
 clip.apply(inputImage, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Test exception message
 String expectedMsg = "ROI can't be null.";
 String actualMsg = e.getMessage();
 assertEquals("Expected error message for clip test with null ROI: ", expectedMsg, actualMsg);
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testClipWithOutOfBoundsROI() {
 // Test error scenario with out-of-bounds ROI
 Clip clip = new Clip(50, 150);
 GrayImage inputImageROI = new GrayImage(IMG_WIDTH + 1, IMG_HEIGHT + 1, new ByteType());
 ROI roi = new ROI(IMG_WIDTH, IMG_HEIGHT);
 try {
 clip.apply(inputImageROI, roi);
 fail("Expected ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // Test exception message
 String expectedMsg = "Error in ROI bounds.";
 String actualMsg = e.getMessage();
 assertEquals("Expected error message for clip test with out-of-bounds ROI: ", expectedMsg, actualMsg);
 }
 }
}