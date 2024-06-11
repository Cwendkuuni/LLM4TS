package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import org.mockito.ArgumentMatchers.*;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ModeController mockController;
 private ExportHook exportHook;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockController = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(mockController);
 }

 @Test
 public void testChooseFile_WhenCalledWithValidArguments_ReturnsExpectedFile() {
 String type = "image/png";
 String description = "Export image file";
 String nameExtension = "png";
 File expectedFile = new File("path/to/expected/file.png");

 when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(mockController.getText(anyString())).thenReturn("File save dialog description");
 when(mockController.getMap()).thenReturn(mock(Map.class));
 when(mockController.getFrame()).thenReturn(mock(JFrame.class));
 when(mockController.getController()).thenReturn(mockController);

 File result = exportHook.chooseFile(type, description, nameExtension);

 assertEquals(expectedFile, result);
 verify(mockController, times(1)).getFileChooser(argThat(isA(ImageFilter.class)));
 verify(mockController, times(1)).getText("File save dialog description");
 verify(mockController, times(1)).getMap();
 verify(mockController, times(1)).getFrame();
 verify(mockController, times(1)).getController();
 }

 @Test
 public void testChooseFile_WhenCalledWithNullController_ReturnsNull() {
 exportHook.setController(null);

 assertNull(exportHook.chooseFile("image/png", "Export image file", null));
 }

 @Test
 public void testGetTranslatableResourceString_WhenCalledWithValidArgument_ReturnsExpectedValue() {
 String resourceName = "%resources.file_save_as";
 String expectedValue = "File save as...";

 String result = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedValue, result);
 }

 @Test
 public void testGetTranslatableResourceString_WhenCalledWithNullArgument_ReturnsNull() {
 String resourceName = null;

 String result = exportHook.getTranslatableResourceString(resourceName);

 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_WhenCalled_ReturnsExpectedBufferedImage() {
 View view = mock(MapView.class);
 when(mockController.getView()).thenReturn(view);

 BufferedImage expectedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

 BufferedImage result = exportHook.createBufferedImage();

 assertSame(expectedImage, result);
 }

 @Test
 public void testCreateBufferedImage_WhenCalledWithNullController_ReturnsNull() {
 exportHook.setController(null);

 assertNull(exportHook.createBufferedImage());
 }

}