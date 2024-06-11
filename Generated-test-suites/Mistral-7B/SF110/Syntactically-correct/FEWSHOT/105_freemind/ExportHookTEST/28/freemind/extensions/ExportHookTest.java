package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.ModeController;
import freemind.controller.FrameController;
import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileChooserDialog;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private ModeController mockController;
    private FrameController mockFrameController;
    private MapView mockMapView;

    @Before
    public void setup() {
        mockController = mock(ModeController.class);
        mockFrameController = mock(FrameController.class);
        mockMapView = mock(MapView.class);
        when(mockController.getView()).thenReturn(mockMapView);
        when(mockController.getText(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockController.getFrame()).thenReturn(mockFrameController);
        when(mockFrameController.getContentPane()).thenReturn(mock(Container.class));
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "export description";
        String nameExtension = null;

        File chosenFile = new File("test.png");
        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileChooserDialog.class));
        when(mock(FileChooserDialog.class).showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);

        File result = new ExportHook().chooseFile(type, description, nameExtension, mockController);

        assertEquals(chosenFile, result);

        verify(mockController, times(1)).getFileChooser(argThat(isInstanceOf(ImageFilter.class)));
        verify(mockController, times(1)).getText("file_already_exists");
    }

    @Test
    public void testChooseFile_FileExists() {
        String type = "png";
        String description = "export description";
        String nameExtension = "test";

        File chosenFile = new File("test.png");
        File existingFile = new File("test_existing.png");

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileChooserDialog.class));
        when(mock(FileChooserDialog.class).showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
        when(mock(FileChooserDialog.class).getSelectedFile()).thenReturn(chosenFile);
        when(mockController.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
        when(JOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);

        File result = new ExportHook().chooseFile(type, description, nameExtension, mockController);

        assertEquals(chosenFile, result);

        verify(mockController, times(1)).getText("file_already_exists");
        verify(mockController, times(1)).showConfirmDialog(any(Container.class), anyString(), anyString(), 0);
    }

    @Test
    public void testChooseFile_Cancel() {
        String type = "png";
        String description = "export description";
        String nameExtension = null;

        when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileChooserDialog.class));
        when(mock(FileChooserDialog.class).showSaveDialog(any(Container.class))).thenReturn(JOptionPane.CANCEL_OPTION);

        File result = new ExportHook().chooseFile(type, description, nameExtension, mockController);

        assertNull(result);

        verify(mockController, times(1)).getFileChooser(argThat(isInstanceOf(ImageFilter.class)));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%message";

        String result = new ExportHook().getTranslatableResourceString(resourceName);

        assertEquals("%message", result);

        verify(mockController, times(1)).getText("%message");
    }

    @Test
    public void testCreateBufferedImage() {
        when(mockMapView.getInnerBounds()).thenReturn(new Rectangle(10, 10, 100, 100));

        BufferedImage result = new ExportHook().createBufferedImage();

        assertNotNull(result);

        verify(mockMapView, times(1)).preparePrinting();
        verify(mockMapView, times(1)).getWidth();
        verify(mockMapView, times(1)).getHeight();
        verify(mockMapView, times(1)).createImage(100, 100);
        verify(mockMapView, times(1)).endPrinting();
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "prefix";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        URL resource = mock(URL.class);
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);

        when(mockController.getResource(prefix + fileName)).thenReturn(resource);
        when(resource.openStream()).thenReturn(inputStream);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);

        new ExportHook().copyFromResource(prefix, fileName, destinationDirectory);

        verify(mockController, times(1)).getResource(prefix + fileName);
        verify(mockController, times(1)).getLogger();
        verify(resource, times(1)).openStream();
        verify(outputStream, times(1)).write(anyByteArray());
        verify(outputStream, times(1)).close();
    }

    @Test
    public void testCopyFromFile() {
        String dir = "test";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        File resource = mock(File.class);
        InputStream inputStream = mock(InputStream.class);
        OutputStream outputStream = mock(OutputStream.class);

        when(new File(dir, fileName)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.isFile()).thenReturn(true);
        when(new FileInputStream(resource)).thenReturn(inputStream);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);

        new ExportHook().copyFromFile(dir, fileName, destinationDirectory);

        verify(mockController, times(1)).getLogger();
        verify(resource, times(1)).exists();
        verify(resource, times(1)).isFile();
        verify(resource, times(1)).getAbsolutePath();
        verify(new File(destinationDirectory), times(1)).mkdirs();
        verify(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
        verify(inputStream, times(1)).readAllBytes();
        verify(outputStream, times(1)).write(anyByteArray());
        verify(outputStream, times(1)).close();
    }
}