package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.xtree.TreeBuilder;
import org.xml.sax.SAXException;

public class LagoonCLITest {

    private static final String TEST_XML_FILE = "test.xml";
    private static final String TEST_PROPERTIES_FILE = "test.properties";
    private static final String TEST_SITEMAP_FILE = "test.sitemap";
    private LagoonCLI lagoonCLI;
    private LagoonProcessor lagoonProcessor;
    private Properties properties;

    @Before
    public void setUp() {
        lagoonCLI = Mockito.spy(new LagoonCLI());
        properties = new Properties();
        lagoonProcessor = mock(LagoonProcessor.class);
    }

    @After
    public void tearDown() throws Exception {
        lagoonCLI = null;
        properties = null;
        lagoonProcessor = null;
    }

    @Test
    public void testMainWithInvalidArgCount() {
        String[] args = new String[0];
        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithInvalidPropertyFile() {
        String[] args = new String[] {"test.non_existent_file.xml"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        doThrow(FileNotFoundException.class).when(lagoonCLI).initializeLagoon(anyString(), any(File.class), anyBoolean(), anyBoolean(), anyLong());

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Error while initializing Lagoon:");
        verify(lagoonCLI, times(1)).println("File not found: test.non_existent_file.xml");
    }

    @Test
    public void testMainWithInvalidArgument() {
        String[] args = new String[] {"test.xml", "invalidArg"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        doThrow(LagoonException.class).when(lagoonCLI).initializeLagoon(anyString(), any(File.class), anyBoolean(), anyBoolean(), anyLong());

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Error while initializing Lagoon:");
        verify(lagoonCLI, times(1)).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagnoon.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMainWithValidArguments() throws IOException, SAXException, LagoonException {
        String[] args = new String[] {"test.xml", "build"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(0L))).thenReturn(lagoonProcessor);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");
        verify(lagoonProcessor, times(1)).build(eq(false));
    }

    @Test
    public void testInitializeLagoonWithInvalidPassword() throws AuthenticationException, FileNotFoundException, IOException, LagoonException {
        String propertyFile = TEST_PROPERTIES_FILE;
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sourceDir", "src");
        properties.setProperty("password", "invalidPassword");
        properties.setProperty("sitemapFile", TEST_SITEMAP_FILE);

        when(lagoonCLI.getProperty("property_file")).thenReturn(new File(propertyFile));
        doThrow(AuthenticationException.class).when(lagoonCLI).initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(0L));

        lagoonCLI.main(new String[] {propertyFile, "build"});
        verify(lagoonCLI, times(1)).println("Error while initializing Lagoon:");
        verify(lagoonCLI, times(1)).println("Incorrect password");
    }

    @Test
    public void testInitializeLagoonWithMissingPassword() throws FileNotFoundException, IOException, LagoonException {
        String propertyFile = TEST_PROPERTIES_FILE;
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sourceDir", "src");

        when(lagoonCLI.getProperty("property_file")).thenReturn(new File(propertyFile));
        doThrow(AuthenticationMissingException.class).when(lagoonCLI).initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(0L));

        lagoonCLI.main(new String[] {propertyFile, "build"});
        verify(lagoonCLI, times(1)).println("Error while initializing Lagoon:");
        verify(lagoonCLI, times(1)).println("Password is required but not specified");
    }

    @Test
    public void testBuildWebsiteSuccessfully() throws IOException, SAXException, LagoonException {
        String[] args = new String[] {"test.xml", "build"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(0L))).thenReturn(lagoonProcessor);
        when(lagoonProcessor.build(eq(false))).thenReturn(true);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");
        verify(lagoonProcessor, times(1)).build(eq(false));
    }

    @Test
    public void testBuildWebsiteWithErrors() throws IOException, SAXException, LagoonException {
        String[] args = new String[] {"test.xml", "build"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(0L))).thenReturn(lagoonProcessor);
        when(lagoonProcessor.build(eq(false))).thenReturn(false);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");
        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).println("...with errors...");
    }

    @Test
    public void testForceBuildWebsiteSuccessfully() throws IOException, SAXException, LagoonException {
        String[] args = new String[] {"test.xml", "force"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(true), eq(0L))).thenReturn(lagoonProcessor);
        when(lagoonProcessor.build(eq(true))).thenReturn(true);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");
        verify(lagoonProcessor, times(1)).build(eq(true));
    }

    @Test
    public void testForceBuildWebsiteWithErrors() throws IOException, SAXException, LagoonException {
        String[] args = new String[] {"test.xml", "force"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(true), eq(0L))).thenReturn(lagoonProcessor);
        when(lagoonProcessor.build(eq(true))).thenReturn(false);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");
        verify(lagoonProcessor, times(1)).build(eq(true));
        verify(lagoonCLI, times(1)).println("...with errors...");
    }

    @Test
    public void testPeriodicBuilding() throws IOException, SAXException, InterruptedException, LagoonException {
        String[] args = new String[] {"test.xml", "5000"};
        when(lagoonCLI.getProperty("targetURL")).thenReturn("http://test.com");
        when(lagoonCLI.getProperty("sourceDir")).thenReturn(new File("src"));
        when(lagoonCLI.getProperty("sitemapFile")).thenReturn(TEST_SITEMAP_FILE);
        when(lagoonCLI.initializeLagoon(eq("http://test.com"), any(File.class), eq(false), eq(false), eq(5000L))).thenReturn(lagoonProcessor);

        lagoonCLI.main(args);
        verify(lagoonCLI, times(1)).println("Lagoon initialized successfully");

        Thread.sleep(1000);

        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).println("Building website...");
        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).showTime(anyLong());

        Thread.sleep(6000);

        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).println("Building website...");
        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).showTime(anyLong());

        Thread.sleep(10000);

        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).println("Building website...");
        verify(lagoonProcessor, times(1)).build(eq(false));
        verify(lagoonCLI, times(1)).showTime(anyLong());
    }
}