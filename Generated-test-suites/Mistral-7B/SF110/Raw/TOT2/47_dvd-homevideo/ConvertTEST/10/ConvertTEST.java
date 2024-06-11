import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import static java.lang.String.format;

public class ConvertTest {

 private Convert convert;
 private String strOutputDir;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() {
 strOutputDir = "test_output_dir";
 System.setProperty("user.dir", strOutputDir);
 System.setProperty("dvd.gui", "TestDVDGUI");
 convert = new Convert("dvd-ntsc", "4:3");
 }

 @Test
 public void testConvertConstructorWithFormatAndAspectRatio() {
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 }

 @Test
 public void testConvertConstructorWithQualityFormatAndAspectRatio() {
 convert = new Convert(5, "dvd-ntsc", "4:3", new TestDVDGUI());
 assertEquals(5, convert.getQuality());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 }

 @Test
 public void testInitMethodWhenThreadIsNotSelected() {
 convert.init();
 assertFalse(convert.isError());
 // Check the output files have been transcoded and muxed correctly
 File[] outputFiles = new File(strOutputDir + "/dv/").listFiles();
 assertEquals(1, outputFiles.length);
 String expectedFileName = "test1.vob";
 assertEquals(expectedFileName, outputFiles[0].getName());
 }

 @Test
 public void testInitMethodWhenThreadIsSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.init();
 assertFalse(convert.isError());
 // Check the output files have been transcoded and muxed correctly
 File[] outputFiles = new File(strOutputDir + "/dv/").listFiles();
 assertEquals(1, outputFiles.length);
 String expectedFileName = "test1.vob";
 assertEquals(expectedFileName, outputFiles[0].getName());
 // Check the progress bar is updated during the conversion process
 assertEquals(100, convert.m_GUI.prgConvert.getValue());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitMethodWhenThereIsNoFileInDvdDirectory() {
 File file = new File(strOutputDir + "/dv/");
 file.deleteOnExit();
 file.mkdir();
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 convert.init();
 fail("Should throw StringIndexOutOfBoundsException when there are no files in the dv directory");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranscodeMethodWhenFormatIsNotSupported() {
 convert = new Convert("unsupportedFormat", "4:3", new TestDVDGUI());
 convert.init();
 convert.Transcode();
 fail("Should throw IllegalArgumentException when the format is not supported");
 }

 @Test
 public void testTranscodeMethodWithSupportedFormat() {
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 convert.init();
 convert.Transcode();
 // Check the output files have been transcoded and muxed correctly
 File[] outputFiles = new File(strOutputDir + "/dv/").listFiles();
 assertEquals(1, outputFiles.length);
 String expectedFileName = "test1.vob";
 assertEquals(expectedFileName, outputFiles[0].getName());
 }

 @Test(expected = IOException.class)
 public void testTranscodeMethodWhenIOExceptionOccurs() {
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 convert.init();
 try {
 convert.m_transcode = convert.m_transcode.replaceAll("mplex", "brokenMplex");
 convert.Transcode();
 }
 catch (Exception e) {
 // Expected IOException
 }
 fail("Should throw IOException when an error occurs during the transcode process");
 }

 @Test
 public void testMatrixMethodWritesMatrixFileCorrectly() {
 convert = new Convert("dvd-ntsc", "4:3", new TestDVDGUI());
 convert.init();
 convert.matrix();
 File matrixFile = new File(strOutputDir + "/matrix.txt");
 String expectedContent = "# High resolution INTRA table\n" +
 "8,16,18,20,24,25,26,30\n" +
 "16,16,20,23,25,26,30,30\n" +
 "18,20,22,24,26,28,29,31\n" +
 "20,21,23,24,26,28,31,31\n" +
 "21,23,24,25,28,30,30,33\n" +
 "23,24,25,28,30,30,33,36\n" +
 "24,25,26,29,29,31,34,38\n" +
 "25,26,28,29,31,34,38,42\n" +
 "# TMPEGEnc NON-INTRA table\n" +
 "16,17,18,19,20,21,22,23\n" +
 "17,18,19,20,21,22,23,24\n" +
 "18,19,20,21,22,23,24,25\n" +
 "19,20,21,22,23,24,26,27\n" +
 "20,21,22,23,25,26,27,28\n" +
 "21,22,23,24,26,27,28,30\n" +
 "22,23,24,26,27,28,30,31\n" +
 "23,24,25,27,28,30,31,33\n";
 assertEquals(expectedContent, new String(Files.readAllBytes(matrixFile.toPath())));
 }
}