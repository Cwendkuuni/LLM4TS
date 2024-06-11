package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testInitializeWithPropertyFile() {
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
        assertFalse(LagoonCLI.properties.isEmpty());
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() {
        String[] args = {"invalid_property_file"};
        LagoonCLI.main(args);
    }

    @Test
    public void testInitializeWithSitemapFile() {
        String[] args = {"sitemap.xml"};
        LagoonCLI.main(args);
        assertNull(LagoonCLI.properties);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithInvalidSitemapFile() {
        String[] args = {"invalid_sitemap_file"};
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildWithoutArguments() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        LagoonCLI.main(new String[]{"property_file.xml", "build"});
        assertTrue(processor.build(false) == true);
        processor.verify(processor).destroy();
    }

    @Test(expected = LagoonException.class)
    public void testBuildWithInvalidArgument() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        String[] args = {"property_file.xml", "invalid"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testBuildWithInvalidInterval() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        props.setProperty("password", "password");
        String[] args = {"property_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildWithInterval() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        props.setProperty("password", "password");
        String[] args = {"property_file.xml", "5000"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(processor.getOutputWriter()).thenReturn(out);
        when(processor.getErrorWriter()).thenReturn(err);
        when(out.flush()).thenReturn(true);
        when(err.flush()).thenReturn(true);
        when(out.write("Building website...")).thenReturn(true);
        when(err.write("Building website...")).thenReturn(true);
        when(out.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(err.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(out.write("Exiting Lagoon...")).thenReturn(true);
        when(err.write("Exiting Lagoon...")).thenReturn(true);
        when(out.close()).thenReturn(true);
        when(err.close()).thenReturn(true);
        when(processor.build(true)).thenReturn(true);
        LagoonCLI.main(new String[]{"property_file.xml", "build", "5000"});
        processor.verify(processor).build(false);
        processor.verify(processor).build(true);
        processor.verify(processor).destroy();
    }

    @Test
    public void testForceBuildWithInterval() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        props.setProperty("password", "password");
        String[] args = {"property_file.xml", "5000", "force"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(false);
        when(processor.build(true)).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(processor.getOutputWriter()).thenReturn(out);
        when(processor.getErrorWriter()).thenReturn(err);
        when(out.flush()).thenReturn(true);
        when(err.flush()).thenReturn(true);
        when(out.write("Force building website...")).thenReturn(true);
        when(err.write("Force building website...")).thenReturn(true);
        when(out.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(err.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(out.write("Exiting Lagoon...")).thenReturn(true);
        when(err.write("Exiting Lagoon...")).thenReturn(true);
        when(out.close()).thenReturn(true);
        when(err.close()).thenReturn(true);
        when(processor.build(false)).thenReturn(false);
        LagoonCLI.main(new String[]{"property_file.xml", "5000", "force"});
        processor.verify(processor).build(false);
        processor.verify(processor).build(true);
        processor.verify(processor).destroy();
    }

    @Test(expected = InterruptedIOException.class)
    public void testBuildWithIntervalAndInterrupted() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        props.setProperty("password", "password");
        String[] args = {"property_file.xml", "5000"};
        LagoonCLI.main(args);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenThrow(new InterruptedIOException());
        when(processor.destroy()).thenReturn(true);
        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(processor.getOutputWriter()).thenReturn(out);
        when(processor.getErrorWriter()).thenReturn(err);
        when(out.flush()).thenReturn(true);
        when(err.flush()).thenReturn(true);
        when(out.write("Building website...")).thenReturn(true);
        when(err.write("Building website...")).thenReturn(true);
        when(out.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(err.printf("%d s", 5000 / 1000L)).thenReturn(true);
        when(out.write("Exiting Lagoon...")).thenReturn(true);
        when(err.write("Exiting Lagoon...")).thenReturn(true);
        when(out.close()).thenReturn(true);
        when(err.close()).thenReturn(true);
        when(processor.build(true)).thenReturn(true);
        Thread thread = new Thread(() -> LagoonCLI.main(new String[]{"property_file.xml", "5000"}));
        thread.start();
        thread.interrupt();
        thread.join();
    }

    @Test
    public void testConsoleInput() {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://localhost");
        props.setProperty("sitemapFile", "sitemap.xml");
        props.setProperty("sourceDir", "/home/user/website");
        props.setProperty("password", "password");
        String[] args = {"property_file.xml"};
        LagoonCLI.main(args);
        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("b");
        when(in.readLine()).thenReturn("b");
        when(in.readLine()).thenReturn("f");
        when(in.readLine()).thenReturn("f");
        when(in.readLine()).thenReturn("q");
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(processor.build(false)).thenReturn(true);
        when(processor.build(true)).thenReturn(true);
        when(processor.destroy()).thenReturn(true);
        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(processor.getOutputWriter()).thenReturn(out);
        when(processor.getErrorWriter()).thenReturn(err);
        when(out.flush()).thenReturn(true);
        when(err.flush()).thenReturn(true);
        when(out.write("Building website...")).thenReturn(true);
        when(err.write("Building website...")).thenReturn(true);
        when(out.write("Exiting Lagoon...")).thenReturn(true);
        when(err.write("Exiting Lagoon...")).thenReturn(true);
        when(out.close()).thenReturn(true);
        when(err.close()).thenReturn(true);
        when(processor.build(false)).thenReturn(false);
        when(processor.build(true)).thenReturn(false);
        when(processor.destroy()).thenReturn(true);
        LagoonCLI.main(new String[]{"property_file.xml"});
        processor.verify(processor).build(false);
        processor.verify(processor).build(false);
        processor.verify(processor).build(true);
        processor.verify(processor).build(true);
        processor.verify(processor).destroy();
    }
}