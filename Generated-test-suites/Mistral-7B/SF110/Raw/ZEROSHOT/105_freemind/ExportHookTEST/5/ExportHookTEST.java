package freemind.extensions;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapView mapViewMock;

 @Before
 public void setUp() {
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
 public void testChooseFile_WhenCalledWithValidArguments_ShouldReturnChosenFile() throws Exception {
 final String type = "png";
 final String description = "PNG Images";
 final String nameExtension = "export";
 final File expectedFile = new File("path/to/chosen/file.png");

 when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(modeControllerMock.getText(anyString())).thenReturn("File name");
 when(modeControllerMock.getMap()).thenReturn(new File("path/to/map.mm"));
 when(modeControllerMock.getFrame()).thenReturn(mock(JFrame.class));

 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);

 verify(modeControllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 verifyNoMoreInteractions(modeControllerMock);
 assertNotNull(chosenFile);
 assertNotNull(chosenFile.getAbsolutePath());
 assertNotNull(chosenFile.getName());
 assertEquals("path/to/chosen/file.png", chosenFile.getAbsolutePath());
 assertEquals("file name.png", chosenFile.getName());
 }

 @Test
 public void testChooseFile_WhenCalledWithNullArguments_ShouldReturnNull() {
 final File chosenFile = exportHook.chooseFile(null, null, null);
 assertNull(chosenFile);
 }

 @Test
 public void testGetTranslatableResourceString_WhenCalledWithValidResourceName_ShouldReturnTranslatedString() {
 final String resourceName = "%resource.key%";
 final String expectedString = "translated string";
 when(modeControllerMock.getText(resourceName.substring(1))).thenReturn(expectedString);

 final String result = exportHook.getTranslatableResourceString(resourceName);

 verify(modeControllerMock, times(1)).getText(resourceName.substring(1));
 verifyNoMoreInteractions(modeControllerMock);
 assertEquals(expectedString, result);
 }

 @Test
 public void testGetTranslatableResourceString_WhenCalledWithNullResourceName_ShouldReturnEmptyString() {
 final String result = exportHook.getTranslatableResourceString(null);
 assertEquals("", result);
 }

 @Test
 public void testCreateBufferedImage_WhenCalled_ShouldReturnBufferedImage() {
 final BufferedImage expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
 when(mapViewMock.createImage(anyInt(), anyInt())).thenReturn(expectedImage);

 final BufferedImage result = exportHook.createBufferedImage();

 verify(mapViewMock, times(1)).getInnerBounds();
 verify(mapViewMock, times(1)).createImage(anyInt(), anyInt());
 verifyNoMoreInteractions(mapViewMock);
 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_WhenCalledWithNullMapView_ShouldThrowNullPointerException() {
 exportHook.createBufferedImage();
 }

 @Test
 public void testCopyFromResource_WhenCalledWithValidArguments_ShouldCopyResourceToDestinationDirectory() throws Exception {
 final String prefix = "prefix";
 final String fileName = "file.txt";
 final String destinationDirectory = "path/to/destination";
 final URL resource = new URL("http://test.com/resource.txt");

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(modeLogger, times(1)).finest("searching for prefixfile.txt");
 verify(modeLogger, times(0)).severe("Cannot find resource: prefixfile.txt");
 verifyNoMoreInteractions(modeLogger);

 final File destinationFile = new File(destinationDirectory, fileName);
 assertTrue(destinationFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_WhenCalledWithNonExistentResource_ShouldThrowFileNotFoundException() throws Exception {
 final String prefix = "prefix";
 final String fileName = "file.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(modeLogger, times(1)).finest("searching for prefixfile.txt");
 verify(modeLogger, times(1)).severe("Cannot find resource: prefixfile.txt");
 }

 @Test(expected = IOException.class)
 public void testCopyFromFile_WhenCalledWithValidArguments_ShouldCopyFileToDestinationDirectory() throws Exception {
 final String dir = "path/to/source";
 final String fileName = "file.txt";
 final String destinationDirectory = "path/to/destination";
 final File sourceFile = new File(dir, fileName);

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(modeLogger, times(1)).finest("searching for path/to/sourcefile.txt");
 verify(modeLogger, times(0)).severe("Cannot find resource: path/to/sourcefile.txt");
 verifyNoMoreInteractions(modeLogger);

 final File destinationFile = new File(destinationDirectory, fileName);
 assertTrue(destinationFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_WhenCalledWithNonExistentFile_ShouldThrowFileNotFoundException() throws Exception {
 final String dir = "path/to/source";
 final String fileName = "file.txt";
 final String destinationDirectory = "path/to/destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(modeLogger, times(1)).finest("searching for path/to/sourcefile.txt");
 verify(modeLogger, times(1)).severe("Cannot find resource: path/to/sourcefile.txt");
 }
}