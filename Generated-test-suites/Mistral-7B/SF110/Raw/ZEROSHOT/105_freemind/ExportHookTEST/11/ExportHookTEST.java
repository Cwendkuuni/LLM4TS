package freemind.extensions;

import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.TextController;
import freemind.model.MapController;
import freemind.model.MapModel;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapController mapControllerMock;
 private MapView mapViewMock;
 private Controller controllerMock;
 private TextController textControllerMock;

 @Before
 public void setUp() {
 modeControllerMock = Mockito.mock(ModeController.class);
 mapControllerMock = Mockito.mock(MapController.class);
 mapViewMock = Mockito.mock(MapView.class);
 controllerMock = Mockito.mock(Controller.class);
 textControllerMock = Mockito.mock(TextController.class);
 exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 exportHook.setView(mapViewMock);
 exportHook.setModeController(modeControllerMock);
 exportHook.setTextController(textControllerMock);
 }

 @After
 public void tearDown() {
 exportHook = null;
 }

 @Test
 public void testChooseFileWithValidData() throws IOException {
 final String expectedType = "jpeg";
 final String expectedDescription = "JPEG files (*.jpeg, *.jpg)";
 final String expectedNameExtension = "jpeg";
 final String expectedFileName = "test.jpeg";
 final File expectedFile = new File(expectedFileName);
 final ModeController controller = Mockito.mock(ModeController.class);
 Mockito.when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(controller.getText("file_already_exists")).thenReturn("File already exists.");

 final File chosenFile = exportHook.chooseFile(expectedType, expectedDescription, expectedNameExtension, controller);
 Mockito.verify(controller, Mockito.times(1)).getFileChooser(Mockito.eq(new ImageFilter(expectedType, expectedDescription)));
 Mockito.verify(controller, Mockito.times(1)).getMap();
 final MapController mapController = Mockito.argumentCaptor(MapController.class).capture(controller.getFileChooser(Mockito.eq(new ImageFilter(expectedType, expectedDescription))));
 Mockito.verify(mapController, Mockito.times(1)).getFile();
 final MapController argMapController = mapController.getValue();
 Mockito.when(argMapController.getFile()).thenReturn(expectedFile);

 final File returnedFile = chosenFile;
 assertEquals(expectedFileName, returnedFile.getName());
 assertEquals(expectedType, returnedFile.getAbsolutePath().substring(returnedFile.getAbsolutePath().lastIndexOf(".") + 1));
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullController() {
 exportHook.setController(null);
 final String type = "jpeg";
 final String description = "JPEG files (*.jpeg, *.jpg)";
 final String nameExtension = "jpeg";
 exportHook.chooseFile(type, description, nameExtension);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullFileChooser() {
 exportHook.setController(controllerMock);
 Mockito.when(controllerMock.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(null);
 final String type = "jpeg";
 final String description = "JPEG files (*.jpeg, *.jpg)";
 final String nameExtension = "jpeg";
 exportHook.chooseFile(type, description, nameExtension);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resource_string%";
 final String expectedValue = "Expected value";
 Mockito.when(controllerMock.getText(Mockito.anyString())).thenReturn(expectedValue);
 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, result);
 }

 @Test
 public void testGetTranslatableResourceStringWithNullResourceName() {
 final String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 final Image image = Mockito.mock(Image.class);
 final Graphics2D graphics = Mockito.mock(Graphics2D.class);
 Mockito.when(mapViewMock.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
 Mockito.when(mapViewMock.getWidth()).thenReturn(50);
 Mockito.when(mapViewMock.getHeight()).thenReturn(50);
 Mockito.when(image.getSubimage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
 Mockito.when(image.getGraphics()).thenReturn(graphics);

 final BufferedImage result = exportHook.createBufferedImage();
 assertEquals(Color.WHITE, result.getBackground());
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImageWithNullView() {
 exportHook.setView(null);
 exportHook.createBufferedImage();
 }

 @Test
 public void testCopyFromResource() throws IOException {
 final String prefix = "/prefix/";
 final String fileName = "test.jpg";
 final String destinationDirectory = "/destination/";
 final URL resource = new URL("http://test.com/test.jpg");
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 Mockito.verify(controllerMock, Mockito.times(1)).getLogger();
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullPrefix() {
 exportHook.copyFromResource(null, "test.jpg", "/destination/");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullFileName() {
 exportHook.copyFromResource("/prefix/", null, "/destination/");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceWithNullDestinationDirectory() {
 exportHook.copyFromResource("/prefix/test.jpg", null);
 }

 @Test
 public void testCopyFromFile() throws IOException {
 final String dir = "/source/";
 final String fileName = "test.jpg";
 final String destinationDirectory = "/destination/";
 final File sourceFile = new File(dir + fileName);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 Mockito.verify(controllerMock, Mockito.times(1)).getLogger();
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullDir() {
 exportHook.copyFromFile(null, "test.jpg", "/destination/");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullFileName() {
 exportHook.copyFromFile("/source/", null, "/destination/");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFileWithNullDestinationDirectory() {
 exportHook.copyFromFile("/source/test.jpg", "/destination/", null);
 }

}