package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.junit.*;
import org.mockito.*;

public class ExportHookTest {

 private ModeController controller;
 private ExportHook exportHook;

 @Before
 public void setUp() {
 controller = mock(ModeController.class);
 exportHook = new ExportHook() {
 @Override
 ModeController getController() {
 return controller;
 }
 };
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(null, "description", null));
 verify(controller, times(1)).getFileChooser(any(ImageFilter.class));
 }

 @Test
 public void testChooseFileValidType() {
 String type = "png";
 File expectedFile = new File("expectedFile.png");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 doAnswer(invocation -> {
 JFileChooser fileChooser = (JFileChooser)invocation.getArgumentAt(0, 0);
 fileChooser.setSelectedFile(expectedFile);
 return fileChooser.showSaveDialog(eq(controller.getFrame().getContentPane()));
 }).when(controller).showSaveDialog(any(Component.class));

 File actualFile = exportHook.chooseFile(type, "description", null);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFileInvalidType() {
 String type = "png";
 String nameExtension = "jpg";
 File expectedFile = new File("expectedFile.jpg");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 doAnswer(invocation -> {
 JFileChooser fileChooser = (JFileChooser)invocation.getArgumentAt(0, 0);
 fileChooser.setSelectedFile(expectedFile);
 return fileChooser.showSaveDialog(eq(controller.getFrame().getContentPane()));
 }).when(controller).showSaveDialog(any(Component.class));

 when(controller.getText("file_already_exists")).thenReturn("File already exists");

 File actualFile = exportHook.chooseFile(type, "description", nameExtension);
 assertNull(actualFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%hello%";
 String expected = "hello";
 String actual = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateBufferedImage() {
 View view = mock(MapView.class);
 when(controller.getView()).thenReturn(view);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 BufferedImage expectedImage = mock(BufferedImage.class);
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource() {
 String prefix = "prefix";
 String fileName = "fileName.txt";
 String destinationDirectory = "destination";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(controller, times(1)).getLogger();
 verify(controller.getLogger()).finest("searching for prefixfileName");
 verify(controller.getLogger()).finest("searching for prefixfileName and should go to destination");
 }

 @Test
 public void testCopyFromFile() {
 String dir = "dir";
 String fileName = "fileName.txt";
 String destinationDirectory = "destination";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(controller, times(1)).getLogger();
 verify(controller.getLogger()).finest("searching for dirfileName");
 }
}