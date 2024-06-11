package freemind.extensions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import freemind.modes.ModeController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FreeMindFileDialog;
import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {

    private ModeController modeController;
    private ExportHook exportHook;
    private MapView mapView;

    @Before
    public void setup() {
        modeController = mock(ModeController.class);
        mapView = mock(MapView.class);
        when(modeController.getView()).thenReturn(mapView);
        exportHook = new ExportHook() {
            @Override
            public ModeController getController() {
                return modeController;
            }
        };
    }

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG files";
        FileFilter filter = new ExportHook().new ImageFilter(type, description);
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(modeController.getFileChooser(filter)).thenReturn(chooser);

        File expectedFile = new File("expectedFilePath");
        when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(expectedFile);

        File actualFile = exportHook.chooseFile(type, description, null);
        assertEquals(expectedFile, actualFile);

        verify(chooser).setSelectedFile(expectedFile);
    }

    @Test
    public void testChooseFile_FileAlreadyExists() {
        String type = "png";
        String description = "PNG files";
        FileFilter filter = new ExportHook().new ImageFilter(type, description);
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(modeController.getFileChooser(filter)).thenReturn(chooser);

        File expectedFile = new File("expectedFilePath");
        when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.APPROVE_OPTION);
        when(chooser.getSelectedFile()).thenReturn(expectedFile);

        String overwriteText = "File already exists. Do you want to overwrite it?";
        when(modeController.getText("file_already_exists")).thenReturn(overwriteText);

        JOptionPane optionPane = mock(JOptionPane.class);
        when(modeController.showConfirmDialog(any(Component.class), eq(overwriteText), eq(overwriteText), 0)).thenReturn(JOptionPane.YES_OPTION);

        File actualFile = exportHook.chooseFile(type, description, null);
        assertEquals(expectedFile, actualFile);

        verify(optionPane).showConfirmDialog(any(Component.class), eq(overwriteText), eq(overwriteText), 0);
    }

    @Test
    public void testChooseFile_UserCancels() {
        String type = "png";
        String description = "PNG files";
        FileFilter filter = new ExportHook().new ImageFilter(type, description);
        FreeMindFileDialog chooser = mock(FreeMindFileDialog.class);
        when(modeController.getFileChooser(filter)).thenReturn(chooser);

        when(chooser.showSaveDialog(any(Container.class))).thenReturn(JFileChooser.CANCEL_OPTION);

        File actualFile = exportHook.chooseFile(type, description, null);
        assertNull(actualFile);
    }

    @Test
    public void testCreateBufferedImage() {
        when(modeController.getView()).thenReturn(mapView);

        BufferedImage actualImage = exportHook.createBufferedImage();
        assertNotNull(actualImage);
    }
}