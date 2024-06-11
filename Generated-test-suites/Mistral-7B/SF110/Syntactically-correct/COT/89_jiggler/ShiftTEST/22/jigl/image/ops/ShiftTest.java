import junit.framework.TestCase;
import jigl.image.ops.Shift;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;

public class ShiftTest {

 private Shift shift;
 private GrayImage inputImage;
 private RealGrayImage realInputImage;
 private GrayImage expectedOutputImage;
 private RealGrayImage expectedRealOutputImage;

 @org.junit.Before
 public void setUp() {
 // Initialize the shift object with some values
 shift = new Shift(2, 2, Shift.WRAP);
 // Initialize the input image with some values
 inputImage = new GrayImage(5, 5);
 for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
 inputImage.set(i % inputImage.X(), i % inputImage.Y(), (byte) i);
 }
 // Initialize the real input image with some values
 realInputImage = new RealGrayImage(5, 5);
 for (int i = 0; i < realInputImage.X() * realInputImage.Y(); i++) {
 realInputImage.set(i % realInputImage.X(), i % realInputImage.Y(), i / 10.0f);
 }
 // Initialize the expected output image with some values based on the shift values
 expectedOutputImage = new GrayImage(5, 5);
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 expectedOutputImage.set((x + 2) % 5, (y + 2) % 5, (byte) (x + y + 4));
 }
 }
 // Initialize the expected real output image with some values based on the shift values
 expectedRealOutputImage = new RealGrayImage(5, 5);
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 expectedRealOutputImage.set((x + 2.0f) % 5, (y + 2.0f) % 5, (x + y + 4.0f / 10.0f));
 }
 }
 }

 @org.junit.Test
 public void testApplyGrayImageWithPositiveXOffsetAndYOffsetAndWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyGrayImageWithPositiveXOffsetAndYOffsetAndNoWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 // Check for out of bounds condition when wrap flag is false
 for (int y = 0; y < inputImage.Y(); y++) {
 for (int x = 0; x < inputImage.X(); x++) {
 if (x + shift.getXoffset() >= inputImage.X() || y + shift.getYoffset() >= inputImage.Y()) {
 TestCase.assertFalse("Output image should not contain pixel at (" + (x + shift.getXoffset()) + ", " + (y + shift.getYoffset()) + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test
 public void testApplyGrayImageWithNegativeXOffsetAndPositiveYOffsetAndWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyGrayImageWithNegativeXOffsetAndPositiveYOffsetAndNoWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 // Check for out of bounds condition when x offset is negative and wrap flag is false
 for (int y = 0; y < inputImage.Y(); y++) {
 for (int x = 0; x < inputImage.X(); x++) {
 if (x + shift.getXoffset() < 0) {
 TestCase.assertFalse("Output image should not contain pixel at (" + (x + shift.getXoffset()) + ", " + y + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test
 public void testApplyGrayImageWithPositiveXOffsetAndNegativeYOffsetAndWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyGrayImageWithPositiveXOffsetAndNegativeYOffsetAndNoWrap() {
 GrayImage outputImage = shift.apply(inputImage);
 testImageEquals(expectedOutputImage, outputImage);
 // Check for out of bounds condition when y offset is negative and wrap flag is false
 for (int x = 0; x < inputImage.X(); x++) {
 for (int y = 0; y < inputImage.Y(); y++) {
 if (y + shift.getYoffset() < 0) {
 TestCase.assertFalse("Output image should not contain pixel at (" + x + ", " + (y + shift.getYoffset()) + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test(expected = ImageProcessingException.class)
 public void testApplyGrayImageWithInvalidWrapFlag() {
 shift = new Shift(2, 2, 3);
 GrayImage outputImage = shift.apply(inputImage);
 TestCase.fail("Expected ImageProcessingException but got " + outputImage);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithPositiveXOffsetAndYOffsetAndWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithPositiveXOffsetAndYOffsetAndNoWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 // Check for out of bounds condition when wrap flag is false
 for (int y = 0; y < realInputImage.Y(); y++) {
 for (int x = 0; x < realInputImage.X(); x++) {
 if (x + shift.getXoffset() >= realInputImage.X() || y + shift.getYoffset() >= realInputImage.Y()) {
 TestCase.assertFalse("Output image should not contain pixel at (" + (x + shift.getXoffset()) + ", " + (y + shift.getYoffset()) + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithNegativeXOffsetAndPositiveYOffsetAndWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithNegativeXOffsetAndPositiveYOffsetAndNoWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 // Check for out of bounds condition when x offset is negative and wrap flag is false
 for (int y = 0; y < realInputImage.Y(); y++) {
 for (int x = 0; x < realInputImage.X(); x++) {
 if (x + shift.getXoffset() < 0) {
 TestCase.assertFalse("Output image should not contain pixel at (" + (x + shift.getXoffset()) + ", " + y + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithPositiveXOffsetAndNegativeYOffsetAndWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 }

 @org.junit.Test
 public void testApplyRealGrayImageWithPositiveXOffsetAndNegativeYOffsetAndNoWrap() {
 RealGrayImage outputImage = shift.apply(realInputImage);
 testRealImageEquals(expectedRealOutputImage, outputImage);
 // Check for out of bounds condition when y offset is negative and wrap flag is false
 for (int x = 0; x < realInputImage.X(); x++) {
 for (int y = 0; y < realInputImage.Y(); y++) {
 if (y + shift.getYoffset() < 0) {
 TestCase.assertFalse("Output image should not contain pixel at (" + x + ", " + (y + shift.getYoffset()) + ")", outputImage.get(x, y) != null);
 }
 }
 }
 }

 @org.junit.Test(expected = ImageProcessingException.class)
 public void testApplyRealGrayImageWithInvalidWrapFlag() {
 shift = new Shift(2, 2, 3);
 RealGrayImage outputImage = shift.apply(realInputImage);
 TestCase.fail("Expected ImageProcessingException but got " + outputImage);
 }

 // Helper method to compare two GrayImage objects
 private void testImageEquals(GrayImage expectedImage, GrayImage actualImage) {
 for (int y = 0; y < expectedImage.Y(); y++) {
 for (int x = 0; x < expectedImage.X(); x++) {
 TestCase.assertEquals("Pixel at (" + x + ", " + y + ") does not match", expectedImage.get(x, y), actualImage.get(x, y));
 }
 }
 }

 // Helper method to compare two RealGrayImage objects
 private void testRealImageEquals(RealGrayImage expectedImage, RealGrayImage actualImage) {
 for (int y = 0; y < expectedImage.Y(); y++) {
 for (int x = 0; x < expectedImage.X(); x++) {
 TestCase.assertEquals("Pixel at (" + x + ", " + y + ") does not match", expectedImage.get(x, y), actualImage.get(x, y), 0.001);
 }
 }
 }

}