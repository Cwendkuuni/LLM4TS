package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import org.junit.Test;

public class ClipTest {

 private GrayImage createGrayImage(int width, int height) {
 return new GrayImage(new ByteArrayImageBuffer(width, height), new GrayImageMetadata(width, height));
 }

 private RealGrayImage createRealGrayImage(int width, int height) {
 return new RealGrayImage(new FloatArrayImageBuffer(width, height), new RealGrayImageMetadata(width, height));
 }

 private ColorImage createColorImage(int width, int height) {
 return new ColorImage(new ColorimageBuffer(width, height, new byte[3 * width * height]), new ColorImageMetadata(width, height));
 }

 private RealColorImage createRealColorImage(int width, int height) {
 return new RealColorImage(new FloatArrayImageBuffer(width, height, 3), new RealColorImageMetadata(width, height));
 }

 @Test
 public void testClipIntInt() {
 GrayImage input = createGrayImage(5, 5);
 Clip clip = new Clip(2, 4);
 Image output = clip.apply(input);
 ROI expectedROI = new ROI(0, 0, input.X() - 1, input.Y() - 1);
 for (int y = expectedROI.uy(); y <= expectedROI.ly(); ++y) {
 for (int x = expectedROI.ux(); x <= expectedROI.lx(); ++x) {
 int value = input.get(x, y).intValue();
 int expectedValue = Math.max(Math.min(value, clip.int_max), clip.int_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).intValue());
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testClipIntIntApplyToGrayImage() {
 GrayImage input = createGrayImage(5, 5);
 GrayImage expectedOutput = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = x * x + y * y;
 expectedOutput.set(x, y, Integer.valueOf(value));
 }
 }
 Clip clip = new Clip(2, 4);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 int value = input.get(x, y).intValue();
 int expectedValue = Math.max(Math.min(value, clip.int_max), clip.int_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).intValue());
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).intValue());
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixels(), output.pixels());
 }

 @Test
 public void testClipIntIntApplyToRealGrayImage() {
 RealGrayImage input = createRealGrayImage(5, 5);
 RealGrayImage expectedOutput = createRealGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = x * 0.1f + y * 0.2f;
 expectedOutput.set(x, y, Float.valueOf(value));
 }
 }
 Clip clip = new Clip(2.5f, 4.5f);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = input.get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).floatValue(), 0.001f);
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixelsFloat(), output.pixelsFloat(), 0.001f);
 }

 @Test
 public void testClipFloatFloat() {
 GrayImage input = createGrayImage(5, 5);
 Clip clip = new Clip(2.5f, 4.5f);
 Image output = clip.apply(input);
 ROI expectedROI = new ROI(0, 0, input.X() - 1, input.Y() - 1);
 for (int y = expectedROI.uy(); y <= expectedROI.ly(); ++y) {
 for (int x = expectedROI.ux(); x <= expectedROI.lx(); ++x) {
 float value = input.get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).floatValue(), 0.001f);
 }
 }
 }

 @Test
 public void testClipFloatFloatApplyToGrayImage() {
 GrayImage input = createGrayImage(5, 5);
 GrayImage expectedOutput = createGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = (float)Math.sqrt(x * x + y * y);
 expectedOutput.set(x, y, Float.valueOf(value));
 }
 }
 Clip clip = new Clip(2.5f, 4.5f);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = input.get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).floatValue(), 0.001f);
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixelsFloat(), output.pixelsFloat(), 0.001f);
 }

 @Test
 public void testClipFloatFloatApplyToRealGrayImage() {
 RealGrayImage input = createRealGrayImage(5, 5);
 RealGrayImage expectedOutput = createRealGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = x * 0.1f + y * 0.2f;
 expectedOutput.set(x, y, Float.valueOf(value));
 }
 }
 Clip clip = new Clip(2.5f, 4.5f);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 float value = input.get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.get(x, y).floatValue(), 0.001f);
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixelsFloat(), output.pixelsFloat(), 0.001f);
 }

 @Test
 public void testClipFloatFloatApplyToColorImage() {
 ColorImage input = createColorImage(5, 5);
 ColorImage expectedOutput = createColorImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 input.setPlane(0, x, y, Float.valueOf((float)(x * x + y * y) / 255.0f));
 input.setPlane(1, x, y, Float.valueOf((float)(x * x + y * y) / 510.0f));
 input.setPlane(2, x, y, Float.valueOf((float)(x * x + y * y) / 765.0f));
 }
 }
 Clip clip = new Clip(0.2f, 0.8f);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 for (int channel = 0; channel < 3; ++channel) {
 float value = input.getPlane(channel).get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.getPlane(channel).get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.getPlane(channel).get(x, y).floatValue(), 0.001f);
 }
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixelsFloat(), output.pixelsFloat(), 0.001f);
 }

 @Test
 public void testClipFloatFloatApplyToRealColorImage() {
 RealColorImage input = createRealColorImage(5, 5);
 RealColorImage expectedOutput = createRealColorImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 expectedOutput.setPlane(0, x, y, Float.valueOf(x * 0.1f + y * 0.2f));
 expectedOutput.setPlane(1, x, y, Float.valueOf(x * 0.2f + y * 0.3f));
 expectedOutput.setPlane(2, x, y, Float.valueOf(x * 0.3f + y * 0.4f));
 }
 }
 Clip clip = new Clip(0.5f, 1.5f);
 Image output = clip.apply(input);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 for (int channel = 0; channel < 3; ++channel) {
 float value = input.getPlane(channel).get(x, y).floatValue();
 float expectedValue = Math.max(Math.min(value, clip.float_max), clip.float_min);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, input.getPlane(channel).get(x, y).floatValue(), 0.001f);
 assertEquals("Failed for pixel (" + x + ", " + y + ")", expectedValue, output.getPlane(channel).get(x, y).floatValue(), 0.001f);
 }
 }
 }
 assertArrayEquals("Output image is not the same as expected", expectedOutput.pixelsFloat(), output.pixelsFloat(), 0.001f);
 }

}