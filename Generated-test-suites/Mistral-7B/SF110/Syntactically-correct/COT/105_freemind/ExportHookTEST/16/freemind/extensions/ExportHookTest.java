package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.resources.Resources;
import freemind.main.FreeMindFrame;
import freemind.main.FreeMindFrameController;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private static final String TEST_IMAGE_TYPE = "png";
 private static final String TEST_MAP_FILE_NAME = "testMap.mm";
 private static final String TEST_OUTPUT_DIRECTORY = "target/test-output";
 private static final String TEST_RESOURCE_DIRECTORY = "src/test/resources";
 private ModeController controller;
 private FreeMindFrame frame;
 private MapController mapController;
 private MapView mapView;
 private ExportHook exportHook;

 @Before
 public void setup() {
 Resources.setInstance(new Resources());
 Resources.getInstance().initResourceBundle();
 frame = new FreeMindFrame();
 controller = new FreeMindFrameController(frame);
 mapController = new MapController(controller);
 mapView = new MapView(mapController);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile_NullType() {
 File result = exportHook.chooseFile(null, "Description", TEST_IMAGE_TYPE);
 assertNull(result);
 }

 @Test
 public void testChooseFile_NullDescription() {
 File result = exportHook.chooseFile("Type", null, TEST_IMAGE_TYPE);
 assertNull(result);
 }

 @Test
 public void testChooseFile_NullNameExtension() {
 File result = exportHook.chooseFile("Type", "Description", null);
 assertNull(result);
 }

 @Test
 public void testChooseFile_NullController() {
 exportHook = new ExportHook();
 File result = exportHook.chooseFile("Type", "Description", TEST_IMAGE_TYPE);
 assertNull(result);
 }

 @Test
 public void testChooseFile_NullMap() {
 File mapFile = new File(TEST_OUTPUT_DIRECTORY, TEST_MAP_FILE_NAME);
 controller.setMap(mapFile);
 File result = exportHook.chooseFile("Type", "Description", TEST_IMAGE_TYPE);
 assertNull(result);
 }

 @Test
 public void testChooseFile_ValidParameters() throws IOException {
 File mapFile = new File(TEST_OUTPUT_DIRECTORY, TEST_MAP_FILE_NAME);
 controller.setMap(mapFile);
 File expectedResult = new File(TEST_OUTPUT_DIRECTORY, TEST_MAP_FILE_NAME.replaceAll("\\.mm$", "." + TEST_IMAGE_TYPE));
 File result = exportHook.chooseFile("Type", "Description", TEST_IMAGE_TYPE);
 assertEquals(expectedResult.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test(expected = NullPointerException.class)
 public void testChooseImageFile_NullController() {
 ExportHook.chooseImageFile(null, "Description", TEST_IMAGE_TYPE, controller);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName() {
 String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName() {
 String result = exportHook.getTranslatableResourceString("resource.name");
 assertNotNull(result);
 }

 @Test
 public void testCreateBufferedImage_NullView() {
 BufferedImage result = exportHook.createBufferedImage();
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_ValidView() {
 BufferedImage expectedResult = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 BufferedImage result = exportHook.createBufferedImage();
 assertNotNull(result);
 assertEquals(expectedResult.getWidth(), result.getWidth());
 assertEquals(expectedResult.getHeight(), result.getHeight());
 }

 @Test(expected = IllegalStateException.class)
 public void testCopyFromResource_NullPrefix() {
 exportHook.copyFromResource(null, "fileName.ext", TEST_OUTPUT_DIRECTORY);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_NonExistingResource() {
 exportHook.copyFromResource("prefix", "nonExistingResource.ext", TEST_OUTPUT_DIRECTORY);
 }

 @Test
 public void testCopyFromResource_ExistingResource() throws IOException {
 URL resource = new URL("classpath:/test/resources/testResource.ext");
 exportHook.copyFromResource("prefix", "testResource.ext", TEST_OUTPUT_DIRECTORY);
 File expectedResult = new File(TEST_OUTPUT_DIRECTORY, "testResource.ext");
 File result = new File(TEST_OUTPUT_DIRECTORY, "prefixtestResource.ext");
 assertTrue(result.exists());
 assertEquals(expectedResult.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDir() {
 exportHook.copyFromFile(null, "fileName.ext", TEST_OUTPUT_DIRECTORY);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_NonExistingFile() {
 exportHook.copyFromFile("dir", "nonExistingFile.ext", TEST_OUTPUT_DIRECTORY);
 }

 @Test
 public void testCopyFromFile_ExistingFile() throws IOException {
 File resourceFile = new File(TEST_RESOURCE_DIRECTORY, "testFile.ext");
 exportHook.copyFromFile(TEST_RESOURCE_DIRECTORY, "testFile.ext", TEST_OUTPUT_DIRECTORY);
 File expectedResult = new File(TEST_OUTPUT_DIRECTORY, "testFile.ext");
 File result = new File(TEST_OUTPUT_DIRECTORY, "testFile.ext");
 assertTrue(result.exists());
 assertEquals(expectedResult.getAbsolutePath(), result.getAbsolutePath());
 }
}