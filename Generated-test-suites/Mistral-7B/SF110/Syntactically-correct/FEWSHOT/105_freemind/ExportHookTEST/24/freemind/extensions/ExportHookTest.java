package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.main.FreeMindFrame;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

    private ExportHook hook;
    private ModeController controller;
    private FreeMindFrame frame;
    private MapController mapController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = mock(ModeController.class);
        frame = mock(FreeMindFrame.class);
        when(controller.getFrame()).thenReturn(frame);
        mapController = mock(MapController.class);
        when(controller.getView()).thenReturn(mapController);
        hook = new ExportHook();
        hook.setController(controller);
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        File nameFile = new File("src/test/resources/test.png");

        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(new JFileChooser());

        File result = hook.chooseFile(type, description, null);

        File expected = new File("src/test/resources/test.png");
        assertEquals(expected, result);

        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
    }

    @Test
    public void testChooseFile_Cancel() {
        String type = "png";
        String description = "PNG Image";

        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(new JFileChooser());

        File result = hook.chooseFile(type, description, null);

        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verify(controller, never()).getMap();
        assertNull(result);
    }

    @Test
    public void testChooseFile_Overwrite() {
        String type = "png";
        String description = "PNG Image";
        String overwriteFileName = "test.png";
        File mapFile = new File("src/test/resources/map.mm");
        File resultFile = new File("src/test/resources/test_overwritten.png");

        when(controller.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(new JFileChooser());
        when(controller.getMap()).thenReturn(mapFile);
        when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);

        File result = hook.chooseFile(type, description, null);

        assertEquals(resultFile, result);
        verify(controller, times(1)).getText("file_already_exists");
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verify(controller, times(1)).getMap();
        verify(controller, times(1)).getText("file_already_exists");
        verify(controller, times(1)).showConfirmDialog(any(Component.class), anyString(), anyString(), 0);
    }

    @Test
    public void testChooseFile_CancelOverwrite() {
        String type = "png";
        String description = "PNG Image";
        String overwriteFileName = "test.png";
        File mapFile = new File("src/test/resources/map.mm");
        File resultFile = new File("src/test/resources/test_overwritten.png");

        when(controller.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(new JFileChooser());
        when(controller.getMap()).thenReturn(mapFile);
        when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.NO_OPTION);

        File result = hook.chooseFile(type, description, null);

        assertNull(result);
        verify(controller, times(1)).getText("file_already_exists");
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verify(controller, times(1)).getMap();
        verify(controller, times(1)).getText("file_already_exists");
        verify(controller, times(1)).showConfirmDialog(any(Component.class), anyString(), anyString(), 0);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%resourceKey%";

        String result = hook.getTranslatableResourceString(resourceName);

        assertEquals("resourceKey", result);
        verifyNoMoreInteractions(controller);
    }

    @Test
    public void testGetTranslatableResourceString_NullResourceName() {
        String resourceName = null;

        String result = hook.getTranslatableResourceString(resourceName);

        assertNull(result);
        verifyNoMoreInteractions(controller);
    }

    @Test
    public void testCreateBufferedImage() throws IOException {
        when(controller.getView()).thenReturn(mock(MapView.class));
        BufferedImage expectedImage = ImageIO.read(new File("src/test/resources/test_image.png"));

        BufferedImage result = hook.createBufferedImage();

        assertEquals(expectedImage, result);
        verify(controller, times(1)).getView();
    }

    @Test
    public void testCreateBufferedImage_NullView() {
        when(controller.getView()).thenReturn(null);

        BufferedImage result = hook.createBufferedImage();

        assertNull(result);
        verify(controller, times(1)).getView();
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "freemind/extensions/ExportHook/";
        String fileName = "test.txt";
        String destinationDirectory = "src/test/resources/";

        hook.copyFromResource(prefix, fileName, destinationDirectory);

        File expectedFile = new File(destinationDirectory + "/test.txt");
        assertTrue(expectedFile.exists());
        verify(hook, times(1)).logger;
        verifyNoMoreInteractions(hook);
    }

    @Test
    public void testCopyFromFile() {
        String dir = "src/test/resources/";
        String fileName = "test.txt";
        String destinationDirectory = "src/test/resources/";

        hook.copyFromFile(dir, fileName, destinationDirectory);

        File expectedFile = new File(destinationDirectory + "/test.txt");
        assertTrue(expectedFile.exists());
        verify(hook, times(1)).logger;
        verifyNoMoreInteractions(hook);
    }

    @Test
    public void testCopyFromFile_NullResource() {
        String dir = "src/test/resources/";
        String fileName = "test.txt";
        String destinationDirectory = "src/test/resources/";

        hook.copyFromFile(dir, null, destinationDirectory);

        verifyNoInteractions(hook);
    }
}