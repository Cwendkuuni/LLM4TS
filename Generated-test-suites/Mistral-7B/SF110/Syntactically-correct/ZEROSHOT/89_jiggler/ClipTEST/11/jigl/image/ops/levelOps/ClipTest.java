package jigl.image.ops.levelOps.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

import org.junit.Test;

public class ClipTest {

 private static final int TEST_IMAGE_WIDTH = 5;
 private static final int TEST_IMAGE_HEIGHT = 5;

 @Test
 public void testClipIntIntConstructor() {
 Clip clip = new Clip(1, 3);
 assertEquals(1, clip.int_min);
 assertEquals(3, clip.int_max);
 }

 @Test
 public void testClipFloatFloatConstructor() {
 Clip clip = new Clip(1.5f, 3.5f);
 assertEquals(1.5f, clip.float_min, 0.001f);
 assertEquals(3.5f, clip.float_max, 0.001f);
 }

 @Test
 public void testApplyGrayImageIntInt() {
 GrayImage grayImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 Clip clip = new Clip(0, 2);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 grayImage.set(x, y, (byte)x);
 }
 }

 GrayImage expectedResult = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 0 && x < 2 && y >= 0 && y < 2) {
 expectedResult.set(x, y, (byte)x);
 } else {
 expectedResult.set(x, y, (byte)2);
 }
 }
 }

 GrayImage result = (GrayImage)clip.apply(grayImage);
 assertArrayEquals(expectedResult.data(), result.data());
 }

 @Test
 public void testApplyGrayImageIntIntWithROI() {
 GrayImage grayImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 Clip clip = new Clip(0, 2);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 grayImage.set(x, y, (byte)x);
 }
 }

 ROI roi = new ROI(2, 2, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);
 GrayImage expectedResult = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 2 && x < TEST_IMAGE_WIDTH - 2 && y >= 2 && y < TEST_IMAGE_HEIGHT - 2) {
 expectedResult.set(x, y, (byte)x);
 } else {
 expectedResult.set(x, y, (byte)2);
 }
 }
 }

 GrayImage result = (GrayImage)clip.apply(grayImage, roi);
 assertArrayEquals(expectedResult.data(), result.data());
 }

 @Test
 public void testApplyRealGrayImageFloatFloat() {
 RealGrayImage realGrayImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 Clip clip = new Clip(1.5f, 3.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 realGrayImage.set(x, y, x / (float)TEST_IMAGE_WIDTH);
 }
 }

 RealGrayImage expectedResult = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 1.5f && x < 3.5f && y >= 1.5f && y < 3.5f) {
 expectedResult.set(x, y, x / (float)TEST_IMAGE_WIDTH);
 } else {
 expectedResult.set(x, y, 1.0f);
 }
 }
 }

 RealGrayImage result = (RealGrayImage)clip.apply(realGrayImage);
 assertArrayEquals(expectedResult.data(), result.data(), 0.001f);
 }

 @Test
 public void testApplyRealGrayImageFloatFloatWithROI() {
 RealGrayImage realGrayImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 Clip clip = new Clip(1.5f, 3.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 realGrayImage.set(x, y, x / (float)TEST_IMAGE_WIDTH);
 }
 }

 ROI roi = new ROI(2, 2, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);
 RealGrayImage expectedResult = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 2 && x < TEST_IMAGE_WIDTH - 2 && y >= 2 && y < TEST_IMAGE_HEIGHT - 2) {
 expectedResult.set(x, y, x / (float)TEST_IMAGE_WIDTH);
 } else {
 expectedResult.set(x, y, 1.0f);
 }
 }
 }

 RealGrayImage result = (RealGrayImage)clip.apply(realGrayImage, roi);
 assertArrayEquals(expectedResult.data(), result.data(), 0.001f);
 }

 @Test
 public void testApplyColorImage() {
 ColorImage colorImage = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 Clip clip = new Clip(0.5f, 1.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 colorImage.setRGB(x, y, x, x, x);
 }
 }

 ColorImage expectedResult = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 0.5f && x < 1.5f && y >= 0.5f && y < 1.5f) {
 expectedResult.setRGB(x, y, x, x, x);
 } else {
 expectedResult.setRGB(x, y, 0, 0, 0);
 }
 }
 }

 ColorImage result = (ColorImage)clip.apply(colorImage);
 assertArrayEquals(expectedResult.data(), result.data());
 }

 @Test
 public void testApplyColorImageWithROI() {
 ColorImage colorImage = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 Clip clip = new Clip(0.5f, 1.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 colorImage.setRGB(x, y, x, x, x);
 }
 }

 ROI roi = new ROI(2, 2, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);
 ColorImage expectedResult = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new byte[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 2 && x < TEST_IMAGE_WIDTH - 2 && y >= 2 && y < TEST_IMAGE_HEIGHT - 2) {
 expectedResult.setRGB(x, y, x, x, x);
 } else {
 expectedResult.setRGB(x, y, 0, 0, 0);
 }
 }
 }

 ColorImage result = (ColorImage)clip.apply(colorImage, roi);
 assertArrayEquals(expectedResult.data(), result.data());
 }

 @Test
 public void testApplyRealColorImage() {
 RealColorImage realColorImage = new RealColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 Clip clip = new Clip(0.5f, 1.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 realColorImage.setRGB(x, y, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH);
 }
 }

 RealColorImage expectedResult = new RealColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 0.5f && x < 1.5f && y >= 0.5f && y < 1.5f) {
 expectedResult.setRGB(x, y, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH);
 } else {
 expectedResult.setRGB(x, y, 0.0f, 0.0f, 0.0f);
 }
 }
 }

 RealColorImage result = (RealColorImage)clip.apply(realColorImage);
 assertArrayEquals(expectedResult.data(), result.data(), 0.001f);
 }

 @Test
 public void testApplyRealColorImageWithROI() {
 RealColorImage realColorImage = new RealColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 Clip clip = new Clip(0.5f, 1.5f);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 realColorImage.setRGB(x, y, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH);
 }
 }

 ROI roi = new ROI(2, 2, TEST_IMAGE_WIDTH - 2, TEST_IMAGE_HEIGHT - 2);
 RealColorImage expectedResult = new RealColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, 3, new float[TEST_IMAGE_WIDTH * TEST_IMAGE_HEIGHT * 3]);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 if (x >= 2 && x < TEST_IMAGE_WIDTH - 2 && y >= 2 && y < TEST_IMAGE_HEIGHT - 2) {
 expectedResult.setRGB(x, y, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH, x / (float)TEST_IMAGE_WIDTH);
 } else {
 expectedResult.setRGB(x, y, 0.0f, 0.0f, 0.0f);
 }
 }
 }

 RealColorImage result = (RealColorImage)clip.apply(realColorImage, roi);
 assertArrayEquals(expectedResult.data(), result.data(), 0.001f);
 }
}