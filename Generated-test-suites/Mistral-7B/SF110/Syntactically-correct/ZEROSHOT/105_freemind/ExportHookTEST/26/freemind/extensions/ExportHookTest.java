package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class ExportHookTest {

 private ModeController controller;
 private ExportHook exportHook;

 @Before
 public void setUp() {
 controller = Mockito.mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "PNG Image (*.png)";
 File expectedFile = new File("test.png");
 File chosenFile = exportHook.chooseFile(type, description, type);
 assertEquals(expectedFile, chosenFile);

 // Test null controller
 exportHook = new ExportHook();
 exportHook.setController(null);
 chosenFile = exportHook.chooseFile(type, description, type);
 assertNull(chosenFile);

 // Test no file selected
 FreeMindFileDialog chooser = Mockito.mock(JFileChooser.class);
 when(controller.getFileChooser(ArgumentMatchers.isA(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(ArgumentMatchers.any(Component.class))).thenReturn(JFileChooser.CANCEL_OPTION);
 chosenFile = exportHook.chooseFile(type, description, type);
 assertNull(chosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 when(controller.getView()).thenReturn(Mockito.mock(MapView.class));
 when(controller.getView().getWidth()).thenReturn(100);
 when(controller.getView().getHeight()).thenReturn(100);
 when(controller.getView().createImage(100, 100)).thenReturn(expectedImage);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testGetTranslatableResourceString() throws IOException {
 ResourceBundle bundle = Mockito.mock(ResourceBundle.class);
 when(controller.getText(ArgumentMatchers.anyString())).thenReturn("Test Text");
 when(controller.getResourceString(ArgumentMatchers.anyString())).thenReturn("%Test Text%");
 String resourceName = "test.string";
 String actualResult = exportHook.getTranslatableResourceString(resourceName);
 assertEquals("Test Text", actualResult);

 // Test null controller
 exportHook = new ExportHook();
 exportHook.setController(null);
 String nullResourceName = "test.null";
 String actualNullResult = exportHook.getTranslatableResourceString(nullResourceName);
 assertNull(actualNullResult);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "freemind/extensions/";
 String fileName = "test.png";
 String destinationDirectory = "test_directory/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "test_directory/";
 String fileName = "test.png";
 String destinationDirectory = "test_destination_directory/";
 File resource = new File(dir, fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }
}