package freemind.extensions.test;

import static org.junit.Assert.*;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import org.junit.Test;

public class ExportHookTest {

 private static final String TEST_IMAGE_TYPE = "png";
 private static final String TEST_IMAGE_DESCRIPTION = "Test Image";
 private static final String TEST_MAP_RESOURCE_NAME = "testMap.mm";
 private static final String TEST_DESTINATION_DIRECTORY = "target/test-exports";
 private static final String TEST_RESOURCE_IMAGE_NAME = "testResourceImage.png";
 private static final String TEST_FILE_IMAGE_NAME = "testFileImage.png";
 private static final String TEST_IMAGE_FILE_NAME = "testImage.png";
 private static final String TEST_UNSUPPORTED_IMAGE_TYPE = "gif";
 private static final String TEST_INVALID_FILE_NAME = "testImage$";
 private static final String TEST_EXISTING_FILE_NAME = "testImage.exists.png";
 private static final String TEST_RESOURCE_STRING = "%testResourceString%";

 @SuppressWarnings("deprecation")
 @Test
 public void testChooseFile() {
 ModeController controller = new TestModeController();
 ExportHook exportHook = new ExportHook();

 // Test null arguments
 assertNull(exportHook.chooseFile(null, null, null));
 assertNull(exportHook.chooseFile(TEST_IMAGE_TYPE, null, null));

 // Test valid arguments
 File chosenFile = exportHook.chooseFile(TEST_IMAGE_TYPE, TEST_IMAGE_DESCRIPTION, TEST_IMAGE_NAME_EXTENSION, controller);
 assertNotNull(chosenFile);
 }

 @Test
 public void testChooseImageFile() {
 ModeController controller = new TestModeController();
 ExportHook exportHook = new ExportHook();

 // Test null arguments
 assertNull(ExportHook.chooseImageFile(null, null, null, controller));

 // Test valid arguments
 File chosenFile = ExportHook.chooseImageFile(TEST_IMAGE_TYPE, TEST_IMAGE_DESCRIPTION, TEST_IMAGE_NAME_EXTENSION, controller);
 assertNotNull(chosenFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 ExportHook exportHook = new ExportHook();

 // Test null argument
 assertNull(exportHook.getTranslatableResourceString(null));

 // Test valid argument
 String returnValue = exportHook.getTranslatableResourceString(TEST_RESOURCE_STRING);
 assertNotNull(returnValue);
 assertFalse(returnValue.startsWith("%"));
 }

 @Test
 public void testCreateBufferedImage() {
 ExportHook exportHook = new ExportHook();
 MapView view = new MapView() {
 @Override
 public void preparePrinting() {}

 @Override
 public void endPrinting() {}
 };
 exportHook.view = view;

 // Test null view
 assertNull(exportHook.createBufferedImage());

 // Test valid view
 Image image = exportHook.createBufferedImage();
 assertNotNull(image);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullPrefix() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource(null, TEST_RESOURCE_IMAGE_NAME, TEST_DESTINATION_DIRECTORY);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullFileName() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource(TEST_PREFIX, null, TEST_DESTINATION_DIRECTORY);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullDestinationDirectory() { ExportHook exportHook = new ExportHook(); exportHook.copyFromResource(TEST_PREFIX, TEST_RESOURCE_IMAGE_NAME, null); }

 @Test
 public void testCopyFromResource() throws IOException {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource(TEST_PREFIX, TEST_RESOURCE_IMAGE_NAME, TEST_DESTINATION_DIRECTORY);
 File file = new File(TEST_DESTINATION_DIRECTORY + "/" + TEST_RESOURCE_IMAGE_NAME);
 assertTrue(file.exists());
 file.delete();
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileNullDir() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(null, TEST_FILE_IMAGE_NAME, TEST_DESTINATION_DIRECTORY);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileNullFileName() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(TEST_DIR, null, TEST_DESTINATION_DIRECTORY);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileInvalidFileName() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(TEST_DIR, TEST_INVALID_FILE_NAME, TEST_DESTINATION_DIRECTORY);
 }

 @Test(expected = IOException.class)
 public void testCopyFromFileUnsupportedImageType() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(TEST_DIR, TEST_FILE_IMAGE_NAME, TEST_DESTINATION_DIRECTORY);
 File file = new File(TEST_DESTINATION_DIRECTORY + "/" + TEST_FILE_IMAGE_NAME);
 assertFalse(file.exists());
 exportHook.copyFromResource(TEST_PREFIX, TEST_UNSUPPORTED_IMAGE_TYPE, TEST_DESTINATION_DIRECTORY);
 File unsupportedFile = new File(TEST_DESTINATION_DIRECTORY + "/" + TEST_UNSUPPORTED_IMAGE_TYPE);
 assertTrue(unsupportedFile.exists());
 }

 @Test(expected = IOException.class)
 public void testCopyFromFileExistingFile() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(TEST_DIR, TEST_IMAGE_FILE_NAME, TEST_DESTINATION_DIRECTORY);
 File file = new File(TEST_DESTINATION_DIRECTORY + "/" + TEST_IMAGE_FILE_NAME);
 assertTrue(file.exists());
 exportHook.copyFromFile(TEST_DIR, TEST_IMAGE_FILE_NAME, TEST_DESTINATION_DIRECTORY);
 File existingFile = new File(TEST_DESTINATION_DIRECTORY + "/" + TEST_IMAGE_FILE_NAME);
 assertTrue(existingFile.exists());
 String overwriteText = MessageFormat.format(controller.getText("file_already_exists"), existingFile.toString());
 int overwriteMap = JOptionPane.showConfirmDialog(new JFrame(), overwriteText, overwriteText, JOptionPane.YES_NO_OPTION);
 assertEquals(JOptionPane.NO_OPTION, overwriteMap);
 }
}

 class TestModeController extends ModeController {
 @Override
 public Frame getFrame() {
 return new JFrame() {
 @Override
 public void setSize(int width, int height) {
 super.setSize(new Dimension(width, height));
 }
 };
 }

 @Override
 public FileChooser getFileChooser(ImageFilter filter) {
 return new FreeMindFileDialog(new JFrame(), filter);
 }

 @Override
 public Map getMap() {
 return new MapView() {
 @Override
 public File getFile() {
 return new File(TEST_MAP_RESOURCE_NAME);
 }
 };
 }

 @Override
 public Text getText(String key) {
 return new MessageFormat(TEST_RESOURCE_STRING).format(null);
 }

 @Override
 public Logger getLogger() {
 return new Logger() {
 @Override
 public void finest(String message) {}

 @Override
 public void severe(String message) {}
 };
 }
}