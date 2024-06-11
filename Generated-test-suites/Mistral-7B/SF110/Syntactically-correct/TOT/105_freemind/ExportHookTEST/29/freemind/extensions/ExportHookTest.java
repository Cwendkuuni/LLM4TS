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
import org.mockito.Mockito;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.Controller;
import freemind.main.MapController;
import freemind.main.MapView;
import freemind.modes.MapMode;
import freemind.view.mindmapview.MindMapView;

public class ExportHookTest {

 private ExportHook hook;
 private ModeController controllerMock;
 private MapView viewMock;
 private Controller controller;

 @Before
 public void setUp() {
 controller = Mockito.mock(Controller.class);
 controllerMock = Mockito.mock(ModeController.class);
 controllerMock.setController(controller);
 viewMock = Mockito.mock(MapView.class);
 hook = new ExportHook() {
 @Override
 public ModeController getController() {
 return controllerMock;
 }

 @Override
 public MapView getView() {
 return viewMock;
 }
 };
 }

 @Test
 public void testChooseFile_ValidFileType_Success() {
 final String type = "jpg";
 final String description = "JPG files";
 final String nameExtension = "jpg";
 final File expectedFile = new File("path/to/expectedFile.jpg");

 when(controllerMock.getText("file_already_exists")).thenReturn("Do you want to replace the existing file?");
 when(controllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(Mockito.mock(MapController.class));

 final ArgumentCaptor<ImageFilter> imageFilterArgumentCaptor = ArgumentCaptor.forClass(ImageFilter.class);
 final JFileChooser fileChooserMock = mock(JFileChooser.class);
 final FileChooserAdapter fileChooserAdapterMock = mock(FileChooserAdapter.class);
 doNothing().when(fileChooserMock).addActionListener(fileChooserAdapterMock);
 doReturn(fileChooserAdapterMock).when(controllerMock).getFileChooser(imageFilterArgumentCaptor.capture());
 doReturn(fileChooserMock).when(controllerMock).getFileChooser(Mockito.eq(type), Mockito.eq(description), Mockito.eq(nameExtension));

 when(fileChooserMock.showSaveDialog(Mockito.any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooserMock.getSelectedFile()).thenReturn(expectedFile);

 final File file = hook.chooseFile(type, description, nameExtension);
 final File actualFile = file;

 assertEquals(expectedFile, actualFile);

 verify(controllerMock, times(1)).getText("file_already_exists");
 verify(controllerMock, times(1)).getFrame();
 verify(controllerMock, times(1)).getFileChooser(imageFilterArgumentCaptor.capture());
 verify(fileChooserMock, times(1)).showSaveDialog(Mockito.any(Component.class));
 verify(controllerMock, times(1)).getMap();
 verify(fileChooserMock, times(1)).setSelectedFile(expectedFile);
 }

 @Test
 public void testChooseFile_InvalidFileType_Failure() {
 final String type = "png";
 final String description = "PNG files";
 final String nameExtension = null;
 final File expectedFile = new File("path/to/expectedFile.jpg");

 when(controllerMock.getText("file_already_exists")).thenReturn("Do you want to replace the existing file?");
 when(controllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(Mockito.mock(MapController.class));

 final ArgumentCaptor<ImageFilter> imageFilterArgumentCaptor = ArgumentCaptor.forClass(ImageFilter.class);
 final JFileChooser fileChooserMock = mock(JFileChooser.class);
 final FileChooserAdapter fileChooserAdapterMock = mock(FileChooserAdapter.class);
 doNothing().when(fileChooserMock).addActionListener(fileChooserAdapterMock);
 doReturn(fileChooserAdapterMock).when(controllerMock).getFileChooser(imageFilterArgumentCaptor.capture());
 doReturn(fileChooserMock).when(controllerMock).getFileChooser(type, description, nameExtension);

 when(fileChooserMock.showSaveDialog(Mockito.any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooserMock.getSelectedFile()).thenReturn(expectedFile);

 final File file = hook.chooseFile(type, description, nameExtension);
 final File actualFile = null;

 assertNull(actualFile);

 verify(controllerMock, times(1)).getText("file_already_exists");
 verify(controllerMock, times(1)).getFrame();
 verify(controllerMock, times(1)).getFileChooser(imageFilterArgumentCaptor.capture());
 verify(fileChooserMock, times(1)).showSaveDialog(Mockito.any(Component.class));
 verify(controllerMock, times(1)).getMap();
 verify(fileChooserMock, times(1)).setSelectedFile(expectedFile);

 final ImageFilter imageFilter = imageFilterArgumentCaptor.getValue();
 verify(imageFilter, times(1)).accept(expectedFile);
 }

 @Test
 public void testGetTranslatableResourceString_ValidKey_Success() {
 final String resourceName = "test.key";
 final String expectedValue = "test value";

 when(controllerMock.getText(resourceName)).thenReturn(expectedValue);

 final String actualValue = hook.getTranslatableResourceString(resourceName);

 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetTranslatableResourceString_InvalidKey_Null() {
 final String resourceName = "invalid.key";

 final String actualValue = hook.getTranslatableResourceString(resourceName);

 assertNull(actualValue);
 }

 @Test
 public void testCreateBufferedImage_Success() throws IOException {
 final BufferedImage expectedImage = ImageIO.read(new File("path/to/expectedImage.png"));

 doReturn(viewMock).when(hook).getView();
 doReturn(expectedImage).when(hook).createImage(viewMock.getWidth(), viewMock.getHeight());

 final BufferedImage actualImage = hook.createBufferedImage();

 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_Failure() {
 doReturn(null).when(hook).getView();

 final BufferedImage actualImage = hook.createBufferedImage();

 assertNull(actualImage);
 }

 @Test
 public void testCopyFromResource_Success() {
 final String prefix = "prefix";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/destination";

 hook.copyFromResource(prefix, fileName, destinationDirectory);

 final File expectedFile = new File(destinationDirectory, fileName);
 final File actualFile = expectedFile;

 assertEquals(expectedFile, actualFile);

 verify(Resources.getInstance(), times(1)).logException(Mockito.any(Exception.class));
 }

 @Test
 public void testCopyFromFile_Success() {
 final String dir = "path/to/source";
 final String fileName = "test.txt";
 final String destinationDirectory = "path/to/destination";

 hook.copyFromFile(dir, fileName, destinationDirectory);

 final File expectedFile = new File(destinationDirectory, fileName);
 final File actualFile = expectedFile;

 assertEquals(expectedFile, actualFile);

 verify(Resources.getInstance(), times(1)).logException(Mockito.any(Exception.class));
 }
}