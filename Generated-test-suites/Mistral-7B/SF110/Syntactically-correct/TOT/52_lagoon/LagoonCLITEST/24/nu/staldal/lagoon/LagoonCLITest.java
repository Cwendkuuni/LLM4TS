package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.Core.AuthenticationException;
import nu.staldal.lagoon.Core.AuthenticationMissingException;
import nu.staldal.lagoon.Core.LagoonException;
import nu.staldal.lagoon.Core.LagoonProcessor;
import nu.staldal.lagoon.Core.XmlTree.TreeBuilder;
import nu.staldal.xtree.InputSource;

public class LagoonCLITest {

    private LagoonProcessor mockProcessor;
    private Properties mockProperties;

    @Before
    public void setUp() {
        mockProperties = new Properties();
        mockProcessor = mock(LagoonProcessor.class);
    }

    @Test
    public void testInvalidArgument() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");

        String[] args = new String[0];
        LagoonCLI.main(args);

        verify(stdoutInputStream(), times(1)).println(syntaxMsg);
        verify(stderrInputStream(), never()).println("Error while initializing Lagoon:");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testInvalidInterval() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("password");

        String[] args = new String[]{"properties.xml", "-1"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Syntax:");
        verify(stderrInputStream(), never()).println("Error while initializing Lagoon:");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testValidArguments() throws IOException, SAXException, LagoonException, InterruptedException {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("password");

        File sitemapFile = mock(File.class);
        when(sitemapFile.exists()).thenReturn(true);

        InputSource inputSource = mock(InputSource.class);
        TreeBuilder treeBuilder = mock(TreeBuilder.class);
        when(TreeBuilder.parseXML(inputSource, false)).thenReturn(mock(Element.class));

        when(mockProcessor.build(false)).thenReturn(true);

        String[] args = new String[]{"properties.xml", "5000"};
        LagoonCLI.main(args);

        verify(stdoutInputStream(), times(1)).println("Lagoon initialized successfully");
        verify(stdoutInputStream(), times(1)).println("Building website...");
        verify(stdoutInputStream(), times(1)).println("...successfully");
        verify(stdoutInputStream(), times(1)).println("Exiting Lagoon...");
        verify(mockProcessor, times(1)).build(false);
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testInvalidPassword() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("wrong_password");

        String[] args = new String[]{"properties.xml"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Error while initializing Lagoon:");
        verify(stderrInputStream(), times(1)).println("Incorrect password");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testFileNotFound() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("password");

        File sitemapFile = mock(File.class);
        when(sitemapFile.exists()).thenReturn(false);

        String[] args = new String[]{"properties.xml"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Error while initializing Lagoon:");
        verify(stderrInputStream(), times(1)).println("File not found: sitemap.xml");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testIOException() throws IOException, SAXException, LagoonException {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("password");

        File sitemapFile = mock(File.class);
        when(sitemapFile.exists()).thenReturn(true);

        InputSource inputSource = mock(InputSource.class);
        TreeBuilder treeBuilder = mock(TreeBuilder.class);
        when(TreeBuilder.parseXML(inputSource, false)).thenThrow(new IOException("Error while parsing XML"));

        String[] args = new String[]{"properties.xml"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Error while initializing Lagoon:");
        verify(stderrInputStream(), times(1)).println("I/O error: Error while parsing XML");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testAuthenticationMissingException() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn(null);

        String[] args = new String[]{"properties.xml"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Error while initializing Lagoon:");
        verify(stderrInputStream(), times(1)).println("Password is required but not specified");
        verifyNoMoreInteractions(mockProcessor);
    }

    @Test
    public void testAuthenticationException() {
        when(mockProperties.getProperty("targetURL")).thenReturn("http://example.com");
        when(mockProperties.getProperty("sitemapFile")).thenReturn("sitemap.xml");
        when(mockProperties.getProperty("password")).thenReturn("password");

        doThrow(new AuthenticationException("Authentication failed")).when(mockProcessor).build(false);

        String[] args = new String[]{"properties.xml"};
        LagoonCLI.main(args);

        verify(stderrInputStream(), times(1)).println("Error while initializing Lagoon:");
        verify(stderrInputStream(), times(1)).println("Incorrect password");
        verifyNoMoreInteractions(mockProcessor);
    }
}