package freemind.extensions;

import static org.junit.Assert.*;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSaveDialog;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import freemind.main.Controller;
import freemind.main.TextUtils;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeController;

public class ExportHookTest {

 private ModeController controller;
 private MapView view;
 private ExportHook exportHook;

 @Before
 public void setUp() {
 controller = Mockito.mock(Controller.class);
 view = Mockito.mock(MapView.class);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile_NullType_ThrowsNullPointerException() {
 assertThrows(NullPointerException.class, () -> exportHook.chooseFile(null, "description", null));
 }

 @Test
 public void testChooseFile_ValidArguments_ReturnsExpectedFile() {
 final String type = "image/png";
 final FileFilter filter = Mockito.mock(FileFilter.class);
 final FileSaveDialog chooser = Mockito.mock(FileSaveDialog.class);
 final String description = "description";
 final String nameExtension = "png";
 final File expectedFile = new File("expected/file.png");

 Mockito.when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(controller.getFileChooser(ArgumentMatchers.eq(filter))).thenReturn(chooser);
 Mockito.when(controller.getText(ArgumentMatchers.anyString())).then(invocation -> invocation.getArgument(0).substring(1));

 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(controller.getMap()).thenReturn(new Object() {
 @Override
 public File getFile() {
 return new File("map.mm");
 }
 });

 Mockito.when(chooser.showSaveDialog(ArgumentMatchers.any(JComponent.class))).thenReturn(JOptionPane.OK_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_InvalidExtension_ReturnsNull() {
 final String type = "image/png";
 final FileFilter filter = Mockito.mock(FileFilter.class);
 final FileSaveDialog chooser = Mockito.mock(FileSaveDialog.class);
 final String description = "description";
 final String nameExtension = "txt";
 final File expectedFile = null;

 Mockito.when(controller.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(controller.getFileChooser(ArgumentMatchers.eq(filter))).thenReturn(chooser);
 Mockito.when(controller.getText(ArgumentMatchers.anyString())).then(invocation -> invocation.getArgument(0).substring(1));

 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(controller.getMap()).thenReturn(new Object() {
 @Override
 public File getFile() {
 return new File("map.mm");
 }
 });

 Mockito.when(chooser.showSaveDialog(ArgumentMatchers.any(JComponent.class))).thenReturn(JOptionPane.OK_OPTION);

 final File actualFile = exportHook.chooseFile(type, description, nameExtension);

 assertNull(actualFile);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName_ReturnsNull() {
 final String resourceName = null;
 final String expectedString = null;

 assertEquals(expectedString, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceName_ReturnsExpectedString() {
 final String resourceName = "%resource.key%";
 final String expectedString = "resource key";

 final String actualString = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedString, actualString);
 }

 @Test
 public void testCreateBufferedImage_ReturnsBufferedImage() {
 assertNotNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCopyFromResource_ValidArguments_CopiesFile() {
 final String prefix = "prefix/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";
 final String expectedFilePath = "destination/file.txt";

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 final File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 assertEquals(expectedFilePath, actualFile.getAbsolutePath());
 }

 @Test
 public void testCopyFromFile_ValidArguments_CopiesFile() {
 final String dir = "source/";
 final String fileName = "file.txt";
 final String destinationDirectory = "destination/";
 final String expectedFilePath = "destination/file.txt";

 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 final File actualFile = new File(destinationDirectory, fileName);
 assertTrue(actualFile.exists());
 assertEquals(expectedFilePath, actualFile.getAbsolutePath());
 }
}