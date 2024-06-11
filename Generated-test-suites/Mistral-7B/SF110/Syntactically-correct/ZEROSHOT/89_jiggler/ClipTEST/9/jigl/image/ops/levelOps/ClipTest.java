package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.types.*;
import jigl.image.ops.*;
import org.junit.Test;

public class ClipTest {

 private static final int TEST_IMAGE_WIDTH = 5;
 private static final int TEST_IMAGE_HEIGHT = 5;

 @Test
 public void testClipIntInt() {
 GrayImage inputImage = new ArrayFactory().createGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 for (int i = 0; i < TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT; i++) {
 inputImage.set(i % TEST_IMAGE_WIDTH, i / TEST_IMAGE_WIDTH, Integer.valueOf(values[i]));
 }

 Clip clip = new Clip(2, 8);
 GrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; y++) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; x++) {
 int expectedValue = Math.max(Math.min(values[x + y * TEST_IMAGE_WIDTH], 8), 2);
 int actualValue = outputImage.get(x, y).intValue();
 assertEquals("Value at (" + x + ", " + y + ") is incorrect.", expectedValue, actualValue);
 }
 }
 }

 @Test
 public void testClipIntIntWithZeroParameters() {
 GrayImage inputImage = new ArrayFactory().createGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 for (int i = 0; i < TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT; i++) {
 inputImage.set(i % TEST_IMAGE_WIDTH, i / TEST_IMAGE_HEIGHT, Integer.valueOf(values[i]));
 }

 Clip clip = new Clip(0, 0);
 GrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; y++) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; x++) {
 int expectedValue = Math.min(Math.max(inputImage.get(x, y).intValue(), 0), TEST_IMAGE_WIDTH - 1);
 int actualValue = outputImage.get(x, y).intValue();
 assertEquals("Value at (" + x + ", " + y + ") is incorrect.", expectedValue, actualValue);
 }
 }
 }

 @Test
 public void testClipFloatFloat() {
 RealGrayImage inputImage = new ArrayFactory().createRealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 float[] values = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f };
 for (int i = 0; i < TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT; i++) {
 inputImage.set(i % TEST_IMAGE_WIDTH, i / TEST_IMAGE_HEIGHT, Float.valueOf(values[i]));
 }

 Clip clip = new Clip(2.5f, 8.5f);
 RealGrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; y++) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; x++) {
 float expectedValue = Math.max(Math.min(values[x + y * TEST_IMAGE_WIDTH], 8.5f), 2.5f);
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals("Value at (" + x + ", " + y + ") is incorrect.", expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testClipFloatFloatWithZeroParameters() {
 RealGrayImage inputImage = new ArrayFactory().createRealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 float[] values = { 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f };
 for (int i = 0; i < TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT; i++) {
 inputImage.set(i % TEST_IMAGE_WIDTH, i / TEST_IMAGE_HEIGHT, Float.valueOf(values[i]));
 }

 Clip clip = new Clip(0.0f, 0.0f);
 RealGrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; y++) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; x++) {
 float expectedValue = Math.min(Math.max(inputImage.get(x, y).floatValue(), 0.0f), TEST_IMAGE_WIDTH - 1.0f);
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals("Value at (" + x + ", " + y + ") is incorrect.", expectedValue, actualValue, 0.001f);
 }
 }
 }
}