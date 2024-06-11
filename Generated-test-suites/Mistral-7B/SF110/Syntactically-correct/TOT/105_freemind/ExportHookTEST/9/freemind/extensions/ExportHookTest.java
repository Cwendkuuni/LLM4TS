package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.View;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.Logger;
import freemind.modes.MapController;

public class ExportHookTest {

 @Mock
 private Controller controller;
 @Mock
 private MapController mapController;
 @Mock
 private View view;
 @Mock
 private Logger logger;
 @InjectMocks
 private ExportHook hook;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(any(FileFilter.class))).thenReturn(mock(JFileChooser.class));
 when(controller.getText(anyString())).thenReturn("Test Text");
 when(controller.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(new File("testMap.mm"));
 }

 @Test
 public void testChooseFile_ValidFileType_ReturnsFile() {
 File expectedFile = new File("test.png");
 File result = hook.chooseFile("png", "Test Description", "png", controller);
 assertEquals(expectedFile, result);
 }

 @Test
 public void testChooseFile_InvalidFileType_ReturnsNull() {
 File result = hook.chooseFile("txt", "Test Description", "png", controller);
 assertNull(result);
 }

 @Test
 public void testChooseFile_FileAlreadyExists_AsksUserForOverwrite() throws IOException {
 File expectedFile = new File("test.png");
 expectedFile.createNewFile();
 String overwriteMessage = "File already exists. Do you want to replace it?";
 when(controller.getText("file_already_exists")).thenReturn(overwriteMessage);
 File result = hook.chooseFile("png", "Test Description", "png", controller);
 assertTrue(result != null);
 }

 @Test
 public void testChooseFile_FileDoesNotExist_DoesNotAskUserForOverwrite() throws IOException {
 File result = hook.chooseFile("png", "Test Description", "png", controller);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage result = hook.createBufferedImage();
 assertNotNull(result);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String result = hook.getTranslatableResourceString("%test%");
 assertEquals("Test Text", result);
 }

 @Test
 public void testGetTranslatableResourceString_NullInput_ReturnsNull() {
 String result = hook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 URL resource = new URL("test.png");
 hook.copyFromResource("test", "test.png", "test");
 verify(logger, times(1)).finest("searching for testtest.png");
 verify(controller, times(1)).getResource("testtest.png");
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_FileNotFound_ThrowsException() {
 hook.copyFromResource("test", "test.png", "test");
 }

 @Test
 public void testCopyFromFile() throws IOException {
 File resource = new File("test.png");
 hook.copyFromFile(".", "test.png", "test");
 verify(logger, times(1)).finest("searching for .test.png");
 verify(controller, times(1)).getFile(".");
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_FileNotFound_ThrowsException() {
 hook.copyFromFile(".", "test.png", "test");
 }
}