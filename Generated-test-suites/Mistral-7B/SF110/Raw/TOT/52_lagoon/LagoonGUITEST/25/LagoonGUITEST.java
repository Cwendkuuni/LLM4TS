package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonException;

import java.awt.event.ActionEvent;
import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

 private LagoonGUI gui;

 @Before
 public void setUp() {
 gui = new LagoonGUI();
 gui.addWindowListener(new WindowAdapter() {
 @Override
 public void windowClosed(WindowEvent e) {
 gui = null;
 }
 });
 gui.show();
 }

 @Test
 public void testInitialize() throws LagoonException, IOException {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 assertEquals("Initialized successfully", gui.statusLabel.getText());
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithInvalidSitemap() {
 File sitemapFile = new File("src/test/resources/testSitemapInvalid.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 fail("Should have thrown LagoonException for invalid sitemap");
 }

 @Test(expected = IOException.class)
 public void testInitializeWithMissingSourceDir() {
 gui.sitemapFile.setValue("src/test/resources/testSitemap.xml");
 gui.sourceDir.setValue("nonexistentDir");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 fail("Should have thrown IOException for missing source dir");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializeWithNullTargetURL() {
 gui.sitemapFile.setValue("src/test/resources/testSitemap.xml");
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue(null);
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 fail("Should have thrown IllegalArgumentException for null target URL");
 }

 @Test
 public void testBuild() throws LagoonException, IOException {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 assertEquals("Building...", gui.statusLabel.getText());
 }

 @Test
 public void testBuildWithSuccess() throws LagoonException, IOException {
 File sitemapFile = new File("src/test/resources/testSitemap.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 String expectedOutput = "--- Start build ---\n" +
 "Building...\n" +
 "--- Build finished successfully 11 s ---";
 assertEquals(expectedOutput, gui.progressArea.getText().trim());
 }

 @Test(expected = IOException.class)
 public void testBuildWithFileNotFoundException() throws LagoonException, IOException {
 File sitemapFile = new File("src/test/resources/testSitemapInvalid.xml");
 gui.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.loadButton.doClick();
 gui.buildButton.doClick();
 fail("Should have thrown IOException for FileNotFoundException");
 }

 @Test(expected = AuthenticationException.class)
 public void testBuildWithInvalidPassword() throws LagoonException, IOException {
 gui.sitemapFile.setValue("src/test/resources/testSitemap.xml");
 gui.sourceDir.setValue("src/test/resources/testSource");
 gui.targetURL.setValue("http://test.com");
 gui.password = "invalidPassword";
 gui.buildButton.doClick();
 fail("Should have thrown AuthenticationException for invalid password");
 }

 @Test
 public void testExit() {
 gui.exitButton.doClick();
 assertNull(gui);
 }
}