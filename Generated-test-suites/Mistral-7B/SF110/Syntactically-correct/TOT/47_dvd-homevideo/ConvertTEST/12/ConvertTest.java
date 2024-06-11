import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;

public class ConvertTest {
    private Convert convert;
    private String[] videoFiles;
    private File outputDir;

    @Before
    public void setUp() {
        outputDir = new File("testOutput");
        convert = new Convert("dvd-ntsc", "16:9", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        });
        convert.init();
        videoFiles = outputDir.listFiles();
    }

    @Test
    public void testInitWithNoThreadCheck() {
        Convert converter = new Convert(5, "dvd-ntsc", "16:9", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        });
        converter.init();
        File[] testFiles = outputDir.listFiles();
        assertEquals(2, testFiles.length);
        for (File file : testFiles) {
            assertTrue(file.isFile());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithInvalidFormat() {
        Convert converter = new Convert(5, "invalidFormat", "16:9", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        });
        converter.init();
        fail("Expected IllegalArgumentException not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithInvalidAspectRatio() {
        Convert converter = new Convert(5, "dvd-ntsc", "invalidAspectRatio", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public String strOutputDir() {
                return outputDir.getAbsolutePath();
            }
        });
        converter.init();
        fail("Expected IllegalArgumentException not thrown.");
    }

    @Test
    public void testTranscodeWithThreadCheck() {
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        Thread thread = convert.m_Thread;
        thread.join();
        File[] testFiles = outputDir.listFiles();
        assertEquals(3, testFiles.length);
        for (File file : testFiles) {
            assertTrue(file.isFile());
        }
    }

    @Test(expected = NullPointerException.class)
    public void testTranscodeWithNullGUI() {
        Convert converter = new Convert(5, "dvd-ntsc", "16:9", null);
        converter.init();
        fail("Expected NullPointerException not thrown.");
    }

    @Test
    public void testMatrixCreation() {
        File matrixFile = new File(outputDir, "matrix.txt");
        assertTrue(matrixFile.exists());
        assertTrue(matrixFile.length() > 0);
    }

    @Test(expected = IOException.class)
    public void testMatrixFileCreationFailure() throws IOException {
        File tempOutputDir = new File("tempOutput");
        Convert converter = new Convert("dvd-ntsc", "16:9", new GUI() {
            @Override
            public boolean menuChkThread() {
                return false;
            }

            @Override
            public String strOutputDir() {
                return tempOutputDir.getAbsolutePath();
            }
        });
        converter.init();
        fail("Expected IOException not thrown.");
    }
}