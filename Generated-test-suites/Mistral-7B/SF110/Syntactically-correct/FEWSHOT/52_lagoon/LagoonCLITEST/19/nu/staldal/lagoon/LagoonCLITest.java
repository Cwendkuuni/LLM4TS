package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.*;
import java.io.*;
import static nu.staldal.xtree.TreeBuilder.fileToInputSource;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String actual = lagoonCLI.getProperty("targetURL");
        String expected = "http://example.com";
        assertEquals(expected, actual);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyNotSpecified() {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.getProperty("nonExistentProperty");
    }

    @Test
    public void testMain_NoArguments() {
        String[] args = new String[0];
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMain_InvalidArgument() {
        String[] args = {"invalidArgument"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = outContent.toString();
        System.setOut(System.out);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain_InvalidInterval() {
        String[] args = {"property_file", "-1"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = outContent.toString();
        System.setOut(System.out);
        assertEquals(expected, actual);
    }

    @Test
    public void testMain_PropertyFile() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"property_file.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Initializing Lagoon...";
        String actual = outContent.toString();
        System.setOut(System.out);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testMain_SitemapFile() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"sitemap_file.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Initializing Lagoon...";
        String actual = outContent.toString();
        System.setOut(System.out);
        assertTrue(actual.contains(expected));
    }

    @Test(expected = LagoonException.class)
    public void testMain_MissingPassword() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"property_file.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nPassword is required but not specified";
        String actual = outContent.toString();
        System.setOut(System.out);
        throw new AssertionError("Unexpected output: " + actual);
    }

    @Test(expected = LagoonException.class)
    public void testMain_IncorrectPassword() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"property_file.xml", "incorrectPassword"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nIncorrect password";
        String actual = outContent.toString();
        System.setOut(System.out);
        throw new AssertionError("Unexpected output: " + actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMain_FileNotFound() throws IOException, SAXException, LagoonException {
        String[] args = {"nonExistentFile.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nFile not found: nonExistentFile.xml";
        String actual = outContent.toString();
        System.setOut(System.out);
        throw new AssertionError("Unexpected output: " + actual);
    }

    @Test(expected = IOException.class)
    public void testMain_IOError() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"property_file.xml"};
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nI/O error:";
        String actual = errContent.toString();
        System.setErr(System.err);
        throw new AssertionError("Unexpected output: " + actual);
    }

    @Test(expected = LagoonException.class)
    public void testMain_LagoonException() throws FileNotFoundException, IOException, SAXException, LagoonException {
        String[] args = {"property_file.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:";
        String actual = errContent.toString();
        System.setOut(System.out);
        System.setErr(System.err);
        throw new AssertionError("Unexpected output: " + actual);
    }

    @Test
    public void testBuild() throws IOException, SAXException, InterruptedException, LagoonException {
        String[] args = {"property_file.xml"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        LagoonProcessor processor = lagoonCLI.getProcessor();
        processor.build(false);
        String expected = "Building website...";
        String actual = outContent.toString();
        System.setOut(System.out);
        System.setErr(System.err);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testForceBuild() throws IOException, SAXException, InterruptedException, LagoonException {
        String[] args = {"property_file.xml", "force"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        LagoonProcessor processor = lagoonCLI.getProcessor();
        processor.build(true);
        String expected = "Force building website...";
        String actual = outContent.toString();
        System.setOut(System.out);
        System.setErr(System.err);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testInterval() throws IOException, SAXException, InterruptedException, LagoonException {
        String[] args = {"property_file.xml", "5000"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        LagoonProcessor processor = lagoonCLI.getProcessor();
        processor.build(false);
        long interval = 5000;
        while (true) {
            processor.build(false);
            long timeElapsed = System.currentTimeMillis() - (long) ((new Date()).getTime() / 1000L - 5);
            if (timeElapsed >= interval) {
                break;
            }
            try {
                Thread.sleep(interval - timeElapsed);
            } catch (InterruptedException ignored) {}
        }
        String expected = "Exiting Lagoon...";
        String actual = outContent.toString();
        System.setOut(System.out);
        System.setErr(System.err);
        assertTrue(actual.contains(expected));
    }

    @Test
    public void testCommandLineArguments() {
        String[] args = {"property_file.xml", "build"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        boolean build = lagoonCLI.isBuild(args);
        assertTrue(build);

        args = new String[]{"property_file.xml", "force"};
        build = lagoonCLI.isForce(args);
        assertTrue(build);

        args = new String[]{"property_file.xml", "5000"};
        long interval = lagoonCLI.getInterval(args);
        assertEquals(5000, interval);

        args = new String[]{"property_file.xml"};
        build = lagoonCLI.isBuild(args);
        assertFalse(build);

        args = new String[]{"property_file.xml", "invalid"};
        build = lagoonCLI.isBuild(args);
        assertFalse(build);

        args = new String[]{"property_file.xml", "nonexistent"};
        interval = lagoonCLI.getInterval(args);
        assertEquals(-1, interval);
    }

    @Test
    public void testParseXML() throws FileNotFoundException, SAXException {
        String filePath = "src/test/resources/sitemap.xml";
        Element sitemapTree = TreeBuilder.parseXML(fileToInputSource(new File(filePath)), false);
        assertNotNull(sitemapTree);
    }
}