package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.cli.LagoonCLI;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.core.TreeBuilder;
import nu.staldal.xtree.Element;

public class LagoonCLITest {

    @Test
    public void testSyntax() {
        LagoonCLI cli = new LagoonCLI(new String[0]);
        cli.main(new String[0]);
        assertTrue(cli.isSyntaxShown());
    }

    @Test
    public void testMainWithPropertyFile() throws Exception {
        LagoonCLI cli = new LagoonCLI(new String[]{"property.xml", "build"});
        cli.main(new String[0]);

        Properties properties = cli.getProperties();
        assertEquals("property.xml", properties.getProperty("property_file"));
        assertEquals("build", properties.getProperty("command"));

        String targetURL = cli.getProperty("targetURL");
        File sitemapFile = cli.getSitemapFile();
        File sourceDir = cli.getSourceDir();
        String password = cli.getProperty("password");

        assertTrue(targetURL.endsWith("/"));
        assertEquals("property.xml", targetURL.substring(0, targetURL.length() - 1));

        assertEquals(new File("property.xml"), sitemapFile);
        assertEquals(new File("."), sourceDir);
        assertNull(password);
    }

    @Test
    public void testMainWithSitemapFile() throws Exception {
        LagoonCLI cli = new LagoonCLI(new String[]{"sitemap.xml", "build"});
        cli.main(new String[0]);

        Properties properties = cli.getProperties();
        assertNull(properties.getProperty("property_file"));
        assertEquals("build", properties.getProperty("command"));

        String targetURL = cli.getProperty("targetURL");
        File sitemapFile = cli.getSitemapFile();
        File sourceDir = cli.getSourceDir();
        String password = cli.getProperty("password");

        assertNull(targetURL);
        assertEquals(new File("sitemap.xml"), sitemapFile);
        assertEquals(new File("."), sourceDir);
        assertNull(password);
    }

    @Test
    public void testMainWithInvalidInterval() throws Exception {
        LagoonCLI cli = new LagoonCLI(new String[]{"property.xml", "-1"});
        cli.main(new String[0]);

        assertTrue(cli.isSyntaxShown());
    }

    @Test
    public void testMainWithNegativeInterval() throws Exception {
        LagoonCLI cli = new LagoonCLI(new String[]{"property.xml", "-5000"});
        cli.main(new String[0]);

        assertTrue(cli.isSyntaxShown());
    }

    @Test
    public void testMainWithValidInterval() throws Exception {
        LagoonCLI cli = new LagoonCLI(new String[]{"property.xml", "5000"});
        cli.main(new String[0]);

        Properties properties = cli.getProperties();
        assertEquals("property.xml", properties.getProperty("property_file"));
        assertEquals("5000", properties.getProperty("command"));
    }

