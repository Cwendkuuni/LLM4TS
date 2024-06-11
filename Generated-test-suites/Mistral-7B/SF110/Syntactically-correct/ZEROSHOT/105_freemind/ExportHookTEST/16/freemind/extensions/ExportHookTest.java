package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import freemind.controller.Controller;
import freemind.controller.ControllerFactory;
import freemind.controller.actions.export.ExportMapImageAction;
import freemind.extensions.ExportHook;
import freemind.mode.MapController;
import freemind.modes.MapMode;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private Controller controller;
 private ExportHook exportHook;
 private MapView mapView;
 private MapController mapController;

 @Before
 public void setUp() {
 controller = ControllerFactory.createController();
 exportHook = new ExportHook();
 mapView = new MapView(controller, null);
 mapController = (MapController) controller.getMode(MapMode.class);
 }

 @After
 public void tearDown() {
 controller = null;
 exportHook = null;
 mapView = null;
 mapController = null;
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "Export as PNG";
 File expectedFile = new File("test/exportedFile.png");
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals("Wrong chosen file", expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_whenNullMapFile_thenReturnNull() {
 String type = "png";
 String description = "Export as PNG";
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertNull("Should return null when mapFile is null", chosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull("Should create BufferedImage", bufferedImage);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%file_already_exists";
 String expectedValue = "File already exists: ";
 String translatableValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals("Get wrong translatable value", expectedValue, translatableValue);
 }

 @Test
 public void testGetTranslatableResourceString_whenNullResourceName_thenReturnNull() {
 String resourceName = null;
 String translatableValue = exportHook.getTranslatableResourceString(resourceName);
 assertNull("Should return null when resourceName is null", translatableValue);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "prefix/";
 String fileName = "testFile.txt";
 String destinationDirectory = "test/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File file = new File(destinationDirectory + "/" + fileName);
 assertTrue("File was not copied", file.exists());
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "testDir/";
 String fileName = "testFile.txt";
 String destinationDirectory = "test/";
 File fileToCopy = new File(dir + fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File copiedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue("File was not copied", copiedFile.exists());
 }

 @Test
 public void testCreateImageFile() throws IOException {
 String type = "png";
 String description = "Export as PNG";
 File expectedFile = new File("test/exportedFile.png");
 ExportMapImageAction exportMapImageAction = new ExportMapImageAction(controller, mapView, mapController, type);
 exportMapImageAction.actionPerformed(null);
 File chosenFile = exportHook.chooseImageFile(type, description, null, controller);
 assertSame("Wrong chosen file", expectedFile, chosenFile);
 }
}