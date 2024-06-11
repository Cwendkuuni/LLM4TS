import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.io.*;

public class ExportHookTest {
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = mock(ModeController.class);
        when(controller.getFrame()).thenReturn(new JFrame());
        when(controller.getText(anyString())).thenReturn("test text");
        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());
        when(controller.getMap()).thenReturn(new MapView());

        File result = exportHook.chooseFile("png", "test description", null, controller);
        File expected = new File("test fileName.png");
        assertEquals(expected, result);
    }
}