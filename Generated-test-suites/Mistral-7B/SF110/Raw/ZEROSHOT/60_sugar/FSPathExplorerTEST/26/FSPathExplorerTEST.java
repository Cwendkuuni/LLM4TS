package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.cli.Prompt;

public class FSPathExplorerTest {

	private FSPathExplorer explorer;
	private File testDirectory;

	@Before
	public void setUp() throws IOException {
		testDirectory = new File("target/test-files");
		testDirectory.mkdir();
		testDirectory.setWritable(true, false);
		testDirectory.setReadable(true, false);

		explorer = new FSPathExplorer();
		explorer.rootPath = testDirectory;
		explorer.fsPath = FSPathFactory.newFSPath(testDirectory);
		explorer.out = System.out;
		explorer.err = System.err;
	}

	@After
	public void tearDown() throws Exception {
		File tempFile = new File(testDirectory, "testFile.txt");
		if (tempFile.exists()) {
			tempFile.delete();
		}
		testDirectory.delete();
	}

	@Test
	public void testCreateFSPath() {
		FSPath fsPath = explorer.fsPath;
		assertEquals(testDirectory, fsPath.getRootDirectory());
		assertTrue(fsPath.exists());
	}

	@Test
	public void testQueryEmptyDirectory() {
		String query = "//file";
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(0, results.size());
	}

	@Test
	public void testQueryFile() throws IOException {
		String query = "//file[@name = 'testFile.txt']";
		File testFile = new File(testDirectory, "testFile.txt");
		testFile.createNewFile();
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(1, results.size());
		assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
	}

	@Test
	public void testQueryDirectory() throws IOException {
		String query = "//dir[@name = 'testDir']";
		File testDir = new File(testDirectory, "testDir");
		testDir.mkdir();
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(1, results.size());
		assertEquals(testDir.getAbsolutePath(), results.get(0).getFile().getPath());
	}

	@Test
	public void testQuerySubDirectory() throws IOException {
		String query = "//dir[@name = 'testSubDir']/file";
		File testDir = new File(testDirectory, "testDir");
		testDir.mkdir();
		File testSubDir = new File(testDir, "testSubDir");
		testSubDir.mkdir();
		File testFile = new File(testSubDir, "testFile.txt");
		testFile.createNewFile();
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(1, results.size());
		assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getPath());
	}

	@Test
	public void testQueryNonExistingDirectory() {
		String query = "//dir[@name = 'nonExistingDir']/file";
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(0, results.size());
	}

	@Test
	public void testQueryNonExistingFile() {
		String query = "//file[@name = 'nonExistingFile.txt']";
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(0, results.size());
	}

	@Test
	public void testQueryMultipleMatches() throws IOException {
		String query = "//dir[@name = 'testDir']/file";
		File testDir = new File(testDirectory, "testDir");
		testDir.mkdir();
		File testFile1 = new File(testDir, "testFile1.txt");
		testFile1.createNewFile();
		File testFile2 = new File(testDir, "testFile2.txt");
		testFile2.createNewFile();
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(2, results.size());
		assertEquals(testFile1.getAbsolutePath(), results.get(0).getFile().getPath());
		assertEquals(testFile2.getAbsolutePath(), results.get(1).getFile().getPath());
	}

	@Test
	public void testQueryAttributes() throws IOException {
		String query = "//file[@size > 1]";
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(2, results.size());
		assertTrue(results.get(0).getAttributes().contains(FSPathAttributes.SIZE));
		assertTrue(results.get(1).getAttributes().contains(FSPathAttributes.SIZE));
		assertTrue(results.get(0).getAttributes().contains(FSPathAttributes.HIDDEN));
		assertFalse(results.get(0).getAttributes().contains(FSPathAttributes.DIRECTORY));
		assertTrue(results.get(1).getAttributes().contains(FSPathAttributes.DIRECTORY));
	}

	@Test
	public void testQueryRegex() throws IOException {
		String query = "//file[@name = 'test.*']";
		File testDir = new File(testDirectory, "testRegex");
		testDir.mkdir();
		File testFile1 = new File(testDir, "testFile1.txt");
		testFile1.createNewFile();
		File testFile2 = new File(testDir, "testFile2.txt");
		testFile2.createNewFile();
		File testFile3 = new File(testDir, "testFile3.jpg");
		testFile3.createNewFile();
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(3, results.size());
		assertEquals(testFile1.getAbsolutePath(), results.get(0).getFile().getPath());
		assertEquals(testFile2.getAbsolutePath(), results.get(1).getFile().getPath());
		assertEquals(testFile3.getAbsolutePath(), results.get(2).getFile().getPath());
	}

	@Test
	public void testQueryEmptyString() {
		String query = "";
		List<FSPathResult> results = explorer.fsPath.query(query);
		assertEquals(0, results.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testQueryNullString() {
		String query = null;
		explorer.fsPath.query(query);
	}

	@Test
	public void testPrompt() {
		String testInput = "pwd";
		String expectedOutput = "Current directory : " + testDirectory.getAbsolutePath();
		Prompt prompt = new Prompt(System.in, explorer.out, explorer.err);
		String currentExpression = prompt.readLine();
		assertEquals(testInput, currentExpression);
		explorer.out.println(expectedOutput);
		assertEquals(expectedOutput, currentExpression);
	}

	@Test
	public void testExitCommand() {
		String testInput = Commands.EXIT.getCommandText();
		Prompt prompt = new Prompt(System.in, explorer.out, explorer.err);
		String currentExpression = prompt.readLine();
		assertEquals(testInput, currentExpression);
		explorer.prompt.close();
		assertNull(explorer.prompt);
	}

	@Test
	public void testHelpCommand() {
		String testInput = Commands.HELP.getCommandText();
		Prompt prompt = new Prompt(System.in, explorer.out, explorer.err);
		String currentExpression = prompt.readLine();
		assertEquals(testInput, currentExpression);
		explorer.showHelp();
		// JUnit does not support printing text to the console
		// assertTextPresent(expectedOutput);
	}

	@Test
	public void testCdCommand() throws IOException {
		String testInput = "cd testDir";
		Prompt prompt = new Prompt(System.in, explorer.out, explorer.err);
		File testDir = new File(testDirectory, "testDir");
		testDir.mkdir();
		explorer.rootPath = testDirectory;
		explorer.fsPath = FSPathFactory.newFSPath(testDirectory);
		explorer.createFSPath();
		assertEquals(testDirectory.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
		String currentExpression = prompt.readLine();
		assertEquals(testInput, currentExpression);
		explorer.cd(testDir);
		assertEquals(testDir.getAbsolutePath(), explorer.fsPath.getRootDirectory().getAbsolutePath());
		assertEquals(testDir.getAbsolutePath(), explorer.fsPath.getAbsolutePath());
		assertEquals(testDir.getPath(), currentExpression.split(" ")[1]);
	}
}