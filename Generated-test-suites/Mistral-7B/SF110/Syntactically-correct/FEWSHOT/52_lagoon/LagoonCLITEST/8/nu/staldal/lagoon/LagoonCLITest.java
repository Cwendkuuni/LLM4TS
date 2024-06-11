package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.SAXException;
import static org.mockito.Mockito.*;
import org.mockito.ArgumentMatchers;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.mockito.MockitoAnnotations.initMocks;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.xtree.TreeBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class LagoonCLITest {

    private Properties properties;
    private FileInputStream fis;
    private PrintWriter out;
    private PrintWriter err;
    private File sitemapFile;
    private Element sitemapTree;
    private LagoonProcessor processor;

    @Before
    public void setup() {
        initMocks(this);
        properties = new Properties();
        fis = mock(FileInputStream.class);
        out = mock(PrintWriter.class);
        err = mock(PrintWriter.class);
        sitemapFile = mock(File.class);
        sitemapTree = mock(Element.class);
        when(sitemapFile.getName()).thenReturn("sitemap.xml");
        when(TreeBuilder.parseXML(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.anyBoolean())).thenReturn(sitemapTree);
        when(sitemapFile.exists()).thenReturn(true);
    }

    @After
    public void tearDown() {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @Test
    public void testMainWithInvalidArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithMissingPropertyFileArgument() {
        String[] args = new String[]{"invalidFile"};
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithMissingPropertyFileAndInvalidArgument() {
        String[] args = new String[]{"invalidArg"};
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithInvalidIntervalArgument() {
        String[] args = new String[]{"propertyFile", "invalidInterval"};
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithNegativeIntervalArgument() {
        String[] args = new String[]{"propertyFile", "-1"};
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(LagoonCLI.syntaxMsg);
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"propertyFile", "1000"};
        LagoonCLI.main(args);
        verifyNoMoreInteractions(System.out);
        verifyNoMoreInteractions(System.err);
    }

    @Test
    public void testInitializeLagoonWithValidPropertyFile() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        verify(sitemapFile, times(1)).exists();
        verify(TreeBuilder.class, times(1)).parseXML(ArgumentMatchers.eq(sitemapFile), ArgumentMatchers.anyBoolean());
        verify(processor, times(1)).__constructor__(ArgumentMatchers.eq("http://example.com"), ArgumentMatchers.eq(sitemapTree), ArgumentMatchers.eq("sourceDir"), ArgumentMatchers.eq("password"), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeLagoonWithMissingPassword() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");

        LagoonCLI.main(args);

        verify(sitemapFile, times(1)).exists();
        verify(TreeBuilder.class, times(1)).parseXML(ArgumentMatchers.eq(sitemapFile), ArgumentMatchers.anyBoolean());
        verify(processor, never()).__constructor__(ArgumentMatchers.eq("http://example.com"), ArgumentMatchers.eq(sitemapTree), ArgumentMatchers.eq("sourceDir"), ArgumentMatchers.eq(null), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeLagoonWithInvalidPassword() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("invalidPassword");

        LagoonCLI.main(args);

        verify(sitemapFile, times(1)).exists();
        verify(TreeBuilder.class, times(1)).parseXML(ArgumentMatchers.eq(sitemapFile), ArgumentMatchers.anyBoolean());
        verify(processor, never()).__constructor__(ArgumentMatchers.eq("http://example.com"), ArgumentMatchers.eq(sitemapTree), ArgumentMatchers.eq("sourceDir"), ArgumentMatchers.eq("invalidPassword"), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingPropertyFile() {
        String[] args = new String[]{"invalidFile.xml"};

        LagoonCLI.main(args);

        verify(sitemapFile, never()).exists();
        verify(TreeBuilder.class, never()).parseXML(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.anyBoolean());
        verify(processor, never()).__constructor__(ArgumentMatchers.anyString(), ArgumentMatchers.any(Element.class), ArgumentMatchers.any(File), ArgumentMatchers.anyString(), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test(expected = IOException.class)
    public void testInitializeLagoonWithIOException() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenThrow(new IOException("Error reading targetURL property"));

        LagoonCLI.main(args);

        verify(sitemapFile, never()).exists();
        verify(TreeBuilder.class, never()).parseXML(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.anyBoolean());
        verify(processor, never()).__constructor__(ArgumentMatchers.anyString(), ArgumentMatchers.any(Element.class), ArgumentMatchers.any(File), ArgumentMatchers.anyString(), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithLagoonException() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenThrow(new LagoonException("Error initializing Lagoon"));

        LagoonCLI.main(args);

        verify(sitemapFile, never()).exists();
        verify(TreeBuilder.class, never()).parseXML(ArgumentMatchers.any(InputStream.class), ArgumentMatchers.anyBoolean());
        verify(processor, never()).__constructor__(ArgumentMatchers.anyString(), ArgumentMatchers.any(Element.class), ArgumentMatchers.any(File), ArgumentMatchers.anyString(), ArgumentMatchers.eq(out), ArgumentMatchers.eq(err));
    }

    @Test
    public void testBuildWebsiteSuccessfully() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        verify(processor, times(1)).build(false);
    }

    @Test
    public void testBuildWebsiteWithErrors() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");
        when(processor.build(false)).thenReturn(false);

        LagoonCLI.main(args);

        verify(processor, times(1)).build(false);
        verify(out, times(1)).println("...with errors...");
    }

    @Test
    public void testForceBuildWebsiteSuccessfully() {
        String[] args = new String[]{"propertyFile.xml", "force"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        verify(processor, times(1)).build(true);
    }

    @Test
    public void testForceBuildWebsiteWithErrors() {
        String[] args = new String[]{"propertyFile.xml", "force"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");
        when(processor.build(true)).thenReturn(false);

        LagoonCLI.main(args);

        verify(processor, times(1)).build(true);
        verify(out, times(1)).println("...with errors...");
    }

    @Test
    public void testPeriodicBuilding() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");
        when(properties.getProperty("interval")).thenReturn("5000");

        LagoonCLI.main(args);

        verify(processor, never()).destroy();

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }

        verify(processor, times(1)).build(false);

        try {
            TimeUnit.MILLISECONDS.sleep(5500);
        } catch (InterruptedException e) {
            // Ignore
        }

        verify(processor, times(1)).build(false);

        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            // Ignore
        }

        verify(processor, times(1)).destroy();
    }

    @Test
    public void testQuit() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        verify(System.in, times(1)).readLine();
    }

    @Test
    public void testQuitWithInvalidCharacter() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        verify(System.in, times(1)).readLine();

        try {
            verify(System.in, times(1)).readLine();
            fail("Expected an error when reading from System.in");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testQuitWithInterruptedException() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        try {
            Thread thread = new Thread(() -> System.in.read());
            thread.start();
            thread.interrupt();
            thread.join();
        } catch (InterruptedException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testQuitWithNullInputStream() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        LagoonCLI.out = null;

        LagoonCLI.main(args);
    }

    @Test(expected = NullPointerException.class)
    public void testQuitWithNullErrorStream() {
        String[] args = new String[]{"propertyFile.xml"};
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("sourceDir");
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(args);

        LagoonCLI.err = null;

        LagoonCLI.main(args);
    }
}