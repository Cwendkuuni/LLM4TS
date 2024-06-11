package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class LagoonGUITest {

    @Test
    public void testInitialize() {
        final LagoonGUI appFrame = new LagoonGUI();

        // Initialization should happen when the build button is clicked for the first time
        appFrame.loadButton.doClick();
        appFrame.buildButton.doClick();

        // Check that init method is called
        verify(appFrame, times(1)).init();

        // Check that UI components are disabled during initialization
        final ArgumentCaptor<ActionEvent> eventCaptor = ArgumentCaptor.forClass(ActionEvent.class);
        verify(appFrame.loadButton, times(1)).setEnabled(false);
        verify(appFrame.saveButton, times(1)).setEnabled(false);
        verify(appFrame.buildButton, times(1)).setEnabled(false);
        verify(appFrame.forceButton, times(1)).setEnabled(false);
        verify(appFrame.sitemapFile, times(1)).setEnabled(false);
        verify(appFrame.sourceDir, times(1)).setEnabled(false);
        verify(appFrame.targetURL, times(1)).setEnabled(false);
    }

    @Test
    public void testBuildSuccess() throws Exception {
        final LagoonGUI appFrame = mock(LagoonGUI.class);
        final LagoonProcessor processor = mock(LagoonProcessor.class);

        when(appFrame.init()).thenCallRealMethod();
        when(appFrame.processor).thenReturn(processor);

        appFrame.loadButton.doClick();
        appFrame.buildButton.doClick();

        // Call init method to ensure initialization
        appFrame.init();

        // Initialize mock LagoonProcessor with a successful build result
        final boolean success = true;
        when(processor.build(anyBoolean())).thenReturn(success);

        // Test progressArea and statusLabel updates
        runApplication(appFrame);

        // Check that progressArea and statusLabel contain the expected messages
        verify(appFrame.progressArea, times(1)).append("--- Start build ---");
        verify(appFrame.progressArea, times(1)).append("--- Build finished successfully ---");
        verify(appFrame.statusLabel, times(1)).setText("Ready");
    }

    @Test
    public void testBuildFailure() throws Exception {
        final LagoonGUI appFrame = mock(LagoonGUI.class);
        final LagoonProcessor processor = mock(LagoonProcessor.class);

        when(appFrame.init()).thenCallRealMethod();
        when(appFrame.processor).thenReturn(processor);

        appFrame.loadButton.doClick();
        appFrame.buildButton.doClick();

        // Call init method to ensure initialization
        appFrame.init();

        // Initialize mock LagoonProcessor with a failed build result
        final boolean success = false;
        when(processor.build(anyBoolean())).thenThrow(new IOException("Build failed"));

        // Test progressArea and statusLabel updates
        runApplication(appFrame);

        // Check that progressArea and statusLabel contain the expected messages
        verify(appFrame.progressArea, times(1)).append("--- Start build ---");
        verify(appFrame.progressArea, times(1)).append("--- Build finished with error(s) ---");
        verify(appFrame.statusLabel, times(1)).setText("Ready");
    }

    @Test
    public void testLoadProperties() throws Exception {
        final LagoonGUI appFrame = new LagoonGUI();
        final Properties prop = new Properties();

        // Set up the properties to be loaded
        prop.setProperty("sitemapFile", "testSitemap.xml");
        prop.setProperty("sourceDir", "testSourceDir");
        prop.setProperty("targetURL", "http://test.com");

        // Test loading properties
        appFrame.loadProperties(new File("testProperties.xml"));

        // Check that sitemapFile, sourceDir, and targetURL components have the expected values
        assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
        assertEquals("testSourceDir", appFrame.sourceDir.getValue());
        assertEquals("http://test.com", appFrame.targetURL.getValue());
    }

    @Test
    public void testSaveProperties() throws Exception {
        final LagoonGUI appFrame = new LagoonGUI();

        // Test saving properties
        appFrame.savePropertyFile();

        // Create a mock FileDialog and save a File with a property file
        final FileDialog propertyFileDialog = mock(FileDialog.class);
        when(propertyFileDialog.getFile()).thenReturn("testProperties.xml");
        appFrame.loadProperties(new File("testProperties.xml")); // Load the file to initialize properties
        appFrame.savePropertyFile();

        // Check that the property file is saved with the expected values
        verify(propertyFileDialog, times(1)).show();
        final File propertyFile = new File("testProperties.xml");
        try (FileReader reader = new FileReader(propertyFile)) {
            final Properties prop = new Properties();
            prop.load(reader);
            assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
            assertEquals("testSourceDir", prop.getProperty("sourceDir"));
            assertEquals("http://test.com", prop.getProperty("targetURL"));
        }
    }

    @Test
    public void testAuthenticate() throws Exception {
        final LagoonGUI appFrame = new LagoonGUI();

        // Test authentication dialogs
        appFrame.loadPropertyFile();
        appFrame.init();

        // Mock the AuthenticationMissingException and AuthenticationException
        final AuthenticationMissingException authenticationMissingException = mock(AuthenticationMissingException.class);
        final AuthenticationException authenticationException = mock(AuthenticationException.class);

        // Test the case where the password is provided on the first attempt
        doThrow(authenticationMissingException).when(appFrame).init();
        appFrame.password = "testPassword";
        appFrame.init();

        // Test the case where the password is not provided on the first attempt
        reset(appFrame);
        doThrow(authenticationMissingException).when(appFrame).init();
        appFrame.init();
        final PasswordDialog pwd1 = new PasswordDialog(appFrame, "Password", "Password needed to access target");
        pwd1.show();
        appFrame.password = "testPassword";
        appFrame.init();

        // Test the case where the password is incorrect
        reset(appFrame);
        doThrow(authenticationException).when(appFrame).init();
        appFrame.password = "incorrectPassword";
        appFrame.init();
        final PasswordDialog pwd2 = new PasswordDialog(appFrame, "Password", "Invalid password, try again");
        pwd2.show();
        appFrame.password = "testPassword";
        appFrame.init();
    }

    private void runApplication(final LagoonGUI appFrame) throws InterruptedException, IOException, SAXException {
        // Start the event dispatch thread to simulate user interaction
        SwingUtilities.invokeAndWait(appFrame::show);

        // Wait for the application to finish initializing
        appFrame.addWindowListener(arg0 -> {
            appFrame.dispatchEvent(new WindowEvent(appFrame, WindowEvent.WINDOW_OPENED));
        });

        // Wait for the application to finish building
        appFrame.addWindowListener(arg0 -> {
            if (arg0.getID() == WindowEvent.WINDOW_CLOSING) {
                appFrame.exit();
            }
        });

        // Wait for the application to finish
        appFrame.setVisible(true);
        appFrame.waitForIdle();
        appFrame.dispose();
    }
}