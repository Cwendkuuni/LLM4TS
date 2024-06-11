package freemind.extensions.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSaveDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.extensions.test.util.TestLogger;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeControllerImpl;

public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private ModeController modeController;
 @Mock
 private MapView mapView;
 @Mock
 private TestLogger logger;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return modeController;
 }

 @Override
 public Logger getLogger() {
 return logger;
 }

 @Override
 public MapView getView() {
 return mapView;
 }
 };
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final File expectedFile = new File("path/to/output/file.png");
 final FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(modeController.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any())).thenReturn(JFileChooser.APPROVE_OPTION);
 when(mapView.getFile()).thenReturn(new File("path/to/mindmap.mm"));

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_FileAlreadyExists_UserCancels() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final File expectedFile = new File("path/to/output/file.png");
 final FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(modeController.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(mapView.getFile()).thenReturn(new File("path/to/mindmap.mm"));

 final String overwriteText = "The file already exists, do you want to replace it?";
 when(controller.getText("file_already_exists")).thenReturn(overwriteText);
 when(chooser.showConfirmDialog(any(), anyString(), anyInt(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertNull(actualFile);
 }

 @Test
 public void testChooseFile_FileAlreadyExists_UserConfirms() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final File expectedFile = new File("path/to/output/file.png");
 final FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(modeController.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(mapView.getFile()).thenReturn(new File("path/to/mindmap.mm"));

 final String overwriteText = "The file already exists, do you want to replace it?";
 when(controller.getText("file_already_exists")).thenReturn(overwriteText);
 when(chooser.showConfirmDialog(any(), anyString(), anyInt(), anyInt())).thenReturn(JOptionPane.YES_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%TEST%";
 final String expectedValue = "Test";

 final String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 exportHook.createBufferedImage();
 verify(logger, times(1)).severe("view is null");
 }

 @Test
 public void testCreateBufferedImage_ValidView() throws IOException {
 final BufferedImage expectedImage = ImageIO.read(new File("path/to/test/image.png"));
 when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

 final BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "freemind/extensions/";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/output/";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(logger, times(1)).finest("searching for freemind/extensions/test.txt");
 verifyNoMoreInteractions(logger);
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_FileNotFound() {
 final String prefix = "freemind/extensions/";
 final String fileName = "nonexistent.txt";
 final String destinationDirectory = "path/to/output/";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(logger, times(1)).severe("Cannot find resource: freemind/extensions/nonexistent.txt");
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "path/to/input/";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/output/";

 final File inputFile = new File(dir, "test.txt");
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(logger, times(1)).finest("searching for path/to/input/test.txt");
 verify(logger, never()).severe("Cannot find resource:");
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_FileNotFound() {
 final String dir = "path/to/input/";
 final String fileName = "nonexistent.txt";
 final String destinationDirectory = "path/to/output/";

 final File inputFile = new File(dir, fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(logger, times(1)).severe("File not found or could not be copied. Was earching for path/to/input/nonexistent.txt and should go to path/to/output/");
 }
}