package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.ResourceManager;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MindMapViewManager;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;
 private ResourceManager resourceManager;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 modeController = mock(ModeController.class);
 mapView = mock(MapView.class);
 resourceManager = mock(ResourceManager.class);
 when(modeController.getResourceManager()).thenReturn(resourceManager);
 when(modeController.getFrame()).thenReturn(new JFrame());
 when(modeController.getText(anyString())).thenAnswer((Answer<String>) invocation -> invocation.getArgumentAt(0, 0));
 when(modeController.getController()).thenReturn(modeController);
 when(modeController.getMap()).thenReturn(new File("testMap.mmf"));
 when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());
 when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(mapView.createImage(anyInt(), anyInt())).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
 when(mapView.getWidth()).thenReturn(100);
 when(mapView.getHeight()).thenReturn(100);
 }

 // Test chooseFile method with valid inputs
 @Test
 public void testChooseFileValidInput() {
 File result = exportHook.chooseFile("png", "Save PNG image", null);
 assertNotNull(result);
 }

 // Test chooseFile method with null input
 @Test(expected = NullPointerException.class)
 public void testChooseFileNullInput() {
 exportHook.chooseFile(null, null, null);
 }

 // Test chooseFile method with null controller input
 @Test(expected = NullPointerException.class)
 public void testChooseFileNullControllerInput() {
 exportHook = new ExportHook();
 exportHook.chooseFile("png", "Save PNG image", null);
 }

 // Test createBufferedImage method with valid input
 @Test
 public void testCreateBufferedImage() {
 BufferedImage result = exportHook.createBufferedImage();
 assertNotNull(result);
 }

 // Test createBufferedImage method with null view input
 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImageNullViewInput() {
 exportHook.createBufferedImage();
 }

 // Test getTranslatableResourceString method with valid input
 @Test
 public void testGetTranslatableResourceStringValidInput() {
 String result = exportHook.getTranslatableResourceString("%0");
 String expected = "File not found or could not be copied.";
 assertEquals(expected, result);
 }

 // Test getTranslatableResourceString method with null input
 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceStringNullInput() {
 exportHook.getTranslatableResourceString(null);
 }

 // Test copyFromResource method with valid input
 @Test
 public void testCopyFromResourceValidInput() {
 exportHook.copyFromResource("prefix", "fileName.txt", "destinationDirectory");
 assertTrue(true);
 }

 // Test copyFromResource method with null prefix input
 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullPrefixInput() {
 exportHook.copyFromResource(null, "fileName.txt", "destinationDirectory");
 }

 // Test copyFromResource method with null fileName input
 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullFileNameInput() {
 exportHook.copyFromResource("prefix", null, "destinationDirectory");
 }

 // Test copyFromResource method with null destinationDirectory input
 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullDestinationDirectoryInput() {
 exportHook.copyFromResource("prefix", "fileName.txt", null);
 }

 // Test copyFromFile method with valid input
 @Test
 public void testCopyFromFileValidInput() {
 exportHook.copyFromFile("srcDir", "fileName.txt", "destinationDirectory");
 assertTrue(true);
 }

 // Test copyFromFile method with null srcDir input
 @Test(expected = NullPointerException.class)
 public void testCopyFromFileNullSrcDirInput() {
 exportHook.copyFromFile(null, "fileName.txt", "destinationDirectory");
 }

 // Test copyFromFile method with null fileName input
 @Test(expected = NullPointerException.class)
 public void testCopyFromFileNullFileNameInput() {
 exportHook.copyFromFile("srcDir", null, "destinationDirectory");
 }

 // Test copyFromFile method with null destinationDirectory input
 @Test(expected = NullPointerException.class)
 public void testCopyFromFileNullDestinationDirectoryInput() {
 exportHook.copyFromFile("srcDir", "fileName.txt", null);
 }

 // Test chooseImageFile method with valid input
 @Test
 public void testChooseImageFileValidInput() {
 File result = ExportHook.chooseImageFile("png", "Save PNG image", null, modeController);
 assertNotNull(result);
 }

 // Test chooseImageFile method with null type input
 @Test(expected = NullPointerException.class)
 public void testChooseImageFileNullTypeInput() {
 ExportHook.chooseImageFile(null, "Save PNG image", null, modeController);
 }

 // Test chooseImageFile method with null description input
 @Test
 public void testChooseImageFileNullDescriptionInput() {
 ExportHook.chooseImageFile("png", null, null, modeController);
 }

 // Test chooseImageFile method with null controller input
 @Test(expected = NullPointerException.class)
 public void testChooseImageFileNullControllerInput() {
 ExportHook.chooseImageFile("png", "Save PNG image", null, null);
 }

 // Test chooseImageFile method with file already exists
 @Test
 public void testChooseImageFileFileAlreadyExists() {
 final String overwriteText = MessageFormat.format(modeController.getText("file_already_exists"), "testFile.png");
 final int overwriteMap = JOptionPane.showConfirmDialog(modeController.getFrame().getContentPane(), overwriteText, overwriteText, JOptionPane.DEFAULT_OPTION);
 assertTrue(overwriteMap == JOptionPane.YES_OPTION);
 File result = ExportHook.chooseImageFile("png", "Save PNG image", null, modeController);
 assertNotNull(result);
 }

 // Test chooseImageFile method with invalid file extension
 @Test
 public void testChooseImageFileInvalidFileExtension() {
 File result = ExportHook.chooseImageFile("jpg", "Save JPG image", null, modeController);
 assertNull(result);
 }
}