package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 }

 @Test
 public void testInitialization() {
 assertTrue(appFrame.getTitle().equals("Lagoon"));
 assertNotNull(appFrame.getInputPanel());
 assertNotNull(appFrame.getCenterPanel());
 assertNotNull(appFrame.getButtonPanel());
 assertNull(appFrame.getProcessor());
 assertNull(appFrame.getStatusLabel().getText());
 }

 @Test
 public void testLoadProperties() throws Exception {
 final File propertyFile = new File("testConfig.properties");
 appFrame.loadProperties(propertyFile);
 assertEquals("testSitemapFile", appFrame.getSitemapFile().getValue());
 assertEquals("testSourceDir", appFrame.getSourceDir().getValue());
 assertEquals("testTargetURL", appFrame.getTargetURL().getValue());
 assertEquals("testPassword", appFrame.getPassword());
 }

 @Test(expected = LagoonException.class)
 public void testLoadPropertiesWithMissingProperty() throws Exception {
 final File propertyFile = new File("testConfigMissingProperty.properties");
 appFrame.loadProperties(propertyFile);
 fail("Should have thrown LagoonException for missing property");
 }

 @Test
 public void testSaveProperties() {
 final File propertyFile = new File("testSavePropertyFile.properties");
 appFrame.savePropertyFile();
 assertTrue(propertyFile.exists());
 }

 @Test
 public void testBuild() {
 appFrame.build(false);
 assertTrue(appFrame.getStatusLabel().getText().contains("Building..."));
 }

 @Test(expected = IOException.class)
 public void testBuildWithFileNotFoundException() {
 appFrame.build(true);
 fail("Should have thrown IOException for FileNotFoundException");
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithLagoonException() {
 appFrame.init();
 appFrame.build(true);
 fail("Should have thrown LagoonException");
 }

 @Test
 public void testExit() {
 appFrame.exit();
 assertFalse(appFrame.isDisplayable());
 }

 @Test
 public void testWindowEvents() {
 appFrame.addWindowListener(e -> {
 switch (e.getID()) {
 case WindowEvent.WINDOW_OPENED:
 assertEquals("Lagoon", appFrame.getTitle());
 break;
 case WindowEvent.WINDOW_CLOSING:
 assertTrue(appFrame.isShowing());
 appFrame.dispose();
 break;
 default:
 fail("Unhandled window event: " + e.getID());
 }
 });
 appFrame.show();
 appFrame.dispatchEvent(new WindowEvent(appFrame, WindowEvent.WINDOW_CLOSING));
 }
}