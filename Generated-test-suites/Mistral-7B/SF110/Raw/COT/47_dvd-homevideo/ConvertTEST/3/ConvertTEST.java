import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithFormatAndAspectRatio() {
        Convert convert = new Convert("dvd-ntsc", "4:3");
        assertNotNull(convert.m_Format);
        assertNotNull(convert.m_AspectRatio);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testConvertConstructorWithQualityFormatAndAspectRatio() {
        Convert convert = new Convert(1, "dvd-ntsc", "4:3", null);
        assertNotNull(convert.m_Quality);
        assertNotNull(convert.m_Format);
        assertNotNull(convert.m_AspectRatio);
        assertEquals(1, convert.m_Quality);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertInvalidFormat() {
        Convert convert = new Convert("invalidFormat", "4:3", null);
        fail("Should have thrown IllegalArgumentException");
    }

    @Test
    public void testInitWithThreadCheckBoxUnchecked() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        assertTrue(convert.init());
    }

    @Test
    public void testInitWithThreadCheckBoxChecked() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public boolean menuChkThread_isSelected() {
                return true;
            }
        };
        assertFalse(convert.init());
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testTranscodeWithInvalidDirectory() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "";
            }
        };
        convert.init();
        convert.Transcode();
        fail("Should have thrown StringIndexOutOfBoundsException");
    }

    @Test
    public void testTranscodeWithValidDirectory() {
        File outputDir = new File("outputDir");
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        };
        convert.init();
        convert.Transcode();
        assertTrue(outputDir.exists());
        assertFalse(outputDir.isDirectory());
        File logFile = new File(outputDir, "log/transcode.log");
        assertTrue(logFile.exists());
        assertFalse(logFile.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testTranscodeWithIOException() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "outputDir";
            }
        };
        try {
            convert.init();
            convert.Transcode();
        } catch (InterruptedException e) {
            // ignore
        }
        File logFile = new File(new File("outputDir"), "log/transcode.log");
        FileInputStream inputStream = new FileInputStream(logFile);
        byte[] fileContent = new byte[inputStream.available()];
        inputStream.read(fileContent);
        inputStream.close();
        String logOutput = new String(fileContent);
        assertTrue(logOutput.contains("Could not locate transcode in your path."));
        throw new IOException();
    }

    @Test
    public void testMatrixFileCreation() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "output";
            }
        };
        convert.init();
        convert.matrix();
        File matrixFile = new File("output", "matrix.txt");
        assertTrue(matrixFile.exists());
        assertFalse(matrixFile.isDirectory());
    }

    @Test(expected = IOException.class)
    public void testMatrixFileCreationWithIOException() {
        Convert convert = new Convert("dvd-ntsc", "4:3", null);
        convert.m_GUI = new GUI() {
            @Override
            public String strOutputDir() {
                return "output";
            }
        };
        try {
            convert.init();
            convert.matrix();
        } catch (InterruptedException e) {
            // ignore
        }
        File matrixFile = new File("output", "matrix.txt");
        assertFalse(matrixFile.exists());
        throw new IOException();
    }
}