    @Test
    public void testInitializeWithMissingPassword() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProperty("password")).thenReturn(null);

        try {
            new LagoonProcessor(null, null, null, null, null, null);
            fail("AuthenticationMissingException expected");
        }
        catch (AuthenticationMissingException e) {
            // expected
        }
    }

    @Test
    public void testInitializeWithIncorrectPassword() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        Properties properties = new Properties();
        properties.setProperty("password", "wrongPassword");
        when(cli.getProperties()).thenReturn(properties);

        try {
            new LagoonProcessor(null, null, null, null, null, null);
            fail("AuthenticationException expected");
        }
        catch (AuthenticationException e) {
            // expected
        }
    }

    @Test
    public void testInitializeWithValidPassword() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        Properties properties = new Properties();
        properties.setProperty("password", "correctPassword");
        when(cli.getProperties()).then
                (() -> properties);

        Element sitemapTree = mock(Element.class);
        when(sitemapTree.getAttributeValue("loc")).thenReturn("http://example.com/sitemap.xml");

        try {
            new LagoonProcessor(null, sitemapTree, null, "correctPassword", null, null);
        }
        catch (Exception e) {
            fail("Unexpected exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuild() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenReturn(true);

        cli.main(new String[]{"property.xml", "build"});
        assertTrue(cli.isBuildCompleted());
    }

    @Test
    public void testForceBuild() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(true)).thenReturn(true);

        cli.main(new String[]{"property.xml", "force"});
        assertTrue(cli.isBuildCompleted());
    }

    @Test
    public void testLoop() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenReturn(false);

        cli.main(new String[]{"property.xml", "5000"});

        Thread thread = new Thread(() -> {
            try {
                cli.main(new String[]{"property.xml", "5000"});
            }
            catch (Exception e) {
                fail("Unexpected exception thrown: " + e.getMessage());
            }
        });
        thread.start();
        thread.join();

        assertTrue(cli.isBuildCompleted());
    }

    @Test
    public void testQuit() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        BufferedReader in = mock(BufferedReader.class);
        when(cli.getInputStream()).thenReturn(System.in);
        when(cli.getBufferedReader()).thenReturn(in);

        String input = "q";
        when(in.readLine()).thenReturn(input);

        cli.main(new String[0]);

        verify(in).close();
    }

    @Test
    public void testBuildCommand() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenReturn(true);

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "build"});

        verify(out).println("Building website...");
        verify(out).println("...done.");
        verify(err).println("");
    }

    @Test
    public void testForceBuildCommand() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(true)).thenReturn(true);

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "force"});

        verify(out).println("Force building website...");
        verify(out).println("...done.");
        verify(err).println("");
    }

    @Test
    public void testLoopCommand() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenReturn(false);

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "5000"});

        verify(out).println("Building website...");
        verify(out).println("...done.");
        verify(err).println("");
        verify(out, times(2)).println("Building website...");
        verify(out, times(2)).println("...done.");
        verify(err, times(2)).println("");
    }

    @Test
    public void testQuitCommand() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        BufferedReader in = mock(BufferedReader.class);
        when(cli.getInputStream()).thenReturn(System.in);
        when(cli.getBufferedReader()).thenReturn(in);

        String input = "q";
        when(in.readLine()).thenReturn(input);

        cli.main(new String[0]);

        verify(in).close();
        verify(in).readLine();
    }

    @Test
    public void testBuildWithError() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenThrow(new IOException("Build failed"));

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "build"});

        verify(out).println("Building website...");
        verify(err).println("I/O error: Build failed");
    }

    @Test
    public void testForceBuildWithError() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(true)).thenThrow(new IOException("Build failed"));

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "force"});

        verify(out).println("Force building website...");
        verify(err).println("I/O error: Build failed");
    }

    @Test
    public void testLoopWithError() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);
        when(cli.getProperties()).thenReturn(new Properties());
        when(cli.getProcessor()).thenReturn(processor);

        when(processor.build(false)).thenThrow(new IOException("Build failed"));

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);
        when(cli.getOutputStream()).thenReturn(out);
        when(cli.getErrorStream()).thenReturn(err);

        cli.main(new String[]{"property.xml", "5000"});

        verify(out).println("Building website...");
        verify(out).println("...done.");
        verify(err).println("I/O error: Build failed");
        verify(out, times(2)).println("Building website...");
        verify(out, times(2)).println("...done.");
        verify(err, times(2)).println("I/O error: Build failed");
    }

    @Test
    public void testThreads() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        when(cli.getThreads()).thenReturn(new Thread[0]);

        cli.main(new String[0]);

        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        assertEquals(0, threads.length);
    }

    @Test
    public void testThreadsWithRunningThreads() throws Exception {
        LagoonCLI cli = mock(LagoonCLI.class);
        Thread thread1 = mock(Thread.class);
        Thread thread2 = mock(Thread.class);
        when(cli.getThreads()).thenReturn(new Thread[]{thread1, thread2});

        cli.main(new String[0]);

        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        assertArrayEquals(new Thread[]{thread1, thread2}, threads);
    }
}