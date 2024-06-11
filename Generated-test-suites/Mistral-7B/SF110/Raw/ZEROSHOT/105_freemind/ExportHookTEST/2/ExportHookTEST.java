package freemind.extensions.test;

import static org.junit.Assert.*;
import static freemind.extensions.ExportHook.*;

import javax.swing.filechooser.FileFilter;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;

 @Before
 public void setup() {
 modeControllerMock = Mockito.mock(ModeController.class);
 exportHook = new ExportHook() {
 @Override
 public ModeController getController() {
 return modeControllerMock;
 }
 };
 }

 @Test
 public void testChooseFileNullController() {
 assertNull(exportHook.chooseFile(null, null, null));
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(modeControllerMock, null, null));
 }

 @Test
 public void testChooseFileNullDescription() {
 assertNull(exportHook.chooseFile(modeControllerMock, "type", null));
 }

 @Test
 public void testChooseFileValidFileChosen() throws IOException {
 final File file = new File("test.png");
 Mockito.when(modeControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(modeControllerMock.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(modeControllerMock.getMap()).thenReturn(Mockito.mock(MindMapModel.class));

 final File chosenFile = exportHook.chooseImageFile("type", "description", null, modeControllerMock);
 assertEquals(file.getAbsolutePath(), chosenFile.getAbsolutePath());
 }

 @Test
 public void testChooseFileInvalidExtension() throws IOException {
 final File file = new File("test.txt");
 Mockito.when(modeControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(modeControllerMock.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(modeControllerMock.getMap()).thenReturn(Mockito.mock(MindMapModel.class));

 final File chosenFile = exportHook.chooseImageFile("type", "description", null, modeControllerMock);
 assertNull(chosenFile);
 }

 @Test
 public void testChooseFileFileAlreadyExists() {
 final File file = new File("test.png");
 Mockito.when(modeControllerMock.getFrame()).thenReturn(Mockito.mock(JFrame.class));
 Mockito.when(modeControllerMock.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(modeControllerMock.getText("file_already_exists")).thenReturn("File already exists.");

 final File chosenFile = exportHook.chooseImageFile("type", "description", null, modeControllerMock);
 assertNull(chosenFile);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 assertEquals("", exportHook.getTranslatableResourceString(null));
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 final String resourceName = "test.resource";
 final String expected = "test";
 assertEquals(expected, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testCreateBufferedImageNullView() {
 assertNull(exportHook.createBufferedImage());
 }

 @Test
 public void testCreateBufferedImageValidView() throws Exception {
 final BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 Mockito.when(exportHook.getController()).thenReturn(modeControllerMock);
 Mockito.when(modeControllerMock.getView()).thenReturn(Mockito.mock(MindMapView.class));
 Mockito.when(modeControllerMock.getView().getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 Mockito.when(modeControllerMock.getView().createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(expectedImage);

 final BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 exportHook.copyFromResource(null, "test.png", "");
 }

 @Test
 public void testCopyFromResourceValidPrefixAndFileName() throws IOException {
 exportHook.copyFromResource("prefix/", "test.png", "destination");
 }

 @Test
 public void testCopyFromFileNullDir() {
 exportHook.copyFromFile(null, "test.png", "");
 }

 @Test
 public void testCopyFromFileValidDirAndFileName() throws IOException {
 exportHook.copyFromFile("/path/to/dir/", "test.png", "destination");
 }

 @Test
 public void testCreateImageFilterValidTypeAndDescription() {
 final FileFilter imageFilter = exportHook.new ImageFilter("png", "Portable Network Graphics (*.png)");
 assertEquals("Portable Network Graphics (*.png)", imageFilter.getDescription());
 assertTrue(imageFilter.accept(new File("test.png")));
 assertFalse(imageFilter.accept(new File("test.txt")));
 }
}