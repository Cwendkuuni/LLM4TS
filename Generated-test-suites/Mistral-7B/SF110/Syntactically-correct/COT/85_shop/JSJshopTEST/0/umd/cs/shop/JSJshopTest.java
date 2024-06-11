package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

    private JSJshop jsJshop;

    @Before
    public void setUp() {
        jsJshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args);
        assertTrue(jsJshop.getTree() != null);
        assertTrue(jsJshop.getSolution() != null);
    }

    @Test(expected = RuntimeException.class)
    public void testMainInvalidUsage() {
        String[] args = {"domainDef-file-name", "problemDef-file-name", "invalid-argument"};
        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        String nameDomainFile = "domain.shp";
        String nameProblemFile = "problem.shp";
        jsJshop = new JSJshop(nameDomainFile, nameProblemFile);
        assertTrue(jsJshop.getTree() != null);
        assertTrue(jsJshop.getSolution() != null);
    }

    @Test(expected = RuntimeException.class)
    public void testJSJshopInvalidFile() {
        String nameDomainFile = "invalid-file.shp";
        String nameProblemFile = "problem.shp";
        jsJshop = new JSJshop(nameDomainFile, nameProblemFile);
    }

    @Test
    public void testParser() {
        JSJshop.testParser();
    }

    @Test(expected = IOException.class)
    public void testParserInvalidFile() {
        JSJshop.testParser();
        JSUtil.setFile("invalid-file.shp", null);
    }

    @Test
    public void testSetFile() {
        String nameFile = "file.shp";
        JSTaskAtom pred = null;
        JSJshopNode tree = jsJshop.setFile(nameFile, pred);
        assertNotNull(tree);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileNullPredicate() {
        String nameFile = "file.shp";
        JSJshopNode tree = jsJshop.setFile(nameFile, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetBufferedReaderFileNotFound() {
        String dir = ".";
        String file = "invalid-file.txt";
        jsJshop.getBufferedReader(dir, file, null);
    }

    @Test
    public void testGetBufferedReader() {
        String dir = ".";
        String file = "valid-file.txt";
        BufferedReader reader = jsJshop.getBufferedReader(dir, file, null);
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "valid-file.html";
        URL url = jsJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetAppletURLInvalidURL() {
        JApplet applet = new JFrame();
        String file = "invalid-url.html";
        JSJshop.getAppletURL(file, applet);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain mydomain (:domain-name mydomain) (:objects (a b c))))"));
        JSJshop.processToken(tokenizer);
        JSPlanningDomain domain = jsJshop.dom();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertEquals(3, addList.size());
        assertEquals(3, deleteList.size());
        assertEquals("a", addList.get(0).toString());
        assertEquals("b", addList.get(1).toString());
        assertEquals("c", addList.get(2).toString());
        assertEquals("a", deleteList.get(0).toString());
        assertEquals("b", deleteList.get(1).toString());
        assertEquals("c", deleteList.get(2).toString());
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenInvalidSyntax() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain mydomain (:domain-name mydomain) (:objects (a b c)))) ( nonsense)"));
        JSJshop.processToken(tokenizer);
    }

    @Test
    public void testDom() {
        String nameFile = "domain.shp";
        JSPlanningDomain domain = jsJshop.dom();
        assertNotNull(domain);
    }

    @Test
    public void testProb() {
        String nameFile = "problem.shp";
        JSPlanningProblem problem = jsJshop.prob();
        assertNotNull(problem);
    }

    @Test
    public void testSol() {
        String nameFile = "problem.shp";
        JSPlan plan = jsJshop.sol();
        assertNotNull(plan);
    }

    @Test
    public void testTree() {
        String nameFile = "solution.shp";
        JSJshopNode tree = jsJshop.tree();
        assertNotNull(tree);
    }
}