package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        String libraryFile = "path/to/library.shp"; // Replace with the path to your library file
        assertTrue(jsJshop.parserFile(libraryFile));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithError() {
        JSJshop jsJshop = new JSJshop();
        String libraryFile = "path/to/error_file.shp"; // Replace with the path to an error file
        jsJshop.parserFile(libraryFile);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        String dir = "path/to/directory/"; // Replace with the path to your directory
        String file = "file.shp"; // Replace with the name of your file
        BufferedReader br = jsJshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testGetBufferedReaderWithApplet() {
        JSJshop jsJshop = new JSJshop();
        String file = "file.shp"; // Replace with the name of your file
        JApplet applet = new JApplet() {
            // Add the necessary implementation for JApplet here
        };
        BufferedReader br = jsJshop.getBufferedReader(null, file, applet);
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsJshop = new JSJshop();
        String file = "file.shp"; // Replace with the name of your file
        JApplet applet = new JApplet() {
            // Add the necessary implementation for JApplet here
        };
        URL url = jsJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain mydomain"));
        jsJshop.processToken(tokenizer);
        assertEquals("mydomain", jsJshop.dom().getName());
    }

    @Test
    public void testParseDomain() {
        JSJshop jsJshop = new JSJshop();
        String libraryFile = "path/to/library.shp"; // Replace with the path to your library file
        jsJshop.parserFile(libraryFile);
        JSPlanningDomain domain = jsJshop.dom();
        assertNotNull(domain);
    }

    @Test
    public void testParseProblem() {
        JSJshop jsJshop = new JSJshop();
        String libraryFile = "path/to/library.shp"; // Replace with the path to your library file
        jsJshop.parserFile(libraryFile);
        JSPlanningProblem problem = jsJshop.prob();
        assertNotNull(problem);
    }

    @Test
    public void testSolveProblem() {
        JSJshop jsJshop = new JSJshop();
        String domainFile = "path/to/domain.shp"; // Replace with the path to your domain file
        String problemFile = "path/to/problem.shp"; // Replace with the path to your problem file
        jsJshop.new JSJshop(domainFile, problemFile);
        List<JSPairPlanTSListNodes> solutions = jsJshop.getSolution().planS().getPlanList();
        assertFalse(solutions.isEmpty());
    }

    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop();
        String domainFile = "path/to/domain.shp"; // Replace with the path to your domain file
        String problemFile = "path/to/problem.shp"; // Replace with the path to your problem file
        jsJshop.new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        String domainFile = "path/to/domain.shp"; // Replace with the path to your domain file
        String problemFile = "path/to/problem.shp"; // Replace with the path to your problem file
        jsJshop.new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testGetTree() {
        JSJshop jsJshop = new JSJshop();
        String domainFile = "path/to/domain.shp"; // Replace with the path to your domain file
        String problemFile = "path/to/problem.shp"; // Replace with the path to your problem file
        jsJshop.new JSJshop(domainFile, problemFile);
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
    }

    @Test
    public void testGetSolution() {
        JSJshop jsJshop = new JSJshop();
        String domainFile = "path/to/domain.shp"; // Replace with the path to your domain file
        String problemFile = "path/to/problem.shp"; // Replace with the path to your problem file
        jsJshop.new JSJshop(domainFile, problemFile);
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
    }
}