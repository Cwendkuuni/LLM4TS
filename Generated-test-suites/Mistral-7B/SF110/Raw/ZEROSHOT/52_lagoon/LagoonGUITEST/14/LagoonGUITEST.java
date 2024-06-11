package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.io.File;

public class LagoonGUITest {

 private LagoonGUI lagoonGUI;

 @Before
 public void setUp() {
 lagoonGUI = new LagoonGUI();
 }

 @After
 public void tearDown() {
 lagoonGUI.exit();
 }

 @Test
 public void testMain() {
 String[] args = new String[0];
 LagoonGUI.main(args);
 assertFalse(lagoonGUI.isDisplayable());
 }

 @Test
 public void testInputComponents() {
 assertEquals("", lagoonGUI.sitemapFile.getValue());
 assertEquals(".", lagoonGUI.sourceDir.getValue());
 assertEquals(".", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testLoadProperties() {
 File propertyFile = new File("testPropertyFile.properties");
 lagoonGUI.loadProperties(propertyFile);
 assertEquals("testSitemapFile", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testSaveProperties() {
 File propertyFile = new File("testPropertyFile.properties");
 lagoonGUI.savePropertyFile();
 assertEquals("testSitemapFile", lagoonGUI.sitemapFile.getValue());
 assertEquals("testSourceDir", lagoonGUI.sourceDir.getValue());
 assertEquals("testTargetURL", lagoonGUI.targetURL.getValue());
 }

 @Test
 public void testBuild() {
 File sitemapFile = new File("testSitemapFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.buildButton.doClick();
 assertTrue(lagoonGUI.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildWithNullProcessor() {
 lagoonGUI.build(true);
 fail("Build method should not be called with null processor");
 }

 @Test
 public void testInitialize() {
 File sitemapFile = new File("testSitemapFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.init();
 assertNotNull(lagoonGUI.processor);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitializeWithMissingPassword() {
 File sitemapFile = new File("testSitemapFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.init();
 fail("Initialize method should throw AuthenticationMissingException when password is missing");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializeWithInvalidPassword() {
 File sitemapFile = new File("testSitemapFile.xml");
 lagoonGUI.sitemapFile.setValue(sitemapFile.getAbsolutePath());
 lagoonGUI.password = "wrongPassword";
 lagoonGUI.init();
 fail("Initialize method should throw AuthenticationException when password is invalid");
 }

 @Test
 public void testExit() {
 lagoonGUI.exitButton.doClick();
 assertTrue(lagoonGUI.isDisposed());
 }
}