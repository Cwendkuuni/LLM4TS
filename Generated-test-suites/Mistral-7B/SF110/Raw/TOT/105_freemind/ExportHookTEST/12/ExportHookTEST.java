package freemind.extensions.test;

import static org.junit.Assert.*;
import static freemind.extensions.ExportHook.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.extensions.ExportHook;
import freemind.extensions.Logger;
import freemind.extensions.Resources;
import freemind.model.MapModel;
import freemind.view.MindMapView;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private Controller controller;
 private MapView view;
 private BufferedImage bufferedImage;

 @Before
 public void setUp() {
 controller = new Controller();
 exportHook = new ExportHook();
 exportHook.setController(controller);
 view = new MapView(controller);
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "Export as Image";
 String nameExtension = null;
 File expectedFile = new File("test.png");
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertSame(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_InvalidExtension() {
 String type = "txt";
 String description = "Export as Text";
 String nameExtension = null;
 File expectedFile = new File("test.txt");
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertSame(expectedFile, actualFile);

 File chosenFile = new File("test.png");
 actualFile = exportHook.chooseFile(type, description, nameExtension, controller);
 assertSame(chosenFile, actualFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%message.welcome";
 String expectedValue = "Welcome to FreeMind";
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 bufferedImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, bufferedImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NoView() {
 exportHook.view = null;
 bufferedImage = exportHook.createBufferedImage();
 fail("Should have thrown NullPointerException");
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "images/icons/";
 String fileName = "icon.png";
 String destinationDirectory = "test/output/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory, fileName);
 assertSame(expectedFile, actualFile);
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "src/test/resources/images/";
 String fileName = "icon.png";
 String destinationDirectory = "test/output/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 File actualFile = new File(destinationDirectory, fileName);
 assertSame(expectedFile, actualFile);
 }

 @Test
 public void testImageFilter() {
 ImageFilter imageFilter = new ImageFilter("png", "Export as PNG image");
 assertTrue(imageFilter.accept(new File("test.png")));
 assertTrue(imageFilter.accept(new File("test.txt")));
 }

 @Test
 public void testChooseImageFile() {
 String type = "png";
 String description = "Export as Image";
 String nameExtension = null;
 ModeController controller = this.controller;
 File expectedFile = new File("test.png");
 File actualFile = ExportHook.chooseImageFile(type, description, nameExtension, controller);
 assertSame(expectedFile, actualFile);
 }
}