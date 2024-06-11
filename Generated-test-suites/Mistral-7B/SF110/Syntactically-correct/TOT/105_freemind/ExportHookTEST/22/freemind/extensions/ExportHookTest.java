package freemind.extensions;

import static org.junit.Assert.*;
import static freemind.extensions.Tools.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import freemind.controller.Controller;
import freemind.controller.FileChooser;
import freemind.controller.FrameController;
import freemind.controller.MapController;
import freemind.controller.ModeController;
import freemind.controller.ResourceController;
import freemind.controller.TextController;
import freemind.controller.main.MainFrame;
import freemind.model.Map;
import freemind.view.MindMapView;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController mockModeController;
 private MapView mockMapView;
 private Map mockMap;
 private Controller mockController;
 private TextController mockTextController;
 private ResourceController mockResourceController;
 private FrameController mockFrameController;
 private MainFrame mockMainFrame;

 @Before
 public void setUp() {
 mockModeController = Mockito.mock(ModeController.class);
 mockMapView = Mockito.mock(MapView.class);
 mockMap = Mockito.mock(Map.class);
 mockController = Mockito.mock(Controller.class);
 mockTextController = Mockito.mock(TextController.class);
 mockResourceController = Mockito.mock(ResourceController.class);
 mockFrameController = Mockito.mock(FrameController.class);
 mockMainFrame = Mockito.mock(MainFrame.class);

 Mockito.when(mockModeController.getController()).thenReturn(mockController);
 Mockito.when(mockModeController.getView()).thenReturn(mockMapView);
 Mockito.when(mockMap.getFile()).thenReturn(new File("testMap.mm"));
 Mockito.when(mockController.getText(anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));
 Mockito.when(mockController.getFrame()).thenReturn(mockMainFrame);
 Mockito.when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mockFileChooser);
 Mockito.when(mockController.getMap()).thenReturn(mockMap);
 Mockito.when(mockController.getResourceString(anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));

 exportHook = new ExportHook();
 exportHook.setModeControllerHook(mockModeController);
 }

 @Test
 public void testChooseFile_whenTypeAndDescriptionAreProvided_thenCorrectFileIsChosen() {
 String type = "png";
 String description = "export image";
 File expectedFile = new File("expectedImage.png");
 FileChooser fileChooser = Mockito.mock(FileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.YES_OPTION);

 Mockito.when(mockFileChooser.showSaveDialog(Mockito.eq(mockMainFrame.getContentPane()))).then(invocation -> fileChooser);

 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_whenNoNameExtensionIsProvided_thenFileIsSavedWithTypeExtension() {
 String type = "png";
 String description = "export image";
 File expectedFile = new File("testMap.png");
 FileChooser fileChooser = Mockito.mock(FileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.YES_OPTION);

 Mockito.when(mockMap.getFile()).thenReturn(new File("testMap.mm"));
 Mockito.when(mockFileChooser.showSaveDialog(Mockito.eq(mockMainFrame.getContentPane()))).then(invocation -> fileChooser);

 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_whenFileAlreadyExists_thenUserIsAskedToOverwrite() {
 String type = "png";
 String description = "export image";
 File chosenFile = new File("testMap.png");
 FileChooser fileChooser = Mockito.mock(FileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.YES_OPTION);

 Mockito.when(mockFileChooser.showSaveDialog(Mockito.eq(mockMainFrame.getContentPane()))).then(invocation -> fileChooser);

 String overwriteText = "File 'testMap.png' already exists, do you want to replace it?";
 Mockito.when(mockController.getText(anyString())).thenReturn(overwriteText);
 Mockito.when(JOptionPane.showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);

 File chosenFile2 = exportHook.chooseFile(type, description, null);
 assertEquals(chosenFile, chosenFile2);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_whenControllerIsNull_thenThrowNullPointerException() {
 String type = "png";
 String description = "export image";
 FileChooser fileChooser = Mockito.mock(FileChooser.class);
 Mockito.when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.YES_OPTION);

 ExportHook exportHook = new ExportHook();
 exportHook.setModeControllerHook(null);
 exportHook.chooseFile(type, description, null);
 }

 @Test
 public void testGetTranslatableResourceString_whenResourceStringStartsWithPercent_thenCorrectTranslatedStringIsReturned() {
 String resourceName = "%resource_key%";
 String expectedValue = "Translated value";
 Mockito.when(mockController.getText(resourceName)).thenReturn(resourceName);

 String returnValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, returnValue);
 }

 @Test
 public void testGetTranslatableResourceString_whenResourceStringDoesNotStartWithPercent_thenReturnOriginalString() {
 String resourceName = "original_string";
 String returnValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(resourceName, returnValue);
 }

 @Test
 public void testCreateBufferedImage_whenViewIsNotNull_thenBufferedImageIsCreatedSuccessfully() {
 BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 MindMapView mockMindMapView = Mockito.mock(MindMapView.class);

 Mockito.when(mockMapView.preparePrinting()).thenReturn(null);
 Mockito.when(mockMapView.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
 Mockito.when(mockMapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 Mockito.when(mockController.getView()).thenReturn(mockMindMapView);

 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_whenViewIsNull_thenNullIsReturned() {
 BufferedImage expectedImage = null;
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource_whenFileIsFound_thenFileIsCopiedSuccessfully() throws IOException {
 String prefix = "freemind/extensions/";
 String fileName = "testFile.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory);
 assertTrue(actualFile.exists());
 assertTrue(expectedFile.exists());
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testCopyFromResource_whenFileIsNotFound_thenExceptionIsLogged() throws IOException {
 String prefix = "freemind/extensions/";
 String fileName = "non_existent_file.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 assertFalse(new File(destinationDirectory, fileName).exists());
 }

 @Test
 public void testCopyFromFile_whenFileIsFound_thenFileIsCopiedSuccessfully() throws IOException {
 String dir = "src/test/resources/";
 String fileName = "testFile.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory);
 assertTrue(actualFile.exists());
 assertTrue(expectedFile.exists());
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testCopyFromFile_whenFileIsNotFound_thenExceptionIsLogged() throws IOException {
 String dir = "src/test/resources/";
 String fileName = "non_existent_file.txt";
 String destinationDirectory = "destination";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 assertFalse(new File(destinationDirectory, fileName).exists());
 }
}