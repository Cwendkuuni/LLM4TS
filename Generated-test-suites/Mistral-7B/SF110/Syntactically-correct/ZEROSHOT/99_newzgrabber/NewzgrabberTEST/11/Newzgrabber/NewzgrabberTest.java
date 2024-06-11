package test.Newzgrabber;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

    private static final String TEST_DATA_DIR = "src/test/resources/Newzgrabber/";

    @Before
    public void setUp() {
        System.setProperty("java.library.path", Newzgrabber.Newzdirectory + File.pathSeparator + System.getProperty("java.library.path"));
    }

    @After
    public void tearDown() {
        System.clearProperty("java.library.path");
    }

    @Test
    public void testNewzgrabberConstruction() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        assertSame(newzgrabber, Newzgrabber.jtp.getSelectedComponent());
        assertSame(newzgrabber, Newzgrabber.searchpanel.getParent());
        assertSame(newzgrabber, Newzgrabber.optionspanel.getParent());
        assertSame(newzgrabber, Newzgrabber.groupspanel.getParent());
        assertSame(newzgrabber, Newzgrabber.transferpanel.getParent());
        assertSame(newzgrabber, Newzgrabber.jtp.getParent());
    }

    @Test
    public void testNewzgrabberBatchConstruction() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertSame(newzgrabber, Newzgrabber.jtp.getSelectedComponent());
        assertNull(Newzgrabber.searchpanel);
        assertNull(Newzgrabber.optionspanel);
        assertNull(Newzgrabber.groupspanel);
        assertNull(Newzgrabber.transferpanel);
        assertSame(newzgrabber, Newzgrabber.jtp.getParent());
    }

    @Test
    public void testNewzgrabberSetupBasicSystem() {
        Newzgrabber newzgrabber = new Newzgrabber();
        assertSame(newzgrabber.Newzdirectory, findDirectory(Newzgrabber.JarFilename));
        assertFalse(System.getProperty("os.name").toLowerCase().indexOf("windows") == -1);
        assertTrue(newzgrabber.Newzdirectory.endsWith(File.separator));
    }

    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String newzdirectory = findDirectory(classfile);
        assertNotNull(newzdirectory);
        assertFalse(newzdirectory.endsWith(File.separator));
        assertTrue(new File(newzdirectory).isDirectory());
    }

    @Test
    public void testUpdateIni() {
        String inifile = TEST_DATA_DIR + "Newzgrabber.ini";
        Newzgrabber.updateIni(inifile, "Messageids", "testkey", "testvalue");
        String[][] data = new String[][] {
            {"Messageids", "testvalue"},
        };
        assertArrayEquals(data, new Ini(inifile, "Messageids").getIniVector().toArray(new String[][] {}));
    }

    @Test
    public void testBatchDriver() throws Exception {
        String newzini = TEST_DATA_DIR + "batch.ini";
        String batchfile = TEST_DATA_DIR + "batch.dat";
        BatchDriver batchDriver = new BatchDriver(newzini, batchfile);
        assertNotNull(batchDriver);
        String[] batchGroups = batchDriver.getGroups();
        assertArrayEquals(new String[] {"Group1", "Group2"}, batchGroups);
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        String[] subjectlines = batchDriver.getSubjectLines();
        assertNotNull(subjectlines);
        assertArrayEquals(new String[] {"Subject1", "Subject2"}, subjectlines);
        SongList songList = new SongList(subjectlines);
        String[] complete = songList.getCompleteSongs();
        assertArrayEquals(new String[] {"Article1", "Article2"}, complete);
    }

    @Test
    public void testDownloader() throws Exception {
        String directory = TEST_DATA_DIR + "download";
        File downloadDirectory = new File(directory);
        Downloader downloader = new Downloader();
        downloader.setDownloadDirectory(downloadDirectory);
        downloader.setServer("testserver");
        downloader.setUsername("testuser");
        downloader.setPassword("testpassword");
        downloader.setGroup("testgroup");
        downloader.setIds(new long[] {1, 2, 3});
        downloader.setTotalLines(100L);
        downloader.startDownload();
        assertTrue(downloadDirectory.exists());
        assertTrue(downloadDirectory.isDirectory());
        assertTrue(downloadDirectory.listFiles().length > 0);
    }

    private static File findDirectory(String classfile) {
        try {
            String path = System.getProperty("java.class.path");
            StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
            while (st.hasMoreTokens()) {
                String nextpath = st.nextToken();
                File file = new File(nextpath);
                if (!file.isFile() && file.isDirectory()) {
                    File classFile = new File(file, classfile);
                    if (classFile.exists()) {
                        return file;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Ini {
    private Vector iniVector = new Vector();

    public Ini(String filename, String section) {
        File inifile = new File(filename);
        try (BufferedReader reader = new BufferedReader(new FileReader(inifile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(section + " =")) {
                    String[] keyvalues = line.substring(section.length() + 1).split("=");
                    iniVector.add(new String[] {keyvalues[0], keyvalues[1]});
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Vector<String[]> getIniVector() {
        return iniVector;
    }

    public void writeIni(LinkedList<Object[]> iniData) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(this.new File(new File("Newzgrabber.ini").getParentFile(), "Newzgrabber.ini").getPath()))) {
            for (Object[] data : iniData) {
                writer.println(Arrays.toString((String[]) data));
            }
        }
    }
}