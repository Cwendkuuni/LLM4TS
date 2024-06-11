import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSaveDialog;
import javax.swing.filechooser.FileView;
import javax.swing.filechooser.SaveFileFilter;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.modes.MapController;
import freemind.model.MMLFile;
import freemind.main.ResourceManager;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ModeControllerView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private MapView mapView;
 private MapController mapController;
 private ResourceManager resourceManager;
 private FileSaveDialog fileSaveDialog;
 private File choosenFile;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 modeController = mock(ModeController.class);
 mapView = mock(MapView.class);
 mapController = mock(MapController.class);
 resourceManager = mock(ResourceManager.class);
 fileSaveDialog = mock(FileSaveDialog.class);
 }

 @Test
 public void testChooseFile_ValidInput_ReturnsFile() {
 when(modeController.getFileChooser(ArgumentsMatchers.any(FileFilter.class)))
 .thenReturn(fileSaveDialog);
 when(fileSaveDialog.showSaveDialog(ArgumentsMatchers.any(Container.class)))
 .thenReturn(JOptionPane.OK_OPTION);

 choosenFile = new File("path/to/save/file.png");
 when(fileSaveDialog.getSelectedFile())
 .thenReturn(choosenFile);

 when(resourceManager.getText("file_already_exists"))
 .thenReturn("File already exists. Do you want to replace it?");

 when(modeController.getText(ArgumentsMatchers.anyString()))
 .then(invocation -> invocation.getArgumentAt(0, String.class));

 ExportHook.chooseImageFile("png", "Save image as PNG", "png", modeController);

 verify(fileSaveDialog).setSelectedFile(choosenFile);
 }

 @Test
 public void testChooseFile_InvalidFileType_ReturnsNull() {
 when(modeController.getFileChooser(ArgumentsMatchers.any(FileFilter.class)))
 .thenReturn(fileSaveDialog);
 when(fileSaveDialog.showSaveDialog(ArgumentsMatchers.any(Container.class)))
 .thenReturn(JOptionPane.OK_OPTION);

 choosenFile = new File("path/to/save/file.txt");
 when(fileSaveDialog.getSelectedFile())
 .thenReturn(choosenFile);

 ExportHook.chooseImageFile("png", "Save image as PNG", "png", modeController);

 verify(fileSaveDialog, Mockito.never()).setSelectedFile(choosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 when(modeController.getView())
 .thenReturn(mapView);
 when(mapView.getInnerBounds())
 .thenReturn(new Rectangle(0, 0, 100, 100));

 BufferedImage result = exportHook.createBufferedImage();

 verify(mapView).preparePrinting();
 verify(mapView).createImage(100, 100);
 verify(mapView).endPrinting();

 assertNotNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 exportHook.createBufferedImage();

 verify(modeController, Mockito.never()).getView();
 }
}