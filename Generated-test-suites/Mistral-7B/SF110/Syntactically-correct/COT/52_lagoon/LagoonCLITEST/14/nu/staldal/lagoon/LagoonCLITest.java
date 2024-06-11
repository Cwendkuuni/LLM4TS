package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonProcessor;
import nu.staldal.lagoon.core.LagoonProcessorException;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @org.junit.Test
    public void testMainWithPropertyFileAndNoArguments() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.MAIN_Message_Initializing_Lagoon_has_been_printed());
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithNoArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        fail("Expected LagoonException to be thrown.");
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testMainWithInvalidArgument() {
        String[] args = new String[]{"test.properties", "invalid"};
        LagoonCLI.main(args);
        fail("Expected LagoonException to be thrown.");
    }

    @org.junit.Test
    public void testMainWithPropertyFileAndBuildArgument() {
        String[] args = new String[]{"test.properties", "build"};
        Properties properties = mock(Properties.class);
        when(properties.getProperty("targetURL")).thenReturn("http://localhost");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
        when(properties.getProperty("sourceDir")).thenReturn(new File("source").getAbsolutePath());
        when(properties.getProperty("password")).thenReturn("password");
        doThrow(new LagoonException("Exception while building")).when(properties).load(any(FileInputStream.class));

        LagoonCLI lagoonCLI = spy(new LagoonCLI());
        doCallRealMethod().when(lagoonCLI).main(args);

        LagoonCLI.main(args);
        verify(lagoonCLI).main(args);
        verify(properties, times(1)).load(any(FileInputStream.class));
        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("sitemap.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).build(eq(false));
        verify(lagoonCLI).destroy();
    }

    @org.junit.Test
    public void testMainWithPropertyFileAndForceArgument() {
        String[] args = new String[]{"test.properties", "force"};
        Properties properties = mock(Properties.class);
        when(properties.getProperty("targetURL")).thenReturn("http://localhost");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
        when(properties.getProperty("sourceDir")).thenReturn(new File("source").getAbsolutePath());
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI lagoonCLI = spy(new LagoonCLI());
        doCallRealMethod().when(lagoonCLI).main(args);

        LagoonCLI.main(args);
        verify(lagoonCLI).main(args);
        verify(properties, times(1)).load(any(FileInputStream.class));
        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("sitemap.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).build(eq(true));
        verify(lagoonCLI).destroy();
    }

    @org.junit.Test
    public void testMainWithPropertyFileAndIntervalArgument() {
        String[] args = new String[]{"test.properties", "1000"};
        Properties properties = mock(Properties.class);
        when(properties.getProperty("targetURL")).thenReturn("http://localhost");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
        when(properties.getProperty("sourceDir")).thenReturn(new File("source").getAbsolutePath());
        when(properties.getProperty("password")).thenReturn("password");

        LagoonCLI lagoonCLI = spy(new LagoonCLI());
        doCallRealMethod().when(lagoonCLI).main(args);

        LagoonCLI.main(args);
        verify(lagoonCLI).main(args);
        verify(properties, times(1)).load(any(FileInputStream.class));
        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("sitemap.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI, times(Long.parseLong("1000"))).build(eq(false));
    }

    @org.junit.Test(expected = NumberFormatException.class)
    public void testMainWithInvalidIntervalArgument() {
        String[] args = new String[]{"test.properties", "invalid"};
        LagoonCLI.main(args);
        fail("Expected NumberFormatException to be thrown.");
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testInitializeWithInvalidPropertyFile() {
        Properties properties = mock(Properties.class);
        when(properties.getProperty("targetURL")).thenReturn("http://localhost");
        when(properties.getProperty("sitemapFile")).thenReturn("invalidFile.xml");

        LagoonCLI lagoonCLI = spy(new LagoonCLI());
        doCallRealMethod().when(lagoonCLI).initialize(anyString(), any(File.class), any(File.class), anyString());

        LagoonCLI.main(new String[]{"test.properties", "build"});
        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("invalidFile.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).build(eq(false));
        verify(lagoonCLI).destroy();

        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("invalidFile.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).initializeCalls(1);
        verify(lagoonCLI).buildCalls(0);
        verify(lagoonCLI).destroyCalls(1);
    }

    @org.junit.Test
    public void testInitialize() throws IOException, SAXException, LagoonException, LagoonProcessorException {
        Properties properties = mock(Properties.class);
        when(properties.getProperty("targetURL")).thenReturn("http://localhost");
        when(properties.getProperty("sitemapFile")).thenReturn(new File("sitemap.xml").getAbsolutePath());
        when(properties.getProperty("sourceDir")).thenReturn(new File("source").getAbsolutePath());
        when(properties.getProperty("password")).thenReturn("password");

        Element sitemapTree = mock(Element.class);
        when(sitemapTree.getFirstChild()).thenReturn(mock(Element.class));

        LagoonProcessor processor = mock(LagoonProcessor.class);

        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(sitemapTree);
        when(processor.build(anyBoolean())).thenReturn(true);

        LagoonCLI lagoonCLI = spy(new LagoonCLI());

        doCallRealMethod().when(lagoonCLI).initialize(anyString(), any(File.class), any(File.class), anyString());

        LagoonCLI.main(new String[]{"test.properties", "build"});
        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("sitemap.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).build(eq(false));
        verify(lagoonCLI).destroy();

        verify(lagoonCLI).initialize(eq("http://localhost"), eq(new File("sitemap.xml")), eq(new File("source")), eq("password"));
        verify(lagoonCLI).initializeCalls(1);
        verify(lagoonCLI).buildCalls(1);
        verify(lagoonCLI).destroyCalls(1);
    }

    private boolean MAIN_Message_Initializing_Lagoon_has_been_printed() {
        Pattern pattern = Pattern.compile("Initializing Lagoon\\.\n", Pattern.MULTILINE);
        String log = System.out.getLowerCaseBuffer().getBuffer().toString();
        return pattern.matcher(log).find();
    }
}