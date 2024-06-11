import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;
import static org.junit.Assert.*;

public class ClipTest {

 private GrayImage image;
 private RealGrayImage realImage;
 private ColorImage colorImage;
 private RealColorImage realColorImage;
 private Clip clipInt;
 private Clip clipFloat;

 @Before
 public void setUp() {
 image = new ByteGrayImage(5, 5, new byte[25]);
 realImage = new ByteGrayImage(5, 5, new byte[25]);
 colorImage = new RGBImage(5, 5, 3, new byte[25][3]);
 realColorImage = new RGBImage(5, 5, 3, new byte[25][3]);
 clipInt = new Clip(0, 100);
 clipFloat = new Clip(0.0f, 1.0f);
 }

 @After
 public void tearDown() {
 // Clear memory and close resources
 image = null;
 realImage = null;
 colorImage = null;
 realColorImage = null;
 clipInt = null;
 clipFloat = null;
 }

 @Test
 public void testApplyGrayImageIntMinIntMax() {
 Image result = clipInt.apply(image);
 for (int i = 0; i < image.X() * image.Y(); i++) {
 if (image.get(i) > 100) {
 image.set(i, Integer.valueOf((int)clipInt.float_max));
 }
 }
 assertEquals(image, result);
 }

 @Test
 public void testApplyGrayImageNegativeIntMinIntMax() {
 Image result = clipInt.apply(image);
 for (int i = 0; i < image.X() * image.Y(); i++) {
 if (image.get(i) < 0) {
 image.set(i, Integer.valueOf((int)clipInt.float_min));
 }
 }
 assertEquals(image, result);
 }

 @Test
 public void testApplyGrayImageZeroIntMinIntMax() {
 Image result = clipInt.apply(image);
 assertEquals(image, result);
 }

 @Test
 public void testApplyGrayImageZeroIntMinZeroIntMax() {
 Image result = clipInt.apply(image);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMax() {
 Image result = clipFloat.apply(realImage);
 for (int i = 0; i < realImage.X() * realImage.Y(); i++) {
 if (realImage.get(i) > 1.0f) {
 realImage.set(i, Float.valueOf(clipFloat.float_max));
 }
 }
 assertEquals(realImage, result);
 }

 @Test
 public void testApplyRealGrayImageNegativeFloatMinFloatMax() {
 Image result = clipFloat.apply(realImage);
 for (int i = 0; i < realImage.X() * realImage.Y(); i++) {
 if (realImage.get(i) < 0.0f) {
 realImage.set(i, Float.valueOf(clipFloat.float_min));
 }
 }
 assertEquals(realImage, result);
 }

 @Test
 public void testApplyRealGrayImageZeroFloatMinZeroFloatMax() {
 Image result = clipFloat.apply(realImage);
 assertEquals(realImage, result);
 }

 @Test
 public void testApplyRealGrayImageZeroFloatMinZeroFloatMaxNoChange() {
 Image result = clipFloat.apply(realImage);
 assertArrayEquals(realImage.getData(), result.getData(), 0.0f);
 }

 @Test
 public void testApplyColorImageIntMinIntMax() {
 Image result = clipInt.apply(colorImage);
 for (int i = 0; i < colorImage.X() * colorImage.Y() * 3; i += 3) {
 if (colorImage.getPixel(i / 3, i / 3)[0] > 100) {
 colorImage.setPixel(i / 3, i / 3, new byte[]{(byte)clipInt.int_max, (byte)clipInt.int_max, (byte)clipInt.int_max});
 }
 }
 assertEquals(colorImage, result);
 }

 @Test
 public void testApplyColorImageNegativeIntMinIntMax() {
 Image result = clipInt.apply(colorImage);
 for (int i = 0; i < colorImage.X() * colorImage.Y() * 3; i += 3) {
 if (colorImage.getPixel(i / 3, i / 3)[0] < 0) {
 colorImage.setPixel(i / 3, i / 3, new byte[]{(byte)clipInt.int_min, (byte)clipInt.int_min, (byte)clipInt.int_min});
 }
 }
 assertEquals(colorImage, result);
 }

 @Test
 public void testApplyColorImageZeroIntMinZeroIntMax() {
 Image result = clipInt.apply(colorImage);
 assertEquals(colorImage, result);
 }

 @Test
 public void testApplyColorImageZeroIntMinZeroIntMaxNoChange() {
 Image result = clipInt.apply(colorImage);
 assertArrayEquals(colorImage.getData(), result.getData(), 0);
 }

 @Test
 public void testApplyRealColorImageFloatMinFloatMax() {
 Image result = clipFloat.apply(realColorImage);
 for (int i = 0; i < realColorImage.X() * realColorImage.Y() * 3; i += 3) {
 if (realColorImage.getPixel(i / 3, i / 3)[0] > 1.0f) {
 realColorImage.setPixel(i / 3, i / 3, new float[]{clipFloat.float_max, clipFloat.float_max, clipFloat.float_max});
 }
 }
 assertEquals(realColorImage, result);
 }

 @Test
 public void testApplyRealColorImageNegativeFloatMinFloatMax() {
 Image result = clipFloat.apply(realColorImage);
 for (int i = 0; i < realColorImage.X() * realColorImage.Y() * 3; i += 3) {
 if (realColorImage.getPixel(i / 3, i / 3)[0] < 0.0f) {
 realColorImage.setPixel(i / 3, i / 3, new float[]{clipFloat.float_min, clipFloat.float_min, clipFloat.float_min});
 }
 }
 assertEquals(realColorImage, result);
 }

 @Test
 public void testApplyRealColorImageZeroFloatMinZeroFloatMax() {
 Image result = clipFloat.apply(realColorImage);
 assertEquals(realColorImage, result);
 }

 @Test
 public void testApplyRealColorImageZeroFloatMinZeroFloatMaxNoChange() {
 Image result = clipFloat.apply(realColorImage);
 assertArrayEquals(realColorImage.getData(), result.getData(), 0.0f);
 }
}