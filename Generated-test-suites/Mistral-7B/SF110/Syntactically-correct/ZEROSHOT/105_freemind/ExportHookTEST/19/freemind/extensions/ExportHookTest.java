package freemind.extensions;

import static org.junit.Assert.*;

import freemind.view.mindmapview.*;
import org.junit.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import static org.mockito.Mockito.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;
import static org.mockito.MockitoAnnotations.initMocks;;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController controllerMock;
 private MapView viewMock;

 @Before
 public void setUp() {
 initMocks(this);
 controllerMock = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 viewMock = mock(MapView.class);
 when(controllerMock.getView()).thenReturn(viewMock);
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "Description";
 String nameExtension = ".jpg";
 File expectedFile = new File("path/to/expected/file.png");
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(mock(MMapModel.class));
 when(viewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals(expectedFile, actualFile);
 verify(controllerMock.getFrame()).getContentPane();
 verify(controllerMock.getFileChooser(any(ImageFilter.class))).showSaveDialog(controllerMock.getFrame().getContentPane());
 }

 @Test
 public void testChooseImageFile() {
 String type = "png";
 String description = "Description";
 String nameExtension = null;
 File expectedFile = new File("path/to/expected/file.png");
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(mock(MMapModel.class));
 when(viewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(controllerMock.getText("file_already_exists")).thenReturn("File already exists.");

 File actualFile = exportHook.chooseImageFile(type, description, nameExtension, controllerMock);

 assertEquals(expectedFile, actualFile);
 verify(controllerMock.getFrame()).getContentPane();
 verify(controllerMock.getFileChooser(any(ImageFilter.class))).showSaveDialog(controllerMock.getFrame().getContentPane());
 verify(controllerMock).getText("file_already_exists");
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resource_key%";
 String expectedString = "Expected translation";
 when(controllerMock.getText("Expected translation")).thenReturn(expectedString);

 String actualString = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedImage = mock(BufferedImage.class);
 when(viewMock.preparePrinting()).thenReturn(true);
 when(viewMock.createImage(anyInt(), anyInt())).thenReturn(expectedImage);

 BufferedImage actualImage = exportHook.createBufferedImage();

 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource() {
 String prefix = "prefix";
 String fileName = "fileName.txt";
 String destinationDirectory = "destination/directory";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(controllerMock.getLogger()).finest("searching for prefixfileName");
 verify(controllerMock.getResource(prefix + fileName)).isInstanceOf(URL.class);
 }

 @Test
 public void testCopyFromFile() {
 String dir = "path/to/directory";
 String fileName = "fileName.txt";
 String destinationDirectory = "destination/directory";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(controllerMock.getLogger()).finest("searching for path/to/directoryfileName");
 verify(new File(dir, fileName)).exists();
 }
}