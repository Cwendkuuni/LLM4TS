package test.Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.Ini;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzgrabber;
import Newzgrabber.SongInfo;
import Newzgrabber.SongList;

public class NewzgrabberTest {

	private static Newzgrabber newzgrabber;
	private static Vector<Object[]> groupDownloadCount;
	private static String newzDirectory;
	private static String jarFilename;
	private static String batchFilename;
	private static String newzIniFileName;
	private static BatchDriver batchDriver;
	private static BatchJob batchJob;
	private static LineData lineData;
	private static SongList songList;
	private static Vector<Object> affiliates;
	private static Downloader downloader;

	@BeforeClass
	public static void setUp() {
		newzDirectory = System.getProperty("user.dir") + File.separator + "testdata";
		jarFilename = "Newz.jar";
		batchFilename = "batch.ini";
		newzIniFileName = "Newzgrabber.ini";
		Newzgrabber.Newzdirectory = newzDirectory;
		Newzgrabber.JarFilename = jarFilename;
		Newzgrabber.BatchFilename = batchFilename;
		Newzgrabber.NewzInifilename = newzIniFileName;
		Newzgrabber.verbose = false;
		Newzgrabber.debug = false;
		Newzgrabber.batch = false;
		Newzgrabber.IsBatch = false;
		newzgrabber = new Newzgrabber("Newzgrabber");
		groupDownloadCount = new Vector<Object[]>();
	}

	@Test
	public void testNewzgrabberBatchMode() {
		Newzgrabber.IsBatch = true;
		try {
			batchDriver = new BatchDriver(newzIniFileName, batchFilename);
			final String[] batchGroups = batchDriver.getGroups();
			assertEquals(2, batchGroups.length);
			for (String group : batchGroups) {
				batchJob = batchDriver.getBatchJob(group);
				assertNotNull(batchJob);
				String[] grouparray = { batchJob.getGroup() };
				lineData = new LineData(batchJob.getServer(), grouparray);
				String[] subjectlines = batchDriver.getSubjectLines();
				songList = new SongList(subjectlines);
				String[] complete = songList.getCompleteSongs();
				assertEquals(2, complete.length);
				for (int i = 0; i < complete.length; ++i) {
					Object[] va = { batchJob.getGroup(), songList.getSongs().get(complete[i]) };
					Hashtable<String, Object> tmp = (Hashtable<String, Object>) va[1];
					long[] orderedIds = SongInfo.getOrderedIds(tmp);
					Long totalLines = lineData.getTotalLines(group, orderedIds);
					assertEquals(100L, totalLines.longValue());
					Object[] rowinfo = { complete[i], group, new Long(totalLines), orderedIds };
					groupDownloadCount.add(rowinfo);
				}
				for (int j = 0; j < groupDownloadCount.size(); ++j) {
					Object[] objarr = groupDownloadCount.elementAt(j);
					String tmpGroup = (String) objarr[1];
					int tmpDownloads = (int) objarr[2].longValue();
					assertEquals(1, tmpDownloads);
				}
			}
			assertEquals(4, groupDownloadCount.size());
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void testNewzgrabberGUI() {
		Newzgrabber.IsBatch = false;
		newzgrabber = new Newzgrabber("Newzgrabber");
		assertNotNull(newzgrabber.getContentPane());
		assertNotNull(newzgrabber.jtp);
		assertEquals(5, newzgrabber.jtp.getTabCount());
	}

	@Test
	public void testSetupBasicSystem() {
		Newzgrabber.setupBasicSystem();
		assertEquals(newzDirectory, Newzgrabber.Newzdirectory);
		assertTrue(new File(Newzgrabber.Newzdirectory).exists());
	}

	@Test
	public void testWindowEvents() {
		newzgrabber = new Newzgrabber("Newzgrabber");
		newzgrabber.addWindowListener(e -> {});
	}

	@Test
	public void testFindDirectory() {
		String classFile = "Newz.class";
		String newzDirectory = Newzgrabber.findDirectory(classFile);
		assertTrue(newzDirectory != null && new File(newzDirectory).exists());
	}

	@Test
	public void testUpdateIni() {
		try {
			Ini ini = new Ini(newzIniFileName, "Messageids");
			Vector<String[]> iniVector = ini.getIniVector();
			newzgrabber.updateIni(newzIniFileName, "Messageids", "TestKey", "TestValue");
			iniVector = ini.getIniVector();
			String[] testKeyValue = null;
			for (String[] stringArr : iniVector) {
				if (stringArr[0].equals("TestKey")) {
					testKeyValue = stringArr;
					break;
				}
			}
			assertEquals("TestKey", testKeyValue[0]);
			assertEquals("TestValue", testKeyValue[1]);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}

	@Test
	public void testMain() {
		try {
			Newzgrabber.main(new String[] { "-verbose" });
			Newzgrabber.verbose = true;
			assertTrue(Newzgrabber.verbose);
			Newzgrabber.main(new String[] {});
			newzgrabber = new Newzgrabber("Newzgrabber");
			assertNotNull(newzgrabber.getContentPane());
			assertNotNull(newzgrabber.jtp);
			assertEquals(5, newzgrabber.jtp.getTabCount());
			Newzgrabber.main(new String[] { "-batch" });
			Newzgrabber.IsBatch = true;
			batchDriver = new BatchDriver(newzIniFileName, batchFilename);
			final String[] batchGroups = batchDriver.getGroups();
			assertEquals(2, batchGroups.length);
			Newzgrabber.main(new String[] { "-debug" });
			Newzgrabber.debug = true;
			assertTrue(Newzgrabber.debug);
		} catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
	}
}