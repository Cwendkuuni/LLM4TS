import org.junit.Test;
import org.junit.Assert.*;
import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

 private GrayImage createGrayImage(int width, int height) {
 return new GrayImage(width, height, new BytePixelType());
 }

 private RealGrayImage createRealGrayImage(int width, int height) {
 return new RealGrayImage(width, height, new FloatPixelType());
 }

 private ColorImage createColorImage(int width, int height) {
 return new ColorImage(width, height, new RGBPixelType());
 }

 private RealColorImage createRealColorImage(int width, int height) {
 return new RealColorImage(width, height, new RGBFPixelType());
 }

 @Test
 public void testClipIntIntConstructor() {
 Clip clip = new Clip(1, 5);
 Assert.assertNotNull(clip);
 }

 @Test
 public void testClipIntIntConstructorWithImage() {
 Clip clip = new Clip(new GrayImage(5, 5, new BytePixelType()), 0, 3);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 value = ((value > 3) ? 3 : value);
 expectedImage.set(x, y, Integer.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(new GrayImage(5, 5, new BytePixelType()), 0, 3);
 Assert.assertEquals(expectedImage, appliedClip.apply(new GrayImage(5, 5, new BytePixelType())));
 }

 @Test
 public void testClipFloatFloatConstructor() {
 Clip clip = new Clip(1.5f, 4.5f);
 Assert.assertNotNull(clip);
 }

 @Test
 public void testClipFloatFloatConstructorWithImage() {
 Clip clip = new Clip(new RealGrayImage(5, 5, new FloatPixelType()), 1.0f, 3.5f);
 RealGrayImage expectedImage = createRealGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = expectedImage.get(x, y);
 value = ((value > 3.5f) ? 3.5f : value);
 expectedImage.set(x, y, Float.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(new RealGrayImage(5, 5, new FloatPixelType()), 1.0f, 3.5f);
 Assert.assertEquals(expectedImage, appliedClip.apply(new RealGrayImage(5, 5, new FloatPixelType())));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMinValue() {
 Clip clip = new Clip(-1, 5);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testClipWithInvalidMaxValue() {
 Clip clip = new Clip(1, -5);
 }

 @Test
 public void testClipWithMinEqualMax() {
 Clip clip = new Clip(5, 5);
 GrayImage inputImage = createGrayImage(5, 5);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 value = value; // no need to change value, since min and max are equal
 expectedImage.set(x, y, Integer.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(inputImage);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithNegativeMinValueAndPositiveMaxValue() {
 Clip clip = new Clip(-3, 5);
 GrayImage inputImage = createGrayImage(5, 5);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 value = ((value > 2) ? 2 : value); // adjust the min value
 value = ((value < -3) ? -3 : value); // adjust the max value
 expectedImage.set(x, y, Integer.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(inputImage);
 appliedClip.setMin(-3);
 appliedClip.setMax(5);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithPositiveMinValueAndNegativeMaxValue() {
 Clip clip = new Clip(5, -3);
 GrayImage inputImage = createGrayImage(5, 5);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 value = ((value > -3) ? -3 : value); // adjust the min value
 value = ((value < 5) ? 5 : value); // adjust the max value
 expectedImage.set(x, y, Integer.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(inputImage);
 appliedClip.setMin(5);
 appliedClip.setMax(-3);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithMinGreaterThanMax() {
 Clip clip = new Clip(5, 1);
 GrayImage inputImage = createGrayImage(5, 5);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 expectedImage.set(x, y, Integer.valueOf(value)); // no need to change value
 }
 }
 Clip appliedClip = new Clip(inputImage);
 appliedClip.setMin(5);
 appliedClip.setMax(1);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithApplyMethodGrayImage() {
 GrayImage inputImage = createGrayImage(5, 5);
 Clip clip = new Clip(inputImage);
 GrayImage expectedImage = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = expectedImage.get(x, y);
 value = ((value > 3) ? 3 : value);
 expectedImage.set(x, y, Integer.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(inputImage);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithApplyMethodRealGrayImage() {
 RealGrayImage inputImage = createRealGrayImage(5, 5);
 Clip clip = new Clip(inputImage);
 RealGrayImage expectedImage = createRealGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = expectedImage.get(x, y);
 value = ((value > 3.5f) ? 3.5f : value);
 expectedImage.set(x, y, Float.valueOf(value));
 }
 }
 Clip appliedClip = new Clip(inputImage);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithApplyMethodColorImage() {
 ColorImage inputImage = createColorImage(5, 5);
 Clip clip = new Clip(inputImage);
 ColorImage expectedImage = createColorImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 int value = expectedImage.getPlane(plane).get(x, y);
 value = ((value > 3) ? 3 : value);
 expectedImage.getPlane(plane).set(x, y, Integer.valueOf(value));
 }
 }
 }
 Clip appliedClip = new Clip(inputImage);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }

 @Test
 public void testClipWithApplyMethodRealColorImage() {
 RealColorImage inputImage = createRealColorImage(5, 5);
 Clip clip = new Clip(inputImage);
 RealColorImage expectedImage = createRealColorImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 for (int plane = 0; plane < 3; ++plane) {
 float value = expectedImage.getPlane(plane).get(x, y);
 value = ((value > 3.5f) ? 3.5f : value);
 expectedImage.getPlane(plane).set(x, y, Float.valueOf(value));
 }
 }
 }
 Clip appliedClip = new Clip(inputImage);
 Assert.assertEquals(expectedImage, appliedClip.apply(inputImage));
 }
}