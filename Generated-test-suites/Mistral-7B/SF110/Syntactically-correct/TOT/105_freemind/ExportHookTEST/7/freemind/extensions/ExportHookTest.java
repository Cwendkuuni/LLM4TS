package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.extensions.Tools;
import freemind.main.FreeMindFrame;
import freemind.main.Logger;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapControllerView;
import freemind.view.mindmapview.MindMapView;
import freemind.view.mindmapview.View;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private FreeMindFrame frame;
 private MapController mapController;
 private Logger logger;
 private Resources resources;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 modeController = mock(ModeController.class);
 frame = mock(FreeMindFrame.class);
 mapController = mock(MapController.class);
 logger = mock(Logger.class);
 resources = mock(Resources.class);
 exportHook = new ExportHook() {
 @Override
 protected Logger getLogger() {
 return logger;
 }

 @Override
 protected MapView getView() {
 MapControllerView mapControllerView = new MapControllerView(mapController, new MindMapView(new View(new Color[0])));
 return mapControllerView.getMapView();
 }

 @Override
 protected Resources getResources() {
 return resources;
 }

 @Override
 protected ModeController getController() {
 return modeController;
 }
 };
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(null, "Description", null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(exportHook.chooseFile("png", null));
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile("png", "Description", null));
 }

 @Test
 public void testChooseFileNullMap() {
 final File mmFile = null;
 when(modeController.getMap()).thenReturn(mmFile);
 assertNull(exportHook.chooseFile("png", "Description", "png"));
 }

 @Test
 public void testChooseFileValidFile() throws IOException {
 final File mmFile = new File("path/to/mindmap.mm");
 when(modeController.getMap()).thenReturn(mmFile);
 final File chosenFile = new File("path/to/exported/image.png");
 final OutputStream outputStream = mock(OutputStream.class);
 final FileChooserUI fileChooserUI = mock(FileChooserUI.class);
 final FreeMindFileDialog chooser = new FreeMindFileDialog(frame, fileChooserUI);
 when(modeController.getFileChooser(any(ImageFilter.class))).thenReturn(chooser);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(outputStream.close()).thenReturn(true);
 final File proposedName = new File("path/to/mindmap.mm").getAbsoluteFile();
 proposedName.setName("exported_image.png");
 when(chooser.getSelectedFile()).thenReturn(proposedName);
 final String ext = "png";
 when(chooser.getFileFilter()).thenReturn(new ImageFilter(ext, null));
 final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 final byte[] imageBytes = Tools.imageToByteArray(image, ext);
 when(outputStream.write(anyByteArray())).thenAnswer(i -> imageBytes.length);
 when(outputStream.write(anyByte[].class, anyInt(), 0, anyInt()))
 .thenAnswer(i -> System.arraycopy(imageBytes, 0, ((byte[]) i), 0, imageBytes.length));

 assertEquals(chosenFile, exportHook.chooseFile("png", "Description", "png"));
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 final String resourceName = "resource.key";
 final String expected = "Expected Translation";
 when(modeController.getText(resourceName)).thenReturn(expected);
 assertEquals(expected, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testCreateBufferedImageNullController() {
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageNullControllerView() {
 exportHook.view = null;
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 final BufferedImage expectedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 when(exportHook.getController().getView()).thenReturn(exportHook.view);
 exportHook.view.preparePrinting();
 exportHook.view.setInnerBounds(new Rectangle(0, 0, 1, 1));
 when(exportHook.view.createImage(anyInt(), anyInt())).thenReturn(expectedImage);
 exportHook.view.endPrinting();
 assertEquals(expectedImage, exportHook.createBufferedImage());
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 exportHook.copyFromResource(null, "filename.ext", "/path/to/destination");
 }

 @Test
 public void testCopyFromResourceNullFileName() {
 exportHook.copyFromResource("prefix/", null, "/path/to/destination");
 }

 @Test
 public void testCopyFromResourceValidFile() throws IOException {
 final URL resource = new URL("file:/path/to/resource.ext");
 when(exportHook.getResource(exportHook.prefix + "filename.ext")).thenReturn(resource);
 final OutputStream outputStream = mock(OutputStream.class);
 final File destinationDirectory = new File("/path/to/destination");
 when(outputStream.close()).thenReturn(true);
 final InputStream inputStream = resource.openStream();
 final byte[] imageBytes = Tools.inputStreamToByteArray(inputStream);
 when(outputStream.write(anyByteArray())).thenAnswer(i -> imageBytes.length);
 when(outputStream.write(anyByte[].class, anyInt(), 0, anyInt()))
 .thenAnswer(i -> System.arraycopy(imageBytes, 0, ((byte[]) i), 0, imageBytes.length));
 exportHook.copyFromResource(exportHook.prefix, "filename.ext", "/path/to/destination");
 }

 @Test
 public void testCopyFromFileNullDir() {
 exportHook.copyFromFile(null, "filename.ext", "/path/to/destination");
 }

 @Test
 public void testCopyFromFileNullFileName() {
 exportHook.copyFromFile("/path/to/dir/", null, "/path/to/destination");
 }

 @Test
 public void testCopyFromFileValidFile() throws IOException {
 final File resource = new File("/path/to/resource.ext");
 when(exportHook.getResource(exportHook.prefix + "filename.ext")).thenThrow(new FileNotFoundException());
 final File destinationDirectory = new File("/path/to/destination");
 when(exportHook.copyStream(anyInputStream(), anyOutputStream(), anyBoolean())).thenAnswer(i -> true);
 exportHook.copyFromFile("/path/to/dir/filename.ext", "filename.ext", "/path/to/destination");
 }
}