import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

 private Convert convert;
 private String format;
 private String aspectRatio;
 private GUI gui;

 @Before
 public void setUp() {
 format = "dvd-ntsc";
 aspectRatio = "4:3";
 gui = new GUI() {}; // Dummy GUI implementation
 convert = new Convert(format, aspectRatio, gui);
 }

 @Test
 public void testConvertConstructionWithFormat() {
 Convert convert = new Convert(format, aspectRatio, gui);
 assertEquals(format, convert.m_Format);
 }

 @Test
 public void testConvertConstructionWithQualityAndFormat() {
 Convert convert = new Convert(5, format, aspectRatio, gui);
 assertEquals(5, convert.m_Quality);
 }

 @Test
 public void testInitNoThread() {
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInitWithThread() {
 convert = new Convert(format, aspectRatio, gui);
 convert.m_GUI.menuChkThread.setSelected(true);
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeWithInvalidFileList() {
 convert.Transcode();
 fail("Expected StringIndexOutOfBoundsException");
 }

 @Test
 public void testTranscodeWithValidFiles() {
 convert.video_files = new String[]{"file1.dv", "file2.dv"};
 convert.Transcode();
 assertTrue(convert.m_Error || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/transcode.log") || convert.m_GUI.ErrorCheck(convert.m_GUI.strOutputDir + "/log/mplex.log"));
 }

 @Test
 public void testMatrixFileCreation() {
 File file = new File(convert.m_GUI.strOutputDir, "matrix.txt");
 assertTrue(file.exists());
 }

}