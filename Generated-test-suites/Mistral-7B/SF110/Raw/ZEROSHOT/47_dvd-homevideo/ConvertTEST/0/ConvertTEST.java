import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String[] videoFiles;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "testOutput";
 File outputFolder = new File(outputDir);
 outputFolder.mkdir();
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 }

 @After
 public void tearDown() {
 File outputFolder = new File(outputDir);
 outputFolder.delete();
 }

 @Test
 public void testInitWithDVDNTSCFormatAnd16_9AspectRatio() {
 assertTrue(convert.init());
 assertEquals(29.97, convert.m_fps, 0.01);
 }

 @Test
 public void testInitWithCustomQualityDVDNTSCFormatAnd16_9AspectRatio() {
 int quality = 2;
 convert = new Convert(quality, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertTrue(convert.init());
 assertEquals(29.97, convert.m_fps, 0.01);
 }

 @Test
 public void testInitWithCustomQualityNonDVDNTSCFormatAnd16_9AspectRatio() {
 int quality = 2;
 convert = new Convert(quality, "pal", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertFalse(convert.init());
 }

 @Test
 public void testTranscodeMethodWithOneFile() {
 convert.video_files = new String[]{"testFile1.dv"};
 convert.transcode();
 File transcodeLogFile = new File(outputDir + "/log/transcode.log");
 assertTrue(transcodeLogFile.exists());
 }

 @Test
 public void testTranscodeMethodWithMultipleFiles() {
 convert.video_files = new String[]{"testFile1.dv", "testFile2.dv"};
 convert.thread_track = 0;
 convert.transcode();
 File transcodeLogFile1 = new File(outputDir + "/log/transcode.log");
 assertTrue(transcodeLogFile1.exists());
 convert.thread_track++;
 convert.transcode();
 File transcodeLogFile2 = new File(outputDir + "/log/transcode.log");
 assertTrue(transcodeLogFile2.exists());
 assertNotSame(transcodeLogFile1.getAbsolutePath(), transcodeLogFile2.getAbsolutePath());
 }

 @Test
 public void testMatrixMethod() {
 convert = new Convert("dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
 File[] lines = matrixFile.listFiles(new FilenameFilter() {
 @Override
 public boolean accept(File file, String name) {
 return name.startsWith("#") || name.length() == 0;
 }
 });
 assertEquals(2, lines.length);
 File highResolutionINTRATable = lines[0];
 String[] highResolutionINTRATableLines = highResolutionINTRATable.list(null);
 assertEquals(8, highResolutionINTRATableLines.length);
 Arrays.stream(highResolutionINTRATableLines).mapToInt(Integer::parseInt).forEach(i -> {
 int expectedIndex = i - 1;
 int actualIndex = Arrays.asList(highResolutionINTRATableLines[1].split(",")).indexOf(String.valueOf(expectedIndex + 1));
 assertEquals(expectedIndex, actualIndex);
 });
 File tmpegEncNONINTRATable = lines[1];
 String[] tmpegEncNONINTRATableLines = tmpegEncNONINTRATable.list(null);
 assertEquals(16, tmpegEncNONINTRATableLines.length);
 }
}