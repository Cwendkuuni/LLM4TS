package nu.staldal.lagoon;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.PropertyLoader;
import nu.staldal.lagoon.sax.TreeBuilder;
import nu.staldal.xtree.Element;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private LagoonProcessor processor;
    private Properties properties;

    @Before
    public void setup() {
        properties = new Properties();
        processor = mock(LagoonProcessor.class);
    }

    @Test
    public void testInvalidArgumemts() {
        when(System.out::println).then(invocation -> System.out.println(SYNTAX_MSG));
        LagoonCLI.main(new String[0]);
        verify(System.out, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testInvalidInterval() {
        String[] args = {"property_file", "abcd"};
        when(System.out::println).then(invocation -> System.out.println(SYNTAX_MSG));
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testInvalidBuildOrForce() {
        String[] args = {"property_file", "invalid"};
        when(System.out::println).then(invocation -> System.out.println(SYNTAX_MSG));
        LagoonCLI.main(args);
        verify(System.out, times(1)).println(SYNTAX_MSG);
    }

    @Test
    public void testValidArgumentsPropertyFile() throws IOException, LagoonException {
        String[] args = {"property_file.xml"};
        when(System.out::println).then(invocation -> System.out.println("Initializing Lagoon..."));
        when(System.getProperty("user.dir")).thenReturn("/home/user");
        File propertyFile = Mockito.mock(File.class);
        when(propertyFile.exists()).thenReturn(true);
        when(propertyFile.getName()).thenReturn("property_file.xml");
        when(new File(System.getProperty("user.dir"))).thenReturn(new File("/home/user"));
        when(new File("/home/user/property_file.xml")).thenReturn(propertyFile);

        LagoonCLI.main(args);

        ArgumentCaptor<Element> sitemapTreeArgumentCaptor = ArgumentCaptor.forClass(Element.class);
        verify(processor).initialize(eq("/home/user/property_file.xml"), sitemapTreeArgumentCaptor.capture());
        Element sitemapTree = sitemapTreeArgumentCaptor.getValue();
        verify(processor).build(false);
    }

    @Test
    public void testValidArgumentsSitemapFile() throws IOException, LagoonException {
        String[] args = {"sitemap_file.xml"};
        when(System.out::println).then(invocation -> System.out.println("Initializing Lagoon..."));
        File propertyFile = Mockito.mock(File.class);
        when(propertyFile.exists()).thenReturn(false);
        when(new File(args[0])).thenReturn(propertyFile);

        LagoonCLI.main(args);

        verify(processor).initialize(eq(null), Mockito.any(Element.class));
        verify(processor).build(false);
    }

    @Test
    public void testPropertyFileWithInvalidXML() throws IOException, LagoonException, SAXException {
        String[] args = {"property_file_invalid.xml"};
        when(System.out::println).then(invocation -> System.out.println("Error while initializing Lagoon:"));
        File propertyFile = Mockito.mock(File.class);
        when(propertyFile.exists()).thenReturn(true);
        when(propertyFile.getName()).thenReturn("property_file_invalid.xml");
        when(new File(System.getProperty("user.dir"))).thenReturn(new File("/home/user"));
        when(new File("/home/user/property_file_invalid.xml")).thenReturn(propertyFile);
        when(TreeBuilder.fileToInputSource(propertyFile)).thenThrow(new SAXException());

        try {
            LagoonCLI.main(args);
            fail("Expected an exception");
        } catch (LagoonException e) {
            // Expected
        }
    }

    @Test
    public void testPasswordMissing() throws IOException, LagoonException, AuthenticationMissingException {
        String[] args = {"property_file.xml"};
        when(System.out::println).then(invocation -> System.out.println("Error while initializing Lagoon:"));

        LagoonCLI.main(args);

        verify(System.out, times(1)).println("Error while initializing Lagoon:\nPassword is required but not specified");
    }

    @Test
    public void testIncorrectPassword() throws IOException, LagoonException, AuthenticationException {
        String[] args = {"property_file.xml"};
        when(System.out::println).then(invocation -> System.out.println("Error while initializing Lagoon:"));
        when(System.getProperty("user.dir")).thenReturn("/home/user");
        File propertyFile = Mockito.mock(File.class);
        when(propertyFile.exists()).thenReturn(true);
        when(propertyFile.getName()).thenReturn("property_file.xml");
        when(new File(System.getProperty("user.dir"))).thenReturn(new File("/home/user"));
        when(new File("/home/user/property_file.xml")).thenReturn(propertyFile);
        when(TreeBuilder.parseXML(Mockito.any(InputSource.class), Mockito.eq(false))).thenReturn(Mockito.mock(Element.class));
        when(LagoonCLI.properties.getProperty("password")).thenReturn("wrong_password");

        LagoonCLI.main(args);

        verify(System.out, times(1)).println("Error while initializing Lagoon:\nIncorrect password");
    }

    @Test
    public void testBuildSuccess() throws IOException, LagoonException {
        String[] args = {"property_file.xml"};
        when(System.out::println).then(invocation -> System.out.println("Building website..."));
        when(System.currentTimeMillis()).thenReturn(1L);
        when(processor.build(false)).thenReturn(true);
        when(System.out::println).then(invocation -> System.out.println("...successfully built in 1 ms"));

        LagoonCLI.main(args);

        verify(processor, times(1)).build(eq(false));
    }

    @Test
    public void testForceBuildSuccess() throws IOException, LagoonException {
        String[] args = {"property_file.xml", "force"};
        when(System.out::println).then(invocation -> System.out.println("Force building website..."));
        when(System.currentTimeMillis()).thenReturn(1L);
        when(processor.build(true)).thenReturn(true);
        when(System.out::println).then(invocation -> System.out.println("...successfully forced built in 1 ms"));

        LagoonCLI.main(args);

        verify(processor, times(1)).build(eq(true));
    }

    @Test
    public void testIntervalBuild() throws IOException, LagoonException, InterruptedException {
        String[] args = {"property_file.xml", "1000"};
        when(System.out::println).then(invocation -> System.out.println("Building website..."));
        when(System.currentTimeMillis()).thenReturn(1L);
        when(processor.build(false)).thenReturn(true);
        when(System.out::println).then(invocation -> System.out.println("...successfully built in 1 ms"));

        LagoonCLI.main(args);

        Thread.sleep(500);

        verify(processor, times(1)).build(eq(false));
        verify(Thread.class).sleep(Mockito.atLeast(500L));
    }

    @Test
    public void testQuit() throws IOException, InterruptedException {
        when(System.in::readLine).thenReturn(null);

        LagoonCLI.main(new String[0]);

        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testQuitWithB() throws IOException, InterruptedException {
        when(System.in::readLine).thenReturn("b");

        LagoonCLI.main(new String[0]);

        verify(processor, times(1)).build(eq(false));
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testQuitWithF() throws IOException, InterruptedException {
        when(System.in::readLine).thenReturn("f");

        LagoonCLI.main(new String[0]);

        verify(processor, times(1)).build(eq(true));
        verify(System.out, times(1)).println("Exiting Lagoon...");
    }

    @Test
    public void testQuitWithQ() throws IOException, InterruptedException {
        when(System.in::readLine).thenReturn("q");

        LagoonCLI.main(new String[0]);

        verify(System.out, times(1)).println("Exiting Lagoon...");
    }
}