package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    private static final String SYNTAX_MSG = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private static Properties properties;
    private LagoonProcessor processor;

    @Before
    public void setUp() {
        properties = new Properties();
        processor = new LagoonProcessor("http://example.com", null, null, null, null, null);
    }

    @Test
    public void testInvalidArguments() {
        String[] args = new String[0];
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (LagoonException e) {
            assertEquals(SYNTAX_MSG, e.getMessage());
        }

        args = new String[]{"property_file"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (LagoonException e) {
            assertEquals("Property file and arguments are missing", e.getMessage());
        }

        args = new String[]{"property_file", "build"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (IOException e) {
            assertEquals("Property file not found", e.getMessage());
        }

        args = new String[]{"property_file", "123"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (NumberFormatException e) {
            fail("Should have thrown a LagoonException instead");
        } catch (LagoonException e) {
            assertEquals("Invalid interval value: '123'", e.getMessage());
        }

        args = new String[]{"property_file", "build", "invalid_file.xml"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (SAXException e) {
            fail("Should have thrown a LagoonException instead");
        } catch (IOException e) {
            fail("Should have thrown a LagoonException instead");
        } catch (LagoonException e) {
                assertEquals("Error while parsing sitemap file", e.getMessage());
        }

        args = new String[]{"property_file", "force"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (AuthenticationMissingException e) {
            assertEquals("Password is required but not specified", e.getMessage());
        }

        args = new String[]{"property_file", "password"};
        try {
            LagoonCLI.main(args);
            fail("Expected an error message");
        } catch (AuthenticationException e) {
            assertEquals("Incorrect password", e.getMessage());
        }
    }

    @Test
    public void testValidArguments() throws IOException, SAXException, InterruptedException, LagoonException {
        File sitemapFile = new File("sitemap.xml");
        String[] args = new String[]{"property_file", "build", sitemapFile.getAbsolutePath()};
        LagoonCLI.main(args);
        assertNotNull(processor);
    }
}