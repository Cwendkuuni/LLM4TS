package freemind.extensions;

import static org.junit.Assert.*;
import freemind.view.mindmapview.*;
import org.junit.*;
import javax.swing.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController controller;
 private MapView view;
 private FileChooser chooser;
 private Container component;

 @Before
 public void setUp() {
 controller = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 view = mock(MapView.class);
 exportHook.setView(view);
 chooser = mock(JFileChooser.class);
 component = mock(Container.class);
 }

 @Test
 public void testChooseFile_NullType() {
 assertNull(exportHook.chooseFile(null, "description", null));
 verify(controller, times(1)).getFileChooser(any(ImageFilter.class));
 }

 @Test
 public void testChooseFile_ValidType() {
 final String type = "png";
 final File chosenFile = new File("path/to/file.png");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(chosenFile);

 final File result = exportHook.chooseFile(type, "description", null);
 assertEquals(chosenFile, result);
 }

 @Test
 public void testChooseFile_InvalidFileExtension() {
 final String type = "png";
 final File chosenFile = new File("path/to/file.txt");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(chosenFile);

 final File result = exportHook.chooseFile(type, "description", null);
 assertNull(result);
 }

 @Test
 public void testChooseFile_FileAlreadyExists() {
 final String type = "png";
 final String proposedName = "path/to/file.png";
 final File existingFile = new File(proposedName);
 when(controller.getText("file_already_exists")).thenReturn("File already exists.");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(existingFile);

 final int overwriteMap = exportHook.getController().showConfirmDialog(component, eq("File already exists."), eq("File already exists."), 0);
 assertFalse(overwriteMap == 0);
 }

 @Test
 public void testCreateBufferedImage_NullView() {
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImage_ValidView() throws Exception {
 final BufferedImage expectedImage = ImageIO.read(new File("path/to/image.jpg"));
 when(view.preparePrinting()).thenReturn(true);
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
 when(view.getWidth()).thenReturn(expectedImage.getWidth());
 when(view.getHeight()).thenReturn(expectedImage.getHeight());
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, expectedImage.getWidth(), expectedImage.getHeight()));
 when(view.endPrinting()).thenReturn(true);

 final BufferedImage result = exportHook.createBufferedImage();
 assertEquals(expectedImage, result);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName() {
 assertNull(exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName() {
 final String resourceName = "test.resource";
 final String expectedValue = "Test Resource";
 when(controller.getText(eq("%test.resource%"))).thenReturn(expectedValue);

 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, result);
 }
}