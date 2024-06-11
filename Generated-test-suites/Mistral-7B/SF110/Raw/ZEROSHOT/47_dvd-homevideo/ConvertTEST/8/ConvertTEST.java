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
 if (!dir.exists()) {
 dir.mkdir();
 }
 convert = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 convert.init();
 }

 @After
 public void tearDown() {
 File dir = new File(outputDir);
 Arrays.stream(dir.listFiles()).forEach(File::deleteOrFail);
 dir.delete();
 }

 @Test
 public void testInitWithDVDNTSC() {
 assertTrue(convert.init());
 }

 @Test
 public void testInitWithDVDPAL() {
 Convert convertPAL = new Convert(0, "dvd-pal", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertTrue(convertPAL.init());
 }

 @Test
 public void testInitWithCustomQualityAndFormat() {
 Convert convertCustom = new Convert(5, "dvd-ntsc", "16:9", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertTrue(convertCustom.init());
 }

 @Test(expected = AssertionError.class)
 public void testInitWithInvalidFormat() {
 Convert invalidFormat = new Convert("invalid", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return outputDir;
 }
 });
 assertFalse(invalidFormat.init());
 }

 @Test(expected = AssertionError.class)
 public void testInitWithNonExistentDirectory() {
 Convert nonExistentDir = new Convert("dvd-ntsc", "4:3", new GUI() {
 @Override
 public String strOutputDir() {
 return "non_existent_directory";
 }
 });
 assertFalse(nonExistentDir.init());
 }
}