package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.xml.sax.SAXException;

import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private LagoonCLI lagoonCLI;
    private Properties properties;
    private File sitemapFile;
    private File propertyFile;

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        properties = new Properties();
        sitemapFile = Mockito.mock(File.class);
        propertyFile = Mockito.mock(File.class);
        lagoonCLI = new LagoonCLI();
    }

    @Test
    public void testInvalidArguments() {
        thrown.expect(IllegalArgumentException.class);
        lagoonCLI.main(new String[0]);
    }

    @Test
    public void testInvalidSyntax() {
        thrown.expect(RuntimeException.class);
        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "invalid"});
    }

    @Test
    public void testInvalidArgumentType() {
        thrown.expect(NumberFormatException.class);
        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file", "abc"});
    }

    @Test
    public void testPropertyFileLoading() throws IOException, SAXException {
        when(propertyFile.exists()).thenReturn(true);
        when(propertyFile.isFile()).thenReturn(true);
        when(propertyFile.getAbsolutePath()).thenReturn("path/to/property_file.xml");

        properties.load(Mockito.mock(FileInputStream.class).mockReturnValue(Mockito.mock(InputStream.class).mockReturnValue("property1=value1\nproperty2=value2")));

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file"});

        verify(System.out).println(SYNTAX_MSG);
        verifyNoMoreInteractions(System.out);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Property targetURL not specified");
    }

    @Test
    public void testBuildWithoutArguments() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).build(false);
        verify(System.out).println("Building website...");
        verifyZeroInteractions(System.err);
    }

    @Test
    public void testBuildWithArguments() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(true)).thenReturn(true);

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file", "build"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).build(true);
        verify(System.out).println("Force building website...");
        verifyZeroInteractions(System.err);
    }

    @Test
    public void testBuildWithInterval() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file", "1000"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).build(false);
        verify(System.out).println("Building website...");

        Thread thread = thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1100);
                } catch (InterruptedException e) {}
            }
        });

        thread.start();

        verify(processor).build(false);
        thread.join();
    }

    @Test
    public void testInvalidArgumentForInterval() {
        thrown.expect(NumberFormatException.class);
        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file", "-500"});
    }

    @Test
    public void testPropertyFileWithErrors() throws IOException, SAXException {
        when(propertyFile.exists()).thenReturn(true);
        when(propertyFile.isFile()).thenReturn(true);
        when(propertyFile.getAbsolutePath()).thenReturn("path/to/property_file.xml");

        properties.load(Mockito.mock(FileInputStream.class).mockReturnValue(Mockito.mock(InputStream.class).mockReturnValue("property1=value1\nproperty2=value2\nproperty3=value3\nproperty4=")));

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file"});

        verify(System.out).println(SYNTAX_MSG);
        verifyNoMoreInteractions(System.out);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Property targetURL not specified");
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Error while parsing property file: property4 is missing a value");
    }

    @Test
    public void testMissingFile() {
        when(sitemapFile.exists()).thenReturn(false);

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println(SYNTAX_MSG);
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("File not found: sitemap_file");
    }

    @Test
    public void testIOException() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        doThrow(IOException.class).when(processor).build(false);

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).build(false);
        verify(System.err).println("Error while building website:");
        verify(System.err).println("I/O error: ");
        verify(System.exit).withArguments(1);
    }

    @Test
    public void testAuthenticationMissingException() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        doThrow(new AuthenticationMissingException("Password is required but not specified")).when(processor).initialize();

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).initialize();
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Password is required but not specified");
        verify(System.exit).withArguments(1);
    }

    @Test
    public void testAuthenticationException() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        doThrow(new AuthenticationException("Incorrect password")).when(processor).initialize();

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).initialize();
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Incorrect password");
        verify(System.exit).withArguments(1);
    }

    @Test
    public void testLagoonException() {
        when(sitemapFile.exists()).thenReturn(true);
        when(sitemapFile.isFile()).thenReturn(true);
        when(sitemapFile.getAbsolutePath()).thenReturn("path/to/sitemap_file.xml");

        LagoonProcessor processor = mock(LagoonProcessor.class);
        doThrow(new LagoonException("Error while initializing Lagoon")).when(processor).initialize();

        lagoonCLI.main(new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file"});

        verify(System.out).println("Lagoon initialized successfully");
        verify(processor).initialize();
        verify(System.err).println("Error while initializing Lagoon:");
        verify(System.err).println("Error while initializing Lagoon");
        verify(System.exit).withArguments(1);
    }
}