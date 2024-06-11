import org.junit.Test;
import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.io.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook hook = new ExportHook();
        String type = "png";
        String description = "Export image";
        String nameExtension = null;
        ModeController controller = new MockModeController(); // Create a mock controller instance for testing
        File chosenFile = hook.chooseFile(type, description, nameExtension, controller);
        assertNotNull(chosenFile);
    }
}