import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.IntegerImage;
import jigl.image.types.RealGrayImage;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import static org.junit.Assert.assertEquals;

public class ClipTest {

 private GrayImage inputImage;
 private RealGrayImage inputImageFloat;
 private Clip clipInt;
 private Clip clipFloat;
 private IntegerImage outputImageInt;
 private RealGrayImage outputImageFloat;

 @Before
 public void setUp() {
 inputImage = new GrayImage(3, 3, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 inputImageFloat = new RealGrayImage(inputImage.X(), inputImage.Y(), new float[inputImage.X() * inputImage.Y()]);
 inputImage.setPixels(inputImageFloat.getPixels());
 clipInt = new Clip(2, 5);
 clipFloat = new Clip(2.5f, 5.5f);
 }

 @After
 public void tearDown() {
 if (inputImage != null) {
 inputImage.dispose();
 }
 if (inputImageFloat != null) {
 inputImageFloat.dispose();
 }
 if (outputImageInt != null) {
 outputImageInt.dispose();
 }
 if (outputImageFloat != null) {
 outputImageFloat.dispose();
 }
 }

 @Test
 public void testApplyGrayImageIntMinIntMax() {
 outputImageInt = (IntegerImage) clipInt.apply(inputImage);
 for (int y = 0; y < outputImageInt.Y(); y++) {
 for (int x = 0; x < outputImageInt.X(); x++) {
 assertEquals(clipInt.int_min, outputImageInt.get(x, y).intValue());
 assertEquals(clipInt.int_max, outputImageInt.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testApplyGrayImageIntMinIntMaxWithROI() {
 ROI roi = new ROI(1, 1, 2, 2);
 outputImageInt = (IntegerImage) clipInt.apply(inputImage, roi);
 for (int y = roi.uy(); y <= roi.ly(); y++) {
 for (int x = roi.ux(); x <= roi.lx(); x++) {
 assertEquals(clipInt.int_min, outputImageInt.get(x, y).intValue());
 assertEquals(clipInt.int_max, outputImageInt.get(x, y).intValue());
 }
 }
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMax() {
 outputImageFloat = (RealGrayImage) clipFloat.apply(inputImageFloat);
 for (int y = 0; y < outputImageFloat.Y(); y++) {
 for (int x = 0; x < outputImageFloat.X(); x++) {
 assertEquals(clipFloat.float_min, outputImageFloat.get(x, y), 0.01f);
 assertEquals(clipFloat.float_max, outputImageFloat.get(x, y), 0.01f);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMaxWithROI() {
 ROI roi = new ROI(1, 1, 2, 2);
 outputImageFloat = (RealGrayImage) clipFloat.apply(inputImageFloat, roi);
 for (int y = roi.uy(); y <= roi.ly(); y++) {
 for (int x = roi.ux(); x <= roi.lx(); x++) {
 assertEquals(clipFloat.float_min, outputImageFloat.get(x, y), 0.01f);
 assertEquals(clipFloat.float_max, outputImageFloat.get(x, y), 0.01f);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullImage() {
 try {
 clipInt.apply(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Image cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullROI() {
 try {
 clipInt.apply(inputImage, null);
 } catch (IllegalArgumentException e) {
 assertEquals("ROI cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageNullImageFloat() {
 try {
 clipFloat.apply(null);
 } catch (IllegalArgumentException e) {
 assertEquals("Image cannot be null.", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageNullImageROI() {
 try {
 clipFloat.apply(inputImageFloat, null);
 } catch (IllegalArgumentException e) {
 assertEquals("ROI cannot be null.", e.getMessage());
 throw e;
 }
 }
}