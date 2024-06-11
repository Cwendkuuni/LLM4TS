package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.SaveFileDialog;

import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.ModeControllerHookContext;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MapViewFactory;

public class ExportHookTest {

 private static final String TEST_RESOURCE_FOLDER = "testResources/";
 private static final String TEST_DESTINATION_FOLDER = "testOutput/";

 @Test
 public void testChooseFile() {
 ExportHook exportHook = new ExportHook();
 ModeController modeController = new ModeControllerHookContext();
 File exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNotNull(exportedFile);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullController() {
 ExportHook exportHook = new ExportHook();
 File exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNull(exportedFile);
 }

 @Test
 public void testChooseFileWithValidFileNameExtension() {
 ExportHook exportHook = new ExportHook();
 ModeController modeController = new ModeControllerHookContext();
 File exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNotNull(exportedFile);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFileWithInvalidFileNameExtension() {
 ExportHook exportHook = new ExportHook();
 ModeController modeController = new ModeControllerHookContext();
 File exportedFile = exportHook.chooseFile("txt", "Save Image As", "png");
 assertNull(exportedFile);
 }

 @Test
 public void testChooseFileWithFileAlreadyExists() {
 ExportHook exportHook = new ExportHook();
 ModeController modeController = new ModeControllerHookContext();
 File mmFile = new File(TEST_DESTINATION_FOLDER + "testFile.png");
 mmFile.createNewFile();
 File exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNotNull(exportedFile);
 File overwrittenFile = new File(TEST_DESTINATION_FOLDER + "testFile.png");
 assertTrue(overwrittenFile.exists());
 JOptionPane.showConfirmDialog(new Object(), "File already exists, do you want to replace it?", "Confirmation Dialog", JOptionPane.YES_OPTION);
 exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNull(exportedFile);
 }

 @Test
 public void testChooseFileWithCancelButton() {
 ExportHook exportHook = new ExportHook();
 ModeController modeController = new ModeControllerHookContext();
 File exportedFile = exportHook.chooseFile("png", "Save Image As", "png");
 assertNull(exportedFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 ExportHook exportHook = new ExportHook();
 String resourceString = exportHook.getTranslatableResourceString("%0%");
 assertEquals("File already exists, do you want to replace it?", MessageFormat.format(resourceString, new Object[]{"testFile.png"}));
 }

 @Test
 public void testGetTranslatableResourceStringWithNullResourceName() {
 ExportHook exportHook = new ExportHook();
 String resourceString = exportHook.getTranslatableResourceString(null);
 assertNull(resourceString);
 }

 @Test
 public void testCreateBufferedImage() {
 ExportHook exportHook = new ExportHook();
 MapView mapView = MapViewFactory.createMapView(new Point(0, 0), new Point(10, 10));
 assertEquals(10, mapView.getWidth());
 assertEquals(10, mapView.getHeight());
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImageWithNullView() {
 ExportHook exportHook = new ExportHook();
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNull(bufferedImage);
 }

 @Test
 public void testCopyFromResource() {
 ExportHook exportHook = new ExportHook();
 String prefix = "freemind/extensions/ExportHook/images/";
 String fileName = "export.png";
 exportHook.copyFromResource(prefix, fileName, TEST_DESTINATION_FOLDER);
 File copyFile = new File(TEST_DESTINATION_FOLDER, fileName);
 assertFalse(copyFile.exists());
 try {
 exportHook.copyFromResource(prefix, fileName, TEST_DESTINATION_FOLDER);
 assertTrue(copyFile.exists());
 } catch (IOException e) {
 fail("Failed to copy file: " + e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullPrefix() {
 ExportHook exportHook = new ExportHook();
 String fileName = "export.png";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromResource(null, fileName, destinationDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullFileName() {
 ExportHook exportHook = new ExportHook();
 String prefix = "freemind/extensions/ExportHook/images/";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromResource(prefix, null, destinationDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullDestinationDirectory() {
 ExportHook exportHook = new ExportHook();
 String prefix = "freemind/extensions/ExportHook/images/";
 String fileName = "export.png";
 exportHook.copyFromResource(prefix, fileName, null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResourceWithNonExistingFile() {
 ExportHook exportHook = new ExportHook();
 String prefix = "freemind/extensions/ExportHook/images/nonExistingFile.png";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromResource(prefix, "nonExistingFile.png", destinationDirectory);
 }

 @Test
 public void testCopyFromFile() {
 ExportHook exportHook = new ExportHook();
 String dir = "testResources/freemind/extensions/ExportHook/images/";
 String fileName = "export.png";
 exportHook.copyFromFile(dir, fileName, TEST_DESTINATION_FOLDER);
 File copyFile = new File(TEST_DESTINATION_FOLDER, fileName);
 assertFalse(copyFile.exists());
 try {
 exportHook.copyFromFile(dir, fileName, TEST_DESTINATION_FOLDER);
 assertTrue(copyFile.exists());
 } catch (IOException e) {
 fail("Failed to copy file: " + e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullDirectory() {
 ExportHook exportHook = new ExportHook();
 String fileName = "export.png";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromFile(null, fileName, destinationDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullFileName() {
 ExportHook exportHook = new ExportHook();
 String dir = "testResources/freemind/extensions/ExportHook/images/";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromFile(dir, null, destinationDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullDestinationDirectory() {
 ExportHook exportHook = new ExportHook();
 String dir = "testResources/freemind/extensions/ExportHook/images/";
 String fileName = "export.png";
 exportHook.copyFromFile(dir, fileName, null);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileWithNonExistingFile() {
 ExportHook exportHook = new ExportHook();
 String dir = "testResources/nonExistingDirectory/";
 String fileName = "nonExistingFile.png";
 String destinationDirectory = TEST_DESTINATION_FOLDER;
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testImageFilterAccept() {
 FileFilter imageFilter = new ExportHook.ImageFilter("png", "Image (*.png)");
 assertTrue(imageFilter.accept(new File("testResources/testImage.png")));
 }

 @Test
 public void testImageFilterGetDescription() {
 FileFilter imageFilter = new ExportHook.ImageFilter("png", "Image (*.png)");
 assertEquals("Image (*.png)", imageFilter.getDescription());
 }
}