import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import nu.staldal.lagoon.AuthenticationException;
import nu.staldal.lagoon.AuthenticationMissingException;
import nu.staldal.lagoon.CoreException;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.sitemap.sax.TreeBuilder;
import static org.mockito.Mockito.*;

public class LagoonCLITest {

 private static final String SYNTAX_MESSAGE = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private LagoonCLI lagoonCLI;
 private LagoonProcessor mockLagoonProcessor;

 @Before
 public void setup() {
 lagoonCLI = new LagoonCLI();
 mockLagoonProcessor = mock(LagoonProcessor.class);
 }

 @Test
 public void testInvalidArgumentCount() {
 String[] args = new String[0];
 lagoonCLI.main(args);
 Assert.assertEquals(SYNTAX_MESSAGE, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidBuildOrForceArgument() {
 String[] args = new String[]{"property_file", "invalid"};
 lagoonCLI.main(args);
 Assert.assertEquals(SYNTAX_MESSAGE, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidIntervalArgument() {
 String[] args = new String[]{"property_file", "-1"};
 lagoonCLI.main(args);
 Assert.assertEquals(SYNTAX_message, System.out.getErrorStream().toString());
 }

 @Test
 public void testInvalidPropertyFile() {
 String propertyFile = "invalid_property_file.txt";
 String[] args = new String[]{propertyFile};
 doThrow(FileNotFoundException.class).when(lagoonCLI).getProperty(eq("propertyFile"));
 lagoonCLI.main(args);
 Assert.assertEquals(String.format("Error while initializing Lagoon:\nFile not found: %s", propertyFile), System.err.getErrorStream().toString());
 }

 @Test
 public void testMissingPasswordProperty() {
 Properties properties = new Properties();
 String[] args = new String[]{"sitemap.xml"};
 when(lagoonCLI.getProperty("password")).thenReturn(null);
 lagoonCLI.main(args);
 Assert.assertEquals("Error while initializing Lagoon:\nPassword is required but not specified", System.err.getErrorStream().toString());
 }

 @Test
 public void testIncorrectPassword() {
 Properties properties = new Properties();
 String password = "incorrect_password";
 properties.put("password", password);
 String[] args = new String[]{"sitemap.xml"};
 when(lagoonCLI.getProperty("password")).thenReturn(password);
 doThrow(AuthenticationException.class).when(mockLagoonProcessor).initialize("http://localhost:8080/", mock(File.class), mock(File.class), eq(properties), any(PrintWriter.class), any(PrintWriter.class));
 lagoonCLI.main(args);
 Assert.assertEquals("Error while initializing Lagoon:\nIncorrect password", System.err.getErrorStream().toString());
 }

 @Test
 public void testBuildSuccessfully() throws CoreException, IOException, SAXException {
 Properties properties = new Properties();
 properties.put("targetURL", "http://localhost:8080/");
 properties.put("sitemapFile", new File("sitemap.xml").getAbsolutePath());
 properties.put("sourceDir", new File("source_dir").getAbsolutePath());
 properties.put("password", "password");
 String[] args = new String[]{"property_file"};
 when(lagoonCLI.getProperty("targetURL")).thenReturn("http://localhost:8080/");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
 when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("source_dir").getAbsolutePath());
 when(lagoonCLI.getProperty("password")).thenReturn("password");
 File sitemapFile = new File("sitemap.xml");
 when(mockLagoonProcessor.initialize(eq("http://localhost:8080/"), any(File.class), any(File.class), eq(properties), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(mockLagoonProcessor);
 when(mockLagoonProcessor.build(false)).thenReturn(true);
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(false);
 }

 @Test
 public void testForceBuildSuccessfully() throws CoreException, IOException, SAXException {
 Properties properties = new Properties();
 properties.put("targetURL", "http://localhost:8080/");
 properties.put("sitemapFile", new File("sitemap.xml").getAbsolutePath());
 properties.put("sourceDir", new File("source_dir").getAbsolutePath());
 properties.put("password", "password");
 String[] args = new String[]{"property_file", "force"};
 when(lagoonCLI.getProperty("targetURL")).thenReturn("http://localhost:8080/");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
 when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("source_dir").getAbsolutePath());
 when(lagoonCLI.getProperty("password")).thenReturn("password");
 File sitemapFile = new File("sitemap.xml");
 when(mockLagoonProcessor.initialize(eq("http://localhost:8080/"), any(File.class), any(File.class), eq(properties), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(mockLagoonProcessor);
 when(mockLagoonProcessor.build(true)).thenReturn(true);
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(true);
 }

 @Test
 public void testIntervalBuild() throws CoreException, IOException, SAXException, InterruptedException {
 Properties properties = new Properties();
 properties.put("targetURL", "http://localhost:8080/");
 properties.put("sitemapFile", new File("sitemap.xml").getAbsolutePath());
 properties.put("sourceDir", new File("source_dir").getAbsolutePath());
 properties.put("password", "password");
 String[] args = new String[]{"property_file", "5000"};
 when(lagoonCLI.getProperty("targetURL")).thenReturn("http://localhost:8080/");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
 when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("source_dir").getAbsolutePath());
 when(lagoonCLI.getProperty("password")).thenReturn("password");
 File sitemapFile = new File("sitemap.xml");
 when(mockLagoonProcessor.initialize(eq("http://localhost:8080/"), any(File.class), any(File.class), eq(properties), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(mockLagoonProcessor);
 when(mockLagoonProcessor.build(false)).thenReturn(true);
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(false);
 verify(mockLagoonProcessor, times(0)).build(true);
 Thread.sleep(6000); // Wait for 6 seconds
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(false);
 Thread.sleep(5000); // Wait for 5 seconds
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(false);
 }

 @Test
 public void testInterruptedBuild() throws CoreException, IOException, SAXException, InterruptedException {
 Properties properties = new Properties();
 properties.put("targetURL", "http://localhost:8080/");
 properties.put("sitemapFile", new File("sitemap.xml").getAbsolutePath());
 properties.put("sourceDir", new File("source_dir").getAbsolutePath());
 properties.put("password", "password");
 String[] args = new String[]{"property_file", "1000"};
 when(lagoonCLI.getProperty("targetURL")).thenReturn("http://localhost:8080/");
 when(lagoonCLI.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
 when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("source_dir").getAbsolutePath());
 when(lagoonCLI.getProperty("password")).thenReturn("password");
 File sitemapFile = new File("sitemap.xml");
 when(mockLagoonProcessor.initialize(eq("http://localhost:8080/"), any(File.class), any(File.class), eq(properties), any(PrintWriter.class), any(PrintWriter.class))).thenReturn(mockLagoonProcessor);
 when(mockLagoonProcessor.build(false)).thenThrow(IOException.class);
 lagoonCLI.main(args);
 verify(mockLagoonProcessor).build(false);
 }

 @Test
 public void testDestroy() {
 String[] args = new String[]{"property_file"};
 lagoonCLI.main(args);
 lagoonCLI.destroy();
 }
}