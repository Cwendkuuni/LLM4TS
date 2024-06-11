package test.Newzgrabber;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Newzgrabber.BatchDriver;
import Newzgrabber.BatchJob;
import Newzgrabber.Downloader;
import Newzgrabber.LineData;
import Newzgrabber.NewsFactory;
import Newzgrabber.Newzgrabber;
import Newzgrabber.OptionsPanel;
import Newzgrabber.SongList;
import Newzgrabber.SongInfo;

public class NewzgrabberTest {

	private Newzgrabber newzgrabber;
	private static final String TEST_NEWZDIRECTORY = "test/Newzgrabber/test_files";
	private static final String TEST_BATCHFILENAME = "test_batch.ini";
	private static final String TEST_NEWZINIFILENAME = "test_Newzgrabber.ini";
	private static final String TEST_JARFILENAME = "test_Newz.jar";

	@Before
	public void setUp() {
		System.setProperty("java.library.path", new File(TEST_NEWZDIRECTORY).getAbsolutePath() + File.pathSeparator + System.getProperty("java.library.path"));
		newzgrabber = new Newzgrabber("Newzgrabber Test");
		Newzgrabber.setupBasicSystem();
		Newzgrabber.BatchFilename = TEST_BATCHFILENAME;
		Newzgrabber.NewzInifilename = TEST_NEWZINIFILENAME;
		Newzgrabber.JarFilename = TEST_JARFILENAME;
		Newzgrabber.verbose = true;
	}

	@After
	public void tearDown() {
		Newzgrabber.verbose = false;
		newzgrabber.dispose();
	}

	@Test
	public void testBatchMode() {
		Newzgrabber.IsBatch = true;
		Newzgrabber newzbatch = new Newzgrabber("Newzbatch");
		assertTrue(newzbatch.IsBatch);
	}

	@Test
	public void testNonBatchMode() {
		Newzgrabber.IsBatch = false;
		Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
		assertFalse(newzgrabber.IsBatch);
	}

	@Test
	public void testGetTotalLines() {
		BatchDriver batchDriver = new BatchDriver(TEST_NEWZINIFILENAME, TEST_BATCHFILENAME);
		BatchJob batchJob = batchDriver.getBatchJob("test_group");
		LineData lineData = new LineData("test_server", new String[] {"test_group"});
		long totalLines = lineData.getTotalLines("test_group", SongInfo.getOrderedIds(null));
		assertEquals(10, totalLines);
	}

	@Test
	public void testStartDownload() {
		BatchDriver batchDriver = new BatchDriver(TEST_NEWZINIFILENAME, TEST_BATCHFILENAME);
		BatchJob batchJob = batchDriver.getBatchJob("test_group");
		Downloader downloader = new Downloader();
		downloader.startDownload();
		assertFalse(downloader.isDownloadComplete());

		String articleId = "test_article_id";
		String[] subjectLines = {"test_article_subject"};
		SongList songList = new SongList(subjectLines);
		String[] complete = songList.getCompleteSongs();
		long[] ids = {Long.parseLong(articleId)};
		long totalLines = 10;
		Object[] rowInfo = {complete[0], "test_group", new Long(totalLines), ids};
		Vector<Object[]> downloadQueue = new Vector<>();
		downloader.setDownloadQueue(downloadQueue);
		downloader.setIds(ids);
		downloader.setTotalLines(totalLines);

		downloader.startDownload();
		assertTrue(downloader.isDownloadComplete());
		assertEquals(articleId, downloader.getDownloadedArticleId());
		assertEquals("test_group", downloader.getGroup());
	}

	@Test
	public void testUpdateIni() {
		Newzgrabber newzgrabber = new Newzgrabber("Newzgrabber");
		NewsFactory newsFactory = new NewsFactory();
		newsFactory.createUSENETnews();
		Vector<String[]> iniVector = new Vector<>();
		iniVector.add(new String[] {"test_key", "test_value"});

		newzgrabber.updateIni(TEST_NEWZINIFILENAME, "Messageids", "test_key", "test_value");

		Ini ini = new Ini(TEST_NEWZINIFILENAME, "Messageids");
		LinkedList<String[]> iniData = ini.getIniData();
		assertEquals(1, iniData.size());
		String[] iniEntry = iniData.get(0);
		assertEquals("test_key", iniEntry[0]);
		assertEquals("test_value", iniEntry[1]);
	}
}