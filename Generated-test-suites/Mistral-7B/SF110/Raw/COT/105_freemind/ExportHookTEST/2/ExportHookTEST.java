package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.Logger;
import freemind.main.TextUtils;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeControllerFrame;
import freemind.view.mindmapview.View;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapController mapController;
 private MapView mapView;
 private BufferedImage bufferedImage;
 private Logger logger;
 private Resources resources;
 private ModeControllerFrame controllerFrame;
 private FileChooser fileChooser;

 @Before
 public void setup() {
  exportHook = new ExportHook();
  modeController = Mockito.mock(ModeController.class);
  mapController = Mockito.mock(MapController.class);
  mapView = Mockito.mock(MapView.class);
  bufferedImage = Mockito.mock(BufferedImage.class);
  logger = Mockito.mock(Logger.class);
  resources = Mockito.mock(Resources.class);
  controllerFrame = Mockito.mock(ModeControllerFrame.class);
  fileChooser = Mockito.mock(JFileChooser.class);
  exportHook.setController(modeController);
  exportHook.setView(mapView);
  exportHook.setLogger(logger);
  exportHook.setResources(resources);
  exportHook.setFrame(controllerFrame);
 }

 @Test
 public void testChooseFile_NullType() {
  when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);
  assertNull(exportHook.chooseFile(null, "Description", null));
  verify(fileChooser).setSelectedFile(null);
 }

 @Test
 public void testChooseFile_ValidType() {
  when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);
  final String type = "png";
  final String description = "Export PNG image";
  when(fileChooser.showSaveDialog(controllerFrame)).thenReturn(JFileChooser.APPROVE_OPTION);
  final File chosenFile = new File("path/to/file.png");
  when(fileChooser.getSelectedFile()).thenReturn(chosenFile);
  when(controllerFrame.getText("file_already_exists")).thenReturn("File already exists. Do you want to replace it?");
  when(JOptionPane.showConfirmDialog(controllerFrame, anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);
  final File result = exportHook.chooseFile(type, description, null);
  assertEquals(chosenFile, result);
 }

 @Test
 public void testChooseFile_InvalidFileExtension() {
  when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);
  final String type = "png";
  final String description = "Export PNG image";
  when(fileChooser.showSaveDialog(controllerFrame)).thenReturn(JFileChooser.APPROVE_OPTION);
  final File chosenFile = new File("path/to/file.jpg");
  when(fileChooser.getSelectedFile()).thenReturn(chosenFile);
  when(controllerFrame.getText("file_already_exists")).thenReturn("File already exists. Do you want to replace it?");
  when(JOptionPane.showConfirmDialog(controllerFrame, anyString(), anyString(), 0)).thenReturn(JOptionPane.NO_OPTION);
  when(fileChooser.getSelectedFile()).thenReturn(null);
  assertNull(exportHook.chooseFile(type, description, null));
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName() {
  final String resourceName = "%resourceKey%";
  final String result = exportHook.getTranslatableResourceString(resourceName);
  assertEquals("resourceKey", result);
 }

 @Test
 public void testGetTranslatableResourceString_InvalidResourceName() {
  final String resourceName = "InvalidResourceName";
  final String result = exportHook.getTranslatableResourceString(resourceName);
  assertNull(result);
 }

 @Test
 public void testCreateBufferedImage() throws IOException {
  when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
  when(mapView.createImage(100, 100)).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
  when(mapView.print(any(Graphics.class))).thenReturn(true);
  when(mapView.getWidth()).thenReturn(100);
  when(mapView.getHeight()).thenReturn(100);
  when(mapView.preparePrinting()).thenReturn(true);
  when(mapView.endPrinting()).thenReturn(true);
  final BufferedImage result = exportHook.createBufferedImage();
  assertEquals(bufferedImage, result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
  when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
  when(mapView.createImage(100, 100)).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
  when(mapView.print(any(Graphics.class))).thenReturn(true);
  when(mapView.getWidth()).thenReturn(100);
  when(mapView.getHeight()).thenReturn(100);
  when(mapView.preparePrinting()).thenReturn(true);
  when(mapView.endPrinting()).thenReturn(true);
  exportHook.setView(null);
  exportHook.createBufferedImage();
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName() throws IOException {
  final String prefix = "prefix";
  final String fileName = "test.txt";
  final String destinationDirectory = "path/to/destination/directory";
  when(getResource(prefix + fileName)).thenReturn(new URL("file:/path/to/resource/test.txt"));
  exportHook.copyFromResource(prefix, fileName, destinationDirectory);
  verify(logger).finest("searching for prefixtest.txt");
  verify(resources).logException(isA(IOException.class));
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullPrefixOrFileName() {
  final String destinationDirectory = "path/to/destination/directory";
  exportHook.copyFromResource(null, null, destinationDirectory);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDirOrFileName() {
  final String dir = null;
  final String fileName = "test.txt";
  final String destinationDirectory = "path/to/destination/directory";
  exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName() throws IOException {
  final String dir = "path/to/source/directory";
  final String fileName = "test.txt";
  final String destinationDirectory = "path/to/destination/directory";
  when(new File(dir, fileName).exists()).thenReturn(true);
  exportHook.copyFromFile(dir, fileName, destinationDirectory);
  verify(logger).finest("searching for path/to/source/directory/test.txt");
  verify(resources).logException(isA(IOException.class));
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_InvalidFile() throws IOException {
  final String dir = "path/to/source/directory";
  final String fileName = "test.txt";
  final String destinationDirectory = "path/to/destination/directory";
  when(new File(dir, fileName).exists()).thenReturn(false);
  exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

}