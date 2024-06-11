import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

 private Convert convert;

 @Test
 public void testConvertConstructor() {
 Convert c = new Convert("dvd-ntsc", "1:1");
 assertEquals("dvd-ntsc", c.getFormat());
 assertEquals("1:1", c.getAspectRatio());
 }

 @Test
 public void testConvertConstructor2() {
 Convert c = new Convert(5, "dvd-pal", "16:9", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return true;
 }
 });
 assertEquals(5, c.getQuality());
 assertEquals("dvd-pal", c.getFormat());
 assertEquals("16:9", c.getAspectRatio());
 }

 @Test
 public void testInit_ThreadRunning() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return true;
 }
 });
 assertTrue(c.init());
 }

 @Test
 public void testInit_ThreadNotRunning() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 c.Transcode();
 assertTrue(c.init());
 }

 @Test
 public void testInit_ErrorOnThreadJoin() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return true;
 }
 });
 try {
 c.m_Thread.join();
 }
 catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 c.Transcode();
 assertTrue(c.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInit_NonStandardFile() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 File dir = new File("dv");
 String[] files = dir.list();
 Arrays.stream(files).forEach(fileName -> {
 if (!fileName.endsWith(".dv")) {
 c.init();
 fail("Should have thrown an exception");
 }
 });
 }

 @Test(expected = IOException.class)
 public void testTranscode_IOException() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 String[] transcode_cmd = { "/bin/sh", "-c", c.m_transcode };
 Process p = Runtime.getRuntime().exec(transcode_cmd);
 p.destroy();
 assertTrue(c.init());
 }

 @Test(expected = NullPointerException.class)
 public void testTranscode_NullPointerException() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 String[] transcode_cmd = null;
 Process p = Runtime.getRuntime().exec(transcode_cmd);
 assertTrue(c.init());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranscode_IllegalArgumentException() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 String[] transcode_cmd = { "/bin/sh", "-c", "transcode --foo" };
 Process p = Runtime.getRuntime().exec(transcode_cmd);
 assertTrue(c.init());
 }

 @Test(expected = Exception.class)
 public void testTranscode_Exception() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 String[] transcode_cmd = { "/bin/sh", "-c", "transcode --error" };
 Process p = Runtime.getRuntime().exec(transcode_cmd);
 p.destroy();
 assertFalse(c.init());
 }

 @Test
 public void testMatrix() {
 Convert c = new Convert("dvd-ntsc", "1:1", new GUI() {
 @Override
 public boolean menuChkThread_isSelected() {
 return false;
 }
 });
 try {
 FileWriter fw = new FileWriter(c.m_GUI.strOutputDir + "/matrix.txt");
 BufferedWriter wr = new BufferedWriter(fw);
 wr.write("# High resolution INTRA table\n");
 wr.write("8,16,18,20,24,25,26,30\n");
 wr.write("16,16,20,23,25,26,30,30\n");
 // Write other lines here
 wr.close();
 fw.close();
 }
 catch (IOException e) {
 throw new RuntimeException(e);
 }
 }

}