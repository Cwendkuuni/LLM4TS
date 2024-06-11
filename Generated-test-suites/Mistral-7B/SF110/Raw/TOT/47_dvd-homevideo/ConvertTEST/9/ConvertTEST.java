import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class ConvertTest {

 private Convert convert;
 private String[] expectedVideoFiles;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output";
 File outputDirectory = new File(outputDir);
 outputDirectory.mkdirs();
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 }, outputDirectory);
 expectedVideoFiles = new String[]{"test1.dv", "test2.dv", "test3.dv"};
 }

 @Test
 public void testInitWithNoThread() {
 convert.init();
 File ListDir = new File(outputDir);
 String[] actualVideoFiles = ListDir.list();
 assertArrayEquals(expectedVideoFiles, actualVideoFiles);
 }

 @Test
 public void testInitWithThread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, outputDirectory);
 convert.init();
 try {
 convert.m_Thread.join();
 } catch (InterruptedException e) {
 fail("Thread should not be interrupted during join()");
 }
 File ListDir = new File(outputDir);
 String[] actualVideoFiles = ListDir.list();
 assertArrayEquals(expectedVideoFiles, actualVideoFiles);
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithNonStandardFiles() {
 File nonStandardFile = new File(outputDir + "/nonStandardFile.txt");
 nonStandardFile.createNewFile();
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 }, outputDirectory);
 convert.init();
 }

 @Test
 public void testTranscodeCommand() {
 String transcodeCommand = convert.getTranscodeCommand();
 assertTrue(transcodeCommand.contains("transcode"));
 assertTrue(transcodeCommand.contains("--export_asr aspectRatio"));
 assertTrue(transcodeCommand.contains("--export_fps frames/s"));
 }

 @Test
 public void testMatrixFileCreation() {
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 File expectedMatrixFile = new File("test_resources/matrix.txt");
 assertTrue(new File(outputDir).equals(expectedMatrixFile.getParentFile()));
 }

 @Test
 public void testTranscodeFailure() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return true;
 }
 }, outputDirectory);
 convert.init();
 File transcodeLogFile = new File(outputDir + "/log/transcode.log");
 File mplexLogFile = new File(outputDir + "/log/mplex.log");
 convert.Transcode();
 assertTrue(transcodeLogFile.exists());
 assertTrue(mplexLogFile.exists());
 String transcodeLogContent = readFileContent(transcodeLogFile);
 String mplexLogContent = readFileContent(mplexLogFile);
 assertTrue(transcodeLogContent.contains("encoding"));
 assertTrue(transcodeLogContent.contains("inp.ac3"));
 assertTrue(transcodeLogContent.contains("mpeg2enc,raw"));
 assertTrue(mplexLogContent.contains("inp.vob"));
 assertTrue(mplexLogContent.contains("inp.m2v"));
 assertTrue(mplexLogContent.contains("inp.ac3"));
 }

 private String readFileContent(File file) {
 StringBuilder contentBuilder = new StringBuilder();
 try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
 String line;
 while ((line = reader.readLine()) != null) {
 contentBuilder.append(line).append("\n");
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 return contentBuilder.toString();
 }
}