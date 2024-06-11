import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructorWithFormat() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertEquals("dvd-ntsc", convert.getFormat());
 }

 @Test
 public void testConvertConstructorWithFormatAndQuality() {
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI());
 assertEquals(0, convert.getQuality());
 }

 @Test
 public void testConvertInitWithoutThread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(convert.init());
 }

 @Test
 public void testConvertInitWithThread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertTrue(convert.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeCmdWithNoFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.Transcode();
 }

 @Test
 public void testTranscodeCmdWithValidFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File dir = new File("dvd");
 File[] files = dir.listFiles();
 for (int i = 0; i < files.length; i++) {
 convert.video_files = new String[files.length];
 convert.video_files[i] = files[i].getName();
 convert.Transcode();
 }
 }

 @Test(expected = IOException.class)
 public void testTranscodeProcessError() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 final String[] transcode_cmd = { "/bin/sh", "-c", convert.m_transcode };
 Process p = Runtime.getRuntime().exec(transcode_cmd, null, new File("."));
 }

 @Test
 public void testInitWithInvalidTranscodePath() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 convert.m_transcode = "invalid_transcode_path";
 assertFalse(convert.init());
 }

 @Test
 public void testInitWithValidTranscodePath() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File transcodePath = new File("valid_transcode_path");
 assertTrue(new File(transcodePath, "transcode").exists());
 convert.m_transcode = "/bin/sh -c " + transcodePath.getAbsolutePath() + "/transcode ...";
 assertTrue(convert.init());
 }

 @Test
 public void testMatrixFileCreation() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 File matrixFile = new File(convert.m_GUI.strOutputDir, "matrix.txt");
 assertTrue(matrixFile.exists());
 }

}