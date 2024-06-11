package freemind.extensions.test;

import static org.junit.Assert.*;
import static freemind.extensions.ExportHook.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.ModeController;
import freemind.controller.FrameController;
import freemind.modes.MMapMode;
import freemind.main.MainFrame;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;
 private MainFrame mainFrame;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 modeController = new MMapMode();
 mapView = new MapView(new MindMapModel(), modeController);
 mainFrame = new MainFrame(new MindMapModel(), new MindMapController());
 }

 // Test for chooseFile method
 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "Image files";
 File expectedFile = new File("file.png");
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertNotNull(chosenFile);
 assertEquals(expectedFile, chosenFile);
 }

 // Test for chooseFile when file with same name already exists
 @Test(expected = IllegalStateException.class)
 public void testChooseFileWhenFileAlreadyExists() {
 String type = "png";
 String description = "Image files";
 File existingFile = new File("file.png");
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals(existingFile, chosenFile);
 }

 // Test for chooseFile when file filter is null
 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullFileFilter() {
 String type = "png";
 String description = "Image files";
 FileFilter fileFilter = null;
 File chosenFile = exportHook.chooseFile(type, description, fileFilter);
 }

 // Test for chooseImageFile method
 @Test
 public void testChooseImageFile() {
 String type = "png";
 String description = "Image files";
 String nameExtension = "png";
 ModeController controller = new MMapMode();
 File expectedFile = new File("file.png");
 File chosenFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
 assertNotNull(chosenFile);
 assertEquals(expectedFile, chosenFile);
 }

 // Test for getTranslatableResourceString method
 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%message.hello";
 String expectedResult = "hello";
 String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedResult, result);
 }

 // Test for createBufferedImage method
 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertSame(expectedImage, actualImage);
 }

 // Test for copyFromResource method
 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "resources/";
 String fileName = "testfile.txt";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 // Test for copyFromFile method
 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "src/test/resources/";
 String fileName = "testfile.txt";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 // Test for ImageFilter class
 @Test
 public void testImageFilter() {
 FileFilter filter = new ImageFilter("png", null);
 assertTrue(filter.accept(new File("image.png")));
 assertTrue(filter.accept(new File("image.jpg")));
 assertFalse(filter.accept(new File("document.doc")));
 }
}