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
import nu.staldal.lagoon.sax.TreeBuilder;
import nu.staldal.xtree.Element;

public class LagoonCLITest {

    private LagoonProcessor processor;
    private PrintWriter out;
    private PrintWriter err;

    @Before
    public void setup() {
        processor = mock(LagoonProcessor.class);
        out = mock(PrintWriter.class);
        err = mock(PrintWriter.class);
    }

    @Test
    public void testValidArguments() throws Exception {
        String[] args = new String[]{"validPropertyFile.xml"};
        when(processor.build(false)).thenReturn(true);
        LagoonCLI.main(args);
        verify(processor).initialize(anyString(), any(File.class), any(File.class), anyString(), eq(out), eq(err));
        verify(processor).build(false);
    }

    @Test(expected = LagoonException.class)
    public void testMissingArguments() throws Exception {
        String[] args = new String[0];
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test(expected = LagoonException.class)
    public void testInvalidArgumentFormat() throws Exception {
        String[] args = new String[]{"invalidArg"};
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidInterval() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "-1"};
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test
    public void testBuildWithSuccess() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "build"};
        when(processor.build(false)).thenReturn(true);
        LagoonCLI.main(args);
        verify(processor).initialize(anyString(), any(File.class), any(File.class), anyString(), eq(out), eq(err));
        verify(processor).build(false);
    }

    @Test
    public void testForceBuildWithSuccess() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "force"};
        when(processor.build(true)).thenReturn(true);
        LagoonCLI.main(args);
        verify(processor).initialize(anyString(), any(File.class), any(File.class), anyString(), eq(out), eq(err));
        verify(processor).build(true);
    }

    @Test
    public void testIntervalWithSuccess() throws Exception {
        String[] args = new String[]{"propertyFile.xml", "1000"};
        when(processor.build(false)).thenReturn(true);
        LagoonCLI.main(args);
        Thread.sleep(1100); // sleep for some time to let the test run for a while
        verifyNoMoreInteractions(processor);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileNotFound() throws Exception {
        String[] args = new String[]{"nonExistentFile.xml"};
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test(expected = IOException.class)
    public void testIOError() throws Exception {
        String[] args = new String[]{"propertyFile.xml"};
        when(processor.build(false)).thenThrow(new IOException("IO error"));
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMissingPassword() throws Exception {
        String[] args = new String[]{"propertyFile.xml"};
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test(expected = AuthenticationException.class)
    public void testIncorrectPassword() throws Exception {
        String[] args = new String[]{"propertyFile.xml"};
        when(processor.build(false)).thenThrow(new AuthenticationException("Password is incorrect"));
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }

    @Test
    public void testInvalidSitemap() throws Exception {
        String[] args = new String[]{"invalidSitemap.xml"};
        when(processor.initialize(anyString(), any(File.class), any(File.class), anyString(), eq(out), eq(err)))
                .thenThrow(new SAXException("Invalid sitemap file"));
        LagoonCLI.main(args);
        verifyNoInteractions(processor);
    }
}