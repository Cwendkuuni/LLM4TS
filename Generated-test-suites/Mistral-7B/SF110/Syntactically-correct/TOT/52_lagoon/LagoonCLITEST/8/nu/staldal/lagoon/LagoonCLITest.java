package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.PropertiesLoader;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private LagoonProcessor processor;
    private PrintWriter out;
    private PrintWriter err;
    private Properties properties;

    @Before
    public void setup() {
        properties = new Properties();
        out = mock(PrintWriter.class);
        err = mock(PrintWriter.class);
        processor = new LagoonProcessor("http://example.com", null, null, null, out, err);
    }

    @Test
    public void testInvalidSyntax() {
        LagoonCLI.main(new String[]{"invalid_file"});
        verify(err, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testInvalidArgument1() {
        LagoonCLI.main(new String[]{"property_file", "invalid_argument"});
        verify(err, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testInvalidArgument2() {
        LagoonCLI.main(new String[]{"property_file", "build", "invalid_argument"});
        verify(err, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testInvalidArgument3() {
        LagoonCLI.main(new String[]{"property_file", "1a", "build"});
        verify(err, times(1)).println("Invalid interval: 1a");
    }

    @Test
    public void testValidCallWithPropertyFile() throws IOException, SAXException, LagoonException {
        when(properties.load(any(FileInputStream.class))).thenReturn(properties);
        when(properties.getProperty("targetURL")).thenReturn("http://example.com");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
        when(properties.getProperty("sourceDir")).thenReturn(new File("source_dir").getAbsolutePath());
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI.main(new String[]{"property_file"});
        verify(processor, times(1)).initialize();
    }

    @Test
    public void testValidCallWithSitemapFile() throws IOException, SAXException, LagoonException {
        File sitemapFile = new File("sitemap.xml");
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        when(out.println(anyString())).thenAnswer(i -> {});
        when(err.println(anyString())).thenAnswer(i -> {});

        LagoonCLI.main(new String[]{"sitemap_file"});
        verify(processor, times(1)).initialize();
        verify(processor, times(1)).build(false);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMissingPassword() throws IOException, SAXException, LagoonException {
        when(properties.getProperty("password")).thenReturn(null);
        LagoonCLI.main(new String[]{"property_file"});
    }

    @Test(expected = AuthenticationException.class)
    public void testInvalidPassword() throws IOException, SAXException, LagoonException {
        when(properties.getProperty("password")).thenReturn("invalid_password");
        LagoonCLI.main(new String[]{"property_file"});
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingPropertyFile() throws IOException, SAXException, LagoonException {
        LagoonCLI.main(new String[]{"missing_file.xml"});
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException, SAXException, LagoonException {
        when(properties.load(any(FileInputStream.class))).thenThrow(new IOException());
        LagoonCLI.main(new String[]{"property_file"});
    }
}