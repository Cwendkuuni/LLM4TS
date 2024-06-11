package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.main.Controller;
import freemind.main.MapController;
import freemind.modes.MapControllerImpl;
import freemind.view.mindmapview.MapView;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileDialog;
import java.awt.image.BufferedImage;
import java.awt.FileDialog;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExportHookTest {

    @SuppressWarnings("unchecked")
    private static MapControllerImpl createMockController() {
        return mock(MapControllerImpl.class);
    }

    @SuppressWarnings("unchecked")
    private static Controller createMockControllerWithView() {
        final MapControllerImpl mapController = createMockController();
        when(mapController.getFrame()).thenReturn(mock(JFrame.class));
        when(mapController.getView()).thenReturn(mock(MapView.class));
        return mapController;
    }

    @Test
    public void testChooseFile() {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockControllerWithView();
        final File file = exportHook.chooseFile("image/png", "Save as PNG", "png");
        verify(controller).getFileChooser(any(FileFilter.class));
        FileDialog chooser = verify(controller).getFileChooser(argThat(isA(FileDialog.class)));
        assertEquals("Save as PNG (*.png)", chooser.getFileFilter().getDescription());
        assertNull(file);
    }

    @Test
    public void testChooseFileWithProposedName() {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockControllerWithView();
        final File mapFile = new File("map.mm");
        final File file = exportHook.chooseFile("image/png", "Save as PNG", "png", controller, mapFile);
        final String proposedName = mapFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + ".png";
        verify(controller).getFileChooser(any(FileFilter.class));
        FileDialog chooser = verify(controller).getFileChooser(argThat(isA(FileDialog.class)));
        assertEquals("Save as PNG (*.png)", chooser.getFileFilter().getDescription());
        assertEquals(new File(proposedName), chooser.getSelectedFile());
        assertTrue(file != null);
        assertTrue(file.getAbsolutePath().endsWith(".png"));
        verify(controller).getText("file_already_exists");
        assertEquals(controller.getText("file_already_exists").substring(1), JOptionPane.showConfirmDialog(verify(controller.getFrame()).getContentPane(), eq(controller.getText("file_already_exists")), eq(controller.getText("file_already_exists")), 0));
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockController();
        final String resourceName = "%resource_name%";
        assertEquals("resource_name", exportHook.getTranslatableResourceString(resourceName));
        verify(controller).getText(eq("resource_name"));
    }

    @Test
    public void testGetTranslatableResourceStringWithNullReturnValue() {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockController();
        final String resourceName = "%resource_name%";
        when(controller.getResourceString(resourceName)).thenReturn(null);
        assertEquals(resourceName, exportHook.getTranslatableResourceString(resourceName));
        verifyZeroInteractions(controller);
    }

    @Test
    public void testCreateBufferedImage() throws Exception {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockController();
        final MapView view = mock(MapView.class);
        when(controller.getView()).thenReturn(view);
        BufferedImage image = exportHook.createBufferedImage();
        verify(view).preparePrinting();
        verify(view).createImage(eq(controller.getWidth()), eq(controller.getHeight()));
        verify(view).endPrinting();
        assertNotNull(image);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBufferedImageWithNullView() {
        final ExportHook exportHook = new ExportHook();
        final Controller controller = createMockController();
        when(controller.getView()).thenReturn(null);
        exportHook.createBufferedImage();
    }

    @Test
    public void testCopyFromResource() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "fileName";
        final String destinationDirectory = "destinationDirectory";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        verify(exportHook.getResource(), times(1)).openStream();
        verify(new FileOutputStream(destinationDirectory + "/" + fileName)).write(anyByteArray());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String dir = "dir";
        final String fileName = "fileName";
        final String destinationDirectory = "destinationDirectory";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        verify(new File(dir, fileName)).exists();
        verify(new FileOutputStream(destinationDirectory + "/" + fileName)).write(anyByteArray());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCopyFromFileWithNullFile() throws Exception {
        final ExportHook exportHook = new ExportHook();
        final String dir = "dir";
        final String fileName = "fileName";
        final String destinationDirectory = "destinationDirectory";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        verifyZeroInteractions(new File(dir, fileName));
        verifyZeroInteractions(new FileOutputStream(destinationDirectory + "/" + fileName));
    }
}