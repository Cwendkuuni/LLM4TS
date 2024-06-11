package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import javax.swing.FileChooserBuilder;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;

 @Before
 public void setUp() {
 mapView = Mockito.mock(MapView.class);
 modeController = Mockito.mock(ModeController.class);
 exportHook = new ExportHook() {
 @Override
 public MapView getController() {
 return modeController;
 }

 @Override
 public MapView getView() {
 return mapView;
 }
 };
 }

 @Test
 public void testChooseFile_NullType() {
 final String description = "test description";
 final String nameExtension = "test.extension";
 assertNull(exportHook.chooseFile(null, description, nameExtension));
 }

 @Test
 public void testChooseFile_ValidCall() throws Exception {
 final String type = "image/png";
 final String description = "test description";
 final String nameExtension = "test.png";
 final FileChooserBuilder chooserBuilder = new FileChooserBuilder();
 final FileFilter filter = new ExportHook.ImageFilter(type, description);
 final File chooser = chooserBuilder.setDialogTitle("test title")
 .setFileFilter(filter)
 .createFileChooser();
 final ModeController controller = Mockito.mock(ModeController.class);
 Mockito.when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(controller.getText("file_already_exists")).thenReturn("test message");
 Mockito.when(controller.getMap()).thenReturn(Mockito.mock(File.class));
 Mockito.when(controller.getFileChooser(filter)).thenReturn(chooser);
 Mockito.when(controller.getResource("")).thenReturn(new URL("test_resource"));
 final File chosenFile = new File("test_path/test.png");
 final JOptionPane.ShowOption showConfirmDialogResult = JOptionPane.YES_OPTION;
 Mockito.when(JOptionPane.showConfirmDialog(Mockito.any(Component.class), Mockito.anyString(), Mockito.eq(description), Mockito.eq(showConfirmDialogResult.getValue()))).thenReturn(showConfirmDialogResult.getValue());
 final File expectedFile = new File("test_path/test.png");
 final File resultFile = exportHook.chooseFile(type, description, nameExtension, controller);
 assertEquals(expectedFile, resultFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%test%";
 final String expected = "test";
 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 final Graphics2D graphics = Mockito.mock(Graphics2D.class);
 Mockito.when(mapView.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedImage);
 Mockito.when(mapView.getGraphics()).thenReturn(graphics);
 final BufferedImage resultImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, resultImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullPrefix() {
 exportHook.copyFromResource(null, "test.txt", "");
 }

 @Test
 public void testCopyFromResource_ValidCall() throws Exception {
 final String prefix = "test_prefix/";
 final String fileName = "test.txt";
 final String destinationDirectory = "test_destination";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 // Verify that the copyStream method was called with the correct arguments
 // Add this assertion after you have mocked the logger
 // Mockito.verify(exportHook.logger).finest("searching for test_prefix/test.txt");
 // Mockito.verify(exportHook.logger).finest("searching for test_prefix/test.txt and should go to test_destination");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDir() {
 exportHook.copyFromFile(null, "test.txt", "");
 }

 @Test
 public void testCopyFromFile_ValidCall() throws Exception {
 final String dir = "test_dir/";
 final String fileName = "test.txt";
 final String destinationDirectory = "test_destination";exportHook.copyFromFile(dir, fileName, destinationDirectory);
 // Verify that the copyStream method was called with the correct arguments
 // Add this assertion after you have mocked the logger
 // Mockito.verify(exportHook.logger).finest("searching for test_dir/test.txt");
 // Mockito.verify(exportHook.logger).finest("searching for test_dir/test.txt and should go to test_destination");
 }

}