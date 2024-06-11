package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.xml.sax.SAXException;

import nu.staldal.xtree.XMLParser;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "testPropertyFile.xml";
 private static final String SITEMAP_FILE = "testSitemapFile.xml";
 private static final String INVALID_ARGUMENT = "invalidArgument";
 private static final String VALID_ARGUMENT = "build";
 private static final long INTERVAL = 1000;
 private static final long NEGATIVE_INTERVAL = -1000;
 private static final String MISSING_PASSWORD = "missingPassword";
 private static final String INCORRECT_PASSWORD = "incorrectPassword";
 private static final String FILE_NOT_FOUND = "fileNotFound";
 private static final String IO_ERROR = "ioError";
 private static final String LAGOON_EXCEPTION = "lagoonException";
 private Properties properties;
 private LagoonCLI lagoonCLI;
 private XMLParser xmlParserMock;

 @Before
 public void setup() {
 properties = new Properties();
 xmlParserMock = mock(XMLParser.class);
 lagoonCLI = new LagoonCLI() {
 private XMLParser parser = xmlParserMock;

 @Override
 public void initializeLagoon(Properties props, File sitemapFile, String targetURL, File sourceDir, String password, PrintWriter out, PrintWriter err) {
 super.initializeLagoon(props, sitemapFile, targetURL, sourceDir, password, out, err);
 this.parser = parser;
 }

 @Override
 public Element parseXML(File xmlFile) throws SAXException, IOException {
 return parser.parseXML(xmlFile);
 }
 };
 }

 @Test
 public void testValidArguments() {
 String[] args = new String[] { PROPERTY_FILE, VALID_ARGUMENT };
 lagoonCLI.main(args);
 verify(parser).parseXML(new File(SITEMAP_FILE));
 }

 @Test(expected = LagoonException.class)
 public void testInvalidArguments() {
 String[] args = new String[] { PROPERTY_FILE, INVALID_ARGUMENT };
 lagoonCLI.main(args);
 verifyNoMoreInteractions(parser);
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.out).println(msgCaptor.capture());
 assertEquals(SYNTAX_MSG, msgCaptor.getValue());
 }

 @Test
 public void testInterval() {
 String[] args = new String[] { SITEMAP_FILE, INTERVAL + "" };
 lagoonCLI.main(args);
 ArgumentCaptor<Long> intervalCaptor = ArgumentCaptor.forClass(Long.class);
 verify(lagoonCLI).scheduleBuild(intervalCaptor.capture());
 assertEquals(INTERVAL, intervalCaptor.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testNegativeInterval() {
 String[] args = new String[] { SITEMAP_FILE, NEGATIVE_INTERVAL + "" };
 lagoonCLI.main(args);
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Interval must be greater than or equal to 0", msgCaptor.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() {
 String[] args = new String[] { PROPERTY_FILE, SITEMAP_FILE };
 lagoonCLI.main(args);
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Error while initializing Lagoon: Password is required but not specified", msgCaptor.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testIncorrectPassword() {
 String[] args = new String[] { PROPERTY_FILE, SITEMAP_FILE, MISSING_PASSWORD };
 lagoonCLI.main(args);
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Error while initializing Lagoon: Incorrect password", msgCaptor.getValue());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFound() {
 String[] args = new String[] { FILE_NOT_FOUND };
 try {
 lagoonCLI.main(args);
 } catch (LagoonException e) {
 throw new RuntimeException(e);
 }
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Error while initializing Lagoon: File not found: " + FILE_NOT_FOUND, msgCaptor.getValue());
 }

 @Test(expected = IOException.class)
 public void testIOError() {
 doThrow(new IOException("Test IO Error")) //
 .when(parser).parseXML(any(File.class));
 String[] args = new String[] { SITEMAP_FILE };
 try {
 lagoonCLI.main(args);
 } catch (LagoonException e) {
 throw new RuntimeException(e);
 }
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Error while initializing Lagoon: I/O error: Test IO Error", msgCaptor.getValue());
 }

 @Test(expected = LagoonException.class)
 public void testLagoonException() {
 doThrow(new LagoonException("Test Lagoon Exception")) //
 .when(parser).parseXML(any(File.class));
 String[] args = new String[] { SITEMAP_FILE };
 try {
 lagoonCLI.main(args);
 } catch (LagoonException e) {
 throw new RuntimeException(e);
 }
 ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
 verify(System.err).println(msgCaptor.capture());
 assertEquals("Error while initializing Lagoon: Test Lagoon Exception", msgCaptor.getValue());
 }
}