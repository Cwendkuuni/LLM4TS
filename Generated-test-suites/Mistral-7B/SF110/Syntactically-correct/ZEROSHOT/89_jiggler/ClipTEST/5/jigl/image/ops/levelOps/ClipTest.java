package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

 private static final int TEST_IMAGE_WIDTH = 5;
 private static final int TEST_IMAGE_HEIGHT = 5;

 @Test
 public void testConstructorIntInt() {
 Clip clip = new Clip(1, 3);
 assertEquals(1, clip.int_min);
 assertEquals(3, clip.int_max);
 }

 @Test
 public void testConstructorFloatFloat() {
 Clip clip = new Clip(2.5f, 4.5f);
 assertEquals(2.5f, clip.float_min, 0.001f);
 assertEquals(4.5f, clip.float_max, 0.001f);
 }

 @Test
 public void testApplyGrayImageIntInt() {
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Integer.valueOf(y * TEST_IMAGE_WIDTH + x));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1, 3);
 GrayImage outputImage = clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int expectedValue = y * TEST_IMAGE_WIDTH + x;
 int actualValue = outputImage.get(x, y).intValue();
 assertEquals(expectedValue, actualValue);
 }
 }
 }

 @Test
 public void testApplyGrayImageFloatFloat() {
 GrayImage inputImage = new GrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Integer.valueOf(y * TEST_IMAGE_WIDTH + x));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1.5f, 3.5f);
 GrayImage outputImage = clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expectedValue = y * TEST_IMAGE_WIDTH + x;
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageIntInt() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Float.valueOf((float)y * TEST_IMAGE_WIDTH + x));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1, 3);
 RealGrayImage outputImage = (RealGrayImage)clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expectedValue = y * TEST_IMAGE_WIDTH + x;
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageFloatFloat() {
 RealGrayImage inputImage = new RealGrayImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT);
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.set(x, y, Float.valueOf((float)y * TEST_IMAGE_WIDTH + x));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1.5f, 3.5f);
 RealGrayImage outputImage = (RealGrayImage)clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expectedValue = y * TEST_IMAGE_WIDTH + x;
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testApplyColorImage() {
 ColorImage inputImage = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, new ByteType(0), new ByteType(0), new ByteType(0));
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.setPlane(0, x, y, Integer.valueOf((y * TEST_IMAGE_WIDTH + x) % 256));
 inputImage.setPlane(1, x, y, Integer.valueOf((y * TEST_IMAGE_WIDTH + x) % 256));
 inputImage.setPlane(2, x, y, Integer.valueOf((y * TEST_IMAGE_WIDTH + x) % 256));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1, 3);
 ColorImage outputImage = clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int expectedValue = (y * TEST_IMAGE_WIDTH + x) % 256;
 int actualValue = outputImage.getPlane(plane, x, y).intValue();
 assertEquals(expectedValue, actualValue);
 }
 }
 }
 }

 @Test
 public void testApplyColorImageFloatFloat() {
 ColorImage inputImage = new ColorImage(TEST_IMAGE_WIDTH, TEST_IMAGE_HEIGHT, new ByteType(0), new ByteType(0), new ByteType(0));
 for (int y = 0; y < TEST_IMAGE_HEIGHT; ++y) {
 for (int x = 0; x < TEST_IMAGE_WIDTH; ++x) {
 inputImage.setPlane(0, x, y, Integer.valueOf((int)(y * TEST_IMAGE_WIDTH + x)));
 inputImage.setPlane(1, x, y, Integer.valueOf((int)(y * TEST_IMAGE_WIDTH + x)));
 inputImage.setPlane(2, x, y, Integer.valueOf((int)(y * TEST_IMAGE_WIDTH + x)));
 }
 }
 ROI roi = new ROI(1, 1, TEST_IMAGE_WIDTH - 1, TEST_IMAGE_HEIGHT - 1);
 Clip clip = new Clip(1.5f, 3.5f);
 ColorImage outputImage = clip.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float expectedValue = (y * TEST_IMAGE_WIDTH + x);
 float actualValue = outputImage.getPlane(plane, x, y).getSample(0, 0).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }
 }
}