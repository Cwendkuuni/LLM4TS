package freemind.extensions;

import static org.junit.Assert.*;
import static freemind.modes.FMModeController.getText;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());

        final String type = "png";
        final String description = "Export as PNG";
        final String nameExtension = "PNG";
        final File chosenFile = new File("test.png");

        assertEquals(chosenFile, exportHook.chooseFile(type, description, nameExtension, controller));

        verify(controller).getFileChooser(isA(ImageFilter.class));
    }

    @Test
    public void testChooseFile_NullController() {
        final ExportHook exportHook = new ExportHook();

        assertNull(exportHook.chooseFile(null, null, null));
    }

    @Test
    public void testChooseFile_NullType() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();

        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());

        assertNull(exportHook.chooseFile(null, "Export as PNG", null, controller));
    }

    @Test
    public void testChooseFile_NullDescription() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());

        final String type = "png";
        final String nameExtension = "PNG";

        assertNull(exportHook.chooseFile(type, null, nameExtension, controller));
    }

    @Test
    public void testChooseFile_NullNameExtension() {
        final ModeController controller = mock(ModeController.class);
        final ExportHook exportHook = new ExportHook();
        when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());

        final String type = "png";
        final String description = "Export as PNG";

        assertNull(exportHook.chooseFile(type, description, null, controller));
    }

    @Test
    public void testCreateBufferedImage() {
        final ExportHook exportHook = new ExportHook();
        final ModeController controller = mock(ModeController.class);
        when(controller.getView()).thenReturn(new JFrame());

        final BufferedImage bufferedImage = exportHook.createBufferedImage();

        assertTrue(bufferedImage != null);
    }

    @Test
    public void testCreateBufferedImage_NullController() {
        final ExportHook exportHook = new ExportHook();

        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testGetTranslatableResourceString() {
        final ExportHook exportHook = new ExportHook();
        final String resourceName = "%your_name%";
        final String translatableResourceString = "Hello, %your_name%";

        assertEquals(translatableResourceString, exportHook.getTranslatableResourceString(resourceName));
    }

    @Test
    public void testGetTranslatableResourceString_NullResourceName() {
        final ExportHook exportHook = new ExportHook();

        assertNull(exportHook.getTranslatableResourceString(null));
    }

    @Test
    public void testCopyFromResource() {
        final ExportHook exportHook = new ExportHook();

        final String prefix = "prefix";
        final String fileName = "test.png";
        final String destinationDirectory = "destination";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        // Verify that the file is copied using getResource method and Tools.copyStream method
        // Add appropriate mocks and verifications here
    }

    @Test
    public void testCopyFromFile() {
        final ExportHook exportHook = new ExportHook();

        final String dir = "src";
        final String fileName = "test.png";
        final String destinationDirectory = "destination";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        // Verify that the file is copied using new File method and Tools.copyStream method
        // Add appropriate mocks and verifications here
    }
}