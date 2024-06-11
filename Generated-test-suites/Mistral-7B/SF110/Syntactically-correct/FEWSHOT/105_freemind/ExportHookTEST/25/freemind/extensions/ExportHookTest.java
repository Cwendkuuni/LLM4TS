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
import org.mockito.Mock;

public class ExportHookTest {

ModeController mockController;
MapView mockView;

@Before
public void setUp() {
mockController = mock(ModeController.class);
mockView = mock(MapView.class);
}

@Test
public void testChooseFile() {
String type = "png";
String description = "Export Image as PNG";
String nameExtension = null;
File expectedFile = new File("path/to/exported/file.png");

when(mockController.getFrame()).thenReturn(new JFrame());
when(mockController.getFileChooser(any(ImageFilter.class))).thenReturn(mock(JFileChooser.class));
when(mockController.getMap()).thenReturn(mock(AbstractMapModel.class));
when(mockController.getText(anyString())).then(i -> i.getValue()[0]);

File result = new ExportHook().chooseFile(type, description, nameExtension, mockController);

assertEquals(expectedFile, result);
verify(mockController, times(1)).getFrame();
verify(mockController, times(1)).getFileChooser(any(ImageFilter.class));
verify(mockController, times(1)).getMap();
verify(mockController, times(1)).getText(eq("file_already_exists"));
verifyNoMoreInteractions(mockController);

JFileChooser fileChooser = mock(JFileChooser.class);
when(fileChooser.showSaveDialog(eq(mock(Container.class)))).thenReturn(JFileChooser.APPROVE_OPTION);
when(fileChooser.getSelectedFile()).thenReturn(expectedFile);

result = new ExportHook().chooseFile(type, description, nameExtension, mockController);

assertEquals(expectedFile, result);
verify(fileChooser, times(1)).showSaveDialog(eq(mock(Container.class)));
}

@Test
public void testChooseFile_WhenCancelled() {
File result = new ExportHook().chooseFile("png", "Export Image as PNG", null, mockController);

assertNull(result);
}

@Test
public void testCreateBufferedImage() {
BufferedImage expectedImage = ImageIO.read(new File("path/to/image.jpg"));

when(mockController.getView()).thenReturn(mockView);
when(mockView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 10, 10));
when(mockView.createImage(anyInt(), anyInt())).thenAnswer(i -> null);
when(mockView.createImage(anyInt(), anyInt())).thenAnswer(i -> new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB));

BufferedImage result = new ExportHook().createBufferedImage();

assertEquals(expectedImage, result);
}

@Test
public void testCreateBufferedImage_WhenViewIsNull() {
BufferedImage result = new ExportHook().createBufferedImage();

assertNull(result);
}

@Test
public void testGetTranslatableResourceString() {
String resourceName = "%resourceKey%";
String expectedValue = "This is a translated value";

when(mockController.getResourceString(resourceName)).thenReturn("This is a resource key");

String result = new ExportHook().getTranslatableResourceString(resourceName);

assertEquals(expectedValue, result);
}

@Test
public void testGetTranslatableResourceString_WhenResourceKeyIsNull() {
String result = new ExportHook().getTranslatableResourceString(null);

assertNull(result);
}
}