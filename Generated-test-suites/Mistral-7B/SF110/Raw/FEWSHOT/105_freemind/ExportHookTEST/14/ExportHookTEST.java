package freemind.extensions;

import static org.junit.Assert.*;
import static freemind.view.mindmapview.Tools.getExtension;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook hook = new ExportHook();
        ModeController controller = Mockito.mock(ModeController.class);
        File chosenFile = hook.chooseFile("image/png", "Save Image as PNG", "png");
        assertNotNull(chosenFile);

        File chooser = Mockito.mock(FreeMindFileDialog.class);
        when(hook.getController().getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
        when(controller.getText("file_already_exists")).thenReturn("File already exists.");
        when(controller.getMap()).thenReturn(new File("testMap.mmf"));
        when(chooser.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("testImage.png"));
        File file = hook.chooseFile("image/png", "Save Image as PNG", "png", controller);
        assertEquals(new File("testImage.png"), file);

        // Test file already exists
        when(controller.getText("file_already_exists")).thenAnswer(i -> i.getCaller().getClass().getSimpleName() + ": File already exists.");
        when(chooser.showSaveDialog(any(JFrame.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(new File("testImage.png"));
        File overwrittenFile = hook.chooseFile("image/png", "Save Image as PNG", "png", controller);
        assertNull(overwrittenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook hook = new ExportHook();
        String resourceName = "%Hello%";
        String translatableString = hook.getController().getText("Hello");
        String result = hook.getTranslatableResourceString(resourceName);
        assertEquals(translatableString, result);

        resourceName = "Hello";
        result = hook.getTranslatableResourceString(resourceName);
        assertEquals(resourceName, result);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook hook = new ExportHook();
        MapView view = Mockito.mock(MapView.class);
        when(hook.getController().getView()).thenReturn(view);
        BufferedImage bufferedImage = hook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        ExportHook hook = new ExportHook();
        String prefix = "images/";
        String fileName = "test.png";
        String destinationDirectory = "testOutput";
        hook.copyFromResource(prefix, fileName, destinationDirectory);
        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        ExportHook hook = new ExportHook();
        String dir = "testInput";
        String fileName = "test.png";
        String destinationDirectory = "testOutput";
        hook.copyFromFile(dir, fileName, destinationDirectory);
        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

}