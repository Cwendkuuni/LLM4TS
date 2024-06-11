package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.actions.view.PrintAction;
import freemind.main.Logger;
import freemind.mode.MapController;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

    private ExportHook exportHook;
    private Controller controllerMock;
    private MapView mapViewMock;
    private MapController mapControllerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        controllerMock = mock(Controller.class);
        mapViewMock = mock(MapView.class);
        mapControllerMock = mock(MapController.class);
        exportHook = new ExportHook();
        exportHook.setController(controllerMock);
        when(controllerMock.getView()).thenReturn(mapViewMock);
        when(mapControllerMock.getFrame()).thenReturn(mock(Frame.class));
        when(mapControllerMock.getText(anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));
        when(controllerMock.getFileChooser(any(FileFilter.class))).thenReturn(mock(FreeMindFileDialog.class));
        when(controllerMock.getMap()).thenReturn(mapControllerMock);
        when(mapViewMock.createImage(anyInt(), anyInt())).thenCallRealMethod();
        when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "Test description";
        File expectedFile = new File("expectedFileName.png");
        when(controllerMock.getResourceString(anyString())).thenReturn("{0}");

        File actualFile = exportHook.chooseFile(type, description, null);
        assertEquals(expectedFile, actualFile);

        verify(controllerMock, times(1)).getText(eq("{0}"));
        verify(controllerMock, times(1)).getFileChooser(any(ImageFilter.class));
        verify(controllerMock, times(1)).getMap();
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage expectedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        BufferedImage actualImage = exportHook.createBufferedImage();
        assertEquals(expectedImage, actualImage);

        verify(mapViewMock, times(1)).preparePrinting();
        verify(mapViewMock, times(1)).createImage(eq(100), eq(100));
        verify(mapViewMock, times(1)).endPrinting();
    }

    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "test";
        String fileName = "testFile.txt";
        String destinationDirectory = "destination";

        URL resource = new URL("http://test.com/test.txt");
        when(controllerMock.getResource(eq(prefix + fileName))).thenReturn(resource);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(controllerMock, times(1)).getLogger();
        verify(controllerMock, times(1)).getResource(eq(prefix + fileName));
        verify(controllerMock, times(1)).openStream();
        verify(controllerMock, times(1)).getOutputStream(eq(destinationDirectory + "/" + fileName));
        verify(controllerMock, times(1)).copyStream(anyInputStream(), anyOutputStream(), eq(true));
    }

    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "test";
        String fileName = "testFile.txt";
        String destinationDirectory = "destination";

        File resource = new File(dir, fileName);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(controllerMock, times(1)).getLogger();
        verify(controllerMock, times(1)).newFileInputStream(eq(new File(dir, fileName)));
        verify(controllerMock, times(1)).getOutputStream(eq(destinationDirectory + "/" + fileName));
        verify(controllerMock, times(1)).copyStream(anyInputStream(), anyOutputStream(), eq(true));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%{0}";

        String expectedString = "Test {0}";

        String actualString = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedString, actualString);
    }
}