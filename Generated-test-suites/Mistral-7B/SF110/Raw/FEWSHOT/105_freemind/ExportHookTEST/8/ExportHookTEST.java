package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.ModeController;
import freemind.model.MapModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController modeControllerMock;
    private MapView mapViewMock;
    private MapModel mapModelMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        modeControllerMock = mock(ModeController.class);
        mapViewMock = mock(MapView.class);
        mapModelMock = mock(MapModel.class);
        exportHook = new ExportHook();
        exportHook.setController(modeControllerMock);
        when(modeControllerMock.getView()).thenReturn(mapViewMock);
        when(modeControllerMock.getMap()).thenReturn(mapModelMock);
    }

    @Test
    public void testChooseFileReturnsNullIfCancelButtonIsClicked() {
        FreeMindFileDialog chooserMock = mock(FreeMindFileDialog.class);
        when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(chooserMock);
        int returnValue = 1;
        when(chooserMock.showSaveDialog(any())).thenReturn(returnValue);

        File result = exportHook.chooseFile("image/jpeg", "Save as JPEG", null);
        assertNull(result);
    }

    @Test
    public void testChooseFileReturnsFileIfSaveButtonIsClicked() {
        File expectedFile = new File("path/to/save/file.jpeg");
        FreeMindFileDialog chooserMock = mock(FreeMindFileDialog.class);
        when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(chooserMock);
        when(chooserMock.showSaveDialog(any())).thenReturn(JOptionPane.OK_OPTION);
        when(chooserMock.getSelectedFile()).thenReturn(expectedFile);

        File result = exportHook.chooseFile("image/jpeg", "Save as JPEG", null);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testChooseFileUsesDefaultFileNameIfMapFileIsNotEmpty() {
        String mapFilePath = "path/to/map/file.mindmap";
        File expectedFile = new File(mapFilePath.replaceFirst("\\.[^.]*?$", "") + ".jpeg");
        FreeMindFileDialog chooserMock = mock(FreeMindFileDialog.class);
        when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(chooserMock);
        when(modeControllerMock.getMap()).thenReturn(mapModelMock);
        when(mapModelMock.getFile()).thenReturn(new File(mapFilePath));
        when(chooserMock.showSaveDialog(any())).thenReturn(JOptionPane.OK_OPTION);

        File result = exportHook.chooseFile("image/jpeg", "Save as JPEG", null);
        assertEquals(expectedFile, result);
    }

    @Test
    public void testCreateBufferedImageReturnsNullIfMapViewIsNull() {
        when(modeControllerMock.getView()).thenReturn(null);
        BufferedImage result = exportHook.createBufferedImage();
        assertNull(result);
    }

    @Test
    public void testCreateBufferedImageReturnsBufferedImageIfMapViewIsNotNull() {
        BufferedImage expectedBufferedImage = mock(BufferedImage.class);
        when(modeControllerMock.getView()).thenReturn(mapViewMock);
        when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(expectedBufferedImage);

        BufferedImage result = exportHook.createBufferedImage();
        assertEquals(expectedBufferedImage, result);
    }

    @Test
    public void testCopyFromResourceCopiesFileIfResourceIsAvailable() throws IOException {
        String prefix = "export/";
        String fileName = "example.txt";
        String destinationDirectory = "path/to/destination/directory";

        URL resource = new URL("resource:/path/to/resource/export/example.txt");
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(modeControllerMock).getLogger().finest("searching for export/example.txt");
        verify(modeControllerMock).getLogger().finest("copying from resource:/path/to/resource/export/example.txt to path/to/destination/directory/example.txt");
    }

    @Test(expected = FileNotFoundException.class)
    public void testCopyFromResourceThrowsExceptionIfResourceIsNotAvailable() throws IOException {
        String prefix = "export/";
        String fileName = "example.txt";
        String destinationDirectory = "path/to/destination/directory";

        URL resource = null;
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(modeControllerMock).getLogger().finest("searching for export/example.txt");
    }

    @Test
    public void testCopyFromFileCopiesFileIfFileIsAvailable() throws IOException {
        String dir = "path/to/source/directory";
        String fileName = "example.txt";
        String destinationDirectory = "path/to/destination/directory";

        File sourceFile = new File(dir, "example.txt");
        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(modeControllerMock).getLogger().finest("searching for path/to/source/directory/example.txt");
        verify(modeControllerMock).getLogger().finest("copying from path/to/source/directory/example.txt to path/to/destination/directory/example.txt");
    }

    @Test(expected = FileNotFoundException.class)
    public void testCopyFromFileThrowsExceptionIfFileIsNotAvailable() throws IOException {
        String dir = "path/to/source/directory";
        String fileName = "example.txt";
        String destinationDirectory = "path/to/destination/directory";

        File sourceFile = null;
        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(modeControllerMock).getLogger().finest("searching for path/to/source/directory/example.txt");
    }
}