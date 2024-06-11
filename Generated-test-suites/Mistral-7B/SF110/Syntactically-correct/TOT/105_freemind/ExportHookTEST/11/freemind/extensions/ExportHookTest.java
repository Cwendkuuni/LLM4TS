package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import freemind.controller.Controller;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.ExportHook;
import freemind.extension.tools.Tools;
import freemind.main.Logger;
import freemind.mode.Modes;
import freemind.view.MindMapView;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {

 @Mock
 private Controller controller;
 @Mock
 private MapView view;
 @Mock
 private FreeMindFileDialog chooser;
 @Mock
 private Logger logger;
 @InjectMocks
 private ExportHook exportHook;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 exportHook.setController(controller);
 }

 @Test
 public void testChooseFile_NullController() {
 exportHook.setController(null);
 assertNull(exportHook.chooseFile("image/jpeg", "Save image", "jpg"));
 }

 @Test
 public void testChooseFile_NullView() {
 exportHook.view = null;
 assertNull(exportHook.chooseFile("image/jpeg", "Save image", "jpg"));
 }

 @Test
 public void testChooseFile_NullDescriptionAndExtension() {
 File result = exportHook.chooseFile("image/jpeg", null, null);
 assertNull(result);
 }

 @Test
 public void testChooseFile_ValidFileChosen() throws IOException {
 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(controller.getMap()).thenReturn(mock(MindMapView.class));
 File tempFile = File.createTempFile("test", "jpg");
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
 when(chooser.getSelectedFile()).thenReturn(tempFile);

 File result = exportHook.chooseFile("image/jpeg", "Save image", "jpg");
 assertEquals(tempFile, result);
 }

 @Test
 public void testChooseFile_FileAlreadyExists() throws IOException {
 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(controller.getMap()).thenReturn(mock(MindMapView.class));
 File tempFile = File.createTempFile("test", "jpg");
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");
 when(JOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.YES_OPTION);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
 when(chooser.getSelectedFile()).thenReturn(tempFile);

 File result = exportHook.chooseFile("image/jpeg", "Save image", "jpg");
 assertEquals(tempFile, result);
 }

 @Test
 public void testChooseFile_InvalidType() throws IOException {
 when(controller.getFrame()).thenReturn(mock(Frame.class));
 when(controller.getFileChooser(any(FileFilter.class))).thenReturn(chooser);
 when(controller.getMap()).thenReturn(mock(MindMapView.class));
 File tempFile = File.createTempFile("test", "jpg");
 when(controller.getText("file_already_exists")).thenReturn("File already exists, do you want to overwrite it?");
 when(JOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), 0)).thenReturn(JOptionPane.NO_OPTION);
 when(chooser.showSaveDialog(any(Container.class))).thenReturn(JOptionPane.OK_OPTION);
 when(controller.getMap().getFile()).thenReturn(new File("test.png"));
 when(chooser.getSelectedFile()).thenReturn(tempFile);
 when(Tools.safeEqualsIgnoreCase(Tools.getExtension(tempFile.getName()), "image/jpeg")).thenReturn(false);

 File result = exportHook.chooseFile("image/jpeg", "Save image", "jpg");
 assertNull(result);
 }

 @Test
 public void testGetTranslatableResourceString() {
 String result = exportHook.getTranslatableResourceString("%title%");
 assertEquals("%title%", result);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceName() {
 String result = exportHook.getTranslatableResourceString(null);
 assertNull(result);
 }

 @Test
 public void testCreateBufferedImage() {
 when(controller.getView()).thenReturn(view);
when(view.getInnerBounds()).thenReturn(new Rectangle(0, 0, 500, 500));
 BufferedImage result = exportHook.createBufferedImage();
 assertNotNull(result);
 }

 @Test
 public void testCreateBufferedImage_NullView() {
 exportHook.view = null;
 BufferedImage result = exportHook.createBufferedImage();
 assertNull(result);
 }

 @Test
 public void testCopyFromResource() throws IOException {
 String prefix = "freemind/extensions/ExportHook";
 String fileName = "example.txt";
 String destinationDirectory = "test";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromResource_NullPrefix() throws IOException {
 String fileName = "example.txt";
 String destinationDirectory = "test";
 exportHook.copyFromResource(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromResource_NullFileName() throws IOException {
 String prefix = "freemind/extensions/ExportHook";
 String destinationDirectory = "test";
 exportHook.copyFromResource(prefix, null, destinationDirectory);
 }

 @Test
 public void testCopyFromFile() throws IOException {
 String dir = "test";
 String fileName = "example.txt";
 String destinationDirectory = "test";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory, fileName);
 assertTrue(expectedFile.exists());
 }

 @Test
 public void testCopyFromFile_NullDir() throws IOException {
 String fileName = "example.txt";
 String destinationDirectory = "test";
 exportHook.copyFromFile(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFile_NullFileName() throws IOException {
 String dir = "test";
 String destinationDirectory = "test";
 exportHook.copyFromFile(dir, null, destinationDirectory);
 }
}