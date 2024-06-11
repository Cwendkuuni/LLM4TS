package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.extensions.ExportHook;
import freemind.main.FreeMindController;
import freemind.main.FreeMindFrame;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MapViewController;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 @Mock
 private FreeMindController freeMindController;
 @Mock
 private MapViewController mapViewController;
 @Mock
 private MapView mapView;
 @Mock
 private FreeMindFrame freeMindFrame;

 private ExportHook exportHook;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 exportHook.setController(freeMindController);
 exportHook.setView(mapViewController);
 exportHook.setFrame(freeMindFrame);
 }

 @Test
 public void testChooseFile_ValidFileChosen() {
 String type = "png";
 String description = "PNG Image";
 File expectedFile = new File("path/to/expected/file.png");

 Mockito.when(freeMindController.getFileChooser(ArgumentMatchers.isA(ImageFilter.class)))
 .thenReturn((FileDialog) Mockito.mock(FileDialog.class));

 Mockito.when(mapViewController.getController()).thenReturn(freeMindController);
 Mockito.when(mapViewController.getMap()).thenReturn(Mockito.mock(File.class));
 Mockito.when(mapViewController.getMap().getFile()).thenReturn(null);

 File chosenFile = exportHook.chooseFile(type, description, null);

 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_NullFileChosen() {
 String type = "png";
 String description = "PNG Image";

 File chosenFile = exportHook.chooseFile(type, description, null);

 assertNull(chosenFile);
 }

 @Test
 public void testCreateBufferedImage() throws Exception {
 Image expectedImage = Mockito.mock(Image.class);
 Graphics graphics = Mockito.mock(Graphics.class);

 Mockito.when(mapViewController.getController()).thenReturn(freeMindController);
 Mockito.when(mapViewController.getView()).thenReturn(mapView);
 Mockito.when(mapView.createImage(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
 .thenReturn(expectedImage);
 Mockito.when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));

 BufferedImage bufferedImage = exportHook.createBufferedImage();

 Mockito.verify(mapView, Mockito.times(1)).preparePrinting();
 Mockito.verify(mapView, Mockito.times(1)).print(Mockito.eq(graphics));
 Mockito.verify(mapView, Mockito.times(1)).endPrinting();

 assertEquals(expectedImage, bufferedImage.getSource());
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%1$s";
 String expectedValue = "expected value";

 Mockito.when(freeMindController.getText(ArgumentMatchers.anyString())).thenReturn(expectedValue);

 String result = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedValue, result);
 }

 @Test
 public void testGetTranslatableResourceString_NullReturnValue() {
 String resourceName = "%1$s";

 String result = exportHook.getTranslatableResourceString(resourceName);

 assertNull(result);
 }
}