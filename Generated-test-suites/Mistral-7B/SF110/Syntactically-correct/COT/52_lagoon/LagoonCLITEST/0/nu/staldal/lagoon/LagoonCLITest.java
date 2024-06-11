package nu.staldal.lagoon; // package name should be updated accordingly

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;
import org.xml.sax.*;
import nu.staldal.lagoon.*; // import all necessary classes
import nu.staldal.xtree.*;
import org.mockito.*;
import org.mockito.annotations.BeforeMethod;
import org.mockito.annotations.Mock;

public class LagoonCLITest {
    // mock objects
    @Mock
    private Properties properties;

    @Mock
    private PrintWriter out;

    @Mock
    private PrintWriter err;

    @Mock
    private LagoonProcessor processor;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    

    @Test
    public void testMainWithInvalidArguments() {
        // given
        String[] args = new String[0];

        // when
        LagoonCLI.main(args);

        // then
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrors().toString());
    }

    @Test
    public void testMainWithInvalidInterval() {
        // given
        String[] args = new String[]{"property_file", "invalid"};

        // when
        LagoonCLI.main(args);

        // then
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrors().toString());
    }

    @Test
    public void testMainWithInvalidBuildOrForce() {
        // given
        String[] args = new String[]{"property_file", "invalid"};

        // when
        LagoonCLI.main(args);

        // then
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getErrors().toString());
    }

    @Test
    public void testMainWithXmlFile() {
        // given
        String[] args = new String[]{"sitemap.xml"};

        // when
        LagoonCLI.main(args);

        // then
        verify(processor, never()).build(true);
        verify(processor, never()).build(false);
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test
    public void testMainWithPropertyFile() {
        // given
        String[] args = new String[]{"properties.xml"};

        // when
        LagoonCLI.main(args);

        // then
        verify(processor, never()).build(true);
        verify(processor, never()).build(false);
        verify(processor, atLeastOnce()).initialize(anyString(), any(File.class), any(File.class), null, any(PrintWriter.class), any(PrintWriter.class));
        verify(out, atLeastOnce()).println("Initializing Lagoon...");
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingPropertyFile() {
        // given
        String[] args = new String[]{"missing_properties.xml"};

        // when
        LagoonCLI.main(args);

        // then
        fail("Should throw LagoonException");
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        // given
        String[] args = new String[]{"property_file", "-1"};

        // when
        LagoonCLI.main(args);

        // then
        fail("Should throw NumberFormatException");
    }

    @Test
    public void testMainWithBuildOption() {
        // given
        String[] args = new String[]{"property_file", "build"};

        // when
        LagoonCLI.main(args);

        // then
        verify(processor, times(1)).build(false);
    }

    @Test
    public void testMainWithForceOption() {
        // given
        String[] args = new String[]{"property_file", "force"};

        // when
        LagoonCLI.main(args);

        // then
        verify(processor, times(1)).build(true);
    }

    @Test
    public void testMainWithIntervalOption() {
        // given
        String[] args = new String[]{"property_file", "1000"};

        // when
        LagoonCLI.main(args);

        // then
        verify(processor, never()).build(true);
        verify(processor, never()).build(false);
        verify(processor, times(1)).setBuildInterval(1000);
    }

    @Test
    public void testInitializeLagoonWithInvalidPropertyFile() {
        // given
        String propertyFile = "missing_properties.xml";

        // when
        try {
            LagoonCLI.initialize(propertyFile);
        } catch (Exception e) {
            // then
            assertEquals(LagoonException.class, e.getClass());
        }
    }

    @Test
    public void testInitializeLagoonWithInvalidUrl() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://invalid_url");

        // when
        try {
            LagoonCLI.initialize(properties);
        } catch (Exception e) {
            // then
            assertEquals(AuthenticationMissingException.class, e.getClass());
        }
    }

    @Test
    public void testInitializeLagoonWithInvalidPassword() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("password")).thenReturn("invalid_password");

        // when
        try {
            LagoonCLI.initialize(properties);
        } catch (Exception e) {
            // then
            assertEquals(AuthenticationException.class, e.getClass());
        }
    }

    @Test
    public void testInitializeLagoonWithMissingPassword() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("password")).thenReturn(null);

        // when
        try {
            LagoonCLI.initialize(properties);
        } catch (Exception e) {
            // then
            assertEquals(AuthenticationMissingException.class, e.getClass());
        }
    }

    @Test
    public void testInitializeLagoonSuccessfully() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("password")).thenReturn("valid_password");
        when(properties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(properties.getProperty("sourceDir")).thenReturn("source_dir");

        doReturn(processor).when(LagoonCLI).initializeProcessor(properties, out, err);

        // when
        LagoonCLI.initialize(properties);

        // then
        verify(processor, times(1)).initialize(anyString(), any(File.class), any(File.class), null, eq(out), eq(err));
        verify(out, times(1)).println("Initializing Lagoon...");
    }

    @Test
    public void testBuildWebsiteSuccessfully() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(processor.build(false)).thenReturn(true);

        // when
        LagoonCLI.main(new String[]{"property_file"});

        // then
        verify(processor, times(1)).build(false);
        verify(out, times(1)).println("Building website...");
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test
    public void testBuildWebsiteWithErrors() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(processor.build(false)).thenReturn(false);

        // when
        LagoonCLI.main(new String[]{"property_file"});

        // then
        verify(processor, times(1)).build(false);
        verify(out, times(1)).println("Building website...");
        verify(out, times(1)).println("...with errors...");
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test
    public void testForceBuildWebsiteSuccessfully() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(processor.build(true)).thenReturn(true);

        // when
        LagoonCLI.main(new String[]{"property_file", "force"});

        // then
        verify(processor, times(1)).build(true);
        verify(out, times(1)).println("Force building website...");
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test
    public void testForceBuildWebsiteWithErrors() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(processor.build(true)).thenReturn(false);

        // when
        LagoonCLI.main(new String[]{"property_file", "force"});

        // then
        verify(processor, times(1)).build(true);
        verify(out, times(1)).println("Force building website...");
        verify(out, times(1)).println("...with errors...");
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    @Test
    public void testPeriodicBuilding() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(properties.getProperty("interval")).thenReturn("1000");

        doReturn(processor).when(LagoonCLI).initializeProcessor(properties, out, err);

        // when
        LagoonCLI.main(new String[]{"property_file"});

        // then
        verify(processor, times(1)).initialize(anyString(), any(File.class), any(File.class), null, eq(out), eq(err));
        verify(out, times(1)).println("Initializing Lagoon...");

        // when
        long timeBefore = System.currentTimeMillis();
        LagoonCLI.main(new String[]{"property_file"});
        long timeElapsed = System.currentTimeMillis() - timeBefore;

        // then
        verify(processor, times(1)).build(false);
        verify(out, times(1)).println("Building website...");
        verify(out, never()).println("...with errors...");

        if (timeElapsed < 1000) {
            try {
                Thread.sleep(1001 - timeElapsed);
            } catch (InterruptedException e) {
            }
        }

        // when
        LagoonCLI.main(new String[]{"property_file"});

        // then
        verify(processor, times(2)).build(false);
        verify(out, times(2)).println("Building website...");
        verify(out, never()).println("...with errors...");

        // when
        timeBefore = System.currentTimeMillis();
        LagoonCLI.main(new String[]{"property_file"});
        timeElapsed = System.currentTimeMillis() - timeBefore;

        // then
        if (timeElapsed < 1000) {
            try {
                Thread.sleep(1001 - timeElapsed);
            } catch (InterruptedException e) {
            }
        }

        // when
        LagoonCLI.main(new String[]{"property_file"});

        // then
        verify(processor, times(3)).build(false);
        verify(out, times(3)).println("Building website...");
        verify(out, never()).println("...with errors...");
    }

    @Test
    public void testQuiting() {
        // given
        when(properties.getProperty("targetURL")).thenReturn("http://target_url");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml"));
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir"));
        when(processor.destroy()).thenReturn(true);

        // when
        LagoonCLI.main(new String[]{"property_file"});
        LagoonCLI.main(new String[]{"property_file", "q"});

        // then
        verify(processor, times(1)).build(false);
        verify(processor, times(1)).destroy();
        verifyNoMoreInteractions(out);
        verifyNoMoreInteractions(err);
    }

    
}