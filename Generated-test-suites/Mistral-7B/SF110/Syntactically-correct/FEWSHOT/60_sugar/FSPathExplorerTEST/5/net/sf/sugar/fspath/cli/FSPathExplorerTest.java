package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;
 private FSPath fsPath;

 @Before
 public void setUp() {
 fsPathExplorer = new FSPathExplorer();
 fsPath = FSPathFactory.newFSPath(new File("."));
 }

 @Test
 public void testStartWithEmptyQuery() {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "";
 String[] expressionWords = currentExpression.split(" ");

 assertNull(expressionWords);
 }

 @Test
 public void testStartWithPWDCommand() {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = Commands.PWD.getCommandText();
 String[] expressionWords = currentExpression.split(" ");

 assertEquals(Commands.PWD.getCommandText(), expressionWords[0]);
 out.flush();
 String result = out.readLine();
 assertEquals("Current directory : " + System.getProperty("user.dir"), result);
 }

 @Test
 public void testStartWithCDCommand() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = Commands.CD.getCommandText() + " test";
 String[] expressionWords = currentExpression.split(" ");

 assertEquals(Commands.CD.getCommandText(), expressionWords[0]);
 File newDir = new File("test");
 File newDirFile = new File(System.getProperty("user.dir"), "test");
 assertTrue(newDir.exists());
 assertTrue(newDir.isDirectory());
 assertTrue(newDir.canRead());
 fsPathExplorer.cd(newDirFile);
 String result = out.readLine();
 assertEquals("changed to : test", result);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithHelpCommand() {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = Commands.HELP.getCommandText();
 String[] expressionWords = currentExpression.split(" ");

 assertEquals(Commands.HELP.getCommandText(), expressionWords[0]);
 }

 @Test
 public void testStartWithEmptyQueryAndFind() {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "";
 String[] expressionWords = currentExpression.split(" ");

 assertNull(expressionWords);

 FSPathResultList resultList = fsPath.query("*");
 for (FSPathResult result : resultList) {
 out.println(result.getFile().getPath());
 }

 String expectedResult = System.getProperty("user.dir");
 String actualResult = out.readLine();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testStartWithFindCommand() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "*.txt";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("find", expressionWords[0]);
 assertEquals("*.txt", expressionWords[1]);

 FSPathResultList resultList = fsPath.query(currentExpression);
 for (FSPathResult result : resultList) {
 out.println(result.getFile().getPath());
 }

 String expectedResult = System.getProperty("user.dir") + File.separator + "test" + File.separator + "test.txt";
 String actualResult = out.readLine();
 assertEquals(expectedResult, actualResult);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithFindCommandWithMultipleFiles() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "*.txt";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("find", expressionWords[0]);
 assertEquals("*.txt", expressionWords[1]);

 FSPathResultList resultList = fsPath.query(currentExpression);
 for (int i = 0; i < resultList.size(); i++) {
 FSPathResult result = resultList.get(i);
 out.println(result.getFile().getPath());
 }

 String expectedResult1 = System.getProperty("user.dir") + File.separator + "test" + File.separator + "test.txt";
 String expectedResult2 = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "Test.java";
 String actualResult1 = out.readLine();
 String actualResult2 = out.readLine();
 assertEquals(expectedResult1, actualResult1);
 assertEquals(expectedResult2, actualResult2);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithFindCommandWithEmptyResult() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "non-existent-file.txt";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("find", expressionWords[0]);
 assertEquals("non-existent-file.txt", expressionWords[1]);

 FSPathResultList resultList = fsPath.query(currentExpression);
 for (FSPathResult result : resultList) {
 out.println(result.getFile().getPath());
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithFindCommandWithInvalidQuery() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "non-existing-command";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("non-existing-command", expressionWords[0]);
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithFindCommandWithInvalidQuery2() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "//dir[@name = 'foo']/foo";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("find", expressionWords[0]);
 assertEquals("//dir[@name = 'foo']/foo", expressionWords[1]);

 FSPathResultList resultList = fsPath.query(currentExpression);
 for (FSPathResult result : resultList) {
 out.println(result.getFile().getPath());
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @Test
 public void testStartWithFindCommandWithInvalidQuery3() {
 try {
 fsPathExplorer.rootPath = null;
 fsPathExplorer.fsPath = fsPath;
 fsPathExplorer.start();

 String currentExpression = "//dir[@name = 'foo']/foo/file";
 String[] expressionWords = currentExpression.split(" ");

 assertNotNull(expressionWords);
 assertEquals("find", expressionWords[0]);
 assertEquals("//dir[@name = 'foo']/foo/file", expressionWords[1]);

 FSPathResultList resultList = fsPath.query(currentExpression);
 for (FSPathResult result : resultList) {
 out.println(result.getFile().getPath());
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail();
 }
 }

 @After
 public void tearDown() {
 try {
 if (fsPath != null) {
 fsPath.close();
 }
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
}