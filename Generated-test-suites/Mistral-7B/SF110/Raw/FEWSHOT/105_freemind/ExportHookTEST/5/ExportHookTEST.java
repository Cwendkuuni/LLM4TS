package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(mock(JFileChooser.class));
        when(controller.getText(anyString())).thenAnswer(invocation -> invocation.getArgument(0).replaceFirst("%", ""));

        final File chosenFile = exportHook.chooseFile("png", "Save as PNG", "png");
        assertNotNull(chosenFile);

        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(fileChooser);
        when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);

        final File proposedFile = new File("path/to/my/map.mmx");
        when(controller.getMap()).thenReturn(mock(MindMapFile.class));
        when(controller.getMap().getFile()).thenReturn(proposedFile);
        when(fileChooser.getSelectedFile()).thenReturn(proposedFile);

        final File chosenFile2 = exportHook.chooseFile("png", "Save as PNG", "png");
        assertEquals(proposedFile, chosenFile2);

        when(fileChooser.getSelectedFile()).thenReturn(null);
        final File chosenFile3 = exportHook.chooseFile("png", "Save as PNG", "png");
        assertNull(chosenFile3);
    }

    @Test
    public void testCreateBufferedImage() {
        final ModeController controller = mock(ModeController.class);
        final MapView view = mock(MapView.class);
        when(controller.getView()).thenReturn(view);

        final ExportHook exportHook = new ExportHook();
        exportHook.setController(controller);

        when(view.getInnerBounds()).thenReturn(new Rectangle(10, 20, 30, 40));
        when(view.getWidth()).thenReturn(50);
        when(view.getHeight()).thenReturn(60);

        final BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        assertEquals("file_already_exists", exportHook.getTranslatableResourceString("%file_already_exists%"));
        assertEquals("file_already_exists", exportHook.getTranslatableResourceString("%file_already_exists%"));
        assertEquals("file_already_exists", exportHook.getTranslatableResourceString("%file_already_exists%"));
    }

    @Test
    public void testCopyFromResource() throws IOException {
        final ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));

        final URL resource = new URL("path/to/resource.txt");
        when(exportHook.getResource("prefix/fileName.txt")).thenReturn(resource);

        final File destinationDirectory = new File("path/to/destination");
        exportHook.copyFromResource("prefix", "fileName.txt", destinationDirectory.getAbsolutePath());

        final File file = new File(destinationDirectory, "fileName.txt");
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        final ExportHook exportHook = new ExportHook();
        exportHook.setLogger(mock(Logger.class));

        final String dir = "path/to/source";
        final String fileName = "sourceFile.txt";
        final String destinationDirectory = "path/to/destination";

        final File sourceFile = new File(dir, fileName);
        when(exportHook.getResource("prefix/fileName.txt")).thenReturn(null);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        final File destinationFile = new File(destinationDirectory, fileName);
        assertTrue(destinationFile.exists());
    }
}