import org.junit.After;
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
 outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdir();
 }

 @After
 public void tearDown() {
 File dir = new File(outputDir);
 dir.delete();
 }

 @Test
 public void testConvertConstructorDVDNTSC() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertEquals(29.97, convert.m_fps, 0.01);
 }

 @Test
 public void testConvertConstructorDVDEurope() {
 convert = new Convert("dvd-europe", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertEquals(25.0, convert.m_fps, 0.01);
 }

 @Test
 public void testConvertConstructorQualityAndFormat() {
 convert = new Convert(1, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertEquals(1, convert.m_Quality);
 assertEquals("dvd-ntsc", convert.m_Format);
 assertEquals("4:3", convert.m_AspectRatio);
}

 @Test
 public void testInitNoThread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 }, 0);
 assertFalse(convert.init());
}

 @Test
 public void testInitThread() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 }, 1);
 assertTrue(convert.init());
}

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testTranscodeCommandWithNullVideoFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 }, 1);
 convert.init();
 File dir = new File(outputDir + "/dv/");
 String[] videoFiles = dir.list();
 String transcodeCommand = convert.m_transcode;
 transcodeCommand = transcodeCommand.replaceAll("test1", videoFiles[0]);
 try {
 Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", transcodeCommand}, null, new File(outputDir));
 fail("Should have thrown StringIndexOutOfBoundsException");
 } catch (IOException e) {
 // Expected
 }
}

 @Test
 public void testTranscodeCommandWithValidVideoFiles() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 }, 1);
 convert.init();
 File dir = new File(outputDir + "/dv/");
 String[] videoFiles = dir.list();
 String transcodeCommand = convert.m_transcode;
 transcodeCommand = transcodeCommand.replaceAll("test1", videoFiles[0]);
 Process p;
 try {
 p = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", transcodeCommand}, null, new File(outputDir));
 } catch (IOException e) {
 fail("Should have been able to execute command");
 }
}

 @Test
 public void testMatrixFileCreation() {
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {return outputDir;}
 }, 1);
 convert.init();
 File matrixFile = new File(outputDir + "/matrix.txt");
 assertTrue(matrixFile.exists());
}

}