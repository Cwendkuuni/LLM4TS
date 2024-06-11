import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir = "output"; // Set your output directory here

 @Test
 public void testConvertConstruction() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 assertFalse(convert.isError());
 }

 @Test
 public void testConvertConstructionWithQuality() {
 convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
 assertEquals(1, convert.getQuality());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 assertFalse(convert.isError());
 }

 @Test
 public void testInitThreadNotSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 assertEquals(true, convert.isError());
 }

 @Test
 public void testInitThreadSelected() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(true);
 assertFalse(convert.init());
 assertEquals(true, convert.isError());
 }

 @Test
 public void testInitSuccess() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(false);
 assertTrue(convert.init());
 assertFalse(convert.isError());
 }

 @Test
 public void testTranscodeSuccess() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(false);
 assertTrue(convert.init());
 File[] files = new File(outputDir).listFiles();
 assertEquals(files.length, convert.video_files.length);
 for (File file : files) {
 String fileName = file.getName();
 assertTrue(Arrays.asList(convert.video_files).contains(fileName.substring(0, 11)));
 }
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithNonStandardFile() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_GUI.menuChkThread.setSelected(false);
 assertTrue(convert.init());
 File[] files = new File(outputDir).listFiles();
 assertFalse(files.length > convert.video_files.length);
 File nonStandardFile = new File(outputDir + "/non_standard_file.mp4");
 assertTrue(nonStandardFile.exists());
 }
}