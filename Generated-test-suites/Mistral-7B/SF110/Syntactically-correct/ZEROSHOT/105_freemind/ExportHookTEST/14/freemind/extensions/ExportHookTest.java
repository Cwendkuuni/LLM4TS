package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController controllerMock;
 private JFrame frameMock;

 @Before
 public void setup() {
 controllerMock = Mockito.mock(ModeController.class);
 frameMock = Mockito.mock(JFrame.class);
 exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 }

 @Test
 public void testChooseFile_ValidTypeAndNameExtension_ReturnsFile() {
 String type = "png";
 String nameExtension = "test";
 File expectedFile = new File("path/to/testFile.png");

 when(controllerMock.getFileChooser(Mockito.any(FileFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 when(controllerMock.getText(Mockito.anyString()))
 .then(invocation -> invocation.getArgumentAt(0, 0));

 File result = exportHook.chooseFile(type, "Export image", nameExtension);

 assertEquals(expectedFile, result);
 }

 @Test
 public void testChooseFile_InvalidTypeAndNameExtension_ReturnsNull() {
 String type = "pdf";
 String nameExtension = "test";

 File result = exportHook.chooseFile(type, "Export image", nameExtension);

 assertNull(result);
 }

 @Test
 public void testChooseFile_NoFileChooser_ReturnsNull() {
 String type = "png";
 String nameExtension = "test";
 when(controllerMock.getFileChooser(Mockito.any(FileFilter.class)))
 .thenReturn(null);

 File result = exportHook.chooseFile(type, "Export image", nameExtension);

 assertNull(result);
 }

 @Test
 public void testChooseFile_CancelButtonPressed_ReturnsNull() {
 String type = "png";
 String nameExtension = "test";
 when(controllerMock.getFileChooser(Mockito.any(FileFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 when(controllerMock.getText(Mockito.anyString()))
 .then(invocation -> invocation.getArgumentAt(0, 0));
 when(controllerMock.getFrame()).thenReturn(frameMock);
 when(frameMock.isDisplayed()).thenReturn(true);
 when(controllerMock.getText("file_already_exists"))
 .thenReturn("File already exists.");
 when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.anyString(), 0))
 .thenReturn(JOptionPane.NO_OPTION);

 File result = exportHook.chooseFile(type, "Export image", nameExtension);

 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceKey_ReturnsTranslatedString() {
 String resourceKey = "%resourceKey%";
 String expectedTranslatedString = "Translated string";

 when(controllerMock.getResourceString(resourceKey)).thenReturn(resourceKey);

 String result = exportHook.getTranslatableResourceString(resourceKey);

 assertEquals(expectedTranslatedString, result);
 }

 @Test
 public void testGetTranslatableResourceString_InvalidResourceKey_ReturnsOriginalString() {
 String resourceKey = "resourceKey";

 String result = exportHook.getTranslatableResourceString(resourceKey);

 assertEquals(resourceKey, result);
 }

 @Test
 public void testCreateBufferedImage() {
 MapView viewMock = Mockito.mock(MapView.class);
 when(controllerMock.getView()).thenReturn(viewMock);
 when(viewMock.preparePrinting()).thenReturn(true);
 when(viewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
 when(viewMock.createImage(10, 10))
 .thenReturn(Mockito.mock(BufferedImage.class));
 when(viewMock.endPrinting()).thenReturn(true);

 BufferedImage result = exportHook.createBufferedImage();

 assertNotNull(result);
 }

 @Test
 public void testCreateBufferedImage_NullView_ReturnsNull() {
 when(controllerMock.getView()).thenReturn(null);

 BufferedImage result = exportHook.createBufferedImage();

 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_ErrorCreatingImage_ReturnsNull() {
 MapView viewMock = Mockito.mock(MapView.class);
 when(controllerMock.getView()).thenReturn(viewMock);
 when(viewMock.preparePrinting()).thenReturn(true);
 when(viewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
 when(viewMock.createImage(10, 10)).thenThrow(new RuntimeException());
 when(viewMock.endPrinting()).thenReturn(true);

 BufferedImage result = exportHook.createBufferedImage();

 assertNull(result);
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName_CopiesFileToDestinationDirectory() {
 String prefix = "prefix";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(controllerMock, times(1)).logger.finest("searching for " + prefix + fileName);
 verify(controllerMock, times(1)).getResource(prefix + fileName);
 verifyNoMoreInteractions(controllerMock.getLogger());

 File expectedDestinationFile = new File(destinationDirectory + File.separator + fileName);
 assertTrue(expectedDestinationFile.exists());
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName_CopiesFileToDestinationDirectory() {
 String dir = "dir";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(controllerMock, times(1)).logger.finest("searching for " + dir + fileName);
 verifyNoMoreInteractions(controllerMock.getLogger());

 File expectedDestinationFile = new File(destinationDirectory + File.separator + fileName);
 assertTrue(expectedDestinationFile.exists());
 }

 @Test
 public void testCopyFromResource_FileNotFound_DoesNotLogException() {
 String prefix = "prefix";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 doThrow(new FileNotFoundException("File not found"))
 .when(controllerMock).getResource(prefix + fileName);

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verifyNoInteractions(controllerMock.getLogger());
 }

 @Test
 public void testCopyFromResource_IOException_LogsException() {
 String prefix = "prefix";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 doThrow(new IOException("IOException"))
 .when(controllerMock).getResource(prefix + fileName);

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(controllerMock.getLogger(), times(1)).severe("File not found or could not be copied. Was earching for " + prefix + fileName + " and should go to " + destinationDirectory);
 }

 @Test
 public void testCopyFromFile_FileNotFound_DoesNotLogException() {
 String dir = "dir";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 doThrow(new FileNotFoundException("File not found"))
 .when(controllerMock).getFile(Mockito.eq(dir), Mockito.eq(fileName));

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verifyNoInteractions(controllerMock.getLogger());
 }

 @Test
 public void testCopyFromFile_IOException_LogsException() {
 String dir = "dir";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";

 doThrow(new IOException("IOException"))
 .when(controllerMock).getFile(Mockito.eq(dir), Mockito.eq(fileName));

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(controllerMock.getLogger(), times(1)).severe("File not found or could not be copied. Was earching for " + dir + fileName + " and should go to " + destinationDirectory);
 }
}