package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://localhost:8080");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String result = lagoonCLI.getProperty("targetURL");
        assertEquals("http://localhost:8080", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWhenPropertyNotSpecified() {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.getProperty("nonExistentProperty");
    }

    @Test
    public void testStripLeadingHyphensWhenNoHyphens() {
        String input = "foo";
        String expected = "foo";
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWhenLeadingHyphens() {
        String input = "--foo";
        String expected = "foo";
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWhenMultipleLeadingHyphens() {
        String input = "---foo";
        String expected = "foo";
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWhenNoLeadingHyphensAndTrailingHyphens() {
        String input = "foo";
        String expected = "foo";
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWhenLeadingAndTrailingHyphens() {
        String input = "--foo--";
        String expected = "foo";
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWhenNullInput() {
        String input = null;
        String expected = null;
        String result = LagoonCLI.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testInitializeLagoonWithValidPropertyFile() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/validPropertyFile.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithInvalidPropertyFileFormat() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/invalidPropertyFile.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithMissingPasswordProperty() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/missingPasswordProperty.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithInvalidPassword() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/invalidPassword.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeLagoonWithMissingSitemapFile() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/validPropertyFileWithMissingSitemap.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testInitializeLagoonWithIOError() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/ioError.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = LagoonException.class)
    public void testInitializeLagoonWithLagoonException() throws IOException, LagoonException, SAXException {
        String propertyFile = "src/test/resources/lagoonException.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
    }

    @Test
    public void testBuildWebsite() throws IOException, LagoonException, SAXException, InterruptedException {
        String propertyFile = "src/test/resources/validPropertyFile.xml";
        String[] args = {"propertyFile.xml", "build"};
        LagoonCLI.main(args);
    }

    @Test
    public void testForceBuildWebsite() throws IOException, LagoonException, SAXException, InterruptedException {
        String propertyFile = "src/test/resources/validPropertyFile.xml";
        String[] args = {"propertyFile.xml", "force"};
        LagoonCLI.main(args);
    }

    @Test
    public void testPeriodicBuildingWebsite() throws IOException, LagoonException, SAXException, InterruptedException {
        String propertyFile = "src/test/resources/validPropertyFile.xml";
        String[] args = {"propertyFile.xml"};
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(args);
        Thread.sleep(1100L); // wait for the first build to finish
        lagoonCLI.main(args);
        Thread.sleep(1100L); // wait for the second build to finish
        lagoonCLI.main(args);
    }
}