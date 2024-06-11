import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.IntegerType;
import jigl.image.types.Plane;
import jigl.image.types.RealType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private RealGrayImage realImage;
 private ColorImage colorImage;
 private RealColorImage realColorImage;
 private ROI roi;

 @Before
 public void setUp() {
 // Create test data
 image = new GrayImage(5, 5, new IntegerType());
 realImage = new RealGrayImage(5, 5, new RealType());
 colorImage = new ColorImage(5, 5, new Plane[3]);
 realColorImage = new RealColorImage(5, 5, new Plane[3], new RealType[3]);

 // Initialize clip with integer values
 clip = new Clip(1, 2);

 // Initialize clip with float values
 clip = new Clip(1.5f, 2.5f);
 }

 @Test
 public void testClipIntegerConstructor() {
 // Test apply method with integer constructor
 clip = new Clip(1, 2);
 Image outputImage = clip.apply(image);
 assertFalse("Expected output image to have different dimensions", image.equals(outputImage));

 int[][] expected = new int[5][5];
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 expected[y][x] = Math.min(Math.max(image.get(x, y), 1), 2);
 }
 }
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 assertEquals("Pixel value not as expected", expected[y][x], outputImage.get(x, y));
 }
 }
 }

 @Test
 public void testClipFloatConstructor() {
 // Test apply method with float constructor
 clip = new Clip(1.5f, 2.5f);
 Image outputImage = clip.apply(realImage);
 assertFalse("Expected output image to have different dimensions", realImage.equals(outputImage));

 float[][] expected = new float[5][5];
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 expected[y][x] = Math.min(Math.max(realImage.get(x, y), 1.5f), 2.5f);
 }
 }
 for (int y = 0; y < 5; y++) {
 for (int x = 0; x < 5; x++) {
 assertEquals("Pixel value not as expected", expected[y][x], outputImage.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyGrayImage() {
 // Test apply method with GrayImage
 clip = new Clip(1, 2);
 roi = new ROI(1, 1, 3, 3);
 Image inputImage = clip.apply(image, roi);
 assertFalse("Expected input image to have different dimensions", image.equals(inputImage));
 assertFalse("Expected output image to have different dimensions", image.equals(clip.apply(image)));

 int[][] expected = new int[3][3];
 for (int y = 0; y < 3; y++) {
 for (int x = 0; x < 3; x++) {
 expected[y][x] = Math.min(Math.max(image.get(x, y), 1), 2);
 }
 }
 for (int y = 0; y < 3; y++) {
 for (int x = 0; x < 3; x++) {
 assertEquals("Pixel value not as expected", expected[y][x], inputImage.get(x, y));
 }
 }
 }

 @Test
 public void testApplyRealGrayImage() {
 // Test apply method with RealGrayImage
 clip = new Clip(1.5f, 2.5f);
 roi = new ROI(1, 1, 3, 3);
 Image inputImage = clip.apply(realImage, roi);
 assertFalse("Expected input image to have different dimensions", realImage.equals(inputImage));
 assertFalse("Expected output image to have different dimensions", realImage.equals(clip.apply(realImage)));

 float[][] expected = new float[3][3];
 for (int y = 0; y < 3; y++) {
 for (int x = 0; x < 3; x++) {
 expected[y][x] = Math.min(Math.max(realImage.get(x, y), 1.5f), 2.5f);
 }
 }
 for (int y = 0; y < 3; y++) {
 for (int x = 0; x < 3; x++) {
 assertEquals("Pixel value not as expected", expected[y][x], inputImage.get(x, y), 0.001f);
 }
 }
 }

 @Test
 public void testApplyColorImage() {
 // Test apply method with ColorImage
 clip = new Clip(1, 2);
 Image outputImage = clip.apply(colorImage);
 assertFalse("Expected output image to have different dimensions", colorImage.equals(outputImage));

 Plane[] planes = new Plane[3];
 for (int i = 0; i < 3; i++) {
 planes[i] = new GrayImage(5, 5, new IntegerType());
 }
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 clip.apply(colorImage, new ROI(0, 0, colorImage.X() - 1, colorImage.Y() - 1), planes[planeIndex]);
 assertFalse("Expected output image to have different dimensions", planes[planeIndex].equals(outputImage.getPlane(planeIndex)));
 }
 }

 @Test
 public void testApplyRealColorImage() {
 // Test apply method with RealColorImage
 clip = new Clip(1.5f, 2.5f);
 Image outputImage = clip.apply(realColorImage);
 assertFalse("Expected output image to have different dimensions", realColorImage.equals(outputImage));

 Plane[] planes = new Plane[3];
 for (int i = 0; i < 3; i++) {
 planes[i] = new RealGrayImage(5, 5, new RealType());
 }
 for (int planeIndex = 0; planeIndex < 3; planeIndex++) {
 clip.apply(realColorImage, new ROI(0, 0, realColorImage.X() - 1, realColorImage.Y() - 1), planes[planeIndex]);
 assertFalse("Expected output image to have different dimensions", planes[planeIndex].equals(outputImage.getPlane(planeIndex)));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullImage() {
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullRoi() {
 clip.apply(image, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNegativeImageDimensions() {
 clip = new Clip(1, 2);
 Image inputImage = clip.apply(new GrayImage(5, 5, new IntegerType()), new ROI(-1, -1, 5, 5));
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNegativeRoiDimensions() {
 clip = new Clip(1, 2);
 ROI roi = new ROI(5, 5, -1, -1);
 Image inputImage = clip.apply(image, roi);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testApplyWithZeroMinAndMax() {
 clip = new Clip(0, 0);
 Image outputImage = clip.apply(image);
 assertFalse("Expected output image to have different dimensions", image.equals(outputImage));
 }

 @Test
 public void testApplyWithZeroMinAndMaxInConstructor() {
 clip = new Clip();
 Image outputImage = clip.apply(image);
 assertFalse("Expected output image to have different dimensions", image.equals(outputImage));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNegativeMin() {
 clip = new Clip(-1, 2);
 try {
 clip.apply(image);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithNegativeMax() {
 clip = new Clip(1, -1);
 try {
 clip.apply(image);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyWithMinGreaterThanMax() {
 clip = new Clip(3, 1);
 try {
 clip.apply(image);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}