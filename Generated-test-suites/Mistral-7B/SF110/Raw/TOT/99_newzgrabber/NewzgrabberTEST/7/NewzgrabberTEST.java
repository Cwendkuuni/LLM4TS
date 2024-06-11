package NewzgrabberTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

    private BatchDriver mockBatchDriver;
    private BatchJob mockBatchJob;
    private LineData mockLineData;
    private NewsFactory mockNewsFactory;
    private Ini mockIni;
    private Vector mockGroupDownloadCount;

    @Before
    public void setupMocks() {
        mockBatchDriver = mock(BatchDriver.class);
        mockBatchJob = mock(BatchJob.class);
        mockLineData = mock(LineData.class);
        mockNewsFactory = mock(NewsFactory.class);
        mockIni = mock(Ini.class);
        when(mockBatchDriver.getGroups()).thenReturn(new String[] {"Group1"});
        when(mockBatchDriver.getBatchJob(anyString())).thenReturn(mockBatchJob);
        when(mockBatchJob.getServer()).thenReturn("http://testserver.com");
        when(mockBatchJob.getGroup()).thenReturn("Group1");
        when(mockBatchJob.getDirectory()).thenReturn(new File("testdir"));
        when(mockBatchJob.getUsername()).thenReturn("testuser");
        when(mockBatchJob.getPassword()).thenReturn("testpassword");
        when(mockBatchDriver.getSubjectLines()).thenReturn(new String[] {"Test Subject"});
        when(mockNewsFactory.createLineData(anyString(), any(String[].class))).thenReturn(mockLineData);
        when(mockIni.getIniVector()).thenReturn(new LinkedList<>());
        mockGroupDownloadCount = new Vector<>();
    }

    @Test
    public void testNewzgrabberWithBatchMode() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
        newzgrabber.setBatch(true);

        // Test calling Newzgrabber constructor with batch mode
        verify(mockBatchDriver, times(1)).new BatchDriver(anyString(), anyString());

        // Test getting batch info from batch file
        newzgrabber.init();
        verify(mockBatchDriver, times(1)).getGroups();

        // Test getting batch job from batch driver
        newzgrabber.getBatchJob("Group1");
        verify(mockBatchDriver, times(1)).getBatchJob("Group1");

        // Test creating LineData object
        newzgrabber.getLineData("http://testserver.com", new String[] {"Group1"});
        verify(mockNewsFactory, times(1)).createLineData("http://testserver.com", new String[] {"Group1"});

        // Test getting subject lines
        newzgrabber.getSubjectLines();
        verify(mockBatchDriver, times(1)).getSubjectLines();

        // Test downloading articles
        newzgrabber.downloadArticles("Group1");
        verify(mockBatchJob, times(1)).setIds((long[]) anyArray());
        verify(mockBatchJob, times(1)).setTotalLines((long) anyInt());
        verify(mockGroupDownloadCount, times(1)).add(any(Object[].class));
        verify(mockIni, times(1)).writeIni(any(LinkedList.class));
        verify(mockDownloader, times(1)).startDownload();
    }

    @Test
    public void testNewzgrabberWithGUIMode() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");

        // Test calling Newzgrabber constructor with GUI mode
        verify(mockBatchDriver, never()).new BatchDriver(anyString(), anyString());

        // Test creating JFrame object
        newzgrabber.createAndShowGUI();
        verify(mockJFrame, times(1)).new JFrame("Newzgrabber");

        // Test adding components to JTabbedPane
        newzgrabber.createComponents();
        verify(mockJTabbedPane, times(1)).addTab("Search", any(JPanel.class));
        verify(mockJTabbedPane, times(1)).addTab("Options", any(JPanel.class));
        verify(mockJTabbedPane, times(1)).addTab("Groups", any(JPanel.class));
        verify(mockJTabbedPane, times(1)).addTab("Transfer", any(JPanel.class));
        verify(mockJTabbedPane, times(1)).addTab("About", any(JPanel.class));

        // Test setting preferred size of JTabbedPane
        newzgrabber.setJTabbedPanePreferredSize();
        verify(mockJTabbedPane, times(1)).setPreferredSize(any(Dimension.class));

        // Test adding JTabbedPane to JFrame
        newzgrabber.addJTabbedPaneToCenter();
        verify(mockJFrame, times(1)).add(any(Component.class), "Center");

        // Test setting visible of JFrame
        newzgrabber.setVisible();
        verify(mockJFrame, times(1)).setVisible(true);
    }

    @Test
    public void testUpdateIni() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");

        // Test updating ini file with empty inifile
        newzgrabber.updateIni("testini.ini", "Messageids", "Group1", "123");
        verify(mockIni, times(1)).writeIni(any(LinkedList.class));

        // Test updating ini file with existing key
        newzgrabber.updateIni("testini.ini", "Messageids", "Group1", "456");
        verify(mockIni, times(1)).writeIni(any(LinkedList.class));

        // Test updating ini file with new key
        newzgrabber.updateIni("testini.ini", "Newprop", "TestKey", "TestValue");
        verify(mockIni, times(1)).writeIni(any(LinkedList.class));
    }

    @Test
    public void testFindDirectory() {
        Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");

        // Test finding directory of a JAR file
        String classfile = "Newz.jar";
        String expectedDirectory = "path/to/Newz.jar";
        String actualDirectory = newzgrabber.findDirectory(classfile);
        assertEquals(expectedDirectory, actualDirectory);

        // Test finding directory of a non-existent JAR file
        classfile = "nonExistentJar.jar";
        assertNull(newzgrabber.findDirectory(classfile));

        // Test finding directory of a non-JAR file
        classfile = "testfile.txt";
        assertNull(newzgrabber.findDirectory(classfile));
    }
}