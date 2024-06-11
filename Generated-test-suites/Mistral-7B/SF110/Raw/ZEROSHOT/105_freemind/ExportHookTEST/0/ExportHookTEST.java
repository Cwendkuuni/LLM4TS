package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.FreeMindFrame;
import freemind.main.FreeMindResources;
import freemind.modes.ModeControllerFactory;
import freemind.view.mindmapview.MapView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private FreeMindFrame freeMindFrame;
 private MapView mapView;

 @Before
 public void setUp() {
 modeController = Mockito.mock(ModeController.class);
 freeMindFrame = Mockito.mock(FreeMindFrame.class);
 mapView = Mockito.mock(MapView.class);
 exportHook = new ExportHook();
 exportHook.setController(modeController);
 }

 @After
 public void tearDown() {
 exportHook = null;
 modeController = null;
 freeMindFrame = null;
 mapView = null;
 }

 @Test
 public void testChooseFile() {
 String type = "png";
 String description = "PNG files (*.png)";
 File expectedChosenFile = new File("expectedPath/expectedFileName.png");
 File chosenFile = exportHook.chooseFile(type, description, null);
 assertEquals(expectedChosenFile, chosenFile);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullController() {
 ExportHook exportHookNullController = new ExportHook();
 exportHookNullController.setController(null);
 exportHookNullController.chooseFile("png", "PNG files (*.png)", null);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resourceKey%";
 String expectedReturnValue = "expectedReturnValue";
 String returnValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedReturnValue, returnValue);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage expectedBufferedImage = ImageIO.read(new File("expectedImagePath"));
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 assertEquals(expectedBufferedImage, bufferedImage);
 }

 @Test
 public void testCopyFromResource() {
 String prefix = "prefix";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 // Add assertions here, if needed
 }

 @Test
 public void testCopyFromFile() {
 String dir = "dir";
 String fileName = "fileName";
 String destinationDirectory = "destinationDirectory";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 // Add assertions here, if needed
 }

 @Test
 public void testImageFilterAccept() {
 FileFilter imageFilter = new ExportHook.ImageFilter("png", null);
 File file = new File("fileWithPngExtension");
 assertTrue(imageFilter.accept(file));
 }

 @Test
 public void testImageFilterGetDescription() {
 FileFilter imageFilter = new ExportHook.ImageFilter("png", "PNG files (*.png)");
 String description = imageFilter.getDescription();
 assertEquals("PNG files (*.png)", description);
 }
}