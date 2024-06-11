package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.Controller;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileChooserDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        Controller mockController = mock(Controller.class);
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mockController);

        String type = "png";
        String description = "Image files";
        String nameExtension = null;
        File expectedFile = new File("path/to/file.png");

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(createFileChooser());
        when(mockController.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        File actualFile = exportHook.chooseFile(type, description, nameExtension);
        assertEquals(expectedFile, actualFile);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(mockController);
    }

    @Test
    public void testChooseFile_FileAlreadyExists() {
        Controller mockController = mock(Controller.class);
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mockController);

        String type = "png";
        String description = "Image files";
        String nameExtension = null;
        File existingFile = new File("path/to/existingFile.png");
        File newFile = new File("path/to/newFile.png");

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(createFileChooser());
        when(mockController.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(mockController.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);

        File actualFile = exportHook.chooseFile(type, description, nameExtension);
        assertEquals(newFile, actualFile);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verify(mockController, times(1)).showConfirmDialog(any(Component.class), anyString(), anyString(), 0);
    }

    @Test
    public void testChooseFile_UserCancels() {
        Controller mockController = mock(Controller.class);
        ExportHook exportHook = new ExportHook();
        exportHook.setController(mockController);

        String type = "png";
        String description = "Image files";
        String nameExtension = null;

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(createFileChooser());
        when(mockController.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        File actualFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(actualFile);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(mockController);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        exportHook.setView(new MapView());

        BufferedImage expectedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

        BufferedImage actualImage = exportHook.createBufferedImage();
        assertEquals(expectedImage, actualImage);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();

        String resourceName = "%resourceKey%";
        String expectedString = "Some translated string";

        String actualString = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetTranslatableResourceString_NoTranslation() {
        ExportHook exportHook = new ExportHook();

        String resourceName = "SomeResourceKey";
        String expectedString = "SomeResourceKey";

        String actualString = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedString, actualString);
    }

    private FileChooserDialog createFileChooser() {
        FileChooserDialog fileChooser = mock(FileChooserDialog.class);
        when(fileChooser.showSaveDialog(any(Component.class))).thenReturn(JOptionPane.YES_OPTION);
        return fileChooser;
    }
}