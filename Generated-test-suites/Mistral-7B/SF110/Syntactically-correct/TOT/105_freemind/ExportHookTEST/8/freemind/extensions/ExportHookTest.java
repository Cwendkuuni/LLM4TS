package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import freemind.extensions.ExportHook;
import freemind.extensions.ModeController;
import freemind.extensions.ModeControllerHookAdapter;
import freemind.extensions.ResourceLoader;
import freemind.main.Controller;
import freemind.main.Logger;
import freemind.main.Resources;
import freemind.modes.MapController;
import freemind.view.MindMapView;
import freemind.view.mindmapview.PrintView;

public class ExportHookTest {

	private ExportHook exportHook;
	private ModeController modeControllerMock;
	private Controller controllerMock;
	private MapController mapControllerMock;
	private MindMapView viewMock;
	private PrintView printViewMock;
	private Logger loggerMock;
	private Resources resourcesMock;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		modeControllerMock = mock(ModeController.class);
		controllerMock = mock(Controller.class);
		mapControllerMock = mock(MapController.class);
		viewMock = mock(MindMapView.class);
		printViewMock = mock(PrintView.class);
		loggerMock = mock(Logger.class);
		resourcesMock = mock(Resources.class);

		when(controllerMock.getFrame()).thenReturn(new Object() {
			Container contentPane = mock(Container.class);
			FileChooserDialog fileChooserMock = mock(FileChooserDialog.class);

			File getMap() {
				return new File("path/to/map.mm");
			}
		});

		when(modeControllerMock.getController()).thenReturn(controllerMock);
		when(modeControllerMock.getText(anyString())).then(invocation -> invocation.getArgumentAt(0, String.class));
		when(modeControllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooserMock);
		when(controllerMock.getView()).thenReturn(viewMock);
		when(viewMock.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
		when(viewMock.createImage(anyInt(), anyInt())).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
		when(viewMock.print(any(Graphics.class))).then(invocation -> {});
		when(viewMock.endPrinting()).then(invocation -> {});
		when(controllerMock.getLogger()).thenReturn(loggerMock);
		when(controllerMock.getResources()).thenReturn(resourcesMock);

		exportHook = new ExportHook();
		exportHook.setController(modeControllerMock);
	}

	@Test
	public void testChooseFile() {
		String type = "jpeg";
		String description = "JPEG Image";
		File expectedFile = new File("path/to/output.jpeg");

		when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
		when(fileChooserMock.getSelectedFile()).thenReturn(expectedFile);

		File actualFile = exportHook.chooseFile(type, description, null);

		assertEquals(expectedFile, actualFile);
	}

	@Test
	public void testChooseFile_FileAlreadyExists() {
		String type = "png";
		String description = "PNG Image";
		String overwriteText = "The file already exists, do you want to replace it?";
		String overwriteResponse = JOptionPane.YES_OPTION.toString();

		when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
		when(fileChooserMock.getSelectedFile()).thenReturn(new File("path/to/output.png"));
		when(controllerMock.getText(anyString())).thenReturn(overwriteText);
		when(JOptionPane.showConfirmDialog(any(Container.class), anyString(), anyString(), 0)).thenReturn(Integer.parseInt(overwriteResponse));

		File actualFile = exportHook.chooseFile(type, description, null);

		assertEquals(new File("path/to/output.png"), actualFile);
	}

	@Test
	public void testChooseFile_InvalidFileType() {
		String type = "txt";
		String description = "Text File";
		File expectedFile = new File("path/to/output.txt");

		when(fileChooserMock.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
		when(fileChooserMock.getSelectedFile()).thenReturn(expectedFile);
		when(Tools.getExtension(expectedFile.getName())).thenReturn("invalid");

		File actualFile = exportHook.chooseFile(type, description, null);

		assertEquals(null, actualFile);
	}

	@Test
	public void testCreateBufferedImage() {
		BufferedImage expectedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

		BufferedImage actualImage = exportHook.createBufferedImage();

		assertEquals(expectedImage.getWidth(), actualImage.getWidth());
		assertEquals(expectedImage.getHeight(), actualImage.getHeight());
		assertArrayEquals(expectedImage.getRGB(0, 0), actualImage.getRGB(0, 0));
	}
}