import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CaptureTest {

    private Capture capture;
    private GUI guiMock;

    @Before
    public void setup() {
        this.guiMock = Mockito.mock(GUI.class);
        this.capture = new Capture();
        this.capture.m_GUI = this.guiMock;
    }

    @Test
    public void testInit_NoThreadSelected() {
        Mockito.when(this.guiMock.menuChkThread.isSelected()).thenReturn(false);
        assertFalse(this.capture.init());
    }

    @Test
    public void testInit_ThreadSelected() {
        Mockito.when(this.guiMock.menuChkThread.isSelected()).thenReturn(true);
        Mockito.doReturn(this.capture).when(this.capture).new Thread(this.capture);
        Mockito.doNothing().when(this.capture).DV_Capture();

        this.capture.init();

        Mockito.verify(this.capture).m_Thread.start();
    }

    @Test(expected = NullPointerException.class)
    public void testDV_Capture_OutputDirNull() {
        this.capture = new Capture();
        this.capture.m_GUI = null;
        this.capture.init();
        this.capture.DV_Capture();
    }

    @Test
    public void testDV_Capture_OutputDirExistsAndIsDirectory() throws IOException, InterruptedException {
        Mockito.when(this.guiMock.strOutputDir).thenReturn(new File("output_dir").getAbsolutePath());
        File outputDir = new File("output_dir");
        outputDir.mkdirs();

        this.capture = new Capture(1, 2, this.guiMock);
        this.capture.init();
        this.capture.DV_Capture();

        Mockito.verify(this.guiMock).MessageBox(ArgumentsMatchers.is("The Output Directory output_dir already exists!\\nWould you like to delete it?"), ArgumentsMatchers.eq(0));
        Mockito.verifyNoMoreInteractions(this.guiMock);

        outputDir.deleteRecursively();
    }

    @Test
    public void testDV_Capture_OutputDirExistsButIsNotADirectory() throws IOException, InterruptedException {
        Mockito.when(this.guiMock.strOutputDir).thenReturn(new File("output_dir/file.txt").getAbsolutePath());

        this.capture = new Capture(1, 2, this.guiMock);
        this.capture.init();
        this.capture.DV_Capture();

        Mockito.verify(this.guiMock).MessageBox(ArgumentsMatchers.is("Please choose a different name for your title or move the file to another location."), ArgumentsMatchers.eq(2));
    }

    @Test
    public void testDV_Capture_DvgrabCommandNotInPath() throws IOException, InterruptedException {
        Mockito.when(this.guiMock.strOutputDir).thenReturn(new File("output_dir").getAbsolutePath());
        File outputDir = new File("output_dir");
        outputDir.mkdirs();

        this.capture = new Capture(1, 2, this.guiMock);
        this.capture.init();
        this.capture.DV_Capture();

        Mockito.verify(this.guiMock).MessageBox(ArgumentsMatchers.is("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo."), ArgumentsMatchers.eq(0));
        Mockito.verifyNoMoreInteractions(this.guiMock);

        outputDir.deleteRecursively();
    }

    @Test
    public void testDV_Capture_CaptureThreadInterrupted() throws IOException, InterruptedException {
        Mockito.when(this.guiMock.strOutputDir).thenReturn(new File("output_dir").getAbsolutePath());
        File outputDir = new File("output_dir");
        outputDir.mkdirs();

        this.capture = new Capture(1, 2, this.guiMock);
        this.capture.init();

        Thread interruptThread = new Thread(() -> {
            try {
                Thread.sleep(100);
                this.capture.m_Thread.interrupt();
            } catch (InterruptedException e) {
            }
        });
        interruptThread.start();

        this.capture.DV_Capture();

        Mockito.verify(this.guiMock).MessageBox(ArgumentsMatchers.is("dvd-homevideo thread was interrupted"), ArgumentsMatchers.eq(0));
        Mockito.verifyNoMoreInteractions(this.guiMock);

        outputDir.deleteRecursively();
        interruptThread.join();
    }
}