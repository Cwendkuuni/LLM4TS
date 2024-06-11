package test;

import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.*;

import static org.junit.Assert.*;

public class JSJshopTest {

    private JSJshop jsJshop;

    @Before
    public void setUp() {
        jsJshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"domain.shp", "problem.shp"};
        JSJshop.main(args);
        assertTrue(jsJshop.probSet.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgs() {
        String[] args = new String[]{"domain.shp", "problem.shp", "invalidArg"};
        JSJshop.main(args);
    }

    @Test
    public void testJSJshop() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        assertEquals(domainFile, jsJshop.dom.getName());
        assertEquals(problemFile, jsJshop.prob.getName());
    }

    @Test
    public void testJSJshopNull() {
        String domainFile = null;
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        try {
            new JSJshop(domainFile, problemFile);
            fail();
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testGetTree() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
    }

    @Test
    public void testGetSolution() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
        assertTrue(solution.size() > 0);
    }

    @Test
    public void testGetAddList() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
        assertTrue(addList.size() > 0);
    }

    @Test
    public void testGetDeleteList() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String[] args = new String[]{"domainFile", "problemFile"};
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
        assertTrue(deleteList.size() > 0);
    }

    @Test
    public void testSetFile() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        String file = "testFile.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        JSJshop jsJshop = new JSJshop();
        JSPairPlanTSListNodes pair = jsJshop.setFile(file, pred);
        assertNotNull(pair);
        assertTrue(pair.size() > 0);
    }

    @Test
    public void testTestParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testParserFile() {
        String libraryFile = "testFile.shp";
        assertTrue(jsJshop.parserFile(libraryFile));
    }

    @Test
    public void testGetBufferedReader() {
        String dir = ".";
        String file = "testFile.shp";
        BufferedReader br = jsJshop.getBufferedReader(dir, file);
        assertNotNull(br);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "testFile.shp";
        URL url = jsJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }
}