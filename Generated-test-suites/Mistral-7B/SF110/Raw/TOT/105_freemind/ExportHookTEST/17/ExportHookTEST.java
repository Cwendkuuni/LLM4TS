package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.controller.FrameController;
import freemind.controller.ModeController;
import freemind.controller.View;
import freemind.model.MapModel;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 @Mock
 private Controller controller;
 @Mock
 private FrameController frameController;
 @Mock
 private MapModel mapModel;
 @Mock
 private View view;
 @InjectMocks
 private ExportHook exportHook;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile_ValidFileExtension() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = null;
 final File expectedChosenFile = new File("path/to/file.png");
 final ArgumentCaptor<File> chosenFileArgumentCaptor = ArgumentCaptor.forClass(File.class);
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite?");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(frameController);
 when(frameController.showSaveDialog(eq(controller.getFrame().getContentPane()), eq(expectedChosenFile))).thenReturn(JOptionPane.YES_OPTION);

 exportHook.chooseFile(type, description, nameExtension);
 verify(controller).getFileChooser(argumentCaptor.capture(ImageFilter.class));
 File chosenFile = chosenFileArgumentCaptor.getValue();
 assertEquals(expectedChosenFile, chosenFile);
 }

 @Test
 public void testChooseFile_InvalidFileExtension() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = null;
 final File expectedChosenFile = new File("path/to/file.jpg");
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite?");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(frameController);
 when(frameController.showSaveDialog(eq(controller.getFrame().getContentPane()), eq(expectedChosenFile))).thenReturn(JOptionPane.YES_OPTION);
 when(controller.getMap()).thenReturn(mapModel);
 when(mapModel.getFile()).thenReturn(new File("path/to/map_file.mm"));

 exportHook.chooseFile(type, description, nameExtension);
 verify(controller).getFileChooser(argumentCaptor.capture(ImageFilter.class));
 File chosenFile = chosenFileArgumentCaptor.getValue();
 assertEquals(new File("path/to/file.png"), chosenFile);
 }

 @Test
 public void testChooseFile_UserCancelsDialog() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = null;
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite?");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(frameController);
 when(frameController.showSaveDialog(eq(controller.getFrame().getContentPane()), anyObject())).thenReturn(JOptionPane.CANCEL_OPTION);

 exportHook.chooseFile(type, description, nameExtension);
 verify(controller, never()).getFileChooser(any(ImageFilter.class));
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resource_key%";
 final String expectedValue = "Expected Value";
 when(controller.getText(resourceName)).thenReturn("Expected Value");

 assertEquals(expectedValue, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedBufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 when(controller.getView()).thenReturn(view);
 when(view.preparePrinting()).thenReturn(true);
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedBufferedImage);

 exportHook.createBufferedImage();
 verify(controller.getView(), times(1)).preparePrinting();
 verify(controller.getView(), times(1)).createImage(anyInt(), anyInt());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 exportHook.createBufferedImage();
 verify(controller.getView(), never()).preparePrinting();
 verify(controller.getView(), never()).createImage(anyInt(), anyInt());
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "prefix/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";
 when(controller.getLogger()).thenReturn(mock(Logger.class));

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(controller.getLogger()).finest("searching for prefixfile.txt");
 verify(controller, times(1)).getResource(prefix + fileName);
 }

 @Test
 public void testCopyFromFile_ValidFileExists() {
 final String dir = "path/to/dir/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(controller.getLogger()).finest("searching for path/to/dir/file.txt");
 verify(controller, times(1)).new FileInputStream(new File(dir, fileName));
 verify(controller, times(1)).new FileOutputStream(new File(destinationDirectory, fileName));
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_FileNotFound() {
 final String dir = "path/to/dir/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(controller.getLogger()).finest("searching for path/to/dir/file.txt");
 verify(controller, times(1)).new FileInputStream(new File(dir, fileName));
 }

 @Test
 public void testCopyFromFile_UserCancelsDialog() {
 final String dir = "path/to/dir/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";
 when(controller.getLogger()).thenReturn(mock(Logger.class));
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite?");
 when(controller.showSaveDialog(eq(controller.getFrame().getContentPane()), eq(new File(destinationDirectory, fileName)))).thenReturn(JOptionPane.CANCEL_OPTION);

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(controller.getLogger(), never()).finest("copying file from " + dir + fileName + " to " + destinationDirectory + "/" + fileName);
 }
}