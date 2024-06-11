package nu.staldal.lagoon.test;

import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import java.io.File;
import java.io.IOException;

public class LagoonCLITest {

 @Test
 public void testInvalidSyntax() {
 LagoonCLI.main(new String[0]);
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getErrors().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidArgumentForBuildOrForce() {
 LagoonCLI.main(new String[]{"propertyFile.xml", "invalidArgument"});
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getErrors().toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testInvalidInterval() {
 LagoonCLI.main(new String[]{"propertyFile.xml", "-1"});
 String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 String actual = System.out.getErrors().toString();
 assertEquals(expected, actual);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws LagoonException, IOException {
 LagoonCLI.main(new String[]{"sitemap.xml"});
 }

 @Test(expected = LagoonException.class)
 public void testIncorrectPassword() throws LagoonException, IOException {
 LagoonCLI.main(new String[]{"propertyFile.xml", "sitemap.xml", "wrongPassword"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundPropertyFile() throws FileNotFoundException, LagoonException {
 LagoonCLI.main(new String[]{"nonexistentFile.xml"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundSitemapFile() throws FileNotFoundException, LagoonException {
 File propertyFile = new File("propertyFile.xml");
 LagoonCLI.main(new String[]{"propertyFile.xml", "nonexistentFile.xml"});
 }

 @Test(expected = IOException.class)
 public void testIOError() throws IOException, LagoonException {
 File sitemapFile = new File("sitemapFile.xml");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 PrintWriter printWriterOut = new PrintWriter(out);
 PrintWriter printWriterErr = new PrintWriter(System.err);
 LagoonCLI.main(new String[]{"propertyFile.xml", "sitemapFile.xml", "build"}, new String[]{"-Djava.io.FileDescriptor.out=", out.toString().getBytes()}, new String[]{"-Djava.io.FileDescriptor.err=", printWriterErr.toString().getBytes()});
 String expected = "Error while initializing Lagoon:\nI/O error: ";
 String actual = printWriterErr.getBuffer().getBytes();
 assertArrayEquals(expected.getBytes(), actual);
 }

 @Test(expected = LagoonException.class)
 public void testLagoonException() throws IOException, LagoonException {
 File sitemapFile = new File("sitemapFile.xml");
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 PrintWriter printWriterOut = new PrintWriter(out);
 PrintWriter printWriterErr = new PrintWriter(System.err);
 LagoonCLI.main(new String[]{"propertyFile.xml", "sitemapFile.xml", "build"}, new String[]{"-Djava.io.FileDescriptor.out=", out.toString().getBytes()}, new String[]{"-Djava.io.FileDescriptor.err=", printWriterErr.toString().getBytes()});
 String expected = "Error while initializing Lagoon:\n";
 String actual = printWriterErr.getBuffer().toString();
 assertTrue(actual.contains(expected));
 }

 @Test
 public void testBuildSuccess() throws IOException, LagoonException {
 File propertyFile = new File("propertyFile.xml");
 File sitemapFile = new File("sitemapFile.xml");
 File sourceDir = new File("sourceDir");
 LagoonCLI.main(new String[]{"propertyFile.xml", "build"});
 assertTrue(sourceDir.exists());
 }

 @Test
 public void testBuildForce() throws IOException, LagoonException {
 File propertyFile = new File("propertyFile.xml");
 File sitemapFile = new File("sitemapFile.xml");
 File sourceDir = new File("sourceDir");
 LagoonCLI.main(new String[]{"propertyFile.xml", "build", "force"});
 assertFalse(sourceDir.exists());
 }

 @Test
 public void testBuildInterval() throws IOException, InterruptedException, LagoonException {
 File propertyFile = new File("propertyFile.xml");
 File sitemapFile = new File("sitemapFile.xml");
 File sourceDir = new File("sourceDir");
 final long interval = 100;
 LagoonCLI.main(new String[]{"propertyFile.xml", "interval"});
 Thread.sleep(interval + 10);
 LagoonCLI.main(new String[]{"propertyFile.xml", "build"});
 Thread.sleep(interval);
 LagoonCLI.main(new String[]{"propertyFile.xml", "build"});
 assertTrue(sourceDir.exists());
 }
}