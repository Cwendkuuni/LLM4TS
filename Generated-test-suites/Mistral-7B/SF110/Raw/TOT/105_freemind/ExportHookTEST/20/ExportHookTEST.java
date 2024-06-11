package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.MapController;
import freemind.model.Map;
import freemind.model.map.Node;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.PrintJob;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 @Mock
 private Controller controller;

 @Mock
 private Map map;

 @Mock
 private MapView view;

 @InjectMocks
 private ExportHook exportHook;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile_ValidImageType_ReturnsFile() {
 final String type = "png";
 final String description = "PNG Image";
 final ModeController modeController = mock(ModeController.class);
 final Container component = mock(Container.class);
 final FileFilter filter = new ExportHook.ImageFilter("png", description);
 final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
 final File mmFile = new File("testMap.mm");
 final File chosenFile = new File("testImage.png");

 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(filter)).thenReturn(chooser);
 when(controller.getMap()).thenReturn(map);
 when(map.getFile()).thenReturn(mmFile);
 when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.APPROVE_OPTION);

 exportHook.chooseImageFile(type, description, null, modeController);

 ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
 verify(chooser).setSelectedFile(fileCaptor.capture());
 assertEquals(chosenFile, fileCaptor.getValue());
}

 @Test
 public void testChooseFile_InvalidImageType_ReturnsNull() {
 final String type = "txt";
 final String description = "Text File";
 final ModeController modeController = mock(ModeController.class);
 final Container component = mock(Container.class);
 final FileFilter filter = new ExportHook.ImageFilter("png", description);
 final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
 final File mmFile = new File("testMap.mm");

 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(filter)).thenReturn(chooser);
 when(controller.getMap()).thenReturn(map);
 when(map.getFile()).thenReturn(mmFile);
 when(chooser.showSaveDialog(component)).thenReturn(JFileChooser.CANCEL_OPTION);

 exportHook.chooseImageFile(type, description, null, modeController);

 verify(chooser, never()).setSelectedFile(any(File.class));
}

 @Test
 public void testGetTranslatableResourceString_ValidResourceKey_ReturnsTranslatedString() {
 final String resourceName = "file.export.savedas";
 final String expectedTranslatedString = "File saved as";

 assertEquals(expectedTranslatedString, exportHook.getTranslatableResourceString(resourceName));
}

 @Test
 public void testGetTranslatableResourceString_InvalidResourceKey_ReturnsOriginalString() {
 final String resourceName = "invalidResourceKey";
 final String expectedOriginalString = "invalidResourceKey";

 assertEquals(expectedOriginalString, exportHook.getTranslatableResourceString(resourceName));
}

 @Test
 public void testCreateBufferedImage_ReturnsBufferedImage() {
 final Node node = mock(Node.class);
 final MapController mapController = mock(MapController.class);
 when(node.getMap()).thenReturn(map);
 when(map.getController()).thenReturn(mapController);
 when(mapController.getView()).thenReturn(view);

 exportHook.view = view;
 exportHook.createBufferedImage();

 verify(view).preparePrinting();
 verify(view).print(any(Graphics2D.class));
 verify(view).endPrinting();
}

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 exportHook.view = null;
 exportHook.createBufferedImage();
}

 @Test
 public void testCopyFromResource_ValidPrefixFileNameDestinationDirectory_CopiesFile() throws IOException {
 final String prefix = "prefix/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "dest";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 final File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
}

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullPrefixFileNameDestinationDirectory() {
 exportHook.copyFromResource(null, null, null);
}

 @Test
 public void testCopyFromFile_ValidDirFileNameDestinationDirectory_CopiesFile() throws IOException {
 final String dir = "source";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "dest";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 final File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
}

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDirFileNameDestinationDirectory() {
 exportHook.copyFromFile(null, null, null);
}

 @Test
 public void testCopyFromFile_FileDoesNotExist_DoesNotThrowException() throws IOException {
 final String dir = "nonexistent";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "dest";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
}

 @Test
 public void testCreateBufferedImage_ViewIsNull() {
 exportHook.view = null;
 exportHook.createBufferedImage();

 verifyNoMoreInteractions(view);
}

 @Test
 public void testCreateBufferedImage_ViewPreparePrintingThrowsNullPointerException() {
 final Node node = mock(Node.class);
 when(node.getMap()).thenReturn(map);
 when(map.getController()).thenReturn(mapController);
 when(mapController.getView()).thenThrow(new NullPointerException());
 when(map.getWidth()).thenReturn(10);
 when(map.getHeight()).thenReturn(10);

 exportHook.view = view;
 exportHook.createBufferedImage();

 verify(view, never()).preparePrinting();
 verify(view, never()).print(any(Graphics2D.class));
 verify(view, never()).endPrinting();
}

 @Test
 public void testCopyFromResource_ResourceNotFound_LogsException() {
 final String prefix = "prefix/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "dest";

 when(exportHook.getResource(prefix + fileName)).thenThrow(new FileNotFoundException());

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(Resources.getInstance()).logException(any(Exception.class));
}

 @Test
 public void testCopyFromFile_FileNotFound_LogsException() throws IOException {
 final String dir = "source";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "dest";

 when(new File(dir, fileName)).thenThrow(new FileNotFoundException());

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(Resources.getInstance()).logException(any(Exception.class));
}

 @Test
 public void testChooseImageFile_NullController_ReturnsNull() {
 final String type = "png";
 final String description = "PNG Image";
 final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
 final File mmFile = new File("testMap.mm");

 exportHook.controller = null;
 exportHook.chooseImageFile(type, description, null, null);

 verify(chooser, never()).setSelectedFile(any(File.class));
}

 @Test
 public void testChooseImageFile_FileDialogThrowsIOException_ReturnsNull() {
 final String type = "png";
 final String description = "PNG Image";
 final ModeController modeController = mock(ModeController.class);
 final Container component = mock(Container.class);
 final FileFilter filter = new ExportHook.ImageFilter("png", description);
 final FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
 final File mmFile = new File("testMap.mm");

 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(filter)).thenReturn(chooser);
 when(controller.getMap()).thenReturn(map);
 when(map.getFile()).thenReturn(mmFile);
 when(chooser.showSaveDialog(component)).thenThrow(new IOException());

 exportHook.chooseImageFile(type, description, null, modeController);

 verify(chooser, never()).setSelectedFile(any(File.class));
}

 @Test
 public void testCreateBufferedImage_ViewWidthAndHeightAreZero_ReturnsNull() {
 exportHook.view = view;
 when(view.getWidth()).thenReturn(0);
 when(view.getHeight()).thenReturn(0);

 exportHook.createBufferedImage();

 assertNull(exportHook.view);
}
}