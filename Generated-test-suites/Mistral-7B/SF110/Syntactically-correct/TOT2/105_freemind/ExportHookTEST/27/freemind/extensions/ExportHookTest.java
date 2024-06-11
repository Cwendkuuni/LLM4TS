package freemind.extensions;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.TestName;

import java.io.*;
import java.net.*;
import java.text.*;
import static freemind.main.Tools.*;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController controller;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        controller = new ModeController();
    }

    @Test
    public void testChooseFile_ValidArguments() {
        String type = "png";
        String description = "Export Image";
        String nameExtension = "image";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension, controller);
        assertTrue(chosenFile != null);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "test.string.resource";
        String expectedResult = "Expected Translatable Resource String";
        String actualResult = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test(expected = NullPointerException.class)
    public void testChooseFile_NullController() {
        String type = "png";
        String description = "Export Image";
        String nameExtension = "image";
        File chosenFile = exportHook.chooseFile(type, description, nameExtension, null);
    }

    @Test
    public void testChooseImageFile_InvalidExtension() {
        String type = "pdf";
        String description = "Export Image";
        String nameExtension = "image";
        ModeController controller = new ModeController();
        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNull(chosenFile);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateBufferedImage_NullView() {
        exportHook.view = null;
        BufferedImage bufferedImage = exportHook.createBufferedImage();
        fail("Expected NullPointerException was not thrown.");
    }

    @After
    public void tearDown() {
        exportHook = null;
        controller = null;
    }
}