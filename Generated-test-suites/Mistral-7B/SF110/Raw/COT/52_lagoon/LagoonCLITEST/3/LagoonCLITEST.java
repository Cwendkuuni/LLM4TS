package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static nu.staldal.lagoon.LagoonCLI.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class LagoonCLITest {

 private Properties properties;
 private File sitemapFile;

 @Before
 public void setUp() {
 properties = new Properties();
 sitemapFile = new File("src/test/resources/testSitemap.xml");
 }

 // Test method for LagoonCLI.main(String[])
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentNumber() {
 LagoonCLI.main(new String[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgument() {
 LagoonCLI.main(new String[]{"test.xml", "invalidArgument"});
 }

 @Test
 public void testValidPropertyFile() {
 LagoonCLI.main(new String[]{"testProperties.xml"});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPropertyFile() {
 LagoonCLI.main(new String[]{"testProperties.xml", "invalid"});
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() {
 LagoonCLI.main(new String[]{"testProperties.xml", "invalidInterval"});
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws LagoonException, IOException, SAXException {
 LagoonCLI.main(new String[]{"testSitemap.xml"});
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws LagoonException, IOException, SAXException {
 properties.setProperty("password", "incorrectPassword");
 LagoonCLI.main(new String[]{"testProperties.xml", "testSitemap.xml"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() {
 LagoonCLI.main(new String[]{"testSitemap.xml", "build"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingSitemapFile() {
 properties.setProperty("sitemapFile", "missingSitemapFile.xml");
 LagoonCLI.main(new String[]{"testProperties.xml", "build"});
 }

 @Test(expected = IOException.class)
 public void testIOReadError() throws LagoonException, IOException, SAXException {
 File invalidSitemapFile = new File("src/test/resources/invalidSitemap.xml");
 LagoonCLI.main(new String[]{"testProperties.xml", invalidSitemapFile.getAbsolutePath()});
 }

 @Test(expected = SAXParseException.class)
 public void testSAXParseError() throws LagoonException, IOException, SAXException {
 File invalidSitemapFile = new File("src/test/resources/invalidSitemapWithError.xml");
 LagoonCLI.main(new String[]{"testProperties.xml", invalidSitemapFile.getAbsolutePath()});
 }

 @Test
 public void testBuildSuccess() throws LagoonException, IOException, SAXException {
 LagoonCLI.main(new String[]{"testProperties.xml", "testSitemap.xml", "build"});
 }

 @Test
 public void testForceBuildSuccess() throws LagoonException, IOException, SAXException {
 LagoonCLI.main(new String[]{"testProperties.xml", "testSitemap.xml", "force"});
 }

 @Test
 public void testInterval() throws LagoonException, IOException, SAXException, InterruptedException {
 LagoonCLI.main(new String[]{"testProperties.xml", "testSitemap.xml", "1000"});
 }

 @Test // Test method for LagoonCLI.getProperty(String)
 public void testGetPropertySuccess() {
 properties.setProperty("targetURL", "http://localhost:8080");
 assertEquals("http://localhost:8080", getProperty("targetURL"));
 }

 @Test(expected = LagoonException.class)
 public void testGetPropertyMissing() {
 assertEquals(null, getProperty("nonExistentProperty"));
 }

 @Test(expected = NumberFormatException.class)
 public void testGetPropertyInvalidInteger() {
 properties.setProperty("interval", "invalid");
 try {
 getProperty("interval");
 fail();
 }
 catch (LagoonException e) {
 // expected
 }
 }

 @Test
 public void testShowTime() {
 long time = 12345L;
 showTime(time);
 }
}