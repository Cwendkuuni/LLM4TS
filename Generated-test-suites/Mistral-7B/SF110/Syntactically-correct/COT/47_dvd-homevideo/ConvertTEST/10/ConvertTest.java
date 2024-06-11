import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "output_directory";
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 }

 @Test
 public void testConvertConstructorWithFormatAndAspectRatio() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI());
 assertNotNull(convert);
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 }

 @Test
 public void testConvertConstructorWithQualityFormatAndAspectRatio() {
 convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
 assertNotNull(convert);
 assertEquals(1, convert.getQuality());
 assertEquals("dvd-ntsc", convert.getFormat());
 assertEquals("4:3", convert.getAspectRatio());
 }

 @Test
 public void testInitWithThread() {
 convert.init();
 assertTrue(convert.isThreadRunning());
 try {
 convert.m_Thread.join();
 }
 catch (InterruptedException e) {
 fail("Thread should not be interrupted");
 }
 assertFalse(convert.isThreadRunning());
}

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithEmptyDir() {
 File dir = new File(outputDir);
 dir.delete();
 convert = new Convert(0, "dvd-ntsc", "4:3", new GUI());
}

 @Test
 public void testTranscode() {
 convert.Transcode();
 File dir = new File(outputDir + "/dv/");
 String[] files = dir.list();
 assertEquals(files.length, 1);
 String fileName = files[0];
 File file = new File(outputDir + "/dv/" + fileName);
 assertTrue(file.isFile());
 assertTrue(file.length() > 0);
}

 @Test
 public void testMatrix() {
 convert.matrix();
 File file = new File(outputDir + "/matrix.txt");
 assertTrue(file.exists());
 FileReader reader = new FileReader(file);
 BufferedReader br = new BufferedReader(reader);
 String line;
 String expectedLine = "# High resolution INTRA table\n";
 assertEquals(expectedLine, line = br.readLine());
 String[] lines = new String[8];
 int i = 0;
 while ((line = br.readLine()) != null) {
 lines[i++] = line;
 }
 assertArrayEquals(Arrays.asList(lines).subList(0, 8).toArray(), new String[]{"8,16,18,20,24,25,26,30", "16,16,20,23,25,26,30,30", "18,20,22,24,26,28,29,31", "20,21,23,24,26,28,31,31", "21,23,24,25,28,30,30,33", "23,24,25,28,30,30,33,36", "24,25,26,29,29,31,34,38", "25,26,28,29,31,34,38,42"});
 br.close();
 reader.close();
 file.delete();
}

 @Test(expected = IOException.class)
 public void testMatrixIOException() {
 try {
 convert.matrix();
 fail("Matrix creation should fail with IO exception");
 }
 catch (IOException e) {
 // expected
 }
}

}