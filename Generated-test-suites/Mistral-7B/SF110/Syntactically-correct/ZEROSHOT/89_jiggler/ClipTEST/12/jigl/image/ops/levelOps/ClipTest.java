package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.types.GrayImage;
import jigl.image.types.ROI;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Before;
import org.junit.Test;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private ImageInputStream inputStreamMock;
 private ImageOutputStream outputStreamMock;

 @Before
 public void setUp() {
 image = new GrayImage(5, 5);
 clip = new Clip(0.5f, 1.5f);
 inputStreamMock = mock(ImageInputStream.class);
 outputStreamMock = mock(ImageOutputStream.class);
 }

 @Test
 public void testApplyGrayImageIntInt() {
 GrayImage expectedResult = new GrayImage(5, 5);
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 int value = x * x + y * y;
 expectedResult.set(x, y, Integer.valueOf(value));
 }
 }
 when(inputStreamMock.read()).thenReturn(image);

 clip.apply(inputStreamMock, outputStreamMock);
 assertArrayEquals(expectedResult.getPixels(), outputStreamMock.getPixels(new int[5 * 5]));
 }

 @Test
 public void testApplyGrayImageROI() {
 ROI roi = new ROI(2, 2, 3, 3);
 GrayImage expectedResult = new GrayImage(3, 3);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int value = x * x + y * y;
 expectedResult.set(x - roi.ux(), y - roi.uy(), Integer.valueOf(value));
 }
 }
 when(inputStreamMock.read()).thenReturn(image);

 clip.apply(inputStreamMock, outputStreamMock, roi);
 assertArrayEquals(expectedResult.getPixels(), outputStreamMock.getPixels(new int[3 * 3]));
 }

 @Test
 public void testApplyRealGrayImageFloatFloat() {
 RealGrayImage expectedResult = new RealGrayImage(5, 5);
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 float value = (float) (x * x + y * y);
 expectedResult.set(x, y, Float.valueOf(value));
 }
 }
 when(inputStreamMock.read()).thenReturn(image);

 clip.apply(inputStreamMock, outputStreamMock, 0.5f, 1.5f);
 assertArrayEquals(expectedResult.getPixels(), outputStreamMock.getPixels(new float[5 * 5]));
 }

 @Test
 public void testApplyRealGrayImageROI() {
 ROI roi = new ROI(2, 2, 3, 3);
 RealGrayImage expectedResult = new RealGrayImage(3, 3);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float value = (float) (x * x + y * y);
 expectedResult.set(x - roi.ux(), y - roi.uy(), Float.valueOf(value));
 }
 }
 when(inputStreamMock.read()).thenReturn(image);

 clip.apply(inputStreamMock, outputStreamMock, 0.5f, 1.5f, roi);
 assertArrayEquals(expectedResult.getPixels(), outputStreamMock.getPixels(new float[3 * 3]), 0.001f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullOutput() {
 clip.apply(inputStreamMock, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNegativeSizeROI() {
 ROI roi = new ROI(-2, -2, 3, 3);
 clip.apply(inputStreamMock, outputStreamMock, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageInvalidInput() {
 Image input = new Object();
 clip.apply(inputStreamMock, outputStreamMock, input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullInput() {
 clip.apply(null, outputStreamMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullInputStream() {
 clip.apply(null, outputStreamMock);
 }
}