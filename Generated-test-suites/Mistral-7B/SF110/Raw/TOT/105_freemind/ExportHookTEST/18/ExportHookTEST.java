package freemind.extensions;

import static org.junit.Assert.*;
import static freemind.extensions.Tools.*;

import java.awt.FileDialog;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import freemind.controller.Controller;
import freemind.controller.FileChooser;
import freemind.controller.FrameController;
import freemind.controller.ModeController;
import freemind.controller.ModeControllerHookAdapter;
import freemind.controller.ResourceController;
import freemind.controller.TextController;
import freemind.controller.View;
import freemind.main.Logger;
import freemind.model.Map;

public class ExportHookTest {

    private ExportHook hook;
    private ModeController controller;
    private FrameController frameController;
    private TextController textController;
    private ResourceController resourceController;
    private Logger logger;

    @Before
    public void setup() {
        controller = Mockito.mock(ModeController.class);
        frameController = Mockito.mock(FrameController.class);
        textController = Mockito.mock(TextController.class);
        resourceController = Mockito.mock(ResourceController.class);
        logger = Mockito.mock(Logger.class);
        hook = new ExportHook() {
            {
                this.getController = () -> controller;
                this.getFrame = () -> frameController;
                this.getText = () -> textController;
                this.getResource = () -> resourceController;
                this.getLogger = () -> logger;
            }
        };
    }

    @Test
    public void testChooseFile_ValidFile_ShouldReturnFile() {
        final String type = "png";
        final String description = "Export image";
        FileChooser chooser = Mockito.mock(FileChooser.class);
        when(controller.getFileChooser(eq(new ImageFilter(type, description)))).thenReturn(chooser);
        Map map = Mockito.mock(Map.class);
        when(controller.getMap()).thenReturn(map);
        File expectedFile = new File("path/to/file.png");
        when(chooser.showSaveDialog(any(Component.class))).thenReturn(JOptionPane.OK_OPTION);
        File actualFile = hook.chooseFile(type, description, null);
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testChooseFile_UserCancels_ShouldReturnNull() {
        final String type = "png";
        final String description = "Export image";
        FileChooser chooser = Mockito.mock(FileChooser.class);
        when(controller.getFileChooser(eq(new ImageFilter(type, description)))).thenReturn(chooser);
        when(chooser.showSaveDialog(any(Component.class))).thenReturn(JOptionPane.CANCEL_OPTION);
        File actualFile = hook.chooseFile(type, description, null);
        assertNull(actualFile);
    }

    @Test
    public void testChooseFile_InvalidFileFormat_ShouldPromptUserToOverwrite() throws HeadlessException {
        final String type = "png";
        final String description = "Export image";
        FileChooser chooser = Mockito.mock(FileChooser.class);
        when(controller.getFileChooser(eq(new ImageFilter(type, description)))).thenReturn(chooser);
        Map map = Mockito.mock(Map.class);
        when(controller.getMap()).thenReturn(map);
        File expectedFile = new File("path/to/file.jpg");
        when(controller.getText("file_already_exists")).thenReturn("File {0} already exists. Overwrite?");
        when(chooser.showSaveDialog(any(Component.class))).thenReturn(JOptionPane.YES_OPTION);
        File actualFile = hook.chooseFile(type, description, null);
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testChooseFile_InvalidFileFormat_ShouldNotOverwrite_UserCancels() throws HeadlessException {
        final String type = "png";
        final String description = "Export image";
        FileChooser chooser = Mockito.mock(FileChooser.class);
        when(controller.getFileChooser(eq(new ImageFilter(type, description)))).thenReturn(chooser);
        Map map = Mockito.mock(Map.class);
        when(controller.getMap()).thenReturn(map);
        when(controller.getText("file_already_exists")).thenReturn("File {0} already exists. Overwrite?");
        when(chooser.showSaveDialog(any(Component.class))).thenReturn(JOptionPane.CANCEL_OPTION);
        File actualFile = hook.chooseFile(type, description, null);
        assertNull(actualFile);
    }

    @Test
    public void testCreateBufferedImage_ShouldReturnBufferedImage() {
        View view = Mockito.mock(View.class);
        when(controller.getView()).thenReturn(view);
        BufferedImage expectedImage = ImageIO.read(new File("path/to/image.jpg"));
        when(view.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
        BufferedImage actualImage = hook.createBufferedImage();
        assertEquals(expectedImage, actualImage);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBufferedImage_ShouldThrowExceptionIfViewIsNull() {
        BufferedImage actualImage = hook.createBufferedImage();
        fail("Expected NullPointerException");
    }

    @Test
    public void testGetTranslatableResourceString_ShouldReturnTranslatedStringIfAvailable() {
        String expectedString = "Expected translated string";
        when(textController.getText(eq("key"))).thenReturn(expectedString);
        String actualString = hook.getTranslatableResourceString("key");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetTranslatableResourceString_ShouldReturnOriginalStringIfNoTranslationAvailable() {
        String originalString = "Original string";
        String expectedString = "Expected string";
        when(textController.getText(eq("key"))).thenReturn(originalString);
        when(controller.getText(originalString)).thenReturn(expectedString);
        String actualString = hook.getTranslatableResourceString("key");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCopyFromResource_ShouldCopyFileFromResource() throws IOException {
        String prefix = "path/to/prefix";
        String fileName = "file.txt";
        String destinationDirectory = "path/to/destination";
        URL resource = new URL("resource:///path/to/resource.txt");
        InputStream in = resource.openStream();
        OutputStream out = Mockito.mock(OutputStream.class);
        hook.copyFromResource(prefix, fileName, destinationDirectory);
        SwingUtilities.invokeLater(() -> {
            try {
                hook.copyStream(in, out, true);
            } catch (IOException e) {
                fail("IOException should not be thrown");
            }
        });
        verify(out).close();
    }

    @Test
    public void testCopyFromFile_ShouldCopyFileFromFile() throws IOException {
        String dir = "path/to/source";
        String fileName = "file.txt";
        String destinationDirectory = "path/to/destination";
        File sourceFile = new File(dir, fileName);
        InputStream in = new FileInputStream(sourceFile);
        OutputStream out = Mockito.mock(OutputStream.class);
        hook.copyFromFile(dir, fileName, destinationDirectory);
        SwingUtilities.invokeLater(() -> {
            try {
                hook.copyStream(in, out, true);
            } catch (IOException e) {
                fail("IOException should not be thrown");
            }
        });
        verify(out).close();
    }
}