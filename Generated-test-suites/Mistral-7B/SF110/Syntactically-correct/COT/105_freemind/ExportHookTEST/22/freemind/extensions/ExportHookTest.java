package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.ResourceManager;
import freemind.main.FreeMindController;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MindMapView;
import junit.framework.JFileDelegate;
import junit.framework.TestCase;
import junit.runner.JUnitCore;

@RunWith(JUnitCore.class)
public class ExportHookTest extends TestCase {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;
 private FreeMindController freeMindController;

 @Before
 public void setUp() {
  exportHook = new ExportHook();
  modeController = Mockito.mock(ModeController.class);
  mapView = Mockito.mock(MapView.class);
  FreeMindController freeMindController = Mockito.mock(FreeMindController.class);
  Mockito.when(modeController.getFrame()).thenReturn(freeMindController);
  Mockito.when(modeController.getController()).thenReturn(modeController);
  Mockito.when(modeController.getText(Mockito.anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));
  Mockito.when(modeController.getMap()).thenReturn(Mockito.mock(MindMapView.class));
  Mockito.when(modeController.getView()).thenReturn(mapView);
  exportHook.setController(modeController);
 }

 @Test
 public void testChooseFileNullType() {
  assertNull(exportHook.chooseFile(null, "Description", null));
 }

 @Test
 public void testChooseFileNullDescription() {
  assertNull(exportHook.chooseFile("type", null, null));
 }

 @Test
 public void testChooseFileNullNameExtension() {
  assertNull(exportHook.chooseFile("type", "Description", null));
 }

 @Test
 public void testChooseFileValidArguments() {
  File expectedFile = new File("path/to/file.type");
  URL url = Mockito.mock(URL.class);
  Mockito.when(url.openStream()).thenReturn(Mockito.mock(InputStream.class));
  Mockito.when(modeController.getFileChooser(Mockito.isInstance(ImageFilter.class))).thenReturn(url);
  Mockito.when(modeController.getMap()).thenReturn(Mockito.mock(MindMapView.class));
  Mockito.when(modeController.getMap().getFile()).thenReturn(new File("path/to/mindmap.mm"));
  File actualFile = exportHook.chooseFile("type", "Description", "type");
  assertEquals(expectedFile, actualFile);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFileInvalidExtension() {
  File chosenFile = new File("path/to/file.png");
  exportHook.chooseFile("type", "Description", "type");
  assertNotSame(chosenFile, exportHook.chooseFile("type", "Description", "type"));
 }

 @Test
 public void testChooseFileExistingFile() {
  File chosenFile = new File("path/to/file.png");
  File existingFile = new File("path/to/file.png");
  Mockito.when(modeController.getFileChooser(Mockito.isInstance(ImageFilter.class))).thenReturn(Mockito.mock(FileDialog.class));
  Mockito.when(modeController.getFileChooser(Mockito.isInstance(ImageFilter.class)).showSaveDialog(Mockito.any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
  Mockito.when(modeController.getFileChooser(Mockito.isInstance(ImageFilter.class)).getSelectedFile()).thenReturn(chosenFile);
  Mockito.when(chosenFile.exists()).thenReturn(true);
  Mockito.when(modeController.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
  JOptionPane.showConfirmDialog(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), JOptionPane.YES_NO_OPTION);
  Mockito.verify(modeController, Mockito.times(1)).getText("file_already_exists");
  assertTrue(exportHook.chooseFile("type", "Description", "type").exists());
 }

 @Test
 public void testCreateBufferedImageNullView() {
  assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidArguments() {
  BufferedImage expectedBufferedImage = Mockito.mock(BufferedImage.class);
  Mockito.when(mapView.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedBufferedImage);
  BufferedImage actualBufferedImage = exportHook.createBufferedImage();
  assertEquals(expectedBufferedImage, actualBufferedImage);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
  assertNull(exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidArguments() {
  String expectedTranslatableResourceString = "Expected Translatable Resource String";
  String actualTranslatableResourceString = exportHook.getTranslatableResourceString("ResourceName");
  assertEquals(expectedTranslatableResourceString, actualTranslatableResourceString);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
  String destinationDirectory = "destinationDirectory";
  String fileName = "fileName.png";
  exportHook.copyFromResource(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromResourceValidArguments() {
  String prefix = "prefix/";
  String fileName = "fileName.png";
  String destinationDirectory = "destinationDirectory";
  File expectedFile = new File("prefix/fileName.png");
  URL resource = Mockito.mock(URL.class);
  Mockito.when(resource.openStream()).thenReturn(Mockito.mock(InputStream.class));
  Mockito.when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
  File actualFile = exportHook.copyFromResource(prefix, fileName, destinationDirectory);
  assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testCopyFromFileNullDir() {
  String fileName = "fileName.png";
  String destinationDirectory = "destinationDirectory";
  exportHook.copyFromFile(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFileValidArguments() {
  String dir = "prefix/";
  String fileName = "fileName.png";
  String destinationDirectory = "destinationDirectory";
  File expectedFile = new File("prefix/fileName.png");
  File resource = new File("prefix/fileName.png");
  File actualFile = exportHook.copyFromFile(dir, fileName, destinationDirectory);
  assertEquals(expectedFile, actualFile);
 }
}