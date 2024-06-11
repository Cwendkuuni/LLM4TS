package nu.staldal.lagoon;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = LagoonCLI.syntaxMsg;
    private static final String PROPERTY_FILE = "property_file.xml";
    private static final String SITEMAP_FILE = "sitemap.xml";
    private LagoonCLI lagoonCLI;
    private Properties properties;
    private File propertyFile;
    private File sitemapFile;

    @Before
    public void setUp() {
        System.setProperty("user.dir", System.getProperty("java.io.tmpdir"));
        lagoonCLI = new LagoonCLI();
        properties = new Properties();
        propertyFile = new File(PROPERTY_FILE);
        sitemapFile = new File(SITEMAP_FILE);
    }

    @Test(expected = AssertionError.class)
    public void testMainWithNoArguments() {
        lagoonCLI.main(new String[0]);
    }

    @Test
    public void testMainWithInvalidArgument() {
        String[] args = {"invalid_arg"};
        lagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getHWB().toString());
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = {"property_file.xml", "invalid_interval"};
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithNegativeInterval() {
        String[] args = {"property_file.xml", "-1"};
        lagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFile() {
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
        assertTrue(properties.size() > 0);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingProperty() {
        properties.put("targetURL", "http://example.com");
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
        assertFalse(properties.containsKey("sitemapFile"));
    }

    @Test
    public void testGetProperty() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", "sitemap.xml");
        String targetURL = lagoonCLI.getProperty("targetURL");
        String sitemapFile = lagoonCLI.getProperty("sitemapFile");
        assertEquals("http://example.com", targetURL);
        assertEquals("sitemap.xml", sitemapFile);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() {
        properties.put("targetURL", "http://example.com");
        String propertyName = "sitemapFile";
        lagoonCLI.getProperty(propertyName);
    }

    @Test
    public void testShowTime() {
        long time = 5000L;
        lagoonCLI.showTime(time);
        if (time < 10000L) {
            assertTrue("in " + time + " ms", time >= 0 && time <= 10000);
        } else {
            assertTrue("in " + time / 1000 + " s", time >= 0);
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingPropertyFile() {
        String[] args = {"missing_property_file.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingSitemapFile() {
        properties.put("targetURL", "http://example.com");
        properties.put("property_file", PROPERTY_FILE);
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
        File sitemapFile = new File(LagoonCLI.getProperty("sitemapFile"));
        assertFalse(sitemapFile.exists());
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeLagoonWithMissingPassword() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "");
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeLagoonWithIncorrectPassword() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "incorrect_password");
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingSourceDir() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml"};
        lagoonCLI.main(args);
        File sourceDir = new File(LagoonCLI.getProperty("sourceDir"));
        assertFalse(sourceDir.exists());
    }

    @Test(expected = IOException.class)
    public void testInitializeLagoonWithIOException() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml"};
        File inputSource = new File("input_source.xml");
        when(TreeBuilder.fileToInputSource(inputSource)).thenThrow(new IOException("IOException"));
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithLagoonException() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml"};
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenThrow(new LagoonException("LagoonException"));
        lagoonCLI.main(args);
    }

    @Test
    public void testBuildWebsite() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml"};
        File outputDirectory = new File("output_directory");
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(eq(false))).thenReturn(true);
        when(processor.getOutputDirectory()).thenReturn(outputDirectory);
        when(LagoonCLI.class.getClassLoader().getResource("nu/staldal/lagoon/LagoonProcessor.class")).thenReturn(LagoonProcessor.class);
        when(processor.newInstance()).thenReturn(processor);
        lagoonCLI.main(args);
        assertTrue(outputDirectory.exists());
    }

    @Test
    public void testForceBuildWebsite() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml", "force"};
        File outputDirectory = new File("output_directory");
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(eq(true))).thenReturn(true);
        when(processor.getOutputDirectory()).thenReturn(outputDirectory);
        when(LagoonCLI.class.getClassLoader().getResource("nu/staldal/lagoon/LagoonProcessor.class")).thenReturn(LagoonProcessor.class);
        when(processor.newInstance()).thenReturn(processor);
        lagoonCLI.main(args);
        assertTrue(outputDirectory.exists());
    }

    @Test
    public void testPeriodicBuildWebsite() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml"};
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        when(LagoonCLI.class.getClassLoader().getResource("nu/staldal/lagoon/LagoonProcessor.class")).thenReturn(LagoonProcessor.class);
        when(processor.newInstance()).thenReturn(processor);
        lagoonCLI.main(args);
        lagoonCLI.main(args);
        lagoonCLI.main(args);
        verify(processor, times(3)).build(eq(false));
        verify(processor, times(3)).destroy();
    }

    @Test
    public void testPeriodicBuildWebsiteWithLongInterval() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml", "5000"};
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        when(LagoonCLI.class.getClassLoader().getResource("nu/staldal/lagoon/LagoonProcessor.class")).thenReturn(LagoonProcessor.class);
        when(processor.newInstance()).thenReturn(processor);
        lagoonCLI.main(args);
        long timeBefore = System.currentTimeMillis();
        lagoonCLI.main(args);
        long timeElapsed = System.currentTimeMillis() - timeBefore;
        verify(processor, times(1)).build(eq(false));
        verify(processor, times(1)).destroy();
        assertTrue(timeElapsed >= 5000);
    }

    @Test
    public void testPeriodicBuildWebsiteWithShortInterval() {
        properties.put("targetURL", "http://example.com");
        properties.put("sitemapFile", SITEMAP_FILE);
        properties.put("password", "password");
        String[] args = {"property_file.xml", "500"};
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        when(LagoonCLI.class.getClassLoader().getResource("nu/staldal/lagoon/LagoonProcessor.class")).thenReturn(LagoonProcessor.class);
        when(processor.newInstance()).thenReturn(processor);
        lagoonCLI.main(args);
        long timeBefore = System.currentTimeMillis();
        lagoonCLI.main(args);
        long timeElapsed = System.currentTimeMillis() - timeBefore;
        verify(processor, times(2)).build(eq(false));
        verify(processor, times(2)).destroy();
        assertTrue(timeElapsed < 500);
    }
}