package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.FreemindController;
import freemind.modes.MindMapController;
import freemind.view.mindmapview.MindMapView;

public class ExportHookTest {

    @Mock
    private FreemindController mockFreemindController;

    @Mock
    private MindMapController mockMindMapController;

    @Mock
    private MindMapView mockMindMapView;

    @Mock
    private Resources mockResources;

    private ExportHook exportHook;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(mockFreemindController.getView()).thenReturn(mockMindMapView);
        when(mockFreemindController.getText(anyString())).then(invocation -> invocation.getArgument(0));
        when(mockFreemindController.getMap()).thenReturn(mockMindMapController);
        when(mockMindMapController.getFile()).thenReturn(new File("testFile.mm"));
        when(mockMindMapController.getFileChooser(any(FileFilter.class))).thenReturn(mockJFileChooser());
        when(mockJFileChooser().showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(mockJFileChooser().getSelectedFile()).thenReturn(new File("testFile.ext"));

        exportHook = new ExportHook() {
            @Override
            protected ModeController getController() {
                return mockFreemindController;
            }

            @Override
            public Resources getResources() {
                return mockResources;
            }
        };
    }

    @Test
    public void testChooseFile() {
        ArgumentCaptor<FileFilter> fileFilterCaptor = ArgumentCaptor.forClass(FileFilter.class);
        when(mockFreemindController.getFileChooser(fileFilterCaptor.capture())).thenReturn(mockJFileChooser());

        ExportHook.FileChooserResult result = exportHook.chooseFile("image/*", "Save image as...", "png");

        assertEquals("png", result.getFileExtension());
        assertEquals("Save image as...", result.getDescription());
        FileFilter fileFilter = fileFilterCaptor.getValue();
        assertEquals("png", fileFilter.getDescription());
        assertTrue(fileFilter instanceof ExportHook.ImageFilter);
    }

    @Test
    public void testCreateBufferedImage() throws IOException {
        BufferedImage expectedImage = ImageIO.read(getClass().getResource("/testImage.jpg"));

        BufferedImage actualImage = exportHook.createBufferedImage();

        assertEquals(expectedImage.getWidth(), actualImage.getWidth());
        assertEquals(expectedImage.getHeight(), actualImage.getHeight());
        assertArrayEquals(expectedImage.getRGB(0, 0), actualImage.getRGB(0, 0));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%0";
        String expectedValue = "value%0";

        String actualValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedValue, actualValue);
    }
}

class MockJFileChooser extends JFileChooser {
    @Override
    public void setSelectedFile(File file) {
        // Do nothing
    }
}