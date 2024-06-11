package nu.staldal.lagoon;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.xml.sax.helper.DefaultHandler.getDefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testValidArguments() throws Exception {
        String[] args = new String[]{"config.properties", "build"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMissingArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
    }

    @Test
    public void testInvalidArgumentBuild() throws Exception {
        String[] args = new String[]{"config.properties", "invalid"};
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testInvalidArgumentForce() throws Exception {
        String[] args = new String[]{"config.properties", "force"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInvalidArgumentIntervalNegative() throws Exception {
        String[] args = new String[]{"config.properties", "-1"};
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testInvalidArgumentIntervalZero() throws Exception {
        String[] args = new String[]{"config.properties", "0"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidArgumentsWithSitemap() throws Exception {
        String sitemapPath = "/path/to/sitemap.xml";
        String[] args = new String[]{sitemapPath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testInvalidSitemapFile() throws Exception {
        String sitemapPath = "/path/to/invalidFile";
        String[] args = new String[]{sitemapPath, "build"};
        assertThrows(FileNotFoundException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testInvalidPropertyFile() throws Exception {
        String propertyFilePath = "/path/to/invalidFile";
        String[] args = new String[]{propertyFilePath, "build"};
        assertThrows(FileNotFoundException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testMissingPasswordProperty() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        assertThrows(AuthenticationMissingException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testInvalidPassword() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String password = "invalidPassword";
        String[] args = new String[]{propertyFilePath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        properties.setProperty("password", password);
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        assertThrows(AuthenticationException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testBuildSuccess() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildFailure() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenThrow(new SAXException());
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(false);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testForceBuildSuccess() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "force"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(true))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testForceBuildFailure() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "force"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenThrow(new SAXException());
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(true))).thenReturn(false);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testIntervalSuccess() throws Exception {
        long interval = 1000L;
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "1000"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
    }

    @Test
    public void testInvalidInterval() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "invalid"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testDebugOutput() throws Exception {
        String propertyFilePath = "/path/to/config.properties";
        String[] args = new String[]{propertyFilePath, "build"};
        Properties properties = new Properties();
        InputStream is = new FileInputStream(new File("config.properties"));
        properties.load(is);
        is.close();
        when(TreeBuilder.parseXML(Mockito.mock(InputStream.class), Mockito.eq(false))).thenReturn(mock(Element.class));
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(Mockito.eq(false))).thenReturn(true);
        when(processor.destroy()).thenReturn(null);
        LagoonCLI.properties = properties;
        LagoonCLI.DEBUG = true;
        LagoonCLI.main(args);
    }
}