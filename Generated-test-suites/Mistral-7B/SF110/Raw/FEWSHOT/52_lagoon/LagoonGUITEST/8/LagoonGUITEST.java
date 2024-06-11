package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class LagoonGUITest {

    @Test
    public void testInitComponents() {
        final LagoonGUI appFrame = new LagoonGUI();
        assertNotNull(appFrame.buttonPanel);
        assertNotNull(appFrame.inputPanel);
        assertNotNull(appFrame.centerPanel);
        assertNotNull(appFrame.loadButton);
        assertNotNull(appFrame.saveButton);
        assertNotNull(appFrame.buildButton);
        assertNotNull(appFrame.forceButton);
        assertNotNull(appFrame.exitButton);
        assertNotNull(appFrame.sitemapFile);
        assertNotNull(appFrame.sourceDir);
        assertNotNull(appFrame.targetURL);
        assertNotNull(appFrame.statusLabel);
        assertNotNull(appFrame.progressArea);
    }

    @Test
    public void testLoadProperties() throws IOException {
        final LagoonGUI appFrame = new LagoonGUI();
        final File testFile = new File("testFile.properties");
        appFrame.loadProperties(testFile);
        assertNotNull(appFrame.sitemapFile.getValue());
        assertNotNull(appFrame.sourceDir.getValue());
        assertNotNull(appFrame.targetURL.getValue());
        assertEquals("expectedValue", appFrame.password);
        testFile.deleteOnExit();
    }

    @Test(expected = LagoonException.class)
    public void testLoadPropertiesWithMissingProperty() throws IOException {
        final LagoonGUI appFrame = new LagoonGUI();
        final File testFile = new File("testFile.properties");
        appFrame.loadProperties(testFile);
        assertNull(appFrame.sitemapFile.getValue());
        assertNull(appFrame.sourceDir.getValue());
        assertNull(appFrame.targetURL.getValue());
        assertNull(appFrame.password);
    }

    @Test
    public void testSaveProperties() throws IOException {
        final LagoonGUI appFrame = new LagoonGUI();
        final File testFile = new File("testFile.properties");
        appFrame.sitemapFile.setValue("testSitemapFile");
        appFrame.sourceDir.setValue("testSourceDir");
        appFrame.targetURL.setValue("testTargetURL");
        appFrame.password = "testPassword";
        appFrame.savePropertyFile();
        final Properties loadedProperties = new Properties();
        final FileInputStream fis = new FileInputStream(testFile);
        loadedProperties.load(fis);
        fis.close();
        assertEquals("testSitemapFile", loadedProperties.getProperty("sitemapFile"));
        assertEquals("testSourceDir", loadedProperties.getProperty("sourceDir"));
        assertEquals("testTargetURL", loadedProperties.getProperty("targetURL"));
        assertEquals("testPassword", loadedProperties.getProperty("password"));
        testFile.deleteOnExit();
    }

    @Test
    public void testBuild() {
        final LagoonGUI appFrame = spy(new LagoonGUI());
        doCallRealMethod().when(appFrame).build(false);
        appFrame.loadButton.addActionListener(arg0 -> appFrame.build(false));
        appFrame.loadButton.actionPerformed(new ActionEvent(appFrame.loadButton, 1, 1));
        verify(appFrame, times(1)).build(false);

        doThrow(new IOException()).when(appFrame).processor.build(false);
        appFrame.build(false);
        assertTrue(appFrame.statusLabel.getText().contains("Error(s) occured while building"));

        doThrow(new FileNotFoundException("testErrorMessage")).when(appFrame).processor.build(false);
        appFrame.build(false);
        assertTrue(appFrame.statusLabel.getText().contains("Fatal Building error: testErrorMessage"));
    }

    @Test
    public void testExit() {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.exit();
        assertFalse(appFrame.isDisplayable());
    }

    @Test(expected = AuthenticationMissingException.class)
    public void testInitWithMissingPassword() throws IOException, SAXException {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.init();
        fail("AuthenticationMissingException should be thrown");
    }

    @Test(expected = AuthenticationException.class)
    public void testInitWithInvalidPassword() throws IOException, SAXException {
        final LagoonGUI appFrame = spy(new LagoonGUI());
        doThrow(new AuthenticationException("testErrorMessage")).when(appFrame).processor.build(false);
        appFrame.init();
        assertTrue(appFrame.statusLabel.getText().contains("Invalid password, try again"));
    }

    @Test
    public void testLoadPropertiesWithPassword() throws IOException {
        final LagoonGUI appFrame = new LagoonGUI();
        final File testFile = new File("testFile.properties");
        appFrame.loadProperties(testFile);
        assertTrue(appFrame.password != null);
    }

}