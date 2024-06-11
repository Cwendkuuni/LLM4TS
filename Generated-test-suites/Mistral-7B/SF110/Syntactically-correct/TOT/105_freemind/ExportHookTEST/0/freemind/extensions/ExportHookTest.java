package freemind.extensions;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;

public class ExportHookTest {

 private ModeController controller;
 private ExportHook exportHook;

 @Before
 public void setup() {
 controller = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "Image files";
 final String nameExtension = null;
 final File expectedFile = new File("expectedFile.png");

 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(mock(FileChooser.class));
 when(controller.getMap()).thenReturn(mock(MMapModel.class));
 when(controller.getFrame()).thenReturn(mock(JFrame.class));
 when(controller.getResourceString(eq("file_already_exists"))).thenReturn("File already exists, do you want to overwrite it?");

 final File result = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, result);

 final File chooser = mock(FileChooser.class);
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(controller.getText("file_already_exists")).thenReturn(null);

 final File result2 = exportHook.chooseFile(type, description, nameExtension);
 assertNull(result2);
 }

 @Test
 public void testChooseFileWithSameFileName() {
 final String type = "png";
 final String description = "Image files";
 final String nameExtension = null;
 final File expectedFile = new File("expectedFile.png");
 final File existingFile = new File("existingFile.png");

 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(mock(FileChooser.class));
 when(controller.getMap()).thenReturn(mock(MMapModel.class));
 when(controller.getFrame()).thenReturn(mock(JFrame.class));
 when(controller.getResourceString(eq("file_already_exists"))).thenReturn("File already exists, do you want to overwrite it?");
 when(controller.getFileChooser(any(ImageFilter.class)).showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);

 final File result = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, result);

 final File chooser = mock(FileChooser.class);
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(controller.getFileChooser(any(ImageFilter.class)).getSelectedFile()).thenReturn(existingFile);
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");

 final int option = JOptionPane.showConfirmDialog(any(Container.class), eq("File already exists, do you want to overwrite it?"), eq("File already exists"), JOptionPane.YES_OPTION);
 when(controller.getText("file_already_exists")).thenReturn(null);

 final File result2 = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, result2);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resourceName%";
 final String expectedValue = "Expected value";

 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, result);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedImage = ImageIO.read(new File("expectedImage.png"));

 when(controller.getView()).thenReturn(mock(MindMapView.class));
 when(controller.getView().getWidth()).thenReturn(500);
 when(controller.getView().getHeight()).thenReturn(500);
 when(controller.getView().createImage(anyInt(), anyInt())).thenReturn(expectedImage);

 final BufferedImage result = exportHook.createBufferedImage();
 assertEquals(expectedImage, result);
 }

 @Test
 public void testCreateBufferedImageWithNullView() {
 exportHook.createBufferedImage();
 verify(controller, never()).getView();
 }

 @Test
 public void testCopyFromResource() throws IOException {
 final String prefix = "prefix/";
 final String fileName = "fileName.txt";
 final String destinationDirectory = "destinationDirectory";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 final File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile() throws IOException {
 final String dir = "/testData/";
 final String fileName = "fileName.txt";
 final String destinationDirectory = "destinationDirectory";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 final File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }
}