package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import freemind.controller.Controller;
import freemind.controller.model.ModeController;
import freemind.controller.status.Logger;
import freemind.extensions.ExportHook;
import freemind.extensions.ExportHook.ImageFilter;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.main.Main;
import freemind.modes.MapController;
import freemind.view.mindmapview.MindMapView;
import freemind.view.mindmapview.MindMapViewFactory;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 @Mock
 private Controller controller;
 @Mock
 private Logger logger;
 @Mock
 private ModeControllerHookAdapter modeControllerHookAdapter;
 @InjectMocks
 private ExportHook exportHook;
 @Mock
 private MapController mapController;
 @Mock
 private MindMapViewFactory viewFactory;

 private File testFile;
 private File testChosenFile;
 private ImageFilter imageFilter;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 exportHook.setController(controller);
 exportHook.setLogger(logger);
 imageFilter = new ExportHook.ImageFilter("png", "PNG-Image");
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile(null, null, null));
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(controller, null, null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(exportHook.chooseFile(controller, "type", null));
 }

 @Test
 public void testChooseFileNullNameExtension() {
 assertNull(exportHook.chooseFile(controller, "type", null));
 }

 @Test
 public void testChooseFileValidFilePath() throws IOException {
 testFile = new File("testFile");
 when(controller.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(testFile);
 testChosenFile = new File("testChosenFile.png");
 when(controller.getText(anyString())).thenReturn("Test Text");

 assertEquals(testChosenFile, exportHook.chooseFile("png", "Test Text", "png", controller));
 verify(controller).getFileChooser(imageFilter);
 verify(controller).getText("file_already_exists");
 verify(controller, times(1)).showConfirmDialog(eq(controller.getFrame().getContentPane()), anyString(), anyString(), anyInt());
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 MindMapView view = mock(MindMapView.class);
 when(controller.getView()).thenReturn(view);
 when(controller.getResourceString(anyString())).thenReturn("Test String");
 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertEquals(null, exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 String resourceName = "testResourceName";
 String expected = "Test Text";
 when(controller.getText(resourceName)).thenReturn(expected);
 assertEquals(expected, exportHook.getTranslatableResourceString(resourceName));
 }
}