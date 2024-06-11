package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.Controller;
import freemind.main.MainTools;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private Controller mockController;
    private MapView mockView;

    @Before
    public void setUp() {
        mockController = mock(Controller.class);
        mockView = mock(MapView.class);
    }

    @Test
    public void testChooseFile_ValidType() {
        final String type = "png";
        final String description = "PNG Image";
        final FileFilter filter = new ExportHook.ImageFilter(type, description);
        final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);

        when(mockController.getFrame()).thenReturn(mock(JFrame.class));
        when(mockController.getFileChooser(eq(filter))).thenReturn(chooser);
        when(mockController.getMap()).thenReturn(mock(File.class));

        final ExportHook exportHook = new ExportHook();
        final File chosenFile = exportHook.chooseFile(type, description, null, mockController);

        verify(mockController).getFileChooser(filter);
        verifyNoMoreInteractions(mockController);

        assertEquals("expected chosenFile to not be null", chosenFile, mockController.getMap());
    }

    @Test
    public void testChooseFile_NullController() {
        final String type = "png";
        final String description = "PNG Image";
        final FileFilter filter = new ExportHook.ImageFilter(type, description);

        final ExportHook exportHook = new ExportHook();
        final File chosenFile = exportHook.chooseFile(type, description, null, null);

        assertNull("expected chosenFile to be null when controller is null", chosenFile);
    }

    @Test
    public void testCreateBufferedImage() {
        final ExportHook exportHook = new ExportHook();
        final MapView view = mock(MapView.class);
        doReturn(view).when(exportHook).getController().getView();

        final BufferedImage bufferedImage = exportHook.createBufferedImage();

        verify(view).preparePrinting();
        verify(view).print(any(Graphics.class));
        verify(view).endPrinting();

        assertNotNull("expected bufferedImage to not be null", bufferedImage);
    }

    @Test
    public void testCreateBufferedImage_NullView() {
        final ExportHook exportHook = new ExportHook();

        final BufferedImage bufferedImage = exportHook.createBufferedImage();

        assertNull("expected bufferedImage to be null when view is null", bufferedImage);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "fileName.txt";
        final String destinationDirectory = "destination";

        exportHook.logger = mock(Logger.class);

        final URL resource = new URL("resource:///prefix/fileName.txt");
        when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
        when(resource.openStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook.logger).finest("searching for prefixfileName");
        verify(exportHook.logger, never()).severe("Cannot find resource: ");
    }

    @Test
    public void testCopyFromResource_NullResource() {
        final ExportHook exportHook = new ExportHook();
        final String prefix = "prefix";
        final String fileName = "fileName.txt";
        final String destinationDirectory = "destination";

        exportHook.logger = mock(Logger.class);

        when(exportHook.getResource(prefix + fileName)).thenReturn(null);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook.logger).finest("searching for prefixfileName");
        verify(exportHook.logger).severe("Cannot find resource: ");
    }

    @Test
    public void testCopyFromFile() throws IOException {
        final ExportHook exportHook = new ExportHook();
        final String dir = "dir";
        final String fileName = "fileName.txt";
        final String destinationDirectory = "destination";

        exportHook.logger = mock(Logger.class);

        final File resource = new File(dir, fileName);
        when(resource.exists()).thenReturn(true);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook.logger).finest("searching for dirfileName");
        verifyNoMoreInteractions(exportHook.logger);
    }

    @Test
    public void testCopyFromFile_NullResource() {
        final ExportHook exportHook = new ExportHook();
        final String dir = "dir";
        final String fileName = "fileName.txt";
        final String destinationDirectory = "destination";

        exportHook.logger = mock(Logger.class);

        final File resource = null;

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook.logger).finest("searching for dirfileName");
        verify(exportHook.logger).severe("File not found or could not be copied. Was earching for dirfileName and should go to destination");
    }

}