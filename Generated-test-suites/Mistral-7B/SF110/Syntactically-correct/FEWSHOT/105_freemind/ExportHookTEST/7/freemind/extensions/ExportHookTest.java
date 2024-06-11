package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final String type = "png";
        final String description = "Image files";
        final String nameExtension = "image";
        final ModeController controller = mock(ModeController.class);
        final MapView view = mock(MapView.class);
        final ExportHook exportHook = new ExportHook();
        exportHook.setController(controller);
        exportHook.setView(view);

        final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        verify(controller).getFileChooser(eq(createImageFilter(type, description)));
        assertEquals(null, chosenFile);

        final Container component = mock(Container.class);
        final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(controller.getFileChooser(eq(createImageFilter(type, description)))).thenReturn(chooser);
        final File mmFile = mock(File.class);
        when(controller.getMap().getFile()).thenReturn(mmFile);
        final String proposedName = "map.image.png";
        when(mmFile.getAbsolutePath()).thenReturn("map.mindmap");
        when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);

        final File chosenFile2 = exportHook.chooseFile(type, description, nameExtension);
        assertEquals(new File("map.image.png"), chosenFile2);
    }

    @Test
    public void testCreateBufferedImage() {
        final ExportHook exportHook = new ExportHook();
        exportHook.setController(mock(ModeController.class));
        exportHook.setView(mock(MapView.class));

        final BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        final String resourceName = "%s.help";
        final String returnValue = exportValueOf("%s.help");
        assertEquals(returnValue, exportHook.getTranslatableResourceString(resourceName));
    }

    @Test
    public void testCopyFromResource() {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "file.txt";
        final String destinationDirectory = "dest";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        verify(exportHook.getLogger()).finest("searching for prefixfile.txt");
        verify(exportHook.getResource(eq(prefix + fileName))).isNotNull();
    }

    @Test(expected = NullPointerException.class)
    public void testCopyFromResourceNullResource() {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "file.txt";
        final String destinationDirectory = "dest";

        when(exportHook.getResource(eq(prefix + fileName))).thenReturn(null);
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }

    private FileFilter createImageFilter(final String type, final String description) {
        return new ExportHook.ImageFilter(type, description);
    }

    private Object exportValueOf(final String format) {
        return MessageFormat.format(format, "value");
    }
}