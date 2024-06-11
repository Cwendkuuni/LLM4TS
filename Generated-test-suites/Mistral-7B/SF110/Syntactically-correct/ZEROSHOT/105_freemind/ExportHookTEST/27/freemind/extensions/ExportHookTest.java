package freemind.extensions;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.controller.Controller;
import freemind.controller.FrameController;
import freemind.controller.ModeController;
import freemind.controller.View;
import freemind.ext.resources.Logger;
import freemind.main.FreeMind;
import freemind.main.Resources;
import freemind.modes.MapController;
import freemind.view.mindmapview.MindMapView;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private Controller controller;
 @Mock
 private FrameController frameController;
 @Mock
 private MapController mapController;
 @Mock
 private MindMapView view;
 @Mock
 private Logger logger;
 @Mock
 private Resources resources;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 exportHook.setLogger(logger);
 exportHook.setController(controller);
 Mockito.when(controller.getText(ArgumentMatchers.anyString())).then(invocation -> invocation.getArgument(0));
 Mockito.when(controller.getFrame()).thenReturn(frameController);
 Mockito.when(frameController.getContentPane()).thenReturn(new Container());
 Mockito.when(controller.getView()).thenReturn(view);
 Mockito.when(view.createImage(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenCallRealMethod();
 Mockito.when(controller.getFileChooser(ArgumentMatchers.isInstance(ImageFilter.class))).thenReturn(Mockito.mock(JFileChooser.class));
 Mockito.when(controller.getMap()).thenReturn(mapController);
 Mockito.when(mapController.getFile()).thenReturn(new File("test.mm"));
 }

 @After
 public void tearDown() {
 exportHook = null;
 }

 @Test
 public void testChooseFile() {
 final String type = "png";
 final String description = "PNG Image";
 final File expectedFile = new File("test.png");
 final File chosenFile = exportHook.chooseFile(type, description, null);
 assertTrue(chosenFile != null);
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFile_NoFileSelected() {
 final String type = "png";
 final String description = "PNG Image";
 final File chosenFile = exportHook.chooseFile(type, description, null);
 assertNull(chosenFile);
 }

 @Test
 public void testCreateBufferedImage() {
 final BufferedImage expectedImage = Mockito.mock(BufferedImage.class);
 Mockito.when(view.createImage(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(expectedImage);
 final BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testCreateBufferedImage_ViewIsNull() {
 final BufferedImage expectedImage = null;
 Mockito.when(view.createImage(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(null);
 final BufferedImage actualImage = exportHook.createBufferedImage();
 assertEquals(expectedImage, actualImage);
 }

 @Test
 public void testGetTranslatableResourceString() {
 final String resourceName = "%resource.title";
 final String expectedValue = "Resource Title";
 final String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetTranslatableResourceString_NullResourceString() {
 final String resourceName = null;
 final String actualValue = exportHook.getTranslatableResourceString(resourceName);
 assertNull(actualValue);
 }

 @Test
 public void testCopyFromResource() {
 final String prefix = "freemind.extensions";
 final String fileName = "export.xml";
 final String destinationDirectory = "test";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 Mockito.verify(logger, Mockito.times(1)).finest("searching for " + prefix + fileName);
 Mockito.verify(logger, Mockito.times(1)).finest("searching for " + prefix + fileName + " and should go to " + destinationDirectory);
 }

 @Test
 public void testCopyFromResource_ResourceNotFound() {
 final String prefix = "freemind.extensions";
 final String fileName = "export.xml";
 final String destinationDirectory = "test";exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 Mockito.verify(logger, Mockito.times(1)).severe("Cannot find resource: " + prefix + fileName);
 }

 @Test
 public void testCopyFromFile() {
 final String dir = "test";
 final String fileName = "export.xml";
 final String destinationDirectory = "test";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 Mockito.verify(logger, Mockito.times(1)).finest("searching for " + dir + fileName);
 }

 @Test
 public void testCopyFromFile_FileNotFound() {
 final String dir = "test";
 final String fileName = "export.xml";
 final String destinationDirectory = "test";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 Mockito.verify(logger, Mockito.times(1)).severe("File not found or could not be copied. Was earching for " + dir + fileName + " and should go to " + destinationDirectory);
 }
}