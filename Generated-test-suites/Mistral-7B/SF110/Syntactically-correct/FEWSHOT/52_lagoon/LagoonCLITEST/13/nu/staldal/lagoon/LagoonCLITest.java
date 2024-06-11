package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testGetProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI lagoonCLI = new LagoonCLI();
        String targetURL = lagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", targetURL);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyMissingProperty() throws LagoonException {
        Properties properties = new Properties();
        LagoonCLI lagoonCLI = new LagoonCLI();
        String targetURL = lagoonCLI.getProperty("nonExistentProperty");
        fail("Property 'nonExistentProperty' not found should have thrown a LagoonException");
    }

    @Test
    public void testInitLagoonWithPropertyFile() throws IOException, LagoonException, SAXException {
        File propertyFile = new File("src/test/resources/lagoon-property-file.xml");
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml"});
        // Add assertions to check the state of the LagoonCLI object after initialization
    }

    @Test(expected = LagoonException.class)
    public void testInitLagoonWithInvalidInterval() throws IOException, LagoonException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml", "-1"});
        fail("Invalid interval should have thrown a LagoonException");
    }

    @Test
    public void testInitLagoonWithValidInterval() throws IOException, LagoonException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml", "1000"});
        // Add assertions to check the state of the LagoonCLI object after initialization
    }

    @Test
    public void testInitLagoonWithSitemapFile() throws IOException, LagoonException, SAXException {
        File sitemapFile = new File("src/test/resources/lagoon-sitemap.xml");
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"sitemapFile.xml"});
        // Add assertions to check the state of the LagoonCLI object after initialization
    }

    @Test(expected = LagoonException.class)
    public void testInitLagoonWithMissingPassword() throws IOException, LagoonException, SAXException {
        File sitemapFile = new File("src/test/resources/lagoon-sitemap-missing-password.xml");
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"sitemapFile.xml"});
        fail("Missing password should have thrown a LagoonException");
    }

    @Test
    public void testBuildWebsite() throws IOException, LagoonException, SAXException, InterruptedException {
        File sitemapFile = new File("src/test/resources/lagoon-sitemap.xml");
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"sitemapFile.xml", "build"});
        // Add assertions to check the state of the LagoonCLI object and the output after building the website
    }

    @Test
    public void testForceBuildWebsite() throws IOException, LagoonException, SAXException {
        File sitemapFile = new File("src/test/resources/lagoon-sitemap.xml");
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"sitemapFile.xml", "force"});
        // Add assertions to check the state of the LagoonCLI object and the output after forcing the building of the website
    }

    @Test
    public void testWaitInterval() throws InterruptedException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml", "10000"});
        Thread.sleep(11000);
        // Add assertions to check if the program waits for the specified interval before building the website again
    }

    @Test
    public void testQuitCommand() throws IOException, InterruptedException {
        LagoonCLI lagoonCLI = new LagoonCLI();
        lagoonCLI.main(new String[]{"propertyFile.xml"});
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.readLine(); // Read the "Ready" message
        in.readLine(); // Read the "q" command
        lagoonCLI.main(new String[]{"propertyFile.xml"});
        // Add assertions to check if the program quits after receiving the "q" command
    }
}