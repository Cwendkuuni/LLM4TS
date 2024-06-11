import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;

public class ConvertTest {

    @Test
    public void testConvertConstructorNoThread() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);

        Assert.assertEquals(format, convert.m_Format);
        Assert.assertEquals(aspectRatio, convert.m_AspectRatio);
        Assert.assertEquals(DVD_GUI, convert.m_GUI);
        Assert.assertNull(convert.m_Thread);
        Assert.assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test
    public void testConvertConstructorWithThread() {
        int quality = 1;
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(quality, format, aspectRatio, DVD_GUI);

        Assert.assertEquals(quality, convert.m_Quality);
        Assert.assertEquals(format, convert.m_Format);
        Assert.assertEquals(aspectRatio, convert.m_AspectRatio);
        Assert.assertNotNull(convert.m_Thread);
        Assert.assertEquals(29.97, convert.m_fps, 0.01);
    }

    @Test(expected = NullPointerException.class)
    public void testInitNullGUI() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI nullGUI = null; // Assuming GUI is a mockable class

        Convert convert = new Convert(format, aspectRatio, nullGUI);

        convert.init();
    }

    @Test
    public void testInitNoThread() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);

        Assert.assertFalse(convert.init());
    }

    @Test
    public void testInitWithThread() {
        int quality = 1;
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(quality, format, aspectRatio, DVD_GUI);

        Assert.assertFalse(convert.init());
        Assert.assertTrue(convert.m_Error);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranscodeWithNonStandardFiles() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        File dir = new File("non_standard_files_directory");
        File[] files = dir.listFiles();

        convert.video_files = files;
        convert.thread_track = files.length;

        convert.Transcode();
    }

    @Test
    public void testTranscode() {
        String format = "dvd-ntsc";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        File dir = new File("standard_files_directory");
        File[] files = dir.listFiles();

        convert.video_files = files;
        convert.thread_track = 0;

        convert.Transcode();

        for (int i = 0; i < files.length; i++) {
            String filePath = files[i].getAbsolutePath();
            convert.video_files = new File[]{files[i]};
            convert.thread_track = i + 1;

            convert.Transcode();
        }
    }

    @Test(expected = IOException.class)
    public void testMatrixFileWriting() {
        String outputDir = "output_directory";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(DVD_GUI);

        convert.m_GUI = DVD_GUI;
        convert.m_strOutputDir = outputDir;

        convert.matrix();
    }

    @Test
    public void testMatrixFileReading() {
        String outputDir = "output_directory";
        GUI DVD_GUI = new GUI(); // Assuming GUI is a mockable class

        Convert convert = new Convert(DVD_GUI);

        convert.m_GUI = DVD_GUI;
        convert.m_strOutputDir = outputDir;

        try {
            File matrixFile = new File(outputDir + "/matrix.txt");
            FileReader fileReader = new FileReader(matrixFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            List<Integer[]> matrix = new ArrayList<>();

            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Integer[] row = new Integer[tokens.length];

                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Integer.parseInt(tokens[i]);
                }

                matrix.add(row);
            }

            bufferedReader.close();
            fileReader.close();

            Assert.assertEquals(2, matrix.size());
            Assert.assertEquals(8, matrix.get(0).length);
            Assert.assertEquals(8, matrix.get(1).length);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 8; j++) {
                    Assert.assertTrue(Arrays.asList(matrix.get(i)).contains(j));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}