package test;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Vector;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.GroupDownloadCount;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzgrabber;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

    private static final String TEST_DIRECTORY = System.getProperty("user.dir") + File.separator + "test-data";

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
    }

    @Test
    public void testNewzgrabber() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        assertNotNull(newzgrabber);
        assertTrue(Newzgrabber.IsBatch == false);
        assertEquals(Newzgrabber.Newzdirectory, findDirectory("Newz.jar"));
    }

    @Test
    public void testFindDirectory() throws Exception {
        String classfile = "Newz.jar";
        String directory = findDirectory(classfile);
        assertEquals(TEST_DIRECTORY, directory.substring(0, TEST_DIRECTORY.length() + 1));
    }

    @Test
    public void testSetupBasicSystem() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        NewsFactory nf = new NewsFactory();
        assertNotNull(nf);
    }

    @Test(expected = Exception.class)
    public void testWindowClosingWithNullIni() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        newzgrabber.updateIni(null, "Messageids", "TestGroup", "TestValue");
    }

    @Test
    public void testUpdateIni() throws Exception {
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        Ini ini = new Ini(TEST_DIRECTORY + File.separator + "test.ini", "Messageids");
        Vector Newids = ini.getIniVector();
        String key = "TestKey";
        String value = "TestValue";
        newzgrabber.updateIni(TEST_DIRECTORY + File.separator + "test.ini", "Messageids", key, value);
        String[] stringarr = null;
        try {
            stringarr = Newids.elementAt(0);
        } catch (Exception e) {
            // expected
        }
        assertNull(stringarr);
        Ini ini2 = new Ini(TEST_DIRECTORY + File.separator + "test.ini", "Messageids");
        Newids = ini2.getIniVector();
        String nextkey = null;
        String thevalue = null;
        for (int i = 0; i < Newids.size(); ++i) {
            String[] teststringarr = Newids.elementAt(i);
            nextkey = teststringarr[0];
            thevalue = teststringarr[1];
            if (nextkey.equals(key)) {
                assertEquals(value, thevalue);
                break;
            }
        }
    }

    @Test
    public void testBatchMode() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        newzgrabber.setBatchMode();
        assertTrue(Newzgrabber.IsBatch);
    }

    @Test
    public void testBatchJob() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        BatchDriver bd = new BatchDriver(TEST_DIRECTORY + File.separator + "test.ini", "test.batch");
        BatchJob bj = bd.getBatchJob("TestGroup");
        assertNotNull(bj);
        assertEquals("TestServer", bj.getServer());
        assertEquals("TestGroup", bj.getGroup());
    }

    @Test
    public void testGetSubjectLines() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        BatchDriver bd = new BatchDriver(TEST_DIRECTORY + File.separator + "test.ini", "test.batch");
        String[] subjectLines = bd.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(3, subjectLines.length);
    }

    @Test
    public void testSongList() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        BatchDriver bd = new BatchDriver(TEST_DIRECTORY + File.separator + "test.ini", "test.batch");
        String[] subjectLines = bd.getSubjectLines();
        SongList sl = new SongList(subjectLines);
        String[] complete = sl.getCompleteSongs();
        assertNotNull(complete);
        assertEquals(3, complete.length);
    }

    @Test
    public void testDownload() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        BatchDriver bd = new BatchDriver(TEST_DIRECTORY + File.separator + "test.ini", "test.batch");
        BatchJob bj = bd.getBatchJob("TestGroup");
        Downloader downloader = new Downloader();
        downloader.setDownloadDirectory(new File(bj.getDirectory()));
        downloader.setServer(bj.getServer());
        downloader.setGroup("TestGroup");
        downloader.setIds((long[]) bj.getIds().toArray(new long[bj.getIds().size()]));
        downloader.setTotalLines(bj.getTotalLines());
        downloader.startDownload();
        assertTrue(downloader.isDownloading());
    }

    @Test
    public void testGroupDownloadCount() throws Exception {
        System.setProperty("java.library.path", TEST_DIRECTORY + File.separator + "natives");
        Newzgrabber newzgrabber = new Newzgrabber("TestNewzgrabber");
        BatchDriver bd = new BatchDriver(TEST_DIRECTORY + File.separator + "test.ini", "test.batch");
        Vector groupDownloadCount = new Vector();
        for (int i = 0; i < bd.getGroups().length; ++i) {
            BatchJob bj = bd.getBatchJob(bd.getGroups()[i]);
            if (bj == null) {
                continue;
            }
            Object[] downloadData = { bj.getGroup(), new Integer(1) };
            groupDownloadCount.add(downloadData);
        }
        GroupDownloadCount gdc = new GroupDownloadCount(groupDownloadCount);
        assertEquals(3, gdc.getDownloadCount("TestGroup"));
    }

    private static File findDirectory(String classfile) {
        try {
            String path = System.getProperty("java.class.path");
            StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
            while (st.hasMoreTokens()) {
                String nextPath = st.nextToken();
                File file = new File(nextPath);
                if (!file.isFile() && file.getName().equals(classfile)) {
                    return file.getParentFile();
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}