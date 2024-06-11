package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.xml.sax.SAXException;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.core.AuthenticationException;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.Property;
import nu.staldal.lagoon.core.PrintWriter;
import nu.staldal.lagoon.sax.TreeBuilder;
import nu.staldal.xtree.Element;

public class LagoonCLITest {

    @org.junit.Test
    public void testValidArguments() throws Exception {
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when valid arguments are provided
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testInvalidSyntax() throws Exception {
        String[] args = {};
        LagoonCLI.main(args);
        // No need to add assertions here as the test expects an exception to be thrown
    }

    @org.junit.Test(expected = NumberFormatException.class)
    public void testInvalidInterval() throws Exception {
        String[] args = {"propertyFile.xml", "invalidInterval"};
        LagoonCLI.main(args);
        // No need to add assertions here as the test expects an exception to be thrown
    }

    @org.junit.Test(expected = LagoonException.class)
    public void testInvalidPropertyFile() throws Exception {
        String[] args = {"invalidPropertyFile.txt"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when an invalid property file is provided
    }

    @org.junit.Test
    public void testValidPropertyFile() throws Exception {
        String[] args = {"propertyFile.xml"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when a valid property file is provided
    }

    @org.junit.Test(expected = AuthenticationException.class)
    public void testIncorrectPassword() throws Exception {
        String[] args = {"propertyFile.xml"};
        Properties properties = new Properties();
        properties.setProperty("password", "incorrectPassword");
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        // No need to add assertions here as the test expects an exception to be thrown
    }

    @org.junit.Test
    public void testBuildSuccess() throws Exception {
        String[] args = {"propertyFile.xml", "build"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when building the website is successful
    }

    @org.junit.Test
    public void testForceBuildSuccess() throws Exception {
        String[] args = {"propertyFile.xml", "force"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when forcing a build is successful
    }

    @org.junit.Test
    public void testIntervalBuild() throws Exception {
        String[] args = {"propertyFile.xml", "1000"};
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when building the website with an interval
    }

    @org.junit.Test(expected = InterruptedException.class)
    public void testInterruptedBuild() throws Exception {
        String[] args = {"propertyFile.xml", "build"};
        Thread thread = new Thread(() -> {
            try {
                LagoonCLI.main(args);
            } catch (Exception e) {
                // Do nothing
            }
        });
        thread.start();
        thread.interrupt();
        // No need to add assertions here as the test expects an InterruptedException to be thrown
    }

    @org.junit.Test(expected = FileNotFoundException.class)
    public void testSourceDirNotExists() throws Exception {
        String[] args = {"propertyFile.xml", "sourceDir/does_not_exist"};
        LagoonCLI.main(args);
        // No need to add assertions here as the test expects an exception to be thrown
    }

    @org.junit.Test(expected = IOException.class)
    public void testSitemapFileNotReadable() throws Exception {
        String[] args = {"propertyFile.xml", "sitemapFile.txt"};
        File sitemapFile = new File("sitemapFile.txt");
        sitemapFile.deleteOnExit();
        sitemapFile.createNewFile();
        try {
            FileInputStream fis = new FileInputStream(sitemapFile);
            fis.close();
        } catch (IOException e) {
            // Do nothing
        }
        LagoonCLI.main(args);
        // No need to add assertions here as the test expects an exception to be thrown
    }

    @org.junit.Test
    public void testValidSitemapFile() throws Exception {
        String[] args = {"propertyFile.xml"};
        File sitemapFile = new File("sitemapFile.xml");
        sitemapFile.createNewFile();
        FileInputStream fis = new FileInputStream(sitemapFile);
        InputSource source = new InputSource(fis);
        Element root = TreeBuilder.parseXML(source, false);
        LagoonCLI.main(args);
        // Add assertions here to check the expected behavior when a valid sitemap file is provided
    }
}