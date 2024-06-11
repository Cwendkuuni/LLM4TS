package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.file.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeControllerImpl;
import freemind.view.mindmapview.ViewManager;

/**
 * Test class for ExportHook.
 *
 * @author your.name
 */
public class ExportHookTest {

    private ModeController controller;
    private ExportHook exportHook;
    private ViewManager viewManager;
    private MapView mapView;

    @Before
    public void setUp() {
        Resources.getInstance().setLogLevel(ResourceBundle.Control.CONTROL_LOG_SEVERE);
        controller = new ModeControllerImpl();
        viewManager = controller.getViewManager();
        mapView = viewManager.getMapView();
        exportHook = new ExportHook() {
            @Override
            public MapView getController() {
                return controller;
            }
        };
    }

    @After
    public void tearDown() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

    @Test
    public void testChooseFile() {
        File expectedFile = new File("expectedFile.png");
        File chosenFile = exportHook.chooseFile("image/png", "PNG Image", "png");
        assertEquals(expectedFile, chosenFile);
    }

    @Test
    public void testChooseFile_nullController() {
        exportHook = new ExportHook();
        File chosenFile = exportHook.chooseFile("image/png", "PNG Image", "png");
        assertNull(chosenFile);
    }

    @Test
    public void testChooseFile_nullType() {
        exportHook = new ExportHook();
        File chosenFile = exportHook.chooseFile(null, "PNG Image", "png");
        assertNull(chosenFile);
    }

    @Test
    public void testChooseFile_nullDescription() {
        exportHook = new ExportHook();
        File chosenFile = exportHook.chooseFile("image/png", null, "png");
        assertNull(chosenFile);
    }

    @Test
    public void testChooseFile_nullMapFile() {
        controller = new ModeControllerImpl();
        exportHook = new ExportHook() {
            @Override
            public MapView getController() {
                return controller;
            }

            @Override
            public Map getMap() {
                return null;
            }
        };
        File chosenFile = exportHook.chooseFile("image/png", "PNG Image", "png");
        assertNull(chosenFile);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "%your.string.key%";
        String expectedValue = "Your string value";
        String translatableValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedValue, translatableValue);
    }

    @Test
    public void testGetTranslatableResourceString_nullResourceName() {
        String translatableValue = exportHook.getTranslatableResourceString(null);
        assertNull(translatableValue);
    }

    @Test
    public void testCreateBufferedImage() {
        Image expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertEquals(expectedImage, bufferedImage);
    }

    @Test
    public void testCreateBufferedImage_nullController() {
        exportHook = new ExportHook();
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        String prefix = "test/resources/";
        String fileName = "testFile.txt";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File expectedFile = new File(destinationDirectory, fileName);
        File actualFile = new File(destinationDirectory, fileName);
        assertTrue(actualFile.exists());
        assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    @Test
    public void testCopyFromResource_nullPrefix() throws IOException {
        String fileName = "testFile.txt";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromResource(null, fileName, destinationDirectory);
        File actualFile = new File(destinationDirectory, fileName);
        assertFalse(actualFile.exists());
    }

    @Test
    public void testCopyFromResource_nullFileName() throws IOException {
        String prefix = "test/resources/";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromResource(prefix, null, destinationDirectory);
        File actualFile = new File(destinationDirectory, "testFile.txt");
        assertFalse(actualFile.exists());
    }

    @Test
    public void testCopyFromResource_nullDestinationDirectory() throws IOException {
        String prefix = "test/resources/";
        String fileName = "testFile.txt";
        exportHook.copyFromResource(prefix, fileName, null);
        File actualFile = new File("testOutput/testFile.txt");
        assertFalse(actualFile.exists());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        String dir = "test/input/";
        String fileName = "testFile.txt";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File expectedFile = new File(destinationDirectory, fileName);
        File actualFile = new File(destinationDirectory, fileName);
        assertTrue(actualFile.exists());
        assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    @Test
    public void testCopyFromFile_nullDir() throws IOException {
        String fileName = "testFile.txt";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromFile(null, fileName, destinationDirectory);
        File actualFile = new File(destinationDirectory, fileName);
        assertFalse(actualFile.exists());
    }

    @Test
    public void testCopyFromFile_nullFileName() throws IOException {
        String dir = "test/input/";
        String destinationDirectory = "testOutput/";
        exportHook.copyFromFile(dir, null, destinationDirectory);
        File actualFile = new File(destinationDirectory, "testFile.txt");
        assertFalse(actualFile.exists());
    }

    @Test
    public void testCopyFromFile_nullDestinationDirectory() throws IOException {
        String dir = "test/input/";
        String fileName = "testFile.txt";
        exportHook.copyFromFile(dir, fileName, null);
        File actualFile = new File("testOutput/testFile.txt");
        assertFalse(actualFile.exists());
    }

    @Test
    public void testImageFilter_accept_validFile() {
        FileNameExtensionFilter filter = new ExportHook.ImageFilter("image/png", null);
        File file = new File("testFile.png");
        assertTrue(filter.accept(file));
    }

    @Test
    public void testImageFilter_accept_invalidFile() {
        FileNameExtensionFilter filter = new ExportHook.ImageFilter("image/png", null);
        File file = new File("testFile.txt");
        assertFalse(filter.accept(file));
    }

    @Test
    public void testImageFilter_getDescription_validDescription() {
        FileNameExtensionFilter filter = new ExportHook.ImageFilter("image/png", "PNG Images");
        assertEquals("PNG Images", filter.getDescription());
    }

    @Test
    public void testImageFilter_getDescription_noDescription() {
        FileNameExtensionFilter filter = new ExportHook.ImageFilter("image/png", null);
        assertEquals("PNG", filter.getDescription());
    }
}