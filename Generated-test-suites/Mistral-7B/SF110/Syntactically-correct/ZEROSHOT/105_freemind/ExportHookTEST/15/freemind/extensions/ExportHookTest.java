package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.extensions.ExportHook;
import freemind.main.Logger;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapController mapControllerMock;
 private Controller controllerMock;
 private Logger loggerMock;

 @Before
 public void setUp() {
 modeControllerMock = Mockito.mock(ModeController.class);
 mapControllerMock = Mockito.mock(MapController.class);
 controllerMock = Mockito.mock(Controller.class);
 loggerMock = Mockito.mock(Logger.class);
 exportHook = new ExportHook() {
 {
 setController(controllerMock);
 setLogger(loggerMock);
 }
 };
 exportHook.setController(controllerMock);
 exportHook.setLogger(loggerMock);
 }

 @Test
 public void testChooseFile_NullType_ReturnsNull() {
 assertNull(exportHook.chooseFile(null, "Description", null));
 }

 @Test
 public void testChooseFile_ValidType_ReturnsFile() {
 final String type = "png";
 final File expected = new File("expectedFile.png");
 Mockito.when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(controllerMock.setText(Mockito.anyString()))
 .then(invocation -> invocation.getArgument(0));
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("mapFile.mindmap"));
 when(mapControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});

 final File actual = exportHook.chooseFile(type, "Description", null);
 assertEquals(expected, actual);
 }

 @Test
 public void testChooseFile_InvalidFileExtension_ReturnsNull() {
 final String type = "png";
 final File expected = null;
 Mockito.when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(controllerMock.setText(Mockito.anyString()))
 .then(invocation -> invocation.getArgument(0));
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("mapFile.mindmap"));
 when(mapControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});

 final File actual = exportHook.chooseFile(type, "Description", null);
 assertNull(actual);
 }

 @Test
 public void testChooseFile_FileExistsAndUserConfirmsToOverwrite_ReturnsFile() throws IOException {
 final String type = "png";
 final String overwriteText = "File already exists. Do you want to overwrite it?";
 final String confirmationText = "Yes";
 final File expected = new File("expectedFile.png");
 final File existingFile = new File("existingFile.png");
 Mockito.when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(controllerMock.getText(Mockito.anyString()))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("mapFile.mindmap"));
 when(mapControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
 .thenReturn(JOptionPane.YES_OPTION);
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});
 when(loggerMock.severe(Mockito.anyString())).thenAnswer(invocation -> {});
 when(controllerMock.getResourceString(Mockito.anyString())).thenReturn("file_already_exists");

 final File actual = exportHook.chooseFile(type, "Description", null);
 assertEquals(expected, actual);
 }

 @Test
 public void testChooseFile_FileExistsAndUserRefusesToOverwrite_ReturnsNull() throws IOException {
 final String type = "png";
 final String overwriteText = "File already exists. Do you want to overwrite it?";
 final String confirmationText = "No";
 final File expected = null;
 final File existingFile = new File("existingFile.png");
 Mockito.when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(controllerMock.getText(Mockito.anyString()))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("mapFile.mindmap"));
 when(mapControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
 .thenReturn(JOptionPane.NO_OPTION);
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});
 when(loggerMock.severe(Mockito.anyString())).thenAnswer(invocation -> {});
 when(controllerMock.getResourceString(Mockito.anyString())).thenReturn("file_already_exists");

 final File actual = exportHook.chooseFile(type, "Description", null);
 assertNull(actual);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName_ReturnsNull() {
 final String resourceName = null;
 final String expected = null;
 assertEquals(expected, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName_ReturnsTranslatedString() {
 final String resourceName = "file_already_exists";
 final String expected = "File already exists.";
 assertEquals(expected, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testCreateBufferedImage_ReturnsBufferedImage() {
 final MapView viewMock = Mockito.mock(MapView.class);
 when(controllerMock.getView()).thenReturn(viewMock);
 final BufferedImage expected = Mockito.mock(BufferedImage.class);
 when(viewMock.createImage(Mockito.anyInt(), Mockito.anyInt()))
 .thenReturn(expected);
 when(viewMock.getWidth()).thenReturn(100);
 when(viewMock.getHeight()).thenReturn(100);

 final BufferedImage actual = exportHook.createBufferedImage();
 assertEquals(expected, actual);
 }

 @Test
 public void testCreateBufferedImage_ReturnsNull() {
 final MapView viewMock = Mockito.mock(MapView.class);
 when(controllerMock.getView()).thenReturn(viewMock);
 when(viewMock.createImage(Mockito.anyInt(), Mockito.anyInt()))
 .thenReturn(null);
 when(viewMock.getWidth()).thenReturn(100);
 when(viewMock.getHeight()).thenReturn(100);

 final BufferedImage actual = exportHook.createBufferedImage();
 assertNull(actual);
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName_CopiesFile() throws IOException {
 final String prefix = "prefix";
 final String fileName = "file.txt";
 final String destinationDirectory = "destinationDirectory";
 final URL resource = Mockito.mock(URL.class);
 final InputStream inputStreamMock = Mockito.mock(InputStream.class);
 final OutputStream outputStreamMock = Mockito.mock(OutputStream.class);
 when(controllerMock.getResource(prefix + fileName)).thenReturn(resource);
 when(resource.openStream()).thenReturn(inputStreamMock);
 when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStreamMock);
 when(controllerMock.copyStream(Mockito.any(InputStream.class), Mockito.any(OutputStream.class), Mockito.anyBoolean())).thenAnswer(invocation -> {});
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});
 when(loggerMock.severe(Mockito.anyString())).thenAnswer(invocation -> {});

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName_CopiesFile() throws IOException {
 final String dir = "dir";
 final String fileName = "file.txt";
 final String destinationDirectory = "destinationDirectory";
 final File fileMock = Mockito.mock(File.class);
 final InputStream inputStreamMock = Mockito.mock(InputStream.class);
 final OutputStream outputStreamMock = Mockito.mock(OutputStream.class);
 when(controllerMock.getLogger()).thenReturn(loggerMock);
 when(new File(dir, fileName)).thenReturn(fileMock);
 when(fileMock.getAbsolutePath()).thenReturn("/absolute/path/to/file.txt");
 when(fileMock.length()).thenReturn(5L);
 when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStreamMock);
 when(controllerMock.copyStream(Mockito.any(File.class), Mockito.any(OutputStream.class), Mockito.anyBoolean())).thenAnswer(invocation -> {});
 when(loggerMock.finest(Mockito.anyString())).thenAnswer(invocation -> {});
 when(loggerMock.severe(Mockito.anyString())).thenAnswer(invocation -> {});

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testImageFilterAccept_NullFile_ReturnsFalse() {
 final String type = "png";
 final File fileMock = null;
 final ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);
 when(imageFilterMock.accept(fileMock)).thenReturn(false);

 assertFalse(imageFilterMock.accept(fileMock));
 }

 @Test
 public void testImageFilterAccept_Directory_ReturnsTrue() {
 final String type = "png";
 final File directoryMock = new File("directory");
 final ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);
 when(imageFilterMock.accept(directoryMock)).thenReturn(true);

 assertTrue(imageFilterMock.accept(directoryMock));
 }

 @Test
 public void testImageFilterAccept_FileWithValidExtension_ReturnsTrue() {
 final String type = "png";
 final String extension = "png";
 final File fileMock = new File("file.png");
 final ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);
 when(imageFilterMock.accept(fileMock)).thenReturn(true);

 assertTrue(imageFilterMock.accept(fileMock));
 }

 @Test
 public void testImageFilterAccept_FileWithInvalidExtension_ReturnsFalse() {
 final String type = "png";
 final String extension = "jpg";
 final File fileMock = new File("file.jpg");
 final ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);
 when(imageFilterMock.accept(fileMock)).thenReturn(false);

 assertFalse(imageFilterMock.accept(fileMock));
 }

 @Test
 public void testImageFilterGetDescription_NullType_ReturnsNull() {
 final String type = null;
 final ExportHook.ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);

 assertNull(imageFilterMock.getDescription());
 }

 @Test
 public void testImageFilterGetDescription_ValidType_ReturnsDescription() {
 final String type = "png";
 final String description = "Description";
 final ExportHook.ImageFilter imageFilterMock = Mockito.mock(ExportHook.ImageFilter.class);
 when(imageFilterMock.getDescription()).thenReturn(description);

 assertEquals(description, imageFilterMock.getDescription());
 }
}