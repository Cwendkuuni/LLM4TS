package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

 private File testPropertyFile;
 private File testSitemapFile;
 private Properties testProperties;

 @Before
 public void setUp() throws Exception {
 testPropertyFile = new File("testPropertyFile.xml");
 testSitemapFile = new File("testSitemapFile.xml");
 testProperties = new Properties();
 testProperties.load(new FileInputStream("testProperties.properties"));
 }

 @Test
 public void testInvalidSyntax() {
 LagoonCLI.main(new String[0]);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getStackTrace()[0].getLineNumber() + ": " + System.out.getErrorStream();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidArg1() {
 String[] args = new String[] {"test.txt"};
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getStackTrace()[0].getLineNumber() + ": " + System.out.getErrorStream();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidArg2() {
 String[] args = new String[] {"testPropertyFile.xml", "invalidArgument"};
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getStackTrace()[0].getLineNumber() + ": " + System.out.getErrorStream();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidInterval() {
 String[] args = new String[] {"testPropertyFile.xml", "-1"};
 LagoonCLI.main(args);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getStackTrace()[0].getLineNumber() + ": " + System.out.getErrorStream();
 assertEquals(expected, actual);
 }

 @Test
 public void testValidSyntaxWithPropertyFile() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml"};
 LagoonCLI.main(args);
 String expected = "Initializing Lagoon...";
 String actual = System.out.getLines()[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testValidSyntaxWithSitemapFile() throws IOException, LagoonException, SAXException {
 Element sitemapTree;
 Element root;
 sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(testSitemapFile), false);
 root = sitemapTree.getDocumentElement();
 String[] args = new String[] {"testSitemapFile.xml"};
 LagoonCLI.main(args);
 String expected = "Initializing Lagoon...";
 String actual = System.out.getLines()[0];
 assertEquals(expected, actual);
 }

 @Test
 public void testValidSyntaxWithPropertyFileAndBuild() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml", "build"};
 LagoonCLI.main(args);
 String expected = "Building website...";
 String actual = System.out.getLines()[1][0];
 assertEquals("B", actual.charAt(0));
 }

 @Test
 public void testValidSyntaxWithPropertyFileAndForce() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml", "force"};
 LagoonCLI.main(args);
 String expected = "Force building website...";
 String actual = System.out.getLines()[1][0];
 assertEquals("F", actual.charAt(0));
 }

 @Test
 public void testValidSyntaxWithPropertyFileAndInterval() throws IOException, InterruptedException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml", "1000"};
 LagoonCLI.main(args);
 String expected = "Building website...";
 String actual = System.out.getLines()[1][0];
 assertEquals("B", actual.charAt(0));
 Thread.sleep(1500);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml"};
 LagoonCLI.main(args);
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml", "wrongPassword"};
 LagoonCLI.main(args);
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() throws LagoonException, SAXException {
 String[] args = new String[] {"test missedPropertyFile.xml"};
 LagoonCLI.main(args);
 }

 @Test(expected = IOException.class)
 public void testMissingSitemapFile() throws IOException, LagoonException, SAXException {
 String[] args = new String[] {"testPropertyFile.xml"};
 LagoonCLI.main(args);
 }
}