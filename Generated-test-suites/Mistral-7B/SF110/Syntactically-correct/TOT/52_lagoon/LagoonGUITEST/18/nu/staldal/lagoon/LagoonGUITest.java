package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.*;

import org.junit.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LagoonGUITest {

 private static final String TEST_XML_FILE = "test.xml";
 private static final String TEST_SOURCE_DIR = "src/test/resources/";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_PASSWORD = "test123";

 @BeforeClass
 public static void setupClass() throws IOException {
 File tempFile = new File(TEST_XML_FILE);
 tempFile.createNewFile();
 try (FileWriter file = new FileWriter(TEST_XML_FILE)) {
 file.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
 "<sitemap>\n" +
 "<url url=\"/\">\n" +
 "<loc>index.html</loc>\n" +
 "</url>\n" +
 "</sitemap>");
 }
 }

 @AfterClass
 public static void teardownClass() {
 File testFile = new File(TEST_XML_FILE);
 testFile.delete();
 }

 @Before
 public void setUp() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_XML_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.targetURL.setValue(TEST_TARGET_URL);
 appFrame.password = TEST_PASSWORD;
 }

 @After
 public void tearDown() {
 LagoonGUI.exit();
 }

 @Test
 public void testInitialize() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.init();
 assertNotNull(appFrame.processor);
 }

 @Test
 public void testBuild() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.init();
 appFrame.build(false);
 String expectedOutput = "--- Build finished successfully 2s ---";
 assertEquals(expectedOutput, appFrame.progressArea.getText());
 }

 @Test(expected = LagoonException.class)
 public void testBuildWithInvalidSitemapFile() {
 LagoonGUI appFrame = new LagoonGUI();
 File invalidSitemapFile = new File("nonExistentFile.xml");
 appFrame.sitemapFile.setValue(invalidSitemapFile.getAbsolutePath());
 appFrame.init();
 appFrame.build(false);
 }

 @Test(expected = IOException.class)
 public void testBuildWithFileNotFoundException() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_XML_FILE);
 appFrame.sourceDir.setValue("nonExistentDir");
 appFrame.init();
 appFrame.build(false);
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testBuildWithMissingPassword() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_XML_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.init();
 appFrame.build(false);
 }

 @Test(expected = AuthenticationException.class)
 public void testBuildWithInvalidPassword() {
 LagoonGUI appFrame = new LagoonGUI();
 appFrame.sitemapFile.setValue(TEST_XML_FILE);
 appFrame.sourceDir.setValue(TEST_SOURCE_DIR);
 appFrame.password = "incorrectPassword";
 appFrame.init();
 appFrame.build(false);
 }
}