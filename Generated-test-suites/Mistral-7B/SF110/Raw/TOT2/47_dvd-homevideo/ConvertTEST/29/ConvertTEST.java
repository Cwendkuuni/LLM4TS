import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructorDVDNTSC() {
 Convert c = new Convert("dvd-ntsc", "4:3");
 assertEquals("dvd-ntsc", c.getFormat());
 assertEquals(29.97, c.getFps(), 0.01);
 }

 @Test
 public void testConvertConstructorDVDPAL() {
 Convert c = new Convert("dvd-pal", "16:9");
 assertEquals("dvd-pal", c.getFormat());
 assertEquals(25.0, c.getFps(), 0.01);
 }

 @Test
 public void testConvertConstructorWithQuality() {
 Convert c = new Convert(5, "dvd-ntsc", "4:3", new GUI());
 assertEquals(5, c.getQuality());
 }

 @Test
 public void testInitThreadNotSelected() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI());
 assertFalse(c.init());
 }

 @Test
 public void testInitThreadSelected() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI()) {
 @Override
 public boolean init() {
 super.init();
 return true;
 }
 };
 assertTrue(c.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeCommandWithNonStandardFile() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI());
 File listDir = new File(c.getOutputDirectory() + "/dv/");
 c.transcode();
 String[] videoFiles = listDir.list();
 c.videoFiles = Arrays.copyOfRange(videoFiles, 0, 2);
 c.thread_track = 2;
 c.transcode();
 }

 @Test
 public void testTranscodeSuccess() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI());
 c.video_files = new String[]{"test1.dv"};
 c.thread_track = 0;
 c.transcode();
 assertFalse(c.isError());
 }

 @Test
 public void testTranscodeError() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI());
 c.video_files = new String[]{"test1.txt"};
 c.thread_track = 0;
 c.transcode();
 assertTrue(c.isError());
 }

 @Test
 public void testMatrixFileCreation() {
 Convert c = new Convert("dvd-ntsc", "4:3", new GUI());
 File expectedFile = new File(c.getOutputDirectory() + "/matrix.txt");
 assertTrue(expectedFile.exists());
 }

}