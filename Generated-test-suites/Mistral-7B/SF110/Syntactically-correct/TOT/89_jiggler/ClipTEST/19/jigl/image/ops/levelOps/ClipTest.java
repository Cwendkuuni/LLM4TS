package jigl.image.ops.levelOps;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.ROI;
import jigl.image.GrayImage;
import jigl.image.RealGrayImage;
import jigl.image.ColorImage;
import jigl.image.RealColorImage;
import jigl.image.types.ImageType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ClipTest {

 @Test
 public void testGrayImageWithIntMinIntMax() {
 GrayImage inputImage = new GrayImage(10, 10, ImageType.UINT8);
 for (int i = 0; i < 10 * 10; i++) {
 inputImage.set(i % 10, i / 10, (byte) (i % 15));
 }
 Clip clip = new Clip(1, 5);
 GrayImage outputImage = (GrayImage) clip.apply(inputImage);
 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 int expectedValue = Math.min(Math.max(x, y), 5);
 int actualValue = (int) outputImage.get(x, y);
 assertEquals("Unexpected pixel value at [" + x + ", " + y + "]", expectedValue, actualValue);
 }
 }
 }

 @Test
 public void testGrayImageWithFloatMinFloatMax() {
 GrayImage inputImage = new GrayImage(10, 10, ImageType.FLOAT32);
 for (int i = 0; i < 10 * 10; i++) {
 inputImage.set(i % 10, i / 10, i % 15 + 5.5f);
 }
 Clip clip = new Clip(1.5f, 5.5f);
 GrayImage outputImage = (GrayImage) clip.apply(inputImage);
 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 float expectedValue = Math.min(Math.max(x, y) + 1.5f, 6.5f);
 float actualValue = (float) outputImage.get(x, y);
 assertEquals("Unexpected pixel value at [" + x + ", " + y + "]", expectedValue, actualValue, 0.01f);
 }
 }
 }

 @Test
 public void testColorImageWithIntMinIntMax() {
 ColorImage inputImage = new ColorImage(10, 10, 3, ImageType.UINT8);
 for (int i = 0; i < 10 * 10 * 3; i++) {
 inputImage.set(i % 10, i / 10, i % 3, (byte) (i % 15));
 }
 Clip clip = new Clip(1, 5);
 clip.apply(inputImage);
 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 for (int plane = 0; plane < 3; plane++) {
 int expectedValue = Math.min(Math.max(x, y), 5);
 int actualValue = (int) inputImage.get(x, y, plane);
 assertEquals("Unexpected pixel value at [" + x + ", " + y + ", plane " + plane + "]", expectedValue, actualValue);
 }
 }
 }
 }

 @Test
 public void testColorImageWithFloatMinFloatMax() {
 ColorImage inputImage = new ColorImage(10, 10, 3, ImageType.FLOAT32);
 for (int i = 0; i < 10 * 10 * 3; i++) {
 inputImage.set(i % 10, i / 10, i % 3, i % 15 + 5.5f);
 }
 Clip clip = new Clip(1.5f, 5.5f);
 clip.apply(inputImage);
 for (int y = 0; y < 10; y++) {
 for (int x = 0; x < 10; x++) {
 for (int plane = 0; plane < 3; plane++) {
 float expectedValue = Math.min(Math.max(x, y) + 1.5f, 6.5f);
 float actualValue = (float) inputImage.get(x, y, plane);
 assertEquals("Unexpected pixel value at [" + x + ", " + y + ", plane " + plane + "]", expectedValue, actualValue, 0.01f);
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNullImage() {
 Clip clip = new Clip(1, 5);
 assertNull("Should not be able to apply clip to null image", clip.apply(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNullROI() {
 GrayImage inputImage = new GrayImage(10, 10, ImageType.UINT8);
 Clip clip = new Clip(1, 5);
 assertNull("Should not be able to apply clip to null roi", clip.apply(inputImage, null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidIntMin() {
 try {
 new Clip(-1, 5);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidIntMax() {
 try {
 new Clip(0, -1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidFloatMin() {
 try {
 new Clip(-1.5f, 5.5f);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidFloatMax() {
 try {
 new Clip(1.5f, -5.5f);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMain() {
 try {
 Image inputImage = null;
 String inputFile = "test_image.bmp";
 ImageInputStream is = new ImageInputStream(inputFile);
 inputImage = is.read();
 is.close();
 float f_val1 = 1.5f;
 float f_val2 = 5.5f;
 Clip clip = new Clip(f_val1, f_val2);
 Image outputImage = clip.apply(inputImage);
 ImageOutputStream os = new ImageOutputStream("output.bmp");
 os.write(outputImage);
 os.close();
 } catch (Exception e) {
 e.printStackTrace();
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}