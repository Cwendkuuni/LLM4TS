package test.Newzgrabber;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.AboutPanel;
import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.DownloadPanel;
import Newzgrabber.GroupsPanel;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.OptionsPanel;
import Newzgrabber.SearchPanel;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

    private static final String TEST_DATA_DIR = "testData/";
    private static final String TEST_INI_FILE = "testData/Newzgrabber.ini";
    private static final String TEST_BATCH_FILE = "testData/batch.ini";
    private static final String TEST_JAR_FILE = "testData/Newz.jar";

    private BatchDriver batchDriverMock;
    private OptionsPanel optionsPanelMock;
    private GroupsPanel groupsPanelMock;
    private SearchPanel searchPanelMock;
    private TransferPanel transferPanelMock;
    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        batchDriverMock = mock(BatchDriver.class);
        optionsPanelMock = mock(OptionsPanel.class);
        groupsPanelMock = mock(GroupsPanel.class);
        searchPanelMock = mock(SearchPanel.class);
        transferPanelMock = mock(TransferPanel.class);
        newzgrabber = new Newzgrabber() {
            @Override
            public void updateIni(String iniFile, String prop, String key, String value) {
                Ini ini = new Ini(TEST_INI_FILE, prop);
                ini.writeIni(Arrays.asList(new Object[][] { { key, value } }));
            }
        };
        newzgrabber.setBatchDriver(batchDriverMock);
        newzgrabber.setOptionsPanel(optionsPanelMock);
        newzgrabber.setGroupsPanel(groupsPanelMock);
        newzgrabber.setSearchPanel(searchPanelMock);
        newzgrabber.setTransferPanel(transferPanelMock);
    }

    @After
    public void tearDown() {
        // Clear mocks
        reset(batchDriverMock, optionsPanelMock, groupsPanelMock, searchPanelMock, transferPanelMock);
    }

    @Test
    public void testNewzgrabberConstructionWithGui() {
        Newzgrabber newzgrabberWithGui = new Newzgrabber("Newzgrabber");
        verify(newzgrabberWithGui, times(1)).getContentPane().setLayout(eq(new BorderLayout()));
        verify(newzgrabberWithGui, times(1)).addWindowListener(eq(this));
        verify(newzgrabberWithGui, times(1)).setSize(eq(600), eq(500));
        verify(newzgrabberWithGui, times(1)).setupBasicSystem();
        verify(newzgrabberWithGui, times(1)).add(eq(Newzgrabber.jtp), eq("Center"));
        verify(newzgrabberWithGui, times(1)).setVisible(eq(true));
        verify(Newzgrabber.jtp, times(1)).addTab("Search", eq(newzgrabberWithGui.searchpanel));
        verify(Newzgrabber.jtp, times(1)).addTab("Options", eq(newzgrabberWithGui.optionspanel));
        verify(Newzgrabber.jtp, times(1)).addTab("Groups", eq(newzgrabberWithGui.groupspanel));
        verify(Newzgrabber.jtp, times(1)).addTab("Transfer", eq(newzgrabberWithGui.transferpanel));
        verify(Newzgrabber.jtp, times(1)).addTab("About", eq(new AboutPanel()));
    }

    @Test
    public void testNewzgrabberConstructionInBatchMode() {
        Newzgrabber newzgrabberInBatchMode = new Newzgrabber();
        verify(newzgrabberInBatchMode, times(1)).setupBasicSystem();
        verifyNoMoreInteractions(newzgrabberInBatchMode);
    }

    @Test
    public void testNewzgrabberLoadsBatchFile() {
        String[] testBatchGroups = { "testGroup1", "testGroup2" };
        when(batchDriverMock.getGroups()).thenReturn(testBatchGroups);
        String[] testSubjectLines = { "testSubjectLine1", "testSubjectLine2" };
        when(batchDriverMock.getSubjectLines()).thenReturn(testSubjectLines);
        String[] testCompleteSongs = { "testCompleteSong1", "testCompleteSong2" };
        SongList testSongList = new SongList(testSubjectLines);
        String[] testOrderedIds = { 1, 2 };
        when(batchDriverMock.getLineData(testBatchGroups[0], (int[]) anyIntArray())).thenReturn(new LineData("testServer", testGroupArray()));
        when(testSongList.getCompleteSongs()).thenReturn(testCompleteSongs);
        when(testSongList.getSongs()).thenReturn(testSongList);

        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.setBatchDriver(batchDriverMock);
        newzgrabber.Newzdirectory = TEST_DATA_DIR;
        newzgrabber.JarFilename = TEST_JAR_FILE;
        newzgrabber.BatchFilename = TEST_BATCH_FILE;
        newzgrabber.NewzInifilename = TEST_INI_FILE;

        newzgrabber.Newzgrabber();

        verify(batchDriverMock).getGroups();
        verify(batchDriverMock).getSubjectLines();
        verify(batchDriverMock, times(testBatchGroups.length)).getBatchJob(eq(anyString()));
    }

    @Test
    public void testNewzgrabberDownloadsArticles() {
        String[] testBatchGroups = { "testGroup1", "testGroup2" };
        String testBatchFileName = TEST_DATA_DIR + "batch.ini";
        when(batchDriverMock.getGroups()).thenReturn(testBatchGroups);
        when(batchDriverMock.getBatchJob(eq("testGroup1"))).thenReturn(new BatchJob("testGroup1", "testServer", "testDirectory"));
        when(batchDriverMock.getServer()).thenReturn("testServer");
        when(batchDriverMock.getDirectory()).thenReturn(new File("testDirectory"));
        String[] testSubjectLines = { "testSubjectLine1", "testSubjectLine2" };
        SongList testSongList = new SongList(testSubjectLines);
        String[] testCompleteSongs = { "testCompleteSong1", "testCompleteSong2" };
        when(batchDriverMock.getSubjectLines()).thenReturn(testSubjectLines);
        when(testSongList.getCompleteSongs()).thenReturn(testCompleteSongs);
        when(testSongList.getSongs()).thenReturn(testSongList);
        long[] testOrderedIds = { 1, 2 };
        when(batchDriverMock.getLineData(testBatchGroups[0], (int[]) anyIntArray())).thenReturn(new LineData("testServer", testGroupArray()));

        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.setBatchDriver(batchDriverMock);
        newzgrabber.Newzdirectory = TEST_DATA_DIR;
        newzgrabber.JarFilename = TEST_JAR_FILE;
        newzgrabber.BatchFilename = TEST_BATCH_FILE;
        newzgrabber.NewzInifilename = TEST_INI_FILE;

        newzgrabber.Newzgrabber();

        Newzgrabber.Downloader downloader = new Newzgrabber.Downloader();
        downloader.setDownloadDirectory(new File("testDirectory"));
        downloader.setServer("testServer");
        downloader.setGroup("testGroup1");
        downloader.setIds(testOrderedIds);
        downloader.setTotalLines((long) 10);

        verify(batchDriverMock).getBatchJob("testGroup1");
        verify(batchDriverMock).getServer();
        verify(batchDriverMock).getDirectory();
        verify(downloadPanelMock, times(2)).startDownload();

        newzgrabber.download(testBatchGroups[0], downloader);
    }

    @Test
    public void testNewzgrabberUpdatesIniFile() {
        Newzgrabber newzgrabber = new Newzgrabber();
        newzgrabber.updateIni(TEST_INI_FILE, "Messageids", "testGroup1", "12345");

        Ini ini = new Ini(TEST_INI_FILE, "Messageids");
        String[] iniContents = ini.getIniVector().toArray(new String[0][2]);
        assertEquals(iniContents.length, 1);
        assertEquals(iniContents[0][0], "testGroup1");
        assertEquals(iniContents[0][1], "12345");
    }
}