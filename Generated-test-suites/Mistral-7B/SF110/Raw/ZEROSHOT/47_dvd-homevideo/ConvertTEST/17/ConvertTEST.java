import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdirs();
 }

 @After
 public void tearDown() {
 File dir = new File(outputDir);
 Arrays.stream(dir.listFiles()).forEach(File::deleteOrFail);
 dir.deleteOrFail();
 }

 @Test
 public void testConvertConstruction() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 // Dummy implementation of GUI class
 @Override
 public void MessageBox(String message, int type) {
 // Not used in this test
 }

 @Override
 public void txtAreaOutput_append(String text) {
 // Not used in this test
 }

 @Override
 public void prgConvert_setValue(int value) {
 // Not used in this test
 }

 @Override
 public void prgConvert_setEnabled(boolean enabled) {
 // Not used in this test
 }

 @Override
 public void lblConvertProg_setText(String text) {
 // Not used in this test
 }

 @Override
 public void lblConvert_setEnabled(boolean enabled) {
 // Not used in this test
 }

 @Override
 public void lblConvertProg_setEnabled(boolean enabled) {
 // Not used in this test
 }

 @Override
 public int spnMinutes_getValue() {
 return 1;
 }

 @Override
 public int spnSeconds_getValue() {
 return 1;
 }
 });

 Assert.assertEquals("dvd-ntsc", convert.m_Format);
 Assert.assertEquals("4:3", convert.m_AspectRatio);
 }

 @Test
 public void testConvertConstructionWithQuality() {
 convert = new Convert(5, "dvd-ntsc", "4:3", new GUI() {
 // Dummy implementation of GUI class
 });

 Assert.assertEquals(5, convert.m_Quality);
 Assert.assertEquals("dvd-ntsc", convert.m_Format);
 Assert.assertEquals("4:3", convert.m_AspectRatio);
 }

 @Test
 public void testInit() throws InterruptedException {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 // Dummy implementation of GUI class
 });

 Assert.assertFalse(convert.init());
 Assert.assertTrue(convert.m_Error);
 }

 @Test
 public void testTranscode() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 // Dummy implementation of GUI class
 });

 File[] files = new File(outputDir).listFiles();
 Assert.assertNotNull(files);
 Assert.assertEquals(1, files.length);
 }

 @Test(expected = NullPointerException.class)
 public void testTranscodeWithNullGUI() {
 convert = new Convert("dvd-ntsc", "4:3", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranscodeWithInvalidFormat() {
 convert = new Convert("invalid_format", "4:3", new GUI() {
 // Dummy implementation of GUI class
 });
 Assert.assertTrue(convert.init());
 Assert.assertTrue(convert.m_Error);
 }

 @Test
 public void testMatrixCreation() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 // Dummy implementation of GUI class
 });
 File matrixFile = new File(outputDir, "matrix.txt");
 Assert.assertTrue(matrixFile.exists());
 matrixFile.deleteOrFail();
 }
}