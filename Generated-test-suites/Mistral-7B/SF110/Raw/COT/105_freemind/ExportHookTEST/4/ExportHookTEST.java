package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExportHookTest {

 private ExportHook hook;
 private ModeController controller;
 private MapView view;

 @Before
 public void setUp() {
 controller = Mockito.mock(ModeController.class);
 hook = new ExportHook();
 hook.setController(controller);
 view = Mockito.mock(MapView.class);
 hook.setView(view);
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "Export as Image";
 final String nameExtension = null;
 final File chosenFile = new File("path/to/file.png");

 when(controller.getText("file_already_exists")).thenReturn("File already exists. Do you want to replace it?");
 when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 doReturn(chosenFile).when(controller.getMap()).getFile();

 final File result = hook.chooseFile(type, description, nameExtension);
 assertEquals(chosenFile, result);
 }

 @Test
 public void testChooseFile_WhenFileExistsWithSameName_ShouldShowConfirmDialog() {
 final String type = "png";
 final String description = "Export as Image";
 final String nameExtension = null;
 final String proposedName = "path/to/file.png";
 final File chooser = Mockito.mock(JFileChooser.class);
 doAnswer(invocation -> JOptionPane.showConfirmDialog(Mockito.any(), Mockito.any(), Mockito.any(), 0)).when(controller).showConfirmDialog(Mockito.any(), Mockito.any(), Mockito.any(), 0);

 final File result = hook.chooseFile(type, description, nameExtension);
 assertTrue(result == null);
 }

 @Test
 public void testChooseFile_WhenFileDoesNotExist_ShouldNotShowSaveDialog() {
 final String type = "png";
 final String description = "Export as Image";
 final String nameExtension = null;
 final File result = hook.chooseFile(type, description, nameExtension);
 assertTrue(result == null);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedImage = createImage(200, 200, Color.WHITE);
 when(view.preparePrinting()).thenReturn(true);
 when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedImage);
 when(view.getWidth()).thenReturn(200);
 when(view.getHeight()).thenReturn(200);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 200, 200));

 final BufferedImage result = hook.createBufferedImage();
 assertEquals(expectedImage, result);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%s_button";
 final String expectedResult = "button";

 final String result = hook.getTranslatableResourceString(resourceName);
 assertEquals(expectedResult, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceString_WhenResourceNameIsNull() {
 hook.getTranslatableResourceString(null);
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "prefix";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination";

 hook.copyFromResource(prefix, fileName, destinationDirectory);
 // Verify that the file has been copied
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromResource_WhenPrefixIsNull() {
 hook.copyFromResource(null, "file.txt", "destination");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromResource_WhenFileNameIsNull() {
 hook.copyFromResource("prefix", null, "destination");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromResource_WhenDestinationDirectoryIsNull() {
 hook.copyFromResource("prefix", "file.txt", null);
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "path/to/dir";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination";

 hook.copyFromFile(dir, fileName, destinationDirectory);
 // Verify that the file has been copied
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromFile_WhenDirIsNull() {
 hook.copyFromFile(null, "file.txt", "destination");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromFile_WhenFileNameIsNull() {
 hook.copyFromFile("path/to/dir", null, "destination");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCopyFromFile_WhenDestinationDirectoryIsNull() {
 hook.copyFromFile("path/to/dir", "file.txt", null);
 }

 @Test
 public void testImageFilterAccept() {
 final ImageFilter imageFilter = new ImageFilter("png", null);
 final File file = new File("path/to/image.jpg");

 assertTrue(imageFilter.accept(file));
 }

 @Test
 public void testImageFilterAccept_WhenFileIsDirectory() {
 final ImageFilter imageFilter = new ImageFilter("png", null);
 final File file = new File("path/to/directory");

 assertTrue(imageFilter.accept(file));
 }

 @Test
 public void testImageFilterAccept_WhenExtensionDoesNotMatch() {
 final ImageFilter imageFilter = new ImageFilter("png", null);
 final File file = new File("path/to/file.xls");

 assertFalse(imageFilter.accept(file));
 }

 @Test
 public void testImageFilterGetDescription() {
 final ImageFilter imageFilter = new ImageFilter("png", "Export as PNG image");

 assertEquals("Export as PNG image", imageFilter.getDescription());
 }

 @Test(expected = NullPointerException.class)
 public void testImageFilterConstructor_WhenTypeIsNull() {
 new ImageFilter(null, "");
 }

 @Test(expected = NullPointerException.class)
 public void testImageFilterConstructor_WhenDescriptionIsNull() {
 new ImageFilter("png", null);
 }
}