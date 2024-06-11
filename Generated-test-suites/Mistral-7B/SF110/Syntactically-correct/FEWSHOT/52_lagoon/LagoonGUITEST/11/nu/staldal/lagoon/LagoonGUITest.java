package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;

public class LagoonGUITest {

    @Test
    public void testLagoonGUIConstructor() {
        new LagoonGUI();
    }

    @Test
    public void testLoadPropertiesFile() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("test.properties");
        appFrame.loadPropertiesFile();
        assertNotNull(appFrame.getSitemapFile().getValue());
        assertNotNull(appFrame.getSourceDir().getValue());
        assertNotNull(appFrame.getTargetURL().getValue());
        assertNull(appFrame.getPassword());
        try {
            appFrame.loadProperties(propertyFile);
            String sitemapFileValue = appFrame.getSitemapFile().getValue();
            String sourceDirValue = appFrame.getSourceDir().getValue();
            String targetURLValue = appFrame.getTargetURL().getValue();
            String password = appFrame.getPassword();
            assertEquals(sitemapFileValue, getProperty(appFrame.getProperties(), "sitemapFile"));
            assertEquals(sourceDirValue, getProperty(appFrame.getProperties(), "sourceDir"));
            assertEquals(targetURLValue, getProperty(appFrame.getProperties(), "targetURL"));
            assertNull(password);
        } catch (IOException e) {
            fail("IOException when loading properties file");
        } catch (LagoonException e) {
            fail("LagoonException when loading properties file");
        }
    }

    @Test
    public void testSavePropertiesFile() {
        LagoonGUI appFrame = new LagoonGUI();
        File propertyFile = new File("test.properties");
        appFrame.savePropertyFile();
        assertNull(appFrame.getProperties().getProperty("sitemapFile"));
        assertNull(appFrame.getProperties().getProperty("sourceDir"));
        assertNull(appFrame.getProperties().getProperty("targetURL"));
        assertNull(appFrame.getPassword());

        try {
            appFrame.loadPropertiesFile();
            String sitemapFileValue = appFrame.getSitemapFile().getValue();
            String sourceDirValue = appFrame.getSourceDir().getValue();
            String targetURLValue = appFrame.getTargetURL().getValue();
            String password = appFrame.getPassword();
            assertEquals(sitemapFileValue, getProperty(appFrame.getProperties(), "sitemapFile"));
            assertEquals(sourceDirValue, getProperty(appFrame.getProperties(), "sourceDir"));
            assertEquals(targetURLValue, getProperty(appFrame.getProperties(), "targetURL"));
            assertNull(password);

            appFrame.savePropertyFile();
            Properties properties = appFrame.getProperties();
            String sitemapFileValueInFile = properties.getProperty("sitemapFile");
            String sourceDirValueInFile = properties.getProperty("sourceDir");
            String targetURLValueInFile = properties.getProperty("targetURL");
            String passwordInFile = properties.getProperty("password");
            assertEquals(sitemapFileValue, sitemapFileValueInFile);
            assertEquals(sourceDirValue, sourceDirValueInFile);
            assertEquals(targetURLValue, targetURLValueInFile);
            assertNull(passwordInFile);
        } catch (IOException e) {
            fail("IOException when loading or saving properties file");
        } catch (LagoonException e) {
            fail("LagoonException when loading properties file");
        }
    }

    @Test
    public void testBuildMethod() throws IOException, SAXException, LagoonException {
        LagoonGUI appFrame = new LagoonGUI();
        File sitemapFile = new File("sitemap.xml");
        File inputFile = new File("input.xml");
        File outputFile = new File("output.xml");
        appFrame.getSitemapFile().setValue(sitemapFile.getAbsolutePath());
        appFrame.getSourceDir().setValue(inputFile.getParent());
        appFrame.getTargetURL().setValue("http://localhost:8080");
        appFrame.init();
        appFrame.build(false);
        assertTrue(appFrame.getProcessor() != null);
        appFrame.getProcessor().destroy();
        appFrame.build(true);
        assertTrue(appFrame.getProcessor() != null);
    }

    @Test
    public void testExitMethod() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.exit();
        fail("System should have exited");
    }

    private Properties getProperties() {
        return new LagoonGUI().getProperties();
    }

    private static String getProperty(Properties prop, String name) throws LagoonException {
        return new LagoonGUI().getProperty(prop, name);
    }
}