import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConvertTest {
    private Convert convert;
    private GUI guiMock;
    private Thread threadMock;

    @Before
    public void setUp() {
        guiMock = Mockito.mock(GUI.class);
        threadMock = Mockito.mock(Thread.class);
        convert = new Convert("dvd-ntsc", "4:3", guiMock);
    }

    @Test
    public void testConvertConstructorWithFormat() {
        Convert convert = new Convert("dvd-ntsc", "4:3", guiMock);
        assertNotNull(convert.m_Format);
        assertEquals("dvd-ntsc", convert.m_Format);
    }

    @Test
    public void testConvertConstructorWithFormatAndQuality() {
        Convert convert = new Convert(0, "dvd-ntsc", "4:3", guiMock);
        assertNotNull(convert.m_Quality);
        assertEquals(0, convert.m_Quality);

        Convert convert1 = new Convert(1, "dvd-ntsc", "4:3", guiMock);
        assertNotNull(convert1.m_Quality);
        assertEquals(1, convert1.m_Quality);

        Convert convert2 = new Convert(2, "dvd-ntsc", "4:3", guiMock);
        assertNotNull(convert2.m_Quality);
        assertEquals(2, convert2.m_Quality);
    }

    @Test
    public void testInitMethod() throws InterruptedException {
        when(guiMock.menuChkThread.isSelected()).thenReturn(true);

        convert.init();
        verify(threadMock).start();
        verify(threadMock).join();

        reset(guiMock);
        when(guiMock.ErrorCheck(Mockito.anyString())).thenReturn(false);
        when(guiMock.menuChkThread.isSelected()).thenReturn(false);

        convert.init();
        verify(convert).Transcode();
    }

    @Test
    public void testTranscodeMethodWithValidArguments() throws IOException, InterruptedException {
        final File listDirMock = Mockito.mock(File.class);
        when(listDirMock.list()).thenReturn(new String[]{"file1.dv", "file2.dv"});

        when(guiMock.strOutputDir).thenReturn(new File("output_dir").getAbsolutePath());

        convert.video_files = new String[]{"file1.dv", "file2.dv"};

        convert.Transcode();

        verify(convert, times(2)).replaceAll(Mockito.eq(this.video_files[Mockito.anyInt()]), Mockito.anyString());
        verify(convert, times(2)).replaceAll(Mockito.eq(this.video_files[Mockito.anyInt()].substring(0, 11)), Mockito.anyString());
        verify(convert, times(2)).replaceAll(Mockito.eq("inp"), Mockito.eq(this.video_files[Mockito.anyInt()].substring(0, 11)));

        File logTranscodeFile = new File(convert.m_GUI.strOutputDir + "/log/transcode.log");
        File logMplexFile = new File(convert.m_GUI.strOutputDir + "/log/mplex.log");

        assertTrue(logTranscodeFile.exists());
        assertTrue(logMplexFile.exists());

        String logTranscodeContent = new String(Files.readAllBytes(logTranscodeFile.toPath()));
        String logMplexContent = new String(Files.readAllBytes(logMplexFile.toPath()));

        List<String> transcodeLines = Arrays.asList(logTranscodeContent.split("\n"));
        List<String> mplexLines = Arrays.asList(logMplexContent.split("\n"));

        assertTrue(transcodeLines.contains("encoding"));
        assertTrue(transcodeLines.contains("encoding: video:"));
        assertTrue(transcodeLines.contains("encoding: audio:"));
        assertTrue(transcodeLines.contains("encoding: video: [video @ 0x168b4b0]"));
        assertFalse(transcodeLines.contains("transcode: command not found"));

        assertTrue(mplexLines.contains("Muxing information"));
        assertTrue(mplexLines.contains("Muxing video: dv_video.dvb"));
        assertTrue(mplexLines.contains("Muxing audio: dv_audio.dvb"));
        assertFalse(mplexLines.contains("mplex: command not found"));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeMethodWithInvalidArguments() throws IOException, InterruptedException {
        final File listDirMock = Mockito.mock(File.class);
        when(listDirMock.list()).thenReturn(new String[]{"file1.dv", "file2.dv"});

        when(guiMock.strOutputDir).thenReturn(new File("output_dir").getAbsolutePath());

        convert.video_files = new String[]{"file1.dv", "file2.dv"};

        convert.thread_track = listDirMock.list().length + 1;
        convert.Transcode();

        verify(guiMock).MessageBox(Mockito.anyString(), Mockito.eq(0));
    }

    @Test
    public void testMatrixMethod() throws IOException {
        File matrixFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");

        convert.matrix();

        assertTrue(matrixFile.exists());
        File matrixContentFile = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        String matrixContent = new String(Files.readAllBytes(matrixContentFile.toPath()));

        List<String> matrixLines = Arrays.asList(matrixContent.split("\n"));

        assertTrue(matrixLines.get(0).startsWith("# High resolution INTRA table"));
        assertTrue(matrixLines.get(1).startsWith("8,"));
        assertTrue(matrixLines.get(1).endsWith("38"));
        assertTrue(matrixLines.get(1).split(",").length == 8);
    }
}