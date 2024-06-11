package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMainWithPropertyFileAndNoArgs() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
    }

    // Method 1 test cases

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithWrongSyntax() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "wrong_syntax"};
        LagoonCLI.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithPropertyFileAndInvalidArgument() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file.xml", "invalid_argument"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithPropertyFileAndInvalidInterval() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithSitemapFileAndNoArgs() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithSitemapFileAndInvalidArgument() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file.xml", "invalid_argument"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithSitemapFileAndInvalidInterval() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "sitemap_file.xml", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingPropertyFile() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "missing_property_file.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFileAndBuild() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file.xml", "build"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFileAndForce() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file.xml", "force"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFileAndInterval() {
        String[] args = new String[]{"nu.staldal.lagoon.LagoonCLI", "property_file.xml", "1000"};
        LagoonCLI.main(args);
    }

    // Method 2 test cases

    @Test
    public void testGetPropertyWithValidName() {
        String propertyFile = "property_file.xml";
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        String result = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithInvalidName() {
        String propertyFile = "property_file.xml";
        LagoonCLI.properties = new Properties();
        String result = LagoonCLI.getProperty("invalid_name");
        fail();
    }

    @Test
    public void testGetPropertyWithEmptyPropertyFile() {
        String propertyFile = "empty_property_file.xml";
        LagoonCLI.properties = new Properties();
        String result = LagoonCLI.getProperty("targetURL");
        assertNull(result);
    }

    // Method 3 test cases

    @Test
    public void testShowTimeWithShortDuration() {
        long time = 1234L;
        LagoonCLI.showTime(time);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in 1.234 ms"));
    }

    @Test
    public void testShowTimeWithLongDuration() {
        long time = 123456789L;
        LagoonCLI.showTime(time);
        String output = System.out.getBuffer().toString();
        assertTrue(output.contains("in 1.234 s"));
    }
}