package net.sf.sugar.fspath.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class FSPathExplorerTest {

    @Test
    public void testFSPathExplorer() {
        FSPathExplorer explorer = new FSPathExplorer();
        assertNull(explorer.rootPath);
        assertNull(explorer.fsPath);
        assertNull(explorer.prompt);
        assertNull(explorer.out);
        assertNull(explorer.err);
    }

    @Test
    public void testSetRootPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();

        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});

        assertEquals(rootPath, explorer.rootPath);
        assertEquals(FSPathFactory.newFSPath(rootPath), explorer.fsPath);
    }

    @Test
    public void testSetQuery() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();

        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[@name=test.txt]"});

        assertEquals(rootPath, explorer.rootPath);
        assertEquals(FSPathFactory.newFSPath(rootPath), explorer.fsPath);
        assertEquals("//file[@name=test.txt]", explorer.fsPathQuery);
    }

    @Test
    public void testCreateFSPath() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
        explorer.createFSPath();

        assertEquals(FSPathFactory.newFSPath(rootPath), explorer.fsPath);
    }

    @Test
    public void testStart() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[@name=test.txt]"});
        explorer.createFSPath();
        explorer.start();

        String expectedOutput = "/tmp/test.txt\n";
        String output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testCDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "cd /usr"});
        explorer.createFSPath();
        explorer.start();

        assertNull(explorer.fsPath);
        assertTrue(new File("/usr").isDirectory());
    }

    @Test
    public void testPWDCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath()});
        explorer.createFSPath();
        explorer.start();

        String expectedOutput = "/tmp\n";
        String output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);

        explorer.configureExplorer(new String[]{"-f", "cd /usr"});
        explorer.createFSPath();
        explorer.start();

        expectedOutput = "/usr\n";
        output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);

        explorer.configureExplorer(new String[]{"-f", "cd ../../"});
        explorer.createFSPath();
        explorer.start();

        expectedOutput = "/tmp\n";
        output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testHelpCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        explorer.start();

        String expectedOutput = "+-------------------------------------------------------+\n|                                                       |\n| FSPathExplorer                                        |\n|                                                       |\n+-------------------------------------------------------+\nType 'help' for commands...\n"
                + "Scanning filesystem metadata...\n"
                + "+-------------------------------------------------------+\n| PLEASE NOTE running FSPathExplorer from the top level                 |\n| of your filesystem is not advised                        |\n+-------------------------------------------------------+\n";
        String output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testFindCommand() throws IOException {
        FSPathExplorer explorer = new FSPathExplorer();
        File rootPath = new File("/tmp");
        explorer.configureExplorer(new String[]{"-p", rootPath.getAbsolutePath(), "-f", "//file[@name=test.txt]"});
        explorer.createFSPath();
        explorer.start();

        String expectedOutput = "/tmp/test.txt\n";
        String output = "";
        try {
            output = readOutputFromTestConsole();
        } finally {
            explorer.prompt.close();
        }

        assertEquals(expectedOutput, output);
    }

    private String readOutputFromTestConsole() {
        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);
        pb.directory(new File("/tmp"));
        Process p = pb.start(new Class[]{FSPathExplorer.class});

        byte[] buffer = new byte[1024];
        int bytesRead;
        String output = "";
        try {
            while ((bytesRead = p.getInputStream().read(buffer)) > 0) {
                output += new String(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            p.destroy();
        }

        return output;
    }
}