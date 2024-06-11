package freemind.extensions.test;

import static org.junit.Assert.*;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;

 @Before
 public void setUp() {
 modeController = Mockito.mock(ModeController.class);
 mapView = Mockito.mock(MapView.class);
 exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return modeController;
 }

 @Override
 public MapView getView() {
 return mapView;
 }
 };
 }

 @Test
 public void testChooseFile_ValidTypeAndNameExtension_ReturnsFile() {
 String type = "png";
 String description = "PNG (*.png)";
 File expectedFile = new File("test.png");
 File resultFile = exportHook.chooseFile(type, description, type);
 assertEquals(expectedFile, resultFile);
 }

 @Test
 public void testChooseFile_InvalidType_ReturnsNull() {
 String type = "jpg";
 String description = "JPG (*.jpg)";
 File resultFile = exportHook.chooseFile(type, description, "gif");
 assertNull(resultFile);
 }

 @Test
 public void testChooseFile_FileExists_ReturnsFile() {
 String type = "png";
 String description = "PNG (*.png)";
 ModeController controller = Mockito.mock(ModeController.class);
 container mockedComponent = Mockito.mock(Container.class);
 FreeMindFileDialog mockedChooser = Mockito.mock(JFileChooser.class);
 controller.when(controller::getFrame).thenReturn(mockedComponent);
 controller.when(controller::getFileChooser).thenReturn(mockedChooser);
 MapView map = Mockito.mock(MapView.class);
 File mmFile = new File("test.mm");
 when(map.getFile()).thenReturn(mmFile);
 File chosenFile = new File("test.png");
 when(mockedChooser.showSaveDialog(mockedComponent)).thenReturn(JFileChooser.APPROVE_OPTION);
 when(mockedChooser.getSelectedFile()).thenReturn(chosenFile);
 exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return controller;
 }
 };
 File resultFile = exportHook.chooseFile(type, description, type);
 assertEquals(chosenFile, resultFile);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_NullController_ThrowsException() {
 String type = "png";
 String description = "PNG (*.png)";
 ExportHook exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return null;
 }
 };
 exportHook.chooseFile(type, description, type);
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName_ReturnsTranslatableString() {
 String resourceName = "%resource.key%";
 String expectedResult = "Translatable string";
 String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName_ReturnsNull() {
 String resourceName = null;
 String result = exportHook.getTranslatableResourceString(resourceName);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage_ReturnsBufferedImage() {
 BufferedImage expectedImage = ImageIO.read(new File("test.png"));
 BufferedImage resultImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, resultImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView_ThrowsException() {
 ExportHook exportHook = new ExportHook() {
 @Override
 public MapView getView() {
 return null;
 }
 };
 exportHook.createBufferedImage();
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName_CopiesFile() {
 String prefix = "test";
 String fileName = "test.txt";
 String destinationDirectory = "test";
 File expectedFile = new File(destinationDirectory + "/" + fileName);
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 assertTrue(expectedFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromResource_InvalidPrefixOrFileName_ThrowsException() {
 String prefix = "test";
 String fileName = "test.txt";
 String destinationDirectory = "test";
 exportHook.copyFromResource(prefix, "invalidFileName", destinationDirectory);
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName_CopiesFile() {
 String dir = "test";
 String fileName = "test.txt";
 String destinationDirectory = "test";
 File expectedFile = new File(destinationDirectory + "/" + fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 assertTrue(expectedFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFile_InvalidDirOrFileName_ThrowsException() {
 String dir = "test";
 String fileName = "test.txt";
 String destinationDirectory = "test";
 exportHook.copyFromFile(dir, "invalidFileName", destinationDirectory);
 }
}