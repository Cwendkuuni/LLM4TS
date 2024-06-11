import org.junit.Test;
import static org.junit.Assert.assertEquals;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.GrayImage;
import jigl.image.ROI;
import jigl.image.types.IntegerType;
import jigl.image.types.FloatType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private ROI roi;

 @Test
 public void testClipIntConstructor() {
 clip = new Clip(0, 5);
 }

 @Test
 public void testApplyIntConstructorWithGrayImage() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = new ROI(1, 1, 3, 3);
 clip = new Clip(new Clip(0, 5));
 clip.apply(image, roi);

 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int expected = Math.min(Math.max(image.get(x, y).getValue().intValue(), 0), 5);
 int actual = image.get(x, y).getValue().intValue();
 assertEquals(expected, actual);
 }
 }
 }

 @Test
 public void testApplyFloatConstructorWithGrayImage() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = new ROI(1, 1, 3, 3);
 clip = new Clip(1.5f, 3.5f);
 clip.apply(image, roi);

 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float expected = Math.min(Math.max(image.get(x, y).getValue().floatValue(), 1.5f), 3.5f);
 float actual = image.get(x, y).getValue().floatValue();
 assertEquals(expected, actual, 0.01f);
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyROI() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = new ROI(-1, -1, 0, 0);
 clip = new Clip(0, 5);
 clip.apply(image, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeROI() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = new ROI(5, 5, -1, -1);
 clip = new Clip(0, 5);
 clip.apply(image, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOutOfImageROI() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = new ROI(10, 10, 3, 3);
 clip = new Clip(0, 5);
 clip.apply(image, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullImage() {
 image = null;
 roi = new ROI(1, 1, 3, 3);
 clip = new Clip(0, 5);
 clip.apply(image, roi);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullROI() {
 image = new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f},
 {1.0f, 2.0f, 3.0f, 4.0f, 5.0f}
 });
 roi = null;
 clip = new Clip(0, 5);
 clip.apply(image, roi);
 }

 @Test
 public void testApplyWithColorImage() {
 Image colorImage = new ColorImage(
 new GrayImage(new IntegerType(5, 5), new float[][]{
 {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
 {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
 {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
 {1.0f, 1.0f, 1.0f, 1.0f, 1.0f},
 {1.0f, 1.0f, 1.0f, 1.0f, 1.0f}
 }),
 new GrayImage(new IntegerType(5, 5), new float[][]{
 {2.0f, 2.0f, 2.0f, 2.0f, 2.0f},
 {2.0f, 2.0f, 2.0f, 2.0f, 2.0f},
 {2.0f, 2.0f, 2.0f, 2.0f, 2.0f},
 {2.0f, 2.0f, 2.0f, 2.0f, 2.0f},
 {2.0f, 2.0f, 2.0f, 2.0f, 2.0f}
 }),
 new GrayImage(new IntegerType(5, 5), new float[][]{
 {3.0f, 3.0f, 3.0f, 3.0f, 3.0f},
 {3.0f, 3.0f, 3.0f, 3.0f, 3.0f},
 {3.0f, 3.0f, 3.0f, 3.0f, 3.0f},
 {3.0f, 3.0f, 3.0f, 3.0f, 3.0f},
 {3.0f, 3.0f, 3.0f, 3.0f, 3.0f}
 })
 );
 clip = new Clip(0, 5);
 clip.apply(colorImage);

 for (int plane = 0; plane < 3; plane++) {
 for (int y = 0; y < colorImage.Y(); y++) {
 for (int x = 0; x < colorImage.X(); x++) {
 float min = 0.0f;
 float max = 5.0f;
 GrayImage planeImage = colorImage.plane(plane);
 assertEquals(min, planeImage.get(x, y).getValue().floatValue(), 0.01f);
 assertEquals(max, planeImage.get(x, y).getValue().floatValue(), 0.01f);
 }
 }
 }
 }

 @Test
 public void testMainMethod() {
 String inputfile = "testImage.jpg";
 Image image = null;
 Image image2 = null;
 ImageInputStream is = null;
 ImageOutputStream os = null;
 String outputfile = "outputImage.jpg";
 try {
 clip = new Clip(1.5f, 3.5f);
 is = new ImageInputStream(inputfile);
 image = is.read();
 is.close();
 os = new ImageOutputStream(outputfile);
 image2 = clip.apply(image);
 os.write(image2);
 os.close();
 } catch (Exception e) {
 e.printStackTrace();
 } finally {
 try {
 if (is != null) {
 is.close();
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 try {
 if (os != null) {
 os.close();
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 }
 }