package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

public class FSPathExplorerTest {

	@org.junit.Before
	public void setUp() {
		System.setIn(System.in);
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@org.junit.After
	public void tearDown() {
		System.setIn(System.in);
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void testNewInstance() {
		FSPathExplorer explorer = new FSPathExplorer();
		assertNotNull(explorer);
		assertEquals(System.out, explorer.out);
		assertEquals(System.err, explorer.err);
	}

	@Test
	public void testShowWelcome() {
		FSPathExplorer explorer = new FSPathExplorer();
		explorer.showWelcome();
	}

	@Test
	public void testNewFSPath() {
		FSPathExplorer explorer = new FSPathExplorer();
		FSPath fsPath = explorer.newFSPath();
		assertNotNull(fsPath);
	}

	@Test
	public void testNewFSPathWithRootPath() {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		FSPath fsPath = explorer.newFSPath(rootPath);
		assertNotNull(fsPath);
		assertEquals(rootPath, fsPath.getRootDirectory());
	}

	@Test
	public void testConfigureExplorerWithInvalidArguments() {
		FSPathExplorer explorer = new FSPathExplorer();
		String[] args = { "-p", "/" };
		explorer.configureExplorer(args);
		assertNull(explorer.rootPath);
		assertNull(explorer.fsPathQuery);
		assertNull(explorer.fsPath);
	}

	@Test
	public void testConfigureExplorerWithValidArguments() {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		String[] args = { "-p", rootPath.getPath(), "-f", "//file[@name = 'test.txt']" };
		explorer.configureExplorer(args);
		assertEquals(rootPath, explorer.rootPath);
		assertEquals("//file[@name = 'test.txt']", explorer.fsPathQuery);
		assertNotNull(explorer.fsPath);
	}

	@Test
	public void testCreateFSPath() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath() });
		explorer.createFSPath();
		assertEquals(rootPath, explorer.fsPath.getRootDirectory());
	}

	@Test
	public void testCreateFSPathWithEmptyQuery() {
		FSPathExplorer explorer = new FSPathExplorer();
		explorer.rootPath = new File("/");
		explorer.configureExplorer(new String[0]);
		explorer.createFSPath();
		assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
		assertEquals("", explorer.fsPathQuery);
	}

	@Test
	public void testCreateFSPathWithInvalidQuery() {
		FSPathExplorer explorer = new FSPathExplorer();
		explorer.rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-f", "//foo" });
		explorer.createFSPath();
		assertEquals(explorer.rootPath, explorer.fsPath.getRootDirectory());
		assertEquals("//foo", explorer.fsPathQuery);
		assertFalse(explorer.fsPath.isValid());
	}

	@Test
	public void testCreateFSPathWithValidQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//file[@name = 'test.txt']" });
		explorer.createFSPath();
		assertEquals(rootPath, explorer.fsPath.getRootDirectory());
		assertEquals("//file[@name = 'test.txt']", explorer.fsPathQuery);
		assertTrue(explorer.fsPath.isValid());
	}

	@Test
	public void testStartWithEmptyQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath() });
		explorer.createFSPath();
		explorer.start();
		assertTrue(System.out.contains("Scanning filesystem metadata..."));
		assertTrue(System.out.contains("Type 'help' for commands..."));
		assertTrue(System.out.contains("Current directory : /"));
		String[] lines = System.out.getLines();
		assertEquals(0, lines.length);
	}

	@Test
	public void testStartWithInvalidQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//foo" });
		explorer.createFSPath();
		explorer.start();
		assertTrue(System.out.contains("Scanning filesystem metadata..."));
		assertTrue(System.out.contains("Type 'help' for commands..."));
		assertTrue(System.out.contains("Current directory : /"));
		assertTrue(System.out.contains("unable to change to foo please make sure that the directory existst and a is readable"));
		assertTrue(System.out.contains("No results"));
		String[] lines = System.out.getLines();
		assertEquals(3, lines.length);
	}

	@Test
	public void testStartWithValidQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//file[@name = 'test.txt']" });
		explorer.createFSPath();
		explorer.start();
		assertTrue(System.out.contains("Scanning filesystem metadata..."));
		assertTrue(System.out.contains("Type 'help' for commands..."));
		assertTrue(System.out.contains("Current directory : /"));
		assertTrue(System.out.contains("/test.txt"));
		assertEquals(1, System.out.getLines().length);
	}

	@Test
	public void testStartWithValidQueryAndChangeDirectory() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//file[@name = 'test.txt']" });
		explorer.createFSPath();
		explorer.start();
		String currentDirectory = System.out.getLines()[2];
		explorer.prompt.writeLine("cd /tmp");
		explorer.start();
		String newDirectory = System.out.getLines()[2];
		assertTrue(currentDirectory.startsWith("/"));
		assertTrue(newDirectory.startsWith("/tmp"));
		assertTrue(System.out.contains("changed to : /tmp"));
		assertTrue(System.out.contains("No results"));
		assertEquals(3, System.out.getLines().length);
	}

	@Test
	public void testStartWithValidQueryAndFind() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//file[@name = 'test.txt']" });
		explorer.createFSPath();
		explorer.start();
		String currentDirectory = System.out.getLines()[2];
		explorer.prompt.writeLine("find //file[@name = 'test.txt']");
		explorer.start();
		assertEquals("/test.txt", System.out.getLines()[0]);
		assertEquals(2, System.out.getLines().length);
	}

	@Test
	public void testStartWithValidQueryAndHelp() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath() });
		explorer.createFSPath();
		explorer.start();
		explorer.prompt.writeLine("help");
		explorer.start();
		String output = System.out.getLines()[0];
		assertTrue(output.contains("FSPathExplorer help :"));
		assertTrue(output.contains("Enter an FSPath query to search the filesystem"));
		assertTrue(output.contains("Examples :"));
		assertTrue(output.contains("Commands :\n"));
		assertTrue(output.contains(Commands.CD.getCommandText()));
		assertTrue(output.contains(Commands.EXIT.getCommandText()));
		assertTrue(output.contains(Commands.FIND.getCommandText()));
		assertTrue(output.contains(Commands.HELP.getCommandText()));
		assertTrue(output.contains(Commands.PWD.getCommandText()));
		assertEquals(7, System.out.getLines().length);
	}

	@Test
	public void testStartWithInvalidQueryAndHelp() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath(), "-f", "//foo" });
		explorer.createFSPath();
		explorer.start();
		explorer.prompt.writeLine("help");
		explorer.start();
		String output = System.out.getLines()[0];
		assertTrue(output.contains("Scanning filesystem metadata..."));
		assertTrue(output.contains("Type 'help' for commands..."));
		assertTrue(output.contains("Current directory : /"));
		assertTrue(output.contains("unable to change to foo please make sure that the directory existst and a is readable"));
		assertTrue(output.contains("No results"));
		assertTrue(output.contains("Type 'help' for commands..."));
		assertTrue(output.contains("Enter an FSPath query to search the filesystem"));
		assertTrue(output.contains("Examples :"));
		assertTrue(output.contains("Commands :\n"));
		assertTrue(output.contains(Commands.CD.getCommandText()));
		assertTrue(output.contains(Commands.EXIT.getCommandText()));
		assertTrue(output.contains(Commands.FIND.getCommandText()));
		assertTrue(output.contains(Commands.HELP.getCommandText()));
		assertTrue(output.contains(Commands.PWD.getCommandText()));
		assertEquals(11, System.out.getLines().length);
	}

	@Test
	public void testStartWithEmptyQueryAndHelp() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/");
		explorer.configureExplorer(new String[] { "-p", rootPath.getPath() });
		explorer.createFSPath();
		explorer.start();
		explorer.prompt.writeLine("help");
		explorer.start();
		String output = System.out.getLines()[0];
		assertTrue(output.contains("Scanning filesystem metadata..."));
		assertTrue(output.contains("Type 'help' for commands..."));
		assertTrue(output.contains("Current directory : /"));
		assertTrue(output.contains("Type 'help' for commands..."));
		assertTrue(output.contains("Enter an FSPath query to search the filesystem"));
		assertTrue(output.contains("Examples :"));
		assertTrue(output.contains("Commands :\n"));
		assertTrue(output.contains(Commands.CD.getCommandText()));
		assertTrue(output.contains(Commands.EXIT.getCommandText()));
		assertTrue(output.contains(Commands.FIND.getCommandText()));
		assertTrue(output.contains(Commands.HELP.getCommandText()));
		assertTrue(output.contains(Commands.PWD.getCommandText()));
		assertEquals(11, System.out.getLines().length);
	}
}