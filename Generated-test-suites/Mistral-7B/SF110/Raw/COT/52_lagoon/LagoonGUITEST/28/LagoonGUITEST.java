package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.XMLInputFactory;

public class LagoonGUITest {

 private LagoonGUI appFrame;

 @Before
 public void setUp() {
 appFrame = new LagoonGUI();
 appFrame.show();
 }

 @After
 public void tearDown() {
 appFrame.exit();
 }

 @Test
 public void testConstructor() {
 LagoonGUI gui = new LagoonGUI();
 assertNotNull(gui);
 }

 @Test
 public void testLoadProperties() {
 appFrame.loadPropertyFile();
 Properties props = new Properties();
 appFrame.loadProperties(new File("test.properties"));
 assertEquals(appFrame.sitemapFile.getValue(), props.getProperty("sitemapFile"));
 assertEquals(appFrame.sourceDir.getValue(), props.getProperty("sourceDir"));
 assertEquals(appFrame.targetURL.getValue(), props.getProperty("targetURL"));
 }

 @Test
 public void testSaveProperties() {
 File propertyFile = new File("test.properties");
 appFrame.savePropertyFile();
 Properties props = new Properties();
 appFrame.loadProperties(propertyFile);
 assertEquals(appFrame.sitemapFile.getValue(), props.getProperty("sitemapFile"));
 assertEquals(appFrame.sourceDir.getValue(), props.getProperty("sourceDir"));
 assertEquals(appFrame.targetURL.getValue(), props.getProperty("targetURL"));
 }

 @Test
 public void testBuild() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.loadButton.doClick();
 appFrame.buildButton.doClick();
 assertTrue(appFrame.statusLabel.getText().contains("Building..."));
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithNullSitemap() {
 appFrame.sitemapFile.setValue(null);
 appFrame.buildButton.doClick();
 fail("Build should have thrown LagoonException");
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithEmptySitemap() {
 appFrame.sitemapFile.setValue("");
 appFrame.buildButton.doClick();
 fail("Build should have thrown LagoonException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testBuildWithNonExistentSourceDir() {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("nonexistent");
 appFrame.buildButton.doClick();
 fail("Build should have thrown FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testBuildWithIOException() throws IOException {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 final PrintStream captureOut = new PrintStream(new ByteArrayOutputStream());
 final PrintStream captureErr = new PrintStream(new ByteArrayOutputStream());
 System.setOut(captureOut);
 System.setErr(captureErr);
 appFrame.buildButton.doClick();
 System.setOut(System.out);
 System.setErr(System.err);
 assertTrue(captureOut.toString().contains("Building..."));
 final String errMsg = captureErr.toString();
 assertTrue(errMsg.contains("IOException"));
 }

 @Test(expected = LagoonException.class)
 public void testInitWithNullSitemap() {
 appFrame.sitemapFile.setValue(null);
 appFrame.init();
 fail("Init should have thrown LagoonException");
 }

 @Test(expected = LagoonException.class)
 public void testInitWithEmptySitemap() {
 appFrame.sitemapFile.setValue("");
 appFrame.init();
 fail("Init should have thrown LagoonException");
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitWithMissingPassword() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("test.xml");appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.password = null;
 appFrame.init();
 fail("Init should have thrown AuthenticationMissingException");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitWithInvalidPassword() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.password = "wrongpassword";
 appFrame.init();
 fail("Init should have thrown AuthenticationException");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitWithNonExistentSitemapFile() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("nonexistent.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.password = "password";
 appFrame.init();
 fail("Init should have thrown FileNotFoundException");
 }

 @Test(expected = IOException.class)
 public void testInitWithIOException() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("test.xml");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.password = "password";
 final PrintStream captureOut = new PrintStream(new ByteArrayOutputStream());
 final PrintStream captureErr = new PrintStream(new ByteArrayOutputStream());
 System.setOut(captureOut);
 System.setErr(captureErr);
 appFrame.init();
 System.setOut(System.out);
 System.setErr(System.err);
 assertTrue(captureOut.toString().contains("Initializing..."));
 final String errMsg = captureErr.toString();
 assertTrue(errMsg.contains("IOException"));
 }

 @Test(expected = LagoonException.class)
 public void testInitWithInvalidXML() throws IOException, LagoonException {
 appFrame.sitemapFile.setValue("<invalidXML>");
 appFrame.sourceDir.setValue("src");
 appFrame.targetURL.setValue("http://localhost");
 appFrame.password = "password";
 appFrame.init();
 fail("Init should have thrown LagoonException");
 }

}