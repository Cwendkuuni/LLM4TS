package freemind.extensions.test;

import static org.junit.Assert.*;

import java.awt.Image;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MindMapView;

@RunWith(MockitoJUnitRunner.class)
public class ExportHookTest {

 private ExportHook exportHook;
 @Mock
 private ModeController modeController;
 @Mock
 private MapView mapView;
 @Mock
 private JFrame frame;

 @Before
 public void setUp() {
 exportHook = new ExportHook();
 exportHook.setController(modeController);
}

 @Test
 public void testChooseFile_ValidFile_ReturnsFile() {
 File chosenFile = exportHook.chooseFile("image/png", "Save as PNG", null);
 assertNotNull(chosenFile);
}

 @Test
 public void testChooseFile_InvalidFileType_ReturnsNull() {
 File chosenFile = exportHook.chooseFile("application/pdf", "Save as PDF", null);
 assertNull(chosenFile);
}

 @Test
 public void testChooseFile_Canceled_ReturnsNull() {
 JFileChooser chooser = Mockito.spy(new JFileChooser());
 Mockito.doReturn(JFileChooser.APPROVE_OPTION).when(modeController).getFileChooser(any(ImageFilter.class));
 Mockito.doReturn(chooser).when(exportHook).getController();
 SwingUtilities.invokeLater(() -> chooser.setSelectedFile(new File("test.png")));
 SwingUtilities.invokeLater(() -> chooser.showSaveDialog(frame));
 Mockito.doReturn(JFileChooser.CANCEL_OPTION).when(chooser).getDialogResult();

 File chosenFile = exportHook.chooseFile("image/png", "Save as PNG", null);
 assertNull(chosenFile);
}

 @Test
 public void testGetTranslatableResourceString_ValidString_ReturnsTranslatedString() {
 String resourceString = "%title%";
 String translatedString = "Title";
 Mockito.doReturn(translatedString).when(modeController).getText(resourceString.substring(1));

 String result = exportHook.getTranslatableResourceString(resourceString);
 assertEquals(translatedString, result);
}

 @Test
 public void testCreateBufferedImage_ValidView_ReturnsBufferedImage() throws Exception {
 Image image = ImageIO.read(new File("test.png"));
 Mockito.doReturn(image).when(mapView).createImage(anyInt(), anyInt());
 Mockito.doReturn(mapView).when(exportHook).getController().getView();

 BufferedImage bufferedImage = exportHook.createBufferedImage();
 assertNotNull(bufferedImage);
}

 @Test(expected = NullPointerException.class)
 public void testCreateBufferedImage_NullView() {
 Mockito.doReturn(null).when(exportHook).getController().getView();

 exportHook.createBufferedImage();
}

 @Test
 public void testCopyFromResource_ValidPrefixFileNameDestinationDirectory_CopiesFile() throws IOException {
 String prefix = "prefix";
 String fileName = "test.png";
 String destinationDirectory = "destination";
 exportHook.copyFromResource(prefix, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue(expectedFile.exists());
}

 @Test
 public void testCopyFromFile_ValidDirFileNameDestinationDirectory_CopiesFile() throws IOException {
 String dir = "source";
 String fileName = "test.png";
 String destinationDirectory = "destination";
 exportHook.copyFromFile(dir, fileName, destinationDirectory);
 File expectedFile = new File(destinationDirectory + "/" + fileName);
 assertTrue(expectedFile.exists());
}

 @Test(expected = NullPointerException.class)
 public void testCopyFromResource_NullPrefix() {
 exportHook.copyFromResource(null, "test.png", "destination");
}

 @Test(expected = NullPointerException.class)
 public void testCopyFromFile_NullDir() {
 exportHook.copyFromFile(null, "test.png", "destination");
}

 @Test(expected = IOException.class)
 public void testCopyFromResource_FileNotFound() throws IOException {
 exportHook.copyFromResource("prefix", "test.png", "destination");
}

 @Test(expected = IOException.class)
 public void testCopyFromFile_FileNotFound() throws IOException {
 exportHook.copyFromFile("source", "test.png", "destination");
}

}