import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.main.FreeMindFrame;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.JFileChooser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 @Mock
 private ModeController controller;
 @Mock
 private FreeMindFrame frame;
 @InjectMocks
 private ExportHook exportHook;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile("png", "Save image as PNG", null));
 }

 @Test
 public void testChooseFileNullMap() {
 ModeController mockController = mock(ModeController.class);
 ExportHook exportHook = new ExportHook();
 exportHook.setController(mockController);
 MapController mapController = mock(MapController.class);
 when(mockController.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(null);
 File result = exportHook.chooseFile("png", "Save image as PNG", "png");
 assertNull(result);
 }

 @Test
 public void testChooseFileExistingFile() throws IOException {
 ModeController mockController = mock(ModeController.class);
 ExportHook exportHook = new ExportHook();
 exportHook.setController(mockController);
 MapController mapController = mock(MapController.class);
 when(mockController.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(new File("path/to/existing/file.mindmap"));
 JFileChooser chooser = mock(JFileChooser.class);
 when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 File proposedName = new File("path/to/existing/file.mindmap").getAbsoluteFile();
 proposedName = new File(proposedName.getPath() + ".png");
 when(chooser.getSelectedFile()).thenReturn(proposedName);
 File result = exportHook.chooseFile("png", "Save image as PNG", "png");
 assertEquals(proposedName.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testChooseFileInvalidFileExtension() throws IOException {
 ModeController mockController = mock(ModeController.class);
 ExportHook exportHook = new ExportHook();
 exportHook.setController(mockController);
 MapController mapController = mock(MapController.class);
 when(mockController.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(new File("path/to/existing/file.mindmap"));
 JFileChooser chooser = mock(JFileChooser.class);
 when(mockController.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 File proposedName = new File("path/to/existing/file.mindmap").getAbsoluteFile();
 proposedName = new File(proposedName.getPath() + ".txt");
 when(chooser.getSelectedFile()).thenReturn(proposedName);
 File expectedFile = new File("path/to/existing/file.mindmap.png");
 File result = exportHook.chooseFile("png", "Save image as PNG", "png");
 assertEquals(expectedFile.getAbsolutePath(), result.getAbsolutePath());
 }

 @Test
 public void testCreateBufferedImageNullView() {
 ExportHook exportHook = new ExportHook();
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 MapView view = mock(MapView.class);
 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
 ExportHook exportHook = new ExportHook();
 exportHook.setController(mock(ModeController.class));
 exportHook.setView(view);
 BufferedImage result = exportHook.createBufferedImage();
 assertNotNull(result);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 ExportHook exportHook = new ExportHook();
 String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 ExportHook exportHook = new ExportHook();
 String result = exportHook.getTranslatableResourceString("test.resource");
 assertNotNull(result);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 ExportHook exportHook = new ExportHook();
 String destinationDirectory = "path/to/destination";
 exportHook.copyFromResource(null, "test.txt", destinationDirectory);
 }

 @Test
 public void testCopyFromResourceValidPrefixAndFileName() throws IOException {
 ExportHook exportHook = new ExportHook();
 String prefix = "path/to/prefix";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 URL resource = new URL(prefix + fileName);
 File result = exportHook.getResource(prefix + fileName);
 assertTrue(result.getPath().equals(resource.getPath()));
 assertTrue(result.exists());
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFileNullDir() {
 ExportHook exportHook = new ExportHook();
 String dir = null;
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFileValidDirAndFileName() throws IOException {
 ExportHook exportHook = new ExportHook();
 String dir = "path/to/source";
 String fileName = "test.txt";
 String destinationDirectory = "path/to/destination";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 File result = exportHook.getResource(destinationDirectory + "/" + fileName);
 assertTrue(result.getPath().equals(expectedFile.getAbsolutePath()));
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testGetResourceNullPrefix() {
 ExportHook exportHook = new ExportHook();
 String prefix = null;
 String fileName = "test.txt";
 File result = exportHook.getResource(prefix + fileName);
 assertNull(result);
 }

 @Test
 public void testGetResourceValidPrefixAndFileName() {
 ExportHook exportHook = new ExportHook();
 String prefix = "path/to/prefix";
 String fileName = "test.txt";
 File result = exportHook.getResource(prefix + fileName);
 assertNotNull(result);
 }
}