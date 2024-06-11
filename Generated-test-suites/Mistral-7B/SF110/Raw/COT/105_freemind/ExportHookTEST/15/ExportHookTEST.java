package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileDialog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.Resources;
import freemind.extensions.logger.Logger;
import freemind.main.FreeMind;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private ModeController modeController;
 @Mock
 private Logger logger;
 @Mock
 private MapView mapView;
 @Mock
 private MapController mapController;
 @Mock
 private Resources resources;
 @Mock
 private FreeMind freeMind;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 exportHook.setController(modeController);
 exportHook.setLogger(logger);
 exportHook.setView(mapView);
 exportHook.setMapController(mapController);
 exportHook.setResources(resources);
 exportHook.setFreeMind(freeMind);
 }

 @After
 public void tearDown() {
 exportHook = null;
 }

 @Test
 public void testChooseFileNullType() {
 final String description = "description";
 final String nameExtension = "png";
 final File file = exportHook.chooseFile(null, description, nameExtension);
 assertNull(file);
 }

 @Test
 public void testChooseFileValidType() throws IOException {
 final String type = "png";
 final String description = "description";
 final String nameExtension = "png";
 final File expectedFile = new File("expectedPath/fileName.png");
 final FileDialog dialog = Mockito.mock(FileDialog.class);
 Mockito.when(modeController.getFileChooser(ArgumentMatchers.any(FileFilter.class)))
 .thenReturn(dialog);
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 Mockito.verify(dialog).setSelectedFile(expectedFile);
 assertEquals(expectedFile, chosenFile);
 }

 @Test
 public void testChooseFileInvalidExtension() throws IOException {
 final String type = "png";
 final String description = "description";
 final String nameExtension = "jpg";
 final File expectedFile = new File("expectedPath/fileName.png");
 final FileDialog dialog = Mockito.mock(FileDialog.class);
 Mockito.when(modeController.getFileChooser(ArgumentMatchers.any(FileFilter.class)))
 .thenReturn(dialog);
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 Mockito.verify(dialog).setSelectedFile(expectedFile);
 assertNotEquals(new File("expectedPath/fileName.jpg"), chosenFile);
 }

 @Test
 public void testChooseFileFileExists() throws IOException {
 final String type = "png";
 final String description = "description";
 final String nameExtension = "png";
 final File existingFile = new File("existingPath/fileName.png");
 final FileDialog dialog = Mockito.mock(FileDialog.class);
 Mockito.when(modeController.getFileChooser(ArgumentMatchers.any(FileFilter.class)))
 .thenReturn(dialog);
 final JTextArea textArea = Mockito.mock(JTextArea.class);
 Mockito.when(modeController.getText(ArgumentMatchers.anyString()))
 .thenReturn(textArea);
 final int overwriteMap = JOptionPane.showConfirmDialog(ArgumentMatchers.any(Component.class),
 ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), 0);
 Mockito.when(textArea.getText())
 .thenReturn("File already exists. Do you want to overwrite it?");
 Mockito.when(JOptionPane.showConfirmDialog(ArgumentMatchers.any(Component.class),
 ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), 0))
 .thenReturn(JOptionPane.YES_OPTION);
 final File chosenFile = exportHook.chooseFile(type, description, nameExtension);
 Mockito.verify(dialog).setSelectedFile(existingFile);
 assertEquals(existingFile, chosenFile);
 }

 @Test
 public void testGetTranslatableResourceStringNullResourceName() {
 final String returnValue = exportHook.getTranslatableResourceString(null);
 assertNull(returnValue);
 }

 @Test
 public void testGetTranslatableResourceStringValidResourceName() {
 final String resourceName = "resourceName";
 final String returnValue = exportHook.getTranslatableResourceString(resourceName);
 assertNotNull(returnValue);
 final String expectedValue = "expectedValue";
 Mockito.when(modeController.getText(resourceName))
 .thenReturn(expectedValue);
 assertEquals(expectedValue, returnValue);
 }

 @Test
 public void testCreateBufferedImageNullView() {
 final BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNull(bufferedImage);
 }

 @Test
 public void testCreateBufferedImageValidView() throws Exception {
 final BufferedImage expectedBufferedImage = ImageIO.read(new File("expectedPath/image.png"));
 final Graphics2D graphics = Mockito.mock(Graphics2D.class);
 final Printable printable = Mockito.mock(Printable.class);
 final Image image = Mockito.mock(Image.class);
 Mockito.when(mapView.createImage(MapView.WIDTH, MapView.HEIGHT))
 .thenReturn(image);
 Mockito.when(mapView.preparePrinting())
 .thenReturn(true);
 Mockito.when(mapView.getInnerBounds())
 .thenReturn(new Rectangle(0, 0, MapView.WIDTH, MapView.HEIGHT));
 Mockito.when(mapView.print(ArgumentMatchers.any(Graphics2D.class)))
 .thenReturn(printable);
 Mockito.when(mapView.endPrinting())
 .thenReturn(true);
 Mockito.when(image.getGraphics())
 .thenReturn(graphics);
 final BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertEquals(expectedBufferedImage, bufferedImage);
 }

 @Test
 public void testCopyFromResourceNullPrefix() {
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 exportHook.copyFromResource(null, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromResourceValidPrefixAndFileName() throws Exception {
 final String prefix = "prefix";
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 final URL resource = Mockito.mock(URL.class);
 final InputStream inputStream = Mockito.mock(InputStream.class);
 Mockito.when(resource.openStream())
 .thenReturn(inputStream);
 final OutputStream outputStream = Mockito.mock(OutputStream.class);
 Mockito.when(getResource(prefix + fileName))
 .thenReturn(resource);
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 Mockito.verify(resource).openStream();
 Mockito.verify(outputStream).close();
 }

 @Test
 public void testCopyFromFileNullDir() {
 final String dir = null;
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 }

 @Test
 public void testCopyFromFileValidDirAndFileName() throws Exception {
 final String dir = "sourceDir";
 final String fileName = "fileName";
 final String destinationDirectory = "destinationDirectory";
 final File file = new File(dir + "/" + fileName);
 final InputStream inputStream = Mockito.mock(InputStream.class);
 final OutputStream outputStream = Mockito.mock(OutputStream.class);
 Mockito.when(new FileInputStream(file))
 .thenReturn(inputStream);
 final File destinationFile = new File(destinationDirectory + "/" + fileName);
 Mockito.when(new FileOutputStream(destinationFile))
 .thenReturn(outputStream);
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 Mockito.verify(file).exists();
 Mockito.verify(inputStream).close();
 Mockito.verify(outputStream).close();
 assertEquals(destinationFile, new File(destinationDirectory + "/" + fileName));
 }
}