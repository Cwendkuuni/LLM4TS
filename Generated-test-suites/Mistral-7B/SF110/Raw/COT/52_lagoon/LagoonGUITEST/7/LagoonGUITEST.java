package nu.staldal.lagoon.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.LagoonGUI;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.xtree.TreeBuilder;
import nu.staldal.xtree.TreeWriter;

public class LagoonGUITest {

    private static final String START_TEST = "/startTest.xml";
    private static final String END_TEST = "/endTest.xml";

    private LagoonGUI lagoonGUI;
    private InputStream inputStreamMock;
    private FileOutputStream outputStreamMock;
    private Properties propertiesMock;

    @Before
    public void setUp() {
        inputStreamMock = Mockito.mock(InputStream.class);
        outputStreamMock = Mockito.mock(FileOutputStream.class);
        propertiesMock = Mockito.mock(Properties.class);

        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testConstructor() {
        // Test that the constructor initializes the necessary components
        assertNotNull(lagoonGUI.getButtonPanel());
        assertNotNull(lagoonGUI.getInputPanel());
        assertNotNull(lagoonGUI.getCenterPanel());
        assertNotNull(lagoonGUI.getLoadButton());
        assertNotNull(lagoonGUI.getSaveButton());
        assertNotNull(lagoonGUI.getBuildButton());
        assertNotNull(lagoonGUI.getForceButton());
        assertNotNull(lagoonGUI.getExitButton());
        assertNotNull(lagoonGUI.getSitemapFile());
        assertNotNull(lagoonGUI.getSourceDir());
        assertNotNull(lagoonGUI.getTargetURL());
        assertNotNull(lagoonGUI.getStatusLabel());
        assertNotNull(lagoonGUI.getProgressArea());
        assertNull(lagoonGUI.getProcessor());
        assertNull(lagoonGUI.getPassword());
    }

    @Test
    public void testMain() throws Exception {
        // Test the main method entry point
        lagoonGUI.main(new String[0]);
        verify(lagoonGUI, times(1)).show();
    }

    @Test
    public void testLoadProperties() {
        // Test loading properties from a file
        when(propertiesMock.getProperty("sitemapFile")).thenReturn("test.xml");
        when(propertiesMock.getProperty("sourceDir")).thenReturn("src");
        when(propertiesMock.getProperty("targetURL")).thenReturn("http://example.com");
        when(propertiesMock.getProperty("password")).thenReturn("secret123");

        lagoonGUI.loadProperties(new File("testProperties.properties"));

        verify(lagoonGUI, times(1)).sitemapFile.setValue("test.xml");
        verify(lagoonGUI, times(1)).sourceDir.setValue("src");
        verify(lagoonGUI, times(1)).targetURL.setValue("http://example.com");
        verify(lagoonGUI, times(1)).password = "secret123";
    }

    @Test
    public void testSaveProperties() throws Exception {
        // Test saving properties to a file
        final Properties testProperties = new Properties();
        testProperties.setProperty("sitemapFile", "test.xml");
        testProperties.setProperty("sourceDir", "src");
        testProperties.setProperty("targetURL", "http://example.com");
        testProperties.setProperty("password", "secret123");

       when(propertiesMock.getProperty("sitemapFile")).thenReturn("test.xml");
        when(propertiesMock.getProperty("sourceDir")).thenReturn("src");
        when(propertiesMock.getProperty("targetURL")).thenReturn("http://example.com");
        when(propertiesMock.getProperty("password")).thenReturn("secret123");

       when(propertiesMock.store(outputStreamMock, "Lagoon properties")).thenReturn(true);

        lagoonGUI.savePropertyFile();

        verify(propertiesMock, times(1)).store(outputStreamMock, "Lagoon properties");
        verify(lagoonGUI, times(1)).statusLabel.setText("Properties saved");
    }

    @Test
    public void testInit() throws Exception {
        // Test initializing the LagoonProcessor
        final Element sitemapTree = Mockito.mock(Element.class);
        when(TreeBuilder.parseXML(inputStreamMock, false)).thenReturn(sitemapTree);
        when(TreeWriter.writeXMLStream(outputStreamMock, sitemapTree)).thenReturn(inputStreamMock);
        when(lagoonGUI.getProcessor()).thenReturn(null);

        lagoonGUI.init();

        verify(lagoonGUI, times(1)).processor = new LagoonProcessor(lagoonGUI.targetURL.getValue(), sitemapTree, new File(lagoonGUI.sourceDir.getValue()), lagoonGUI.password,
                Mockito.mock(TreeWriter.class), Mockito.mock(TreeWriter.class));
    }

    @Test
    public void testBuild() throws Exception {
        // Test building without errors
        final boolean success = true;
        when(lagoonGUI.processor.build(Mockito.eq(false))).thenReturn(success);

        lagoonGUI.build(false);

        verify(lagoonGUI, times(1)).progressArea.setText("--- Start build ---\n");
        verify(lagoonGUI, times(1)).statusLabel.setText("Building...");
        verify(lagoonGUI, times(1)).progressArea.append("--- Build finished successfully " + Mockito.anyString() + " ---");
        verify(lagoonGUI, times(1)).statusLabel.setText("Ready");
    }

    @Test
    public void testBuildWithErrors() throws Exception {
        // Test building with errors
        final boolean success = false;
        when(lagoonGUI.processor.build(Mockito.eq(false))).thenThrow(new IOException());

        lagoonGUI.build(false);

        verify(lagoonGUI, times(1)).progressArea.setText("--- Start build ---\n");
        verify(lagoonGUI, times(1)).statusLabel.setText("Building...");
        verify(lagoonGUI, times(1)).progressArea.append("--- Build finished with error(s) " + Mockito.anyString() + " ---");
        verify(lagoonGUI, times(1)).showMessageDialog(Mockito.any(Component.class), Mockito.eq("Building error"), Mockito.anyString());
    }

    @Test
    public void testBuildWithForce() throws Exception {
        // Test building with force
        final boolean success = true;
        when(lagoonGUI.processor.build(Mockito.eq(true))).thenReturn(success);

        lagoonGUI.build(true);

        verify(lagoonGUI, times(1)).progressArea.setText("--- Start build ---\n");
        verify(lagoonGUI, times(1)).statusLabel.setText("Building...");
        verify(lagoonGUI, times(1)).progressArea.append("--- Build finished successfully " + Mockito.anyString() + " ---");
        verify(lagoonGUI, times(1)).statusLabel.setText("Ready");
    }

    @Test(expected = FileNotFoundException.class)
    public void testBuildWithMissingFile() throws Exception {
        // Test building with a missing file
        final File missingFile = new File("missingFile.xml");
        when(lagoonGUI.getSitemapFile().getValue()).thenReturn(missingFile.getAbsolutePath());

        lagoonGUI.build(false);

        verify(lagoonGUI, times(1)).progressArea.setText("--- Start build ---\n");
        verify(lagoonGUI, times(1)).statusLabel.setText("Building...");
        verify(lagoonGUI, times(1)).statusLabel.setText("Not initialized");
        verify(lagoonGUI, times(1)).showMessageDialog(Mockito.any(Component.class), Mockito.eq("Initializing error"), Mockito.anyString());
    }

    @Test(expected = IOException.class)
    public void testInitWithIOException() throws Exception {
        // Test initializing with an IOException
        when(TreeBuilder.parseXML(inputStreamMock, false)).thenThrow(new IOException());

        lagoonGUI.init();

        verify(lagoonGUI, times(1)).statusLabel.setText("Initializing...");
        verify(lagoonGUI, times(1)).init();
        verify(lagoonGUI, times(1)).statusLabel.setText("Not initialized");
        verify(lagoonGUI, times(1)).showMessageDialog(Mockito.any(Component.class), Mockito.eq("Initializing error"), Mockito.anyString());
    }

    @Test(expected = LagoonException.class)
    public void testInitWithLagoonException() throws Exception {
        // Test initializing with a LagoonException
        when(TreeBuilder.parseXML(inputStreamMock, false)).thenReturn(Mockito.mock(Element.class));
        when(TreeWriter.writeXMLStream(outputStreamMock, Mockito.any(Element.class))).thenThrow(new LagoonException("Test error"));

        lagoonGUI.init();

        verify(lagoonGUI, times(1)).statusLabel.setText("Initializing...");
        verify(lagoonGUI, times(1)).init();
        verify(lagoonGUI, times(1)).statusLabel.setText("Not initialized");
        verify(lagoonGUI, times(1)).showMessageDialog(Mockito.any(Component.class), Mockito.eq("Initializing error"), Mockito.anyString());
    }

    @Test
    public void testExit() throws Exception {
        // Test exiting the application
        lagoonGUI.exit();
        verify(lagoonGUI, times(1)).destroy();
        System.exit(0);
    }

    @Test
    public void testLoadPropertiesWithNullFile() throws Exception {
        // Test loading properties with a null file
        lagoonGUI.loadProperties(null);
        verify(lagoonGUI, times(0)).loadProperties(Mockito.any(File.class));
    }

    @Test
    public void testLoadPropertyFile() {
        // Test loading a property file
        final File propertyFile = new File("testProperties.properties");

        lagoonGUI.loadPropertyFile();

        verify(lagoonGUI, times(1)).loadProperties(propertyFile);
    }

    @Test
    public void testSavePropertyFileWithNullFile() {
        // Test saving properties with a null file
        lagoonGUI.savePropertyFile();

        verify(lagoonGUI, times(0)).savePropertyFile();
    }

    @Test
    public void testSavePropertyFile() throws Exception {
        // Test saving properties to a file
        final File propertyFile = new File("testProperties.properties");

        lagoonGUI.savePropertyFile();

        verify(lagoonGUI, times(1)).savePropertyFile();
        verify(lagoonGUI, times(1)).statusLabel.setText("Properties saved");
    }

    @Test
    public void testLoadPropertiesWithEmptyFile() throws Exception {
        // Test loading properties from an empty file
        final File emptyFile = new File("emptyProperties.properties");

        when(propertiesMock.load(Mockito.eq(inputStreamMock))).thenReturn(new Properties());

        lagoonGUI.loadProperties(emptyFile);

        verify(lagoonGUI, times(1)).sitemapFile.setValue("");
        verify(lagoonGUI, times(1)).sourceDir.setValue("");
        verify(lagoonGUI, times(1)).targetURL.setValue("");
        verify(lagoonGUI, times(1)).password = "";
    }

    @Test
    public void testGetPropertyWithNullProperties() throws LagoonException {
        // Test getting a property with null properties
        when(propertiesMock.getProperty(Mockito.anyString())).thenReturn(null);

        assertEquals("", lagoonGUI.getProperty(propertiesMock, "sitemapFile"));
    }

    @Test
    public void testGetProperty() throws LagoonException {
        // Test getting a property
        when(propertiesMock.getProperty("sitemapFile")).thenReturn("test.xml");

        assertEquals("test.xml", lagoonGUI.getProperty(propertiesMock, "sitemapFile"));
    }

    @Test
    public void testShowMessageDialog() {
        // Test showing a message dialog
        final Component parentComponent = Mockito.mock(Component.class);
        final String message = "Test message";
        final int messageType = JOptionPane.INFORMATION_MESSAGE;

        lagoonGUI.showMessageDialog(parentComponent, message, "Test dialog", messageType);

        verify(lagoonGUI, times(1)).showMessageDialog(parentComponent, message, "Test dialog", messageType);
    }
}