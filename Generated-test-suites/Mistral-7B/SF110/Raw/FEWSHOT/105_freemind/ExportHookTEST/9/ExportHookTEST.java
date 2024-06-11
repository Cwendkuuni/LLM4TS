package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import javax.swing.filechooser.*;
import java.text.*;
import javax.swing.*;
import freemind.modes.*;
import java.awt.image.*;
import java.awt.*;
import freemind.main.*;
import java.net.*;
import java.io.*;
import org.mockito.MockitoAnnotations;

public class ExportHookTest {

 private ModeController mockController;
 private ExportHook exportHook;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockController = mock(ModeController.class);
 exportHook = new ExportHook();
 exportHook.setController(mockController);
 }

 @Test
 public void testChooseFileNullType() {
 assertNull(exportHook.chooseFile(null, "Description", null));
 verify(mockController, times(0)).getFileChooser(any(ImageFilter.class));
 }

 @Test
 public void testChooseFileValidType() {
 String type = "png";
 File expectedFile = new File("path/to/file.png");
 when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
 doReturn(expectedFile).when(mockController).getMap();

 File actualFile = exportHook.chooseFile(type, "Description", null);
 assertEquals(expectedFile, actualFile);
 verify(mockController, times(1)).getFileChooser(argThat(imageFilter -> Tools.safeEqualsIgnoreCase(imageFilter.getType(), type)));
 }

 @Test
 public void testCreateBufferedImage() {
 MapView mockView = mock(MapView.class);
 when(mockController.getView()).thenReturn(mockView);
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertNotNull(actualImage);
 verify(mockView, times(1)).preparePrinting();
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%resourceKey%";
 String expectedString = "expected translation";
 when(mockController.getText(resourceName.substring(1))).thenReturn(expectedString);
 String actualString = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedString, actualString);
 }
}