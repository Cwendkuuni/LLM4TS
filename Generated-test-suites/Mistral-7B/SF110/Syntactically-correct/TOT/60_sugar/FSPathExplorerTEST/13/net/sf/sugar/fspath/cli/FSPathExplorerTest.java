package net.sf.sugar.fspath.cli;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer explorer;
 private File rootPath;
 private String query;

 @Before
 public void setup() {
 explorer = new FSPathExplorer();
 rootPath = new File("./testFiles");
 explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
 explorer.createFSPath();
 query = "/dir[@name = 'test']";
 }

 @Test
 public void testFindDirectory() {
 FSPathResultList results = explorer.fsPath.query(query);
 File expected = new File(rootPath, "test");
 assertTrue(results.size() > 0);
 assertEquals(expected.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testFindMissingDirectory() {
 query = "/dir[@name = 'missing']";
 explorer.fsPath.query(query);
 }

 @Test
 public void testFindFileInDirectory() {
 File testDir = new File(rootPath, "test");
 File testFile = new File(testDir, "testFile.txt");
 testFile.getParentFile().mkdirs();
 try {
 testFile.createNewFile();
 } catch (IOException e) {
 e.printStackTrace();
 }

 query = "/file[@name = 'testFile.txt']";
 results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test(expected = IOException.class)
 public void testFindMissingFile() {
 query = "/file[@name = 'missingFile.txt']";
 explorer.fsPath.query(query);
 }

 @Test
 public void testChangeDirectory() {
 File testSubDirectory = new File(rootPath, "testSubDirectory");
 testSubDirectory.mkdir();
 File testFile = new File(testSubDirectory, "testFile.txt");
 try {
 testFile.createNewFile();
 } catch (IOException e) {
 e.printStackTrace();
 }

 explorer.prompt.write("cd testSubDirectory");
 explorer.start();
 explorer.prompt.write(query);
 results = explorer.fsPath.query(query);
 assertTrue(results.size() > 0);
 assertEquals(testFile.getAbsolutePath(), results.get(0).getFile().getAbsolutePath());
 }

 @Test
 public void testChangeDirectoryInvalidDirectory() {
 explorer.prompt.write("cd nonExistentDirectory");
 explorer.start();
 explorer.prompt.write(query);
 results = explorer.fsPath.query(query);
 assertTrue(results.isEmpty());
 }

 @Test
 public void testHelpCommand() {
 explorer.prompt.write("help");
 explorer.start();
 //check help output here
 }

 @Test
 public void testExitCommand() {
 explorer.prompt.write("exit");
 explorer.start();
 }

 @Test
 public void testPwdCommand() {
 explorer.prompt.write("pwd");
 explorer.start();
 String expected = rootPath.getAbsolutePath();
 assertEquals(expected, explorer.out.toString());
 }
}