package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static nu.staldal.lagoon.LagoonCLI.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class LagoonCLITest {

 @Test
 public void testValidArguments() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);

 assertTrue(new File("test_source/build").exists());
 }

 @Test(expected = Exception.class)
 public void testInvalidSyntax() throws Exception {
 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidInterval() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password\ninterval=-1");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = LagoonException.class)
 public void testMissingPropertyFile() throws Exception {
 String[] args = new String[]{"nonexistent_file.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = LagoonException.class)
 public void testMissingTargetURL() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties_missing_target.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("sitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties_missing_target.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties_missing_password.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source");
 propsWriter.close();

 String[] args = new String[]{"test_properties_missing_password.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = SAXException.class)
 public void testInvalidSitemapFile() throws Exception {
 File sitemapFile = new File("test_sitemap.xml");
 Files.write(sitemapFile.toPath(), new byte[0]);

 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test(expected = IOException.class)
 public void testMissingSitemapFile() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=nonexistent_file.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);
 fail("No error message was printed");
 }

 @Test
 public void testBuild() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml", "build"};
 LagoonCLI.main(args);

 assertTrue(new File("test_source/build").exists());
 }

 @Test
 public void testForceBuild() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml", "force"};
 LagoonCLI.main(args);

 assertTrue(new File("test_source/build").exists());
 }

 @Test
 public void testInterval() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password\ninterval=1000");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);

 Thread.sleep(1100);
 String[] args2 = new String[]{"test_properties.xml"};
 LagoonCLI.main(args2);

 Thread.sleep(50);
 String[] args3 = new String[]{"test_properties.xml"};
 LagoonCLI.main(args3);

 assertTrue(new File("test_source/build").exists());
 }

 @Test
 public void testBuildAndConsoleCommands() throws Exception {
 Properties props = new Properties();
 File propsFile = new File("test_properties.xml");
 FileWriter propsWriter = new FileWriter(propsFile);
 propsWriter.write("targetURL=http://test.com\nsitemapFile=test_sitemap.xml\nsourceDir=test_source\npassword=test_password");
 propsWriter.close();

 String[] args = new String[]{"test_properties.xml"};
 LagoonCLI.main(args);
 Thread.sleep(1000);

 String[] args2 = new String[]{"test_properties.xml", "b"};
 LagoonCLI.main(args2);
 Thread.sleep(1000);

 String[] args3 = new String[]{"test_properties.xml", "f"};
 LagoonCLI.main(args3);
 Thread.sleep(1000);

 String[] args4 = new String[]{"test_properties.xml", "q"};
 LagoonCLI.main(args4);
 }
}