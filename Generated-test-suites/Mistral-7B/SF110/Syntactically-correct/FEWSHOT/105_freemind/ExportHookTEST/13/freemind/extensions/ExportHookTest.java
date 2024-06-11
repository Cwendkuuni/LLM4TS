package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.Controller;
import freemind.controller.ControllerFactory;
import freemind.controller.ModeController;
import freemind.main.FreeMindFrame;
import freemind.modes.MementoMode;
import freemind.view.mindmapview.MindMapView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

    private ExportHook exportHook;
    private Controller controller;
    private MapView view;
    private MementoMode mode;
    private FreeMindFrame frame;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controller = mock(Controller.class);
        frame = mock(FreeMindFrame.class);
        mode = mock(MementoMode.class);
        when(controller.getView()).thenReturn(view);
        when(controller.getText(ArgumentMatchers.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(controller.getFrame()).thenReturn(frame);
        when(controller.getFileChooser(ArgumentMatchers.isA(FileFilter.class))).thenReturn(mock(JFileChooser.class));
        exportHook = new ExportHook();
        exportHook.setController(controller);
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG files (*.png)";
        File expectedResult = new File("path/to/expected.png");

        FileFilter filter = mock(FileFilter.class);
        when(filter.accept(ArgumentMatchers.any(File.class))).thenReturn(true);
        when(filter.getDescription()).thenReturn(description);

        JFileChooser chooser = mock(JFileChooser.class);
        when(controller.getFileChooser(filter)).thenReturn(chooser);

        File mmFile = new File("path/to/map.mm");
        when(controller.getMap()).thenReturn(mmFile);

        File chosenFile = exportHook.chooseFile(type, description, null);

        verify(controller, times(1)).getFileChooser(filter);
        verify(controller, times(1)).getMap();
        verify(chooser, times(1)).showSaveDialog(ArgumentMatchers.any(Component.class));

        if (chosenFile != null) {
            assertEquals(expectedResult.toString(), chosenFile.toString());
        } else {
            assertEquals(null, chosenFile);
        }
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage expectedResult = ImageIO.read(new File("path/to/image.jpg"));

        when(controller.getView()).thenReturn(new MindMapView(new Dimension(500, 500), new Color(0, 0, 0), new Color(255, 255, 255), new Color(0, 0, 0), new Color(0, 0, 0), 1.0F, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));

        BufferedImage result = exportHook.createBufferedImage();

        verify(controller, times(1)).getView();

        if (result != null) {
            assertEquals(expectedResult.getWidth(), result.getWidth());
            assertEquals(expectedResult.getHeight(), result.getHeight());
            assertArrayEquals(expectedResult.getRGB(0, 0), result.getRGB(0, 0));
        } else {
            assertEquals(null, result);
        }
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%resourceName%";
        String expectedResult = "Resource Name";

        String returnValue = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedResult, returnValue);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "prefix";
        String fileName = "fileName.png";
        String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        URL resource = mock(URL.class);
        when(exportHook.getResource(prefix + fileName)).thenReturn(resource);

        verify(exportHook, times(1)).getResource(prefix + fileName);
        verify(resource, times(1)).openStream();
        verify(new FileOutputStream(destinationDirectory + "/" + fileName), times(1)).write(ArgumentMatchers.anyByteArray());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "path/to/dir";
        String fileName = "fileName.png";
        String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File resource = new File(dir, fileName);

        verify(exportHook, times(1)).getResource(ArgumentMatchers.eq(null));
        verify(resource, times(1)).exists();
        verify(new FileInputStream(resource), times(1)).readAllBytes();
        verify(new FileOutputStream(destinationDirectory + "/" + fileName), times(1)).write(ArgumentMatchers.anyByteArray());
    }
}