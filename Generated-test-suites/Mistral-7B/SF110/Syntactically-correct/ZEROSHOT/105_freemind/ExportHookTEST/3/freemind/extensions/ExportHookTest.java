package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSaveDialog;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.actions.export.ExportAction;
import freemind.extensions.ExportHook;
import freemind.main.MindMapDocument;
import freemind.main.MindMapModel;
import freemind.main.Resources;
import freemind.modes.MapView;
import freemind.view.mindmapview.MapViewImpl;
import freemind.view.printing.PrintingHelper;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeController;
 private Controller controller;
 private MapView mapView;
 private PrintingHelper printingHelper;
 private MindMapDocument mindMapDocument;
 private MindMapModel mindMapModel;

 @Before
 public void setup() {
 exportHook = new ExportHook();
 modeController = Mockito.mock(ModeController.class);
 controller = Mockito.mock(Controller.class);
 mapView = Mockito.mock(MapViewImpl.class);
 printingHelper = Mockito.mock(PrintingHelper.class);
 mindMapDocument = Mockito.mock(MindMapDocument.class);
 mindMapModel = Mockito.mock(MindMapModel.class);
 ExportAction.setPrintingHelper(printingHelper);

 exportHook.setController(controller);
 }

 @After
 public void tearDown() {
 exportHook = null;
 modeController = null;
 controller = null;
 mapView = null;
 printingHelper = null;
 mindMapDocument = null;
 mindMapModel = null;
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG Image (*.png)";
 final String nameExtension = "image";
 File expectedFile = new File("expectedFile.png");
 File resultFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, resultFile);
 }

 @Test
 public void testChooseFile_whenFileWithSameNameAndExtensionAlreadyExists_shouldReturnFalse() {
 final String type = "png";
 final String description = "PNG Image (*.png)";
 final String nameExtension = "image";
 final File file = new File("existingFile.png");
 final File saveDirectory = new File("saveDirectory");
 final File resultFile = exportHook.chooseFile(type, description, nameExtension, modeController, file, saveDirectory);
 assertNull(resultFile);
 }

 @Test
 public void testChooseFile_whenFileHasDifferentExtension_shouldReturnFileWithCorrectExtension() {
 final String type = "png";
 final String description = "PNG Image (*.png)";
 final String nameExtension = "image";
 final File file = new File("existingFile.jpg");
 final File saveDirectory = new File("saveDirectory");
 final File resultFile = exportHook.chooseFile(type, description, nameExtension, modeController, file, saveDirectory);
 final File expectedFile = new File("saveDirectory/existingFile.png");
 assertEquals(expectedFile, resultFile);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%export.saveAsTitle";
 String expectedString = "Save As...";
 String resultString = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedString, resultString);
 }

 @Test
 public void testCreateBufferedImage() {
 Image resultImage = exportHook.createBufferedImage();
 assertNotNull(resultImage);
 }

 @Test
 public void testCreateBufferedImage_whenViewIsNull_shouldReturnNull() {
 exportHook.view = null;
 Image resultImage = exportHook.createBufferedImage();
 assertNull(resultImage);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 final String prefix = "/path/to/resources/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "/path/to/destination/directory/";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory + "/testFile.txt");
 File resultFile = new File(destinationDirectory);
 assertTrue(resultFile.exists());
 assertEquals(expectedFile, resultFile.listFiles()[0]);
 }

 @Test
 public void testCopyFromFile() throws IOException {
 final String dir = "/path/to/source/directory/";
 final String fileName = "testFile.txt";
 final String destinationDirectory = "/path/to/destination/directory/";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory + "/testFile.txt");
 File resultFile = new File(destinationDirectory);
 assertTrue(resultFile.exists());
 assertEquals(expectedFile, resultFile.listFiles()[0]);
 }

 @Test
 public void testCreateImageFilter() {
 FileFilter expectedFileFilter = Mockito.mock(FileFilter.class);
 Mockito.when(expectedFileFilter.accept(Mockito.any(File.class))).then(new Answer<Boolean>() {
 @Override
 public Boolean answer(InvocationOnMock invocation) throws Throwable {
 return true;
 }
 });
 FileFilter resultFileFilter = exportHook.new ImageFilter("png", "PNG Image (*.png)");
 assertEquals(expectedFileFilter, resultFileFilter);
 }

 @Test
 public void testShowSaveFileDialog() {
 final JFrame frame = Mockito.mock(JFrame.class);
 final FileSaveDialog saveDialog = Mockito.mock(FileSaveDialog.class);
 final File file = new File("testFile.png");
 final String description = "Save Image";
 final String nameExtension = "png";
 final ModeController controller = Mockito.mock(ModeController.class);
 final MindMapDocument mindMapDocument = Mockito.mock(MindMapDocument.class);
 final MindMapModel mindMapModel = Mockito.mock(MindMapModel.class);
 Mockito.when(controller.getFrame()).thenReturn(frame);
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(saveDialog);
 Mockito.when(controller.getMap()).thenReturn(mindMapDocument);
 Mockito.when(mindMapDocument.getFile()).thenReturn(file);
 final File resultFile = exportHook.chooseImageFile("png", description, nameExtension, controller);
 assertEquals(file, resultFile);
 }

 @Test
 public void testShowSaveFileDialog_whenFileIsAlreadyExistsWithSameNameAndExtension_shouldShowSaveFileDialogWithProposedName() {
 final JFrame frame = Mockito.mock(JFrame.class);
 final FileSaveDialog saveDialog = Mockito.mock(FileSaveDialog.class);
 final File file = new File("testFile.png");
 final String description = "Save Image";
 final String nameExtension = "png";
 final ModeController controller = Mockito.mock(ModeController.class);
 final MindMapDocument mindMapDocument = Mockito.mock(MindMapDocument.class);
 final MindMapModel mindMapModel = Mockito.mock(MindMapModel.class);
 final String proposedName = "testFile.png_copy";
 Mockito.when(controller.getFrame()).thenReturn(frame);
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(saveDialog);
 Mockito.when(controller.getMap()).thenReturn(mindMapDocument);
 Mockito.when(mindMapDocument.getFile()).thenReturn(file);
 Mockito.when(saveDialog.showSaveDialog(Mockito.any(Component.class))).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return JOptionPane.YES_OPTION;
 }
 });
 final File resultFile = exportHook.chooseImageFile("png", description, nameExtension, controller, file, null);
 assertEquals(new File("testFile.png_copy.png"), resultFile);
 }

 @Test
 public void testShowSaveFileDialog_whenFileIsAlreadyExistsWithDifferentExtension_shouldShowSaveFileDialogWithProposedNameAndExtension() {
 final JFrame frame = Mockito.mock(JFrame.class);
 final FileSaveDialog saveDialog = Mockito.mock(FileSaveDialog.class);
 final File file = new File("testFile.png");
 final String description = "Save Image";
 final String nameExtension = "image";
 final ModeController controller = Mockito.mock(ModeController.class);
 final MindMapDocument mindMapDocument = Mockito.mock(MindMapDocument.class);
 final MindMapModel mindMapModel = Mockito.mock(MindMapModel.class);
 final String proposedName = "testFile.png_copy";
 final String extension = "png";
 Mockito.when(controller.getFrame()).thenReturn(frame);
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(saveDialog);
 Mockito.when(controller.getMap()).thenReturn(mindMapDocument);
 Mockito.when(mindMapDocument.getFile()).thenReturn(file);
 Mockito.when(saveDialog.showSaveDialog(Mockito.any(Component.class))).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return JOptionPane.YES_OPTION;
 }
 });
 final File resultFile = exportHook.chooseImageFile("png", description, nameExtension, controller, file, null);
 assertEquals(new File(proposedName + "." + extension), resultFile);
 }

 @Test
 public void testShowSaveFileDialog_whenFileDoesNotExist_shouldCreateNewFileWithProposedNameAndExtension() {
 final JFrame frame = Mockito.mock(JFrame.class);
 final FileSaveDialog saveDialog = Mockito.mock(FileSaveDialog.class);
 final File saveDirectory = new File("saveDirectory");
 final String description = "Save Image";
 final String nameExtension = "png";
 final ModeController controller = Mockito.mock(ModeController.class);
 final MindMapDocument mindMapDocument = Mockito.mock(MindMapDocument.class);
 final MindMapModel mindMapModel = Mockito.mock(MindMapModel.class);
 final String proposedName = "testFile";
 final String extension = "png";
 Mockito.when(controller.getFrame()).thenReturn(frame);
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(saveDialog);
 Mockito.when(controller.getMap()).thenReturn(mindMapDocument);
 Mockito.when(saveDialog.showSaveDialog(Mockito.any(Component.class))).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return JOptionPane.YES_OPTION;
 }
 });
 final File resultFile = exportHook.chooseImageFile("png", description, nameExtension, controller, null, saveDirectory);
 final File expectedFile = new File(saveDirectory, proposedName + "." + extension);
 assertEquals(expectedFile, resultFile);
 }

 @Test
 public void testShowSaveFileDialog_whenUserCancels_shouldReturnNull() {
 final JFrame frame = Mockito.mock(JFrame.class);
 final FileSaveDialog saveDialog = Mockito.mock(FileSaveDialog.class);
 final ModeController controller = Mockito.mock(ModeController.class);
 final MindMapDocument mindMapDocument = Mockito.mock(MindMapDocument.class);
 final MindMapModel mindMapModel = Mockito.mock(MindMapModel.class);
 Mockito.when(controller.getFrame()).thenReturn(frame);
 Mockito.when(controller.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(saveDialog);
 Mockito.when(controller.getMap()).thenReturn(mindMapDocument);
 Mockito.when(saveDialog.showSaveDialog(Mockito.any(Component.class))).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return JOptionPane.CANCEL_OPTION;
 }
 });
 final File resultFile = exportHook.chooseImageFile("png", "Save Image", "png", controller, null, null);
 assertNull(resultFile);
 }
}