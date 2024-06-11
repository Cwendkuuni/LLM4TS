package nu.staldal.lagoon.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.MessageDialog;
import nu.staldal.lagoon.core.PasswordDialog;
import nu.staldal.lagoon.core.YesNoQueryDialog;
import nu.staldal.lagoon.core.xtree.TreeBuilder;
import nu.staldal.lagoon.gui.InputComponent;
import nu.staldal.lagoon.gui.LagoonGUI;
import nu.staldal.lagoon.gui.TextAreaWriter;

public class LagoonGUITest {
    private LagoonGUI appFrame;
    private LagoonProcessor mockProcessor;
    private InputComponent mockSitemapFile;
    private InputComponent mockSourceDir;
    private InputComponent mockTargetURL;
    private TextAreaWriter mockProgressArea;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        appFrame = new LagoonGUI();
        mockProcessor = mock(LagoonProcessor.class);
        mockSitemapFile = mock(InputComponent.class);
        mockSourceDir = mock(InputComponent.class);
        mockTargetURL = mock(InputComponent.class);
        mockProgressArea = mock(TextAreaWriter.class);

        appFrame.progressArea = mockProgressArea;
        appFrame.processor = mockProcessor;
        appFrame.sitemapFile = mockSitemapFile;
        appFrame.sourceDir = mockSourceDir;
        appFrame.targetURL = mockTargetURL;
    }

    @Test
    public void testInit() {
        when(mockSitemapFile.getValue()).thenReturn("testSitemap.xml");
        when(mockSitemapFile.hasChanged()).thenReturn(false);
        when(mockSourceDir.getValue()).thenReturn("testSourceDir");
        when(mockSourceDir.hasChanged()).thenReturn(false);
        when(mockTargetURL.getValue()).thenReturn("testTargetURL");
        when(mockTargetURL.hasChanged()).thenReturn(false);

        appFrame.init();

        verify(mockProcessor, never()).destroy();
        verify(mockProcessor, never()).build(anyBoolean());
        verifyNoMoreInteractions(mockProcessor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithChangedSitemapFile() {
        when(mockSitemapFile.getValue()).thenReturn("testSitemap.xml");
        when(mockSitemapFile.hasChanged()).thenReturn(true);
        when(mockSourceDir.getValue()).thenReturn("testSourceDir");
        when(mockSourceDir.hasChanged()).thenReturn(false);
        when(mockTargetURL.getValue()).thenReturn("testTargetURL");
        when(mockTargetURL.hasChanged()).thenReturn(false);

        appFrame.init();

        verify(mockProcessor).destroy();
        verify(mockProcessor).build(false);
        verify(mockProcessor).build(false);
        verify(mockProcessor).destroy();
        verify(mockProcessor, times(2)).build(false);
        assertEquals("Initializing...", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithFileNotFoundException() {
        when(mockSitemapFile.getValue()).thenThrow(new FileNotFoundException("Sitemap file not found"));

        appFrame.init();

        verify(mockProcessor).destroy();
        verify(mockProcessor, never()).build(anyBoolean());
        assertNull(appFrame.processor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithIOException() {
        when(mockSitemapFile.getValue()).thenThrow(new IOException("I/O error"));

        appFrame.init();

        verify(mockProcessor).destroy();
        verify(mockProcessor, never()).build(anyBoolean());
        assertNull(appFrame.processor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testInitWithLagoonException() {
        when(mockSitemapFile.getValue()).thenThrow(new LagoonException("Lagoon exception"));

        appFrame.init();

        verify(mockProcessor).destroy();
        verify(mockProcessor, never()).build(anyBoolean());
        assertNull(appFrame.processor);
        assertEquals("Not initialized", appFrame.statusLabel.getText());
    }

    @Test
    public void testBuildFalse() throws IOException {
        when(mockSitemapFile.getValue()).thenReturn("testSitemap.xml");
        when(mockSitemapFile.hasChanged()).thenReturn(false);
        when(mockSourceDir.getValue()).thenReturn("testSourceDir");
        when(mockSourceDir.hasChanged()).thenReturn(false);
        when(mockTargetURL.getValue()).thenReturn("testTargetURL");
        when(mockTargetURL.hasChanged()).thenReturn(false);

        appFrame.init();
        appFrame.loadButton.actionPerformed(mock(ActionEvent.class));

        verify(mockProcessor, never()).destroy();
        verify(mockProcessor, never()).build(anyBoolean());

        appFrame.buildButton.actionPerformed(mock(ActionEvent.class));

        verify(mockProcessor).build(false);
        assertEquals("Building...", appFrame.statusLabel.getText());
    }

    @Test
    public void testBuildTrue() throws IOException, InterruptedException {
        when(mockSitemapFile.getValue()).thenReturn("testSitemap.xml");
        when(mockSitemapFile.hasChanged()).thenReturn(false);
        when(mockSourceDir.getValue()).thenReturn("testSourceDir");
        when(mockSourceDir.hasChanged()).thenReturn(false);
        when(mockTargetURL.getValue()).thenReturn("testTargetURL");
        when(mockTargetURL.hasChanged()).thenReturn(false);
        when(mockProcessor.build(anyBoolean())).thenThrow(new InterruptedException("Building exception"));

        appFrame.init();
        appFrame.loadButton.actionPerformed(mock(ActionEvent.class));

        verify(mockProcessor, never()).destroy();
        verify(mockProcessor, never()).build(anyBoolean());

        appFrame.forceButton.actionPerformed(mock(ActionEvent.class));

        verify(mockProcessor).destroy();
        verify(mockProcessor).build(true);
        verify(mockProcessor).build(true);
        verify(mockProcessor).destroy();
        verify(mockProcessor, times(2)).build(true);
        assertEquals("Initializing...", appFrame.statusLabel.getText());
    }

    @Test
    public void testLoadPropertiesFile() {
        final File propertyFile = new File("testPropertyFile.properties");

        appFrame.loadPropertyFile();

        verify(mockSitemapFile).setValue("testSitemap.xml");
        verify(mockSourceDir).setValue("testSourceDir");
        verify(mockTargetURL).setValue("testTargetURL");
        verify(mockProgressArea).setText("--- Start build ---\n");
        verify(mockProcessor, never()).destroy();
        verify(mockProcessor, never()).build(anyBoolean());
    }

    @Test
    public void testSavePropertiesFile() throws IOException {
        final File propertyFile = new File("testPropertyFile.properties");
        when(mockSitemapFile.getValue()).thenReturn("testSitemap.xml");
        when(mockSourceDir.getValue()).thenReturn("testSourceDir");
        when(mockTargetURL.getValue()).thenReturn("testTargetURL");

        appFrame.savePropertyFile();

        verify(mockSitemapFile).setValue("testSitemap.xml");
        verify(mockSourceDir).setValue("testSourceDir");
        verify(mockTargetURL).setValue("testTargetURL");
        verify(mockProgressArea).setText("--- Start build ---\n");
        verify(mockProcessor).destroy();
        verify(mockProcessor, never()).build(anyBoolean());

        verify(mockProgressArea).append("--- Build finished successfully " + anyString() + " ---");
    }
}