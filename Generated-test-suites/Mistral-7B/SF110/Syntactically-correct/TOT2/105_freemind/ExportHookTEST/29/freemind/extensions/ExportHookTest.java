import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.ModeController;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ModeController controller = Mockito.mock(ModeController.class);
        ExportHook exportHook = new ExportHook();
        File expectedFile = new File("test.png");

        when(controller.getFileChooser(Mockito.any(FileNameExtensionFilter.class)))
                .thenReturn(Mockito.mock(JFileChooser.class));
        when(controller.getText(Mockito.anyString())).thenReturn("Test File Name");

        File actualFile = exportHook.chooseFile("image/png", "Test Description", "png", controller);

        assertEquals(expectedFile, actualFile);
        verify(controller.getFileChooser(Mockito.any(FileNameExtensionFilter.class))).setSelectedFile(expectedFile);
    }
}