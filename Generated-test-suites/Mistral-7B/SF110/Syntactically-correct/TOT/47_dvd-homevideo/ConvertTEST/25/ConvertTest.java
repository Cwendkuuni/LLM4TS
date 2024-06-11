import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

 private Convert convert;
 private String[] videoFiles = new String[]{"test1.dv", "test2.dv"};
 private File directory;

 @Before
 public void setUp() {
 directory = new File("testOutputDir");
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return false;
 }
 }, directory);
 }

 @Test
 public void testInitWithNoThreadCheck() {
 convert.init();
 assertFalse(convert.m_Error);
 }

 @Test
 public void testInitWithThreadCheck() {
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return true;
 }
 }, directory);
 convert.init();
 assertFalse(convert.m_Error);

 try {
 convert.m_Thread.join();
 } catch (InterruptedException e) {
 fail("Thread did not join correctly");
 }
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithNonStandardFileInDVDDirectory() {
 File incorrectFile = new File("testOutputDir/nonStandardFile.mp4");
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return false;
 }
 }, directory);
 directory.listFiles()[0] = incorrectFile;
 convert.init();
 }

 @Test
 public void testTranscodeWithValidInput() {
 convert.Transcode();
 assertTrue(new File(directory, "test1.vob").exists());
 assertTrue(new File(directory, "test1.ac3").exists());
 }

 @Test(expected = IOException.class)
 public void testTranscodeWithIOException() {
 convert = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThreadIsSelected() {
 return true;
 }
 }, directory);
 final String transcodeCmd = convert.m_transcode;
 final String mplexCmd = convert.mplex;
 final String[] transcodeCmdArr = transcodeCmd.split(" ");
 final String[] mplexCmdArr = mplexCmd.split(" ");
 transcodeCmdArr[0] = "echo";
 mplexCmdArr[0] = "echo";
 try {
 Process p1 = Runtime.getRuntime().exec(transcodeCmdArr);
 Process p2 = Runtime.getRuntime().exec(mplexCmdArr);
 p1.waitFor();
 p2.waitFor();
 fail("IO Expected but no error occurred");
 } catch (IOException e) {
 //Expected
 } catch (InterruptedException e) {
 fail("Threads did not wait correctly");
 }
 }
}