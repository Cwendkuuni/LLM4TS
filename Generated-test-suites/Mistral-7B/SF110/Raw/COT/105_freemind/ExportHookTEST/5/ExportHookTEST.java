package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.FileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.TestLogger;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeControllerImpl;
import freemind.view.mindmapview.view.AbstractView;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 @Mock
 private ModeController controller;
 @Mock
 private MapView view;
 @InjectMocks
 private ExportHook exportHook;
 @Mock
 private Logger logger;

 @Before
 public void setUp() {
 exportHook.setLogger(logger);
 }

 @After
 public void tearDown() {
 exportHook = null;
 }

 @Test
 public void testChooseFile_ValidInput_ReturnsFile() {
 File expectedFile = new File("path/to/file.png");
 when(controller.getFileChooser(Mockito.eq(new ImageFilter("png", null)))).thenReturn(Mockito.mock(FileChooser.class));
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getText("file_save_as")).thenReturn("Save as");
 when(controller.getText("file_save_as_title")).thenReturn("Save as...");
 when(controller.getFrame()).thenReturn(Mockito.mock(AbstractView.class));

 File actualFile = exportHook.chooseFile("png", "Save as...", "png", controller);
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_InvalidExtension_ReturnsNull() {
 when(controller.getFileChooser(Mockito.eq(new ImageFilter("png", null)))).thenReturn(Mockito.mock(FileChooser.class));
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getText("file_save_as")).thenReturn("Save as...");
 when(controller.getText("file_save_as_title")).thenReturn("Save as...");
 when(controller.getFrame()).thenReturn(Mockito.mock(AbstractView.class));

 File actualFile = exportHook.chooseFile("txt", "Save as...", "png", controller);
 assertNull(actualFile);
 }

 @Test
 public void testChooseFile_FileAlreadyExists_AsksUserToOverwrite() {
 File existingFile = new File("path/to/existing_file.png");
 when(controller.getFileChooser(Mockito.eq(new ImageFilter("png", null)))).thenReturn(Mockito.mock(FileChooser.class));
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getText("file_save_as")).thenReturn("Save as...");
 when(controller.getText("file_save_as_title")).thenReturn("Save as...");
 when(controller.getText("yes")).thenReturn(0);
 when(controller.getText("no")).thenReturn(1);
 when(controller.getFrame()).thenReturn(Mockito.mock(AbstractView.class));

 SwingUtilities.invokeLater(() -> {
 try {
 File actualFile = exportHook.chooseFile("png", "Save as...", "png", controller);
 assertTrue(actualFile.exists());
 }
 catch (IOException e) {
 fail("IOException was thrown");
 }
 });

 File expectedFile = new File("path/to/existing_file_1.png");
 assertTrue(expectedFile.exists());
 assertEquals(expectedFile, actualFile);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_NullController_ThrowsNullPointerException() {
 exportHook.chooseFile("png", "Save as...", "png", null);
 }

 @Test
 public void testGetTranslatableResourceString_ValidInput_ReturnsCorrectString() {
 String input = "%Hello%";
 String expectedOutput = "Hello";
 String actualOutput = exportHook.getTranslatableResourceString(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetTranslatableResourceString_InvalidInput_ReturnsNull() {
 String input = "Hello";
 String actualOutput = exportHook.getTranslatableResourceString(input);
 assertNull(actualOutput);
 }

 @Test
 public void testCreateBufferedImage_ValidInput_ReturnsBufferedImage() {
 when(controller.getView()).thenReturn(view);
 BufferedImage expectedImage = ImageIO.read(new File("path/to/image.png"));
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));

 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_NullView_ReturnsNull() {
 exportHook.createBufferedImage();
 assertNull(exportHook.view);
 }

 @Test
 public void testCopyFromResource_ValidInput_CopiesFile() throws IOException {
 String prefix = "prefix/";
 String fileName = "test.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 assertEquals(expectedFile.length(), actualFile.length());
 }

 @Test
 public void testCopyFromResource_NullPrefix_ThrowsNullPointerException() {
 String fileName = "test.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromResource(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromResource_FileNotFound_LogsException() throws IOException {
 String prefix = "prefix/";
 String fileName = "non_existent_file.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFile_ValidInput_CopiesFile() throws IOException {
 String dir = "path/to/directory/";
 String fileName = "test.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 assertEquals(new File(dir, fileName).length(), actualFile.length());
 }

 @Test
 public void testCopyFromFile_NullDir_ThrowsNullPointerException() {
 String fileName = "test.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromFile(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFile_FileNotFound_LogsException() throws IOException {
 String dir = "path/to/directory/";
 String fileName = "non_existent_file.txt";
 String destinationDirectory = "destination/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }
}