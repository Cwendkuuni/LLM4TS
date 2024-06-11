import junit.framework.TestCase;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.ops.ROI;

public class ClipTest {

 private Clip clip;
 private GrayImage image;
 private RealGrayImage realImage;
 private ColorImage colorImage;
 private RealColorImage realColorImage;
 private ROI roi;

 @Before
 public void setUp() {
 image = new GrayImage(5, 5);
 realImage = new RealGrayImage(5, 5);
 colorImage = new ColorImage(5, 5, 3);
 realColorImage = new RealColorImage(5, 5, 3);
 clip = new Clip(0, 5);
 roi = new ROI(0, 0, 5, 5);
 }

 @Test
 public void testClipIntConstructor() {
 clip = new Clip(0, 5);
 assertTrue(clip.int_min == 0 && clip.int_max == 5);
 }

 @Test
 public void testClipFloatConstructor() {
 clip = new Clip(0.0f, 5.0f);
 assertTrue(clip.float_min == 0.0f && clip.float_max == 5.0f);
 }

 @Test
 public void testApplyGrayImageIntMinIntMax() {
 clip.apply(image);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 int value = image.get(x, y).intValue();
 assertTrue(value >= clip.int_min && value <= clip.int_max);
 }
 }
 }

 @Test
 public void testApplyRealGrayImageFloatMinFloatMax() {
 clip.apply(realImage);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 float value = realImage.get(x, y).floatValue();
 assertTrue(value >= clip.float_min && value <= clip.float_max);
 }
 }
 }

 @Test
 public void testApplyColorImageIntMinIntMax() {
 clip.apply(colorImage);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int channel = 0; channel < 3; ++channel) {
 int value = colorImage.get(x, y, channel).intValue();
 assertTrue(value >= clip.int_min && value <= clip.int_max);
 }
 }
 }
 }

 @Test
 public void testApplyRealColorImageFloatMinFloatMax() {
 clip.apply(realColorImage);
 for (int y = roi.uy(); y <= roi.ly(); ++y) {
 for (int x = roi.ux(); x <= roi.lx(); ++x) {
 for (int channel = 0; channel < 3; ++channel) {
 float value = realColorImage.get(x, y, channel).floatValue();
 assertTrue(value >= clip.float_min && value <= clip.float_max);
 }
 }
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullImage() {
 clip.apply(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyNullROI() {
 clip.apply(image, null);
 }

 @Test
 public void testMainMethod() {
 try {
 Image inputImage = new GrayImage(5, 5);
 RealGrayImage expectedOutputImage = new RealGrayImage(5, 5);
 for (int y = 0; y < 5; ++y) {
 for (int x = 0; x < 5; ++x) {
 expectedOutputImage.set(x, y, Math.max(Math.min(x, y), 0.0f));
 }
 }
 String inputFile = "input.jpg";
 String outputFile = "output.jpg";
 String[] argv = new String[]{"0.0", "5.0", inputFile, outputFile};
 Clip.main(argv);
 Image outputImage = new ImageInputStream(outputFile).read();
 assertEquals(expectedOutputImage, outputImage);
 } catch (Exception e) {
 fail("Exception was thrown: " + e.getMessage());
 }
 }

 @After
 public void tearDown() {
 image = null;
 realImage = null;
 colorImage = null;
 realColorImage = null;
 clip = null;
 roi = null;
 }
}