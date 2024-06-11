package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testMainWithInvalidArgumentCount() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithInvalidArgumentType() {
        String[] args = new String[]{"test.xml", "abc"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithInvalidIntervalArgument() {
        String[] args = new String[]{"property.xml", "abc123"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithInvalidBuildOrForceArgument() {
        String[] args = new String[]{"property.xml", "buildX"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithInvalidIntervalArgumentValue() {
        String[] args = new String[]{"property.xml", "1a"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithNegativeIntervalArgumentValue() {
        String[] args = new String[]{"property.xml", "-1"};
        LagoonCLI.main(args);
        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property.xml", "5000"};
        String expectedOutput = "Initializing Lagoon...";
        LagoonCLI.main(args);
        assertEquals(expectedOutput, System.out.getStackTrace()[0].getMessage());
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        String expectedOutput = "http://example.com";
        assertEquals(expectedOutput, LagoonCLI.getProperty("targetURL"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyIsNotSpecified() throws LagoonException {
        Properties properties = new Properties();
        String expectedMessage = "Property targetURL not specified";
        LagoonCLI.getProperty("targetURL");
        assertEquals(expectedMessage, System.err.getStackTrace()[0].getMessage());
    }

    @Test
    public void testShowTime() {
        long time = 12345;
        String expectedOutput = "in 12345 ms";
        LagoonCLI.showTime(time);
        assertEquals(expectedOutput, System.out.getStackTrace()[0].getMessage());

        time = 12345678;
        expectedOutput = "in 12.345 s";
        LagoonCLI.showTime(time);
        assertEquals(expectedOutput, System.out.getStackTrace()[0].getMessage());
    }

    @Test(expected = FileNotFoundException.class)
    public void testParseXMLWithFileNotFound() throws IOException, SAXException {
        File sitemapFile = new File("not_existing.xml");
        LagoonProcessor processor = new LagoonProcessor(null, null, null, null, null, null);
        LagoonCLI.parseXML(processor, sitemapFile);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeLagoonWithMissingPassword() {
        File sitemapFile = new File("sitemap.xml");
        LagoonProcessor processor = new LagoonProcessor(null, null, null, null, null, null);
        LagoonCLI.initializeLagoon(processor, sitemapFile, null);
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeLagoonWithIncorrectPassword() {
        File sitemapFile = new File("sitemap.xml");
        String incorrectPassword = "incorrect_password";
        LagoonProcessor processor = new LagoonProcessor(null, null, null, incorrectPassword, null, null);
        LagoonCLI.initializeLagoon(processor, sitemapFile, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingPropertyFile() {
        String[] args = new String[]{"sitemap.xml", "5000"};
        LagoonCLI.main(args);
        String expectedErrorMessage = "File not found: ";
        LagoonProcessor processor = new LagoonProcessor(null, null, null, null, null, null);
        LagoonCLI.initializeLagoon(processor, null, args[0]);
    }
}