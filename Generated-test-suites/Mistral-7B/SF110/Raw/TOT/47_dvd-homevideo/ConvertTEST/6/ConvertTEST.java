import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;

public class ConvertTest {

 private Convert convert;
 private String outputDir;

 @Before
 public void setUp() {
 outputDir = "test_output";
 File dir = new File(outputDir);
 dir.mkdirs();
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 convert.init();
 }

 @After
 public void tearDown() {
 File dir = new File(outputDir);
 File[] files = dir.listFiles();
 for (File file : files) {
 file.delete();
 }
 dir.delete();
 }

 @Test
 public void testInitWithDvdNtscFormat() {
 assertTrue(convert.init());
 }

 @Test
 public void testInitWithDvdPalFormat() {
 Convert convertPal = new Convert(0, "dvd-pal", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertTrue(convertPal.init());
 }

 @Test(expected = RuntimeException.class)
 public void testInitWithInvalidFormat() {
 Convert convertInvalidFormat = new Convert(0, "invalid_format", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertFalse(convertInvalidFormat.init());
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testInitWithNonStandardFileInDvDirectory() {
 File testFile = new File(outputDir + "/dv/non_standard_file.mp4");
 testFile.createNewFile();
 Convert convertNonStandard = new Convert(0, "dvd-ntsc", "4:3", new GUI() {
 @Override
 public boolean menuChkThread() {
 return false;
 }
 });
 assertFalse(convertNonStandard.init());
 testFile.delete();
 }
}