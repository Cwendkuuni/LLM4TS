package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Image;
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
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import freemind.main.MainFrame;

public class ExportHookTest {

    @Mock(answer = Answers.CALLS_REAL_METHOD)
    private ModeController mockController;

    @Mock
    private MapView mockMapView;

    @Mock
    private MainFrame mockMainFrame;

    private ExportHook exportHook;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.exportHook = new ExportHook();
        this.exportHook.setController(this.mockController);
        when(this.mockController.getFrame()).thenReturn(this.mockMainFrame);
        when(this.mockController.getView()).thenReturn(this.mockMapView);
    }

    @Test
    public void testChooseFile_ValidParameters_ReturnsFile() {
        final String type = "png";
        final String description = "PNG images";
        final File expectedFile = new File("path/to/file.png");

        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(this.mockController.getFileChooser(any(FileFilter.class))).thenReturn(fileChooser);
        doReturn(expectedFile).when(fileChooser).getSelectedFile();

        final File result = this.exportHook.chooseFile(type, description, null);

        assertEquals(expectedFile, result);
    }

    @Test
    public void testChooseFile_NullController_ReturnsNull() {
        final String type = "png";
        final String description = "PNG images";

        final File result = this.exportHook.chooseFile(type, description, null);

        assertNull(result);
    }

    @Test
    public void testGetTranslatableResourceString_ValidResourceKey_ReturnsTranslatedString() {
        final String resourceKey = "%resource.key%";
        final String expectedTranslation = "translated string";

        doReturn(expectedTranslation).when(this.mockController).getText(resourceKey);

        final String result = this.exportHook.getTranslatableResourceString(resourceKey);

        assertEquals(expectedTranslation, result);
    }

    @Test
    public void testGetTranslatableResourceString_NullResourceKey_ReturnsEmptyString() {
        final String resourceKey = null;

        final String result = this.exportHook.getTranslatableResourceString(resourceKey);

        assertEquals("", result);
    }

    @Test
    public void testCreateBufferedImage_ValidView_ReturnsImage() {
        final Image expectedImage = ImageIO.read(getClass().getResource("/test_image.jpg"));
        when(this.mockMapView.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
        when(this.mockMapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

        final BufferedImage result = this.exportHook.createBufferedImage();

        assertNotNull(result);
        assertEquals(expectedImage.getWidth(null), result.getWidth());
        assertEquals(expectedImage.getHeight(null), result.getHeight());
    }

    @Test
    public void testCreateBufferedImage_NullView_ReturnsNull() {
        final BufferedImage result = this.exportHook.createBufferedImage();

        assertNull(result);
    }

    @Test
    public void testCopyFromResource_ValidParameters_CopiesFile() throws IOException {
        final String prefix = "prefix/";
        final String fileName = "test.txt";
        final String destinationDirectory = "destination/";
        final URL resource = new URL("http://test.com/test.txt");
        this.exportHook.setLogger(mock(Logger.class));

        this.exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(this.mockController, times(1)).getText("file_already_exists");
        verify(this.mockController, times(1)).showConfirmDialog(any(Component.class), anyString(), anyString(), 0);
        verify(this.mockController, times(1)).getText(prefix + fileName.substring(1));
        verify(this.mockController, times(1)).getFrame();
        verify(this.mockController, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(this.mockController);

        final InputStream inputStream = resource.openStream();
        final OutputStream outputStream = new FileOutputStream(destinationDirectory + "/" + fileName);
        Tools.copyStream(inputStream, outputStream, true);
    }

    @Test
    public void testCopyFromFile_ValidParameters_CopiesFile() throws IOException {
        final String dir = "source/";
        final String fileName = "test.txt";
        final String destinationDirectory = "destination/";

        this.exportHook.setLogger(mock(Logger.class));

        this.exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(this.mockController, times(1)).getText("file_already_exists");
        verify(this.mockController, times(1)).showConfirmDialog(any(Component.class), anyString(), anyString(), 0);
        verify(this.mockController, times(1)).getText(dir + fileName.substring(1));
        verify(this.mockController, times(1)).getFrame();
        verify(this.mockController, times(1)).getFileChooser(any(FileFilter.class));
        verifyNoMoreInteractions(this.mockController);

        final File sourceFile = new File(dir, fileName);
        final InputStream inputStream = new FileInputStream(sourceFile);
        final OutputStream outputStream = new FileOutputStream(destinationDirectory + "/" + fileName);
        Tools.copyStream(inputStream, outputStream, true);
    }

    @Test
    public void testChooseImageFile_NullType_ThrowsNullPointerException() {
        final ModeController controller = mock(ModeController.class);

        assertThrows(NullPointerException.class, () -> this.exportHook.chooseImageFile(null, "description", null, controller));
    }

    @Test
    public void testChooseImageFile_NullController_ReturnsNull() {
        final String type = "png";
        final String description = "PNG images";

        final File result = this.exportHook.chooseImageFile(type, description, null, null);

        assertNull(result);
    }

    @Test
    public void testChooseImageFile_ValidParameters_ReturnsFile() {
        final String type = "png";
        final String description = "PNG images";
        final ModeController controller = mock(ModeController.class);
        final JFileChooser fileChooser = mock(JFileChooser.class);
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(fileChooser);
        doReturn(new File("path/to/file.png")).when(fileChooser).getSelectedFile();

        final File result = this.exportHook.chooseImageFile(type, description, null, controller);

        assertEquals(new File("path/to/file.png"), result);
    }
}