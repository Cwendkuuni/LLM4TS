package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 }

 @Test
 public void testEmptyQuery() throws IOException {
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testQuerySingleFile() throws IOException {
 File file = new File("/tmp/file.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = 'file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 FSPathResult result = resultList.get(0);
 assertEquals(file.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testQuerySingleDirectory() throws IOException {
 File directory = new File("/tmp/directory");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name = 'directory']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 FSPathResult result = resultList.get(0);
 assertEquals(directory.getAbsolutePath(), result.getFile().getPath());
 }

 @Test
 public void testQueryMultipleFiles() throws IOException {
 File file1 = new File("/tmp/file1.txt");
 File file2 = new File("/tmp/file2.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = 'file1.txt' or name = 'file2.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(2, resultList.size());
 }

 @Test
 public void testQueryMultipleDirectories() throws IOException {
 File directory1 = new File("/tmp/directory1");
 File directory2 = new File("/tmp/directory2");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name = 'directory1' or name = 'directory2']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(2, resultList.size());
 }

 @Test
 public void testQueryRecursive() throws IOException {
 File file = new File("/tmp/directory/file.txt");
 File directory = new File("/tmp/directory");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = 'file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery, FSPath.SEARCH_RECURSIVE);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testQuerySearchUp() throws IOException {
 File file = new File("/tmp/directory/file.txt");
 File directory = new File("/tmp/directory");
 fsPathExplorer.rootPath = new File("/tmp/directory");
 fsPathExplorer.fsPathQuery = "file[name = 'file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery, FSPath.SEARCH_UP);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testQueryFindFilesWithinDirectory() throws IOException {
 File directory = new File("/tmp");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[@name = '.' and file[name = '*']]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 int expectedNumberOfFiles = 6; //assumes there are 6 files in the /tmp directory
 assertEquals(expectedNumberOfFiles, resultList.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueryInvalidArgument() throws IOException {
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name = 'directory'][invalid]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 fail("expected an IllegalArgumentException, but got a FSPathResultList");
 }

 @Test
 public void testFindFilesWithAttributes() throws IOException {
 File file = new File("/tmp/file.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 FSPathAttributes attribute = FSPathAttributes.HIDDEN;
 fsPathExplorer.fsPathQuery = "file[name = 'file.txt'][fs:" + attribute + "]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindDirectoriesWithAttributes() throws IOException {
 File directory = new File("/tmp/directory");
 fsPathExplorer.rootPath = new File("/tmp");
 FSPathAttributes attribute = FSPathAttributes.DIRECTORY;
 fsPathExplorer.fsPathQuery = "dir[name = 'directory'][fs:" + attribute + "]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(directory.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindFilesAndDirectoriesWithAttributes() throws IOException {
 File file = new File("/tmp/file.txt");
 File directory = new File("/tmp/directory");
 fsPathExplorer.rootPath = new File("/tmp");
 FSPathAttributes fileAttribute = FSPathAttributes.HIDDEN;
 FSPathAttributes directoryAttribute = FSPathAttributes.DIRECTORY;
 fsPathExplorer.fsPathQuery = "file[name = 'file.txt'][fs:" + fileAttribute + "] or dir[name = 'directory'][fs:" + directoryAttribute + "]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(2, resultList.size());
 }

 @Test
 public void testFindFilesWithContains() throws IOException {
 File file = new File("/tmp/file_with_text.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[contains(name, 'text')]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindDirectoriesWithContains() throws IOException {
 File directory = new File("/tmp/directory_with_name");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[contains(name, 'name')]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(directory.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindFilesWithStartsWith() throws IOException {
 File file = new File("/tmp/file_starting_with_text.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[startsWith(name, 'text')]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindDirectoriesWithEndsWith() throws IOException {
 File directory = new File("/tmp/directory_ending_with_name");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[endsWith(name, 'name')]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(directory.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindFilesWithRegex() throws IOException {
 File file = new File("/tmp/file_with_number_and_text.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name =~ /\\.\\d+_[a-z]+\\.txt/]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testFindDirectoriesWithRegex() throws IOException {
 File directory = new File("/tmp/directory_with_name_and_number");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name =~ /directory_[0-9]+/]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(directory.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testInvalidRegex() throws IOException {
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name =~ /\\.txt\\.txt/]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testInvalidQuery() throws IOException {
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "@dir[name = 'directory']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testEmptyDirectory() throws IOException {
 File emptyDirectory = new File("/tmp/empty_directory");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name = 'empty_directory']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertTrue(emptyDirectory.list().length == 0);
 }

 @Test
 public void testDirectoryWithFiles() throws IOException {
 File directory = new File("/tmp/directory_with_files");
 File file1 = new File(directory, "file1.txt");
 File file2 = new File(directory, "file2.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "dir[name = 'directory_with_files']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(directory.getAbsolutePath(), resultList.get(0).getFile().getPath());
 FSPathResultList filesResultList = fsPathExplorer.fsPath.query("file[name = 'file1.txt'] or file[name = 'file2.txt']", resultList.get(0).getFile());
 assertEquals(2, filesResultList.size());
 }

 @Test
 public void testInvalidPath() throws IOException {
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = '../file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testInvalidPathWithParent() throws IOException {
 File parentDirectory = new File("/tmp/parent_directory");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = '../../file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }

 @Test
 public void testValidPathWithParent() throws IOException {
 File parentDirectory = new File("/tmp/parent_directory");
 File file = new File(parentDirectory, "file.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = '../../file.txt']";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testValidPathWithParentAndFileAttributes() throws IOException {
 File parentDirectory = new File("/tmp/parent_directory");
 File file = new File(parentDirectory, "file.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = '../../file.txt'][size > 1000]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertEquals(1, resultList.size());
 assertEquals(file.getAbsolutePath(), resultList.get(0).getFile().getPath());
 }

 @Test
 public void testInvalidPathWithParentAndFileAttributes() throws IOException {
 File parentDirectory = new File("/tmp/parent_directory");
 File file = new File(parentDirectory, "file.txt");
 fsPathExplorer.rootPath = new File("/tmp");
 fsPathExplorer.fsPathQuery = "file[name = '../../file.txt'][size > 10000]";
 FSPathResultList resultList = fsPathExplorer.fsPath.query(fsPathExplorer.fsPathQuery);
 assertTrue(resultList.isEmpty());
 }
}