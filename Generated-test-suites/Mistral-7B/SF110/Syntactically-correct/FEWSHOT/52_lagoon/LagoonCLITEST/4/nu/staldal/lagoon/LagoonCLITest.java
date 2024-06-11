package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import static nu.staldal.lagoon.LagoonCLI.*;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        assertEquals("http://example.com", getProperty("targetURL"));
        assertThrows(LagoonException.class, () -> getProperty("nonExistentProperty"));
    }

    @Test
    public void testMain_ValidArguments() {
        String[] args = new String[]{"config.xml"};
        LagoonCLI.main(args);
        // Add more test cases for different valid arguments
    }

    @Test
    public void testMain_InvalidArguments() {
        String[] args = new String[0];
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
        args = new String[]{"invalidFile", "build"};
        assertThrows(NumberFormatException.class, () -> LagoonCLI.main(args));
        args = new String[]{"property_file", "10a"};
        assertThrows(NumberFormatException.class, () -> LagoonCLI.main(args));
        args = new String[]{"property_file", "invalid"};
        assertThrows(LagoonException.class, () -> LagoonCLI.main(args));
    }

    @Test
    public void testMain_ParsingPropertiesFile() {
        String[] args = new String[]{"config.properties"};
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        properties.setProperty("sitemapFile", "sitemap.xml");
        properties.setProperty("sourceDir", "/path/to/source");
        properties.setProperty("password", "password");
        System.setProperty("user.dir", "/path/to/project");
        LagoonCLI.main(args);
        assertEquals(properties.getProperty("targetURL"), getProperty("targetURL"));
        assertEquals(properties.getProperty("sitemapFile"), getProperty("sitemapFile"));
        assertEquals(properties.getProperty("sourceDir"), getProperty("sourceDir"));
        assertEquals(properties.getProperty("password"), getProperty("password"));
    }

    @Test
    public void testMain_BuildingWebsite() {
        String[] args = new String[]{"sitemap.xml", "build"};
        LagoonCLI.main(args);
        // Add more test cases for different build scenarios
    }

    @Test
    public void testMain_ForceBuildingWebsite() {
        String[] args = new String[]{"sitemap.xml", "force"};
        LagoonCLI.main(args);
        // Add more test cases for different force build scenarios
    }

    @Test
    public void testMain_IntervalBuildingWebsite() {
        String[] args = new String[]{"sitemap.xml", "5000"};
        LagoonCLI.main(args);
        // Add more test cases for different interval build scenarios
    }

    @Test
    public void testMain_ReadingUserInput() {
        String[] args = new String[]{"property_file"};
        final ByteArrayOutputStream errOut = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errOut));
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        LagoonCLI.main(args);
        String input = "b\nq";
        final ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        LagoonCLI.main(args);
        assertEquals("Building website...\nin " + getInterval(5000) + "\nsReady\nBuilding website...\nin " + getInterval(5000) + "\n", out.toString());
        assertEquals("", errOut.toString());
    }

    // Add more test cases as needed for other methods or edge cases

    private long getInterval(int milliseconds) {
        return milliseconds;
    }
}