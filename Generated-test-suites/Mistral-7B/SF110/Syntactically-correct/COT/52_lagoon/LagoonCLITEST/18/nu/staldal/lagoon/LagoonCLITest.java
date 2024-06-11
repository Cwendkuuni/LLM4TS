package nu.staldal.lagoon;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class LagoonCLITest {

    @BeforeClass
    public static void setup() {
        LagoonCLI.DEBUG = false;
    }

    

    // Test for main method with valid arguments
    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[] {"property_file.xml", "build"};
        LagoonCLI.main(args);
        // Add assertions here based on the expected behavior of your system
    }

    // Test for main method with invalid arguments
    @Test(expected = LagoonException.class)
    public void testMainWithInvalidArguments() {
        String[] args = new String[] {"property_file.xml", "abc"};
        LagoonCLI.main(args);
    }

    // Test for main method with missing arguments
    @Test
    public void testMainWithMissingArguments() {
        String[] args = new String[] {"property_file.xml"};
        LagoonCLI.main(args);
        // Add assertions here based on the expected behavior of your system
    }

    // Test for getProperty method with valid property name
    @Test
    public void testGetPropertyWithValidName() {
        String propertyFile = "property_file.xml";
        String expectedValue = "expectedValue";
        LagoonCLI.properties.setProperty("propertyName", expectedValue);
        String result = LagoonCLI.getProperty("propertyName");
        assertEquals(expectedValue, result);
    }

    // Test for getProperty method with invalid property name
    @Test(expected = LagoonException.class)
    public void testGetPropertyWithInvalidName() {
        String propertyFile = "property_file.xml";
        LagoonCLI.properties.setProperty("propertyName", "expectedValue");
        String result = LagoonCLI.getProperty("invalidPropertyName");
    }

    // Test for showTime method with small time
    @Test
    public void testShowTimeWithSmallTime() {
        long time = 5;
        LagoonCLI.showTime(time);
        // Add assertions here based on the expected behavior of your system
    }

    // Test for showTime method with large time
    @Test
    public void testShowTimeWithLargeTime() {
        long time = 5000;
        LagoonCLI.showTime(time);
        // Add assertions here based on the expected behavior of your system
    }

    
}