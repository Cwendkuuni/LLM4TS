package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

 @Test
 public void testClipIntInt() {
 Clip clip = new Clip(5, 10);
 GrayImage inputImage = new GrayImage(new int[10][10], 10, 10);
 GrayImage outputImage = new GrayImage(new int[10][10], 10, 10);
 clip.apply(inputImage);
 ROI roi = new ROI(0, 0, 5, 5);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int expectedValue = Math.min(Math.max(inputImage.get(x, y), 5), 10);
 int actualValue = outputImage.get(x, y).intValue();
 assertEquals(expectedValue, actualValue);
 }
 }
 }

 @Test
 public void testClipIntInt_ApplyToRealGrayImage() {
 Clip clip = new Clip(5.0f, 10.0f);
 RealGrayImage inputImage = new RealGrayImage(new float[10][10], 10, 10);
 RealGrayImage outputImage = new RealGrayImage(new float[10][10], 10, 10);
 clip.apply(inputImage);
 ROI roi = new ROI(0, 0, 5, 5);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expectedValue = Math.min(Math.max(inputImage.get(x, y), 5.0f), 10.0f);
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testClipFloatFloat() {
 Clip clip = new Clip(5.0f, 10.0f);
 RealGrayImage inputImage = new RealGrayImage(new float[10][10], 10, 10);
 RealGrayImage outputImage = new RealGrayImage(new float[10][10], 10, 10);
 clip.apply(inputImage);
 ROI roi = new ROI(0, 0, 5, 5);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expectedValue = Math.min(Math.max(inputImage.get(x, y), 5.0f), 10.0f);
 float actualValue = outputImage.get(x, y).floatValue();
 assertEquals(expectedValue, actualValue, 0.001f);
 }
 }
 }

 @Test
 public void testClipFloatFloat_ApplyToColorImage() {
 Clip clip = new Clip(5.0f, 10.0f);
 ColorImage inputImage = new ColorImage(new GrayImage(new int[10][10], 10, 10), new GrayImage(new int[10][10], 10, 10), new GrayImage(new int[10][10], 10, 10), 10, 10);
 ColorImage outputImage = new ColorImage(new GrayImage(new int[10][10], 10, 10), new GrayImage(new int[10][10], 10, 10), new GrayImage(new int[10][10], 10, 10), 10, 10);
 clip.apply(inputImage);
 ROI roi = new ROI(0, 0, 5, 5);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int rChannelExpectedValue = Math.min(Math.max(inputImage.plane(0).get(x, y).intValue(), 5), 10);
 int gChannelExpectedValue = Math.min(Math.max(inputImage.plane(1).get(x, y).intValue(), 5), 10);
 int bChannelExpectedValue = Math.min(Math.max(inputImage.plane(2).get(x, y).intValue(), 5), 10);
 int rChannelActualValue = outputImage.plane(0).get(x, y).intValue();
 int gChannelActualValue = outputImage.plane(1).get(x, y).intValue();
 int bChannelActualValue = outputImage.plane(2).get(x, y).intValue();
 assertEquals(rChannelExpectedValue, rChannelActualValue);
 assertEquals(gChannelExpectedValue, gChannelActualValue);
 assertEquals(bChannelExpectedValue, bChannelActualValue);
 }
 }
 }

 @Test
 public void testClipMain() {
 try {
 Image inputImage = null;
 String inputFile = "testImage.pgm";
 Image outputImage = null;
 String outputFile = "testOutputImage.pgm";
 String commandLineArguments[] = {"5.0f", "10.0f", inputFile, outputFile};
 Clip.main(commandLineArguments);
 ImageInputStream is = new ImageInputStream(inputFile);
 inputImage = is.read();
 is.close();
 Clip clip = new Clip(5.0f, 10.0f);
 outputImage = clip.apply(inputImage);
 ImageOutputStream os = new ImageOutputStream(outputFile);
 os.write(outputImage);
 os.close();
 ImageInputStream isOutput = new ImageInputStream(outputFile);
 Image expectedImage = isOutput.read();
 isOutput.close();
 assertArrayEquals(expectedImage.getPixels(), outputImage.getPixels());
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to execute main method");
 }
 }

}