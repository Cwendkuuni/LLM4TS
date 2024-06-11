package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PropertyReader;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.TreeBuilder;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.InputSource;

public class LagoonCLITest {

    private Properties properties;
    private File sitemapFile;
    private LagoonProcessor processor;
    private PrintWriter out;
    private PrintWriter err;

    @Before
    public void setUp() {
        properties = new Properties();
        sitemapFile = new File("test.sitemap.xml");
        out = mock(PrintWriter.class);
        err = mock(PrintWriter.class);
    }

    @After
    public void tearDown() {
        processor = null;
        sitemapFile.deleteOnExit();
    }

    @Test
    public void testMainWithInvalidArgument() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        verify(out, times(1)).println(syntaxMsg);
    }

    @Test
    public void testMainWithPropertyFile() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.properties"};
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "test.sitemap.xml");
        properties.setProperty("sourceDir", "src/test/resources");

        FileInputStream fis = new FileInputStream(sitemapFile);
        Element sitemapTree = TreeBuilder.parseXML(new InputSource(fis), false);
        processor = new LagoonProcessor("http://example.com", sitemapTree, new File("src/test/resources"), null, out, err);

        String[] validArgs = new String[]{"build"};
        LagoonCLI.main(args.concat(new String[0]));
        verify(processor, times(1)).build(false);

        verifyNoMoreInteractions(processor);
    }

    @Test
    public void testMainWithSitemapFile() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.sitemap.xml"};

        File file = new File("test.sitemap.xml");
        Element sitemapTree = new Element("sitemapindex");
        sitemapTree.addChild(new Element("sitemap"));
        sitemapTree.getChild(0).addChild(new Element("urlset", new String[]{"xmlns", "xmlns:xsi"}));
        TreeBuilder.writeXML(sitemapTree, file);

        String[] validArgs = new String[]{"build"};
        LagoonCLI.main(args.concat(new String[0]));
        verify(processor, times(1)).build(false);

        verifyNoMoreInteractions(processor);
        file.delete();
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"test.properties", "abc"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithValidInterval() {
        String[] args = new String[]{"test.properties", "1000"};
        LagoonCLI.main(args);
        verify(out, times(1)).println("Initializing Lagoon...");
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingProperty() throws IOException, SAXException {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMainWithMissingPassword() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.xml"};
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        processor = new LagoonProcessor("http://example.com", sitemapTree, new File("src/test/resources"), null, out, err);
        LagoonCLI.main(new String[]{"build"});
    }

    @Test(expected = AuthenticationException.class)
    public void testMainWithIncorrectPassword() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.xml", "123"};
        Element sitemapTree = TreeBuilder.parseXML(TreeBuilder.fileToInputSource(sitemapFile), false);
        processor = new LagoonProcessor("http://example.com", sitemapTree, new File("src/test/resources"), "123", out, err);
        LagoonCLI.main(new String[]{"build"});
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainWithMissingFile() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testMainWithIOError() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.xml", "build"};
        doThrow(new IOException("Test error")).when(out).println(anyString());
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithLagoonError() throws IOException, SAXException, LagoonException {
        String[] args = new String[]{"test.xml", "build"};
        doThrow(new LagoonException("Test error")).when(processor).build(anyBoolean());
        LagoonCLI.main(args);
    }
}