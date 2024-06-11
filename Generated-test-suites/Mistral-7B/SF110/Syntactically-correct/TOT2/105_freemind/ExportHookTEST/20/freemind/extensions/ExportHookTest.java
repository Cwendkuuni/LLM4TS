package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.*;
import org.junit.Before;
import org.mockito.Mockito;
import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController controller;

    @Before
    public void setUp() {
        controller = new ModeController();
        exportHook = new ExportHook();
        exportHook.setController(controller);
    }

    @Test
    public void testChooseFile() {
        File file = exportHook.chooseFile("png", "Save as PNG", null);
        assertNotNull(file);
    }

    @Test
    public void testChooseFile_UnsupportedType() {
        String type = "xyz";
        File file = exportHook.chooseFile(type, "Save as XYZ", null);
        assertNull(file);
    }

    @Test
    public void testChooseFile_UserCancelled() {
        String type = "png";
        File chooser = Mockito.mock(JFileChooser.class);
        when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(chooser);
        when(chooser.showSaveDialog(Mockito.any(JFrame.class))).thenReturn(JFileChooser.CANCEL_OPTION);
        File file = exportHook.chooseFile(type, "Save as PNG", null);
        assertNull(file);
    }

    @Test
    public void testChooseFile_Directory() {
        String type = "png";
        File directory = new File("test_directory");
        when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
        when(controller.getMap().getFile()).thenReturn(directory);
        when(controller.getText("file_already_exists")).thenReturn("File already exists");
        File file = exportHook.chooseFile(type, "Save as PNG", null);
        assertNull(file);
    }

    @Test
    public void testChooseFile_InvalidFileName() {
        String type = "png";
        String fileName = "test_file%";
        File file = exportHook.chooseFile(type, "Save as PNG", fileName);
        assertNotNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%TEST_RESOURCE_STRING%";
        String expected = "Test Resource String";
        String result = exportHook.getController().getText(resourceName);
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBufferedImage_NullView() {
        ExportHook exportHook = new ExportHook();
        exportHook.createBufferedImage();
    }
}