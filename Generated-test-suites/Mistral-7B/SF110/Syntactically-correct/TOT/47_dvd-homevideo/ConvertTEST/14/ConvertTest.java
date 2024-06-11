import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 this.outputDir = "target/test-output";
 this.convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 this.convert.init();
 }

 @Test
 public void testInitWithMenuChkThreadFalse() {
 assertTrue(this.convert.init());
 assertTrue(new File(this.outputDir + "/log/transcode.log").exists());
 assertTrue(new File(this.outputDir + "/log/mplex.log").exists());
 }

 @Test
 public void testInitWithMenuChkThreadTrue() {
 this.convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 });
 this.convert.init();
 assertFalse(this.convert.init());
 assertFalse(new File(this.outputDir + "/log/transcode.log").exists());
 assertFalse(new File(this.outputDir + "/log/mplex.log").exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithNonStandardFileInDvDirectory() {
 this.convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 }, new String[]{"non_standard_file.ext"});
 this.convert.init();
 }

 @Test
 public void testTranscode() {
 this.convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 this.convert.init();
 String[] videoFiles = new File(this.outputDir + "/dv/").list();
 Arrays.sort(videoFiles);
 String expectedTranscodeCommand = this.convert.m_transcode.replaceAll("inp", videoFiles[0]).replaceAll("test1", videoFiles[0].substring(0, 11));
 String actualTranscodeCommand = this.convert.transcodeCommand();
 assertEquals(expectedTranscodeCommand, actualTranscodeCommand);
 }
}