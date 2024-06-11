package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;

import org.junit.Test;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\n" +
         "nu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\n" +
         "nu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";

 @Test
 public void testInvalidArguments() {
     LagoonCLI.main(new String[0]);
     assertEquals("Syntax:\n" + SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidArgumentType() {
     LagoonCLI.main(new String[]{"property.xml", "invalid"});
     assertEquals("Syntax:\n" + SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidInterval() {
     LagoonCLI.main(new String[]{"property.xml", "-1"});
     assertEquals("Syntax:\n" + SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testValidArguments() {
     LagoonCLI.main(new String[]{"property.xml", "1000"});
     Properties properties = LagoonCLI.properties;
     assertEquals("targetURL", properties.getProperty("targetURL"));
     assertEquals("sitemapFile", properties.getProperty("sitemapFile"));
 }

 @Test
 public void testInvalidPropertyFile() {
     LagoonCLI.main(new String[]{"invalid.xml", "1000"});
     assertEquals("Error while initializing Lagoon:\nFile not found: invalid.xml\n", System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidSitemapFile() {
     File propertyFile = new File("property.xml");
     Properties properties = new Properties();
     properties.load(new FileInputStream(propertyFile));
     String targetURL = properties.getProperty("targetURL");
     String sitemapFileString = properties.getProperty("sitemapFile");
     File sitemapFile = new File(sitemapFileString);
     LagoonCLI.main(new String[]{propertyFile.getAbsolutePath(), "invalid.xml", "1000"});
     assertEquals("Error while initializing Lagoon:\nSAXException: org.xml.sax.SAXParseException: Root element must be <xml-mapping>, <urlset> or <urlset xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi:schemaLocation=\"http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd\">.\n at [row,col]:[1,1]\n", System.out.getErrorStream().toString());
 }

 @Test
 public void testValidBuild() throws IOException, SAXException {
     Properties properties = new Properties();
     properties.setProperty("targetURL", "http://test.com");
     properties.setProperty("sourceDir", "/tmp/output");
     properties.setProperty("sitemapFile", "test.xml");
     properties.save(new FileOutputStream("property.xml"), null);
     LagoonCLI.main(new String[]{"property.xml", "build"});
     assertEquals("Lagoon initialized successfully\nBuilding website...\n...successfully.\n", System.out.getBuffer().toString());
 }

 @Test
 public void testValidForceBuild() throws IOException, SAXException {
     Properties properties = new Properties();
     properties.setProperty("targetURL", "http://test.com");
     properties.setProperty("sourceDir", "/tmp/output");
     properties.setProperty("sitemapFile", "test.xml");
     properties.save(new FileOutputStream("property.xml"), null);
     LagoonCLI.main(new String[]{"property.xml", "force"});
     assertEquals("Lagoon initialized successfully\nForce building website...\n...successfully.\n", System.out.getBuffer().toString());
 }

 @Test
 public void testValidInterval() throws IOException, SAXException, InterruptedException {
     Properties properties = new Properties();
     properties.setProperty("targetURL", "http://test.com");
     properties.setProperty("sourceDir", "/tmp/output");
     properties.setProperty("sitemapFile", "test.xml");
     properties.save(new FileOutputStream("property.xml"), null);
     LagoonCLI.main(new String[]{"property.xml", "3000"});
     Thread.sleep(5000);
     LagoonCLI.main(new String[]{"property.xml"});
     LagoonCLI.main(new String[]{"property.xml"});
 }

 @Test
 public void testInvalidIntervalNegative() {
     LagoonCLI.main(new String[]{"property.xml", "-1000"});
     assertEquals("Syntax:\n" + SYNTAX_MSG, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidIntervalZero() {
     LagoonCLI.main(new String[]{"property.xml", "0"});
     assertEquals("Lagoon initialized successfully\n", System.out.getBuffer().toString());
 }
}