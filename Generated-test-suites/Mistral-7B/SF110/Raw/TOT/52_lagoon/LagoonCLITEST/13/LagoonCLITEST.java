package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.xtree.TreeBuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class LagoonCLITest {

 private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
 private static final String PROPERTY_FILE = "test.properties";
 private static final String SITEMAP_FILE = "test.sitemap";
 private static final String TARGET_URL = "http://www.example.com";
 private static final String SOURCE_DIR = "/tmp/test";
 private static final String PASSWORD = "testPassword";
 private static final long INTERVAL = 1000L;
 private static final boolean BUILD = false;
 private static final boolean FORCE = false;

 private LagoonCLI lagoonCLI;
 private Properties properties;

 @Before
 public void setup() throws IOException, SAXException {
 properties = new Properties();
 FileInputStream fis = new FileInputStream(new File(PROPERTY_FILE));
 properties.load(fis);
 fis.close();
 MockitoAnnotations.initMocks(this);
 lagoonCLI = new LagoonCLI();
 }

 @Test
 public void testInvalidArgumentCount() {
 lagoonCLI.main(new String[0]);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getProperties().getProperty("msg"));
 }

 @Test
 public void testInvalidArgumentType() {
 String[] args = {"test.properties", "abc"};
 lagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getProperties().getProperty("msg"));
 }

 @Test
 public void testInvalidPropertyFile() {
 File propertyFile = new File("test.propertiesx");
 lagoonCLI.main(new String[]{"test.propertiesx"});
 verify(System.out, times(1)).println(SYNTAX_MSG);
 }

 @Test
 public void testValidPropertyFile() throws IOException {
 File propertyFile = new File(PROPERTY_FILE);
 File sitemapFile = new File(SITEMAP_FILE);
 String[] args = {"test.properties"};
 when(properties.getProperty("targetURL")).thenReturn(TARGET_URL);
 when(properties.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(properties.getProperty("sourceDir")).thenReturn(SOURCE_DIR);
 when(properties.getProperty("password")).thenReturn(PASSWORD);

 Element sitemapTree = mock(Element.class);
 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(File.class), anyBoolean())).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 doReturn(processor).when(lagoonCLI).newLagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

 lagoonCLI.main(args);
 ArgumentCaptor<Properties> propertiesCaptor = ArgumentCaptor.forClass(Properties.class);
 verify(processor).init(argThat(p -> p.equals(propertiesCaptor.getValue())), eq(TARGET_URL), eq(sitemapTree), eq(new File(SOURCE_DIR)), eq(PASSWORD), eq(System.out), eq(System.err));
 }

 @Test
 public void testInvalidInterval() {
 String[] args = {"test.properties", "abc"};
 lagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getProperties().getProperty("msg"));
 }

 @Test
 public void testInvalidBuildFlag() {
 String[] args = {"test.properties", "buildx"};
 lagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getProperties().getProperty("msg"));
 }

 @Test
 public void testInvalidForceFlag() {
 String[] args = {"test.properties", "forcex"};
 lagoonCLI.main(args);
 assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getProperties().getProperty("msg"));
 }

 @Test
 public void testValidArguments() throws IOException, SAXException {
 String[] args = {"test.properties", "1000"};
 when(properties.getProperty("targetURL")).thenReturn(TARGET_URL);
 when(properties.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(properties.getProperty("sourceDir")).thenReturn(SOURCE_DIR);
 when(properties.getProperty("password")).thenReturn(PASSWORD);

 Element sitemapTree = mock(Element.class);
 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(File.class), anyBoolean())).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 doReturn(processor).when(lagoonCLI).newLagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

 lagoonCLI.main(args);
 ArgumentCaptor<Properties> propertiesCaptor = ArgumentCaptor.forClass(Properties.class);
 verify(processor).init(argThat(p -> p.equals(propertiesCaptor.getValue())), eq(TARGET_URL), eq(sitemapTree), eq(new File(SOURCE_DIR)), eq(PASSWORD), eq(System.out), eq(System.err));
 }

 @Test
 public void testBuildWebsite() throws IOException, InterruptedException, SAXException {
 String[] args = {"test.properties"};
 when(properties.getProperty("targetURL")).thenReturn(TARGET_URL);
 when(properties.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(properties.getProperty("sourceDir")).thenReturn(SOURCE_DIR);
 when(properties.getProperty("password")).thenReturn(PASSWORD);

 Element sitemapTree = mock(Element.class);
 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(File.class), anyBoolean())).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 doReturn(processor).when(lagoonCLI).newLagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

 lagoonCLI.main(args);
 ArgumentCaptor<Properties> propertiesCaptor = ArgumentCaptor.forClass(Properties.class);
 verify(processor).init(argThat(p -> p.equals(propertiesCaptor.getValue())), eq(TARGET_URL), eq(sitemapTree), eq(new File(SOURCE_DIR)), eq(PASSWORD), eq(System.out), eq(System.err));
 verify(processor).build(eq(false));
 }

 @Test
 public void testForceBuildWebsite() throws IOException, InterruptedException, SAXException {
 String[] args = {"test.properties"};
 when(properties.getProperty("targetURL")).thenReturn(TARGET_URL);
 when(properties.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(properties.getProperty("sourceDir")).thenReturn(SOURCE_DIR);
 when(properties.getProperty("password")).thenReturn(PASSWORD);

 Element sitemapTree = mock(Element.class);
 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(File.class), anyBoolean())).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 doReturn(processor).when(lagoonCLI).newLagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

 lagoonCLI.main(args);
 ArgumentCaptor<Properties> propertiesCaptor = ArgumentCaptor.forClass(Properties.class);
 verify(processor).init(argThat(p -> p.equals(propertiesCaptor.getValue())), eq(TARGET_URL), eq(sitemapTree), eq(new File(SOURCE_DIR)), eq(PASSWORD), eq(System.out), eq(System.err));
 verify(processor).build(eq(true));
 }

 @Test
 public void testInterval() throws IOException, InterruptedException, SAXException {
 String[] args = {"test.properties", "1000"};
 when(properties.getProperty("targetURL")).thenReturn(TARGET_URL);
 when(properties.getProperty("sitemapFile")).thenReturn(SITEMAP_FILE);
 when(properties.getProperty("sourceDir")).thenReturn(SOURCE_DIR);
 when(properties.getProperty("password")).thenReturn(PASSWORD);

 Element sitemapTree = mock(Element.class);
 TreeBuilder treeBuilder = mock(TreeBuilder.class);
 when(treeBuilder.parseXML(any(File.class), anyBoolean())).thenReturn(sitemapTree);
 LagoonProcessor processor = mock(LagoonProcessor.class);
 doReturn(processor).when(lagoonCLI).newLagoonProcessor(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

 lagoonCLI.main(args);
 ArgumentCaptor<Properties> propertiesCaptor = ArgumentCaptor.forClass(Properties.class);
 verify(processor).init(argThat(p -> p.equals(propertiesCaptor.getValue())), eq(TARGET_URL), eq(sitemapTree), eq(new File(SOURCE_DIR)), eq(PASSWORD), eq(System.out), eq(System.err));
 verify(processor, times(2)).build(eq(false));
 }

}