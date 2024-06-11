package freemind.extensions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileChooserDialog;
import javax.swing.filechooser.FileFilter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

    @Mock
    private ModeController controller;

    @Mock
    private MapView view;

    @Mock
    private ViewController viewController;

    @Mock
    private Logger logger;

    @Mock
    private Resources resources;

    @InjectMocks
    private ExportHook exportHook;

    @Test
    public void testChooseFile_ShouldReturnNullIfControllerIsNull() {
        assertNull(exportHook.chooseFile(null, null, null));
    }

    @Test
    public void testChooseFile_ShouldReturnChosenFile() {
        final File expectedFile = new File("expected_file.png");

        when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(mock(FileChooserDialog.class));
        when(controller.getFileChooser(Mockito.any(FileFilter.class)).getSelectedFile()).thenReturn(expectedFile);

        final File actualFile = exportHook.chooseFile("image/png", "Save as PNG", null, controller);

        assertSame(expectedFile, actualFile);
    }

    @Test
    public void testGetTranslatableResourceString_ShouldReturnTranslatedString() {
        final String resourceName = "%resource_key%";
        final String expectedTranslation = "translated_string";

        when(controller.getText(resourceName)).thenReturn(expectedTranslation);

        final String actualTranslation = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedTranslation, actualTranslation);
    }

    @Test
    public void testGetTranslatableResourceString_ShouldReturnOriginalStringIfNoTranslationAvailable() {
        final String resourceName = "original_string";
        final String expectedOriginalString = "original_string";

        final String actualOriginalString = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedOriginalString, actualOriginalString);
    }

    @Test
    public void testCreateBufferedImage_ShouldReturnBufferedImage() {
        final Dimension size = new Dimension(100, 100);
        when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, size.width, size.height));

        final BufferedImage expectedBufferedImage = ImageIO.read(getClass().getResource("/test_image.png"));
        when(view.createImage(size.width, size.height)).thenReturn(expectedBufferedImage);

        final BufferedImage actualBufferedImage = exportHook.createBufferedImage();

        assertSame(expectedBufferedImage, actualBufferedImage);
    }

    @Test
    public void testCreateBufferedImage_ShouldReturnNullIfViewIsNull() {
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource_ShouldCopyResourceToDestinationDirectory() throws Exception {
        final String prefix = "prefix";
        final String fileName = "test_image.png";
        final String destinationDirectory = "/destination";

        final URL resource = new URL("resource://test/test_image.png");
        when(getResource(prefix + fileName)).thenReturn(resource);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        final File expectedFile = new File(destinationDirectory, fileName);
        assertTrue(expectedFile.exists());
    }

    @Test
    public void testCopyFromFile_ShouldCopyFileToDestinationDirectory() throws Exception {
        final String dir = "/test_directory";
        final String fileName = "test_image.png";
        final String destinationDirectory = "/destination";

        final File resource = new File(dir, fileName);
        when(getResource(prefix + fileName)).thenReturn(null);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        final File expectedFile = new File(destinationDirectory, fileName);
        assertTrue(expectedFile.exists());
    }

    @Test
    public void testChooseImageFile_ShouldReturnChosenFile() {
        final JFrame frame = mock(JFrame.class);
        final ModeController controller = mock(ModeController.class);
        final ImageFilter filter = mock(ImageFilter.class);

        when(controller.getFrame()).thenReturn(frame);
        when(controller.getFileChooser(filter)).thenReturn(mock(FileChooserDialog.class));

        final File expectedFile = new File("expected_file.png");
        when(controller.getMap().getFile()).thenReturn(null);
        when(controller.getText("file_already_exists")).thenReturn("File already exists.");
        when(controller.getText(Mockito.anyString())).then(invocationOnMock -> invocationOnMock.getArgument(0));
        when(controller.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);

        final String proposedName = "proposed_name";
        when(controller.getMap().getFile()).thenReturn(new File(proposedName));
        when(controller.getText(proposedName)).thenReturn("Map File");

        final File actualFile = exportHook.chooseImageFile("image/png", null, null, controller);

        assertSame(expectedFile, actualFile);
    }

    @Test
    public void testChooseImageFile_ShouldReturnNullIfUserCancelsDialog() {
        final JFrame frame = mock(JFrame.class);
        final ModeController controller = mock(ModeController.class);
        final ImageFilter filter = mock(ImageFilter.class);

        when(controller.getFrame()).thenReturn(frame);
        when(controller.getFileChooser(filter)).thenReturn(mock(FileChooserDialog.class));

        final String proposedName = "proposed_name";
        when(controller.getMap().getFile()).thenReturn(new File(proposedName));
        when(controller.getText(proposedName)).thenReturn("Map File");

        final File actualFile = exportHook.chooseImageFile("image/png", null, null, controller);

        assertNull(actualFile);
    }

    @Test
    public void testChooseImageFile_ShouldReturnNullIfUserSelectsDirectory() {
        final JFrame frame = mock(JFrame.class);
        final ModeController controller = mock(ModeController.class);
        final ImageFilter filter = mock(ImageFilter.class);

        when(controller.getFrame()).thenReturn(frame);
        when(controller.getFileChooser(filter)).thenReturn(mock(FileChooserDialog.class));

        final File actualFile = exportHook.chooseImageFile("image/png", null, null, controller);

        assertNull(actualFile);
    }

    @Test
    public void testChooseImageFile_ShouldReturnNullIfProposedFilenameIsEmpty() {
        final JFrame frame = mock(JFrame.class);
        final ModeController controller = mock(ModeController.class);
        final ImageFilter filter = mock(ImageFilter.class);

        when(controller.getFrame()).thenReturn(frame);
        when(controller.getFileChooser(filter)).thenReturn(mock(FileChooserDialog.class));

        final File actualFile = exportHook.chooseImageFile("image/png", null, null, controller);

        assertNull(actualFile);
    }

    @Test
    public void testGetResourceString_ShouldReturnOriginalStringIfNoTranslationAvailable() {
        final String resourceName = "original_string";

        final String actualOriginalString = exportHook.getResourceString(resourceName);

        assertEquals(resourceName, actualOriginalString);
    }

    @Test
    public void testGetResourceString_ShouldReturnTranslatedStringIfAvailable() {
        final String resourceName = "%resource_key%";
        final String expectedTranslation = "translated_string";

        when(controller.getText(resourceName)).thenReturn(expectedTranslation);

        final String actualTranslation = exportHook.getResourceString(resourceName);

        assertEquals(expectedTranslation, actualTranslation);
    }

    private URL getResource(String path) {
        return getClass().getResource(path);
    }
}