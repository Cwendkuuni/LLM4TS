import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest
{
    @Test
    public void testConvertConstructorDvdNtsc()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        assertEquals("dvd-ntsc", convert.getFormat());
        assertEquals(29.97, convert.getFps(), 0.01);
    }

    @Test
    public void testConvertConstructorDvdPAL()
    {
        Convert convert = new Convert("dvd-pal", "16:9");
        assertEquals("dvd-pal", convert.getFormat());
        assertEquals(25.0, convert.getFps(), 0.01);
    }

    @Test
    public void testConvertConstructorWithQuality()
    {
        Convert convert = new Convert(1, "dvd-ntsc", "4:3", new GUI());
        assertEquals(1, convert.getQuality());
    }

    @Test
    public void testInitWithThreadCheckboxUnchecked()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        assertFalse(convert.init());
        assertTrue(convert.isError());
    }

    @Test
    public void testInitWithThreadCheckboxChecked()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.setThread(new Thread(convert));
        convert.getThread().start();
        try {
            convert.getThread().join();
        }
        catch (InterruptedException e) {
            fail("Thread should not have been interrupted.");
        }
        assertFalse(convert.isError());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidList()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File listDir = new File("non-existent-directory");
        String[] videoFiles = listDir.list();
        convert.video_files = videoFiles;
        convert.transcode();
    }

    @Test
    public void testTranscodeWithValidFiles()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File listDir = new File("valid-directory");
        String[] videoFiles = listDir.list();
        convert.video_files = Arrays.copyOfRange(videoFiles, 0, 5);
        convert.transcode();
        assertTrue(convert.isPrgConvertEnabled());
        assertTrue(convert.isLblConvertEnabled());
        assertTrue(convert.isLblConvertProgEnabled());
    }

    @Test
    public void testMatrixFileCreation()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        File matrixFile = new File(convert.getStrOutputDir() + "/matrix.txt");
        assertTrue(matrixFile.exists());
    }

    @Test(expected = IOException.class)
    public void testMatrixFileCreationFailure()
    {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        try {
            convert.matrix();
        }
        catch (IOException e) {
            // Expected
        }
        File matrixFile = new File(convert.getStrOutputDir() + "/matrix.txt");
        assertFalse(matrixFile.exists());
    }
}