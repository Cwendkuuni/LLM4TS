package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.Controller;
import freemind.main.ControllerFactory;
import freemind.main.FreeMindLogger;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private Controller controller;
 private FreeMindLogger logger;

 @Before
 public void setUp() {
 modeController = ControllerFactory.createModeController();
 controller = modeController.getController();
 exportHook = new ExportHook();
 exportHook.setController(modeController);
 logger = controller.getLogger();
 }

 @After
 public void tearDown() {
 modeController.dispose();
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "Export as PNG";
 String nameExtension = null;
 File expectedFile = new File("expected.png");
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertNotNull(actualFile);
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testChooseFileWithProposedName() {
 String type = "png";
 String description = "Export as PNG";
 String nameExtension = null;
 File mmFile = new File("map.mm");
 File expectedFile = new File("map_export.png");
 File actualFile = exportHook.chooseFile(type, description, nameExtension, modeController);
 assertNotNull(actualFile);
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testChooseFileWithCustomExtension() {
 String type = "svg";
 String description = "Export as SVG";
 String nameExtension = "export";
 File expectedFile = new File("map_export.svg");
 File actualFile = exportHook.chooseFile(type, description, nameExtension, modeController);
 assertNotNull(actualFile);
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resources.export.export_image_title";
 String expectedValue = "Export image as";
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreateBufferedImage() throws IOException {
 MapView view = modeController.getMapController().getMapView();
 exportHook.createBufferedImage();
 BufferedImage actualImage = exportHook.createBufferedImage();
 BufferedImage expectedImage = view.createImage(view.getWidth(), view.getHeight());
 assertSame(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource() {
 String prefix = "resources/export/";
 String fileName = "export.png";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResourceWithMissingFile() {
 String prefix = "non_existent_resources/export/";
 String fileName = "export.png";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 fail("Copy from resource with missing file should throw FileNotFoundException");
 }

 @Test
 public void testCopyFromFile() {
 String dir = "src/test/resources/freemind/extensions/export/";
 String fileName = "export.png";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileWithMissingFile() {
 String dir = "src/test/resources/freemind/extensions/export/non_existent/";
 String fileName = "export.png";
 String destinationDirectory = "target/test-classes/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 fail("Copy from file with missing file should throw FileNotFoundException");
 }
}