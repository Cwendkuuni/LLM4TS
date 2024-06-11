package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;

 @Before
 public void setUp() {
 // Initialize objects
 exportHook = new ExportHook();
 modeController = Mockito.mock(ModeController.class);
 mapView = Mockito.mock(MapView.class);

 // Set up dependencies
 exportHook.setController(modeController);
 exportHook.setView(mapView);
 }

 @After
 public void tearDown() {
 // Release resources
 exportHook = null;
 modeController = null;
 mapView = null;
 }

 @Test
 public void testChooseFile() {
 // Test case 1: Choose a valid file
 String type = "png";
 String description = "Export as PNG image";
 String nameExtension = null;
 File expectedFile = new File("test/exportedFile.png");
 File actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, actualFile);

 // Test case 2: Choose a file with name extension
 type = "jpeg";
 nameExtension = "jpg";
 expectedFile = new File("test/exportedFile.jpg");
 actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, actualFile);

 // Test case 3: Do not choose a file
 type = "xml";
 File mmFile = new File("test/mapFile.xml");
 expectedFile = null;
 actualFile = exportHook.chooseFile(type, description, nameExtension);
 assertNull(actualFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 // Test case 1: Return a translatable string
 String resourceName = "%resourceString%";
 String expectedValue = "Translated value";
 String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);

 // Test case 2: Return a non-translatable string
 resourceName = "nonTranslatableString";
 expectedValue = "nonTranslatableValue";
 actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testCreateBufferedImage() {
 // Test case 1: Create a buffered image
 Image image = Mockito.mock(Image.class);
 when(mapView.getImage()).thenReturn(image);
 BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 when(mapView.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedImage);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);

 // Test case 2: Return null if map view is null
 when(mapView.getImage()).thenReturn(null);
 actualImage = exportHook.createBufferedImage();
 assertNull(actualImage);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 // Test case 1: Copy a file from a resource
 String prefix = "freemind/extensions/ExportHookTest/resources/";
 String fileName = "testFile.txt";
 String destinationDirectory = "test/output/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, "testFile.txt");
 File actualFile = new File(destinationDirectory, "testFile.txt");
 assertTrue(actualFile.exists());
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }

 @Test
 public void testCopyFromFile() throws IOException {
 // Test case 1: Copy a file from a file
 String dir = "test/input/";
 String fileName = "testFile.txt";
 String destinationDirectory = "test/output/";
 File expectedFile = new File(destinationDirectory, "testFile.txt");
 File actualFile = new File(destinationDirectory, "testFile.txt");
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 assertTrue(actualFile.exists());
 assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
 }
}