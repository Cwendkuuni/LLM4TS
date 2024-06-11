package freemind.extensions.test;

import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import javax.swing.JOptionPane;
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

 @Before
 public void setUp() {
 this.hook = new ExportHook();
 this.controller = new MockModeController();
 this.view = new MockMapView();
 this.hook.setController(this.controller);
 this.hook.setView(this.view);
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(this.hook.chooseFile("type", "description", "nameExtension"));
 }

 @Test
 public void testChooseFileNullView() {
 this.hook.setController(this.controller);
 this.hook.setView(null);
 assertNull(this.hook.chooseFile("type", "description", "nameExtension"));
 }

 @Test
 public void testChooseFileNullType() {
 this.hook.setController(this.controller);
 this.hook.setView(this.view);
 assertNull(this.hook.chooseFile(null, "description", "nameExtension"));
 }

 @Test
 public void testChooseFileNullDescription() {
 this.hook.setController(this.controller);
 this.hook.setView(this.view);
 assertNull(this.hook.chooseFile("type", null, "nameExtension"));
 }

 @Test
 public void testChooseFileNullNameExtension() {
 this.hook.setController(this.controller);
 this.hook.setView(this.view);
 assertNull(this.hook.chooseFile("type", "description", null));
 }

 @Test
 public void testChooseFileValidArguments() throws IOException {
 final File chosenFile = new File("src/test/resources/testFile.png");
 this.controller.expectAndReturn("getFileChooser(FileFilter)").once().andReturn(new JFileChooser());
 this.controller.expect("getText(String)").andReturn("Test File").once();
 this.controller.expect("getMap").andReturn(new File("src/test/resources/testMap.mm")).once();
 this.controller.expect("setText(String, String)").andAnswer((InvocationOnMock arg0) -> arg0.getArgumentAt(0, 0)).once();
 this.controller.expect("getFrame").andReturn(new JFrame()).once();
 this.controller.expect("getContentPane").andReturn(new JComponent()).once();
 this.controller.expect("getFileChooser(FileFilter)").andReturn(new JFileChooser()).once();
 this.controller.expect("showSaveDialog(Component)").andReturn(JFileChooser.APPROVE_OPTION).once();

 final File expectedFile = new File("src/test/resources/testMap.mmTestFile.png");
 final File actualFile = this.hook.chooseFile("image/png", "Test File", "png");
 assertEquals(expectedFile, actualFile);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFileInvalidType() throws IOException {
 final File chosenFile = new File("src/test/resources/testFile.txt");
 this.controller.expectAndReturn("getFileChooser(FileFilter)").once().andReturn(new JFileChooser());
 this.controller.expect("getText(String)").andReturn("Test File").once();
 this.controller.expect("getMap").andReturn(new File("src/test/resources/testMap.mm")).once();
 this.controller.expect("setText(String, String)").andAnswer((InvocationOnMock arg0) -> arg0.getArgumentAt(0, 0)).once();
 this.controller.expect("getFrame").andReturn(new JFrame()).once();
 this.controller.expect("getContentPane").andReturn(new JComponent()).once();
 this.controller.expect("getFileChooser(FileFilter)").andReturn(new JFileChooser()).once();
 this.controller.expect("showSaveDialog(Component)").andReturn(JFileChooser.APPROVE_OPTION).once();

 this.hook.chooseFile("application/pdf", "Test File", "png");
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(this.hook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidArguments() {
 this.hook.expect("getResourceString(String)").andReturn("Test %s").once();
 this.hook.expect("getText(String)").andReturn("Test").once();
 final String expectedResult = "Test Test";
 assertEquals(expectedResult, this.hook.getTranslatableResourceString("%sTest"));
 }

 @Test
 public void testCreateBufferedImageNullView() {
 this.hook.setController(this.controller);
 this.hook.setView(null);
 assertNull(this.hook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImage() {
 this.hook.expect("getController").andReturn(this.controller).once();
 this.hook.expect("getView").andReturn(this.view).once();
 this.hook.expect("getWidth").andReturn(10).once();
 this.hook.expect("getHeight").andReturn(10).once();
 this.hook.expect("preparePrinting").once();
 this.hook.expect("createImage").andReturn(new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)).once();
 this.hook.expect("endPrinting").once();

 final BufferedImage expectedBufferedImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
 final BufferedImage actualBufferedImage = this.hook.createBufferedImage();
 assertEquals(expectedBufferedImage, actualBufferedImage);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 this.hook.setLogger(new MockLogger());
 this.hook.copyFromResource(null, "testFile.png", "destinationDirectory");
 }

 @Test
 public void testCopyFromResourceValidArguments() throws IOException {
 final URL resource = new URL("file:src/test/resources/testFile.png");
 this.hook.expect("getLogger").andReturn(this.logger).once();
 this.hook.expect("getResource").andReturn(resource).once();
 this.hook.expect("openStream").once();
 this.hook.expect("copyStream").once();
 this.hook.expect("println").withArguments("searching for testFile.png").once();
 this.hook.copyFromResource("test", "testFile.png", "destinationDirectory");
 }

 @Test
 public void testCopyFromFileNullDir() {
 this.hook.setLogger(new MockLogger());
 this.hook.copyFromFile(null, "testFile.png", "destinationDirectory");
 }

 @Test
 public void testCopyFromFileValidArguments() throws IOException {
 final File file = new File("src/test/resources/testFile.png");
 this.hook.expect("getLogger").andReturn(this.logger).once();
 this.hook.expect("copyStream").once();
 this.hook.expect("println").withArguments("searching for src/test/resources/testFile.png").once();
 this.hook.copyFromFile("src/test/resources", "testFile.png", "destinationDirectory");
 }

 @Test
 public void testImageFilterAcceptNullFile() {
 final FileFilter filter = new ExportHook.ImageFilter("image/png", null);
 assertFalse(filter.accept(null));
 }

 @Test
 public void testImageFilterAcceptDirectory() {
 final FileFilter filter = new ExportHook.ImageFilter("image/png", null);
 assertTrue(filter.accept(new File("src/test/resources")));
 }

 @Test
 public void testImageFilterAcceptFileWithSameExtension() {
 final FileFilter filter = new ExportHook.ImageFilter("image/png", null);
 final File file = new File("src/test/resources/testFile.png");
 assertTrue(filter.accept(file));
 }

 @Test
 public void testImageFilterAcceptFileWithDifferentExtension() {
 final FileFilter filter = new ExportHook.ImageFilter("image/png", null);
 final File file = new File("src/test/resources/testFile.jpg");
 assertFalse(filter.accept(file));
 }

 @Test
 public void testImageFilterGetDescription() {
 final FileFilter filter = new ExportHook.ImageFilter("image/png", "Test Image Filter");
 assertEquals("Test Image Filter", filter.getDescription());
 }

 @Test
 public void testGetResourceNullPrefix() {
 assertNull(this.hook.getResource("test"));
 }

 @Test
 public void testGetResourceValidArguments() {
 final String expectedResult = "testResource.txt";
 this.hook.expect("getClass").andReturn(ExportHook.class).once();
 this.hook.expect("getResourceAsStream").andReturn(new InputStream() {
 @Override
 public int read() throws IOException {
 return (int)expectedResult.charAt(0);
 }
 }).once();
 this.hook.expect("close").once();
 this.hook.expect("getDescriptor().getMain").andReturn(getClass()).once();
 final URL resource = this.hook.getResource("test/testResource.txt");
 assertEquals(expectedResult, resource.getPath());
 }

 @After
 public void tearDown() {
 this.controller = null;
 this.view = null;
 this.hook = null;
 }
}