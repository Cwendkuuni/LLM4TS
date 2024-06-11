import junit.framework.Assert;
import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.ImageOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.ImageType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest extends TestCase {

 private Clip clip;
 private GrayImage image;
 private GrayImage expectedResult;

 @Before
 public void setUp() {
 image = new GrayImage(new int[3][3], ImageType.BYTE);
 expectedResult = new GrayImage(new int[3][3], ImageType.BYTE);
 expectedResult.set(0, 0, (byte) 50);
 expectedResult.set(0, 1, (byte) 100);
 expectedResult.set(0, 2, (byte) 75);
 expectedResult.set(1, 0, (byte) 150);
 expectedResult.set(1, 1, (byte) 200);
 expectedResult.set(1, 2, (byte) 175);
 expectedResult.set(2, 0, (byte) 255);
 expectedResult.set(2, 1, (byte) 255);
 expectedResult.set(2, 2, (byte) 255);
 clip = new Clip(50.0f, 150.0f);
 }

 @Test
 public void testApplyGrayImageZeroMinMax() {
 GrayImage result = clip.apply(image);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testApplyGrayImageCustomMinMax() {
 clip = new Clip(75.0f, 125.0f);
 GrayImage result = clip.apply(image);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testApplyGrayImageNegativeMinMax() {
 clip = new Clip(-50.0f, -150.0f);
 GrayImage result = clip.apply(image);
 Assert.assertSame(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageZeroMin() {
 clip = new Clip(0.0f, 0.0f);
 clip.apply(image);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullImage() {
 clip = new Clip(50.0f, 150.0f);
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyGrayImageNullROI() {
 clip = new Clip(50.0f, 150.0f);
 clip.apply(image, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyGrayImageInvalidROI() {
 ROI roi = new ROI(-1, 0, 3, 3);
 clip = new Clip(50.0f, 150.0f);
 clip.apply(image, roi);
 }

 @Test
 public void testApplyRealGrayImageZeroMinMax() {
 RealGrayImage realImage = new RealGrayImage(new float[3][3], ImageType.FLOAT);
 for (int i = 0; i < 3; i++) {
 for (int j = 0; j < 3; j++) {
 realImage.set(i, j, (float) 50.0);
 }
 }
 RealGrayImage result = (RealGrayImage) clip.apply(realImage);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testApplyRealGrayImageCustomMinMax() {
 clip = new Clip(75.0f, 125.0f);
 RealGrayImage result = (RealGrayImage) clip.apply(realImage);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testApplyRealGrayImageNegativeMinMax() {
 clip = new Clip(-50.0f, -150.0f);
 RealGrayImage result = (RealGrayImage) clip.apply(realImage);
 Assert.assertSame(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageZeroMin() {
 clip = new Clip(0.0f, 0.0f);
 clip.apply(realImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageNullImage() {
 clip = new Clip(50.0f, 150.0f);
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyRealGrayImageNullROI() {
 clip = new Clip(50.0f, 150.0f);
 clip.apply(realImage, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyRealGrayImageInvalidROI() {
 ROI roi = new ROI(-1, 0, 3, 3);
 clip = new Clip(50.0f, 150.0f);
 clip.apply(realImage, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageEmptyArgs() {
 clip = new Clip(50.0f, 150.0f);
 ColorImage colorImage = null;
 clip.apply(colorImage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageNullImage() {
 clip = new Clip(50.0f, 150.0f);
 ColorImage colorImage = new ColorImage(new int[3][3][3], ImageType.BYTE);
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyColorImageNullROI() {
 clip = new Clip(50.0f, 150.0f);
 ColorImage colorImage = new ColorImage(new int[3][3][3], ImageType.BYTE);
 clip.apply(colorImage, null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testApplyColorImageInvalidROI() {
 ROI roi = new ROI(-1, 0, 3, 3);
 clip = new Clip(50.0f, 150.0f);
 clip.apply(colorImage, roi);
 }

 @Test
 public void testMain() {
 String inputFile = "test_input.jpg";
 String outputFile = "test_output.jpg";
 ImageInputStream is = new ImageInputStream(inputFile);
 Image image = is.read();
 is.close();
 ImageOutputStream os = new ImageOutputStream(outputFile);
 Float minValue = Float.valueOf(25.0f);
 Float maxValue = Float.valueOf(75.0f);
 Clip clip = new Clip(minValue, maxValue);
 Image image2 = clip.apply(image);
 os.write(image2);
 os.close();
 }
}