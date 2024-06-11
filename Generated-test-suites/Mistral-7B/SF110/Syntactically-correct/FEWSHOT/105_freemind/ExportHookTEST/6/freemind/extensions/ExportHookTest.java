package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.main.Controller;
import freemind.main.Logger;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

    private ExportHook exportHook;
    private MapController mockController;
    private Controller mockFrame;
    private Logger mockLogger;

    @Before
    public void initMocks() {
        mockController = mock(MapController.class);
        mockFrame = mock(Controller.class);
        mockLogger = mock(Logger.class);
        exportHook = new ExportHook() {
            {
                this.getController = () -> mockController;
                this.getLogger = () -> mockLogger;
                this.getView = () -> getMockView();
            }
        };
        MockitoAnnotations.initMocks(this);
    }

    private MapView getMockView() {
        MapView mockView = mock(MapView.class);
        when(mockView.createImage(anyInt(), anyInt())).thenReturn(getMockBufferedImage());
        when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        when(mockView.getWidth()).thenReturn(200);
        when(mockView.getHeight()).thenReturn(300);
        return mockView;
    }

    private BufferedImage getMockBufferedImage() {
        BufferedImage mockImage = mock(BufferedImage.class);
        when(mockImage.getType()).thenReturn(BufferedImage.TYPE_INT_RGB);
        when(mockImage.getGraphics()).thenReturn(mock(Graphics.class));
        when(mockImage.getColorModel()).thenReturn(ColorModel.getRGBcolorModel());
        when(mockImage.getHeight()).thenReturn(100);
        when(mockImage.getWidth()).thenReturn(100);
        return mockImage;
    }

    @Test
    public void testChooseFile_whenValidTypeAndFileChosen_thenFileReturned() {
        String type = "png";
        String description = "Image files";
        String nameExtension = "png";
        when(mockController.getText(ArgumentMatchers.anyString())).thenReturn("Choose a file");
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockFileChooser());
        when(mockController.getMap()).thenReturn(mockMap());

        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertTrue(chosenFile != null);
        verify(mockFileChooser(), times(1)).showSaveDialog(ArgumentMatchers.eq(mockFrame.getContentPane()));
    }

    @Test
    public void testChooseFile_whenInvalidTypeAndFileChosen_thenFileWithTypeIsReturned() {
        String type = "png";
        String description = "Image files";
        String nameExtension = "jpg";
        when(mockController.getText(ArgumentMatchers.anyString())).thenReturn("Choose a file");
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockFileChooser());
        when(mockController.getMap()).thenReturn(mockMap());

        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertTrue(chosenFile != null);
        assertEquals("jpg", Tools.getExtension(chosenFile.getName()));
        verify(mockFileChooser(), times(1)).showSaveDialog(ArgumentMatchers.eq(mockFrame.getContentPane()));
    }

    @Test
    public void testChooseFile_whenFileChooserCanceled_thenNullReturned() {
        String type = "png";
        String description = "Image files";
        String nameExtension = "png";
        when(mockController.getText(ArgumentMatchers.anyString())).thenReturn("Choose a file");
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockCanceledFileChooser());
        when(mockController.getMap()).thenReturn(mockMap());

        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertNull(chosenFile);
        verify(mockFileChooser(), times(1)).showSaveDialog(ArgumentMatchers.eq(mockFrame.getContentPane()));
    }

    @Test
    public void testChooseFile_whenFileAlreadyExists_thenUserConfirmationRequested() {
        String type = "png";
        String description = "Image files";
        String nameExtension = "png";
        when(mockController.getText(ArgumentMatchers.anyString())).thenReturn("Choose a file");
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockFileChooser());
        when(mockController.getMap()).thenReturn(mockMapWithFile());

        ExportHook exportHook = spy(new ExportHook() {
            {
                this.getController = () -> mockController;
                this.getLogger = () -> mockLogger;
            }
        });

        doNothing().when(exportHook).showConfirmationDialog(ArgumentMatchers.eq(mockFrame.getContentPane()), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyInt());

        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        assertTrue(chosenFile != null);
        verify(exportHook, times(1)).showConfirmationDialog(ArgumentMatchers.eq(mockFrame.getContentPane()),
                ArgumentMatchers.eq("file_already_exists"), ArgumentMatchers.eq("file_already_exists"), ArgumentMatchers.eq(JOptionPane.YES_NO_OPTION));
    }

    @Test
    public void testGetTranslatableResourceString_whenResourceStringStartsWithPercentSign_thenControllerTextIsReturned() {
        String resourceName = "%s";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        String controllerText = "Hello %s";
        when(mockController.getText(ArgumentMatchers.anyString())).thenReturn(controllerText);

        assertEquals("Hello World", returnValue);
    }

    @Test
    public void testGetTranslatableResourceString_whenResourceStringDoesNotStartWithPercentSign_thenResourceStringIsReturned() {
        String resourceName = "Hello";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals("Hello", returnValue);
    }

    @Test
    public void testCreateBufferedImage_whenViewIsNotNull_thenBufferedImageIsReturned() {
        when(mockController.getView()).thenReturn(getMockView());

        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test
    public void testCreateBufferedImage_whenViewIsNull_thenNullIsReturned() {
        when(mockController.getView()).thenReturn(null);

        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource_whenFileFound_thenFileIsCopied() throws IOException {
        String prefix = "prefix/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromResource_whenFileNotFound_thenNothingIsDone() {
        String prefix = "prefix/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertFalse(file.exists());
    }

    @Test
    public void testCopyFromFile_whenFileFound_thenFileIsCopied() throws IOException {
        String dir = "source";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile_whenFileNotFound_thenNothingIsDone() {
        String dir = "source";
        String fileName = "test.txt";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertFalse(file.exists());
    }

    private File mockMap() {
        return mock(File.class);
    }

    private File mockMapWithFile() {
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        return file;
    }

    private FileDialog mockFileChooser() {
        FileDialog mockFileChooser = mock(FileDialog.class);
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockFileChooser);
        when(mockFileChooser.showSaveDialog(ArgumentMatchers.eq(mockFrame.getContentPane()))).thenReturn(JFileChooser.APPROVE_OPTION);
        return mockFileChooser;
    }

    private FileDialog mockCanceledFileChooser() {
        FileDialog mockFileChooser = mock(FileDialog.class);
        when(mockController.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(mockFileChooser);
        when(mockFileChooser.showSaveDialog(ArgumentMatchers.eq(mockFrame.getContentPane()))).thenReturn(JFileChooser.CANCEL_OPTION);
        return mockFileChooser;
    }
}