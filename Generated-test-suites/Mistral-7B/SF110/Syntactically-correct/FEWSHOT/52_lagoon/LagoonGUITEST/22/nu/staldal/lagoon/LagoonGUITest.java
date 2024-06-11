package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import nu.staldal.lagoon.Core.LagoonException;
import nu.staldal.lagoon.Core.LagoonProcessor;
import nu.staldal.lagoon.Core.MessageDialog;
import nu.staldal.lagoon.Core.PasswordDialog;
import nu.staldal.lagoon.Core.YesNoQueryDialog;
import nu.staldal.lagoon.GUI.Frame;
import nu.staldal.lagoon.GUI.InputComponent;
import nu.staldal.lagoon.GUI.LagoonGUI;
import nu.staldal.lagoon.GUI.Panel;
import nu.staldal.lagoon.GUI.TextAreaWriter;
import nu.staldal.lagoon.XTree.TreeBuilder;
import nu.staldal.xtree.SAXException;

public class LagoonGUITest {

    private LagoonGUI appFrame;

    @Before
    public void setUp() {
        appFrame = new LagoonGUI();
    }

    @Test
    public void testInit() {
        // Test initial state
        assertNull(appFrame.processor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithSitemapFileChange() {
        final File sitemapFile = new File("testSitemapFile.xml");
        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.init();
        assertNotNull(appFrame.processor);
    }

    @Test(expected = LagoonException.class)
    public void testInitWithInvalidXMLFile() {
        final File sitemapFile = new File("testInvalidXMLFile.xml");
        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.init();
    }

    @Test(expected = IOException.class)
    public void testInitWithFileNotFoundException() {
        final File sitemapFile = new File("testFileNotFound.xml");
        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.init();
    }

    @Test
    public void testBuild() throws IOException, LagoonException, SAXException {
        final File sitemapFile = new File("testSitemapFile.xml");
        final File sourceDir = new File("testSourceDir");
        final File targetURL = new File("testTargetURL");

        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
        appFrame.targetURL.setValue(targetURL.getAbsolutePath());

        appFrame.init();

        // Simulate button click
        appFrame.buildButton.actionPerformed(new ActionEvent(appFrame.buildButton, 0, 0));

        // Test build result
        assertTrue(appFrame.processor != null);
    }

    @Test
    public void testBuildWithForce() throws IOException, LagoonException, SAXException {
        final File sitemapFile = new File("testSitemapFile.xml");
        final File sourceDir = new File("testSourceDir");
        final File targetURL = new File("testTargetURL");

        appFrame.sitemapFile.setValue(sitemapFile.getAbsolutePath());
        appFrame.sourceDir.setValue(sourceDir.getAbsolutePath());
        appFrame.targetURL.setValue(targetURL.getAbsolutePath());

        appFrame.init();

        // Simulate button click
        appFrame.forceButton.actionPerformed(new ActionEvent(appFrame.forceButton, 0, 0));

        // Test build result
        assertTrue(appFrame.processor != null);
    }

    @Test
    public void testSavePropertyFile() {
        final File propertyFile = new File("testPropertyFile.properties");

        appFrame.loadPropertyFile(); // Initialize properties

        appFrame.saveButton.actionPerformed(new ActionEvent(appFrame.saveButton, 0, 0));

        // Test property file content
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            String expectedValue = appFrame.sitemapFile.getValue();
            assertEquals(expectedValue, prop.getProperty("sitemapFile"));
            expectedValue = appFrame.sourceDir.getValue();
            assertEquals(expectedValue, prop.getProperty("sourceDir"));
            expectedValue = appFrame.targetURL.getValue();
            assertEquals(expectedValue, prop.getProperty("targetURL"));
            assertEquals("Lagoon properties", prop.getProperty("password"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Test
    public void testLoadPropertyFile() {
        final File propertyFile = new File("testPropertyFile.properties");

        try {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(propertyFile);
            Properties prop = new Properties();
            prop.setProperty("sitemapFile", "testSitemapFile.xml");
            prop.setProperty("sourceDir", "testSourceDir");
            prop.setProperty("targetURL", "testTargetURL");
            prop.setProperty("password", "testPassword");
            prop.store(fos, "Lagoon properties");
            fos.close();

            appFrame.loadPropertyFile();

            String expectedValue = "testSitemapFile.xml";
            assertEquals(expectedValue, appFrame.sitemapFile.getValue());
            expectedValue = "testSourceDir";
            assertEquals(expectedValue, appFrame.sourceDir.getValue());
            expectedValue = "testTargetURL";
            assertEquals(expectedValue, appFrame.targetURL.getValue());
            assertEquals("testPassword", appFrame.password);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (propertyFile.exists()) {
                propertyFile.delete();
            }
        }
    }

    @Test
    public void testLoadPropertiesWithInvalidFile() {
        final File propertyFile = new File("testInvalidPropertyFile.properties");

        appFrame.loadPropertyFile();

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            fail("An exception should have been thrown");
        } catch (IOException e) {
            // Expected
        } catch (LagoonException e) {
            fail("An IOException should have been thrown instead");
        }
    }
}