package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import static nu.staldal.lagoon.LagoonCLI.*;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://test.com");
        assertEquals("http://test.com", getProperty("targetURL"));
        assertEquals("", getProperty("unknownProperty"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyThrowsException() {
        Properties properties = new Properties();
        try {
            getProperty("unknownProperty");
        } catch (LagoonException e) {
            assertEquals("Property unknownProperty not specified", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testMainWithPropertyFile() {
        String[] args = new String[]{"config.properties"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithSitemapFile() {
        String[] args = new String[]{"sitemap.xml"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }

    @Test(expected = LagoonException.class)
    public void testMainThrowsExceptionWithInvalidArgument1() {
        String[] args = new String[]{"invalidFile"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainThrowsExceptionWithInvalidArgument2() {
        String[] args = new String[]{"10a"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainThrowsExceptionWithInvalidArgument3() {
        String[] args = new String[]{"build10a"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testMainThrowsExceptionWithInvalidArgument4() {
        String[] args = new String[]{"1000z"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithValidArgument1() {
        String[] args = new String[]{"config.properties", "5000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithValidArgument2() {
        String[] args = new String[]{"sitemap.xml", "build"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithValidArgument3() {
        String[] args = new String[]{"sitemap.xml", "5000"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithValidArgument4() {
        String[] args = new String[]{"sitemap.xml", "force"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }

    @Test
    public void testMainWithValidArgument5() {
        String[] args = new String[]{"sitemap.xml", "100"};
        LagoonCLI.main(args);
        assertTrue(LagoonCLI.DEBUG == false);
        assertNotNull(LagoonCLI.properties);
    }
}