package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.main.Controller;
import freemind.modes.ModeController;

public class ExportHookTest {

 private Controller controller;
 private ExportHook exportHook;
 private MapView view;

 @Before
 public void setUp() {
 controller = new Controller();
 exportHook = new ExportHook();
 view = new MapView(controller);
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "PNG Images";
 String fileName = "test.png";
 File expectedFile = new File("test.png");
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_NullType() {
 String description = "PNG Images";
 String fileName = "test.png";
 File chosenFile = exportHook.chooseFile(null, description, null);
 assertNull(chosenFile);
 }

 @Test
 public void testChooseFile_NullDescription() {
 String type = "png";
 File chosenFile = exportHook.chooseFile(type, null, null);
 assertNull(chosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertSame(expectedImage, actualImage);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateBufferedImage_NoView() {
 exportHook.createBufferedImage();
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%message.greeting";
 String expectedValue = controller.getText("message.greeting");
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName() {
 String actualValue = exportHook.getTranslatableResourceString(null);
 assertNull(actualValue);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "path/to/resources/";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromResource_NullPrefix() {
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromResource(null, fileName, destinationDirectory);
 assertFalse(new File(destinationDirectory, fileName).exists());
 }

 @Test
 public void testCopyFromResource_NullFileName() {
 String prefix = "path/to/resources/";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromResource(prefix, null, destinationDirectory);
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "path/to/source/";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 File sourceFile = new File(dir, fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_NullDir() {
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromFile(null, fileName, destinationDirectory);
 assertFalse(new File(destinationDirectory, fileName).exists());
 }

 @Test
 public void testCopyFromFile_NullFileName() {
 String dir = "path/to/source/";
 String destinationDirectory = "path/to/destination/";
 exportHook.copyFromFile(dir, null, destinationDirectory);
 }
}