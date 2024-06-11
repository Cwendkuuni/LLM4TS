import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final String type = "png";
        final String description = "Export Image";
        final String nameExtension = null;
        final ModeController controller = mock(ModeController.class);
        final Container component = mock(Container.class);
        final ImageFilter filter = new ImageFilter(type, description);
        final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        final File mmFile = new File("map.mm");

        when(controller.getFrame()).thenReturn(new JFrame());
        when(controller.getFileChooser(filter)).thenReturn(chooser);
        when(controller.getMap()).thenReturn(mmFile);

        final ExportHook exportHook = new ExportHook();
        final File chosenFile = exportHook.chooseFile(type, description, nameExtension, controller);

        assertNotNull(chosenFile);
    }
}