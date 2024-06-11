import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.AuthenticationMissingException;
import nu.staldal.lagoon.AuthenticationException;
import nu.staldal.lagoon.FileNotFoundException;
import nu.staldal.lagoon.IOException;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "testPropertyFile.xml";
 private static final String TEST_SIMAP_FILE = "testSitemap.xml";
 private static final String TEST_TARGET_URL = "http://test.com";
 private static final String TEST_SOURCE_DIR = "src/test/resources";
 private static final String TEST_PASSWORD = "testPassword";
 private LagoonCLI lagoonCLI;
 private LagoonProcessor processor;

 @Before
 public void setup() throws Exception {
 properties = new Properties();
 File propertyFile = new File(TEST_PROPERTY_FILE);
 FileInputStream fis = new FileInputStream(propertyFile);
 properties.load(fis);
 fis.close();
 lagoonCLI = new LagoonCLI(new String[]{"testPropertyFile.xml"});
 processor = lagoonCLI.getProcessor();
 }

 @Test
 public void testMainWithPropertyFile() {
 String[] args = new String[]{"testPropertyFile.xml"};
 lagoonCLI.main(args);
 assertNotNull(processor);
 }

 @Test(expected = LagoonException.class)
 public void testMainWithInvalidArgument() {
 String[] args = new String[]{"testPropertyFile.xml", "abc"};
 lagoonCLI.main(args);
 fail("Expected LagoonException to be thrown");
 }

 @Test
 public void testMainWithValidArguments() {
 String[] args = new String[]{"testPropertyFile.xml", "build"};
 lagoonCLI.main(args);
 assertNotNull(processor);
 }

 @Test(expected = NumberFormatException.class)
 public void testMainWithInvalidInterval() {
 String[] args = new String[]{"testPropertyFile.xml", "1a"};
 lagoonCLI.main(args);
 fail("Expected NumberFormatException to be thrown");
 }

 @Test
 public void testMainWithValidInterval() {
 String[] args = new String[]{"testPropertyFile.xml", "1000"};
 lagoonCLI.main(args);
 assertNotNull(processor);
 }

 @Test(expected = LagoonException.class)
 public void testInitializeWithMissingPassword() throws Exception {
 String[] args = new String[]{"testSitemap.xml"};
 lagoonCLI = new LagoonCLI(args);
 try {
 processor = lagoonCLI.getProcessor();
 fail("Expected AuthenticationMissingException to be thrown");
 }
 catch (AuthenticationMissingException e) {
 // Expected
 }
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializeWithIncorrectPassword() throws Exception {
 properties.setProperty("password", "testPasswordIncorrect");
 String[] args = new String[]{"testPropertyFile.xml"};
 lagoonCLI = new LagoonCLI(args);
 try {
 processor = lagoonCLI.getProcessor();
 fail("Expected AuthenticationException to be thrown");
 }
 catch (AuthenticationException e) {
 // Expected
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitializeWithMissingPropertyFile() throws Exception {
 File propertyFile = new File("nonExistentPropertyFile.xml");
 String[] args = new String[]{"nonExistentPropertyFile.xml"};
 lagoonCLI = new LagoonCLI(args);
 try {
 processor = lagoonCLI.getProcessor();
 fail("Expected FileNotFoundException to be thrown");
 }
 catch (FileNotFoundException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testInitializeWithInvalidSitemap() throws Exception {
 File sitemapFile = new File("testSitemapInvalid.xml");
 String[] args = new String[]{"testPropertyFile.xml", "build"};
 lagoonCLI = new LagoonCLI(args);
 try {
 processor = lagoonCLI.getProcessor();
 fail("Expected IOException to be thrown");
 }
 catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testBuild() throws Exception {
 processor.build(false);
 }

 @Test
 public void testForceBuild() throws Exception {
 processor.build(true);
 }
}