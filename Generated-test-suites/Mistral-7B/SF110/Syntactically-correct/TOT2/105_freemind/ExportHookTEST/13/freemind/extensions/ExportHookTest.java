import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportHookTest {

    @Test
    public void testChooseFile_ValidInput_ReturnsFile() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        final File chosenFile = new File("path/to/file.png");

        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
        when(controller.getText(anyString())).thenReturn("File name");
        when(controller.getMap()).thenReturn(mock(MindMap.class));

        final File result = exportHook.chooseFile("image/png", "Description", null, controller);

        assertNotNull(result);
        assertNotNull(result.getAbsolutePath());
        assertEquals("path/to/file.png", result.getAbsolutePath());
    }

    @Test
    public void testChooseFile_InvalidInput_ReturnsNull() {
        final ModeController controller = mock(ModeController.class);

        final ExportHook exportHook = new ExportHook();

        final File result = exportHook.chooseFile("image/png", "Description", null, controller);

        assertNull(result);
    }
}