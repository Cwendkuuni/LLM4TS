package freemind.extensions;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.FreeMindFrame;
import freemind.modes.MindMapMode;
import freemind.view.mindmapview.MapView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController modeController;
    private FreeMindFrame freeMindFrame;
    private MapView mapView;

    @Before
    public void setup() {
        modeController = Mockito.mock(ModeController.class);
        freeMindFrame = Mockito.mock(FreeMindFrame.class);
        mapView = Mockito.mock(MapView.class);
        Mockito.when(modeController.getFrame()).thenReturn(freeMindFrame);
        Mockito.when(modeController.getMap()).thenReturn(new MindMapMode());
        Mockito.when(modeController.getView()).thenReturn(mapView);
        exportHook = new ExportHook();
        exportHook.setController(modeController);
    }

    @Test
    public void testChooseFile() throws IOException {
        Mockito.when(modeController.getFileChooser(new ImageFilter("image/jpeg", "Save JPEG Image"))).thenReturn(new JFileChooser());
        File chosenFile = exportHook.chooseFile("image/jpeg", "Save JPEG Image", null);
        assertNotNull(chosenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test.string.resource";
        String expectedResult = "Expected result";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBufferedImageWithNullView() {
        ExportHook exportHook = new ExportHook();
        exportHook.view = null;
        exportHook.createBufferedImage();
    }
}