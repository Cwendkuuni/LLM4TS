import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.ModeController;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class ExportHookChooseFileTest {

    @ org.junit.Before
    public void setup() {
        this.controller = mock(ModeController.class);
    }

    @Test
    public void testChooseFileSuccess() {
        when(this.controller.getFileChooser(any(FileFilter.class)))
                .thenReturn(mock(JFileChooser.class));
        when(this.controller.getText(anyString()))
                .thenReturn("Test File Description");
        File chosenFile = new ExportHook().chooseFile("image/png", "Test File Description", null, this.controller);
        assertEquals("expectedFile.png", chosenFile.getAbsolutePath());
    }
}