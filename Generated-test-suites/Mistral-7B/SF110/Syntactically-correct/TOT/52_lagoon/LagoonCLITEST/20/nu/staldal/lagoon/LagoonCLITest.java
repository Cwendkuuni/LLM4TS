package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.xtree.TreeBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "test.properties";
 private static final String SITEMAP_FILE = "test.sitemap.xml";
 private Properties properties;
 private LagoonCLI lagoonCLI;

 @Before
 public void setUp() {
 properties = new Properties();
 lagoonCLI = spy(new LagoonCLI());
 }

 @After
 public void tearDown() {
 properties = null;
 lagoonCLI = null;
 }

 @Test
 public void testInvalidArguments() {
 String[] args = new String[0];
 lagoonCLI.main(args);
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidIntervalArgument() {
 String[] args = new String[]{"test.properties", "-1"};
 lagoonCLI.main(args);
 assertEquals(SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testValidArguments() throws IOException, SAXException {
 String[] args = new String[]{"test.properties", "1000"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("interval")).thenReturn("1000");
 doReturn(new File(SITEMAP_FILE)).when(lagoonCLI).getProperty("sitemapFile");
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(new File("password.txt")).when(lagoonCLI).getProperty("password");

 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(mock(Element.class));
 doReturn(treeBuilder).when(lagoonCLI).parseXML(new File(SITEMAP_FILE));

 Properties properties = new Properties();
 properties.setProperty("targetURL", "http://test.com");
 properties.setProperty("sitemapFile", SITEMAP_FILE);
 properties.setProperty("sourceDir", ".");
 properties.setProperty("password", "testpassword");
 doReturn(properties).when(lagoonCLI).getProperties();

 lagoonCLI.main(args);
 verify(lagoonCLI, times(1)).initializeLagoon();
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalArgumentNumberFormatException() {
 String[] args = new String[]{"test.properties", "invalid-interval"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("interval")).thenReturn("invalid-interval");
 doReturn(new File(SITEMAP_FILE)).when(lagoonCLI).getProperty("sitemapFile");
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(null).when(lagoonCLI).getProperty("password");

 lagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPasswordException() throws IOException, SAXException {
 String[] args = new String[]{"test.xml"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("password")).thenReturn(null);
 doReturn(new File(SITEMAP_FILE)).when(lagoonCLI).getProperty("sitemapFile");
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(mock(Element.class)).when(lagoonCLI).parseXML(new File(SITEMAP_FILE));
 doThrow(new AuthenticationMissingException("Password is required but not specified")).when(lagoonCLI).initializeLagoon();
 }

 @Test(expected = LagoonException.class)
 public void testIncorrectPasswordException() throws IOException, SAXException {
 String[] args = new String[]{"test.xml", "password"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("password")).thenReturn("wrongpassword");
 doReturn(new File(SITEMAP_FILE)).when(lagoonCLI).getProperty("sitemapFile");
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(mock(Element.class)).when(lagoonCLI).parseXML(new File(SITEMAP_FILE));
 doThrow(new AuthenticationException("Incorrect password")).when(lagoonCLI).initializeLagoon();
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() throws IOException, SAXException {
 String[] args = new String[]{"test.xml", "build"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("sitemapFile")).thenThrow(new FileNotFoundException("sitemap.xml not found"));
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(mock(Element.class)).when(lagoonCLI).parseXML(any(File.class));
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getOutWriter();
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getErrWriter();
 doThrow(new FileNotFoundException("test.xml not found")).when(lagoonCLI).initializeLagoon();
 }

 @Test(expected = IOException.class)
 public void testIOErrorException() throws IOException, SAXException {
 String[] args = new String[]{"test.xml", "build"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("test.xml"));
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(mock(Element.class)).when(lagoonCLI).parseXML(any(File.class));
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getOutWriter();
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getErrWriter();
 doThrow(new IOException("I/O error: test.xml is a directory")).when(lagoonCLI).initializeLagoon();
 }

 @Test(expected = LagoonException.class)
 public void testLagoonException() throws IOException, SAXException {
 String[] args = new String[]{"test.xml", "build"};
 doReturn(new File(PROPERTY_FILE)).when(lagoonCLI).getProperty("propertyFile");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("test.xml"));
 doReturn(new File("targetURL")).when(lagoonCLI).getProperty("targetURL");
 doReturn(new File("sourceDir")).when(lagoonCLI).getProperty("sourceDir");
 doReturn(mock(Element.class)).when(lagoonCLI).parseXML(any(File.class));
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getOutWriter();
 doReturn(mock(PrintWriter.class)).when(lagoonCLI).getErrWriter();
 doThrow(new LagoonException("Error while initializing Lagoon: test error")).when(lagoonCLI).initializeLagoon();
 }
}