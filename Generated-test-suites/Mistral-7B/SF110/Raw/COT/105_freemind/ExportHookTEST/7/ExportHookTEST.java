package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import javax.imageio.ImageIO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapView mapViewMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 modeControllerMock = mock(ModeController.class);
 mapViewMock = mock(MapView.class);
 exportHook = new ExportHook() {
 @Override
 public MapView getController() {
 return modeControllerMock;
 }

 @Override
 public MapView getView() {
 return mapViewMock;
 }
 };
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile(null, null, null));
 }

 @Test
 public void testChooseFileValidFile() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "test";
 final File file = new File("path/to/file.png");
 when(modeControllerMock.getFileChooser(any(ImageFilter.class)))
 .thenReturn(mock(JFileChooser.class));
 when(modeControllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(modeControllerMock.getMap()).thenReturn(new File("path/to/mindmap.mm"));
 when(modeControllerMock.getText(anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));
 when(modeControllerMock.getText("file_already_exists")).thenReturn("File already exists.");
 when(mock(JOptionPane.class).showConfirmDialog(any(Component.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);
 when(mock(JFileChooser.class).showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);

 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(file, chosenFile);

 verify(modeControllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getText("file_already_exists");
 verify(modeControllerMock, times(1)).getFrame();
 verify(modeControllerMock, times(1)).getMap();
 verify(modeControllerMock, times(1)).getController();
 verify(modeControllerMock, times(1)).getText("%s", "File already exists.");
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileNullDescription() {
 exportHook.chooseFile(null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileNullNameExtension() {
 exportHook.chooseFile("png", null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileNullType() {
 exportHook.chooseFile(null, "Description", null);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 exportHook = new ExportHook();
 assertNull(exportHook.createBufferedImage());
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceStringNullResourceName() {
 exportHook.getTranslatableResourceString(null);
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 final String resourceName = "test_resource";
 final String translatableResourceString = "translated_string";
 when(modeControllerMock.getText(anyString())).thenReturn(translatableResourceString);

 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(translatableResourceString, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetTranslatableResourceStringInvalidResourceName() {
 exportHook.getTranslatableResourceString("%invalid%");
 }
}