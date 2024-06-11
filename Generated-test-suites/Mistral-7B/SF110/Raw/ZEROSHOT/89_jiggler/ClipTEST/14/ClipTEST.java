import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.ImageOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.ops.SimpleOperator;
import jigl.image.ops.ROI;

import static org.junit.Assert.*;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private RealGrayImage imageReal;
 private ColorImage colorImage;
 private RealColorImage realColorImage;

 @Before
 public void setUp() {
 image = new GrayImage(10, 10, new int[10 * 10]);
 imageReal = new RealGrayImage(10, 10, new float[10 * 10]);
 colorImage = new ColorImage(10, 10, 3, new int[3 * 10 * 10]);
 realColorImage = new RealColorImage(10, 10, 3, new float[3 * 10 * 10]);
 clip = new Clip(0, 5);
 }

 @After
 public void tearDown() {
 // Release resources
 image = null;
 imageReal = null;
 colorImage = null;
 realColorImage = null;
 clip = null;
 }

 @Test
 public void testClipGrayImageIntInt() {
 ImageOperator output = clip.apply(image);
 assertSame(image, output.image());
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 int expected = Math.min(Math.max(image.get(x, y), clip.int_min), clip.int_max);
 int actual = ((GrayImage)output.image()).get(x, y);
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testClipGrayImageFloatFloat() {
 clip = new Clip(2.5f, 7.5f);
 ImageOperator output = clip.apply(image);
 assertSame(image, output.image());
 for (int y = 0; y < image.Y(); ++y) {
 for (int x = 0; x < image.X(); ++x) {
 float expected = Math.min(Math.max(image.get(x, y) / 255.0f, clip.float_min), clip.float_max);
 float actual = ((GrayImage)output.image()).get(x, y) / 255.0f;
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testClipRealGrayImageFloatFloat() {
 clip = new Clip(2.5f, 7.5f);
 ImageOperator output = clip.apply(imageReal);
 assertSame(imageReal, output.image());
 for (int y = 0; y < imageReal.Y(); ++y) {
 for (int x = 0; x < imageReal.X(); ++x) {
 float expected = Math.min(Math.max(imageReal.get(x, y), clip.float_min), clip.float_max);
 float actual = ((RealGrayImage)output.image()).get(x, y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }

 @Test
 public void testClipColorImageIntInt() {
 clip = new Clip(0, 5);
 ImageOperator output = clip.apply(colorImage);
 assertSame(colorImage, output.image());
 for (int y = 0; y < colorImage.Y(); ++y) {
 for (int x = 0; x < colorImage.X(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int expected = Math.min(Math.max(colorImage.get(x, y, plane), clip.int_min), clip.int_max);
 int actual = ((GrayImage)output.image().plane(plane)).get(x, y);
 assertEquals(expected, actual);
 }
 }
 }
 }

 @Test
 public void testClipColorImageFloatFloat() {
 clip = new Clip(0.5f, 0.75f);
 ImageOperator output = clip.apply(colorImage);
 assertSame(colorImage, output.image());
 for (int y = 0; y < colorImage.Y(); ++y) {
 for (int x = 0; x < colorImage.X(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float expected = Math.min(Math.max(colorImage.get(x, y, plane) / 255.0f, clip.float_min), clip.float_max);
 float actual = ((RealGrayImage)output.image().plane(plane)).get(x, y) / 255.0f;
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }

 @Test
 public void testClipRealColorImageFloatFloat() {
 clip = new Clip(0.5f, 0.75f);
 ImageOperator output = clip.apply(realColorImage);
 assertSame(realColorImage, output.image());
 for (int y = 0; y < realColorImage.Y(); ++y) {
 for (int x = 0; x < realColorImage.X(); ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float expected = Math.min(Math.max(realColorImage.get(x, y, plane), clip.float_min), clip.float_max);
 float actual = ((RealGrayImage)output.image().plane(plane)).get(x, y);
 assertEquals(expected, actual, 0.001f);
 }
 }
 }
 }
}