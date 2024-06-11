package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.*;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private ImageFilter imageFilterMock;
 private FreeMindFileDialog fileChooserMock;
 private MapView viewMock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 modeControllerMock = mock(ModeController.class);
 imageFilterMock = mock(ImageFilter.class);
 fileChooserMock = mock(FreeMindFileDialog.class);
 viewMock = mock(MapView.class);
 exportHook = new ExportHook();
 exportHook.setController(modeControllerMock);
 }

 @Test
 public void testChooseFileWithValidArguments() {
 String type = "png";
 String description = "Export Image As...";
 String nameExtension = "png";
 File expectedFile = new File("path/to/file.png");
 when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileChooserMock);
 when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooserMock.getSelectedFile()).thenReturn(expectedFile);
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFileWithNullArguments() {
 assertNull(exportHook.chooseFile(null, null, null));
 }

 @Test
 public void testChooseFileWithInvalidFile() {
 String type = "png";
 String description = "Export Image As...";
 String nameExtension = "png";
 when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileChooserMock);
 when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooserMock.getSelectedFile()).thenReturn(new File("path/to/invalidFile"));
 assertNull(exportHook.chooseFile(type, description, nameExtension));
 }

 @Test
 public void testChooseFileWithOverwriteConfirmation() {
 String type = "png";
 String description = "Export Image As...";
 String nameExtension = "png";
 when(modeControllerMock.getFileChooser(imageFilterMock)).thenReturn(fileChooserMock);
 when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooserMock.getSelectedFile()).thenReturn(new File("path/to/existingFile.png"));
 when(modeControllerMock.getText(eq("file_already_exists"))).thenReturn("File already exists. Do you want to replace it?");
 when(JOptionPane.showConfirmDialog(any(Component.class), eq("File already exists. Do you want to replace it?"), eq("File already exists"), 0)).thenReturn(JOptionPane.YES_OPTION);
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 File expectedFile = new File("path/to/existingFile.png");
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resourceKey%";
 String expectedResult = "Expected translation";
 when(modeControllerMock.getText(eq("resourceKey"))).thenReturn("Expected translation");
 String actualResult = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testGetTranslatableResourceStringWithNullResult() {
 String resourceName = "%resourceKey%";
 String actualResult = exportHook.getTranslatableResourceString(resourceName);
 assertNull(actualResult);
 }

 @Test
 public void testCreateBufferedImage() {
 MapView view = mock(MapView.class);
 when(modeControllerMock.getView()).thenReturn(view);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertNotNull(actualImage);
 }

 @Test
 public void testCreateBufferedImageWithNullView() {
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertNull(actualImage);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "prefix/";
 String fileName = "testFile.txt";
 String destinationDirectory = "dest/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 verify(modeControllerMock, times(1)).getResource(eq(prefix + fileName));
 File resourceFile = verify(modeControllerMock).getResource(eq(prefix + fileName)).get();
 FileOutputStream outputStream = verify(resourceFile).getOutputStream();
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "path/to/dir/";
 String fileName = "testFile.txt";
 String destinationDirectory = "dest/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 verify(modeControllerMock, times(1)).getLogger();
 File resourceFile = verify(modeControllerMock).getLogger().getResource(eq("prefix/testFile.txt")).get();
 FileInputStream inputStream = verify(resourceFile).getInputStream();
 FileOutputStream outputStream = verify(modeControllerMock).getLogger().getResource(eq("dest/testFile.txt")).getOutputStream();
 }
}