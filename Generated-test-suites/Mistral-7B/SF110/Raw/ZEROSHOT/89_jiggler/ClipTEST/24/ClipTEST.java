package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Before;
import org.junit.Test;

public class ClipTest {

 private GrayImage image;
 private GrayImage expectedImage;
 private ROI roi;
 private Clip clip;

 @Before
 public void setUp() {
 image = new GrayImage(new int[5][5], 5, 5);
 expectedImage = new GrayImage(new int[5][5], 5, 5);
 roi = new ROI(2, 2, 3, 3);
 clip = new Clip(2, 5);
 }

 @Test
 public void testApplyGrayImage() {
 GrayImage result = clip.apply(image);
 assertArrayEquals(expectedImage.getPixelBuffer(), result.getPixelBuffer());
 }

 @Test
 public void testApplyGrayImageWithROI() {
 GrayImage result = clip.apply(image, roi);
 assertArrayEquals(expectedImage.getSubImage(roi.ux(), roi.uy(), roi.lx() - roi.ux() + 1, roi.ly() - roi.uy() + 1).getPixelBuffer(), result.getPixelBuffer());
 }

 @Test
 public void testApplyRealGrayImage() {
 RealGrayImage result = (RealGrayImage) clip.apply(new RealGrayImage(new float[5][5], 5, 5), new ROI(2, 2, 3, 3));
 assertArrayEquals(expectedImage.getPixelBuffer(), result.getPixelBuffer(), 0.001f);
 }

 @Test
 public void testApplyColorImage() {
 ColorImage colorImage = new ColorImage(new GrayImage(new int[5][5], 5, 5), new GrayImage(new int[5][5], 5, 5), new GrayImage(new int[5][5], 5, 5));
 ColorImage result = clip.apply(colorImage);
 for (int i = 0; i < colorImage.X() * colorImage.Y(); ++i) {
 if (i >= roi.ux() && i <= roi.lx() && i >= roi.uy() && i <= roi.ly()) {
 int expectedPixel = expectedImage.get(i % image.X(), i / image.X());
 int actualPixel = result.getPixel(i);
 assertArrayEquals(new int[] { expectedPixel, expectedPixel, expectedPixel }, actualPixel.getRGB());
 } else {
 assertFalse(result.getPixel(i).equals(colorImage.getPixel(i)));
 }
 }
 }

 @Test
 public void testApplyColorImageWithROI() {
 ColorImage colorImage = new ColorImage(new GrayImage(new int[5][5], 5, 5), new GrayImage(new int[5][5], 5, 5), new GrayImage(new int[5][5], 5, 5));
 ColorImage result = clip.apply(colorImage, roi);
 for (int i = 0; i < colorImage.X() * colorImage.Y(); ++i) {
 if (i >= roi.ux() && i <= roi.lx() && i >= roi.uy() && i <= roi.ly()) {
 int expectedPixel = expectedImage.get(i % image.X(), i / image.X());
 int actualPixel = result.getPixel(i).getRGB();
 assertArrayEquals(new int[] { expectedPixel, expectedPixel, expectedPixel }, actualPixel);
 } else {
 assertFalse(result.getPixel(i).equals(colorImage.getPixel(i)));
 }
 }
 }

 @Test
 public void testApplyRealColorImage() {
 RealColorImage result = (RealColorImage) clip.apply(new RealColorImage(new float[5][5][3], 5, 5, 3), new ROI(2, 2, 3, 3));
 for (int x = 0; x < 3; ++x) {
 for (int y = 0; y < 3; ++y) {
 float expectedPixel[] = expectedImage.getPixel(roi.ux() + x, roi.uy() + y);
 float actualPixel[] = result.getPixel(x, y);
 assertArrayEquals(expectedPixel, actualPixel, 0.001f);
 }
 }
 }

 @Test
 public void testApplyRealColorImageWithROI() {
 RealColorImage result = (RealColorImage) clip.apply(new RealColorImage(new float[5][5][3], 5, 5, 3), roi);
 for (int x = 0; x < roi.lx() - roi.ux() + 1; ++x) {
 for (int y = 0; y < roi.ly() - roi.uy() + 1; ++y) {
 float expectedPixel[] = expectedImage.getPixel(roi.ux() + x, roi.uy() + y);
 float actualPixel[] = result.getPixel(x, y);
 assertArrayEquals(expectedPixel, actualPixel, 0.001f);
 }
 }
 }
}