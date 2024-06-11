package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileDialog;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapView mapViewMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 modeControllerMock = mock(ModeController.class);
 mapViewMock = mock(MapView.class);
 exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return modeControllerMock;
 }

 @Override
 public MapView getView() {
 return mapViewMock;
 }
 };
 }

 @Test
 public void testChooseFile_Success() throws IOException {
 // Given
 final String type = "png";
 final String description = "Export as PNG";
 final String nameExtension = null;
 final File expectedFile = new File("expectedFilePath");
 when(modeControllerMock.getText("file_already_exists")).thenReturn("Overwrite existing file? {0}");
 when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileDialog.class));
 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(ImageIO.read(new File("mapImagePath")));
 when(mapViewMock.print(any(Graphics.class))).thenReturn(true);

 // When
 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 // Then
 assertEquals("Expected file matches the actual file", expectedFile, actualFile);
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getFileChooser(any(FileFilter.class));
 verify(mapViewMock, times(1)).getInnerBounds();
 verify(mapViewMock, times(1)).createImage(anyInt(), anyInt());
 verify(mapViewMock, times(1)).print(any(Graphics.class));
 }

 @Test
 public void testChooseFile_Fail_NoFileChosen() {
 // Given
 final String type = "png";
 final String description = "Export as PNG";
 final String nameExtension = null;
 when(modeControllerMock.getText("file_already_exists")).thenReturn("Overwrite existing file? {0}");
 when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileDialog.class));
 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(ImageIO.read(new File("mapImagePath")));
 when(mapViewMock.print(any(Graphics.class))).thenReturn(true);
 when(modeControllerMock.getFileChooser(any(FileFilter.class)).showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(modeControllerMock.getFileChooser(any(FileFilter.class)).getSelectedFile()).thenReturn(null);

 // When
 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 // Then
 assertNull("File should be null when no file is chosen", actualFile);
 }

 @Test
 public void testChooseFile_Fail_InvalidFileType() throws IOException {
 // Given
 final String type = "txt";
 final String description = "Export as Text";
 final String nameExtension = "txt";
 final File expectedFile = new File("expectedFilePath");
 when(modeControllerMock.getText("file_already_exists")).thenReturn("Overwrite existing file? {0}");
 when(modeControllerMock.getFileChooser(any(FileFilter.class))).thenReturn(mock(FileDialog.class));
 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(ImageIO.read(new File("mapImagePath")));
 when(mapViewMock.print(any(Graphics.class))).thenReturn(true);
 when(modeControllerMock.getText("file_already_exists")).thenReturn("File {0} already exists. Overwrite?");
 when(modeControllerMock.getFileChooser(any(FileFilter.class)).showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(modeControllerMock.getFileChooser(any(FileFilter.class)).getSelectedFile()).thenReturn(expectedFile);
 when(modeControllerMock.getFileChooser(any(FileFilter.class)).getSelectedFile().exists()).thenReturn(false);

 // When
 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 // Then
 assertEquals("Expected file matches the actual file", expectedFile, actualFile);
 verify(modeControllerMock, times(2)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getFileChooser(any(FileFilter.class));
 verify(mapViewMock, times(1)).getInnerBounds();
 verify(mapViewMock, times(1)).createImage(anyInt(), anyInt());
 verify(mapViewMock, times(1)).print(any(Graphics.class));
 }

 @Test
 public void testGetTranslatableResourceString_Success() {
 // Given
 final String resourceName = "%s_resource";

 // When
 final String actualString = exportHook.getTranslatableResourceString(resourceName);

 // Then
 assertEquals("Expected string is the same as the input string with %s replaced by the string from getController().getText()", "s_resource", actualString);
 }

 @Test
 public void testGetTranslatableResourceString_Failure() {
 // Given
 final String resourceName = "%s_resource";

 // When
 final String actualString = exportHook.getTranslatableResourceString(resourceName);

 // Then
 assertNull("String should be null when the string does not start with %", actualString);
 }

 @Test
 public void testCreateBufferedImage_Success() throws Exception {
 // Given
 final BufferedImage expectedImage = ImageIO.read(new File("mapImagePath"));

 // When
 final BufferedImage actualImage = exportHook.createBufferedImage();

 // Then
 assertNotNull("Buffered image should not be null", actualImage);
 assertEquals("Buffered images are the same", expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_Failure() {
 // Given

 // When
 final BufferedImage actualImage = exportHook.createBufferedImage();

 // Then
 assertNull("Buffered image should be null when the view is null", actualImage);
 }

 @Test
 public void testCopyFromResource_Success() throws IOException {
 // Given
 final String prefix = "resources/images/";
 final String fileName = "logo.png";
 final String destinationDirectory = "expectedDirectoryPath";

 // When
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 // Then
 final File expectedFile = new File(destinationDirectory + "/logo.png");
 assertTrue("File should exist after copying", expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_Success() throws IOException {
 // Given
 final String dir = "expectedDirectoryPath";
 final String fileName = "logo.png";
 final String destinationDirectory = "destinationDirectoryPath";
 final File fileToCopy = new File("expectedFilePath/logo.png");

 // When
 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 // Then
 final File expectedFile = new File(destinationDirectory + "/logo.png");
 assertTrue("File should exist after copying", expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_Failure() throws IOException {
 // Given
 final String dir = "expectedDirectoryPath";
 final String fileName = "logo.png";
 final String destinationDirectory = "destinationDirectoryPath";
 final File fileToCopy = null;

 // When
 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 // Then
 assertFalse("File should not exist after copying null file", new File(destinationDirectory + "/logo.png").exists());
 }
}