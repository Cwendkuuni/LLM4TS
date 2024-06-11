package freemind.extensions;

import static org.junit.Assert.*;
import freemind.view.mindmapview.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import org.junit.*;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController controller;
 private MapView view;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 controller = Mockito.mock(ModeController.class);
 view = Mockito.mock(MapView.class);
 }

 @Test
 public void testChooseFile_ValidInput_ReturnsFile() {
 String type = "png";
 String description = "Image file";
 File expectedFile = new File("path/to/expected/file.png");
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 when(controller.getText(eq("file_already_exists"))).thenReturn("File already exists.");
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 doReturn(expectedFile).when(controller).getMap();
 doReturn(controller).when(exportHook).getController();
 doReturn(view).when(exportHook).getView();

 File result = exportHook.chooseFile(type, description, null);
 assertTrue(result.exists());
 assertEquals(expectedFile, result);
 }

 @Test
 public void testChooseFile_InvalidInput_ReturnsNull() {
 String type = "png";
 String description = "Image file";
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 doReturn(null).when(controller).getMap();
 doReturn(exportHook).when(exportHook).getController();
 doReturn(view).when(exportHook).getView();

 File result = exportHook.chooseFile(type, description, null);
 assertNull(result);
 }

 @Test
 public void testChooseFile_TypeMismatch_ReturnsNull() {
 String type = "png";
 String description = "Image file";
 File expectedFile = new File("path/to/expected/file.jpg");
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 doReturn(expectedFile).when(controller).getMap();
 doReturn(controller).when(exportHook).getController();
 doReturn(view).when(exportHook).getView();

 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 doReturn(new File("path/to/selected/file.txt")).when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(null);

 File result = exportHook.chooseFile(type, description, null);
 assertNull(result);
 }

 @Test
 public void testChooseFile_CancelButtonClicked_ReturnsNull() {
 String type = "png";
 String description = "Image file";
 when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 doReturn(null).when(controller).getMap();
 doReturn(controller).when(exportHook).getController();
 doReturn(view).when(exportHook).getView();

 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 doReturn(controller).when(controller.getFileChooser(any(JFileChooser.class))).thenReturn(Mockito.mock(JFileChooser.class));
 doReturn(Mockito.mock(JOptionPane.class)).when(controller).getText(anyString());
 doReturn(JOptionPane.NO_OPTION).when(controller).showConfirmDialog(any(Component.class), anyString(), anyString(), anyInt());

 File result = exportHook.chooseFile(type, description, null);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_ValidInput_ReturnsBufferedImage() {
 when(exportHook.getController()).thenReturn(controller);
 when(exportHook.getView()).thenReturn(view);
 doReturn(view).when(view).preparePrinting();
 doReturn(new Rectangle(0, 0, 10, 10)).when(view).getInnerBounds();
 doReturn(Mockito.mock(Graphics.class)).when(view).print(any(Graphics.class));
 doReturn(Mockito.mock(BufferedImage.class)).when(view).createImage(anyInt(), anyInt());
 doReturn(Mockito.mock(Graphics.class)).when(Mockito.mock(BufferedImage.class).getGraphics());
 doReturn(Mockito.mock(BufferedImage.class)).when(Mockito.mock(BufferedImage.class).getSubimage(anyInt(), anyInt(), anyInt(), anyInt()));
 doReturn(view).when(view).endPrinting();

 BufferedImage result = exportHook.createBufferedImage();
 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_ViewIsNull_ThrowsNullPointerException() {
 when(exportHook.getController()).thenReturn(controller);
 when(exportHook.getView()).thenReturn(null);

 exportHook.createBufferedImage();
 }

 @Test
 public void testGetTranslatableResourceString_ValidInput_ReturnsTranslatableString() {
 String resourceName = "test.key";
 String expectedString = "Test string.";
 when(exportHook.getController()).thenReturn(controller);
 doReturn(expectedString).when(controller).getText(resourceName);

 String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedString, result);
 }

 @Test
 public void testGetTranslatableResourceString_NullInput_ReturnsNull() {
 String resourceName = null;
 when(exportHook.getController()).thenReturn(controller);

 String result = exportHook.getTranslatableResourceString(resourceName);
 assertNull(result);
 }
}