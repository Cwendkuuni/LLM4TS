package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import javax.swing.FileChooserBuilder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final String type = "jpg";
        final String description = "JPG files (*.jpg)";
        final String nameExtension = "test";
        final ModeController controllerMock = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook() {
            @Override
            protected File chooseFile(final String type, final String description, final String nameExtension) {
                return exportHook.chooseImageFile(type, description, nameExtension, controllerMock);
            }
        };

        final FileFilter filter = new ExportHook.ImageFilter("jpg", "JPG files (*.jpg)");
        final FreeMindFileDialog chooserMock = mock(FreeMindFileDialog.class);
        when(controllerMock.getFileChooser(filter)).thenReturn(chooserMock);

        final MapView mapViewMock = mock(MapView.class);
        final File mmFile = new File("test.mm");
        when(controllerMock.getMap()).thenReturn(mapViewMock);
        when(mapViewMock.getFile()).thenReturn(mmFile);

        final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertEquals("Expected chosenFile to not be null", chosenFile, exportHook.chooseImageFile(type, description, nameExtension, controllerMock));

        verify(controllerMock, times(1)).getFileChooser(filter);
        verify(chooserMock, times(1)).showSaveDialog(any(Container.class));
        verify(controllerMock, times(1)).getText("file_already_exists");
        verify(chooserMock, times(1)).setSelectedFile(mmFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        final String resourceName = "%resource.test";
        final String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals("Expected returnValue to equal \"resource.test\"", "resource.test", returnValue);
    }

    @Test
    public void testCreateBufferedImage() throws Exception {
        final ExportHook exportHook = new ExportHook();
        final BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull("Expected bufferedImage to not be null", bufferedImage);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "test.txt";
        final String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        assertTrue("Expected file to be copied", new File(destinationDirectory + "/" + fileName).exists());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String dir = "source";
        final String fileName = "test.txt";
        final String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        assertTrue("Expected file to be copied", new File(destinationDirectory + "/" + fileName).exists());
    }
}