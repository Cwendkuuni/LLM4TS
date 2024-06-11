package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.Logger;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;
 private Logger logger;

 @Before
 public void setUp() {
 modeController = Mockito.mock(ModeController.class);
 mapView = Mockito.mock(MapView.class);
 logger = Mockito.mock(Logger.class);
 exportHook = new ExportHook() {
 @Override
 public Logger getLogger() {
 return logger;
 }

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
 public void testChooseFile() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "";
 final File expectedChosenFile = new File("expectedChosenFile.png");
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedChosenFile, chosenFile);

 // Test null file chooser
 final ModeController nullController = null;
 final File nullChosenFile = exportHook.chooseFile(type, description, nameExtension, nullController);
 assertNull(nullChosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertSame(expectedBufferedImage, bufferedImage);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resourceName%";
 final String expectedTranslatableResourceString = "expectedTranslatableResourceString";
 final String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedTranslatableResourceString, translatableResourceString);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 final String prefix = "prefix";
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 final File expectedFile = new File(destinationDirectory, fileName);
 final File actualFile = new File(destinationDirectory);
 assertTrue(actualFile.exists());
 assertTrue(expectedFile.equals(actualFile.getAbsoluteFile()));
 }

 @Test
 public void testCopyFromFile() throws IOException {
 final String dir = "dir";
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 final File expectedFile = new File(destinationDirectory, fileName);
 final File resource = new File(dir, fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 final File actualFile = new File(destinationDirectory);
 assertTrue(actualFile.exists());
 assertTrue(expectedFile.equals(actualFile.getAbsoluteFile()));
 }

 @Test
 public void testChooseImageFile() throws Exception {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "png";
 final File mmFile = new File("mmFile.mm");
 final ModeController controller = Mockito.mock(ModeController.class);
 final Container component = Mockito.mock(Container.class);
 final FreeMindFileDialog chooser = Mockito.mock(FreeMindFileDialog.class);
 final File chosenFile = new File("chosenFile.png");
 exportHook.chooseImageFile(type, description, nameExtension, controller);
 Mockito.verify(controller).getFrame();
 Mockito.verify(controller).getFileChooser(Mockito.isInstance(new ImageFilter(type, description)));
 Mockito.verifyNoMoreInteractions(controller);
 Mockito.verify(controller).getMap();
 Mockito.verify(controller.getMap()).getFile();
 Mockito.verify(controller).getText(Mockito.eq("file_already_exists"));
 Mockito.verifyNoMoreInteractions(controller);
 Mockito.verify(component).getContentPane();
 Mockito.verify(controller).getFileChooser(Mockito.isInstance(chooser));
 Mockito.verify(controller).getText("file_already_exists");
 Mockito.verify(chooser).setSelectedFile(expectedChosenFile);
 Mockito.verify(controller).setText("file_already_exists");
 Mockito.verify(controller).showSaveDialog(component);
 Mockito.verify(chooser).getSelectedFile();
 Mockito.verify(controller).getText("file_already_exists");
 Mockito.verify(controller).showConfirmDialog(component, Mockito.isInstance(Mockito.anyString()), Mockito.isInstance(Mockito.anyString()), 0);
 Mockito.verifyNoMoreInteractions(controller, component, chooser);
 }
}