import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;
    private String[] videoFiles;
    private File listDir;
    private String expectedErrorMessage;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public String strOutputDir() {
                return "output_dir";
            }

            @Override
            public void MessageBox(String message, int type) {
                // Do nothing
            }

            @Override
            public void txtAreaOutput_setCaretPosition(int position) {
                // Do nothing
            }

            @Override
            public String[] menuChkThread_getSelectedFiles() {
                return new String[]{"test1.dv"};
            }

            @Override
            public int[] spnMinutes_getValue() {
                return new int[]{5};
            }

            @Override
            public int[] spnSeconds_getValue() {
                return new int[]{0};
            }
        });
        listDir = new File("output_dir/dv/");
        videoFiles = listDir.list();
        expectedErrorMessage = "Transcoding Error - Could not locate transcode in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.";
    }

    @After
    public void tearDown() {
        if (convert != null) {
            try {
                convert.init();
            } catch (Exception e) {
                // Ignore exceptions during test tear-down
            }
            convert = null;
        }
    }

    @Test
    public void testInit_ThreadDisable() {
        assertTrue(convert.init());
        assertTrue(listDir.list().length > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInit_ThreadEnable_NoTranscode() {
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
    }

    @Test
    public void testInit_ThreadEnable_TranscodeNotFound() {
        System.setProperty("path.separator", File.pathSeparatorChar + ";");
        System.setProperty("java.class.path", "/path/to/your/test/project/:" + System.getProperty("java.class.path"));
        System.setProperty("os.name", "Windows 10");
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertEquals(expectedErrorMessage, convert.m_GUI.txtAreaOutput.getText());
    }

    @Test
    public void testInit_ThreadEnable_Success() {
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
        assertTrue(Arrays.stream(listDir.list()).anyMatch(fileName -> fileName.endsWith(".vob")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranscode_InvalidFormat() {
        convert = new Convert("invalid_format", "1:1", new GUI() {
            // GUI implementation here
        });
        convert.Transcode();
    }

    @Test
    public void testTranscode_Success() {
        String output1 = "encoding video test1.dv\n";
        String output2 = "encoding video test2.dv\n";
        String output3 = "encoding video test3.dv\n";
        String output4 = "encoding video test4.dv\n";
        String output5 = "encoding video test5.dv\n";

        convert = new Convert("dvd-ntsc", "1:1", new GUI() {
            @Override
            public String strOutputDir() {
                return "output_dir";
            }

            @Override
            public void MessageBox(String message, int type) {
                assertEquals("", message);
            }

            @Override
            public void txtAreaOutput_setCaretPosition(int position) {
                // Do nothing
            }

            @Override
            public String[] menuChkThread_getSelectedFiles() {
                return new String[]{"test1.dv", "test2.dv", "test3.dv", "test4.dv", "test5.dv"};
            }

            @Override
            public int[] spnMinutes_getValue() {
                return new int[]{5};
            }

            @Override
            public int[] spnSeconds_getValue() {
                return new int[]{0};
            }
        });

        convert.init();

        String actualOutput = convert.m_GUI.txtAreaOutput.getText();
        String expectedOutput = output1 + output2 + output3 + output4 + output5;

        assertTrue(actualOutput.contains(output1));
        assertTrue(actualOutput.contains(output2));
        assertTrue(actualOutput.contains(output3));
        assertTrue(actualOutput.contains(output4));
        assertTrue(actualOutput.contains(output5));
        assertEquals(expectedOutput, actualOutput);
    }
}