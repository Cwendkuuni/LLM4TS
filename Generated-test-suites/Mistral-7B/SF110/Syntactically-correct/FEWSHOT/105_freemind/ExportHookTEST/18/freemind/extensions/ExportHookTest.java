package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import javax.swing.filechooser.FileChooserBuilder;
import javax.swing.filechooser.FileChooserDescriptor;
import javax.swing.filechooser.FileFilter;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        final String type = "png";
        final String description = "Image files";
        final String nameExtension = null;
        final ModeController controllerMock = mock(ModeController.class);
        when(controllerMock.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        ExportHook exportHook = new ExportHook();

        File chosenFile = exportHook.chooseFile(type, description, nameExtension, controllerMock);
        assertNull(chosenFile);

        // test with existing map file
        File mapFile = new File("testMap.mm");
        when(controllerMock.getMap()).thenReturn(mapFile);
        String proposedName = mapFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + nameExtension + "." + type;
        File chooserMock = mock(FileChooserBuilder.class);
        when(controllerMock.getFileChooser(any(FileFilter.class))).thenReturn(chooserMock);
        when(chooserMock.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        File chosenFile2 = exportHook.chooseFile(type, description, nameExtension, controllerMock);
        assertEquals(new File(proposedName), chosenFile2);
    }

    @Test
    public void testCreateBufferedImage() {
        final MapView viewMock = mock(MapView.class);
        ExportHook exportHook = new ExportHook();
        when(exportHook.getController()).thenReturn(mock(ModeController.class));
        when(exportHook.getController().getView()).thenReturn(viewMock);

        BufferedImage bufferedImage = exportHook.createBufferedImage();
        assertNotNull(bufferedImage);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "%resource.key%";
        String returnValue = exportHook.getTranslatableResourceString(resourceName);
        assertEquals("resource.key", returnValue);
    }

    @Test
    public void testCopyFromResource() throws IOException {
        ExportHook exportHook = new ExportHook();
        String prefix = "prefix/";
        String fileName = "test.txt";
        String destinationDirectory = "dest/";

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testCopyFromFile() throws IOException {
        ExportHook exportHook = new ExportHook();
        String dir = "src/test/resources/";
        String fileName = "test.txt";
        String destinationDirectory = "dest/";

        exportHook.copyFromFile(dir, fileName, destinationDirectory);

        File file = new File(destinationDirectory, fileName);
        assertTrue(file.exists());
    }

    @Test
    public void testImageFilterAccept() {
        FileFilter imageFilter = new ExportHook.ImageFilter("png", null);
        File file = new File("test.docx");

        assertFalse(imageFilter.accept(file));
    }

    @Test
    public void testImageFilterGetDescription() {
        FileFilter imageFilter = new ExportHook.ImageFilter("png", "Image files (*.png)");

        assertEquals("Image files (*.png)", imageFilter.getDescription());
    }

    @Test
    public void testChooseImageFile() throws IOException {
        final String type = "png";
        final String description = "Image files";
        final String nameExtension = null;
        final ModeController controllerMock = mock(ModeController.class);
        when(controllerMock.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);

        ExportHook exportHook = new ExportHook();

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controllerMock);
        assertNull(chosenFile);

        // test with existing map file
        File mapFile = new File("testMap.mm");
        when(controllerMock.getMap()).thenReturn(mapFile);
        String proposedName = mapFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + nameExtension + "." + type;
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, "Save Image File", new File("."));
        descriptor.setFileFilter(new ExportHook.ImageFilter(type, "Image files (*.png)"));
        FileChooserBuilder chooserBuilder = new FileChooserBuilder();
        chooserBuilder.setDialogType(JFileChooser.SAVE_DIALOG);
        chooserBuilder.setMultiSelectionEnabled(false);
        chooserBuilder.setApproveButtonText("Save");
        chooserBuilder.setDialogTitle("Save Image File");
        chooserBuilder.setCurrentDirectory(new File("."));
        chooserBuilder.setFileFilter(descriptor.getFileFilter());
        FileDialog chooserMock = (FileDialog) chooserBuilder.build();
        when(controllerMock.getFileChooser(any(FileFilter.class))).thenReturn(chooserMock);
        when(chooserMock.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        File chosenFile2 = exportHook.chooseImageFile(type, description, nameExtension, controllerMock);
        assertEquals(new File(proposedName), chosenFile2);
    }
}