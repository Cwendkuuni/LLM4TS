import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {
 private static Newzgrabber newzgrabber;
 private static final String expectedBatchFilename = "batch.ini";
 private static final String expectedNewzdirectory = "path/to/newzdirectory";
 private static final String expectedJarFilename = "Newz.jar";

 @BeforeClass
 public static void setUpClass() {
 Newzgrabber.verbose = false;
 Newzgrabber.debug = false;
 Newzgrabber.batch = false;
 Newzgrabber.IsBatch = false;
 Newzgrabber.JarFilename = expectedJarFilename;
 Newzgrabber.BatchFilename = expectedBatchFilename;
 Newzgrabber.NewzInifilename = "Newzgrabber.ini";
 newzgrabber = new Newzgrabber("Newzgrabber");
 }

 @AfterClass
 public static void tearDownClass() {
 newzgrabber.windowClosing(null);
 }

 @Test
 public void testNewzgrabberConstruction() {
 assertEquals("Newzgrabber", newzgrabber.getTitle());
 assertNotNull(Newzgrabber.jtp);
 assertNotNull(Newzgrabber.optionspanel);
 assertNotNull(Newzgrabber.groupspanel);
 assertNotNull(Newzgrabber.searchpanel);
 assertNotNull(Newzgrabber.transferpanel);
 }

 @Test
 public void testNewzgrabberBatchMode() {
 Newzgrabber.IsBatch = true;
 Newzgrabber newzgrabberBatch = new Newzgrabber("");
 assertNotNull(newzgrabberBatch.getBatchDirectory());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewzgrabberInvalidBatchMode() {
 Newzgrabber.IsBatch = true;
 Newzgrabber newzgrabberInvalid = new Newzgrabber("Invalid Argument");
 }

 @Test
 public void testFindDirectory() {
 String actualDirectory = Newzgrabber.findDirectory(expectedJarFilename);
 assertEquals(expectedNewzdirectory, actualDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testFindDirectoryNullPath() {
 String actualDirectory = Newzgrabber.findDirectory(null);
 }
}