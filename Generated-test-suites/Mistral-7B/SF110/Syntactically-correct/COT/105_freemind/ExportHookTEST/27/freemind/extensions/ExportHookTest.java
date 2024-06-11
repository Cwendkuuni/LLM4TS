import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHookTest {

 private ExportHook hook;
 private MapView view;
 private ModeController controller;

 @Before
 public void setUp() {
 view = mock(MapView.class);
 controller = mock(ModeController.class);
 hook = new ExportHook() {
 @Override
 MapView getController() {
 return controller;
 }

 @Override
 MapView getView() {
 return view;
 }
 };
 }

 @Test
 public void testChooseFile_ValidFileType_ReturnsFile() {
 final String type = "png";
 final String description = "PNG Images";
 final File file = hook.chooseFile(type, "Save as PNG", type);
 Assert.assertNotNull(file);
 }

 @Test
 public void testChooseFile_InvalidFileType_ReturnsNull() {
 final String type = "jpg";
 final String description = "JPG Images";
 final File file = hook.chooseFile(type, "Save as JPG", null);
 Assert.assertNull(file);
 }

 @Test
 public void testChooseFile_FileAlreadyExists_AsksUserForOverwrite() {
 final String type = "png";
 final String description = "PNG Images";
 final String fileName = "test.png";
 final String destinationDirectory = "/temp";
 final File chosenFile = new File(destinationDirectory + "/" + fileName);
 when(controller.getText("file_already_exists")).thenReturn("The file " + chosenFile + " already exists. Do you want to replace it?");
 final int overwriteMap = JOptionPane.showConfirmDialog(any(), eq(controller), eq("Replace File"), JOptionPane.YES_NO_OPTION);
 when(controller.getFileChooser(any())).thenReturn(mock(JFileChooser.class));
 final File file = hook.chooseFile(type, description, null);
 verify(controller).getText("file_already_exists");
 verify(controller).getFileChooser(any());
 verifyZeroInteractions(controller.getFrame());
 Assert.assertEquals(chosenFile, file);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_NullController_ThrowsException() {
 hook.setController(null);
 hook.chooseFile("png", "Save as PNG", null);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage image = hook.createBufferedImage();
 Assert.assertNotNull(image);
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName_ReturnsTranslatedString() {
 final String resourceName = "%hello%";
 final String translatedString = "Hello";
 when(controller.getText(eq("%hello%"))).thenReturn(translatedString);
 final String result = hook.getTranslatableResourceString(resourceName);
 verify(controller).getText(eq("%hello%"));
 Assert.assertEquals(translatedString, result);
 }

 @Test
 public void testGetTranslatableResourceString_InvalidResourceName_ReturnsOriginalString() {
 final String resourceName = "invalidResourceName";
 final String result = hook.getTranslatableResourceString(resourceName);
 Assert.assertEquals(resourceName, result);
 }

 @Test
 public void testCopyFromResource_ValidPrefixAndFileName_CopiesFile() {
 final String prefix = "prefix.";
 final String fileName = "test.png";
 final String destinationDirectory = "/temp";
 hook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(logger).finest("searching for prefix.test.png");
 verify(logger, never()).severe("Cannot find resource: ");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullLogger_ThrowsException() {
 hook.setLogger(null);
 hook.copyFromResource("prefix.", "test.png", "/temp");
 }

 @Test
 public void testCopyFromFile_ValidDirAndFileName_CopiesFile() {
 final String dir = "/path/to/dir";
 final String fileName = "test.png";
 final String destinationDirectory = "/temp";
 hook.copyFromFile(dir, fileName, destinationDirectory);
 verify(logger).finest("searching for /path/to/dir/test.png");
 verify(logger, never()).severe("Cannot find resource: ");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullLogger_ThrowsException() {
 hook.setLogger(null);
 hook.copyFromFile("/path/to/dir", "test.png", "/temp");
 }

}