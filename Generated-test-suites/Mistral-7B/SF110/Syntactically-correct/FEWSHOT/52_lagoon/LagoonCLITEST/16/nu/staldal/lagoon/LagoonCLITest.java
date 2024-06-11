package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "property_file.txt";
 private static final String SITEMAP_FILE = "sitemap_file.xml";
 private static final String TARGET_URL = "target_url";
 private static final String SOURCE_DIR = "source_dir";
 private static final String PASSWORD = "password";
 private static final String USER_DIR = System.getProperty("user.dir");
 private static final String XTREE_RESOURCE = "/xtree/";
 private static Properties properties;
 private File propertyFile;
 private File sitemapFile;
 private File inputSource;
 private SAXParser parser;
 private PrintWriter out;
 private PrintWriter err;
 private LagoonProcessor processor;

 @Before
 public void setUp() throws Exception {
 properties = new Properties();
 propertyFile = new File(PROPERTY_FILE);
 sitemapFile = new File(SITEMAP_FILE);
 inputSource = mock(InputStream.class);
 when(inputSource.toString()).thenReturn(XTREE_RESOURCE + "sitemap.xml");
 parser = mock(SAXParser.class);
 out = mock(PrintWriter.class);
 err = mock(PrintWriter.class);
 }

 @After
 public void tearDown() throws Exception {
 propertyFile.deleteOnExit();
 sitemapFile.deleteOnExit();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArguments() throws Exception {
 LagoonCLI.main(new String[0]);
 }

 @Test
 public void testValidArgumentsPropertyFile() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE });
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE, "abc" });
 }

 @Test
 public void testValidArgumentsInterval() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE, "1000" });
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalNegative() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE, "-1000" });
 }

 @Test
 public void testValidArgumentsBuild() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE, "build" });
 }

 @Test
 public void testValidArgumentsForce() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE, "force" });
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() throws Exception {
 LagoonCLI.main(new String[] { SITEMAP_FILE });
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingSitemapFile() throws Exception {
 LagoonCLI.main(new String[] { PROPERTY_FILE });
 }

 @Test(expected = LagoonException.class)
 public void testMissingTargetURL() throws Exception {
 properties.setProperty("targetURL", null);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = LagoonException.class)
 public void testMissingSitemapFileProperty() throws Exception {
 properties.setProperty("sitemapFile", null);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = LagoonException.class)
 public void testMissingSourceDir() throws Exception {
 properties.setProperty("sourceDir", null);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testMissingPassword() throws Exception {
 properties.setProperty("password", null);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws Exception {
 processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(false);
 when(processor.build(true)).thenReturn(false);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingTargetURLFile() throws Exception {
 properties.setProperty("targetURL", USER_DIR);
 processor = new LagoonProcessor(TARGET_URL, null, null, null, out, err);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = IOException.class)
 public void testIOError() throws Exception {
 when(parser.parse(inputSource, null)).thenThrow(new IOException("I/O error"));
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test(expected = SAXException.class)
 public void testSAXException() throws Exception {
 when(parser.parse(inputSource, null)).thenThrow(new SAXException("SAX exception"));
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test
 public void testBuild() throws Exception {
 processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(true);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE, "build" });
 }

 @Test
 public void testForceBuild() throws Exception {
 processor = mock(LagoonProcessor.class);
 when(processor.build(true)).thenReturn(true);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE, "force" });
 }

 @Test
 public void testInterval() throws Exception {
 processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(true);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE, "1500" });
 Thread.sleep(1600);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE, "1500" });
 Thread.sleep(1000);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE, "1500" });
 }

 @Test
 public void testRunning() throws Exception {
 processor = mock(LagoonProcessor.class);
 when(processor.build(false)).thenReturn(true);
 when(processor.build(true)).thenReturn(true);
 LagoonCLI.main(new String[] { PROPERTY_FILE, SITEMAP_FILE });
 }

 @Test
 public void testPrintTime() throws Exception {
 long time = 3500;
 showTime(time);
 }
}