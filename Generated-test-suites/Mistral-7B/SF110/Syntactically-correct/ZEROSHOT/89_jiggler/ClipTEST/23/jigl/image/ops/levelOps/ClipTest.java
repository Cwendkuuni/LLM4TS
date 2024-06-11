package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;
import org.junit.Test;

public class ClipTest {

 private GrayImage createGrayImage(int width, int height) {
 return new GrayImage(width, height);
 }

 private RealGrayImage createRealGrayImage(int width, int height) {
 return new RealGrayImage(width, height);
 }

 private ColorImage createColorImage(int width, int height) {
 return new ColorImage(3, width, height);
 }

 private RealColorImage createRealColorImage(int width, int height) {
 return new RealColorImage(3, width, height);
 }

 @Test
 public void testClipIntInt() {
 Clip clip = new Clip(5, 10);
 GrayImage image = createGrayImage(10, 10);

 clip.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));

 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 int expected = Math.max(Math.min(image.get(x, y).intValue(), 10), 5);
 int actual = image.get(x, y).intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testClipFloatFloat() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealGrayImage image = createRealGrayImage(10, 10);

 clip.apply(image, new ROI(0, 0, image.X() - 1, image.Y() - 1));

 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y).floatValue(), 1.5f), 0.5f);
 float actual = image.get(x, y).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipIntIntNullROI() {
 Clip clip = new Clip(5, 10);
 GrayImage image = createGrayImage(10, 10);

 clip.apply(image, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipFloatFloatNullROI() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealGrayImage image = createRealGrayImage(10, 10);

 clip.apply(image, null);
 }

 @Test
 public void testClipIntIntWithImageDimensions() {
 Clip clip = new Clip(5, 10);
 GrayImage image = createGrayImage(10, 10);

 clip.apply(image);

 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 int expected = Math.max(Math.min(image.get(x, y).intValue(), 10), 5);
 int actual = image.get(x, y).intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testClipFloatFloatWithImageDimensions() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealGrayImage image = createRealGrayImage(10, 10);

 clip.apply(image);

 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y).floatValue(), 1.5f), 0.5f);
 float actual = image.get(x, y).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testClipIntIntWithImageROI() {
 Clip clip = new Clip(5, 10);
 GrayImage image = createGrayImage(15, 15);
 ROI roi = new ROI(5, 5, 5, 5);

 clip.apply(image, roi);

 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int expected = Math.max(Math.min(image.get(x, y).intValue(), 10), 5);
 int actual = image.get(x, y).intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testClipFloatFloatWithImageROI() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealGrayImage image = createRealGrayImage(15, 15);
 ROI roi = new ROI(5, 5, 5, 5);

 clip.apply(image, roi);

 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y).floatValue(), 1.5f), 0.5f);
 float actual = image.get(x, y).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testClipIntIntWithColorImage() {
 Clip clip = new Clip(5, 10);
 ColorImage image = createColorImage(10, 10);

 clip.apply(image);

 for (int plane = 0; plane < image.numPlanes(); ++plane) {
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 int expected = Math.max(Math.min(image.get(x, y, plane).intValue(), 10), 5);
 int actual = image.get(x, y, plane).intValue();
 assertEquals(expected, actual);
 }
 }
 }
 }

 @Test
 public void testClipFloatFloatWithColorImage() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealColorImage image = createRealColorImage(10, 10);

 clip.apply(image);

 for (int plane = 0; plane < image.numPlanes(); ++plane) {
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y, plane).floatValue(), 1.5f), 0.5f);
 float actual = image.get(x, y, plane).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }

 @Test
 public void testClipIntIntWithRealColorImage() {
 Clip clip = new Clip(5, 10);
 RealColorImage image = createRealColorImage(10, 10);

 clip.apply(image);

 for (int plane = 0; plane < image.numPlanes(); ++plane) {
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y, plane).floatValue(), 10.0f), 5.0f);
 float actual = image.get(x, y, plane).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }

 @Test
 public void testClipFloatFloatWithRealColorImage() {
 Clip clip = new Clip(0.5f, 1.5f);
 RealColorImage image = createRealColorImage(10, 10);

 clip.apply(image);

 for (int plane = 0; plane < image.numPlanes(); ++plane) {
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.max(Math.min(image.get(x, y, plane).floatValue(), 1.5f), 0.5f);
 float actual = image.get(x, y, plane).floatValue();
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }
}