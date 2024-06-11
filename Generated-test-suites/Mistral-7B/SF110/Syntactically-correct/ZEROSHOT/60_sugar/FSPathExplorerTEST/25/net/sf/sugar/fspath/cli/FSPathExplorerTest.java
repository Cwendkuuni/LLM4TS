package net.sf.sugar.fspath.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;
import net.sf.sugar.fspath.cli.Commands;
import net.sf.sugar.fspath.cli.FSPathExplorer;

/**
 * Test class for FSPathExplorer.
 */
public class FSPathExplorerTest {

    @Test
    public void testFSPathExplorer() throws IOException {
        // Given
        String testDirPath = System.getProperty("user.dir");
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File(testDirPath);
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
        explorer.createFSPath();

        // When - pwd command
        String expectedPwd = rootPath.getAbsolutePath();
        String actualPwd = explorer.prompt.readLineAndConsume("pwd");
        assertEquals(expectedPwd, actualPwd);

        // When - cd command
        File testFile = new File(rootPath, "testFile.txt");
        explorer.prompt.readLineAndConsume("cd test");
        explorer.prompt.readLineAndConsume("cd file.txt");
        File currentPath = explorer.fsPath.getFile();
        String actualCd = currentPath.getAbsolutePath();
        assertTrue(testFile.getAbsolutePath().startsWith(actualCd));

        // When - help command
        explorer.prompt.readLineAndConsume("help");
        String expectedHelp = "Commands :";
        String actualHelp = explorer.out.toString();
        assertTrue(actualHelp.contains(expectedHelp));

        // When - find command
        explorer.prompt.readLineAndConsume("find dir[@name = 'test']");
        List<FSPathResult> results = explorer.fsPath.getResults();
        assertEquals(1, results.size());
        File expectedResult = new File(testDirPath, "test");
        File actualResult = results.get(0).getFile();
        assertEquals(expectedResult.getAbsolutePath(), actualResult.getAbsolutePath());

        // When - exit command
        explorer.prompt.readLineAndConsume("exit");
        // Then - application should close
        assertFalse(explorer.isAlive());
    }

    /**
     * Checks if the given command is a command.
     *
     * @param command the command to check
     * @return {@code true} if the command is a command, {@code false} otherwise
     */
    private static boolean isCommand(String command) {
        for (Commands c : Commands.values()) {
            if (command.equals(c.getCommandText())) {
                return true;
            }
        }
        return false;
    }
}