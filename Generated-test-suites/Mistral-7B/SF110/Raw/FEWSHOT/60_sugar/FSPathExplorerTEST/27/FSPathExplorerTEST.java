package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.sugar.fspath.FSPath;
import net.sf.sugar.fspath.FSPathAttributes;
import net.sf.sugar.fspath.FSPathFactory;
import net.sf.sugar.fspath.FSPathResult;
import net.sf.sugar.fspath.FSPathResultList;

public class FSPathExplorerTest {

    @Test
    public void testFSPathExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNotNull(explorer);

        // Test empty constructor
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);

        // Test constructor with arguments
        String[] args = new String[]{"-p", "/tmp", "-f", "//file[@name = 'test.txt']"};
        explorer.configureExplorer(args);
        File rootPath = explorer.rootPath;
        assertNotNull(rootPath);
        assertEquals("/tmp", rootPath.getAbsolutePath());
        assertNotNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);

        // Test configureExplorer with invalid arguments
        args = new String[]{"-p", "/tmp", "-f", "//file[@name = 'test.txt'", "-find", "//dir[@name = 'test']"};
        explorer = new FSPathExplorer();
        explorer.configureExplorer(args);
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);

        // Test createFSPath
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        assertNotNull(explorer.fsPath);

        // Test start
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/ tmp"}); // Misspelled path on purpose
        explorer.createFSPath();

        List<String> commands = new ArrayList<>();
        commands.add("pwd");
        commands.add("cd /tmp");
        commands.add("help");
        commands.add("cd ..");
        commands.add("cd ..");
        commands.add("cd ..");
        commands.add("find //file[@name = 'test.txt']");
        commands.add("exit");

        for (String command : commands) {
            try {
                explorer.prompt.println(command);
                String response = explorer.prompt.readLine();
                if (command.startsWith("find")) {
                    String expected = "/tmp/test.txt";
                    assertEquals(expected, response);
                } else if (command.equals("pwd")) {
                    String expected = "/";
                    assertEquals(expected, response);
                } else if (command.equals("help")) {
                    // Not testing output here
                    assertTrue(explorer.prompt.isOpen());
                } else {
                    assertTrue(true);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                fail("Unexpected exception: " + ioe.getMessage());
            }
        }

        // Test query with empty query
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String emptyQuery = "";
        FSPathResultList resultList = explorer.fsPath.query(emptyQuery);
        assertEquals(0, resultList.size());

        // Test query with valid query
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String query = "//file[@name = 'test.txt']";
        resultList = explorer.fsPath.query(query);
        assertEquals(1, resultList.size());
        FSPathResult result = resultList.get(0);
        File expectedFile = new File("/tmp/test.txt");
        assertEquals(expectedFile.getAbsolutePath(), result.getFile().getAbsolutePath());

        // Test query with invalid query
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String invalidQuery = "//file[@name = 'test.txt'/@name = 'foo']";
        resultList = explorer.fsPath.query(invalidQuery);
        assertEquals(0, resultList.size());

        // Test query with query containing FSPathAttributes
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithAttributes = "//file[@name = 'test.txt'][fs:isFile][fs:size > 1]";
        resultList = explorer.fsPath.query(queryWithAttributes);
        assertEquals(1, resultList.size());

        // Test query with query containing FSPathAttributes and logical OR
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithAttributesOr = "//file[@name = 'test.txt'][fs:isFile][fs:size > 1] //file[@name = 'test2.txt'][fs:isFile][fs:size > 1]";
        resultList = explorer.fsPath.query(queryWithAttributesOr);
        assertEquals(2, resultList.size());

        // Test query with query containing FSPathAttributes and logical AND
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithAttributesAnd = "//file[@name = 'test.txt'][fs:isFile] //file[@name = 'test2.txt'][fs:isDirectory][fs:size > 1]";
        resultList = explorer.fsPath.query(queryWithAttributesAnd);
        assertEquals(1, resultList.size());

        // Test query with query containing wildcard character
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithWildcard = "//file[@name = 'test.*']";
        resultList = explorer.fsPath.query(queryWithWildcard);
        assertEquals(2, resultList.size());

        // Test query with query containing parentheses
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithParentheses = "//file[@name = 'test.txt'] //(//file[@name = 'test2.txt'] //file[@name = 'test3.txt'] //file[@name = 'test4.txt'])";
        resultList = explorer.fsPath.query(queryWithParentheses);
        assertEquals(2, resultList.size());

        // Test query with query containing double negative
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithDoubleNegative = "//file[@name = 'test.txt'] //:(//file[@name = 'test2.txt'] //file[@name = 'test3.txt'] //file[@name = 'test4.txt'])";
        resultList = explorer.fsPath.query(queryWithDoubleNegative);
        assertEquals(3, resultList.size());

        // Test query with query containing regular expression
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithRegex = "//file[@name = '(test|test2|test3).txt']";
        resultList = explorer.fsPath.query(queryWithRegex);
        assertEquals(3, resultList.size());

        // Test query with query containing logical NOT
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithNot = "//file[@name = 'test.txt'] //:(//file[@name = 'test2.txt'] //file[@name = 'test3.txt'] //file[@name = 'test4.txt']) //:(//file[@name = 'test5.txt'] //file[@name = 'test6.txt'] //file[@name = 'test7.txt'])";
        resultList = explorer.fsPath.query(queryWithNot);
        assertEquals(6, resultList.size());

        // Test query with query containing multiple FSPathAttributes
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithMultipleAttributes = "//file[@name = 'test.txt'][fs:isFile][fs:size > 1][fs:isHidden]";
        resultList = explorer.fsPath.query(queryWithMultipleAttributes);
        assertEquals(1, resultList.size());

        // Test query with query containing multiple FSPathAttributes and logical OR
        explorer = new FSPathExplorer();
        explorer.configureExplorer(new String[]{"-p", "/tmp"});
        explorer.createFSPath();
        String queryWithMultipleAttributesOr = "//file[@name = 'test.txt'][fs:isFile][fs:size > 1] //file[@name = 'test2.txt'][fs:isFile][fs:size > 1][fs:isHidden]";
        resultList = explorer.fsPath.query(queryWithMultipleAttributesOr);
        assertEquals(2, resultList.size());
    }

    private enum Commands {
        PWD("/pwd", "Print working directory"),
        CD("/cd", "Change directory"),
        HELP("/help", "Show help and commands"),
        EXIT("/exit", "Exit the application"),
        ;

        private String commandText;
        private String description;

        Commands(String commandText, String description) {
            this.commandText = commandText;
            this.description = description;
        }

        public String getCommandText() {
            return commandText;
        }

        public String getDescription() {
            return description;
        }

        public static boolean isCommand(String commandText) {
            for (Commands command : Commands.values()) {
                if (commandText.equalsIgnoreCase(command.getCommandText())) {
                    return true;
                }
            }
            return false;
        }
    }
}