import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testInitialization() {
 assertNotNull(appFrame.getInputPanel());
 assertNotNull(appFrame.getButtonPanel());
 assertNotNull(appFrame.getCenterPanel());
 assertNotNull(appFrame.getStatusLabel());
 assertNotNull(appFrame.getProgressArea());
 assertNull(appFrame.getProcessor());
 assertEqual(0L, appFrame.getsitemapLastModified());
 }

 @Test
 public void testLoadingProperties() {
 final File propertyFile = new File("testProperties.txt");
 appFrame.loadProperties(propertyFile);
 assertTrue(appFrame.getSitemapFile().getValue().contains("testSitemapFile"));
 assertTrue(appFrame.getSourceDir().getValue().contains("testSourceDir"));
 assertTrue(appFrame.getTargetURL().getValue().contains("testTargetURL"));
 assertNull(appFrame.getPassword());
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadingInvalidPropertyFile() {
 final File propertyFile = new File("invalidFile.txt");
 appFrame.loadProperties(propertyFile);
 fail("FileNotFoundException should be thrown");
 }

 @Test
 public void testBuildingWithValidSitemapFile() {
 appFrame.sitemapFile.setValue("testSitemapFile.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.build(false);
 assertEquals("--- Build finished successfully ---", appFrame.getProgressArea().getText());
 }

 @Test(expected = IOException.class)
 public void testBuildingWithInvalidSitemapFile() {
 appFrame.sitemapFile.setValue("invalidFile.xml");
 appFrame.sourceDir.setValue("testSourceDir");
 appFrame.targetURL.setValue("testTargetURL");
 appFrame.build(false);
 fail("IOException should be thrown");
 }

 @Test(expected = LagoonException.class)
 public void testBuildingWithNullProcessor() {
 appFrame.processor = null;
 appFrame.build(false);
 fail("LagoonException should be thrown");
 }

 @Test
 public void testExit() {
 appFrame.exit();
 assertTrue(appFrame.isDisplayable()); // Check if the frame is still displayed before System.exit()
 System.exit(0);
 // Frame should no longer be displayable after System.exit()
 assertFalse(appFrame.isDisplayable());
 }
}