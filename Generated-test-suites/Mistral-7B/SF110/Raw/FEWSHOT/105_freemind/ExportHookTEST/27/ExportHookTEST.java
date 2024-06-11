package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.main.MindMapController;
import freemind.view.mindmapview.MapView;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        MindMapController mockController = mock(MindMapController.class);
        ExportHook exportHook = new ExportHook() {
            @Override
            ModeController getController() {
                return mockController;
            }
        };

        String type = "png";
        String description = "PNG Images (*.png)";
        String nameExtension = ".png";
        File expectedFile = new File("expectedFilePath");
        when(mockController.getFileChooser(any(FileFilter.class)))
                .thenReturn(new JFileChooser());
        when(mockController.getMap()).thenReturn(new File("mapFile"));
        when(mockController.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        File actualFile = exportHook.chooseFile(type, description, nameExtension);

        verify(mockController).getFileChooser(argThat(isA(FileFilter.class)));
        assertEquals(expectedFile, actualFile);
    }

    @Test
    public void testCreateBufferedImage() throws Exception {
        ExportHook exportHook = new ExportHook();
        assertNotNull(exportHook.createBufferedImage());
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "%someKey%";
        String expectedResult = "someKey";
        String actualResult = exportHook.getTranslatableResourceString(resourceName);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        ExportHook exportHook = new ExportHook();
        String prefix = "images/";
        String fileName = "example.png";
        String destinationDirectory = "target/test-classes/resources";

        exportHook.logger = mock(Logger.class);
        URL resource = new URL("file:src/test/resources/" + prefix + fileName);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
        when(resource.openStream()).thenReturn(in);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(out);
        when(Tools.copyStream(anyInputStream(), anyOutputStream(), anyBoolean())).thenReturn(true);

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        verify(exportHook.logger, times(1)).finest("searching for " + prefix + fileName);
        verify(resource, times(1)).openStream();
        verify(out, times(1)).write(anyByteArray());
        verify(out, times(1)).close();
    }

    @Test
    public void testCopyFromFile() throws IOException {
        ExportHook exportHook = new ExportHook();
        String dir = "src/test/resources/";
        String fileName = "example.png";
        String destinationDirectory = "target/test-classes/resources";

        exportHook.logger = mock(Logger.class);
        File resource = new File(dir, fileName);
        InputStream in = mock(InputStream.class);
        OutputStream out = mock(OutputStream.class);
        when(exportHook.logger).thenCallRealMethod("finest");
        when(new FileInputStream(resource)).thenReturn(in);
        when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(out);
        when(Tools.copyStream(anyInputStream(), anyOutputStream(), anyBoolean())).thenReturn(true);

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        verify(exportHook.logger, times(1)).finest("searching for " + dir + fileName);
        verify(resource, times(1)).exists();
        verify(resource, times(1)).isFile();
        verify(resource, times(1)).getAbsolutePath();
        verify(in, times(1)).close();
        verify(out, times(1)).write(anyByteArray());
        verify(out, times(1)).close();
    }
}