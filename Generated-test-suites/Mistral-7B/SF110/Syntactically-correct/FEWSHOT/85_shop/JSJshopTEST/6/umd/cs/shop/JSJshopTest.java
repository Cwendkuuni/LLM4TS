package umd.cs.shop;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListPlanningProblem;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

    @Test
    public void testJSJshopInstance() {
        JSJshop jsJshop = new JSJshop();
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
        JSJshop jsJshop1 = new JSJshop("domain.pl", "problem.pl");
        assertNotNull(jsJshop1.getTree());
        assertNotNull(jsJshop1.getSolution());
    }

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        try {
            jsJshop.parserFile("file.pl");
            Fail("parserFile should have thrown an exception");
        } catch (Exception e) {
            // Expected
        }

        JSUtil util = new JSUtil();
        util.initParseTable(new StreamTokenizer(new StringReader("")));
        JSJshop jsJshop1 = new JSJshop();
        jsJshop1.parserFile("test.pl");
        JSPlanningDomain dom = jsJshop1.dom();
        JSPlanningProblem prob = jsJshop1.prob();
        JSListLogicalAtoms addList = jsJshop1.getAddList();
        JSListLogicalAtoms deleteList = jsJshop1.getDeleteList();
        JSPlan sol = jsJshop1.sol();
        JSJshopNode tree = jsJshop1.tree();

        // Add checks for the parsed data here.
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        JSListPlanningProblem probSet = jsJshop.probSet;
        JSJshopNode tree = jsJshop.tree;
        JSPairPlanTSListNodes sol = jsJshop.solution;

        jsJshop.setFile("file.pl", new JSTaskAtom("p"));

        // Add checks for the set data here.
        assertEquals(probSet.size(), 1);
        assertNotNull(tree);
        assertNotNull(sol);
    }

    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.getTree();
        JSListLogicalAtoms addList = jsJshop.getAddList();

        // Add checks for the getAddList data here.
        assertNull(addList);

        JSJshop jsJshop1 = new JSJshop("domain.pl", "problem.pl");
        addList = jsJshop1.getAddList();
        // Add checks for the getAddList data here.
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.getTree();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();

        // Add checks for the getDeleteList data here.
        assertNull(deleteList);

        JSJshop jsJshop1 = new JSJshop("domain.pl", "problem.pl");
        deleteList = jsJshop1.getDeleteList();
        // Add checks for the getDeleteList data here.
        assertNotNull(deleteList);
    }

    @Test
    public void testSolve() {
        JSJshop jsJshop = new JSJshop();
        JSListPlanningProblem probSet = jsJshop.probSet;
        JSJshopNode tree = jsJshop.tree;
        JSPairPlanTSListNodes sol = jsJshop.solution;

        jsJshop.setFile("domain.pl", new JSTaskAtom("p"));

        // Add checks for the solution data here.

        JSJshop jsJshop1 = new JSJshop("domain.pl", "problem.pl");
        assertEquals(probSet.size(), 1);
        sol = jsJshop1.getSolution();
        // Add checks for the solution data here.
        assertNotNull(sol);
        JSPlan plan = sol.planS().plan();
        // Add checks for the plan data here.
        assertNotNull(plan);
    }

    @Test
    public void testMain() {
        try {
            JSJshop.main(new String[]{"", "", ""});
            Fail("main should have failed");
        } catch (Exception e) {
            // Expected
        }

        try {
            JSJshop.main(new String[]{"domain.pl", "problem.pl", "all"});
            JSJshop jsJshop = new JSJshop();
            JSListPlanningProblem probSet = jsJshop.probSet;
            JSJshopNode tree = jsJshop.tree;
            JSPairPlanTSListNodes sol = jsJshop.solution;

            JSJshop.main(new String[]{"domain.pl", "problem.pl", "one"});
            JSJshop jsJshop1 = new JSJshop();
            probSet = jsJshop1.probSet;
            tree = jsJshop1.tree;
            sol = jsJshop1.solution;

            // Add checks for the main data here.
            assertEquals(probSet.size(), 1);
            assertNotNull(tree);
            assertNotNull(sol);
        } catch (Exception e) {
            Fail("main should not have failed");
        }
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader("", "");
        assertNull(reader);

        JSJshop jsJshop1 = new JSJshop();
        BufferedReader reader1 = jsJshop1.getBufferedReader("file.pl", null);
        assertNotNull(reader1);

        JSApplet applet = new JFrame();
        BufferedReader reader2 = jsJshop1.getBufferedReader("file.pl", applet);
        assertNotNull(reader2);
    }

    @Test
    public void testGetAppletURL() {
        JSJshop jsJshop = new JSJshop();
        URL url = jsJshop.getAppletURL("file.pl", null);
        assertNull(url);

        JSApplet applet = new JFrame();
        URL url1 = jsJshop.getAppletURL("file.pl", applet);
        assertNotNull(url1);
    }
}