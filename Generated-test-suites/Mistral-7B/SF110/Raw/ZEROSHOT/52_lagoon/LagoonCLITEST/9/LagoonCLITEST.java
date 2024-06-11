package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class LagoonCLITest {

 @Test
 public void testInvalidArguments() {
 LagoonCLI.main(new String[0]);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrorStream().toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() {
 Properties props = new Properties();
 File propFile = new File("src/test/resources/lagoon.properties");
 props.load(new FileInputStream(propFile));
 LagoonCLI.main(new String[]{"src/test/resources/lagoon.properties", "-1"});
 }

 @Test
 public void testValidArgumentsPropertyFile() throws IOException, SAXException, LagoonException {
 Properties props = new Properties();
 File propFile = new File("src/test/resources/lagoon.properties");
 props.load(new FileInputStream(propFile));
 LagoonCLI.main(new String[]{"src/test/resources/lagoon.properties"});
 assertEquals("Lagoon initialized successfully", System.out.getErrorStream().toString().trim().split("\\r\\n")[0]);
 }

 @Test
 public void testValidArgumentsSitemapFile() throws IOException, SAXException, LagoonException {
 File sitemapFile = new File("src/test/resources/sitemap.xml");
 LagoonCLI.main(new String[]{"src/test/resources/sitemap.xml"});
 assertEquals("Lagoon initialized successfully", System.out.getErrorStream().toString().trim().split("\\r\\n")[0]);
 }

 @Test
 public void testBuild() throws IOException, SAXException, LagoonException, InterruptedException {
 Properties props = new Properties();
 File propFile = new File("src/test/resources/lagoon.properties");
 props.load(new FileInputStream(propFile));
 File sitemapFile = new File("src/test/resources/sitemap.xml");
 LagoonCLI.main(new String[]{"src/test/resources/lagoon.properties", "build"});
 assertTrue(new File("build/html").exists());
 }

 @Test
 public void testForceBuild() throws IOException, SAXException, LagoonException {
 Properties props = new Properties();
 File propFile = new File("src/test/resources/lagoon.properties");
 props.load(new FileInputStream(propFile));
 File sitemapFile = new File("src/test/resources/sitemap.xml");
 File htmlDir = new File("build/html");
 if (htmlDir.exists()) {
 htmlDir.delete();
 }
 LagoonCLI.main(new String[]{"src/test/resources/lagoon.properties", "build", "force"});
 assertTrue(new File("build/html").exists());
 }

 @Test
 public void testInterval() throws IOException, SAXException, LagoonException, InterruptedException {
 Properties props = new Properties();
 File propFile = new File("src/test/resources/lagoon.properties");
 props.load(new FileInputStream(propFile));
 File sitemapFile = new File("src/test/resources/sitemap.xml");
 LagoonCLI.main(new String[]{"src/test/resources/lagoon.properties", "10000"});
 Thread.sleep(11000);
 assertFalse(new File("build/html").exists());
 }
}