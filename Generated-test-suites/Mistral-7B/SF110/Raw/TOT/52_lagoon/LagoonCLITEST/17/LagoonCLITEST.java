package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "test.properties";
 private static final String SITEMAP_FILE = "test.sitemap";
 private static final String[] ARGS_FOR_PROPERTY_FILE = {"test.properties"};
 private static final String[] ARGS_FOR_SITEMAP_FILE = {"test.sitemap"};
 private static final String[] ARGS_FOR_INTERVAL = {"1"};
 private static final String[] ARGS_FOR_BUILD = {"build"};
 private static final String[] ARGS_FOR_FORCE = {"force"};
 private LagoonCLI lagoonCLI;
 private Properties properties;
 private File sitemapFile;

 @Before
 public void setUp() throws Exception {
 properties = new Properties();
 sitemapFile = new File(SITEMAP_FILE);
 lagoonCLI = mock(LagoonCLI.class);
 when(lagoonCLI.getProperty("targetURL")).thenReturn("http://example.com");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src/test/resources/output"));
 when(lagoonCLI.build(false)).thenReturn(true);
 when(lagoonCLI.build(true)).thenReturn(false);
 }

 @Test
 public void testValidArgumentsForPropertyFile() {
 lagoonCLI.main(ARGS_FOR_PROPERTY_FILE);
 assertEquals("Lagoon initialized successfully", System.out.readLine());
 }

 @Test
 public void testValidArgumentsForSitemapFile() {
 lagoonCLI.main(ARGS_FOR_SITEMAP_FILE);
 assertEquals("Lagoon initialized successfully", System.out.readLine());
 }

 @Test
 public void testInvalidArgumentsForInterval() {
 lagoonCLI.main(new String[]{"test.properties", "-1"});
 assertEquals(SYNTAX_MSG, System.out.readLine());
 }

 @Test
 public void testInvalidArgumentsForBuildOrForce() {
 lagoonCLI.main(new String[]{"test.properties", "invalid"});
 assertEquals(SYNTAX_MSG, System.out.readLine());
 }

 @Test
 public void testValidArgumentsForBuild() throws IOException {
 when(lagoonCLI.build(false)).thenReturn(false);
 lagoonCLI.main(ARGS_FOR_BUILD);
 assertEquals("Building website...\n...with errors...", System.out.readLine());
 }

 @Test
 public void testValidArgumentsForForce() throws IOException {
 when(lagoonCLI.build(true)).thenReturn(true);
 lagoonCLI.main(ARGS_FOR_FORCE);
 assertEquals("Force building website...\nLagoon initialized successfully", System.out.readLine(), "Expected 'Lagoon initialized successfully' after force build");
 }

 @Test
 public void testValidArgumentsForInterval() throws InterruptedException {
 when(lagoonCLI.build(false)).thenReturn(false);
 lagoonCLI.main(ARGS_FOR_INTERVAL);
 assertEquals("Building website...\n...with errors...", System.out.readLine());
 Thread.sleep(1100);
 assertFalse(System.in.available());
 }

 @Test(expected = LagoonException.class)
 public void testMissingPassword() {
 when(lagoonCLI.getProperty("password")).thenReturn(null);
 lagoonCLI.main(new String[]{"test.xml"});
 }

 @Test(expected = AuthenticationException.class)
 public void testInvalidPassword() {
 when(lagoonCLI.getProperty("password")).thenReturn("invalidpassword");
 lagoonCLI.main(new String[]{"test.xml"});
 }

 @Test(expected = FileNotFoundException.class)
 public void testMissingPropertyFile() {
 lagoonCLI.main(new String[]{"test.nonexistent.properties"});
 assertEquals("Error while initializing Lagoon:\nFile not found: test.nonexistent.properties", System.err.readLine());
 }

 @Test(expected = IOException.class)
 public void testMissingSitemapFile() {
 when(lagoonCLI.build(false)).thenThrow(new IOException("Test IOException"));
 lagoonCLI.main(new String[]{"test.properties"});
 assertEquals("Error while initializing Lagoon:\nI/O error: Test IOException", System.err.readLine());
 }

 @Test
 public void testDebugMode() {
 System.setProperty("DEBUG", "true");
 lagoonCLI = new LagoonCLI();
 lagoonCLI.main(ARGS_FOR_PROPERTY_FILE);
 assertTrue(LagoonCLI.DEBUG);
 }

 @Test
 public void testNullArguments() {
 assertThrows(NullPointerException.class, () -> lagoonCLI.main(null));
 }

 @Test
 public void testEmptyArguments() {
 assertThrows(ArrayIndexOutOfBoundsException.class, () -> lagoonCLI.main(new String[]{}));
 }

}