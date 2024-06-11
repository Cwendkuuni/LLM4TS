import static org.junit.Assert.*;
import static freemind.extensions.ExportHook.*;
import javax.swing.JFileChooser;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ExportHookTest {

    @org.junit.Test
    public void testChooseFile() {
        // Given: Create mock ModeController and set up expectations for getFileChooser and getMap methods
        ModeController controllerMock = Mockito.mock(ModeController.class);
        JFileChooser fileChooserMock = Mockito.mock(JFileChooser.class);
        File mmFile = new File("test.mm");
        when(controllerMock.getFileChooser(any(ImageFilter.class))).thenReturn(fileChooserMock);
        when(controllerMock.getMap()).thenReturn(mmFile);

        // When: Call the chooseFile method with valid arguments
        File chosenFile = new ExportHook().chooseFile("image/png", "Save PNG Image", "png");

        // Then: Check if the file chooser dialog was displayed and a valid file was selected
        verify(fileChooserMock, times(1)).showSaveDialog(controllerMock.getFrame().getContentPane());
        assertEquals("expectedSaveDirectory/test.png", chosenFile.getAbsolutePath());

        // When: Call the chooseFile method with null arguments
        File nullChosenFile = new ExportHook().chooseFile(null, null, null);

        // Then: Check if the file dialog was not displayed and null was returned
        verify(fileChooserMock, never()).showSaveDialog(controllerMock.getFrame().getContentPane());
        assertNull(nullChosenFile);

        // When: Call the chooseFile method with an invalid file type
        when(controllerMock.getMap()).thenReturn(null);
        File invalidChosenFile = new ExportHook().chooseFile("image/jpg", "Save JPG Image", "jpg");

        // Then: Check if the file dialog was displayed and null was returned
        verify(fileChooserMock, times(1)).showSaveDialog(controllerMock.getFrame().getContentPane());
        assertNull(invalidChosenFile);

        // Clean up: Reset mocks
        reset(controllerMock, fileChooserMock);
    }
}