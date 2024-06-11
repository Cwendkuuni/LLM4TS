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
import java.text.*;
import org.junit.*;
import org.mockito.*;

public class ExportHookTest {

 @Before
 public void setUp() {
 ModeController controllerMock = mock(ModeController.class);
 view = mock(MapView.class);
 when(controllerMock.getView()).thenReturn(view);
 ExportHook exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(null, "description", null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(exportHook.chooseFile("type", null, null));
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile("type", "description", null));
 }

 @Test
 public void testChooseFileValidFileName() throws IOException {
 String expectedFileName = "testExport.png";
 when(controllerMock.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
 File chosenFile = new File("path/to/testExport.png");
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(mock(MindMapFile.class));
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));

 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);

 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);

 File actualFile = exportHook.chooseFile("image/*", "Save Image", "png");
 assertEquals(expectedFileName, actualFile.getName());
 assertEquals("path/to/testExport.png", actualFile.getAbsolutePath());

 verify(controllerMock).getText("file_already_exists");
 verify(controllerMock).getFileChooser(any(ImageFilter.class));
 verify(controllerMock).getMap();
 verify(controllerMock.getFrame()).getContentPane();
 verify(controllerMock.getFileChooser(eq(new ImageFilter("image/*", null))), times(2)).showSaveDialog(any(Container.class));
 }

 @Test
 public void testChooseFileInvalidFileNameExtension() throws IOException {
 String expectedFileName = "testExport.txt";
 when(controllerMock.getText("file_already_exists")).thenReturn("File already exists. Do you want to overwrite it?");
 File chosenFile = new File("path/to/testExport.txt");
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(controllerMock.getMap()).thenReturn(mock(MindMapFile.class));
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 when(controllerMock.getFrame()).thenReturn(mock(JFrame.class));

 JFileChooser fileChooser = mock(JFileChooser.class);
 when(fileChooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);

 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);

 File actualFile = exportHook.chooseFile("image/*", "Save Image", null);
 assertEquals(expectedFileName, actualFile.getName());
 assertEquals("path/to/testExport.txt", actualFile.getAbsolutePath());
 assertEquals("png", actualFile.getAbsolutePath().substring(actualFile.getAbsolutePath().lastIndexOf(".")));

 verify(controllerMock).getText("file_already_exists");
 verify(controllerMock).getFileChooser(any(ImageFilter.class));
 verify(controllerMock).getMap();
 verify(controllerMock.getFrame()).getContentPane();
 verify(controllerMock.getFileChooser(eq(new ImageFilter("image/*", null))), times(2)).showSaveDialog(any(Container.class));
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 String expectedResult = "%testResource";
 String result = exportHook.getTranslatableResourceString(expectedResult);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidView() throws Exception {
 BufferedImage expectedBufferedImage = ImageIO.read(new File("testImage.png"));
 when(view.preparePrinting()).thenReturn(true);
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedBufferedImage);
 when(view.endPrinting()).thenReturn(true);
 BufferedImage actualBufferedImage = exportHook.createBufferedImage();
 assertNotNull(actualBufferedImage);
 assertEquals(expectedBufferedImage, actualBufferedImage);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 exportHook.copyFromResource(null, "testResource.txt", "path/to/destination");
 }

 @Test
 public void testCopyFromResourceValidPrefixAndFileName() throws IOException {
 String expectedFilePath = "path/to/destination/testResource.txt";
 exportHook.copyFromResource("prefix/", "testResource.txt", "path/to/destination");
 assertTrue(new File(expectedFilePath).exists());
 }

 @Test
 public void testCopyFromFileNullDir() {
 exportHook.copyFromFile(null, "testResource.txt", "path/to/destination");
 }

 @Test
 public void testCopyFromFileValidDirAndFileName() throws IOException {
 String expectedFilePath = "path/to/destination/testResource.txt";
 exportHook.copyFromFile("path/to/source", "testResource.txt", "path/to/destination");
 assertTrue(new File(expectedFilePath).exists());
 }
}