package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import org.junit.Test;

public class LagoonCLITest {
    

    @Test
    public void testMainWithNoArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        assertEquals(LagoonCLI.syntaxMsg, System.out.getErrorStream().toString());
    }

    @Test
    public void testMainWithInvalidArgument() {
        String[] args = new String[]{"invalid_argument"};
        LagoonCLI.main(args);
        assertEquals(LagoonCLI.syntaxMsg, System.out.getErrorStream().toString());
    }

    @Test
    public void testMainWithInvalidPropertyFile() {
        String[] args = new String[]{"invalid_property_file"};
        LagoonCLI.main(args);
        assertEquals(LagoonCLI.syntaxMsg, System.out.getErrorStream().toString());
    }

    @Test
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"property_file", "-1"};
        LagoonCLI.main(args);
        assertEquals(LagoonCLI.syntaxMsg, System.out.getErrorStream().toString());
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"property_file", "10"};
        LagoonCLI.main(args);
        // Additional assertions can be added here based on the expected behavior of the application
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingProperty() {
        String[] args = new String[]{"property_file"};
        LagoonCLI.main(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidIntervalFormat() {
        String[] args = new String[]{"property_file", "invalid_interval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testgetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        String result = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testgetPropertyWithMissingProperty() throws LagoonException {
        Properties properties = new Properties();
        String result = LagoonCLI.getProperty("missing_property");
    }

    @Test
    public void testShowTime() {
        long time = 5000L;
        LagoonCLI.showTime(time);
        if (time < 10000L) {
            assertTrue(System.out.getErrorStream().toString().contains("in " + time + " ms"));
        } else {
            assertTrue(System.out.getErrorStream().toString().contains("in " + time / 1000L + " s"));
        }
    }

    @Test
    public void testGetThreads() {
        LagoonCLI.main(new String[]{"property_file"});
        // Additional assertions can be added here based on the expected behavior of the application
    }

    
}