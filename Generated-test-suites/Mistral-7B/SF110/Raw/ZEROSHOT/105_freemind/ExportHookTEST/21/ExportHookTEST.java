package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import freemind.controller.Controller;
import freemind.controller.ControllerListener;
import freemind.controller.ModeController;
import freemind.controller.actions.PrintAction;
import freemind.extensions.ExportHook;
import freemind.main.FreeMind;
import freemind.main.Logger;
import freemind.resources.Tools;
import freemind.view.mindmapview.MapView;
import freemind.view.swing.FreeMindFileDialog;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 private Controller controller;
 private ModeController modeController;
 private Logger logger;
 private MapView view;

 @Before
 public void setUp() {
 controller = Mockito.mock(Controller.class);
 modeController = Mockito.mock(ModeController.class);
 logger = Mockito.mock(Logger.class);
 exportHook = new ExportHook() {
 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public Logger getLogger() {
 return logger;
 }

 @Override
 public MapView getView() {
 return view;
 }

 @Override
 public ModeController getModeController() {
 return modeController;
 }
 };
 view = Mockito.mock(MapView.class);
 }

 @After
 public void tearDown() {
 exportHook = null;
 controller = null;
 modeController = null;
 logger = null;
 view = null;
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG";
 final String nameExtension = null;
 final File expectedFile = new File("exportedFile.png");
 final FileDialog chooser = Mockito.mock(FileDialog.class);
 final FreeMindFileDialog freeMindFileDialog = Mockito.mock(FreeMindFileDialog.class);
 final Rectangle innerBounds = Mockito.mock(Rectangle.class);
 final Image image = Mockito.mock(Image.class);
 final Graphics graphics = Mockito.mock(Graphics.class);

 when(controller.getFrame()).thenReturn(new JFrame());
 when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(freeMindFileDialog);
 when(freeMindFileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(view.getInnerBounds()).thenReturn(innerBounds);
 when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
 when(view.print(Mockito.any(Graphics.class))).thenReturn(true);
 when(logger.isFinestEnabled()).thenReturn(true);

 ArgumentCaptor<ImageFilter> imageFilterArgumentCaptor = ArgumentCaptor.forClass(ImageFilter.class);
 ArgumentCaptor<String> nameExtensionArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> destinationDirectoryArgumentCaptor = ArgumentCaptor.forClass(String.class);

 OngoingStubbing<File> mmFileStubbing = Mockito.doReturn(new File("mapFile.mindmap"));
 when(controller.getMap()).thenReturn(mmFileStubbing.createMock());

 when(freeMindFileDialog.setSelectedFile(Mockito.any(File.class))).thenReturn(expectedFile);
 when(freeMindFileDialog.getFileFilter()).thenReturn(new ImageFilter(type, description));
 when(controller.getText(Mockito.anyString())).thenReturn("File already exists");

 File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 assertEquals(expectedFile, chosenFile);

 verify(controller, times(1)).getFrame();
 verify(controller, times(1)).getFileChooser(imageFilterArgumentCaptor.capture());
 assertEquals(type, imageFilterArgumentCaptor.getValue().getType());
 verify(controller, times(1)).getText("file_already_exists");
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%s_FILE_ALREADY_EXISTS";
 final String expectedString = "File already exists";

 assertEquals(expectedString, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testCreateBufferedImage() {
 final Image image = Mockito.mock(Image.class);
 final Graphics graphics = Mockito.mock(Graphics.class);

 when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(view.preparePrinting()).thenReturn(true);
 when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
 when(view.print(Mockito.any(Graphics.class))).thenReturn(true);
 when(view.getWidth()).thenReturn(100);
 when(view.getHeight()).thenReturn(100);
 when(logger.isFinestEnabled()).thenReturn(true);

 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertEquals("Unexpected buffered image size", 100, bufferedImage.getWidth());
 assertEquals("Unexpected buffered image size", 100, bufferedImage.getHeight());
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "prefix/";
 final String fileName = "test.txt";
 final String destinationDirectory = "destinationDirectory/";

 when(logger.isFinestEnabled()).thenReturn(true);
 when(getResource(prefix + fileName)).thenReturn(new URL("resource:/test.txt"));

 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 verify(logger, times(1)).finest("searching for prefix/test.txt");
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "testDir/";
 final String fileName = "test.txt";
 final String destinationDirectory = "destinationDirectory/";

 when(logger.isFinestEnabled()).thenReturn(true);
 when(new File(dir, fileName)).thenReturn(new File("testFile.txt"));

 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 verify(logger, times(1)).finest("searching for testDir/test.txt");
 }

 @Test
 public void testChooseImageFileWithEmptyNameExtension() {
 final String type = "png";
 final String description = "PNG";
 final String nameExtension = null;
 final File expectedFile = new File("exportedFile.png");
 final FileDialog chooser = Mockito.mock(FileDialog.class);
 final FreeMindFileDialog freeMindFileDialog = Mockito.mock(FreeMindFileDialog.class);
 final Rectangle innerBounds = Mockito.mock(Rectangle.class);
 final Image image = Mockito.mock(Image.class);
 final Graphics graphics = Mockito.mock(Graphics.class);

 when(controller.getFrame()).thenReturn(new JFrame());
 when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(freeMindFileDialog);
 when(freeMindFileDialog.showSaveDialog(Mockito.any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(view.getInnerBounds()).thenReturn(innerBounds);
 when(view.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(image);
 when(view.print(Mockito.any(Graphics.class))).thenReturn(true);
 when(logger.isFinestEnabled()).thenReturn(true);

 ArgumentCaptor<ImageFilter> imageFilterArgumentCaptor = ArgumentCaptor.forClass(ImageFilter.class);
 ArgumentCaptor<String> nameExtensionArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<String> destinationDirectoryArgumentCaptor = ArgumentCaptor.forClass(String.class);

 OngoingStubbing<File> mmFileStubbing = Mockito.doReturn(new File("mapFile.mindmap"));
 when(controller.getMap()).thenReturn(mmFileStubbing.createMock());

 when(freeMindFileDialog.setSelectedFile(Mockito.any(File.class))).thenReturn(expectedFile);
 when(freeMindFileDialog.getFileFilter()).thenReturn(new ImageFilter(type, description));
 when(controller.getText("file_already_exists")).thenReturn("File already exists");
 when(controller.getModeController()).thenReturn(modeController);
 when(controller.getView()).thenReturn(view);
 when(controller.getFrame()).thenReturn(new JFrame());
 when(controller.getView()).thenReturn(view);
 when(controller.getControllerListener()).thenReturn(Mockito.mock(ControllerListener.class));
 when(controller.getAction(PrintAction.class)).thenReturn(Mockito.mock(PrintAction.class));
 when(controller.getFileChooser(Mockito.any(ImageFilter.class))).thenReturn(chooser);
 when(controller.getMap()).thenReturn(mmFileStubbing.createMock());
 when(controller.getText("file_already_exists")).thenReturn(MessageFormat.format(new MessageFormat("%s_FILE_ALREADY_EXISTS"), new Object[] { "File already exists" }));

 exportHook.chooseImageFile(type, description, nameExtension, controller);

 verify(controller, times(1)).getFrame();
 verify(controller, times(1)).getFileChooser(imageFilterArgumentCaptor.capture());
 assertEquals(type, imageFilterArgumentCaptor.getValue().getType());
 verify(controller, times(1)).getText("file_already_exists");
 }
}