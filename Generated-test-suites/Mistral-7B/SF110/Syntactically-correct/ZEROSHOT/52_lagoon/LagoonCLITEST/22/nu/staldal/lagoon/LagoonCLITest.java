package nu.staldal.lagoon.test;

import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import static nu.staldal.lagoon.LagoonCLI.*;

public class LagoonCLITest {

 @Test
 public void testInvalidArguments() {
 String[] args = new String[0];
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getUTF8Text());
 }

 @Test
 public void testInvalidInterval() {
 String[] args = new String[]{"property.xml", "abc"};
 LagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getUTF8Text());
 }

 @Test
 public void testValidArgumentsWithPropertyFile() {
 String[] args = new String[]{"property.xml"};
 System.setProperty("user.dir", "test");
 LagoonCLI.main(args);
 String targetURL = "http://localhost:8080";
 String sitemapFile = "sitemap.xml";
 String password = null;
 assertEquals(targetURL, getProperty("targetURL"));
 assertEquals(sitemapFile, getProperty("sitemapFile"));
 assertEquals(null, getProperty("sourceDir"));
 assertEquals(null, getProperty("password"));
 }

 @Test
 public void testValidArgumentsWithSitemapFile() {
 String[] args = new String[]{"sitemap.xml"};
 LagoonCLI.main(args);
 String targetURL = null;
 String sitemapFile = "sitemap.xml";
 File sourceDir = new File("test");
 String password = null;
 assertNull(targetURL);
 assertEquals(sitemapFile, getProperty("sitemapFile"));
 assertEquals(sourceDir.getAbsolutePath(), getProperty("sourceDir"));
 assertEquals(null, getProperty("password"));
 }

 @Test
 public void testValidArgumentsWithBuild() {
 String[] args = new String[]{"property.xml", "build"};
 LagoonCLI.main(args);
 boolean build = true;
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertTrue(build);
 }

 @Test
 public void testValidArgumentsWithForce() {
 String[] args = new String[]{"property.xml", "force"};
 LagoonCLI.main(args);
 boolean build = false;
 assertFalse(LagoonCLI.build);
 assertTrue(LagoonCLI.force);
 }

 @Test
 public void testValidArgumentsWithInterval() {
 String[] args = new String[]{"property.xml", "5000"};
 LagoonCLI.main(args);
 long interval = 5000L;
 assertFalse(LagoonCLI.build);
 assertFalse(LagoonCLI.force);
 assertEquals(interval, LagoonCLI.interval);
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws Exception {
 String[] args = new String[]{"property.xml"};
 LagoonCLI.main(args);
 assertNull(LagoonCLI.properties.getProperty("password"));
 String password = "password";
 LagoonCLI.properties.setProperty("password", password);
 DataInputStream input = new DataInputStream(new FileInputStream("property.xml"));
 DataOutputStream output = new DataOutputStream(new FileOutputStream("property.xml"));
 byte[] passwordBytes = password.getBytes();
 output.writeInt(passwordBytes.length + 4);
 output.writeUTF("password");
 output.writeUTF("password");
 output.writeBytes(passwordBytes);
 output.close();
 input.readUTF();
 input.readUTF();
 input.readInt();
 input.readByte();
 input.readByte();
 input.readInt();
 input.close();
 LagoonCLI.main(args);
 assertNull(LagoonCLI.properties.getProperty("password"));
 }

 @Test(expected = LagoonException.class)
 public void testIncorrectPassword() throws Exception {
 String[] args = new String[]{"property.xml"};
 LagoonCLI.main(args);
 assertNull(LagoonCLI.properties.getProperty("password"));
 String password = "password";
 LagoonCLI.properties.setProperty("password", password);
 DataInputStream input = new DataInputStream(new FileInputStream("property.xml"));
 DataOutputStream output = new DataOutputStream(new FileOutputStream("property.xml"));
 byte[] passwordBytes = password.getBytes();
 output.writeInt(passwordBytes.length + 4);
 output.writeUTF("password");
 output.writeUTF("wrongpassword");
 output.writeBytes(passwordBytes);
 output.close();
 input.readUTF();
 input.readUTF();
 input.readInt();
 input.readByte();
 input.readByte();
 input.readInt();
 input.close();
 LagoonCLI.main(args);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFound() {
 String[] args = new String[]{"nonexistentfile.xml"};
 LagoonCLI.main(args);
 }

 @Test(expected = IOException.class)
 public void testIOException() {
 String[] args = new String[]{"input.xml"};
 LagoonCLI.main(args);
 }

 @Test(expected = LagoonException.class)
 public void testLagoonException() {
 String[] args = new String[]{"input.xml"};
 LagoonCLI.main(args);
 }

 @Test
 public void testBuildWebsite() throws Exception {
 String[] args = new String[]{"property.xml", "build"};
 LagoonCLI.main(args);
 }

 @Test
 public void testForceBuildWebsite() throws Exception {
 String[] args = new String[]{"property.xml", "force"};
 LagoonCLI.main(args);
 }

 @Test
 public void testPeriodicBuilding() throws Exception {
 String[] args = new String[]{"property.xml", "1000"};
 LagoonCLI.main(args);
 }
}