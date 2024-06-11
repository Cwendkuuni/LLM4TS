package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.main.Controller;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExportHookTest {

 private ExportHook hook;
 private MapView view;
 private Controller controller;
 private MapController mapController;

 @Before
 public void setup() {
 view = Mockito.mock(MapView.class);
 controller = Mockito.mock(Controller.class);
 mapController = Mockito.mock(MapController.class);
 hook = new ExportHook() {
 @Override
 public MapView getView() {
 return view;
 }

 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public MapController getMapController() {
 return mapController;
 }
 };
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(hook.chooseFile(null, "", null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(hook.chooseFile("image/png", null, null));
 }

 @Test
 public void testChooseFileNullNameExtension() {
 File chosenFile = hook.chooseFile("image/png", "Description", null);
 assertNotNull(chosenFile);
 }

 @Test
 public void testChooseFileValidFile() throws IOException {
 File mmFile = new File("testMap.mm");
 when(controller.getMap().getFile()).thenReturn(mmFile);
 File chosenFile = hook.chooseFile("image/png", "Description", "png");
 assertEquals("testMap.png", chosenFile.getName());
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(hook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 String resourceName = "%resource_key%";
 when(controller.getText(resourceName)).thenReturn("Resource Key");
 String result = hook.getTranslatableResourceString(resourceName);
 assertEquals("Resource Key", result);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(hook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidView() throws Exception {
 BufferedImage bufferedImage = hook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullPrefix() {
 hook.copyFromResource(null, "fileName.png", "");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullFileName() {
 hook.copyFromResource("prefix", null, "");
 }

 @Test
 public void testCopyFromResourceValidPrefixFileNameDestinationDirectory() throws Exception {
 File destinationDirectory = new File("test_destination");
 hook.copyFromResource("prefix", "fileName.png", destinationDirectory.getAbsolutePath());
 File expectedFile = new File(destinationDirectory, "fileName.png");
 assertTrue(expectedFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileNullDir() throws Exception {
 hook.copyFromFile(null, "fileName.png", "");
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileNullFileName() throws Exception {
 hook.copyFromFile("dir", null, "");
 }

 @Test
 public void testCopyFromFileValidDirFileNameDestinationDirectory() throws Exception {
 File destinationDirectory = new File("test_destination");
 hook.copyFromFile("dir/fileName.png", "fileName.png", destinationDirectory.getAbsolutePath());
 File expectedFile = new File(destinationDirectory, "fileName.png");
 assertTrue(expectedFile.exists());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testImageFilterAcceptNullFile() {
 FileFilter filter = new ExportHook.ImageFilter(null, null);
 assertFalse(filter.accept(null));
 }

 @Test
 public void testImageFilterAcceptValidFile() {
 FileFilter filter = new ExportHook.ImageFilter("image/png", null);
 File file = new File("testFile.png");
 assertTrue(filter.accept(file));
 }

 @Test
 public void testImageFilterGetDescriptionNullType() {
 FileFilter filter = new ExportHook.ImageFilter(null, "Description");
 assertEquals("Description", filter.getDescription());
 }

 @Test
 public void testImageFilterGetDescriptionValidType() {
 FileFilter filter = new ExportHook.ImageFilter("image/png", "Description");
 assertEquals("Description", filter.getDescription());
 assertEquals("Image files (*.png)", filter.getDescription());
 }
}