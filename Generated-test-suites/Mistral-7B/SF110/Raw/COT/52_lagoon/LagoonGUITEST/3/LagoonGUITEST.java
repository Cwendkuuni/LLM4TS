package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.Element;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setup() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 assertEquals("Lagoon", lagoonGUI.getTitle());
 assertNotNull(lagoonGUI.getInputPanel());
 assertNotNull(lagoonGUI.getCenterPanel());
 assertNotNull(lagoonGUI.getButtonPanel());
 assertNull(lagoonGUI.getProcessor());
 assertNull(lagoonGUI.getSitemapLastModified());
 }

 @Test
 public void testLoadButtonActionPerformed() throws Exception {
 lagoonGUI.loadPropertyFile();
 lagoonGUI.loadButton.dispatchEvent(new ActionEvent(lagoonGUI.loadButton, ActionEvent.ACTION_PERFORMED, null));
 // Check if loadProperties method is called
 // Check if sitemapFile value is updated
 // Check if loadButton text is changed
}

 @Test
 public void testSaveButtonActionPerformed() throws Exception {
 // Prepare property file to be saved
 Properties prop = new Properties();
 prop.setProperty("sitemapFile", "testSitemap.xml");
 prop.setProperty("sourceDir", "testSourceDir");
 prop.setProperty("targetURL", "testTargetURL");
 prop.setProperty("password", "testPassword");
 File propertyFile = new File("testPropertyFile.properties");
 try (FileOutputStream fos = new FileOutputStream(propertyFile)) {
 prop.store(fos, "Lagoon properties");
 }

 lagoonGUI.savePropertyFile();
 lagoonGUI.saveButton.dispatchEvent(new ActionEvent(lagoonGUI.saveButton, ActionEvent.ACTION_PERFORMED, null));
 // Check if saveProperties method is called
 // Check if property file is saved
 // Check if statusLabel text is updated
}

 @Test
 public void testBuildMethodNormalCase() throws Exception {
 // Set up input components with valid values
 File sm = new File("testSitemap.xml");
 sm.setLastModified(System.currentTimeMillis() - 1000L);
 lagoonGUI.sitemapFile.setValue(sm.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 lagoonGUI.password = "testPassword";

 lagoonGUI.build(false);
 // Check if build method is called
 // Check if processor is initialized
 // Check if progressArea and statusLabel texts are updated
}

 @Test(expected = FileNotFoundException.class)
 public void testBuildMethodFileNotFoundCase() throws Exception {
 lagoonGUI.sitemapFile.setValue("testSitemapFileThatDoesNotExist.xml");
 lagoonGUI.build(false);
}

 @Test(expected = IOException.class)
 public void testBuildMethodIOErrorCase() throws Exception {
 lagoonGUI.sitemapFile.setValue("testSitemapFileWithIOException.xml");
 lagoonGUI.build(false);
}

 @Test(expected = LagoonException.class)
 public void testInitAuthenticationMissingExceptionCase() throws Exception {
 lagoonGUI.password = null;
 lagoonGUI.init();
}

 @Test(expected = AuthenticationException.class)
 public void testInitAuthenticationExceptionCase() throws Exception {
 lagoonGUI.password = "testInvalidPassword";
 lagoonGUI.init();
}

 @Test
 public void testInitNormalCase() throws Exception {
 // Set up input components with valid values
 File sm = new File("testSitemap.xml");
 sm.setLastModified(System.currentTimeMillis() - 1000L);
 lagoonGUI.sitemapFile.setValue(sm.getAbsolutePath());
 lagoonGUI.sourceDir.setValue("testSourceDir");
 lagoonGUI.targetURL.setValue("testTargetURL");
 lagoonGUI.password = "testPassword";

 lagoonGUI.init();
 // Check if init method is called
 // Check if processor is initialized
 // Check if progressArea and statusLabel texts are updated
}

 @Test(expected = FileNotFoundException.class)
 public void testInitFileNotFoundCase() throws Exception {
 lagoonGUI.sitemapFile.setValue("testSitemapFileThatDoesNotExist.xml");
 lagoonGUI.init();
}

 @Test(expected = IOException.class)
 public void testInitIOErrorCase() throws Exception {
 lagoonGUI.sitemapFile.setValue("testSitemapFileWithIOException.xml");
 lagoonGUI.init();
}

 @Test
 public void testInitLagoonExceptionCase() throws Exception {
 lagoonGUI.sitemapFile.setValue("testSitemapFileWithLagoonException.xml");
 lagoonGUI.init();
}

}