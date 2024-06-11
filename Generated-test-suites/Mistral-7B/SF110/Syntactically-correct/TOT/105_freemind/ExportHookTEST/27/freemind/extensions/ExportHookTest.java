package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.main.Controller;
import freemind.main.I18N;
import freemind.modes.MapController;
import freemind.view.MindMapView;
import freemind.view.mindmapview.MapView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Mock;
import org.jmock.lib.legacy.MockUp;

public class ExportHookTest extends MockObjectTestCase {

 private static final String TEST_TYPE = "testImageType";
 private static final String TEST_DESCRIPTION = "Test Image Description";
 private static final String TEST_NAME_EXTENSION = ".testExtension";
 private static final String TEST_RESOURCE_NAME = "%resourceName%";
 private static final String TEST_RESOURCE_FILE_NAME = "testResourceFileName.testType";
 private static final String TEST_DESTINATION_DIRECTORY = "testDestinationDirectory";
 private ExportHook exportHook;
 private MockController controllerMock;
 private MockMapController mapControllerMock;
 private MockImageFilter imageFilterMock;
 private MockFileChooser fileChooserMock;
 private MockFrame frameMock;
 private MockFileDialog chooserMock;
 private MapView viewMock;

 @Before
 public void setUp() {
 imageFilterMock = new MockUp<ImageFilter>(ImageFilter.class) {
 @Override
 public boolean accept(File file) {
 return true;
 }
 };
 fileChooserMock = mock(FileDialog.class);
 chooserMock = mock(FileDialog.class);
 controllerMock = mock(Controller.class);
 mapControllerMock = mock(MapController.class);
 viewMock = mock(MapView.class);
 exportHook = new ExportHook() {
 @Override
 MapController getController() {
 return controllerMock.proxy();
 }

 @Override
 ImageFilter getImageFilter(String type, String description) {
 return imageFilterMock.proxy();
 }

 @Override
 FileDialog getFileChooser(ImageFilter filter) {
 return chooserMock.proxy();
 }

 @Override
 MindMapView getView() {
 return viewMock.proxy();
 }

 @Override
 void preparePrinting() {
 // no-op
 }

 @Override
 void endPrinting() {
 // no-op
 }
 };
 ClassImposteriser.install(controllerMock);
 ClassImposteriser.install(mapControllerMock);
 ClassImposteriser.install(fileChooserMock);
 ClassImposteriser.install(chooserMock);
 ClassImposteriser.install(mapViewMock = mock(MapView.class));
 }

