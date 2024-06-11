package nu.staldal.lagoon.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.LagoonCLI;
import nu.staldal.lagoon.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.Property;
import nu.staldal.xtree.TreeBuilder;

public class LagoonCLITest {

    @Test
    public void testInvalidSyntax() throws Exception {
        String[] args = new String[0];
        LagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagoon.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = System.out.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidArgument() throws Exception {
        String[] args = new String[]{"invalid_argument"};
        Properties properties = new Properties();
        LagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagoon.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = System.out.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidInterval() throws Exception {
        String[] args = new String[]{"property_file", "-1"};
        LagoonCLI.main(args);
        String expected = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.stagoon.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        String actual = System.out.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidSyntaxWithPropertyFile() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        String expected = "Initializing Lagoon...";
        String actual = System.out.getHistory(0).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testValidSyntaxWithSitemapFile() throws Exception {
        String[] args = new String[]{"sitemap_file.xml"};
        LagoonCLI.main(args);
        String expected = "Initializing Lagoon...";
        String actual = System.out.getHistory(0).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testInvalidFile() throws Exception {
        String[] args = new String[]{"invalid_file"};
        LagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nFile not found: invalid_file";
        String actual = System.err.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAuthenticationMissingException() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        LagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nPassword is required but not specified";
        String actual = System.err.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testAuthenticationException() throws Exception {
        String[] args = new String[]{"property_file.xml", "incorrect_password"};
        LagoonCLI.main(args);
        String expected = "Error while initializing Lagoon:\nIncorrect password";
        String actual = System.err.getHints().get("msg").toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testBuild() throws Exception {
        String[] args = new String[]{"property_file.xml", "build"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        String expected = "Building website...";
        String actual = System.out.getHistory(1).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testForceBuild() throws Exception {
        String[] args = new String[]{"property_file.xml", "force"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        String expected = "Force building website...";
        String actual = System.out.getHistory(1).toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testInterval() throws Exception {
        String[] args = new String[]{"property_file.xml", "1000"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        Thread.sleep(1100);
        String expected = "Building website...";
        String actual = System.out.getHistory(1).toString();
        assertEquals(expected, actual);
        LagoonCLI.main(args);
        Thread.sleep(900);
        String expected2 = "Building website...";
        String actual2 = System.out.getHistory(1).toString();
        assertEquals(expected2, expected2);
    }

    @Test
    public void testCommandLineInput() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        String expected = "Ready";
        String actual = System.out.getHistory(1).toString();
        assertEquals(expected, actual);
        String command = "b";
        System.out.println(command);
        System.in = new ByteArrayInputStream(command.getBytes());
        LagoonCLI.main(args);
        String expected2 = "Building website...";
        String actual2 = System.out.getHistory(1).toString();
        assertEquals(expected2, expected2);
    }

    @Test
    public void testThreads() throws Exception {
        String[] args = new String[]{"property_file.xml"};
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream("property_file.xml");
        properties.load(fis);
        fis.close();
        LagoonCLI.properties = properties;
        LagoonCLI.main(args);
        String command = "b";
        System.out.println(command);
        System.in = new ByteArrayInputStream(command.getBytes());
        LagoonCLI.main(args);
        LagoonCLI.main(args);
        LagoonCLI.main(args);
        Thread.sleep(1000);
        LagoonProcessor processor = new LagoonProcessor(null, null, null, null, null, null);
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate(threads);
        int count = 0;
        for (Thread thread : threads) {
            if (thread != null && thread != Thread.currentThread()) {
                count++;
                System.out.println("Thread still running: " + thread);
            }
        }
        assertEquals(3, count);
    }
}