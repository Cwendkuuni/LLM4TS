package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import nu.staldal.lagoon.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"test.property", "1000"};
        LagoonCLI.main(args);
        // Add assertions for expected behavior
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArgumentsNumber() {
        String[] args = new String[0];
        LagoonCLI.main(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMainWithInvalidArgumentsSyntax() {
        String[] args = new String[]{"invalidFile", "invalidArgument"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithPropertyFile() {
        String[] args = new String[]{"test.property"};
        LagoonCLI.main(args);
        // Add assertions for expected behavior related to handling property file
    }

    @Test(expected = NumberFormatException.class)
    public void testMainWithInvalidInterval() {
        String[] args = new String[]{"test.property", "invalidInterval"};
        LagoonCLI.main(args);
    }

    @Test
    public void testMainWithBuildFlag() {
        String[] args = new String[]{"test.property", "build"};
        LagoonCLI.main(args);
        // Add assertions for expected behavior related to building
    }

    @Test
    public void testMainWithForceFlag() {
        String[] args = new String[]{"test.property", "force"};
        LagoonCLI.main(args);
        // Add assertions for expected behavior related to force building
    }

    @Test
    public void testMainWithIntervalFlag() {
        String[] args = new String[]{"test.property", "1000"};
        LagoonCLI.main(args);
        // Add assertions for expected behavior related to interval

        args = new String[]{"test.property", "0"};
        try {
            LagoonCLI.main(args);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test(expected = LagoonException.class)
    public void testMainWithMissingProperty() {
        String[] args = new String[]{"test.property"};
        LagoonCLI.main(args);
    }

    @Test(expected = FileNotFoundException.class)
    public void testMainWithMissingPropertyFile() {
        String[] args = new String[]{"missingFile.property"};
        LagoonCLI.main(args);
    }

    @Test(expected = IOException.class)
    public void testMainWithInvalidSitemapFile() {
        String[] args = new String[]{"test.property", "test.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = SAXException.class)
    public void testMainWithInvalidXMLSitemapFile() throws SAXException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "testInvalidXML.xml");
        LagoonCLI.properties = properties;
        String[] args = new String[]{"testInvalidXML.xml"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testMainWithMissingPassword() {
        String[] args = new String[]{"test.property"};
        LagoonCLI.main(args);
    }

    @Test(expected = AuthenticationException.class)
    public void testMainWithIncorrectPassword() {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://test.com");
        properties.setProperty("sitemapFile", "test.xml");
        properties.setProperty("password", "incorrectPassword");
        LagoonCLI.properties = properties;
        String[] args = new String[]{"test.xml"};
        LagoonCLI.main(args);
    }

}