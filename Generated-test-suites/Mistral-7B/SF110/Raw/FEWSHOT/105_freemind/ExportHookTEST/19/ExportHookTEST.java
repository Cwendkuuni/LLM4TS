package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.Controller;
import freemind.main.MainFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        Controller mockController = mock(Controller.class);
        ExportHook exportHook = new ExportHook() {
            @Override
            Controller getController() {
                return mockController;
            }
        };

        String type = "png";
        String description = "PNG Images";
        String nameExtension = null;
        File expectedFile = new File("expectedFile.png");
        when(mockController.getFrame()).thenReturn(mock(MainFrame.class));
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(JFileChooser.class));
        when(mockController.getMap()).thenReturn(mock(MapView.class));

        File actualFile = exportHook.chooseFile(type, description, nameExtension);

        assertEquals(expectedFile, actualFile);
        verify(mockController, times(1)).getFrame();
        verify(mockController, times(1)).getFileChooser(isA(FileFilter.class));
        verify(mockController, times(1)).getMap();
    }

    @Test
    public void testChooseImageFile() {
        Controller mockController = mock(Controller.class);
        ExportHook exportHook = new ExportHook() {
            @Override
            Controller getController() {
                return mockController;
            }
        };

        String type = "png";
        String description = "PNG Images";
        String nameExtension = null;

        FileFilter mockFileFilter = mock(FileFilter.class);
        when(mockFileFilter.accept(any(File.class))).thenReturn(true);
        when(mockController.getFileChooser(mockFileFilter)).thenReturn(mock(JFileChooser.class));

        File expectedFile = new File("expectedFile.png");
        when(mockController.getMap()).thenReturn(mock(MapView.class));
        when(mockController.getText(anyString())).thenReturn("file_already_exists");
        when(mockController.getFrame()).thenReturn(mock(MainFrame.class));

        File actualFile = exportHook.chooseImageFile(type, description, nameExtension, mockController);

        assertEquals(expectedFile, actualFile);
        verify(mockController, times(1)).getFileChooser(mockFileFilter);
        verify(mockController, times(1)).getText("file_already_exists");
        verify(mockController, times(1)).getFrame();
        verify(mockController, times(1)).getMap();
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "%resource_name%";
        String expectedValue = "expected_value";

        String returnValue = exportHook.getResourceString(resourceName);
        String actualValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedValue, actualValue);
        assertEquals(returnValue, actualValue);
    }

    @Test
    public void testCreateBufferedImage() throws IOException {
        ExportHook exportHook = new ExportHook();
        MapView mockMapView = mock(MapView.class);

        when(exportHook.getController()).thenReturn(mock(Controller.class));
        when(exportHook.getController().getView()).thenReturn(mockMapView);

        BufferedImage actualImage = exportHook.createBufferedImage();

        verify(mockMapView, times(1)).preparePrinting();
        verify(mockMapView, times(1)).createImage(anyInt(), anyInt());
        verify(mockMapView, times(1)).endPrinting();
        assertNotNull(actualImage);
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook, times(1)).getResource(prefix + fileName);
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        String dir = "dir";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook, times(1)).getLogger();
        verify(exportHook, times(1)).getLogger().finest("searching for " + dir + fileName);
        verify(exportHook, times(1)).getLogger().severe("Cannot find resource: " + dir + fileName);
    }
}