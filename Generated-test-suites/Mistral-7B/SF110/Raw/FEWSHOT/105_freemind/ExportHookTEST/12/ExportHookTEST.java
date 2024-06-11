package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.Controller;
import freemind.modes.MapController;
import freemind.main.MindMapDocument;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExportHookTest {

    private Controller mockController;
    private MapView mockView;
    private ExportHook exportHook;

    @Before
    public void setUp() {
        mockController = mock(Controller.class);
        mockView = mock(MapView.class);
        exportHook = new ExportHook() {
            @Override
            public MapView getView() {
                return mockView;
            }

            @Override
            public Controller getController() {
                return mockController;
            }
        };
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "Export as image";
        String nameExtension = null;

        File expectedFile = new File("expectedFile.png");
        when(mockController.getFrame()).thenReturn(mock(JFrame.class));
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));
        when(mockController.getMap()).thenReturn(new MindMapDocument());

        File actualFile = exportHook.chooseFile(type, description, nameExtension);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verify(mockController, times(1)).getFrame();
        verifyNoMoreInteractions(mockController);

        verify(mockController.getFileChooser(any(FileFilter.class))).setSelectedFile(expectedFile);
        verify(mockController.getFileChooser(any(FileFilter.class))).showSaveDialog(any(Container.class));

        assertTrue(actualFile.equals(expectedFile));
    }

    @Test
    public void testChooseFile_FileAlreadyExists() {
        String type = "png";
        String description = "Export as image";
        String nameExtension = null;

        File expectedFile = new File("expectedFile.png");
        when(mockController.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));
        when(mockController.getFrame()).thenReturn(mock(JFrame.class));
        when(mockController.getMap()).thenReturn(new MindMapDocument());

        File actualFile = exportHook.chooseFile(type, description, nameExtension);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verify(mockController, times(1)).getFrame();
        verify(mockController, times(1)).getText("file_already_exists");
        verify(mockController, times(1)).showConfirmDialog(any(Container.class), anyString(), anyString(), JOptionPane.YES_NO_OPTION);

        assertTrue(actualFile.equals(expectedFile));
    }

    @Test
    public void testChooseFile_Cancel() {
        String type = "png";
        String description = "Export as image";
        String nameExtension = null;

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));
        when(mockController.getFrame()).thenReturn(mock(JFrame.class));
        when(mockController.getMap()).thenReturn(new MindMapDocument());

        File actualFile = exportHook.chooseFile(type, description, nameExtension);

        verify(mockController, times(1)).getFileChooser(any(FileFilter.class));
        verify(mockController, times(1)).getFrame();
        verify(mockController.getFileChooser(any(FileFilter.class))).showSaveDialog(any(Container.class));

        verify(mockController.getFileChooser(any(FreeMindFileDialog.class))).setSelectedFile(null);

        assertNull(actualFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%resourceName%";

        String expectedResult = "Expected result";
        when(mockController.getText(resourceName.substring(1))).thenReturn(expectedResult);

        String actualResult = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetTranslatableResourceString_NoTranslation() {
        String resourceName = "%resourceName%";

        String expectedResult = "Expected result";
        when(mockController.getText(resourceName.substring(1))).thenReturn(null);

        String actualResult = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCreateBufferedImage() {
        when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));

        BufferedImage expectedBufferedImage = mock(BufferedImage.class);
        when(mockView.createImage(10, 10)).thenReturn(expectedBufferedImage);

        BufferedImage actualBufferedImage = exportHook.createBufferedImage();

        assertEquals(expectedBufferedImage, actualBufferedImage);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "prefix/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        URL resource = mock(URL.class);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(mockController).finest("searching for " + prefix + fileName);
        verify(mockController).getResource(prefix + fileName);
        verify(mockController).logException(any(Exception.class));

        verify(resource).openStream();
        verify(in).close();
        verify(out).close();
    }

    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "dir/";
        String fileName = "file.txt";
        String destinationDirectory = "destination";

        File resource = mock(File.class);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(mockController).finest("searching for " + dir + fileName);
        verify(mockController).logException(any(Exception.class));

        verify(resource).exists();
        verify(resource).isFile();
        verify(resource).canRead();

        verify(mockController).getResource(eq(dir + fileName));
        verify(mockController).logException(any(Exception.class));

        verify(resource).openInputStream();
        verify(in).close();

        verify(destinationDirectory).mkdirs();
        verify(mockController).logException(any(Exception.class));

        verify(destinationDirectory).exists();

        verify(destinationDirectory).getFileOutputStream(eq(fileName));
        verify(out).close();
    }
}