 @Test
 public void testChooseFileWithValidExtension() {
 when(controllerMock.getText(anyString())).thenReturn(TEST_DESCRIPTION);
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooserMock);
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("testMapFile.mindmap"));

 final File expectedFile = new File("testOutput.testType");
 when(fileChooserMock.showSaveDialog(any(Container.class))).thenAnswer(i -> {
 final File file = new File(TEST_DESTINATION_DIRECTORY, "testOutput.testType");
 chooserMock.setSelectedFile(file);
 return File.DIALOG_SAVE;
 });

 exportHook.chooseFile(TEST_TYPE, TEST_DESCRIPTION, TEST_NAME_EXTENSION);

 final File actualFile = exportHook.getController().getFileChooser(imageFilterMock.proxy()).getSelectedFile();
 assertEquals(expectedFile, actualFile);
 }

 @Test
 public void testChooseFileWithInvalidExtension() {
 when(controllerMock.getText(anyString())).thenReturn(TEST_DESCRIPTION);
 when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooserMock);
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 when(mapControllerMock.getFile()).thenReturn(new File("testMapFile.mindmap"));

 final File expectedFile = new File("testOutput.testType");
 when(fileChooserMock.showSaveDialog(any(Container.class))).thenAnswer(i -> {
 final File file = new File(TEST_DESTINATION_DIRECTORY, "testOutput.testType");
 chooserMock.setSelectedFile(file);
 return File.DIALOG_SAVE;
 });

 exportHook.chooseFile(TEST_TYPE, TEST_DESCRIPTION, TEST_NAME_EXTENSION);

 final File actualFile = exportHook.getController().getFileChooser(imageFilterMock.proxy()).getSelectedFile();
 assertEquals(expectedFile, actualFile);

 // Verify that the file name was corrected if necessary
 final String actualExtension = Tools.getExtension(actualFile.getName());
 assertEquals(TEST_TYPE, actualExtension);
 }

 @Test
 public void testGetTranslatableResourceString() {
 when(controllerMock.getText(TEST_RESOURCE_NAME)).thenReturn("test string");

 final String expected = I18N.getText("test string");
 assertEquals(expected, exportHook.getTranslatableResourceString(TEST_RESOURCE_NAME));
 }

 @Test
 public void testCreateBufferedImage() {
 when(controllerMock.getView()).thenReturn(viewMock);
 when(viewMock.getInnerBounds()).thenReturn(new Rectangle(10, 10, 200, 200));
 when(viewMock.createImage(anyInt(), anyInt())).thenReturn(mock(BufferedImage.class));

 final BufferedImage actualImage = exportHook.createBufferedImage();

 final BufferedImage expectedImage = mock(BufferedImage.class);
 verify(viewMock).preparePrinting();
 verify(viewMock).print(any(Graphics2D.class));
 verify(viewMock).getInnerBounds();
 verify(viewMock).createImage(anyInt(), anyInt());
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResource() {
 when(controllerMock.getLogger()).thenReturn(mock(Logger.class));

 exportHook.copyFromResource(TEST_PREFIX, TEST_RESOURCE_FILE_NAME, TEST_DESTINATION_DIRECTORY);

 final URL resource = mock(URL.class);
 when(controllerMock.getResource(TEST_PREFIX + TEST_RESOURCE_FILE_NAME)).thenReturn(resource);
 final InputStream inputStream = mock(InputStream.class);
 when(resource.openStream()).thenReturn(inputStream);
 final OutputStream outputStream = mock(OutputStream.class);
 when(new FileOutputStream(TEST_DESTINATION_DIRECTORY + "/" + TEST_RESOURCE_FILE_NAME)).thenReturn(outputStream);

 exportHook.copyStream(inputStream, outputStream, true);

 final File expectedFile = new File(TEST_DESTINATION_DIRECTORY, TEST_RESOURCE_FILE_NAME);
 assertEquals(expectedFile, exportHook.getController().getFile(TEST_DESTINATION_DIRECTORY, TEST_RESOURCE_FILE_NAME));
 }

 @Test
 public void testCopyFromFile() {
 when(controllerMock.getLogger()).thenReturn(mock(Logger.class));

 exportHook.copyFromFile("testDir", TEST_RESOURCE_FILE_NAME, TEST_DESTINATION_DIRECTORY);

 final File resourceFile = new File("testDir", TEST_RESOURCE_FILE_NAME);
 when(resourceFile.exists()).thenReturn(true);
 final InputStream inputStream = mock(InputStream.class);
 when(new FileInputStream(resourceFile)).thenReturn(inputStream);
 final OutputStream outputStream = mock(OutputStream.class);
 when(new FileOutputStream(TEST_DESTINATION_DIRECTORY + "/" + TEST_RESOURCE_FILE_NAME)).thenReturn(outputStream);

 exportHook.copyStream(inputStream, outputStream, true);

 final File expectedFile = new File(TEST_DESTINATION_DIRECTORY, TEST_RESOURCE_FILE_NAME);
 assertEquals(expectedFile, exportHook.getController().getFile(TEST_DESTINATION_DIRECTORY, TEST_RESOURCE_FILE_NAME));
 }

 @Test
 public void testCreateImageFilter() {
 final ImageFilter imageFilter = exportHook.getImageFilter(TEST_TYPE, TEST_DESCRIPTION);

 assertEquals(TEST_TYPE, imageFilter.getDescription());
 }
}