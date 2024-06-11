package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.PropertyReader;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

 private static final String CORRECT_PROPERTY_FILE = "src/test/resources/correct_property_file.xml";
 private static final String INVALID_PROPERTY_FILE = "src/test/resources/invalid_property_file.xml";
 private static final String VALID_SIMAP_FILE = "src/test/resources/valid_sitemap.xml";
 private static final String INVALID_SIMAP_FILE = "src/test/resources/invalid_sitemap.xml";
 private static final String VALID_ARGUMENTS = "property_file valid_argument";
 private static final String INVALID_ARGUMENTS = "property_file invalid_argument";
 private static final String INVALID_INTERVAL = "-1";
 private static final String VALID_INTERVAL = "1000";
 private static final String BUILD = "build";
 private static final String FORCE = "force";
 private static final String ARGUMENTS_WITH_BUILD = "property_file valid_argument build";
 private static final String ARGUMENTS_WITH_FORCE = "property_file valid_argument force";
 private static final String ARGUMENTS_WITH_INTERVAL = "property_file valid_argument 1000";

 private Properties properties;
 private File propertyFile;
 private File sitemapFile;
 private LagoonProcessor lagoonProcessor;
 private PrintWriter printWriterOut;
 private PrintWriter printWriterErr;

 @Before
 public void setUp() throws Exception {
 properties = new Properties();
 propertyFile = new File(CORRECT_PROPERTY_FILE);
 sitemapFile = new File(VALID_SIMAP_FILE);
 printWriterOut = mock(PrintWriter.class);
 printWriterErr = mock(PrintWriter.class);
 }

 @After
 public void tearDown() throws Exception {
 if (lagoonProcessor != null) {
 lagoonProcessor.destroy();
 }
 }

 @Test
 public void testInitializationWithValidArguments() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, VALID_ARGUMENTS})).thenCallRealMethod();
 then(LagoonCLI.properties.load(any(FileInputStream.class))).should().beCalled();
 verify(System.out).println(isA(String.class));
 verify(System.out).println("Lagoon initialized successfully");
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithMissingPropertyFile() throws Exception {
 when(System.getProperty("user.dir")).thenReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {"invalid_property_file.xml", ""})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithInvalidPropertyFile() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 given(LagoonCLI.properties.load(any(FileInputStream.class))).willThrow(new IOException());
 when(LagoonCLI.main(new String[] {INVALID_PROPERTY_FILE, ""})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithInvalidArgument() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, INVALID_ARGUMENT})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }

 @Test(expected = LagoonException.class)
 public void testInitializationWithInvalidInterval() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, INVALID_INTERVAL})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }

 @Test
 public void testInitializationWithValidIntervalAndBuild() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_INTERVAL, BUILD})).thenCallRealMethod();
 verify(LagoonProcessor.class).getConstructor(eq(String.class), eq(File.class), eq(File.class), any(PrintWriter.class), any(PrintWriter.class)).isCalled();
 }

 @Test
 public void testInitializationWithValidIntervalAndForce() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_INTERVAL, FORCE})).thenCallRealMethod();
 verify(LagoonProcessor.class).getConstructor(eq(String.class), eq(File.class), eq(File.class), any(PrintWriter.class), any(PrintWriter.class)).isCalled();
 }

 @Test
 public void testInitializationWithValidArgumentsWithSitemapFile() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {VALID_SIMAP_FILE, VALID_ARGUMENTS})).thenCallRealMethod();
 verify(TreeBuilder.class).parseXML(any(InputSource.class), anyBoolean()).isCalled();
 }

 @Test(expected = AuthenticationMissingException.class)
 public void testInitializationWithMissingPassword() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ""})).thenCallRealMethod();
 verify(System.err).println("Error while initializing Lagoon:");
 verify(System.err).println("Password is required but not specified");
 }

 @Test(expected = AuthenticationException.class)
 public void testInitializationWithInvalidPassword() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 given(LagoonCLI.properties.getProperty("password")).willReturn("invalid_password");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ""})).thenCallRealMethod();
 verify(System.err).println("Error while initializing Lagoon:");
 verify(System.err).println("Incorrect password");
 }

 @Test(expected = FileNotFoundException.class)
 public void testInitializationWithMissingSitemapFile() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {INVALID_SIMAP_FILE, ""})).thenCallRealMethod();
 verify(System.err).println("Error while initializing Lagoon:");
 verify(System.err).println("File not found: " + INVALID_SIMAP_FILE);
 }

 @Test(expected = IOException.class)
 public void testInitializationWithInvalidSitemapFile() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 given(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).willThrow(new SAXException(new IOException()));
 when(LagoonCLI.main(new String[] {VALID_SIMAP_FILE, ""})).thenCallRealMethod();
 verify(System.err).println("Error while initializing Lagoon:");
 }

 @Test
 public void testBuildingWebsite() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_BUILD})).thenCallRealMethod();
 verify(lagoonProcessor.build(eq(false))).isCalled();
 }

 @Test
 public void testForceBuildingWebsite() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_FORCE})).thenCallRealMethod();
 verify(lagoonProcessor.build(eq(true))).isCalled();
 }

 @Test
 public void testBuildingWebsiteWithInterval() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_INTERVAL})).thenCallRealMethod();
 Thread.sleep(1050);
 verify(lagoonProcessor.build(eq(false))).isCalled();
 }

 @Test
 public void testBuildingWebsiteWithInvalidArguments() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, "invalid_argument"})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }

 @Test
 public void testBuildingWebsiteWithInvalidInterval() throws Exception {
 given(System.getProperty("user.dir")).willReturn("src/test/resources");
 when(LagoonCLI.main(new String[] {CORRECT_PROPERTY_FILE, ARGUMENTS_WITH_INTERVAL, "invalid_interval"})).thenCallRealMethod();
 verify(System.out).println(syntaxMsg);
 }
}