package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.view.viewtools.View;

public class ExportHookTest {

 private ModeController controller;
 private MapView view;
 private ExportHook exportHook;

 @Before
 public void setUp() {
 controller = new MapController();
 view = new MapView(controller);
 exportHook = new ExportHook() {
 @Override
 public MapView getController() {
 return controller;
 }
 };
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG Image (*.png)";
 final File expectedFile = new File("expectedFilePath/image.png");
 final File resultFile = exportHook.chooseFile(type, description, null);
 assertTrue("Result file should be equal to expected file", expectedFile.equals(resultFile));
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWhenControllerIsNull() {
 exportHook = new ExportHook() {
 @Override
 public MapView getController() {
 return null;
 }
 };
 final String type = "png";
 final String description = "PNG Image (*.png)";
 final File resultFile = exportHook.chooseFile(type, description, null);
 fail("Should throw NullPointerException when controller is null");
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resourceString%";
 final String expected = "expectedString";
 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals("Result string should be equal to expected", expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceStringWhenResourceNameIsNull() {
 final String result = exportHook.getTranslatableResourceString(null);
 fail("Should throw NullPointerException when resourceName is null");
 }

 @Test
 public void testCreateBufferedImage() {
 final Image expectedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 final Image resultImage = exportHook.createBufferedImage();
 assertEquals("Result image should be equal to expected image", expectedImage, resultImage);
 }

 @Test
 public void testCreateBufferedImageWhenViewIsNull() {
 exportHook = new ExportHook() {
 @Override
 public MapView getController() {
 return controller;
 }
 };
 exportHook.view = null;
 final Image resultImage = exportHook.createBufferedImage();
 assertNull("Result image should be null when view is null", resultImage);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 final String prefix = "prefix";
 final String fileName = "file.txt";
 final String destinationDirectory = "destinationDirectory";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 final File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue("File should be created", expectedFile.exists());
 }

 @Test
 public void testCopyFromFile() throws IOException {
 final String dir = "sourceDirectory";
 final String fileName = "file.txt";
 final String destinationDirectory = "destinationDirectory";
 File sourceFile = new File(dir, fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 final File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue("File should be created and be equal to source file", expectedFile.exists() && expectedFile.equals(sourceFile));
 }

 @Test
 public void testCopyFromFileWhenFileNotFound() {
 final String dir = "nonexistentDirectory";
 final String fileName = "file.txt";
 final String destinationDirectory = "destinationDirectory";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 assertFalse("File should not be created when file not found", new File(destinationDirectory + "/" + fileName).exists());
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWhenDirIsNull() {
 exportHook.copyFromFile(null, "file.txt", "destinationDirectory");
 fail("Should throw NullPointerException when dir is null");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWhenFileNameIsNull() {
 exportHook.copyFromFile("sourceDirectory", null, "destinationDirectory");
 fail("Should throw NullPointerException when fileName is null");
 }
}