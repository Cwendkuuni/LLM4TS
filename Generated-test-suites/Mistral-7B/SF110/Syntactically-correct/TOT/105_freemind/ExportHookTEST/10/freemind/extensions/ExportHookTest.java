import static org.junit.Assert.*;
import static freemind.extensions.Tools.safeEqualsIgnoreCase;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.FreeMindFileDialog;
import javax.swing.JFileChooser;
import java.awt.FileDialog;
import java.io.File;

public class ExportHookTest {

    @org.junit.Before
    public void setUp() {
        // Initialize mocks or setup environment as needed
    }

    @org.junit.Test
    public void testChooseFile() throws Exception {
        final String type = "png";
        final String description = "Export as PNG";
        final String nameExtension = null;
        final ModeController controller = mock(ModeController.class);
        final Container component = mock(Container.class);
        final ImageFilter filter = new ExportHook.ImageFilter("png", null);
        final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        final File mmFile = new File("path/to/map.mm"); // Replace with a valid File object

        when(controller.getFrame()).thenReturn(new JFrame() {
            @Override
            public Container getContentPane() {
                return component;
            }
        });

        when(controller.getFileChooser(filter)).thenReturn(chooser);
        when(controller.getMap()).thenReturn(mmFile);

        final File chosenFile = new ExportHook().chooseFile(type, description, nameExtension, controller);

        // Test typical use case
        assertNotNull(chosenFile);
        assertEquals(type, Tools.getExtension(chosenFile.getName()));

        // Test edge case: File chooser doesn't return a file
        when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(null);
        chosenFile = new ExportHook().chooseFile(type, description, nameExtension, controller);
        assertNull(chosenFile);

        // Test error case: User cancels file selection
        when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.CANCEL_OPTION);
        chosenFile = new ExportHook().chooseFile(type, description, nameExtension, controller);
        assertNull(chosenFile);

        // Test edge case: Selected file has an incorrect extension
        when(chooser.getSelectedFile()).thenReturn(new File("path/to/image.jpg"));
        chosenFile = new ExportHook().chooseFile(type, description, nameExtension, controller);
        assertNull(chosenFile);

        // Test edge case: File already exists
        when(controller.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
        when(JOptionPane.showConfirmDialog(component, anyString(), anyString(), 0)).thenReturn(JOptionPane.NO_OPTION);
        chosenFile = new ExportHook().chooseFile(type, description, nameExtension, controller);
        assertNull(chosenFile);
    }
}