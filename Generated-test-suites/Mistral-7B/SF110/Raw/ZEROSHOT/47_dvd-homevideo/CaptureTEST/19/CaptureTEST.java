import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

    private Capture capture;
    private GUI guiMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        guiMock = Mockito.mock(GUI.class);
        capture = new Capture(1, 2, guiMock);
    }

    @Test
    public void testInit_createsCaptureObjectWithDefaults() {
        Capture capture = new Capture();
        assertFalse(capture.init());
        assertNull(capture.m_Thread);
        assertNull(capture.m_Error);
    }

    @Test
    public void testInit_createsCaptureObjectWithValidArguments() {
        Mockito.when(guiMock.menuChkThread.isSelected()).thenReturn(false);

        Capture capture = new Capture(1, 2, guiMock);
        assertTrue(capture.init());
        assertNotNull(capture.m_Thread);
        assertFalse(capture.m_Error);
    }

    @Test(expected = NullPointerException.class)
    public void testInit_throwsNullPointerExceptionWhenGuiIsNull() {
        Capture capture = new Capture(1, 2, null);
    }

    @Test
    public void testRun_startsCaptureThread() {
        Mockito.when(guiMock.menuChkThread.isSelected()).thenReturn(false);

        capture.run();
        assertNotNull(capture.m_Thread);
        assertTrue(capture.m_Thread.isAlive());
    }

    @Test
    public void testDV_Capture_createsOutputDirectory() {
        File outputDir = new File("output");
        Mockito.when(guiMock.strOutputDir).thenReturn(outputDir.getAbsolutePath());

        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_deletesExistingDirectory() {
        File outputDir = new File("output");
        outputDir.mkdirs();

        Mockito.when(guiMock.MessageBox).thenReturn(0);
        capture.DV_Capture();
        assertFalse(outputDir.exists());
    }

    @Test
    public void testDV_Capture_deletesExistingFile() {
        File outputDir = new File("output");
        File outputFile = new File(outputDir, "existing_file.txt");
        outputFile.createNewFile();

        Mockito.when(guiMock.MessageBox).thenReturn(0);
        capture.DV_Capture();
        assertFalse(outputFile.exists());
    }

    @Test
    public void testDV_Capture_doesNotDeleteNonExistingDirectory() {
        File outputDir = new File("output");
        Mockito.when(guiMock.MessageBox).thenReturn(JOptionPane.NO_OPTION);

        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
    }

    @Test
    public void testDV_Capture_doesNotDeleteNonExistingFile() {
        File outputDir = new File("output");
        File outputFile = new File(outputDir, "non_existing_file.txt");

        Mockito.when(guiMock.MessageBox).thenReturn(JOptionPane.NO_OPTION);

        capture.DV_Capture();
        assertFalse(outputFile.exists());
    }

    @Test
    public void testDV_Capture_showsMessageBoxWhenDirectoryCreationFails() {
        File outputDir = new File("output");
        Mockito.when(guiMock.MessageBox).thenReturn(JOptionPane.NO_OPTION);

        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
        Mockito.verify(guiMock, Mockito.times(1)).MessageBox("Please choose a different name for your title or move the directory to another location.", JOptionPane.PLAIN_MESSAGE);
    }

    @Test
    public void testDV_Capture_showsMessageBoxWhenFileDeletionFails() {
        File outputDir = new File("output");
        File outputFile = new File(outputDir, "existing_file.txt");
        outputFile.createNewFile();

        Mockito.when(guiMock.MessageBox).thenReturn(JOptionPane.NO_OPTION);

        capture.DV_Capture();
        assertTrue(outputFile.exists());
        Mockito.verify(guiMock, Mockito.times(1)).MessageBox("Please choose a different name for your title or move the file to another location.", JOptionPane.PLAIN_MESSAGE);
    }
}