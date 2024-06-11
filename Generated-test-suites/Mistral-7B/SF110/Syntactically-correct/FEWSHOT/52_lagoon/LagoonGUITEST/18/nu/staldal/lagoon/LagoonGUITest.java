package nu.staldal.lagoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class LagoonGUITest {

    private LagoonGUI appFrame;
    private InputComponent sitemapFile, sourceDir, targetURL;
    private Button loadButton, saveButton, buildButton, forceButton, exitButton;
    private Label statusLabel;
    private TextArea progressArea;
    private LagoonProcessor processorMock;

    @Before
    public void setup() {
        appFrame = new LagoonGUI();
        sitemapFile = mock(InputComponent.class);
        sourceDir = mock(InputComponent.class);
        targetURL = mock(InputComponent.class);
        loadButton = mock(Button.class);
        saveButton = mock(Button.class);
        buildButton = mock(Button.class);
        forceButton = mock(Button.class);
        exitButton = mock(Button.class);
        statusLabel = mock(Label.class);
        progressArea = mock(TextArea.class);
        processorMock = mock(LagoonProcessor.class);

        when(appFrame.getSitemapFile()).thenReturn(sitemapFile);
        when(appFrame.getSourceDir()).thenReturn(sourceDir);
        when(appFrame.getTargetURL()).thenReturn(targetURL);
        when(appFrame.getLoadButton()).thenReturn(loadButton);
        when(appFrame.getSaveButton()).thenReturn(saveButton);
        when(appFrame.getBuildButton()).thenReturn(buildButton);
        when(appFrame.getForceButton()).thenReturn(forceButton);
        when(appFrame.getExitButton()).thenReturn(exitButton);
        when(appFrame.getStatusLabel()).thenReturn(statusLabel);
        when(appFrame.getProgressArea()).thenReturn(progressArea);
        when(appFrame.getProcessor()).thenReturn(processorMock);

        appFrame.add(appFrame.getInputPanel(), "North");
        appFrame.add(appFrame.getCenterPanel(), "Center");
        appFrame.add(appFrame.getButtonPanel(), "South");
    }

    @After
    public void cleanup() {
        appFrame.dispose();
    }

    @Test
    public void testInitializeComponents() {
        verify(appFrame, times(1)).add(appFrame.getInputPanel(), "North");
        verify(appFrame, times(1)).add(appFrame.getCenterPanel(), "Center");
        verify(appFrame, times(1)).add(appFrame.getButtonPanel(), "South");
    }

    @Test
    public void testLoadPropertiesWhenFileIsNull() {
        appFrame.loadPropertyFile();
        verify(appFrame, times(0)).loadProperties(any(File.class));
    }

    @Test
    public void testLoadPropertiesWhenFileIsProvided() {
        File file = Mockito.mock(File.class);
        appFrame.loadPropertyFile();
        verify(appFrame, times(1)).loadProperties(file);
    }

    @Test
    public void testSavePropertiesWhenFileIsNull() {
        appFrame.savePropertyFile();
        verify(appFrame, times(0)).savePropertyFile();
    }

    @Test
    public void testSavePropertiesWhenFileIsProvided() {
        File file = Mockito.mock(File.class);
        appFrame.savePropertyFile();
        verify(appFrame, times(1)).savePropertyFile();
    }

    @Test
    public void testBuildWhenForceIsFalse() {
        appFrame.build(false);
        verify(appFrame, times(1)).build(false);
    }

    @Test
    public void testBuildWhenForceIsTrue() {
        appFrame.build(true);
        verify(appFrame, times(1)).build(true);
    }

    @Test
    public void testExit() {
        appFrame.exit();
        verify(appFrame, times(1)).exit();
    }

    @Test
    public void testInitWhenProcessorIsNotNullAndSettingsHaveChanged() {
        when(sitemapFile.hasChanged()).thenReturn(true);
        when(sourceDir.hasChanged()).thenReturn(true);
        when(targetURL.hasChanged()).thenReturn(true);

        appFrame.init();
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testInitWhenProcessorIsNotNullButSettingsHaveNotChanged() {
        when(sitemapFile.hasChanged()).thenReturn(false);
        when(sourceDir.hasChanged()).thenReturn(false);
        when(targetURL.hasChanged()).thenReturn(false);

        appFrame.init();
        verify(appFrame, times(0)).init();
    }

    @Test
    public void testInitWhenProcessorIsNull() {
        appFrame.init();
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testInitWhenSitemapFileDoesNotExist() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(false);

        appFrame.init();
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testInitWhenSitemapFileLastModifiedIsGreaterThanLastKnown() {
        when(new File(sitemapFile.getValue()).lastModified()).thenReturn(100L);
        when(appFrame.getSitemapLastModified()).thenReturn(0L);

        appFrame.init();
        verify(appFrame, times(1)).init();
    }

    @Test
    public void testInitWhenSitemapFileLastModifiedIsSmallerOrEqualToLastKnown() {
        when(new File(sitemapFile.getValue()).lastModified()).thenReturn(0L);
        when(appFrame.getSitemapLastModified()).thenReturn(0L);

        appFrame.init();
        verify(appFrame, times(0)).init();
    }

    @Test
    public void testBuildWhenProcessorIsNull() {
        appFrame.build(false);
        verify(appFrame, times(1)).init();
        verify(processorMock, never()).build(anyBoolean());
    }

    @Test
    public void testBuildWhenProcessorIsNotNull() {
        appFrame.build(false);
        verify(appFrame, times(1)).build(false);
        verify(processorMock, times(1)).build(false);
    }

    @Test
    public void testBuildWhenExceptionIsThrown() {
        when(processorMock.build(anyBoolean())).thenThrow(new IOException());

        appFrame.build(false);
        verify(appFrame, times(1)).build(false);
        verify(processorMock, times(1)).build(false);
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringParsingXML() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenThrow(new SAXException());

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringCreatingLagoonProcessor() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(Mockito.mock(Element.class));
        when(appFrame.getPassword()).thenReturn("password");

        doThrow(new AuthenticationMissingException("Authentication missing")).when(processorMock).__constructor__(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringCreatingLagoonProcessorWithInvalidPassword() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(Mockito.mock(Element.class));

        doThrow(new AuthenticationException("Invalid password")).when(processorMock).__constructor__(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringCreatingLagoonProcessorWithFileNotFound() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(Mockito.mock(Element.class));

        doThrow(new FileNotFoundException("File not found")).when(processorMock).__constructor__(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringCreatingLagoonProcessorWithIOException() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(Mockito.mock(Element.class));

        doThrow(new IOException("IOException")).when(processorMock).__constructor__(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testInitWhenExceptionIsThrownDuringCreatingLagoonProcessorWithLagoonException() {
        when(new File(sitemapFile.getValue()).exists()).thenReturn(true);
        when(TreeBuilder.parseXML(any(InputSource.class), anyBoolean())).thenReturn(Mockito.mock(Element.class));

        doThrow(new LagoonException("LagoonException")).when(processorMock).__constructor__(anyString(), any(Element.class), any(File.class), anyString(), any(PrintWriter.class), any(PrintWriter.class));

        appFrame.init();
        verify(appFrame, times(1)).init();
        verify(appFrame, times(1)).showMessageDialog(anyString(), anyString());
    }

    @Test
    public void testLoadPropertiesWhenFileDoesNotExist() {
        when(sitemapFile.getValue()).thenReturn("nonexistentfile.properties");

        appFrame.loadProperties(new File("nonexistentdirectory"));
        verify(appFrame, times(0)).loadProperties(any(File.class));
    }

    @Test
    public void testLoadPropertiesWhenFileExists() {
        File file = Mockito.mock(File.class);
        when(sitemapFile.getValue()).thenReturn("testfile.properties");

        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        appFrame.loadPropertyFile();
        verify(appFrame, times(1)).loadProperties(fileCaptor.capture());
        assertEquals("testfile.properties", fileCaptor.getValue().getPath());
    }

    @Test
    public void testSavePropertiesWhenFileDoesNotExist() {
        when(appFrame.getSitemapFile().getValue()).thenReturn("testfile.properties");

        appFrame.savePropertyFile();
        verify(appFrame, times(0)).savePropertyFile();
    }

    @Test
    public void testSavePropertiesWhenFileExists() {
        File file = Mockito.mock(File.class);
        when(appFrame.getSitemapFile().getValue()).thenReturn("testfile.properties");

        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        appFrame.savePropertyFile();
        verify(appFrame, times(1)).savePropertyFile();
        assertEquals("testfile.properties", fileCaptor.getValue().getPath());
    }

    @Test
    public void testSavePropertyFileWhenYesIsSelected() {
        when(appFrame.getSitemapFile().getValue()).thenReturn("testfile.properties");
        when(appFrame.getProperties().getProperty("password")).thenReturn("password");

        YesNoQueryDialog yesNoDialog = Mockito.mock(YesNoQueryDialog.class);
        when(yesNoDialog.getResult()).thenReturn(true);

        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        appFrame.savePropertyFile();
        verify(appFrame, times(1)).savePropertyFile();
        verify(appFrame, times(1)).showDialog(anyString(), anyString(), eq(YesNoQueryDialog.class), eq("Save property file"), any(String.class), any(String.class), any(String.class), any(String.class));
        verify(appFrame, times(1)).savePropertyFile();
        verify(appFrame, times(1)).setStatusLabelText("Properties saved");
    }

    @Test
    public void testSavePropertyFileWhenNoIsSelected() {
        when(appFrame.getSitemapFile().getValue()).thenReturn("testfile.properties");

        YesNoQueryDialog yesNoDialog = Mockito.mock(YesNoQueryDialog.class);
        when(yesNoDialog.getResult()).thenReturn(false);

        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        appFrame.savePropertyFile();
        verify(appFrame, times(0)).savePropertyFile();
        verify(appFrame, times(1)).showDialog(anyString(), anyString(), eq(YesNoQueryDialog.class), eq("Save property file"), any(String.class), any(String.class), any(String.class), any(String.class));
    }

}