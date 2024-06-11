package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import freemind.controller.Controller;
import freemind.extensions.ExportHook;
import freemind.extensions.ExportHook.ImageFilter;
import freemind.main.Logger;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private Controller controller;
 private MapView view;
 private Logger logger;

 @Before
 public void setUp() {
 controller = new Controller();
 exportHook = new ExportHook();
 exportHook.setController(controller);
 logger = new Logger() {
 @Override
 public void severe(String msg) {
 System.out.println("SEVERE: " + msg);
 }

 @Override
 public void finer(String msg) {
 System.out.println("FINE: " + msg);
 }
 };
 exportHook.setLogger(logger);
 view = new MapView(controller);
 }

 @After
 public void tearDown() {
 // Clean up any resources created during the test
 controller = null;
 exportHook = null;
 logger = null;
 view = null;
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final Point location = new Point(0, 0);
 final File expectedResult = new File("path/to/expected/result.png");

 final File result = exportHook.chooseFile(type, description, nameExtension);
 assertNotNull(result);
 assertEquals(expectedResult.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testChooseFile_NullMapFile() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final File expectedResult = null;

 final File result = exportHook.chooseFile(type, description, nameExtension, null);
 assertNull(result);
 }

 @Test
 public void testChooseImageFile() throws IOException {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final Controller controller = new Controller();
 final JFrame frame = controller.getFrame();
 final ImageFilter filter = new ExportHook.ImageFilter("png", "PNG files");
 File chooser = controller.getFileChooser(filter);
 final File mmFile = new File("path/to/map/file.mm");
 final String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + ((nameExtension != null) ? nameExtension : "") + "." + type;
 chooser.setSelectedFile(new File(proposedName));
 final int returnVal = SwingUtilities.invokeAndWait(() -> chooser.showSaveDialog(frame.getContentPane()));
 final File chosenFile = (returnVal == 0) ? chooser.getSelectedFile() : null;
 final File expectedResult = new File("path/to/expected/result.png");

 if (chosenFile != null) {
 assertEquals(expectedResult.getAbsolutePath(), chosenFile.getAbsolutePath());
 } else {
 assertNull(chosenFile);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testChooseImageFile_NullController() {
 final String type = "png";
 final String description = "PNG files (*.png)";
 final String nameExtension = null;
 final Controller controller = null;
 final JFrame frame = new JFrame();
 final ImageFilter filter = new ExportHook.ImageFilter("png", "PNG files");
 File chooser = controller.getFileChooser(filter);
 final File mmFile = new File("path/to/map/file.mm");
 final String proposedName = mmFile.getAbsolutePath().replaceFirst("\\.[^.]*?$", "") + ((nameExtension != null) ? nameExtension : "") + "." + type;
 chooser.setSelectedFile(new File(proposedName));
 SwingUtilities.invokeAndWait(() -> chooser.showSaveDialog(frame.getContentPane()));
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resourceKey%";
 final String expectedResult = "Translated resource key";

 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName() {
 final String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage() {
 final Image expectedImage = ImageIO.read(new File("path/to/expected/image.png"));
 final BufferedImage resultImage = exportHook.createBufferedImage();
 assertEquals(expectedImage.getWidth(null), resultImage.getWidth());
 assertEquals(expectedImage.getHeight(null), resultImage.getHeight());
 assertTrue(ImageIO.write(resultImage, "png", new File("path/to/expected/result.png")));
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 exportHook.view = null;
 exportHook.createBufferedImage();
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "freemind/extensions/ExportHook/";
 final String fileName = "testImage.png";
 final String destinationDirectory = "path/to/destination/directory/";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File destinationFile = new File(destinationDirectory, fileName);
 assertTrue(destinationFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_FileNotFound() {
 final String prefix = "freemind/extensions/ExportHook/nonExistentFile.png";
 final String destinationDirectory = "path/to/destination/directory/";

 exportHook.copyFromResource(prefix, null, destinationDirectory);
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "path/to/source/directory/";
 final String fileName = "testImage.png";
 final String destinationDirectory = "path/to/destination/directory/";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File destinationFile = new File(destinationDirectory, fileName);
 assertTrue(destinationFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_FileNotFound() {
 final String dir = "path/to/source/directory/nonExistentFile.png";
 final String destinationDirectory = "path/to/destination/directory/";

 exportHook.copyFromFile(dir, null, destinationDirectory);
 }

 @Test(expected = IOException.class)
 public void testCopyFromFile_IOException() {
 final String dir = "path/to/source/directory/testImage.png";
 final String destinationDirectory = "path/to/destination/directory/";
 final File destinationFile = new File(destinationDirectory, "testImage_copied.png");
 final File overwrittenFile = new File(destinationDirectory, "testImage_overwritten.png");

 try {
 exportHook.copyFromFile(dir, "testImage.png", destinationDirectory);
 fail("Expected IOException when file already exists");
 } catch (IOException e) {
 // expected
 }

 try {
 exportHook.copyFromFile(dir, "testImage.png", destinationDirectory);
 exportHook.copyFromFile(dir, "testImage.png", destinationDirectory);
 // should overwrite the existing file
 File copiedFile = new File(destinationDirectory, "testImage_copied.png");
 assertTrue(copiedFile.exists());
 } catch (IOException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}