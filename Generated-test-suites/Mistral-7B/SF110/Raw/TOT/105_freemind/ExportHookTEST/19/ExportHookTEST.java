package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
 public ModeController getController() {
 return modeControllerMock;
 }

 @Override
 public MapView getView() {
 return mapViewMock;
 }
 };
 }

 @Test
 public void testChooseFile_ValidFileIsChosen() {
 String type = "png";
 String description = "Export Image";
 String nameExtension = ".png";
 File expectedFile = new File("path/to/file.png");
 when(modeControllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(mapViewMock.getFile()).thenReturn(null);
 when(controller -> controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 verify(modeControllerMock.getFrame()).setVisible(true);
 verify(modeControllerMock.getFileChooser(any(ImageFilter.class))).showSaveDialog(eq(modeControllerMock.getFrame().getContentPane()), eq(expectedFile));
 verify(modeControllerMock).getText("file_already_exists");
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFile_NullFileIsReturnedIfCancelIsClicked() {
 String type = "png";
 String description = "Export Image";
 String nameExtension = ".png";
 when(modeControllerMock.getFrame()).thenReturn(mock(JFrame.class));
 when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 when(mapViewMock.getFile()).thenReturn(new File("path/to/mapfile.mm"));
 when(controller -> controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");

 File actualFile = exportHook.chooseFile(type, description, nameExtension);

 verify(modeControllerMock.getFrame()).setVisible(true);
 verify(modeControllerMock.getFileChooser(any(ImageFilter.class))).showSaveDialog(eq(modeControllerMock.getFrame().getContentPane()), eq(nullValue()));
 assertEquals(null, actualFile);
 }

 @Test
 public void testGetTranslatableResourceString_ValidResourceNameIsReturned() {
 String resourceName = "%resource.key%";
 String expectedValue = "Expected Value";
 when(modeControllerMock.getText(eq(resourceName))).thenReturn(expectedValue);

 String actualValue = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetTranslatableResourceString_NullValueIsReturnedIfResourceNameIsEmpty() {
 String resourceName = "";

 String actualValue = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(null, actualValue);
 }

 @Test
 public void testCreateBufferedImage_ValidBufferedImageIsReturned() throws IOException {
 BufferedImage expectedBufferedImage = ImageIO.read(new File("path/to/image.jpg"));
 when(mapViewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));

 BufferedImage actualBufferedImage = exportHook.createBufferedImage();

 assertEquals(expectedBufferedImage, actualBufferedImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_InvalidViewIsReturned() {
 when(mapViewMock.getInnerBounds()).thenReturn(null);

 exportHook.createBufferedImage();
 }
}