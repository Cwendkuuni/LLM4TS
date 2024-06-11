import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructorWithFormatAndAspectRatio() {
 Convert testConvert = new Convert("dvd-ntsc", "4:3");
 assertEquals("dvd-ntsc", testConvert.getFormat());
 assertEquals("4:3", testConvert.getAspectRatio());
 }

 @Test
 public void testConvertConstructorWithQualityFormatAndAspectRatio() {
 Convert testConvert = new Convert(1, "dvd-ntsc", "4:3", null);
 assertEquals(1, testConvert.getQuality());
 assertEquals("dvd-ntsc", testConvert.getFormat());
 assertEquals("4:3", testConvert.getAspectRatio());
 }

 @Test
 public void testInitMethodWhenThreadIsSelected() throws InterruptedException, IOException {
 Convert testConvert = new Convert("dvd-ntsc", "4:3", null);
 testConvert.init();
 assertTrue(testConvert.isError());

 File outputDir = new File("outputDir");
 File logTranscode = new File(outputDir, "log/transcode.log");
 File logMplex = new File(outputDir, "log/mplex.log");
 assertFalse(logTranscode.exists());
 assertFalse(logMplex.exists());
 }

 @Test
 public void testInitMethodWhenThreadIsNotSelected() throws InterruptedException, IOException {
 Convert testConvert = new Convert("dvd-ntsc", "4:3", null);
 testConvert.init();
 assertFalse(testConvert.isError());
 File outputDir = new File("outputDir");
 File logTranscode = new File(outputDir, "log/transcode.log");
 File logMplex = new File(outputDir, "log/mplex.log");
 assertTrue(logTranscode.exists());
 assertTrue(logMplex.exists());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeMethodWithNonStandardFilesInDvDirectory() {
 Convert testConvert = new Convert("dvd-ntsc", "4:3", null);
 testConvert.transcode();
 }

 @Test
 public void testTranscodeMethod() throws IOException, InterruptedException {
 Convert testConvert = new Convert("dvd-ntsc", "4:3", null);
 testConvert.transcode();
 File outputDir = new File("outputDir");
 File logTranscode = new File(outputDir, "log/transcode.log");
 File logMplex = new File(outputDir, "log/mplex.log");
 assertTrue(logTranscode.exists());
 assertTrue(logMplex.exists());
 }

 @Test
 public void testMatrixMethod() throws IOException {
 Convert testConvert = new Convert("dvd-ntsc", "4:3", null);
 testConvert.matrix();
 File matrixFile = new File(testConvert.getStrOutputDir(), "matrix.txt");
 assertTrue(matrixFile.exists());
 FileReader fileReader = new FileReader(matrixFile);
 BufferedReader bufferedReader = new BufferedReader(fileReader);
 String line;
 String expectedLine = "# High resolution INTRA table\n";
 assertEquals(expectedLine, line = bufferedReader.readLine());
 }

}