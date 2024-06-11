package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.controller.Controller;
import freemind.controller.MapController;
import freemind.model.MapModel;
import freemind.view.MindMapView;
import freemind.view.mindmapview.MapView;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private MapController controller;
 @Mock
 private MapView view;
 @Mock
 private MapModel mapModel;
 @Mock
 private JOptionPane optionPane;
 @Mock
 private FileChooserFactory fileChooserFactory;
 @Mock
 private FreeMindFileDialog fileChooser;

 @Before
 public void setUp() {
 exportHook = new ExportHook() {
 @Override
 public MapView getView() {
 return view;
 }

 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public JOptionPane getTextUI() {
 return optionPane;
 }

 @Override
 public FileChooserFactory getFileChooserFactory() {
 return fileChooserFactory;
 }
 };

 Mockito.when(controller.getFrame()).thenReturn(Mockito.mock(Frame.class));
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(fileChooser);
 }

 @Test
 public void testChooseFile_whenValidFileIsSelected_shouldReturnFile() {
 final String type = "png";
 final String description = "PNG Image";
 final String nameExtension = "png";
 final File expectedFile = new File("path/to/test/file.png");

 Mockito.when(fileChooser.showSaveDialog(Mockito.any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
 Mockito.when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals("Expected file to be equal to the returned file", expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_whenInvalidFileIsSelected_shouldReturnNull() {
 final String type = "png";
 final String description = "PNG Image";
 final String nameExtension = "png";

 Mockito.when(fileChooser.showSaveDialog(Mockito.any(Container.class))).thenReturn(JOptionPane.CANCEL_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertNull("Expected null to be returned", actualFile);
 }

 @Test
 public void testChooseFile_whenFileAlreadyExists_shouldReturnFileAfterConfirmingOverwrite() {
 final String type = "png";
 final String description = "PNG Image";
 final String nameExtension = "png";
 final String fileName = "existingFile.png";
 final String filePath = "path/to/existing/file.png";
 final String proposedName = "newFile.png";
 final File expectedFile = new File(filePath + "/" + proposedName);

 Mockito.when(fileChooser.getSelectedFile()).thenReturn(new File(filePath + "/" + fileName));
 Mockito.when(optionPane.showConfirmDialog(Mockito.any(Container.class), Mockito.anyString(), Mockito.anyString(), Mockito.eq(0))).thenReturn(JOptionPane.YES_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals("Expected file to be equal to the returned file", expectedFile, actualFile);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceString_whenNullResourceNameIsPassed_shouldThrowNullPointerException() {
 exportHook.getTranslatableResourceString(null);
 }

 @Test
 public void testCreateBufferedImage_whenViewIsNotNull_shouldReturnBufferedImage() {
 final BufferedImage expectedImage = ImageIO.read(new File("path/to/test/image.jpg"));

 Mockito.when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 Mockito.when(view.createImage(100, 100)).thenReturn(expectedImage);

 final BufferedImage actualImage = exportHook.createBufferedImage();

 assertSame("Expected buffered image to be equal to the returned buffered image", expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_whenViewIsNull_shouldReturnNull() {
 final BufferedImage actualImage = exportHook.createBufferedImage();

 assertNull("Expected null to be returned", actualImage);
 }

 @Test
 public void testCopyFromResource_whenResourceExists_shouldCopyFile() throws IOException {
 final String prefix = "prefix";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue("Expected file to exist", expectedFile.exists());
 }

 @Test
 public void testCopyFromResource_whenResourceDoesNotExist_shouldNotDoAnything() {
 final String prefix = "prefix";
 final String fileName = "nonExistentResource.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertFalse("Expected file to not exist", expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_whenFileExists_shouldCopyFile() throws IOException {
 final String dir = "path/to/source";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue("Expected file to exist", expectedFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_whenFileDoesNotExist_shouldThrowFileNotFoundException() {
 final String dir = "path/to/nonexistent/file";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testGetImageFilter_whenValidTypeIsPassed_shouldReturnImageFilter() {
 final String type = "png";

 FileFilter actualFilter = exportHook.getImageFilter(type);

 assertEquals("Expected image filter to have the correct type", type, actualFilter.getDescription());
 }

 @Test
 public void testGetImageFilter_whenInvalidTypeIsPassed_shouldReturnNull() {
 final String type = "invalid";

 FileFilter actualFilter = exportHook.getImageFilter(type);

 assertNull("Expected null to be returned", actualFilter);
 }
}