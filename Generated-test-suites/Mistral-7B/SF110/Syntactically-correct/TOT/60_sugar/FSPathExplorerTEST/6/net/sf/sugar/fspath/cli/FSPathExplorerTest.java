import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class FSPathExplorerTest {

 private FSPathExplorer fsPathExplorer;

 @Before
 public void setup() {
 fsPathExplorer = new FSPathExplorer();
 }

 @Test
 public void testPWDCommand() {
 // given
 fsPathExplorer.rootPath = new File("./src/test/java");
 fsPathExplorer.createFSPath();

 // when
 String expectedPath = "./src/test/java";
 String actualPath = fsPathExplorer.prompt.readLineAndConsume(Commands.PWD.getCommandText());

 // then
 Assert.assertEquals(expectedPath, actualPath);
 }

 @Test
 public void testCDCommand() {
 // given
 fsPathExplorer.rootPath = new File("./src/test/java");
 fsPathExplorer.createFSPath();
 File testDir = new File("./src/test/resources");

 // when
 fsPathExplorer.prompt.sendInputAndConsume("cd test");
 fsPathExplorer.prompt.sendInputAndConsume("cd resources");

 // then
 Assert.assertEquals(testDir.getAbsolutePath(), fsPathExplorer.fsPath.getRootDirectory().getAbsolutePath());
 }

 @Test
 public void testHelpCommand() {
 // given
 fsPathExplorer.rootPath = new File("./src/test/java");
 fsPathExplorer.createFSPath();

 // when
 String expectedHelp = "FSPathExplorer help :\n\nEnter an FSPath query to search the filesystem\n\nExamples :\n\n1) //dir[@name = 'foo'] \n   finds any directory under the current directory named 'foo'\n\n2) //dir[@name = 'src']//file[@length > 10000]\n   finds any file which is nested anywhere below a 'src' directory\n   with a file size greater than 10000 bytes\n\nAttibutes : (see javadoc for java.io.File for more info)\n";
 String actualHelp = "";
 try {
 actualHelp = (String) fsPathExplorer.prompt.sendCommandAndGetResult(Commands.HELP.getCommandText());
 } catch (IOException e) {
 e.printStackTrace();
 }

 // then
 Assert.assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testQueryCommand() throws IOException {
 // given
 File rootDir = new File("./src/test/java");
 fsPathExplorer.rootPath = rootDir;
 fsPathExplorer.createFSPath();
 FSPathResultList resultList = fsPathExplorer.fsPath.query("//dir[@name = 'test']");

 // when
 String expectedPath = "./src/test/java/test";
 String actualPath = resultList.get(0).getFile().getPath();

 // then
 Assert.assertEquals(expectedPath, actualPath);
 }

 @Test(expected = IOException.class)
 public void testInvalidQueryCommand() throws IOException {
 // given
 File rootDir = new File("./src/test/java");
 fsPathExplorer.rootPath = rootDir;
 fsPathExplorer.createFSPath();

 // when & then
 fsPathExplorer.prompt.sendInputAndConsume("find evil");
 }

 @Test
 public void testExitCommand() {
 // given
 File rootDir = new File("./src/test/java");
 fsPathExplorer.rootPath = rootDir;
 fsPathExplorer.createFSPath();

 // when
 fsPathExplorer.prompt.sendInputAndConsume(Commands.EXIT.getCommandText());

 // then
 Assert.assertTrue(true);
 }
}