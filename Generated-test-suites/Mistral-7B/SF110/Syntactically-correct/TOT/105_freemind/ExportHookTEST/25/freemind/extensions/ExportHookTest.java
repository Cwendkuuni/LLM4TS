package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.main.Controller;
import freemind.main.Logger;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ExportHook exportHook;
 private MapController mapControllerMock;
 private Controller controllerMock;
 private MapView mapViewMock;
 private Logger loggerMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mapControllerMock = mock(MapController.class);
 controllerMock = mock(Controller.class);
 mapViewMock = mock(MapView.class);
 loggerMock = mock(Logger.class);
 exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 exportHook.setView(mapViewMock);
 exportHook.setLogger(loggerMock);
 }

 @Test
 public void testChooseFileWithValidExtensionAndFileExists() {
 String type = "png";
 String description = "Export Image";
 String nameExtension = null;
 File expectedFile = new File("path/to/existing/file.png");
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(FileDialog.class));
 when(controllerMock.getMap()).thenReturn(new File("path/to/mindmap.mm"));

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals(expectedFile, actualFile);
 verify(controllerMock, times(1)).getFrame();
 verify(controllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 verify(controllerMock, times(1)).getMap();
 }

 @Test
 public void testChooseFileWithValidExtensionAndFileDoesNotExist() {
 String type = "png";
 String description = "Export Image";
 String nameExtension = null;
 File expectedFile = new File("path/to/new/file.png");
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(FileDialog.class));

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertNull(actualFile);
 verify(controllerMock, times(1)).getFrame();
 verify(controllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 }

 @Test
 public void testChooseFileWithInvalidExtension() {
 String type = "pdf";
 String description = "Export Image";
 String nameExtension = null;
 File expectedFile = new File("path/to/new/file.png");
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(FileDialog.class));
 when(controllerMock.getMap()).thenReturn(new File("path/to/mindmap.mm"));

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals(expectedFile, actualFile);
 // In this test case, the file extension should be changed to "png" in the test method because the chosenFile and expectedFile have the incorrect extension
 verify(controllerMock, times(1)).getFrame();
 verify(controllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 verify(controllerMock, times(1)).getMap();
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullController() {
 exportHook = new ExportHook();
 File actualFile = exportHook.chooseFile("png", "Export Image", null);
 fail("Should throw a NullPointerException");
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resource_text%";
 String expectedValue = "Expected translated text";
 when(controllerMock.getText(resourceName.substring(1))).thenReturn(expectedValue);

 String actualValue = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreateBufferedImage() {
 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(10, 20, 300, 400));
 when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(mock(BufferedImage.class));
 when(mapViewMock.getWidth()).thenReturn(500);
 when(mapViewMock.getHeight()).thenReturn(600);
 when(mapViewMock.getViewSize()).thenReturn(new Dimension(1000, 1200));
 when(mapViewMock.getPrintable(any(Graphics.class))).thenReturn(mapViewMock);

 BufferedImage actualImage = exportHook.createBufferedImage();

 assertEquals(mapViewMock, actualImage.getSource());
 }

 @Test
 public void testCopyFromResource() {
 String prefix = "prefix/";
 String fileName = "test.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory, fileName);
 verify(loggerMock, times(1)).finest("searching for " + prefix + fileName);
 verifyNoMoreInteractions(loggerMock);
 }

 @Test
 public void testCopyFromFile() {
 String dir = "path/to/directory";
 String fileName = "test.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory, fileName);
 verify(loggerMock, times(1)).finest("searching for " + dir + fileName);
 verifyNoMoreInteractions(loggerMock);
 }

 @Test
 public void testCopyFromFileWithNullDir() {
 String fileName = "test.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromFile(null, fileName, destinationDirectory);
 fail("Should not call loggerMock.finest() when dir is null");
 }
}