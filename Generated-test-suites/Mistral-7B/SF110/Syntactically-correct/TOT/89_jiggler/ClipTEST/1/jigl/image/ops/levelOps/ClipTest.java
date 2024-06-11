import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.ImageOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.ops.ROI;

public class ClipTest extends TestCase {

 // Test for constructor with int min and max values
 public void testClipIntConstructor() {
 ImageOperator clip = new Clip(5, 10);
 // Test with valid input image
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, 127);
 inputImage.set(0, 1, 150);
 inputImage.set(0, 2, 110);
 inputImage.set(0, 3, 135);
 inputImage.set(0, 4, 170);

 GrayImage expectedOutputImage = new GrayImage(5, 5);
 expectedOutputImage.set(0, 0, 127);
 expectedOutputImage.set(0, 1, 127); // min value
 expectedOutputImage.set(0, 2, 110);
 expectedOutputImage.set(0, 3, 135);
 expectedOutputImage.set(0, 4, 170); // max value

 GrayImage outputImage = (GrayImage) clip.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);

 // Test with invalid input image (null)
 claimFalse("Expected an IllegalArgumentException when input image was null", () -> {
 clip.apply(null);
 return true;
 });
 }

 // Test for constructor with float min and max values
 public void testClipFloatConstructor() {
 ImageOperator clip = new Clip(2.5f, 7.5f);
 // Test with valid input image
 RealGrayImage inputImage = new RealGrayImage(5, 5, 0.0f);
 inputImage.set(0, 0, 1.2f);
 inputImage.set(0, 1, 3.5f);
 inputImage.set(0, 2, 0.8f);
 inputImage.set(0, 3, 5.1f);
 inputImage.set(0, 4, 9.3f);

 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5, 0.0f);
 expectedOutputImage.set(0, 0, 2.5f); // min value
 expectedOutputImage.set(0, 1, 3.5f);
 expectedOutputImage.set(0, 2, 0.8f);
 expectedOutputImage.set(0, 3, 5.1f); // max value
 expectedOutputImage.set(0, 4, 7.5f);

 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);

 // Test with invalid input image (null)
 claimFalse("Expected an IllegalArgumentException when input image was null", () -> {
 clip.apply(null);
 return true;
 });
 }

 // Test apply method with int min and max values for GrayImage
 public void testClipApplyIntMinMaxGrayImage() {
 ImageOperator clip = new Clip(5, 10);
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.set(0, 0, 127);
 inputImage.set(0, 1, 150);
 inputImage.set(0, 2, 110);
 inputImage.set(0, 3, 135);
 inputImage.set(0, 4, 170);

 GrayImage expectedOutputImage = new GrayImage(5, 5);
 expectedOutputImage.set(0, 0, 127);
 expectedOutputImage.set(0, 1, 127); // min value
 expectedOutputImage.set(0, 2, 110);
 expectedOutputImage.set(0, 3, 135);
 expectedOutputImage.set(0, 4, 170); // max value

 GrayImage outputImage = (GrayImage) clip.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method with float min and max values for RealGrayImage
 public void testClipApplyFloatMinMaxRealGrayImage() {
 ImageOperator clip = new Clip(2.5f, 7.5f);
 RealGrayImage inputImage = new RealGrayImage(5, 5, 0.0f);
 inputImage.set(0, 0, 1.2f);
 inputImage.set(0, 1, 3.5f);
 inputImage.set(0, 2, 0.8f);
 inputImage.set(0, 3, 5.1f);
 inputImage.set(0, 4, 9.3f);

 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5, 0.0f);
 expectedOutputImage.set(0, 0, 2.5f); // min value
 expectedOutputImage.set(0, 1, 3.5f);
 expectedOutputImage.set(0, 2, 0.8f);
 expectedOutputImage.set(0, 3, 5.1f); // max value
 expectedOutputImage.set(0, 4, 7.5f);

 RealGrayImage outputImage = (RealGrayImage) clip.apply(inputImage);
 assertEquals(expectedOutputImage, outputImage);
 }

 // Test apply method with invalid input image (null)
 public void testClipApplyInvalidInputImage() {
 ImageOperator clip = new Clip(5, 10);

 // Test with null input image
 claimFalse("Expected an IllegalArgumentException when input image was null", () -> {
 clip.apply(null);
 return true;
 });
 }

 // Test apply method with invalid ROI
 public void testClipApplyInvalidROI() {
 ImageOperator clip = new Clip(5, 10);
 GrayImage inputImage = new GrayImage(5, 5);
 inputImage.setPlanes(new GrayImage[3]);
 ROI roi = new ROI(-1, -1, 5, 5);

 // Test with invalid ROI (negative width or height)
 claimFalse("Expected an IllegalArgumentException when ROI width or height was negative", () -> {
 clip.apply(inputImage, roi);
 return true;
 });
 }

 // Test main method
 public void testClipMainMethod() {
 String inputFile = "testImage.pgm";
 String outputFile = "outputImage.pgm";
 String tempFile = "tempImage.pgm";

 try {
 ImageOperator clip = new Clip(1.5f, 4.5f);
 Image image = null;
 Image image2 = null;
 ImageInputStream is = new ImageInputStream(inputFile);
 image = is.read();
 is.close();

 Float f_val1 = Float.valueOf(1.5f);
 Float f_val2 = Float.valueOf(4.5f);
 Float val1 = f_val1;
 Float val2 = f_val2;
 Clip clipObj = new Clip(val1, val2);
 image2 = clipObj.apply(image);

 ImageOutputStream os = new ImageOutputStream(outputFile);
 os.write(image2);
 os.close();

 // Test with invalid input file (null)
 claimFalse("Expected an IOException when input file was null", () -> {
 ImageInputStream is2 = new ImageInputStream(null);
 Clip clipObj2 = new Clip(1.5f, 4.5f);
 Image image3 = is2.read();
 clipObj2.apply(image3);
 return true;
 });
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
}