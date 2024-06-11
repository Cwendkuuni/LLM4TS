package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.Properties;
import java.util.Random;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

 private static final String VALID_PROPERTIES_FILE = "validProperties.xml";
 private static final String VALID_SITEMAP_FILE = "validSitemap.xml";
 private static final String INVALID_PROPERTIES_FILE = "invalidProperties.xml";
 private static final String INVALID_SITEMAP_FILE = "invalidSitemap.xml";
 private static final String VALID_ARGUMENTS = "property_file valid_interval";
 private static final String INVALID_ARGUMENTS = "property_file invalid_argument";
 private static final String MISSING_ARGUMENTS = "missing_argument";
 private static final String PASSWORD = "password";
 private static final String TARGET_URL = "http://test.com";
 private LagoonProcessor processor;
 private Properties properties;
 private Random random;

 @Before
 public void setUp() {
 properties = new Properties();
 random = new Random();
 }

 @Test
 public void testMainWithValidArguments() throws Exception {
 String[] args = new String[] { VALID_PROPERTIES_FILE, "valid_interval" };
 givenPropertiesWithValidValues();
 whenMainIsCalledWithValidArguments(args);
 thenLagoonIsInitializedSuccessfully();
 }

 @Test
 public void testMainWithInvalidArguments() throws Exception {
 String[] args = new String[] { VALID_PROPERTIES_FILE, "invalid_argument" };
 givenPropertiesWithValidValues();
 whenMainIsCalledWithInvalidArguments(args);
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testMainWithMissingArguments() throws Exception {
 String[] args = new String[0];
 givenPropertiesWithValidValues();
 whenMainIsCalledWithMissingArguments(args);
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testMainWithInvalidPropertiesFile() throws Exception {
 String[] args = new String[] { INVALID_PROPERTIES_FILE, MISSING_ARGUMENTS };
 givenPropertiesWithInvalidValues();
 whenMainIsCalledWithValidArguments(args);
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testMainWithInvalidSitemapFile() throws Exception {
 String[] args = new String[] { VALID_PROPERTIES_FILE, VALID_SITEMAP_FILE };
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(args);
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithValidSitemapAndProperties() throws Exception {
 givenPropertiesWithValidValues();
 givenSitemapWithValidValues();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "build" });
 thenLagoonIsInitializedSuccessfully();
 thenWebsiteIsBuiltSuccessfully();
 }

 @Test
 public void testBuildWithInvalidSitemapAndProperties() throws Exception {
 givenPropertiesWithValidValues();
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "build" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithValidPropertiesButInvalidSitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenSitemapWithInvalidValues();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "build" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithValidPropertiesAndEmptySitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenEmptySitemap();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "build" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithForce() throws Exception {
 givenPropertiesWithValidValues();
 givenSitemapWithValidValues();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "force" });
 thenWebsiteIsBuiltSuccessfully();
 }

 @Test
 public void testBuildWithForceAndInvalidSitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "force" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithForceAndValidSitemapButInvalidProperties() throws Exception {
 givenSaxExceptionWhenParsingPropertiesFile();
 givenSitemapWithValidValues();
 whenMainIsCalledWithValidArguments(new String[] { INVALID_PROPERTIES_FILE, "force" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithForceAndValidPropertiesButInvalidSitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "force" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithInterval() throws Exception {
 givenPropertiesWithValidValues();
 givenSitemapWithValidValues();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "10000" });
 thenWebsiteIsBuiltSuccessfully();
 thenWaitsForSpecifiedInterval();
 }

 @Test
 public void testBuildWithIntervalAndInvalidSitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "10000" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithIntervalAndValidSitemapButInvalidProperties() throws Exception {
 givenSaxExceptionWhenParsingPropertiesFile();
 givenSitemapWithValidValues();
 whenMainIsCalledWithValidArguments(new String[] { INVALID_PROPERTIES_FILE, "10000" });
 thenErrorMessageIsPrintedAndReturns();
 }

 @Test
 public void testBuildWithIntervalAndValidPropertiesButInvalidSitemap() throws Exception {
 givenPropertiesWithValidValues();
 givenSaxExceptionWhenParsingSitemapFile();
 whenMainIsCalledWithValidArguments(new String[] { VALID_PROPERTIES_FILE, "10000" });
 thenErrorMessageIsPrintedAndReturns();
 }

 private void givenPropertiesWithValidValues() {
 properties.setProperty("targetURL", TARGET_URL);
 properties.setProperty("sitemapFile", VALID_SITEMAP_FILE);
 properties.setProperty("sourceDir", "source");
 properties.setProperty("password", PASSWORD);
 }

 private void givenPropertiesWithInvalidValues() throws Exception {
 properties.setProperty("targetURL", "");
 properties.setProperty("sitemapFile", INVALID_SITEMAP_FILE);
 properties.setProperty("sourceDir", "");
 properties.setProperty("password", "");
 }

 private void givenSaxExceptionWhenParsingPropertiesFile() throws Exception {
 doThrow(new SAXException("Test exception"))
 .when(properties).load(any(FileInputStream.class));
 }

 private void givenSaxExceptionWhenParsingSitemapFile() throws Exception {
 doThrow(new SAXException("Test exception"))
 .when(TreeBuilder.class)
 .parseXML(any(FileSource.class), isA(Boolean.class));
 }

 private void givenEmptySitemap() throws Exception {
 doReturn(new Element[0])
 .when(TreeBuilder.class)
 .parseXML(any(FileSource.class), isA(Boolean.class));
 }

 private void whenMainIsCalledWithValidArguments(String[] args) throws Exception {
 LagoonCLI.main(args);
 }

 private void whenMainIsCalledWithInvalidArguments(String[] args) throws Exception {
 LagoonCLI.main(args);
 verify(System.out, times(1)).println(syntaxMsg);
 verifyNoMoreInteractions(System.out);
 }

 private void whenMainIsCalledWithMissingArguments(String[] args) throws Exception {
 LagoonCLI.main(args);
 verify(System.out, times(1)).println(syntaxMsg);
 verifyNoMoreInteractions(System.out);
 }

 private void thenLagoonIsInitializedSuccessfully() throws Exception {
 verify(System.out, times(1)).println("Lagoon initialized successfully");
 }

 private void thenWebsiteIsBuiltSuccessfully() throws Exception {
 verify(processor, times(1)).build(false);
 }

 private void thenErrorMessageIsPrintedAndReturns() throws Exception {
 verify(System.err, times(1)).println("Error while initializing Lagoon:");
 System.exit(0);
 }

 private void thenWaitsForSpecifiedInterval() throws InterruptedException {
 long interval = 10000;
 Thread.sleep(interval);
 }
}