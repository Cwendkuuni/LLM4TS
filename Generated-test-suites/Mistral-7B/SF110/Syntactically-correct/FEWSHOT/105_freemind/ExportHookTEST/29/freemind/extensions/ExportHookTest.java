package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        final String type = "png";
        final String description = "PNG Image";
        final String nameExtension = "png";

        when(controller.getText(eq("file_already_exists"))).thenReturn("File already exists");
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));
        when(controller.getMap()).thenReturn(new MapView() {
            @Override
            public File getFile() {
                return new File("map.mm");
            }
        });

        final File chosenFile = exportHook.chooseFile(type, description, nameExtension, controller);

        verify(controller, times(1)).getText(eq("file_already_exists"));
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(controller);

        assertEquals("map.png", chosenFile.getName());
        assertEquals("png", Tools.getExtension(chosenFile.getName()));
        assertEquals("PNG Image", chooser.getDescription());
    }

    @Test
    public void testChooseFile_noFileChosen() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        final String type = "png";
        final String description = "PNG Image";
        final String nameExtension = "png";

        when(controller.getText(eq("file_already_exists"))).thenReturn("File already exists");
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));

        final File chosenFile = exportHook.chooseFile(type, description, nameExtension, controller);

        verify(controller, times(1)).getText(eq("file_already_exists"));
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(controller);

        assertNull(chosenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        final String resourceName = "%file_already_exists";

        final String returnValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("File already exists", returnValue);
    }

    @Test
    public void testGetTranslatableResourceString_noTranslation() {
        final ExportHook exportHook = new ExportHook();
        final String resourceName = "file_already_exists";

        final String returnValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("file_already_exists", returnValue);
    }

    @Test
    public void testCreateBufferedImage() {
        final ExportHook exportHook = new ExportHook();
        final MapView view = mock(MapView.class);

        when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

        final BufferedImage bufferedImage = exportHook.createBufferedImage();

        verify(view, times(1)).preparePrinting();
        verify(view, times(1)).getWidth();
        verify(view, times(1)).getHeight();
        verify(view, times(1)).createImage(anyInt(), anyInt(), anyInt());
        verify(view, times(1)).endPrinting();

        assertNotNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource() {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "/resources/images/";
        final String fileName = "example.png";
        final String destinationDirectory = "/destination/";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook, times(1)).getResource(eq(prefix + fileName));
        verify(exportHook, times(1)).getLogger().finest(containsString("searching for " + prefix + fileName));
        verify(exportHook, times(1)).getLogger().severe(containsString("Cannot find resource: " + prefix + fileName));
    }

    @Test
    public void testCopyFromFile() {
        final ExportHook exportHook = new ExportHook();
        final String dir = "/source/";
        final String fileName = "example.png";
        final String destinationDirectory = "/destination/";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook, times(1)).getLogger().finest(containsString("searching for " + dir + fileName));
        verify(exportHook, times(1)).getLogger().severe(containsString("Cannot find resource: " + dir + fileName));
    }
}