package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.types.*;
import org.junit.Test;

public class ClipTest {

 private static final int TEST_IMAGE_WIDTH = 10;
 private static final int TEST_IMAGE_HEIGHT = 10;
 private static final int TEST_MIN = 5;
 private static final int TEST_MAX = 15;
 private static final float TEST_MIN_FLOAT = 2.5f;
 private static final float TEST_MAX_FLOAT = 7.5f;

 @Test
 public void testClipWithIntMinIntMax() {
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.UINT8);
 for (int i = 0; i < TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT; ++i) {
 inputImage.set(i % TEST_IMAGE_WIDTH, i / TEST_IMAGE_WIDTH, (byte) (i % 10 + 1));
 }
 Clip clip = new Clip(TEST_MIN, TEST_MAX);
 GrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 int expected = Math.max(Math.min(inputImage.get(x, y).getIntValue(), TEST_MAX), TEST_MIN);
 int actual = outputImage.get(x, y).getIntValue();
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected, actual);
 }
 }
 }

 @Test
 public void testClipWithFloatMinFloatMax() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.FLOAT32);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Math.random() * 255.0f);
 }
 }
 Clip clip = new Clip(TEST_MIN_FLOAT, TEST_MAX_FLOAT);
 RealGrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 float expected = Math.max(Math.min(inputImage.get(x, y).getFloatValue(), TEST_MAX_FLOAT), TEST_MIN_FLOAT);
 float actual = outputImage.get(x, y).getFloatValue();
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected, actual, 0.001f);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithBothIntMinIntMaxAndFloatMinFloatMax() {
 Clip clip = new Clip(TEST_MIN, TEST_MAX, TEST_MIN_FLOAT, TEST_MAX_FLOAT);
 }

 @Test
 public void testClipWithIntMinIntMaxOnRealGrayImage() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.FLOAT32);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Math.random() * 255.0f);
 }
 }
 Clip clip = new Clip(TEST_MIN, TEST_MAX);
 RealGrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 float expected = Math.max(Math.min(inputImage.get(x, y).getFloatValue(), TEST_MAX), TEST_MIN);
 float actual = outputImage.get(x, y).getFloatValue();
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testClipWithFloatMinFloatMaxOnGrayImage() {
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.UINT8);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, (byte) (Math.random() * 255.0f + 1));
 }
 }
 Clip clip = new Clip(TEST_MIN_FLOAT, TEST_MAX_FLOAT);
 GrayImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 float expected = Math.max(Math.min(inputImage.get(x, y).getIntValue() / 255.0f, TEST_MAX_FLOAT), TEST_MIN_FLOAT);
 int actual = outputImage.get(x, y).getIntValue();
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", Math.round(expected * 255.0f), actual, 0);
 }
 }
 }

 @Test
 public void testClipWithIntMinIntMaxOnColorImage() {
 RGBImage inputImage = new RGBImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.UINT8, DataType.UINT8, DataType.UINT8);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, new RGB(Math.random() * 255.0f, Math.random() * 255.0f, Math.random() * 255.0f));
 }
 }
 Clip clip = new Clip(TEST_MIN, TEST_MAX);
 RGBImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 RGB expected = new RGB(Math.max(Math.min(inputImage.get(x, y).getRed(), TEST_MAX), TEST_MIN),
 Math.max(Math.min(inputImage.get(x, y).getGreen(), TEST_MAX), TEST_MIN),
 Math.max(Math.min(inputImage.get(x, y).getBlue(), TEST_MAX), TEST_MIN));
 RGB actual = outputImage.get(x, y);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getRed(), actual.getRed(), 0);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getGreen(), actual.getGreen(), 0);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getBlue(), actual.getBlue(), 0);
 }
 }
 }

 @Test
 public void testClipWithFloatMinFloatMaxOnRealColorImage() {
 RealRGBImage inputImage = new RealRGBImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, DataType.FLOAT32, DataType.FLOAT32, DataType.FLOAT32);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, new RGBF(Math.random() * 255.0f, Math.random() * 255.0f, Math.random() * 255.0f));
 }
 }
 Clip clip = new Clip(TEST_MIN_FLOAT, TEST_MAX_FLOAT);
 RealRGBImage outputImage = clip.apply(inputImage);

 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 RGBF expected = new RGBF(Math.max(Math.min(inputImage.get(x, y).getRed(), TEST_MAX_FLOAT), TEST_MIN_FLOAT),
 Math.max(Math.min(inputImage.get(x, y).getGreen(), TEST_MAX_FLOAT), TEST_MIN_FLOAT),
 Math.max(Math.min(inputImage.get(x, y).getBlue(), TEST_MAX_FLOAT), TEST_MIN_FLOAT));
 RGBF actual = outputImage.get(x, y);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getRed(), actual.getRed(), 0.001f);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getGreen(), actual.getGreen(), 0.001f);
 assertEquals("Pixel value is incorrect at position (" + x + ", " + y + ")", expected.getBlue(), actual.getBlue(), 0.001f);
 }
 }
 }
}