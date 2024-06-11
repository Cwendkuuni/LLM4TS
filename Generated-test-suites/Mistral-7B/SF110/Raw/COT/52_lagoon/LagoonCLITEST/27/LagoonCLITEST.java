package nu.staldal.lagoon;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private static final String PROPERTY_FILE = "test.properties";
    private static final String SIZEMAP_FILE = "test.sitemap";

    @Before
    public void setUp() {
        LagoonCLI.DEBUG = false;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgsLength() {
        LagoonCLI.main(new String[0]);
    }

    @Test
    public void testValidArgsNoOption() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgWithNoFile() {
        String[] args = new String[]{"build"};
        LagoonCLI.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgInvalidOption() {
        String[] args = new String[]{"invalid"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidInterval() {
        String[] args = new String[]{"test.properties", "abc"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidInterval() {
        String[] args = new String[]{"test.properties", "5000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.interval > 0);
    }

    @Test(expected = LagoonException.class)
    public void testMissingPropertyFile() {
        String[] args = new String[]{"nonexistent.properties"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidPropertyFile() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
        String targetURL = LagoonCLI.getProperty("targetURL");
        String sitemapFile = LagoonCLI.getProperty("sitemapFile");
        String sourceDir = LagoonCLI.getProperty("sourceDir");
        assertNotNull(targetURL);
        assertNotNull(sitemapFile);
        assertNotNull(sourceDir);
    }

    @Test(expected = LagoonException.class)
    public void testMissingPassword() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidPassword() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        String password = LagoonCLI.properties.getProperty("password");
        assertNotNull(password);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingSitemapFile() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidSitemapFile() {
        String[] args = new String[]{"test.sitemap"};
        LagoonCLI.main(args);
        File sitemapFile = new File(LagoonCLI.getProperty("sitemapFile"));
        assertTrue(sitemapFile.exists());
    }

    @Test(expected = LagoonException.class)
    public void testInvalidPassword() {
        String[] args = new String[]{"test.xml", "wrongPassword"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidPasswordFile() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
        File sitemapFile = new File(LagoonCLI.getProperty("sitemapFile"));
        LagoonProcessor processor = new LagoonProcessor(LagoonCLI.getProperty("targetURL"),
                LagoonCLI.parseXML(sitemapFile), new File(LagoonCLI.getProperty("sourceDir")),
                LagoonCLI.properties.getProperty("password"), new PrintWriter(System.out, true),
                new PrintWriter(System.err, true));
        assertNotNull(processor);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMissingPasswordInPropertyFile() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testIncorrectPassword() {
        String[] args = new String[]{"test.xml", "incorrectPassword"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidBuild() {
        String[] args = new String[]{"test.sitemap"};
        LagoonCLI.main(args);
        LagoonProcessor processor = new LagoonProcessor(LagoonCLI.getProperty("targetURL"),
                LagoonCLI.parseXML(new File(LagoonCLI.getProperty("sitemapFile"))),
                new File(LagoonCLI.getProperty("sourceDir")),
                LagoonCLI.properties.getProperty("password"), new PrintWriter(System.out, true),
                new PrintWriter(System.err, true));
        boolean result = processor.build(false);
        assertTrue(result);
    }

    @Test
    public void testValidForceBuild() {
        String[] args = new String[]{"test.sitemap"};
        LagoonCLI.main(args);
        LagoonProcessor processor = new LagoonProcessor(LagoonCLI.getProperty("targetURL"),
                LagoonCLI.parseXML(new File(LagoonCLI.getProperty("sitemapFile"))),
                new File(LagoonCLI.getProperty("sourceDir")),
                LagoonCLI.properties.getProperty("password"), new PrintWriter(System.out, true),
                new PrintWriter(System.err, true));
        boolean result = processor.build(true);
        assertTrue(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMissingSourceDir() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidSourceDir() {
        String[] args = new String[]{"test.sitemap"};
        LagoonCLI.main(args);
        File sourceDir = new File(LagoonCLI.getProperty("sourceDir"));
        assertTrue(sourceDir.exists());
    }

    @Test(expected = IOException.class)
    public void testInvalidXML() {
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testValidXML() {
        String[] args = new String[]{"test.sitemap"};
        LagoonCLI.main(args);
        Element sitemapTree = LagoonCLI.parseXML(new File(LagoonCLI.getProperty("sitemapFile")));
        assertNotNull(sitemapTree);
    }

    @Test
    public void testShowTimeShortInterval() {
        long timeElapsed = LagoonCLI.showTime(10);
        String expectedOutput = "in 10 ms";
        String actualOutput = getConsoleOutput();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testShowTimeLongInterval() {
        long timeElapsed = LagoonCLI.showTime(12000);
        String expectedOutput = "in 11 s";
        String actualOutput = getConsoleOutput();
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testQuitCommand() {
        String[] args = new String[]{"test.properties"};
        LagoonCLI.main(args);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s = in.readLine();
        assertEquals("b", s.charAt(0));
        s = in.readLine();
        assertEquals("q", s.charAt(0));
    }

    private Element parseXML(File file) {
        try {
            InputSource inputSource = new InputSource(new FileInputStream(file));
            return TreeBuilder.parseXML(inputSource, false).getDocumentElement();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getConsoleOutput() {
        StringBuilder output = new StringBuilder();
        PrintStream consoleOut = System.out;
        try {
            PrintStream oldOut = new PrintStream(new ByteArrayOutputStream());
            System.setOut(new PrintStream(new ByteArrayOutputStream()));
            LagoonCLI.showTime(1000);
            output.append(System.out.getBuffer().getBytes());
            System.setOut(consoleOut);
        } finally {
            System.out.getBuffer().setLength(0);
        }
        return output.toString();
    }
}