package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;
import static org.mockito.Mockito.mock;

public class ExportHookTest {

 private ModeController controller;
 private ExportHook exportHook;
 private MapController mapController;
 private MapView view;
 private FileChooserModel fileChooserModel;
 private FreeMindFileDialog fileChooser;
 private JFrame frame;

 @Before
 public void setUp() {
 controller = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(controller);
 mapController = mock(MapController.class);
 when(controller.getMap()).thenReturn(mapController);
 view = mock(MapView.class);
 when(controller.getView()).thenReturn(view);
 fileChooserModel = mock(FreeMindFileDialog.FreeMindFileFilterModel.class);
 fileChooser = mock(FreeMindFileDialog.class);
 when(controller.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);
 frame = mock(JFrame.class);
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(null, "Description", null));
 verifyNoInteractions(fileChooser);
 }

 @Test
 public void testChooseFileUnknownType() {
 final String type = "unknownType";
 final String description = "Description";
 final String nameExtension = null;
 assertNull(exportHook.chooseFile(type, description, nameExtension));
 verify(fileChooser, never()).setSelectedFile(any(File.class));
 }

 @Test
 public void testChooseFileValidType() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "png";
 final File proposedFile = new File("proposedFile");
 when(fileChooserModel.getSelectedFiles()).thenReturn(new File[] { proposedFile });
 when(fileChooser.getFileFilterModel()).thenReturn(fileChooserModel);
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(proposedFile, chosenFile);
 verify(fileChooser).setSelectedFile(proposedFile);
 verify(fileChooser).showSaveDialog(eq(frame)).times(1);
 verify(controller).getText("file_already_exists");
 }

 @Test
 public void testChooseFileFileWithSameExtension() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "png";
 final File proposedFile = new File("proposedFile.png");
 when(fileChooserModel.getSelectedFiles()).thenReturn(new File[] { proposedFile });
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 exportHook.chooseFile(type, description, nameExtension);
 verify(fileChooser).setSelectedFile(proposedFile);
 verify(fileChooser).showSaveDialog(eq(frame)).times(1);
 verify(controller).getText("file_already_exists");
 }

 @Test
 public void testChooseFileFileWithoutExtension() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = null;
 final File proposedFile = new File("proposedFile");
 when(fileChooserModel.getSelectedFiles()).thenReturn(new File[] { proposedFile });
 final File chosenFile = new File("chosenFile.png");
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(proposedFile);
 when(fileChooser.showSaveDialog(eq(frame))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(chosenFile);
 exportHook.chooseFile(type, description, nameExtension);
 assertEquals(chosenFile, proposedFile);
 verify(fileChooser).setSelectedFile(proposedFile);
 verify(fileChooser).showSaveDialog(eq(frame)).times(1);
 verify(controller).getText("file_already_exists");
 verifyZeroInteractions(mapController);
 }

 @Test
 public void testChooseFileFileWithDifferentExtension() {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "jpg";
 final File proposedFile = new File("proposedFile.png");
 when(fileChooserModel.getSelectedFiles()).thenReturn(new File[] { proposedFile });
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getMap()).thenReturn(mapController);
 when(mapController.getFile()).thenReturn(proposedFile);
 when(fileChooser.showSaveDialog(eq(frame))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(new File("chosenFile.png")); //File with different extension but same name
 exportHook.chooseFile(type, description, nameExtension);
 verify(fileChooser).setSelectedFile(proposedFile);
 verify(fileChooser).showSaveDialog(eq(frame)).times(1);
 verify(controller).getText("file_already_exists");
 verifyZeroInteractions(mapController);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertNull(exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringNoTranslationNeeded() {
 final String resourceName = "resourceName";
 final String returnValue = "returnValue";
 assertEquals(returnValue, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testGetTranslatableResourceStringWithTranslation() {
 final String resourceName = "%resourcePlaceholder%";
 final String expectedTranslation = "expectedTranslation";
 when(controller.getText(resourceName)).thenReturn(expectedTranslation);
 final String returnValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedTranslation, returnValue);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedBufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
 when(controller.getView()).thenReturn(view);
 when(view.createImage(anyInt(), anyInt())).thenReturn(expectedBufferedImage);
 final BufferedImage actualBufferedImage = exportHook.createBufferedImage();
 assertEquals(expectedBufferedImage, actualBufferedImage);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(exportHook.createBufferedImage());
 verifyNoInteractions(view);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 exportHook.copyFromResource(null, "fileName.ext", "destinationDirectory");
 verifyNoInteractions(logger);
 }

 @Test
 public void testCopyFromResourceInvalidFile() {
 exportHook.copyFromResource("prefix/", "InvalidFile.ext", "destinationDirectory");
 verifyNoInteractions(logger);
 }

 @Test
 public void testCopyFromResourceValidFile() throws IOException {
 final String prefix = "prefix/";
 final String fileName = "fileName.ext";
 final String destinationDirectory = "destinationDirectory";
 final InputStream inputStream = new ByteArrayInputStream("InputStreamContent".getBytes());
 final OutputStream outputStream = new ByteArrayOutputStream();
 when(getResource(prefix + fileName)).thenReturn(new URL("resource:prefix/fileName.ext"));
 when(getResource(prefix + fileName).openStream()).thenReturn(inputStream);
 when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 assertEquals("InputStreamContent", new String(outputStream.toByteArray()));
 verify(logger).finest("searching for prefix/fileName.ext");
 verify(getResource(prefix + fileName)).once();
 verify(getResource(prefix + fileName).openStream()).once();
 verify(new FileOutputStream(destinationDirectory + "/" + fileName)).once();
 }

 @Test
 public void testCopyFromFileNullDir() {
 exportHook.copyFromFile(null, "fileName.ext", "destinationDirectory");
 verifyNoInteractions(logger);
 }

 @Test
 public void testCopyFromFileInvalidFile() {
 exportHook.copyFromFile("dir/", "InvalidFile.ext", "destinationDirectory");
 verifyNoInteractions(logger);
 }

 @Test
 public void testCopyFromFileValidFile() throws IOException {
 final String dir = "dir/";
 final String fileName = "fileName.ext";
 final String destinationDirectory = "destinationDirectory";
 final File resource = new File(dir, fileName);
 final InputStream inputStream = new FileInputStream(resource);
 final OutputStream outputStream = new ByteArrayOutputStream();
 when(new FileOutputStream(destinationDirectory + "/" + fileName)).thenReturn(outputStream);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 assertEquals(Tools.readFile(resource), new String(outputStream.toByteArray()));
 verify(logger).finest("searching for dir/fileName.ext");
 verify(new FileInputStream(resource)).once();
 verify(new FileOutputStream(destinationDirectory + "/" + fileName)).once();
 }
}