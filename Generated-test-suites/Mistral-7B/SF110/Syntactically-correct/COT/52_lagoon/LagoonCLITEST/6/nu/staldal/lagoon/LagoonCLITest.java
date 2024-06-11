package nu.staldal.lagoon.test;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;
import static nu.staldal.lagoon.LagoonCLI.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonCLITest {
    

    @Test
    public void testMainWithInvalidArgs() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        Assert.assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getConsoleText());
    }

    @Test
    public void testMainWithInvalidPropertyFileArg() {
        String[] args = new String[]{"invalidFile.txt"};
        LagoonCLI.main(args);
        Assert.assertEquals("Error while initializing Lagoon:\nFile not found: invalidFile.txt", System.err.getConsoleText());
    }

    @Test
    public void testMainWithInvalidBuildArg() {
        String[] args = new String[]{"properties.xml", "invalidBuild"};
        LagoonCLI.main(args);
        Assert.assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getConsoleText());
    }

    @Test
    public void testMainWithInvalidForceArg() {
        String[] args = new String[]{"properties.xml", "force"};
        LagoonCLI.main(args);
        Assert.assertEquals("Error while initializing Lagoon:\nPassword is required but not specified", System.err.getConsoleText());
    }

    @Test
    public void testMainWithInvalidIntervalArg() {
        String[] args = new String[]{"properties.xml", "-1"};
        LagoonCLI.main(args);
        Assert.assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]", System.out.getConsoleText());
    }

    @Test
    public void testMainWithValidArgs() throws Exception {
        String[] args = new String[]{"properties.xml", "1000"};
        LagoonCLI.main(args);
        Assert.assertEquals("Lagoon initialized successfully", System.out.getConsoleText());
    }

    @Test
    public void testGetPropertyWithMissingProperty() throws LagoonException {
        Properties props = new Properties();
        String propertyName = "missingProperty";
        LagoonCLI.getProperty(propertyName, props);
        Assert.fail(); // Exception should be thrown
    }

    @Test
    public void testGetPropertyWithValidProperty() throws Exception {
        Properties props = new Properties();
        props.setProperty("targetURL", "http://example.com");
        String propertyValue = LagoonCLI.getProperty("targetURL", props);
        Assert.assertEquals("http://example.com", propertyValue);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingPassword() throws Exception {
        Properties props = new Properties();
        String propertyName = "password";
        LagoonCLI.getProperty(propertyName, props);
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingPropertiesFile() {
        String[] args = new String[]{"missingPropertiesFile.txt"};
        LagoonCLI.main(args);
        Assert.assertEquals("Error while initializing Lagoon:\nFile not found: missingPropertiesFile.txt", System.err.getConsoleText());
    }

    @Test(expected = SAXException.class)
    public void testInitializeWithInvalidSitemap() throws Exception {
        String sitemapFile = "invalidSitemap.xml";
        LagoonCLI.main(new String[]{"properties.xml", sitemapFile});
    }

    @Test
    public void testInitializeWithValidSitemap() throws Exception {
        String sitemapFile = "validSitemap.xml";
        LagoonCLI.main(new String[]{"properties.xml", sitemapFile});
        Assert.assertEquals("Lagoon initialized successfully", System.out.getConsoleText());
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitializeWithMissingPassword() throws Exception {
        String sitemapFile = "validSitemap.xml";
        LagoonCLI.main(new String[]{"missingPropertiesFile.xml", sitemapFile});
    }

    @Test(expected = AuthenticationException.class)
    public void testInitializeWithInvalidPassword() throws Exception {
        Properties props = new Properties();
        props.setProperty("password", "wrongPassword");
        String sitemapFile = "validSitemap.xml";
        LagoonCLI.main(new String[]{"properties.xml", sitemapFile});
    }

    @Test(expected = FileNotFoundException.class)
    public void testInitializeWithMissingTargetURL() throws Exception {
        Properties props = new Properties();
        props.setProperty("sitemapFile", "validSitemap.xml");
        LagoonCLI.main(new String[]{"missingPropertiesFile.xml", "validSitemap.xml"});
    }

    @Test(expected = IOException.class)
    public void testInitializeWithIOReason() throws Exception {
        String sitemapFile = "validSitemap.xml";
        LagoonCLI.main(new String[]{"properties.xml", sitemapFile + "notExistingFile"});
    }

    @Test(expected = LagoonException.class)
    public void testInitializeWithLagoonException() throws Exception {
        String sitemapFile = "validSitemap.xml";
        LagoonCLI.main(new String[]{"properties.xml", "validSitemap.xml", "notExistingFile"});
    }

    
}