import static org.junit.Assert.*;
import org.junit.Test;
import static java.lang.System.out;

public class LagoonCLITest {
    private final static String SYNTAX_ERROR_MESSAGE = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
    private final static String PROPERTY_FILE_PATH = "test_property_file.xml";
    private final static String SITEMAP_FILE_PATH = "test_sitemap_file.xml";

    @Test
    public void testMain() {
        String[] args = new String[]{"property_file"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.properties != null);
        assertEquals("expected_targetURL", LagoonCLI.getProperty("targetURL"));
        assertEquals("expected_sitemap_file_path", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("expected_source_dir_path", LagoonCLI.getProperty("sourceDir"));
        // Add more assertions for typical use cases

        args = new String[]{"property_file", "invalid_interval"};
        LagoonCLI.main(args);
        assertEquals(SYNTAX_ERROR_MESSAGE, out.getHWB().toString());
        // Add more assertions for edge cases and error scenarios

        args = new String[]{"property_file", "build"};
        LagoonCLI.main(args);
        // Add code to test the build functionality

        args = new String[]{"property_file", "force"};
        LagoonCLI.main(args);
        // Add code to test the force build functionality

        args = new String[]{"property_file", "interval_less_than_zero"};
        LagoonCLI.main(args);
        assertEquals(SYNTAX_ERROR_MESSAGE, out.getHWB().toString());
    }

    @Test
    public void testGetProperty() {
        String name = "targetURL";
        String propertyValue = LagoonCLI.getProperty(name);
        assertNotNull(propertyValue);
        assertEquals("expected_targetURL", propertyValue);

        name = "non_existent_property";
        try {
            LagoonCLI.getProperty(name);
            fail("LagoonException expected");
        } catch (LagoonException e) {
            assertEquals("Property non_existent_property not specified", e.getMessage());
        }

        name = "password";
        try {
            LagoonCLI.getProperty(name);
            fail("AuthenticationException expected");
        } catch (AuthenticationException e) {
            assertEquals("Incorrect password", e.getMessage());
        }

        name = "sourceDir";
        File sourceDir = new File(LagoonCLI.getProperty("sourceDir"));
        assertTrue(sourceDir.exists());
    }

    @Test
    public void testShowTime() {
        long ms = 5000;
        LagoonCLI.showTime(ms);
        String expectedOutput = "in 5 s";
        String actualOutput = out.getHWB().toString();
        assertEquals(expectedOutput, actualOutput);

        ms = 123;
        LagoonCLI.showTime(ms);
        expectedOutput = "in 123 ms";
        actualOutput = out.getHWB().toString();
        assertEquals(expectedOutput, actualOutput);

        ms = 100;
        LagoonCLI.showTime(ms);
        expectedOutput = "in 0.1 s";
        actualOutput = out.getHWB().toString();
        assertEquals(expectedOutput, actualOutput);
    }
}