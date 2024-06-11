package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXException;
import nu.staldal.lagoon.*;
import nu.staldal.lagoon.core.*;
import nu.staldal.xtree.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class LagoonGUITest {

    private LagoonGUI appFrame;
    private ActionEvent actionEvent;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        appFrame = spy(new LagoonGUI());
        actionEvent = mock(ActionEvent.class);
    }

    @After
    public void tearDown() {
        appFrame = null;
    }

    @Test
    public void testInit() throws Exception {
        appFrame.init();
        assertEquals("Initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testBuildWithoutForce() throws Exception {
        when(appFrame.sitemapFile.hasChanged()).thenReturn(false);
        when(appFrame.sitemapFile.getValue()).thenReturn(new File("sitemap.xml").getAbsolutePath());
        appFrame.build(false);
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testBuildWithForce() throws Exception {
        when(appFrame.sitemapFile.hasChanged()).thenReturn(true);
        when(appFrame.sitemapFile.getValue()).thenReturn(new File("sitemap.xml").getAbsolutePath());
        appFrame.build(true);
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testExit() {
        appFrame.exit();
        assertTrue(appFrame.isDisplayable());
        verify(appFrame, times(1)).destroy();
    }

    @Test
    public void testLoadProperties() throws Exception {
        final File propertyFile = new File("propertyFile.txt");
        appFrame.loadProperties(propertyFile);
        assertEquals("propertyFile.txt", appFrame.sitemapFile.getValue());
        assertEquals(".sourceDirPath", appFrame.sourceDir.getValue());
        assertEquals(".targetURL", appFrame.targetURL.getValue());
        assertNull(appFrame.password);
    }

    @Test
    public void testSaveProperties() throws IOException {
        final File propertyFile = new File("propertyFile.txt");
        appFrame.savePropertyFile();
        assertTrue(propertyFile.exists());
        assertTrue(propertyFile.length() > 0);
    }

    @Test
    public void testLoadPropertyFile() {
        final FileDialog propertyFileDialog = mock(FileDialog.class);
        when(propertyFileDialog.show()).thenReturn(WindowConstants.FILE_DIALOG_OK);
        when(propertyFileDialog.getFile()).thenReturn(new File("propertyFile.txt"));
        appFrame.loadPropertyFile();
        verify(appFrame, times(1)).loadProperties(propertyFileDialog.getFile());
    }

    @Test
    public void testActionPerformedLoadButton() {
        appFrame.loadButton.addActionListener(appFrame);
        appFrame.loadButton.actionPerformed(actionEvent);
        verify(appFrame, times(1)).loadPropertyFile();
    }

    @Test
    public void testActionPerformedSaveButton() {
        appFrame.saveButton.addActionListener(appFrame);
        appFrame.saveButton.actionPerformed(actionEvent);
        verify(appFrame, times(1)).savePropertyFile();
    }

    @Test
    public void testActionPerformedBuildButton() {
        appFrame.buildButton.addActionListener(appFrame);
        appFrame.buildButton.actionPerformed(actionEvent);
        verify(appFrame, times(1)).build(false);
    }

    @Test
    public void testActionPerformedForceButton() {
        appFrame.forceButton.addActionListener(appFrame);
        appFrame.forceButton.actionPerformed(actionEvent);
        verify(appFrame, times(1)).build(true);
    }

    @Test
    public void testActionPerformedExitButton() {
        appFrame.exitButton.addActionListener(appFrame);
        appFrame.exitButton.actionPerformed(actionEvent);
        verify(appFrame, times(1)).exit();
    }
}