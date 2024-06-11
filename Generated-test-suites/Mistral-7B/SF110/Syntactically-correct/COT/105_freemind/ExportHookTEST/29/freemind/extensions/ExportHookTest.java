package freemind.extensions.test;

import static org.junit.Assert.*;
import static freemind.extensions.ExportHook.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.ModeController;
import freemind.main.FreeMind;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.*;

public class ExportHookTest {

 private static final String TEST_FILE_NAME = "testFile";
 private static final String TEST_TYPE = "png";
 private static final String TEST_DESCRIPTION = "Test Image";
 private static final String TEST_DESTINATION_DIRECTORY = "target/test-classes/export";

 @org.junit.BeforeClass
 public static void setup() {
 FreeMind.main(new String[0]); // Start Freemind
 }

 @org.junit.AfterClass
 public static void teardown() {
 // Shutdown Freemind
 FreeMind.quit();
 }

 // Test chooseFile method
 @org.junit.Test
 public void testChooseFile() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = exportHook.chooseFile(TEST_TYPE, TEST_DESCRIPTION, TEST_TYPE);
 assertNotNull(file);
 }

 // Test chooseFile with null type
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testChooseFileNullType() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = exportHook.chooseFile(null, TEST_DESCRIPTION, TEST_TYPE);
 fail("Expected IllegalArgumentException");
 }

 // Test chooseFile with null description
 @org.junit.Test
 public void testChooseFileNullDescription() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = exportHook.chooseFile(TEST_TYPE, null, TEST_TYPE);
 assertNotNull(file);
 }

 // Test chooseFile with null type and description
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testChooseFileNullTypeAndDescription() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = exportHook.chooseFile(null, null, TEST_TYPE);
 fail("Expected IllegalArgumentException");
 }

 // Test chooseImageFile method
 @org.junit.Test
 public void testChooseImageFile() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = ExportHook.chooseImageFile(TEST_TYPE, TEST_DESCRIPTION, TEST_TYPE, controller);
 assertNotNull(file);
 }

 // Test chooseImageFile with null type
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testChooseImageFileNullType() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = ExportHook.chooseImageFile(null, TEST_DESCRIPTION, TEST_TYPE, controller);
 fail("Expected IllegalArgumentException");
 }

 // Test chooseImageFile with null description
 @org.junit.Test
 public void testChooseImageFileNullDescription() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = ExportHook.chooseImageFile(TEST_TYPE, null, TEST_TYPE, controller);
 assertNotNull(file);
 }

 // Test chooseImageFile with null type and description
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testChooseImageFileNullTypeAndDescription() {
 ModeController controller = Freemind.getController();
 ExportHook exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 File file = ExportHook.chooseImageFile(null, null, TEST_TYPE, controller);
 fail("Expected IllegalArgumentException");
 }

 // Test getTranslatableResourceString method
 @org.junit.Test
 public void testGetTranslatableResourceString() {
 ExportHook exportHook = new ExportHook();
 String resourceString = exportHook.getTranslatableResourceString("%test%");
 assertEquals("test", exportHook.getController().getText(resourceString.substring(1)));
 }

 // Test getTranslatableResourceString with null resourceName
 @org.junit.Test
 public void testGetTranslatableResourceStringNullResourceName() {
 ExportHook exportHook = new ExportHook();
 String resourceString = exportHook.getTranslatableResourceString(null);
 assertNull(resourceString);
 }

 // Test createBufferedImage method
 @org.junit.Test
 public void testCreateBufferedImage() {
 ExportHook exportHook = new ExportHook();
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 // Test createBufferedImage with null view
 @org.junit.Test(expected = NullPointerException.class)
 public void testCreateBufferedImageNullView() {
 ExportHook exportHook = new ExportHook() {
 @Override
 MapView getView() {
 return null;
 }
 };
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 fail("Expected NullPointerException");
 }

 // Test copyFromResource method
 @org.junit.Test
 public void testCopyFromResource() throws IOException {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource("prefix", "fileName", TEST_DESTINATION_DIRECTORY);
 File file = new File(TEST_DESTINATION_DIRECTORY, "fileName");
 assertTrue(file.exists());
 }

 // Test copyFromResource with null prefix
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromResourceNullPrefix() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource(null, "fileName", TEST_DESTINATION_DIRECTORY);
 fail("Expected IllegalArgumentException");
 }

 // Test copyFromResource with null fileName
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromResourceNullFileName() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource("prefix", null, TEST_DESTINATION_DIRECTORY);
 fail("Expected IllegalArgumentException");
 }

 // Test copyFromResource with null destinationDirectory
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromResourceNullDestinationDirectory() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromResource("prefix", "fileName", null);
 fail("Expected IllegalArgumentException");
 }

 // Test copyFromFile method
 @org.junit.Test
 public void testCopyFromFile() throws IOException {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile("dir", "fileName", TEST_DESTINATION_DIRECTORY);
 File file = new File(TEST_DESTINATION_DIRECTORY, "fileName");
 assertTrue(file.exists());
 }

 // Test copyFromFile with null dir
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromFileNullDir() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile(null, "fileName", TEST_DESTINATION_DIRECTORY);
 fail("Expected IllegalArgumentException");
 }

 // Test copyFromFile with null fileName
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromFileNullFileName() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile("dir", null, TEST_DESTINATION_DIRECTORY);
 fail("Expected IllegalArgumentException");
 }

 // Test copyFromFile with null destinationDirectory
 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testCopyFromFileNullDestinationDirectory() {
 ExportHook exportHook = new ExportHook();
 exportHook.copyFromFile("dir", "fileName", null);
 fail("Expected IllegalArgumentException");
 }
}