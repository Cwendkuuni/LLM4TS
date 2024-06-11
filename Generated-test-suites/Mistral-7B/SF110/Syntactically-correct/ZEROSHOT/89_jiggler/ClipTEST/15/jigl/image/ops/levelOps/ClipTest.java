package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

 private GrayImage createGrayImage(int width, int height) {
 return new BufferedGrayImage(width, height, new DataBufferByte(width * height));
 }

 private RealGrayImage createRealGrayImage(int width, int height) {
 return new BufferedRealGrayImage(width, height, new DataBufferDouble(width * height));
 }

 private ColorImage createColorImage(int width, int height) {
 return new BufferedColorImage(width, height, new DataBufferByte[3], new Raster());
 }

 private RealColorImage createRealColorImage(int width, int height) {
 return new BufferedRealColorImage(width, height, new DataBufferDouble[3], new Raster());
 }

 @Test
 public void testClipIntInt() {
 GrayImage image = createGrayImage(3, 3);
 Clip clip = new Clip(1, 2);
 clip.apply(image);
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 int value = image.get(x, y).intValue();
 assertEquals(Math.min(value, 2), value);
 }
 }
 }

 @Test
 public void testClipIntIntWithROI() {
 GrayImage image = createGrayImage(3, 3);
 Clip clip = new Clip(1, 2);
 ROI roi = new ROI(1, 1, 2, 2);
 clip.apply(image, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int value = image.get(x, y).intValue();
 assertEquals(Math.min(value, 2), value);
 }
 }
 }

 @Test
 public void testClipFloatFloat() {
 RealGrayImage image = createRealGrayImage(3, 3);
 Clip clip = new Clip(1.5f, 2.5f);
 clip.apply(image);
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float value = image.get(x, y).floatValue();
 assertEquals(Math.min(value, 2.5f), value, 0.01f);
 }
 }
 }

 @Test
 public void testClipFloatFloatWithROI() {
 RealGrayImage image = createRealGrayImage(3, 3);
 Clip clip = new Clip(1.5f, 2.5f);
 ROI roi = new ROI(1, 1, 2, 2);
 clip.apply(image, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float value = image.get(x, y).floatValue();
 assertEquals(Math.min(value, 2.5f), value, 0.01f);
 }
 }
 }

 @Test
 public void testClipIntIntAppliedToColorImage() {
 ColorImage image = createColorImage(3, 3);
 Clip clip = new Clip(1, 2);
 clip.apply(image);
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int value = image.get(x, y, plane).intValue();
 assertEquals(Math.min(value, 2), value);
 }
 }
 }
 }

 @Test
 public void testClipIntIntAppliedToColorImageWithROI() {
 ColorImage image = createColorImage(3, 3);
 Clip clip = new Clip(1, 2);
 ROI roi = new ROI(1, 1, 2, 2);
 clip.apply(image, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int value = image.get(x, y, plane).intValue();
 assertEquals(Math.min(value, 2), value);
 }
 }
 }
 }

 @Test
 public void testClipFloatFloatAppliedToRealColorImage() {
 RealColorImage image = createRealColorImage(3, 3);
 Clip clip = new Clip(1.5f, 2.5f);
 clip.apply(image);
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float value = image.get(x, y, plane).floatValue();
 assertEquals(Math.min(value, 2.5f), value, 0.01f);
 }
 }
 }
 }

 @Test
 public void testClipFloatFloatAppliedToRealColorImageWithROI() {
 RealColorImage image = createRealColorImage(3, 3);
 Clip clip = new Clip(1.5f, 2.5f);
 ROI roi = new ROI(1, 1, 2, 2);
 clip.apply(image, roi);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float value = image.get(x, y, plane).floatValue();
 assertEquals(Math.min(value, 2.5f), value, 0.01f);
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMinValue() {
 GrayImage image = createGrayImage(3, 3);
 Clip clip = new Clip(-1, 2);
 clip.apply(image);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMaxValue() {
 GrayImage image = createGrayImage(3, 3);
 Clip clip = new Clip(1, -1);
 clip.apply(image);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMinValueForRealImage() {
 RealGrayImage image = createRealGrayImage(3, 3);
 Clip clip = new Clip(-1.0f, 2.5f);
 clip.apply(image);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMaxValueForRealImage() {
 RealGrayImage image = createRealGrayImage(3, 3);
 Clip clip = new Clip(1.5f, -1.0f);
 clip.apply(image);
 fail("Exception expected: IllegalArgumentException");
 }

}