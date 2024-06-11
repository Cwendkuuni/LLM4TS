package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

	@Test
	public void testMain() throws Exception {
		FSPathExplorer explorer = new FSPathExplorer();
		String[] args = new String[] {"/path/to/root/directory", "-find", "*.txt"};
		explorer.configureExplorer(args);
		explorer.createFSPath();
		explorer.start();
	}

	@Test
	public void testConfigureExplorer_NoArguments() {
		FSPathExplorer explorer = new FSPathExplorer();
		explorer.configureExplorer(new String[] {});
		assertNull(explorer.rootPath);
		assertNull(explorer.fsPathQuery);
	}

	@Test
	public void testConfigureExplorer_RootPathGiven() {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/path/to/root/directory");
		String[] args = new String[] {"/path/to/root/directory"};
		explorer.configureExplorer(args);
		assertEquals(rootPath, explorer.rootPath);
		assertNull(explorer.fsPathQuery);
	}

	@Test
	public void testConfigureExplorer_FindQueryGiven() {
		FSPathExplorer explorer = new FSPathExplorer();
		String[] args = new String[] {"-find", "*.txt"};
		explorer.configureExplorer(args);
		assertNull(explorer.rootPath);
		assertEquals("*.txt", explorer.fsPathQuery);
	}

	@Test
	public void testConfigureExplorer_BothGiven() {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/path/to/root/directory");
		String[] args = new String[] {"/path/to/root/directory", "-find", "*.txt"};
		explorer.configureExplorer(args);
		assertEquals(rootPath, explorer.rootPath);
		assertEquals("*.txt", explorer.fsPathQuery);
	}

	@Test
	public void testCreateFSPath_NoRootPath() {
		FSPathExplorer explorer = new FSPathExplorer();
		explorer.createFSPath();
		assertNotNull(explorer.fsPath);
		assertEquals(FSPathFactory.getDefaultFSPath(), explorer.fsPath);
	}

	@Test
	public void testCreateFSPath_WithRootPath() {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/path/to/root/directory");
		explorer.configureExplorer(new String[] {"/path/to/root/directory"});
		explorer.createFSPath();
		assertEquals(rootPath, explorer.fsPath.getRootDirectory());
	}

	@Test
	public void testStart_NoQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/path/to/root/directory");
		explorer.configureExplorer(new String[] {"/path/to/root/directory"});
		explorer.createFSPath();
		explorer.start();
		//TODO add assertions for output
	}

	@Test
	public void testStart_FindQuery() throws IOException {
		FSPathExplorer explorer = new FSPathExplorer();
		File rootPath = new File("/path/to/root/directory");
		String[] args = new String[] {"/path/to/root/directory", "-find", "*.txt"};
		explorer.configureExplorer(args);
		explorer.createFSPath();
		explorer.start();
		//TODO add assertions for output
	}

	@Test
	public void testFSPathQuery_Basic() {
		FSPathExplorer explorer = new FSPathExplorer();
		String query = "//dir[@name = 'src']//file[@length > 10000]";
		 explorer.fsPathQuery = query;
		 FSPathResultList results = FSPathFactory.getDefaultFSPath().query(query);
		 //TODO add assertions for results
	}

	@Test
	public void testFSPathQuery_Attributes() {
		FSPathExplorer explorer = new FSPathExplorer();
		String query = "//dir[@name = 'src']//file[@length > 10000 and fs:isDirectory = false and fs:isFile = true]";
		 explorer.fsPathQuery = query;
		 FSPathResultList results = FSPathFactory.getDefaultFSPath().query(query);
		 //TODO add assertions for results
	}

	@Test
	public void testFSPathQuery_AttributesWithQuotes() {
		FSPathExplorer explorer = new FSPathExplorer();
		String query = "//dir[@name = 'src']//file[@length > 10000 and (fs:isDirectory = 'false') and (fs:isFile = 'true')]";
		 explorer.fsPathQuery = query;
		 FSPathResultList results = FSPathFactory.getDefaultFSPath().query(query);
		 //TODO add assertions for results
	}

}