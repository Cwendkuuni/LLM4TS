package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSaveDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.controller.ControllerFactory;
import freemind.controller.ModeController;
import freemind.controller.View;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.MindMapDocument;
import freemind.modes.PrintMode;
import freemind.view.MindMapView;
import freemind.view.map.MapView;
import freemind.view.mindmapview.MindMapPrintView;

public class ExportHookTest {

 private Controller controller;
 private ExportHook exportHook;
 private MapView view;
 private MindMapDocument document;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 controller = ControllerFactory.createController();
 exportHook = new ExportHook();
 exportHook.setController(controller);
 view = mock(MapView.class);
 document = mock(MindMapDocument.class);
 when(controller.getView()).thenReturn(view);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(view.createImage(anyInt(), anyInt())).thenReturn(null);
 when(controller.getText(anyString())).thenReturn("Test Text");
 }

 @Test
 public void testChooseFile_ImageFile_Success() {
 String type = "png";
 String description = "PNG Images";
 File expectedFile = new File("test.png");
 FileFilter imageFilter = new ExportHook.ImageFilter(type, description);
 FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(controller.getFileChooser(imageFilter)).thenReturn(chooser);
 MindMapDocument mockDocument = mock(MindMapDocument.class);
 when(controller.getMap()).thenReturn(mockDocument);
 String proposedName = "test";
 when(mockDocument.getFile()).thenReturn(new File("test.mm"));
 when(chooser.showSaveDialog(any(JFrame.class.getClass()))).thenReturn(JOptionPane.OK_OPTION);

 File actualFile = exportHook.chooseFile(type, description, null);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_ImageFile_Error_FileAlreadyExists() {
 String type = "png";
 String description = "PNG Images";
 File expectedFile = new File("test.png");
 File actualFile = new File("test.png");
 FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(controller.getFileChooser(imageFilter())).thenReturn(chooser);
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(chooser.showConfirmDialog(any(JFrame.class.getClass()), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.NO_OPTION);

 File actualFile2 = exportHook.chooseFile(type, description, null);
 assertNull(actualFile2);
 }

 @Test
 public void testChooseFile_ImageFile_Error_WrongType() {
 String type = "png";
 String description = "PNG Images";
 File expectedFile = new File("test.png");
 File actualFile = new File("test.jpg");
 FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(controller.getFileChooser(imageFilter())).thenReturn(chooser);
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(chooser.getSelectedFile()).thenReturn(actualFile);
 when(chooser.showConfirmDialog(any(JFrame.class.getClass()), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.YES_OPTION);

 File actualFile2 = exportHook.chooseFile(type, description, null);
 assertEquals(expectedFile, actualFile2);
 }

 @Test
 public void testGetTranslatableResourceString_Success() {
 String resourceName = "%test";
 String expectedValue = "Test Text";
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetTranslatableResourceString_Failure() {
 String resourceName = "%test";
 String expectedValue = null;
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreateBufferedImage_Success() {
 BufferedImage expectedImage = ImageIO.read(getClass().getResource("/test.png"));
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_Failure() {
 BufferedImage expectedImage = null;
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource_Success() {
 String prefix = "freemind/extensions/";
 String fileName = "test.png";
 String destinationDirectory = "test_directory";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromResource_Failure() {
 String prefix = "freemind/extensions/";
 String fileName = "test.png";
 String destinationDirectory = "test_directory";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertFalse(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_Success() {
 String dir = "test_directory";
 String fileName = "test.png";
 String destinationDirectory = "test_directory";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_Failure() {
 String dir = "test_directory";
 String fileName = "test.png";
 String destinationDirectory = "test_directory";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertFalse(expectedFile.exists());
 }

 @Test
 public void testChooseFile_NullMap() {
 String type = "png";
 String description = "PNG Images";
 FileFilter imageFilter = new ExportHook.ImageFilter(type, description);
 FileSaveDialog chooser = mock(FileSaveDialog.class);
 when(controller.getFileChooser(imageFilter)).thenReturn(chooser);
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(chooser.showSaveDialog(any(JFrame.class.getClass()))).thenReturn(JOptionPane.OK_OPTION);

 MindMapDocument mockDocument = null;
 when(controller.getMap()).thenReturn(mockDocument);

 File actualFile = exportHook.chooseFile(type, description, null);
 assertNull(actualFile);
 }

 @Test
 public void testCreateBufferedImage_NullView() {
 PrintMode printMode = mock(PrintMode.class);
 when(controller.getView()).thenReturn(null);
 when(controller.getMode(PrintMode.class)).thenReturn(printMode);

 BufferedImage actualImage = exportHook.createBufferedImage();
 assertNull(actualImage);
 }
}