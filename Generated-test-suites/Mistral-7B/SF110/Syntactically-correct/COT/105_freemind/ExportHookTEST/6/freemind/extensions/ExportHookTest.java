import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.controller.ModeController;
import freemind.main.MainFrame;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ExportHookTest {

 private ExportHook hook;
 private ModeController controller;
 private MapView view;
 private MainFrame mainFrame;

 @Before
 public void setUp() {
 this.controller = new ModeController();
 this.hook = new ExportHook();
 this.hook.setController(this.controller);
 this.view = new MapView();
 this.mainFrame = new MainFrame();
 this.mainFrame.setController(this.controller);
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(hook.chooseFile(null, "Description", null));
 }

 @Test
 public void testChooseFileValidCall() {
 final String type = "png";
 final String description = "Export as PNG";
 final File expected = new File("path/to/valid/file.png");
 final File result = hook.chooseFile(type, description, null, controller);
 assertEquals(expected, result);
 }

 @Test
 public void testChooseFileInvalidFileType() {
 final String type = "png";
 final String description = "Export as PNG";
 final File expected = new File("path/to/valid/file.jpg");
 final File result = hook.chooseFile(type, description, null, controller);
 assertNull(result);
 }

 @Test
 public void testChooseFileFileAlreadyExists() {
 final String type = "png";
 final String description = "Export as PNG";
 final String overwriteText = "The file already exists, do you want to replace it?";
 final String fileName = "testFile.png";
 final String expectedFilePath = "path/to/valid/file.png";
 final File expected = new File(expectedFilePath);
 final File actualFile = new File(expectedFilePath);
 actualFile.createNewFile();
 final File result = hook.chooseFile(type, description, null, controller);
 assertSame(expected, result);
 JOptionPane.showMessageDialog(mainFrame.getContentPane(), "You confirmed to overwrite the file.", "File already exists", JOptionPane.PLAIN_MESSAGE);
 actualFile.deleteOnExit();
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(hook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidCall() {
 final String resourceName = "%1$sFile.exported";
 final String expected = controller.getText("File.exported");
 final String result = hook.getTranslatableResourceString(resourceName);
 assertEquals(expected, result);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(hook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidCall() {
 this.view.setSize(500, 500);
 final BufferedImage result = hook.createBufferedImage();
 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResourceNullPrefix() {
 hook.copyFromResource(null, "fileName.ext", "destinationDirectory");
 }

 @Test
 public void testCopyFromResourceValidCall() {
 final String prefix = "freemind/extensions/";
 final String fileName = "image.png";
 final String destinationDirectory = "path/to/destination/directory/";
 final URL resource = new URL("resource:/test.png");
 hook.copyFromResource(prefix, fileName, destinationDirectory);
 assertTrue(new File(destinationDirectory + fileName).exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testCopyFromFileNullDir() {
 hook.copyFromFile(null, "fileName.ext", "destinationDirectory");
 }

 @Test
 public void testCopyFromFileValidCall() {
 final String dir = "path/to/source/directory/";
 final String fileName = "image.png";
 final String destinationDirectory = "path/to/destination/directory/";
 final File sourceFile = new File(dir, fileName);
 hook.copyFromFile(dir, fileName, destinationDirectory);
 assertTrue(new File(destinationDirectory + fileName).exists());
 }

 @Test(expected = IOException.class)
 public void testImageFilterAcceptNullFile() {
 final FileFilter filter = hook.new ImageFilter("png", null);
 assertFalse(filter.accept(null));
 }

 @Test
 public void testImageFilterAcceptValidCall() {
 final FileFilter filter = hook.new ImageFilter("png", null);
 final File testFile = new File("path/to/valid/file.png");
 assertTrue(filter.accept(testFile));
 }

 @Test
 public void testImageFilterAcceptInvalidCall() {
 final FileFilter filter = hook.new ImageFilter("png", null);
 final File testFile = new File("path/to/invalid/file");
 assertFalse(filter.accept(testFile));
 }

 @Test
 public void testImageFilterGetDescriptionNullType() {
 final FileFilter filter = hook.new ImageFilter(null, "description");
 assertEquals("", filter.getDescription());
 }

 @Test
 public void testImageFilterGetDescriptionValidCall() {
 final FileFilter filter = hook.new ImageFilter("png", "export as PNG");
 assertEquals("export as PNG", filter.getDescription());
 }
}