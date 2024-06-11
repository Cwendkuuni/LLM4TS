package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

	private FSPathExplorer explorer;
	private String query;
	private File root;

	@Before
	public void setUp() {
		query = "//file[@name = 'test.txt']";
		root = new File("src/test/resources");
		explorer = new FSPathExplorer();
		explorer.configureExplorer(new String[] { "-p", root.getAbsolutePath(), "-f", query });
		explorer.createFSPath();
	}

	@After
	public void tearDown() {
		explorer.prompt.close();
		explorer = null;
	}

	@Test
	public void testCreateExplorer() {
		assertNotNull(explorer);
	}

	@Test
	public void testCreateFSPath() {
		assertNotNull(explorer.fsPath);
	}

	@Test
	public void testFindFileQuery() throws IOException {
		FSPathResultList results = explorer.fsPath.query(query);
		assertTrue(results.size() > 0);
		FSPathResult result = results.get(0);
		assertEquals(new File(root, "testQuery" + File.separator + "test.txt").getAbsolutePath(), result.getFile().getAbsolutePath());
	}

	@Test
	public void testFindNoResultQuery() throws IOException {
		FSPathResultList results = explorer.fsPath.query("//file[@name = 'nonExistentFile']");
		assertFalse(results.size() > 0);
	}

	@Test(expected = IOException.class)
	public void testFindQueryWithInvalidPath() throws IOException {
		explorer.configureExplorer(new String[] { "-p", "nonExistentPath" });
		explorer.createFSPath();
		explorer.fsPath.query("//file[@name = 'test.txt']");
	}

	@Test
	public void testCdCommand() throws IOException {
		String cdQuery = "cd " + File.separator + "testQuery";
		explorer.prompt.sendInput(cdQuery);
		assertEquals(new File(root, "testQuery").getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
	}

	@Test(expected = IOException.class)
	public void testCdCommandInvalidDirectory() throws IOException {
		String cdQuery = "cd nonExistentDirectory";
		explorer.prompt.sendInput(cdQuery);
	}
}