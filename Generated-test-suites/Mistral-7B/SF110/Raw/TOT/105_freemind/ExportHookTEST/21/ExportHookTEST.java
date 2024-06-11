package freemind.extensions;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.controller.mode.ModeController;
import freemind.controller.mode.ModeControllerHookAdapter;
import freemind.controller.view.MapView;
import freemind.main.Logger;
import freemind.resources.Resources;
import freemind.view.mindmapview.MindMapView;

public class ExportHookTest {

    @Mock
    private Controller controller;

    @Mock
    private MapView view;

    @Mock
    private Logger logger;

    @Mock
    private Resources resources;

    @InjectMocks
    private ExportHook exportHook;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(controller.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileDialog.class));
        when(controller.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
    }

    @Test
    public void testChooseFile_ValidFileIsSelected() throws IOException {
        final String type = "png";
        final String description = "Export as PNG";
        final File expectedChosenFile = new File("output.png");
        final FileDialog chooser = mock(FileDialog.class);
        when(chooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
        when(controller.getFrame().getContentPane()).thenReturn(mock(Container.class));
        when(controller.getMap()).thenReturn(mock(MindMapView.class));

        File chosenFile = exportHook.chooseImageFile(type, description, null, controller);

        assertEquals(expectedChosenFile, chosenFile);
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verify(controller, times(1)).getText(description);
        verify(controller, times(1)).getFrame();
        verify(controller, times(1)).getMap();
    }

    @Test
    public void testChooseFile_InvalidFileIsSelected() throws IOException {
        final String type = "png";
        final String description = "Export as PNG";
        final FileDialog chooser = mock(FileDialog.class);
        when(chooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
        when(controller.getFrame().getContentPane()).thenReturn(mock(Container.class));
        when(controller.getMap()).thenReturn(mock(MindMapView.class));

        File chosenFile = exportHook.chooseImageFile(type, description, null, controller);

        assertEquals(null, chosenFile);
        verify(controller, times(1)).getFileChooser(any(FileFilter.class));
        verify(controller, times(1)).getText(description);
        verify(controller, times(1)).getFrame();
        verify(controller, times(1)).getMap();
    }

    @Test
    public void testCreateBufferedImage() {
        when(controller.getView()).thenReturn(view);

        BufferedImage result = exportHook.createBufferedImage();

        assertEquals(view, exportHook.view);
        verify(view, times(1)).preparePrinting();
        verify(controller, times(1)).getView();
    }

    @Test
    public void testGetTranslatableResourceString() {
        final String resourceName = "%s";

        String result = exportHook.getTranslatableResourceString(resourceName);

        assertNull(result);
        verify(controller, times(1)).getText("%s");
    }

    @Test
    public void testGetTranslatableResourceString_TranslatedStringIsReturned() {
        final String resourceName = "test.resource";
        final String expectedTranslation = "test translation";

        when(controller.getText(resourceName)).thenReturn(expectedTranslation);

        String result = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedTranslation, result);
        verify(controller, times(1)).getText(resourceName);
    }
}