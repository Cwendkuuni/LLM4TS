package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import nu.staldal.lagoon.*;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @Test
 public void testConstructor() {
 assertEquals("Lagoon", lagoonGUI.getTitle());
 assertNotNull(lagoonGUI.getInputPanel());
 assertNotNull(lagoonGUI.getCenterPanel());
 assertNotNull(lagoonGUI.getButtonPanel());
 assertNull(lagoonGUI.getProcessor());
 assertEquals(0L, lagoonGUI.getSitemapLastModified());
 }

 @Test
 public void testLoadButtonListener() {
 final File file = new File("test.properties");
 lagoonGUI.loadButton.doClick();
 assertTrue(lagoonGUI.isPropertyFileLoaded(file));
}

 @Test (expected = IllegalArgumentException.class)
 public void testLoadButtonListenerWithNullFile() {
 lagoonGUI.loadButton.doClick();
 File nullFile = null;
 assertTrue(lagoonGUI.isPropertyFileLoaded(nullFile));
}

 @Test
 public void testSaveButtonListener() {
 final File file = new File("test.properties");
 lagoonGUI.saveButton.doClick();
 assertTrue(lagoonGUI.isPropertyFileSaved(file));
}

 @Test (expected = IllegalArgumentException.class)
 public void testSaveButtonListenerWithNullFile() {
 lagoonGUI.saveButton.doClick();
 File nullFile = null;
 assertTrue(lagoonGUI.isPropertyFileSaved(nullFile));
}

 @Test
 public void testBuildButtonListener() {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.buildButton.doClick();
 assertNotNull(lagoonGUI.getProcessor());
}

 @Test (expected = IllegalArgumentException.class)
 public void testBuildButtonListenerWithNullSitemapFile() {
 lagoonGUI.sitemapFile.setValue(null);
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.buildButton.doClick();
 assertNull(lagoonGUI.getProcessor());
}

 @Test
 public void testBuildButtonListenerWithInvalidSitemapFile() {
 lagoonGUI.sitemapFile.setValue("test.txt");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.buildButton.doClick();
 assertNull(lagoonGUI.getProcessor());
}

 @Test
 public void testBuildButtonListenerWithForceTrue() {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.forceButton.doClick();
 assertNotNull(lagoonGUI.getProcessor());
}

 @Test
 public void testForceButtonListenerWithForceFalse() {
 lagoonGUI.sitemapFile.setValue("test.xml");
 lagoonGUI.sourceDir.setValue("src");
 lagoonGUI.targetURL.setValue("http://localhost:8080");
 lagoonGUI.buildButton.doClick();
 assertNull(lagoonGUI.getProcessor());
}

 @Test
 public void testExitButtonListener() {
 lagoonGUI.exitButton.doClick();
 assertFalse(lagoonGUI.isVisible());
}
}