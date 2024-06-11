package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

	private FSPathExplorer explorer;
	private FSPathFactory factory;
	private FSPath queryPath;

	@org.junit.Before
	public void setUp() throws IOException {
		factory = new FSPathFactory();
		File root = new File(".");
		explorer = new FSPathExplorer();
		explorer.rootPath = root;
		explorer.createFSPath();
		queryPath = explorer.fsPath;
	}

	@org.junit.Test
	public void testCreateFSPath() throws IOException {
		assertEquals(queryPath.getRootDirectory(), new File(".").getAbsoluteFile());
	}

	@org.junit.Test
	public void testStartWithoutQuery() {
		explorer.start();
	}

	@org.junit.Test
	public void testStartWithQuery() throws IOException {
		explorer.fsPathQuery = "/dir[@name = 'test']/file[name = 'test.txt']";
		explorer.start();

		List<FSPathResult> results = queryPath.query(explorer.fsPathQuery);
		assertTrue(results.size() > 0);
	}

	@org.junit.Test
	public void testQuerySimplePath() throws IOException {
		FSPathResult result = queryPath.query("/").get(0);
		assertEquals(result.getFile(), new File(".").getAbsoluteFile());
	}

	@org.junit.Test
	public void testQuerySimpleName() throws IOException {
		FSPathResult result = queryPath.query("test.txt").get(0);
		assertEquals(result.getFile().getAbsolutePath(), "test.txt");
	}

	@org.junit.Test
	public void testQuerySimpleAttributes() throws IOException {
		FSPathResult result = queryPath.query("/dir[@name = 'test']", FSPathAttributes.SIZE).get(0);
		assertEquals(result.getFile().getAbsolutePath(), "test/");
		assertTrue(result.getAttributes().containsKey(FSPathAttributes.SIZE));
		assertEquals(result.getAttributes().get(FSPathAttributes.SIZE).longValue(), 0);
	}

	@org.junit.Test
	public void testQueryComplexPath() throws IOException {
		FSPathResult result = queryPath.query("/user/home/Documents/test.txt").get(0);
		assertEquals(result.getFile().getAbsolutePath(), "/user/home/Documents/test.txt");
	}

	@org.junit.Test
	public void testQueryComplexAttributes() throws IOException {
		FSPathResult result = queryPath.query("/user/home/Documents/test.txt", FSPathAttributes.SIZE, FSPathAttributes.LAST_MODIFIED)
				.get(0);
		assertEquals(result.getFile().getAbsolutePath(), "/user/home/Documents/test.txt");
		assertTrue(result.getAttributes().containsKey(FSPathAttributes.SIZE));
		assertEquals(result.getAttributes().get(FSPathAttributes.SIZE).longValue(), 0);
		assertTrue(result.getAttributes().containsKey(FSPathAttributes.LAST_MODIFIED));
		assertEquals(result.getAttributes().get(FSPathAttributes.LAST_MODIFIED).longValue(), System.currentTimeMillis());
	}

	@org.junit.Test
	public void testQueryComplexAttributesWithNoMatch() throws IOException {
		FSPathResultList results = queryPath.query("/user/home/Documents/test2.txt", FSPathAttributes.SIZE, FSPathAttributes.LAST_MODIFIED);
		assertFalse(results.size() > 0);
	}

	@org.junit.Test
	public void testStartWithInvalidQuery() {
		explorer.fsPathQuery = "invalidquery";
		explorer.start();
	}

	@org.junit.Test
	public void testQuitCommand() {
		explorer.start();
		explorer.prompt.write("exit\n");
		explorer.start();
	}

	@org.junit.Test
	public void testHelpCommand() {
		explorer.start();
		explorer.prompt.write("help\n");
		explorer.start();
	}

	@org.junit.Test
	public void testCdCommand() {
		explorer.start();
		explorer.prompt.write("cd test\n");
		explorer.start();
		FSPathResult result = queryPath.query(".").get(0);
		assertEquals(result.getFile().getAbsolutePath(), "test/.");
	}

	@org.junit.Test
	public void testCdCommandToNonExistingDirectory() {
		explorer.start();
		explorer.prompt.write("cd test2\n");
		explorer.start();
		explorer.prompt.write("pwd\n");
		explorer.start();
		assertEquals(queryPath.getRootDirectory().getAbsolutePath(), ".");
	}

	@org.junit.Test
	public void testPwdCommand() {
		explorer.start();
		explorer.prompt.write("cd test\n");
		explorer.start();
		explorer.prompt.write("pwd\n");
		explorer.start();
		assertEquals(queryPath.getRootDirectory().getAbsolutePath(), "test/.");
	}

	@org.junit.Test
	public void testInvalidCommand() {
		explorer.start();
		explorer.prompt.write("invalidcommand\n");
		explorer.start();
	}

	@org.junit.Test
	public void testQuitCommandInQuitState() {
		explorer.start();
		explorer.prompt.write("exit\n");
		explorer.start();
		explorer.prompt.write("exit\n");
		explorer.start();
	}

	@org.junit.Test
	public void testHelpCommandInQuitState() {
		explorer.start();
		explorer.prompt.write("exit\n");
		explorer.start();
		explorer.prompt.write("help\n");
		explorer.start();
	}

	@org.junit.Test
	public void testCdCommandInQuitState() {
		explorer.start();
		explorer.prompt.write("exit\n");
		explorer.start();
		explorer.prompt.write("cd test\n");
		explorer.start();
		explorer.prompt.write("pwd\n");
		explorer.start();
		assertEquals(queryPath.getRootDirectory().getAbsolutePath(), "test/.");
	}

}