package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.ContentManager;
import freemind.main.MapController;
import freemind.modes.MapMode;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private ModeController modeController;
 @Mock
 private MapView mapView;
 @Mock
 private Printable printable;
 @Mock
 private MapMode mapMode;
 @Mock
 private ContentManager contentManager;
 @Mock
 private ResourceBundle resourceBundle;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 exportHook = new ExportHook();
 exportHook.setController(modeController);
 exportHook.setView(mapView);
 exportHook.setPrintable(printable);
 exportHook.setMapMode(mapMode);
 exportHook.setContentManager(contentManager);
 exportHook.setResourceBundle(resourceBundle);
 }

 @Test
 public void testChooseFile_ValidImageType_ReturnsSelectedFile() throws IOException {
 final String imageType = "png";
 final String description = "Export image as PNG";
 final File selectedFile = new File("test/exportedFile.png");
 final FileDialog chooser = mock(FileDialog.class);
 when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(chooser.getSelectedFile()).thenReturn(selectedFile);

 final ArgumentCaptor<File> fileArgumentCaptor = ArgumentCaptor.forClass(File.class);
 exportHook.chooseImageFile(imageType, description, null, modeController);
 verify(modeController).getFileChooser(argThat(f -> f.accepts(selectedFile)));
 verify(chooser).setSelectedFile(fileArgumentCaptor.capture());
 assertEquals("test/exportedFile.png", fileArgumentCaptor.getValue().getAbsolutePath());
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_NullMap_ReturnsNull() {
 exportHook.setMap(null);
 final File file = exportHook.chooseImageFile("png", "Export image as PNG", null, modeController);
 assertNull(file);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceString_NullResourceName_ReturnsNull() {
 assertNull(exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName_ReturnsTranslatedString() {
 final String resourceName = "file_already_exists";
 final String translatedString = "File already exists.";
 when(resourceBundle.getString(resourceName)).thenReturn(resourceName);
 when(modeController.getText(resourceName)).thenReturn(translatedString);
 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals("File already exists.", result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView_ReturnsNull() {
 exportHook.setView(null);
 final BufferedImage result = exportHook.createBufferedImage();
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_ValidView_ReturnsBufferedImage() {
 final int width = 100;
 final int height = 100;
 when(mapView.getWidth()).thenReturn(width);
 when(mapView.getHeight()).thenReturn(height);
 when(mapView.preparePrinting()).thenReturn(true);
 when(mapView.createImage(width, height)).thenReturn(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
 when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, width, height));
 when(mapView.print(any(Graphics.class))).thenReturn(true);
 when(mapView.endPrinting()).thenReturn(true);

 final BufferedImage result = exportHook.createBufferedImage();
 assertEquals(width, result.getWidth());
 assertEquals(height, result.getHeight());
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullPrefix_DoesNothing() {
 exportHook.copyFromResource(null, "testFile.txt", null);
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName_CopiesFile() {
 final String prefix = "freemind/extensions/ExportHook/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "test/copiedFiles/";
 final URL resource = mock(URL.class);
 final InputStream inputStream = mock(InputStream.class);
 final OutputStream outputStream = mock(OutputStream.class);
 when(resource.openStream()).thenReturn(inputStream);
 when(exportHook.getResource(prefix + fileName)).thenReturn(resource);
 when(resource.openConnection()).thenReturn(mock(URLConnection.class));
 when(resource.openStream()).thenReturn(inputStream);
 when(resource.getFile()).thenReturn(new File(prefix + fileName));
 when(contentManager.getFile(destinationDirectory + "/" + fileName)).thenReturn(null);
 when(contentManager.saveFile(destinationDirectory + "/" + fileName, inputStream)).thenReturn(outputStream);

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(resource).openStream();
 verify(contentManager).saveFile(eq(destinationDirectory + "/" + fileName), eq(inputStream));
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDir_DoesNothing() {
 exportHook.copyFromFile(null, "testFile.txt", null);
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName_CopiesFile() {
 final String dir = "test/resources/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "test/copiedFiles/";
 final File file = new File(dir, fileName);
 final InputStream inputStream = mock(InputStream.class);
 final OutputStream outputStream = mock(OutputStream.class);
 when(file.exists()).thenReturn(true);
 when(contentManager.getFile(destinationDirectory + "/" + fileName)).thenReturn(null);
 when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
 when(file.length()).thenReturn(1024);

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(file.length()).times(1).thenReturn(1024);
 verify(contentManager).saveFile(eq(destinationDirectory + "/" + fileName), eq(inputStream));
 }
}