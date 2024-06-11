import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.modes.MapController;
import freemind.view.mindmapview.MapView;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ExportHookTest {

 private ExportHook exportHook;
 private ModeController controllerMock;
 private MapView viewMock;
 private MapController mapControllerMock;

 @Before
 public void setup() {
 controllerMock = mock(ModeController.class);
 viewMock = spy(new MapView());
 mapControllerMock = mock(MapController.class);
 when(controllerMock.getFrame()).thenReturn(new JFrame());
 when(controllerMock.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(new FreeMindFileDialog(controllerMock.getFrame(), mapControllerMock, "saveImageAsFile", null));
 when(controllerMock.getText(Mockito.anyString())).thenReturn("Test Text");
 when(controllerMock.getMap()).thenReturn(mapControllerMock);
 exportHook = new ExportHook();
 exportHook.setController(controllerMock);
 }

 @Test
 public void testChooseFile() throws IOException {
 final String type = "png";
 final String description = "Test Description";
 final String fileName = "test.png";
 final String destinationDirectory = "test_directory";
 final File expectedChosenFile = new File(destinationDirectory + "/test.png");
 ArgumentCaptor<FileFilter> fileFilterCaptor = ArgumentCaptor.forClass(FileFilter.class);

 exportHook.chooseImageFile(type, "Test Description", null, controllerMock);

 verify(controllerMock.getFileChooser(fileFilterCaptor.capture()))
 .showSaveDialog(Mockito.eq(controllerMock.getFrame().getContentPane()), Mockito.eq(mapControllerMock), Mockito.eq("saveImageAsFile"), Mockito.eq(expectedChosenFile));

 FileFilter fileFilter = fileFilterCaptor.getValue();
 fileFilter.accept(expectedChosenFile);

 verify(controllerMock).getText("Test Text");

 BufferedImage image = exportHook.createBufferedImage();
 File chosenFile = exportHook.chooseImageFile(type, "Test Description", null, controllerMock);

 verify(controllerMock).getText("file_already_exists");
 JOptionPane.showConfirmDialog(controllerMock.getFrame().getContentPane(), Mockito.anyString(), Mockito.anyString(), 0);

 verify(controllerMock.getFileChooser(fileFilter)).setSelectedFile(expectedChosenFile);
 verify(controllerMock.getFileChooser(fileFilter)).showSaveDialog(Mockito.eq(controllerMock.getFrame().getContentPane()), Mockito.eq(mapControllerMock)) == 0;

 final File chosenFileWithExtension = new File(expectedChosenFile.getAbsolutePath() + "." + type);
 verify(controllerMock).getFileChooser(fileFilter).setSelectedFile(chosenFileWithExtension);
 verify(controllerMock.getFileChooser(fileFilter)).showSaveDialog(Mockito.eq(controllerMock.getFrame().getContentPane()), Mockito.eq(mapControllerMock)) == 0;

 exportHook.copyFromResource("prefix", "fileName.extension", destinationDirectory);
 File resourceFile = new File("test_resource_directory/fileName.extension");
 Mockito.verify(controllerMock).getResource(Mockito.eq("prefix/fileName.extension"));
 Mockito.verify(controllerMock).getResource(Mockito.eq("prefix/fileName.extension")).openStream();
 Mockito.verify(controllerMock).getFileOutputStream(Mockito.eq(destinationDirectory + "/" + "fileName.extension"));
 Mockito.verify(controllerMock).copyStream(Mockito.anyInputStream(), Mockito.anyOutputStream(), Mockito.eq(true));
 }

 @Test
 public void testGetTranslatableResourceString() {
 String resourceName = "%s.description";
 String expectedResult = "Test Text";

 String result = exportHook.getTranslatableResourceString(resourceName);

 assertEquals(expectedResult, result);
 }

 @Test
 public void testCreateBufferedImage() {
 exportHook.createBufferedImage();
 verify(viewMock).preparePrinting();
 verify(viewMock).print(Mockito.anyGraphics());
 verify(viewMock).endPrinting();
 }

 @Test(expected = NullPointerException.class)
 public void testChooseFileWithNullController() {
 ExportHook exportHook = new ExportHook();
 exportHook.chooseImageFile("png", "Test Description", null, null);
 }

}