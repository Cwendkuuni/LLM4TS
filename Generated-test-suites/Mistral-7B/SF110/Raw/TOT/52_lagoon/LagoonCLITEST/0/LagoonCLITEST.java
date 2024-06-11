package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest
{
    @ org.junit.Before
    public void setUp()
    {
        // Set up mocks
        Properties mockProperties = mock(Properties.class);
        PrintWriter mockOut = mock(PrintWriter.class);
        PrintWriter mockErr = mock(PrintWriter.class);
        LagoonProcessor mockProcessor = mock(LagoonProcessor.class);
        when(mockProcessor.build(false)).thenReturn(true);
        when(mockProcessor.build(true)).thenReturn(false);
        when(mockProcessor.destroy()).thenReturn(true);

        // Initialize LagoonCLI with mocks
        LagoonCLI.properties = mockProperties;
        LagoonCLI.properties.load(mock(FileInputStream.class));
        LagoonCLI.main(new String[]{"test.xml", "build"});
        verify(mockProcessor).initialize(eq("test.xml"), eq(mockProperties), eq(mockOut), eq(mockErr));
    }

    @ org.junit.Test
    public void testMainWithValidArguments() throws IOException, LagoonException, SAXException
    {
        // Test main method with valid arguments
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);

        // Verify initialize method called with correct arguments
        verify(mockProcessor).initialize(eq("test.xml"), eq(mockProperties), eq(mockOut), eq(mockErr));
        verify(mockProcessor).build(false);
    }

    @ org.junit.Test
    public void testMainWithInvalidArguments()
    {
        // Test main method with invalid arguments
        String[] args = new String[]{"test.xml", "invalid"};
        LagoonCLI.main(args);

        // Verify no initialize method called
        verify(mockProcessor, never()).initialize(any(), any(), any(), any());
    }

    @ org.junit.Test
    public void testMainWithMissingPropertyFile()
    {
        // Test main method with missing property file
        String[] args = new String[]{"missing.xml", "build"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println(LagoonCLI.syntaxMsg);
    }

    @ org.junit.Test
    public void testMainWithMissingBuildArgument()
    {
        // Test main method with missing build argument
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println(LagoonCLI.syntaxMsg);
    }

    @ org.junit.Test
    public void testMainWithInvalidIntervalArgument()
    {
        // Test main method with invalid interval argument
        String[] args = new String[]{"test.xml", "invalid_interval"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println(LagoonCLI.syntaxMsg);
    }

    @ org.junit.Test
    public void testMainWithNegativeIntervalArgument()
    {
        // Test main method with negative interval argument
        String[] args = new String[]{"test.xml", "-1"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println(LagoonCLI.syntaxMsg);
    }

    @ org.junit.Test
    public void testMainWithMissingPasswordProperty() throws AuthenticationException
    {
        // Test main method with missing password property
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Password is required but not specified");
    }

    @ org.junit.Test
    public void testMainWithIncorrectPassword()
    {
        // Test main method with incorrect password
        String[] args = new String[]{"test.xml", "incorrect_password"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Incorrect password");
    }

    @ org.junit.Test
    public void testMainWithInvalidSitemapFile()
    {
        // Test main method with invalid sitemap file
        String[] args = new String[]{"test.xml", "test.xml"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("File not found: test.xml");
    }

    @ org.junit.Test
    public void testMainWithIOException() throws IOException
    {
        // Test main method with IOException
        doThrow(new IOException()).when(mockProperties).load(any());
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("I/O error: " + anyString());
    }

    @ org.junit.Test
    public void testMainWithLagoonException() throws LagoonException
    {
        // Test main method with LagoonException
        doThrow(new LagoonException("Test exception")).when(mockProcessor).initialize(any(), any(), any(), any());
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Error while initializing Lagoon: Test exception");
    }

    @ org.junit.Test
    public void testMainWithAuthenticationMissingException()
    {
        // Test main method with AuthenticationMissingException
        doThrow(new AuthenticationMissingException()).when(mockProcessor).initialize(any(), any(), any(), any());
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Error while initializing Lagoon: Password is required but not specified");
    }

    @ org.junit.Test
    public void testMainWithAuthenticationException()
    {
        // Test main method with AuthenticationException
        doThrow(new AuthenticationException("Test exception")).when(mockProcessor).initialize(any(), any(), any(), any());
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Error while initializing Lagoon: Incorrect password");
    }

    @ org.junit.Test
    public void testInitializeWithInvalidPropertyFile() throws IOException, LagoonException, SAXException
    {
        // Test initialize method with invalid property file
        String[] args = new String[]{"test.xml"};
        doThrow(new FileNotFoundException("Test file not found")).when(mockProperties).load(any());
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("File not found: test.xml");
    }

    @ org.junit.Test
    public void testInitializeWithInvalidSitemapFile() throws IOException, LagoonException, SAXException
    {
        // Test initialize method with invalid sitemap file
        String[] args = new String[]{"test.xml"};
        when(mockProperties.getProperty("sitemapFile")).thenReturn("test.xml");
        when(TreeBuilder.fileToInputSource(new File("test.xml"))).thenThrow(new SAXException("Test exception"));
        LagoonCLI.main(args);

        // Verify error message printed
        verify(mockErr).println("Error while initializing Lagoon: Test exception");
    }

    @ org.junit.Test
    public void testBuildWithSuccess() throws IOException, LagoonException, SAXException
    {
        // Test build method with success
        String[] args = new String[]{"test.xml", "build"};
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockOut).println("...successfully");
    }

    @ org.junit.Test
    public void testBuildWithErrors() throws IOException, LagoonException, SAXException
    {
        // Test build method with errors
        String[] args = new String[]{"test.xml", "build"};
        when(mockProcessor.build(false)).thenReturn(false);
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockErr).println("...with errors...");
    }

    @ org.junit.Test
    public void testForceBuildWithSuccess() throws IOException, LagoonException, SAXException
    {
        // Test force build method with success
        String[] args = new String[]{"test.xml", "force"};
        LagoonCLI.main(args);
        verify(mockProcessor).build(true);
        verify(mockOut).println("Force building website...");
        verify(mockOut).println("...successfully");
    }

    @ org.junit.Test
    public void testForceBuildWithErrors() throws IOException, LagoonException, SAXException
    {
        // Test force build method with errors
        String[] args = new String[]{"test.xml", "force"};
        when(mockProcessor.build(true)).thenReturn(false);
        LagoonCLI.main(args);
        verify(mockProcessor).build(true);
        verify(mockOut).println("Force building website...");
        verify(mockErr).println("...with errors...");
    }

    @ org.junit.Test
    public void testIntervalLoopWithSuccess() throws IOException, InterruptedException, LagoonException, SAXException
    {
        // Test interval loop with success
        String[] args = new String[]{"test.xml", "1000"};
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockOut).println("...successfully");
        Thread.sleep(1100);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockOut).println("...successfully");
        Thread.sleep(1000);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockOut).println("...successfully");
    }

    @ org.junit.Test
    public void testIntervalLoopWithError() throws IOException, InterruptedException, LagoonException, SAXException
    {
        // Test interval loop with error
        String[] args = new String[]{"test.xml", "1000"};
        when(mockProcessor.build(false)).thenReturn(false);
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockErr).println("...with errors...");
        Thread.sleep(1100);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockErr).println("...with errors...");
        Thread.sleep(1000);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockErr).println("...with errors...");
    }

    @ org.junit.Test
    public void testBuildOnKeyPressWithSuccess() throws IOException, InterruptedException, LagoonException, SAXException
    {
        // Test build on key press with success
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        verify(mockOut).println("Ready");
        Thread.sleep(100);
        verifyZeroInteractions(mockProcessor);
        Thread.sleep(1000);
        String input = "b";
        BufferedReader mockIn = mock(BufferedReader.class);
        when(mockIn.readLine()).thenReturn(input);
        BufferedReader in = mock(BufferedReader.class);
        when(System.in).thenReturn(in);
        when(in.readLine()).thenReturn(input);
        verifyZeroInteractions(mockProcessor);
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockOut).println("...successfully");
    }

    @ org.junit.Test
    public void testBuildOnKeyPressWithError() throws IOException, InterruptedException, LagoonException, SAXException
    {
        // Test build on key press with error
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        verify(mockOut).println("Ready");
        Thread.sleep(100);
        verifyZeroInteractions(mockProcessor);
        Thread.sleep(1000);
        String input = "b";
        BufferedReader mockIn = mock(BufferedReader.class);
        when(mockIn.readLine()).thenReturn(input);
        BufferedReader in = mock(BufferedReader.class);
        when(System.in).thenReturn(in);
        when(in.readLine()).thenReturn(input);
        when(mockProcessor.build(false)).thenReturn(false);
        LagoonCLI.main(args);
        verify(mockProcessor).build(false);
        verify(mockOut).println("Building website...");
        verify(mockErr).println("...with errors...");
    }

    @ org.junit.Test
    public void testBuildOnKeyPressWithQuit() throws IOException, InterruptedException, LagoonException, SAXException
    {
        // Test build on key press with quit
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        verify(mockOut).println("Ready");
        Thread.sleep(100);
        verifyZeroInteractions(mockProcessor);
        Thread.sleep(1000);
        String input = "q";
        BufferedReader mockIn = mock(BufferedReader.class);
        when(mockIn.readLine()).thenReturn(input);
        BufferedReader in = mock(BufferedReader.class);
        when(System.in).thenReturn(in);
        when(in.readLine()).thenReturn(input);
        LagoonCLI.main(args);
        verifyZeroInteractions(mockProcessor);
        verify(mockProcessor).destroy();
        verify(mockOut).println("Exiting Lagoon...");
        verifyZeroInteractions(mockProcessor);
    }
}