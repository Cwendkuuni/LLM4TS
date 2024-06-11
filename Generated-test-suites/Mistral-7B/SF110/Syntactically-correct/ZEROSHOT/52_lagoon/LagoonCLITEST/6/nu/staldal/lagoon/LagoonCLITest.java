package nu.staldal.lagoon.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import static nu.staldal.lagoon.LagoonCLI.*;

public class LagoonCLITest {

 private static final String TEST_PROPERTY_FILE = "src/test/resources/testPropertyFile.xml";
 private static final String TEST_SITEMAP_FILE = "src/test/resources/testSitemap.xml";
 private static final String TEST_TARGET_URL = "http://test.com";
 private LagoonCLI lagoonCLI;

 @Before
 public void setUp() {
 properties = new Properties();
 properties.setProperty("targetURL", TEST_TARGET_URL);
 properties.setProperty("sitemapFile", TEST_SITEMAP_FILE);
 properties.setProperty("password", "testPassword");
 lagoonCLI = new LagoonCLI();
 }

 @After
 public void tearDown() {
 if (lagoonCLI != null) {
 lagoonCLI.destroy();
 }
 }

 @Test
 public void testInvalidArgumentCount() {
 lagoonCLI.main(new String[0]);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrSysOut().trim());
 }

 @Test
 public void testInvalidArgumentType() {
 lagoonCLI.main(new String[]{"", "invalid"});
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrSysOut().trim());
 }

 @Test
 public void testInvalidInterval() {
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "-1"});
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrSysOut().trim());
 }

 @Test
 public void testValidArguments() {
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "build"});
 assertTrue(System.out.getErrSysOut().isEmpty());
 }

 @Test(expected = NumberFormatException.class)
 public void testInvalidIntervalNumberFormat() {
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, "abc"});
 }

 @Test
 public void testPropertyFileNotFound() {
 File propertyFile = new File("nonExistentFile.xml");
 lagoonCLI.main(new String[]{propertyFile.getAbsolutePath()});
 assertEquals("File not found: nonExistentFile.xml", System.err.getErrSysOut().trim());
 }

 @Test(expected = FileNotFoundException.class)
 public void testSitemapFileNotFound() {
 File sitemapFile = new File("src/test/resources/nonExistentFile.xml");
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, sitemapFile.getAbsolutePath()});
 }

 @Test(expected = AuthenticationException.class)
 public void testIncorrectPassword() {
 properties.setProperty("password", "incorrectPassword");
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE, "build"});
 }

 @Test
 public void testBuildSuccess() {
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE, "build"});
 assertTrue(System.out.getErrSysOut().isEmpty());
 }

 @Test
 public void testForceBuildSuccess() {
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE, "force"});
 assertTrue(System.out.getErrSysOut().isEmpty());
 }

 @Test
 public void testRunWithInput() throws InterruptedException {
 String input = "b";
 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 PrintStream out = new PrintStream(new ByteArrayOutputStream());
 PrintStream err = new PrintStream(new ByteArrayOutputStream());
 System.setOut(out);
 System.setErr(err);
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE});
 assertEquals("", System.out.getErrSysOut().trim());
 assertEquals("Building website...\n...successfully\n", out.getBuffer().toString());

 String input2 = "f";
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE});
 assertEquals("Ready", System.out.getErrSysOut().trim());
 System.setIn(new ByteArrayInputStream(input.getBytes()));
 in.close();
 out = new PrintStream(System.out);
 err = new PrintStream(System.err);
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE});
 assertEquals("Force building website...\n...successfully\n", out.getBuffer().toString());
 }

 @Test
 public void testRunWithInputQ() throws InterruptedException {
 String input = "q";
 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
 PrintStream out = new PrintStream(new ByteArrayOutputStream());
 PrintStream err = new PrintStream(new ByteArrayOutputStream());
 System.setOut(out);
 System.setErr(err);
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE});
 assertEquals("Ready", System.out.getErrSysOut().trim());
 System.setIn(new ByteArrayInputStream(input.getBytes()));
 in.close();
 out = new PrintStream(System.out);
 err = new PrintStream(System.err);
 lagoonCLI.main(new String[]{TEST_PROPERTY_FILE, TEST_SITEMAP_FILE});
 assertEquals("Exiting Lagoon...", out.getBuffer().toString());
 }
}