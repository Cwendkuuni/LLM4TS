package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.view.mindmapview.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController modeControllerMock;
 private MapView mapViewMock;

 @Before
 public void setUp() {
 modeControllerMock = mock(ModeController.class);
 mapViewMock = mock(MapView.class);
 exportHook = new ExportHook() {
 @Override
 MapView getController() {
 return modeControllerMock;
 }

 @Override
 MapView getView() {
 return mapViewMock;
 }
 };
 }

 @Test
 public void testChooseFile() throws IOException {
 final String type = "png";
 final String description = "Description";
 final String nameExtension = "test";
 final File expectedFile = new File("expectedFile.png");
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);

 when(modeControllerMock.getFrame()).thenReturn(Mockito.mock(Frame.class));
 when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 when(modeControllerMock.getMap()).thenReturn(Mockito.mock(MMapModel.class));
 when(modeControllerMock.getText(anyString())).thenReturn("Text");

 final JFileChooser fileChooser = Mockito.mock(JFileChooser.class);
 when(fileChooser.showSaveDialog(any(Component.class))).thenReturn(JFileChooser.APPROVE_OPTION);
 when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

 when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooser);

 assertEquals(expectedFile, exportHook.chooseFile(type, description, nameExtension));

 verify(modeControllerMock, times(1)).getText("Text");
 verify(modeControllerMock, times(1)).getFrame();
 verify(modeControllerMock, times(1)).getFileChooser(any(ImageFilter.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFile_nullType() {
 final String description = "Description";
 final String nameExtension = "test";
 exportHook.chooseFile(null, description, nameExtension);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFile_nullDescription() {
 final String type = "png";
 final String nameExtension = "test";
 exportHook.chooseFile(type, null, nameExtension);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testChooseFile_nullNameExtension() {
 final String type = "png";
 final String description = "Description";
 exportHook.chooseFile(type, description, null);
 }

 @Test
 public void testCreateBufferedImage() {
 BufferedImage actualImage = exportHook.createBufferedImage();
 assertNotNull(actualImage);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resourceName%";
 final String expectedValue = "Expected Value";

 when(modeControllerMock.getText(resourceName.substring(1))).thenReturn(expectedValue);

 assertEquals(expectedValue, exportHook.getTranslatableResourceString(resourceName));
 }

 @Test
 public void testGetTranslatableResourceString_nullResourceName() {
 final String nullResourceName = null;
 assertEquals(nullResourceName, exportHook.getTranslatableResourceString(nullResourceName));
 }
}