import org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

 private static final int MIN_INT = Integer.MIN_VALUE;
 private static final int MAX_INT = Integer.MAX_VALUE;
 private static final float MIN_FLOAT = Float.MIN_VALUE;
 private static final float MAX_FLOAT = Float.MAX_VALUE;

 @Test
 public void testClipIntIntGrayImage() {
 GrayImage inputImage = new IdentityImage(new Size(3, 3), new GrayPixelType());
 inputImage.setPixel(0, 0, new GrayPixel(127));
 inputImage.setPixel(0, 1, new GrayPixel(255));
 inputImage.setPixel(1, 0, new GrayPixel(0));
 inputImage.setPixel(1, 1, new GrayPixel(128));
 inputImage.setPixel(2, 2, new GrayPixel(200));

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply(inputImage);

 // Test typical use case
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 GrayPixel expectedPixel = inputImage.get(x, y);
 GrayPixel actualPixel = (GrayPixel)outputImage.get(x, y);
 assertTrue(expectedPixel.value() >= clip.int_min() && expectedPixel.value() <= clip.int_max());
 assertTrue(actualPixel.value() >= clip.int_min() && actualPixel.value() <= clip.int_max());
 assertEquals(expectedPixel, actualPixel);
 }
 }

 // Test edge cases
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 GrayPixel pixel = (GrayPixel)outputImage.get(x, y);
 if (pixel.value() == clip.int_min()) {
 assertEquals(clip.int_min(), pixel.value());
 }
 if (pixel.value() == clip.int_max()) {
 assertEquals(clip.int_max(), pixel.value());
 }
 }
 }

 // Test error scenario - min value is greater than max value
 try {
 Clip clip2 = new Clip(MAX_INT, MIN_INT);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClipFloatFloatRealGrayImage() {
 RealGrayImage inputImage = new IdentityImage(new Size(3, 3), new RealGrayPixelType());
 inputImage.setPixel(0, 0, new RealGrayPixel(127.0f));
 inputImage.setPixel(0, 1, new RealGrayPixel(255.0f));
 inputImage.setPixel(1, 0, new RealGrayPixel(0.0f));
 inputImage.setPixel(1, 1, new RealGrayPixel(128.0f));
 inputImage.setPixel(2, 2, new RealGrayPixel(200.0f));

 Clip clip = new Clip(0.5f, 1.5f);
 Image outputImage = clip.apply(inputImage);

 // Test typical use case
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 RealGrayPixel expectedPixel = inputImage.get(x, y);
 RealGrayPixel actualPixel = (RealGrayPixel)outputImage.get(x, y);
 assertTrue(expectedPixel.value() >= clip.float_min() && expectedPixel.value() <= clip.float_max());
 assertTrue(actualPixel.value() >= clip.float_min() && actualPixel.value() <= clip.float_max());
 assertEquals(expectedPixel, actualPixel);
 }
 }

 // Test edge cases
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 RealGrayPixel pixel = (RealGrayPixel)outputImage.get(x, y);
 if (pixel.value() == clip.float_min()) {
 assertEquals(clip.float_min(), pixel.value(), 0.001);
 }
 if (pixel.value() == clip.float_max()) {
 assertEquals(clip.float_max(), pixel.value(), 0.001);
 }
 }
 }

 // Test error scenario - min value is greater than max value
 try {
 Clip clip2 = new Clip(-1.0f, 0.0f);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClipIntIntColorImage() {
 ColorImage inputImage = new IdentityImage(new Size(3, 3), new RGBPixelType());
 inputImage.setPlane(0, new IdentityImage(new Size(3, 3), new GrayPixelType()));
 inputImage.setPlane(1, new IdentityImage(new Size(3, 3), new GrayPixelType()));
 inputImage.setPlane(2, new IdentityImage(new Size(3, 3), new GrayPixelType()));

 inputImage.getPlane(0).setPixel(0, 0, new GrayPixel(127));
 inputImage.getPlane(0).setPixel(0, 1, new GrayPixel(255));
 inputImage.getPlane(0).setPixel(1, 0, new GrayPixel(0));
 inputImage.getPlane(0).setPixel(1, 1, new GrayPixel(128));
 inputImage.getPlane(0).setPixel(2, 2, new GrayPixel(200));

 inputImage.getPlane(1).setPixel(0, 0, new GrayPixel(127));
 inputImage.getPlane(1).setPixel(0, 1, new GrayPixel(255));
 inputImage.getPlane(1).setPixel(1, 0, new GrayPixel(0));
 inputImage.getPlane(1).setPixel(1, 1, new GrayPixel(128));
 inputImage.getPlane(1).setPixel(2, 2, new GrayPixel(200));

 inputImage.getPlane(2).setPixel(0, 0, new GrayPixel(127));
 inputImage.getPlane(2).setPixel(0, 1, new GrayPixel(255));
 inputImage.getPlane(2).setPixel(1, 0, new GrayPixel(0));
 inputImage.getPlane(2).setPixel(1, 1, new GrayPixel(128));
 inputImage.getPlane(2).setPixel(2, 2, new GrayPixel(200));

 Clip clip = new Clip(50, 150);
 Image outputImage = clip.apply(inputImage);

 // Test typical use case
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 for (int plane = 0; plane < outputImage.numPlanes(); ++plane) {
 GrayImage planeImage = (GrayImage)outputImage.getPlane(plane);
 GrayPixel expectedPixel = inputImage.getPlane(plane).get(x, y);
 GrayPixel actualPixel = (GrayPixel)planeImage.get(x, y);
 assertTrue(expectedPixel.value() >= clip.int_min() && expectedPixel.value() <= clip.int_max());
 assertTrue(actualPixel.value() >= clip.int_min() && actualPixel.value() <= clip.int_max());
 assertEquals(expectedPixel, actualPixel);
 }
 }
 }

 // Test edge cases
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 for (int plane = 0; plane < outputImage.numPlanes(); ++plane) {
 GrayPixel pixel = (GrayPixel)outputImage.getPlane(plane).get(x, y);
 if (pixel.value() == clip.int_min()) {
 assertEquals(clip.int_min(), pixel.value());
 }
 if (pixel.value() == clip.int_max()) {
 assertEquals(clip.int_max(), pixel.value());
 }
 }
 }
 }

 // Test error scenario - min value is greater than max value
 try {
 Clip clip2 = new Clip(MAX_INT, MIN_INT);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testClipFloatFloatRealColorImage() {
 RealColorImage inputImage = new IdentityImage(new Size(3, 3), new RGBAPixelType());
 inputImage.setPlane(0, new IdentityImage(new Size(3, 3), new RealGrayPixelType()));
 inputImage.setPlane(1, new IdentityImage(new Size(3, 3), new RealGrayPixelType()));
 inputImage.setPlane(2, new IdentityImage(new Size(3, 3), new RealGrayPixelType()));

 inputImage.getPlane(0).setPixel(0, 0, new RealGrayPixel(127.0f));
 inputImage.getPlane(0).setPixel(0, 1, new RealGrayPixel(255.0f));
 inputImage.getPlane(0).setPixel(1, 0, new RealGrayPixel(0.0f));
 inputImage.getPlane(0).setPixel(1, 1, new RealGrayPixel(128.0f));
 inputImage.getPlane(0).setPixel(2, 2, new RealGrayPixel(200.0f));

 inputImage.getPlane(1).setPixel(0, 0, new RealGrayPixel(127.0f));
 inputImage.getPlane(1).setPixel(0, 1, new RealGrayPixel(255.0f));
 inputImage.getPlane(1).setPixel(1, 0, new RealGrayPixel(0.0f));
 inputImage.getPlane(1).setPixel(1, 1, new RealGrayPixel(128.0f));
 inputImage.getPlane(1).setPixel(2, 2, new RealGrayPixel(200.0f));

 inputImage.getPlane(2).setPixel(0, 0, new RealGrayPixel(127.0f));
 inputImage.getPlane(2).setPixel(0, 1, new RealGrayPixel(255.0f));
 inputImage.getPlane(2).setPixel(1, 0, new RealGrayPixel(0.0f));
 inputImage.getPlane(2).setPixel(1, 1, new RealGrayPixel(128.0f));
 inputImage.getPlane(2).setPixel(2, 2, new RealGrayPixel(200.0f));

 Clip clip = new Clip(0.5f, 1.5f);
 Image outputImage = clip.apply(inputImage);

 // Test typical use case
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 for (int plane = 0; plane < outputImage.numPlanes(); ++plane) {
 RealGrayPixel expectedPixel = inputImage.getPlane(plane).get(x, y);
 RealGrayPixel actualPixel = (RealGrayPixel)outputImage.getPlane(plane).get(x, y);
 assertTrue(expectedPixel.value() >= clip.float_min() && expectedPixel.value() <= clip.float_max());
 assertTrue(actualPixel.value() >= clip.float_min() && actualPixel.value() <= clip.float_max());
 assertEquals(expectedPixel, actualPixel);
 }
 }
 }

 // Test edge cases
 for (int y = 0; y < outputImage.Y(); ++y) {
 for (int x = 0; x < outputImage.X(); ++x) {
 for (int plane = 0; plane < outputImage.numPlanes(); ++plane) {
 RealGrayPixel pixel = (RealGrayPixel)outputImage.getPlane(plane).get(x, y);
 if (pixel.value() == clip.float_min()) {
 assertEquals(clip.float_min(), pixel.value(), 0.001);
 }
 if (pixel.value() == clip.float_max()) {
 assertEquals(clip.float_max(), pixel.value(), 0.001);
 }
 }
 }
 }

 // Test error scenario - min value is greater than max value
 try {
 Clip clip2 = new Clip(-1.0f, 0.0f);
 fail("Exception expected");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMain() {
 try {
 Image image = null;
 String inputfile = "test_image.pgm";
 Image image2 = null;
 ImageInputStream is = new ImageInputStream(new File(inputfile));
 image = is.read();
 is.close();

 Float f_val1 = Float.valueOf(0.5f);
 Float f_val2 = Float.valueOf(1.5f);
 float val1 = f_val1;
 float val2 = f_val2;

 Clip clip = new Clip(val1, val2);
 image2 = clip.apply(image);

 ImageOutputStream os = new ImageOutputStream(new File("output_image.pgm"));
 os.write(image2);
 os.close();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Exception not expected");
 }
 }
}