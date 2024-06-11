package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.PropertyReader;
import nu.staldal.lagoon.core.XTree;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private LagoonCLI lagoonCLI;
    private PrintWriter out;
    private PrintWriter err;
    private Properties properties;
    private XTree sitemapTree;

    @Before
    public void setup() {
        lagoonCLI = new LagoonCLI();
        out = mock(PrintWriter.class);
        err = mock(PrintWriter.class);
        properties = new Properties();
        sitemapTree = Mockito.mock(XTree.class);
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
    }

    @Test
    public void testValidArguments() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "build"};
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
    }

    @Test(expected = LagoonException.class)
    public void testInvalidArguments() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "invalidArgument"};
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidInterval() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "invalidInterval"};
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]");
    }

    @Test
    public void testMissingFile() throws IOException, LagoonException {
        String[] args = new String[] {"missingFile.xml", "build"};
        lagoonCLI.main(args);
        verify(System.err, times(1)).println("Error while initializing Lagoon:\nFile not found: missingFile.xml");
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingPropertyFile() throws IOException, LagoonException {
        String[] args = new String[] {"missingPropertyFile.xml", "build"};
        when(sitemapTree.getDocument()).thenThrow(new FileNotFoundException("Missing property file"));
        lagoonCLI.main(args);
        verify(System.err, times(1)).println("Error while initializing Lagoon:\nFile not found: missingPropertyFile.xml");
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMissingPassword() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        lagoonCLI.main(args);
        verify(System.err, times(1)).println("Error while initializing Lagoon:\nPassword is required but not specified");
    }

    @Test(expected = AuthenticationException.class)
    public void testIncorrectPassword() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("incorrectPassword");
        lagoonCLI.main(args);
        verify(System.err, times(1)).println("Error while initializing Lagoon:\nIncorrect password");
    }

    @Test
    public void testBuildSuccess() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "build"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sitemapFile").item(0).getTextContent()).thenReturn("/sitemap.xml");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("targetURL").item(0).getTextContent()).thenReturn("http://example.com");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sourceDir").item(0).getTextContent()).thenReturn("/src");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("password");
        when(TreeBuilder.fileToInputSource(new File("/sitemap.xml"))).thenReturn(Mockito.mock(org.xml.sax.InputSource.class));
        when(TreeBuilder.parseXML(Mockito.any(org.xml.sax.InputSource.class), Mockito.anyBoolean())).thenReturn(sitemapTree);
        when(new File("/src/index.html").exists()).thenReturn(true);
        when(new File("/src/index.html").canWrite()).thenReturn(true);
        when(new PrintWriter(Mockito.any(FileWriter.class), Mockito.anyBoolean())).then(writer -> {
            writer.write("Test content");
            writer.close();
        });
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
        verify(System.out, times(1)).println("...successfully");
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testBuildFailure() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "build"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sitemapFile").item(0).getTextContent()).thenReturn("/sitemap.xml");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("targetURL").item(0).getTextContent()).thenReturn("http://example.com");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sourceDir").item(0).getTextContent()).thenReturn("/src");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("password");
        when(TreeBuilder.fileToInputSource(new File("/sitemap.xml"))).thenReturn(Mockito.mock(org.xml.sax.InputSource.class));
        when(TreeBuilder.parseXML(Mockito.any(org.xml.sax.InputSource.class), Mockito.anyBoolean())).thenReturn(sitemapTree);
        when(new File("/src/index.html").exists()).thenReturn(false);
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
        verify(System.out, times(1)).println("Failed to build website");
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testForceBuildSuccess() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "force"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sitemapFile").item(0).getTextContent()).thenReturn("/sitemap.xml");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("targetURL").item(0).getTextContent()).thenReturn("http://example.com");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sourceDir").item(0).getTextContent()).thenReturn("/src");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("password");
        when(TreeBuilder.fileToInputSource(new File("/sitemap.xml"))).thenReturn(Mockito.mock(org.xml.sax.InputSource.class));
        when(TreeBuilder.parseXML(Mockito.any(org.xml.sax.InputSource.class), Mockito.anyBoolean())).thenReturn(sitemapTree);
        when(new File("/src/index.html").exists()).thenReturn(true);
        when(new File("/src/index.html").canWrite()).thenReturn(false);
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Force building website...");
        verify(System.out, times(1)).println("...successfully (overwrote existing file)");
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testForceBuildFailure() throws IOException, LagoonException {
        String[] args = new String[] {"propertyFile.xml", "force"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sitemapFile").item(0).getTextContent()).thenReturn("/sitemap.xml");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("targetURL").item(0).getTextContent()).thenReturn("http://example.com");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sourceDir").item(0).getTextContent()).thenReturn("/src");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("password");
        when(TreeBuilder.fileToInputSource(new File("/sitemap.xml"))).thenReturn(Mockito.mock(org.xml.sax.InputSource.class));
        when(TreeBuilder.parseXML(Mockito.any(org.xml.sax.InputSource.class), Mockito.anyBoolean())).thenReturn(sitemapTree);
        when(new File("/src/index.html").exists()).thenReturn(false);
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Force building website...");
        verify(System.out, times(1)).println("Failed to build website (permission denied)");
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testInterval() throws IOException, LagoonException, InterruptedException {
        String[] args = new String[] {"propertyFile.xml", "1000"};
        when(sitemapTree.getDocument()).thenReturn(Mockito.mock(org.w3c.dom.Document.class));
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sitemapFile").item(0).getTextContent()).thenReturn("/sitemap.xml");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("targetURL").item(0).getTextContent()).thenReturn("http://example.com");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("sourceDir").item(0).getTextContent()).thenReturn("/src");
        when(sitemapTree.getDocument().getDocumentElement().getElementsByTagName("password").item(0).getTextContent()).thenReturn("password");
        when(TreeBuilder.fileToInputSource(new File("/sitemap.xml"))).thenReturn(Mockito.mock(org.xml.sax.InputSource.class));
        when(TreeBuilder.parseXML(Mockito.any(org.xml.sax.InputSource.class), Mockito.anyBoolean())).thenReturn(sitemapTree);
        when(new File("/src/index.html").exists()).thenReturn(true);
        when(new File("/src/index.html").canWrite()).thenReturn(true);
        when(new PrintWriter(Mockito.any(FileWriter.class), Mockito.anyBoolean())).then(writer -> {
            writer.write("Test content");
            writer.close();
        });
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
        verify(System.out, times(1)).println("...successfully");
        verify(System.out, times(1)).println("Exiting Lagoon...");
        Thread.sleep(1100);
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
        verify(System.out, times(1)).println("...successfully");
        verify(System.out, times(1)).println("Exiting Lagoon...");
        Thread.sleep(1001);
        lagoonCLI.main(args);
        verify(System.out, times(1)).println("Lagoon initialized successfully");
        verify(System.out, times(1)).println("Building website...");
        verify(System.out, times(1)).println("...successfully");
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }
}