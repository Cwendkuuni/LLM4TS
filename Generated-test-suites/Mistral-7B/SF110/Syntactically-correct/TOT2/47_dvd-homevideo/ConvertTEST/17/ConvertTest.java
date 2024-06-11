import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert converter;
 private String outputDir = "output";

 @Before
 public void setup() {
 File outputDirectory = new File(outputDir);
 outputDirectory.mkdirs();
 converter = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 converter.init();
 }

 @Test
 public void testInitWithEmptyDVDDirectory() {
 File listDir = new File(outputDir + "/dv/");
 assertFalse(listDir.isDirectory());
 assertTrue(converter.init());
 }

 @Test
 public void testInitWithNonDVDFile() {
 File nonDVDFile = new File(outputDir + "/nonDVD.txt");
 nonDVDFile.createNewFile();
 converter = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertFalse(new File(outputDir + "/dv/nonDVD.txt").exists());
 converter.init();
 }

 @Test
 public void testInitWithValidFiles() {
 File listDir = new File(outputDir + "/dv/");
 assertFalse(listDir.isDirectory());
 converter = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 }, new String[]{"dv1.dv", "dv2.dv"});
 assertTrue(new File(outputDir + "/dv/dv1.dv").exists());
 assertTrue(new File(outputDir + "/dv/dv2.dv").exists());
 converter.init();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitWithInvalidFormat() {
 converter = new Convert("invalid", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertTrue(converter.init());
 }

 @Test(expected = NullPointerException.class)
 public void testInitWithNullGUI() {
 converter = new Convert("dvd-ntsc", "4:3", null);
 assertFalse(converter.init());
 }

 @Test
 public void testTranscode() {
 File listDir = new File(outputDir + "/dv/");
 String[] videoFiles = listDir.list();
 assertNotNull(videoFiles);
 assertEquals(2, videoFiles.length);
 converter.transcode();
 File transcodeLog = new File(outputDir + "/log/transcode.log");
 assertTrue(transcodeLog.exists());
 }

 @Test
 public void testTranscodeWithExceptionWhenEncounteringInvalidFile() {
 File nonDVDFile = new File(outputDir + "/nonDVD.txt");
 nonDVDFile.createNewFile();
 converter = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 }, new String[]{"dv1.dv", "dv2.dv", "nonDVD.txt"});
 assertTrue(converter.init());
 String expectedErrorMessage = "Are there non-standard (not .dv) files in the dv directory?\n";
 assertTrue(converter.m_Error);
 assertEquals(expectedErrorMessage, converter.m_GUI.getMessageBoxText());
 }

 @Test
 public void testMatrix() {
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 }
}