package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import freemind.controller.Controller;
import freemind.controller.ModeController;
import freemind.controller.Statusbar;
import freemind.controller.View;
import freemind.extensions.ExportHook;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Tools;
import freemind.main.Logger;
import freemind.main.Resources;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ViewController;

public class ExportHookTest {

 private Controller controller;
 private MapView mapView;
 private ViewController viewController;
 private Statusbar statusbar;
 private ExportHook exportHook;
 private BufferedImage bufferedImage;

 @Before
 public void setUp() {
 controller = Mockito.mock(Controller.class);
 mapView = Mockito.mock(MapView.class);
 viewController = Mockito.mock(ViewController.class);
 statusbar = Mockito.mock(Statusbar.class);
 exportHook = new ExportHook() {
 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public MapView getView() {
 return mapView;
 }

 @Override
 public ViewController getViewController() {
 return viewController;
 }

 @Override
 public Statusbar getStatusbar() {
 return statusbar;
 }

 @Override
 public Logger getLogger() {
 return Mockito.mock(Logger.class);
 }

 @Override
 public Resources getResources() {
 return Resources.getInstance();
 }
 };
 }

 @Test
 public void testChooseFile_whenFileNameIsProvided_thenReturnChosenFile() {
 final File chosenFile = new File("test.png");
 when(controller.getFileChooser(Mockito.any(ImageFilter.class)))
 .thenReturn(Mockito.mock(JFileChooser.class));
 when(controller.getText(Mockito.anyString())).thenReturn("testDescription");
 when(mapView.getController()).thenReturn(controller);
 when(mapView.getFile()).thenReturn(new File("testMap.mind"));
 when(controller.getMap()).thenReturn(new MapController());

 final File exportedFile = exportHook.chooseFile("png", "testDescription", "png");
 assertEquals(chosenFile, exportedFile);
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_whenControllerIsNull_thenThrowException() {
 exportHook.chooseFile("png", "testDescription", "png");
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_whenMapViewIsNull_thenThrowException() {
 exportHook = new ExportHook() {
 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public MapView getView() {
 return null;
 }

 @Override
 public ViewController getViewController() {
 return viewController;
 }

 @Override
 public Statusbar getStatusbar() {
 return statusbar;
 }

 @Override
 public Logger getLogger() {
 return Mockito.mock(Logger.class);
 }

 @Override
 public Resources getResources() {
 return Resources.getInstance();
 }
 };
 exportHook.chooseFile("png", "testDescription", "png");
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFile_whenControllerGetMapReturnsNull_thenThrowException() {
 exportHook = new ExportHook() {
 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public MapView getView() {
 return mapView;
 }

 @Override
 public ViewController getViewController() {
 return viewController;
 }

 @Override
 public Statusbar getStatusbar() {
 return statusbar;
 }

 @Override
 public Logger getLogger() {
 return Mockito.mock(Logger.class);
 }

 @Override
 public Resources getResources() {
 return Resources.getInstance();
 }
 };
 when(controller.getMap()).thenReturn(null);
 exportHook.chooseFile("png", "testDescription", "png");
 }

 @Test
 public void testCreateBufferedImage() {
 when(mapView.preparePrinting()).thenReturn(true);
 when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
 when(mapView.createImage(100, 100)).thenReturn(Mockito.mock(Image.class));
 when(mapView.getWidth()).thenReturn(100);
 when(mapView.getHeight()).thenReturn(100);
 when(mapView.print(Mockito.any(Graphics.class))).thenReturn(true);

 bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_whenMapViewIsNull_thenThrowException() {
 exportHook = new ExportHook() {
 @Override
 public Controller getController() {
 return controller;
 }

 @Override
 public MapView getView() {
 return null;
 }

 @Override
 public ViewController getViewController() {
 return viewController;
 }

 @Override
 public Statusbar getStatusbar() {
 return statusbar;
 }

 @Override
 public Logger getLogger() {
 return Mockito.mock(Logger.class);
 }

 @Override
 public Resources getResources() {
 return Resources.getInstance();
 }
 };
 exportHook.createBufferedImage();
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateBufferedImage_whenMapViewPreparePrintingReturnsFalse_thenThrowException() {
 when(mapView.preparePrinting()).thenReturn(false);
 exportHook.createBufferedImage();
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%test";
 final String expected = "test";

 when(controller.getText(Mockito.anyString())).thenReturn("testText");

 final String result = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testGetTranslatableResourceString_whenControllerIsNull_thenThrowException() {
 exportHook.getTranslatableResourceString("test");
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "test";
 final String fileName = "test.txt";
 final String destinationDirectory = "testDestination";

 when(controller.getLogger()).thenReturn(Mockito.mock(Logger.class));
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);

 verify(controller.getLogger(), times(1)).finest("searching for testtest.txt");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_whenControllerIsNull_thenThrowException() {
 exportHook.copyFromResource("test", "test.txt", "testDestination");
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "test";
 final String fileName = "test.txt";
 final String destinationDirectory = "testDestination";

 when(controller.getLogger()).thenReturn(Mockito.mock(Logger.class));
 exportHook.copyFromFile(dir, fileName, destinationDirectory);

 verify(controller.getLogger(), times(1)).finest("searching for testtest.txt");
 }

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_whenControllerIsNull_thenThrowException() {
 exportHook.copyFromFile("test", "test.txt", "testDestination");
 }
}