package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.Downloader;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzgrabber;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class NewzgrabberTest {

    private static Newzgrabber newzgrabber;
    private static NewsFactory nf;
    private static Vector<Object[]> groupDownloadCount;

    @BeforeClass
    public static void setup() {
        Newzgrabber.verbose = false;
        Newzgrabber.debug = false;
        Newzgrabber.batch = false;
        Newzgrabber.IsBatch = false;
        Newzgrabber.Newzdirectory = "test_directory";
        Newzgrabber.JarFilename = "Newz.jar";
        Newzgrabber.BatchFilename = "batch.ini";
        Newzgrabber.NewzInifilename = "Newzgrabber.ini";

        newzgrabber = new Newzgrabber("Newzgrabber");
        nf = new NewsFactory();
        groupDownloadCount = new Vector<>();
    }

    @Test
    public void testBatchMode() throws IOException, InterruptedException {
        final String[] batchGroups = {"Group1", "Group2"};
        final String newzini = newzgrabber.Newzdirectory + File.separator + Newzgrabber.NewzInifilename;
        final BatchDriver bd = new BatchDriver(newzini, Newzgrabber.Newzdirectory + File.separator + Newzgrabber.BatchFilename);
        final String[] BatchGroups = bd.getGroups();

        Assert.assertArrayEquals(batchGroups, BatchGroups);

        for (String group : batchGroups) {
            final BatchJob bj = bd.getBatchJob(group);
            final String[] grouparray = {bj.getGroup()};
            final LineData ld = new LineData(bj.getServer(), grouparray);
            final String[] subjectlines = bd.getSubjectLines();
            final SongList sl = new SongList(subjectlines);
            final String[] complete = sl.getCompleteSongs();

            int downloads = 0;
            for (String completeSong : complete) {
                final Object[] va = {bj.getGroup(), sl.getSongs().get(completeSong)};
                final Hashtable<Integer, SongInfo> tmp = (Hashtable<Integer, SongInfo>) va[1];
                final long[] OrderedIds = SongInfo.getOrderedIds(tmp);
                if (OrderedIds != null) {
                    final long TotalLines = ld.getTotalLines(group, OrderedIds);
                    if (tmp.get("UPPERLIMIT") < 2) {
                        continue;
                    }
                    final SongInfo si = tmp.get(new Integer(1));
                    final long id = si.getId();
                    final long[] tmpids = {id};
                    if (ld.getTotalLines(group, tmpids) < 100L) {
                        continue;
                    }
                    downloads++;
                }
            }

            final Object[] DownloadData = {group, new Integer(downloads)};
            groupDownloadCount.add(DownloadData);
        }

        for (Object[] downloadData : groupDownloadCount) {
            final String group = (String) downloadData[0];
            final int downloads = ((Integer) downloadData[1]).intValue();
            System.out.println(group + "\t" + downloads);
            Assert.assertEquals("Incorrect number of downloads for group " + group, 2, downloads);
        }
    }

    @Test
    public void testGUI() {
        // TODO: Write test cases for GUI components
    }

    @Test
    public void testDownload() throws Exception {
        final String group = "TestGroup";
        final String server = "TestServer";
        final String username = "TestUsername";
        final String password = "TestPassword";
        final String directory = "TestDirectory";

        final Newzgrabber newInstance = new Newzgrabber(group);
        final Downloader downloader = new Downloader();

        downloader.setDownloadDirectory(new File(directory));
        downloader.setServer(server);
        downloader.setUsername(username);
        downloader.setPassword(password);
        downloader.setGroup(group);
        downloader.setIds(new long[]{1L});
        downloader.setTotalLines(100L);

        downloader.startDownload();

        // TODO: Verify that the file was downloaded correctly
    }

    @Test
    public void testUpdateIni() {
        final String inifile = "test.ini";
        final String prop = "Messageids";
        final String key = "TestKey";
        final String value = "TestValue";

        newzgrabber.updateIni(inifile, prop, key, value);

        // TODO: Verify that the ini file was updated correctly
    }
}