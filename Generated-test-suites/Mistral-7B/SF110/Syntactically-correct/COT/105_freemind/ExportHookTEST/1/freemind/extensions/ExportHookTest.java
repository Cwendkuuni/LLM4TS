package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.resources.Logger;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.view.View;
import freemind.controller.Controller;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private Controller controller;

 @Before
 public void setup() {
 exportHook = new ExportHook();
 modeController = mock(ModeController.class);
 controller = mock(Controller.class);
 when(modeController.getFrame()).thenReturn(new JFrame());
 when(modeController.getText(anyString())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(new JFileChooser());
 when(modeController.getMap()).thenReturn(new MindMapModel());
 when(controller.getView()).thenReturn(new MapView());
 when(controller.getModeController()).thenReturn(modeController);
 when(controller.getResourceString(anyString())).thenCallRealMethod();
 when(controller.getLogger()).thenReturn(new Logger());
 }

 @Test
 public void testChooseFile() {
 // Test with valid arguments
 String type = "png";
 String description = "PNG Image";
 String nameExtension = null;
 File expectedFile = new File("path/to/file.png");
 File actualFile = exportHook.chooseFile(type, description, nameExtension, controller);
 assertEquals(expectedFile, actualFile);

 // Test with null type
 type = null;
 assertNull(exportHook.chooseFile(type, description, nameExtension, controller));

 // Test with null description
 description = null;
 assertNull(exportHook.chooseFile(type, description, nameExtension, controller));

 // Test with null nameExtension
 nameExtension = null;
 assertNull(exportHook.chooseFile(type, description, null, controller));
 }

 @Test
 public void testGetTranslatableResourceString() {
 // Test with valid resourceString
 String resourceName = "%MESSAGE_KEY%";
 String expectedValue = "Message";
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);

 // Test with null resourceString
 resourceName = null;
 assertNull(exportHook.getTranslatableResourceString(resourceName));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImageWithNullView() {
 exportHook.createBufferedImage();
 fail("createBufferedImage should throw NullPointerException when view is null");
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 // Arrange
 MapView view = (MapView)controller.getView();
 BufferedImage expectedImage = ImageIO.read(getClass().getResource("/test.png"));

 // Act
 BufferedImage actualImage = exportHook.createBufferedImage();

 // Assert
 assertNotNull(actualImage);
 assertSame(expectedImage.getWidth(), actualImage.getWidth());
 assertSame(expectedImage.getHeight(), actualImage.getHeight());
 }

 @Test
 public void testCopyFromResource() {
 // Test with valid prefix, fileName, and destinationDirectory
 String prefix = "prefix/";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 // Verify that the file was copied
 File expectedFile = new File(destinationDirectory + "/test.txt");
 assertTrue(expectedFile.exists());

 // Test with null prefix
 prefix = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 // Test with null fileName
 fileName = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 // Test with null destinationDirectory
 destinationDirectory = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
}

 @Test
 public void testCopyFromFile() {
 // Test with valid dir, fileName, and destinationDirectory
 String dir = "path/to/source/";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 // Verify that the file was copied
 File expectedFile = new File(destinationDirectory + "/test.txt");
 assertTrue(expectedFile.exists());

 // Test with null dir
 dir = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 // Test with null fileName
 fileName = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromFile(prefix, null, destinationDirectory);

 // Test with null destinationDirectory
 destinationDirectory = null;
 assertNotNull(exportHook);
 // No exception should be thrown
 exportHook.copyFromFile(prefix, fileName, null);
}
}