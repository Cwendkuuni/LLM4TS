package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.xml.sax.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;

import java.io.*;
import nu.staldal.lagoon.core.*;
import java.util.*;
import nu.staldal.xtree.*;

public class LagoonCLITest {

    @Test
    public void testInitializeLagoon() {
        String[] args = new String[]{"property_file"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        Properties properties = new Properties();
        FileInputStream fis = mock(FileInputStream.class);
        when(fis.readAllBytes()).thenReturn("targetURL=http://test.com\nsitemapFile=sitemap.xml\nsourceDir=./src\npassword=testpassword".getBytes());
        when(fis.available()).thenReturn(1000);

        when(Properties.class.getMethod("load", FileInputStream.class)).thenAnswer(invocation -> properties.load(fis));

        lagoonCLI.properties = properties;

        try {
            lagoonCLI.main(args);
            assertEquals("http://test.com", lagoonCLI.getProperty("targetURL"));
            assertEquals(new File("./src/sitemap.xml"), lagoonCLI.getProperty("sitemapFile"));
            assertEquals(new File("./src"), lagoonCLI.getProperty("sourceDir"));
            assertEquals("testpassword", lagoonCLI.properties.getProperty("password"));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithMissingProperty() {
        String[] args = new String[]{"property_file"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        FileInputStream fis = mock(FileInputStream.class);
        when(fis.readAllBytes()).thenReturn("targetURL=http://test.com\nsitemapFile=sitemap.xml\nsourceDir=./src".getBytes());

        lagoonCLI.properties = new Properties();
        lagoonCLI.main(args);

        try {
            lagoonCLI.getProperty("password");
            fail();
        } catch (LagoonException e) {
            assertEquals("Property password not specified", e.getMessage());
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testInitializeLagoonWithInvalidInterval() {
        String[] args = new String[]{"property_file", "invalid"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithNegativeInterval() {
        String[] args = new String[]{"property_file", "-1"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
    }

    @Test
    public void testBuildWebsite() {
        String[] args = new String[]{"sitemap_file", "build"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        Element sitemapTree = mock(Element.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);

        when(sitemapTree.getTagName()).thenReturn("sitemap");

        lagoonCLI.main(args);

        try {
            lagoonCLI.main(new String[]{"sitemap_file", "build"});
            verify(processor, times(1)).build(false);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testForceBuildWebsite() {
        String[] args = new String[]{"sitemap_file", "force"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        Element sitemapTree = mock(Element.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);

        when(sitemapTree.getTagName()).thenReturn("sitemap");

        lagoonCLI.main(args);

        try {
            lagoonCLI.main(new String[]{"sitemap_file", "force"});
            verify(processor, times(1)).build(true);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testInterval() {
        String[] args1 = new String[]{"property_file", "1000"};
        String[] args2 = new String[]{"property_file", "5000"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        Element sitemapTree = mock(Element.class);
        LagoonProcessor processor = mock(LagoonProcessor.class);

        when(sitemapTree.getTagName()).thenReturn("sitemap");

        lagoonCLI.main(args1);
        lagoonCLI.main(args2);

        try {
            long interval1 = lagoonCLI.interval;
            long interval2 = lagoonCLI.interval;
            assertEquals(5000, interval2);
            assertNotEquals(1000, interval2);
            assertNotEquals(1000, interval1);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testRunWithConsoleInput() throws IOException, InterruptedException {
        String[] args = new String[]{"property_file"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("b");

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);

        try {
            lagoonCLI.main(args);

            verify(out, times(1)).println("Building website...");
            verify(out, times(1)).println("...with errors...");
            verify(err, never()).println("Error while initializing Lagoon:");
            verify(err, never()).println("Password is required but not specified");
            verify(err, never()).println("Incorrect password");
            verify(err, never()).println("File not found: ");
            verify(err, never()).println("I/O error: ");
            verify(err, never()).println("Error while initializing Lagoon:");

            Thread.sleep(100);
            verify(out, times(1)).flush();
            verify(err, times(1)).flush();
        } catch (Exception e) {
            fail();
        } finally {
            in.close();
            out.close();
            err.close();
        }
    }

    @Test
    public void testRunWithConsoleInputWithForce() throws IOException, InterruptedException {
        String[] args = new String[]{"property_file"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("f");

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);

        try {
            lagoonCLI.main(args);

            verify(out, times(1)).println("Force building website...");
            verify(out, times(1)).println("...with errors...");
            verify(err, never()).println("Error while initializing Lagoon:");
            verify(err, never()).println("Password is required but not specified");
            verify(err, never()).println("Incorrect password");
            verify(err, never()).println("File not found: ");
            verify(err, never()).println("I/O error: ");
            verify(err, never()).println("Error while initializing Lagoon:");

            Thread.sleep(100);
            verify(out, times(1)).flush();
            verify(err, times(1)).flush();
        } catch (Exception e) {
            fail();
        } finally {
            in.close();
            out.close();
            err.close();
        }
    }

    @Test
    public void testRunWithConsoleInputWithQuit() throws IOException, InterruptedException {
        String[] args = new String[]{"property_file"};
        LagoonCLI lagoonCLI = new LagoonCLI();

        BufferedReader in = mock(BufferedReader.class);
        when(in.readLine()).thenReturn("q");

        PrintWriter out = mock(PrintWriter.class);
        PrintWriter err = mock(PrintWriter.class);

        try {
            lagoonCLI.main(args);

            verify(out, never()).println("Building website...");
            verify(out, never()).println("...with errors...");
            verify(err, never()).println("Error while initializing Lagoon:");
            verify(err, never()).println("Password is required but not specified");
            verify(err, never()).println("Incorrect password");
            verify(err, never()).println("File not found: ");
            verify(err, never()).println("I/O error: ");
            verify(err, never()).println("Error while initializing Lagoon:");

            Thread.sleep(100);
            verify(out, times(1)).flush();
            verify(err, times(1)).flush();
        } catch (Exception e) {
            fail();
        } finally {
            in.close();
            out.close();
            err.close();
        }
    }

    @Test
    public void testShowTime() {
        LagoonCLI lagoonCLI = new LagoonCLI();

        long time = 12345;

        lagoonCLI.showTime(time);

        if (time < 10000) {
            assertTrue(lagoonCLI.out.contains("in " + time + " ms"));
        } else {
            assertTrue(lagoonCLI.out.contains("in " + time / 1000L + " s"));
        }
    }

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();

        properties.setProperty("targetURL", "http://test.com");

        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;

        assertEquals("http://test.com", lagoonCLI.getProperty("targetURL"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() {
        Properties properties = new Properties();

        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.properties = properties;

        try {
            lagoonCLI.getProperty("targetURL");
            fail();
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }
}