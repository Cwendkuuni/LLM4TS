package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

    @Mock
    private ModeController controller;

    private ExportHook exportHook;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        exportHook = new ExportHook();
        exportHook.setController(controller);
    }

    @Test
    public void testChooseFileNullArguments() {
        assertNull(exportHook.chooseFile(null, null, null));
        assertNull(exportHook.chooseFile(null, null, ""));
        assertNull(exportHook.chooseFile("", null, ""));
    }

    @Test
    public void testChooseFileValidArguments() throws IOException {
        final String type = "png";
        final String description = "Export as PNG";
        final String nameExtension = "png";

        final FileFilter imageFilter = mock(FileFilter.class);
        when(controller.getFileChooser(imageFilter)).thenReturn(new JFileChooser());

        final File chosenFile = new File("test.png");
        when(controller.getMap().getFile()).thenReturn(new File("test.mm"));

        final JFileChooser fileChooser = exportHook.chooseImageFile(type, description, nameExtension, controller);

        verify(controller, times(1)).getFileChooser(imageFilter);

        final ArgumentCaptor<FileFilter> imageFilterArgumentCaptor = ArgumentCaptor.forClass(FileFilter.class);
        verify(fileChooser, times(1)).setFileFilter(imageFilterArgumentCaptor.capture());

        final ImageFilter capturedImageFilter = imageFilterArgumentCaptor.getValue();
        assertEquals("Expected image filter type to match", type, capturedImageFilter.getType());
        assertEquals("Expected image filter description to match", description, capturedImageFilter.getDescription());

        fileChooser.setSelectedFile(chosenFile);

        final int returnVal = fileChooser.showSaveDialog(mock(Container.class));

        verify(controller, times(1)).getText("file_already_exists");
        verifyNoMoreInteractions(controller);

        assertEquals("Expected chosen file to match the selected file from the JFileChooser", chosenFile, exportHook.chooseImageFile(type, description, nameExtension, controller).getSelectedFile());
    }

    @Test
    public void testGetTranslatableResourceStringNullResourceName() {
        assertNull(exportHook.getTranslatableResourceString(null));
    }

    @Test
    public void testGetTranslatableResourceStringValidResourceName() {
        final String resourceName = "test.resource";
        final String expectedResult = "test result";

        when(controller.getText(anyString())).thenReturn(expectedResult);
        final String result = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("Expected getTranslatableResourceString to return the expected result", expectedResult, result);
    }

    @Test
    public void testCreateBufferedImageNullView() {
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCreateBufferedImageValidView() throws Exception {
        final Image image = mock(Image.class);
        when(controller.getView()).thenReturn(new MindMapView() {
            @Override
            public Image createImage(int width, int height) {
                return image;
            }
        });

        final BufferedImage bufferedImage = exportHook.createBufferedImage();

        verify(controller.getView(), times(1)).preparePrinting();
        verify(controller.getView(), times(1)).getInnerBounds();
        verify(controller.getView(), times(1)).createImage(anyInt(), anyInt());
        verify(controller.getView(), times(1)).endPrinting();

        assertEquals("Expected buffered image width to match the MindMapView width", exportHook.getController().getView().getWidth(), bufferedImage.getWidth());
        assertEquals("Expected buffered image height to match the MindMapView height", exportHook.getController().getView().getHeight(), bufferedImage.getHeight());
    }

    @Test
    public void testCopyFromResourceNullPrefixOrFileName() {
        exportHook.copyFromResource(null, null, "destinationDirectory");
    }

    @Test
    public void testCopyFromResourceValidPrefixAndFileName() throws IOException {
        final String prefix = "test";
        final String fileName = "test.txt";
        final String destinationDirectory = "destinationDirectory";

        final URL resource = new URL("file:test.txt");
        when(exportHook.getResource(prefix + fileName)).thenReturn(resource);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook, times(1)).getResource(prefix + fileName);
        verify(new FileOutputStream(destinationDirectory + "/" + fileName), times(1)).write(anyByteArray());
    }

    @Test
    public void testCopyFromFileNullDirOrFileName() {
        exportHook.copyFromFile(null, null, "destinationDirectory");
    }

    @Test
    public void testCopyFromFileValidDirAndFileName() throws IOException {
        final String dir = "test";
        final String fileName = "test.txt";
        final String destinationDirectory = "destinationDirectory";

        final File file = new File("test.txt");
        when(new File(dir, fileName).exists()).thenReturn(true);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook, times(1)).getLogger().finest("searching for " + dir + fileName);
        verify(new FileOutputStream(destinationDirectory + "/" + fileName), times(1)).write(anyByteArray());
    }
}