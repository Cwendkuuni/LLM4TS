package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop("testDomain.shp", "testProblem.shp");
        assertNotNull(jsjshop);
        assertEquals(0, jsjshop.getAddList().size());
        assertEquals(0, jsjshop.getDeleteList().size());
        assertNull(jsjshop.getSolution());
        assertNull(jsjshop.getTree());
    }

    @Test
    public void testJSJshopSolveOnePlan() {
        JSJshop jsjshop = new JSJshop("testDomain.shp", "testProblem.shp");
        jsjshop.setFile("testSolution.shp", null);
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();

        assertNotNull(tree);
        assertNotNull(solution);
        assertEquals(1, solution.size());
        JSPlan sol = solution.planS().plan();
        assertFalse(sol.isFailure());
        assertEquals(1, sol.size());
    }

    @Test
    public void testJSJshopSolveAllPlans() {
        JSJshop jsjshop = new JSJshop("testDomain.shp", "testProblem.shp");
        jsjshop.setFile("testDomain.shp", new JSTaskAtom("p"));
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();

        assertNotNull(tree);
        assertNotNull(solution);
        assertEquals(1, solution.size());
        JSPlan sol = solution.planS().plan();
        assertFalse(sol.isFailure());
        assertEquals(1, sol.size());

        jsjshop.setFile("testDomain.shp", new JSTaskAtom("q"));
        solution = jsjshop.getSolution();

        assertNotNull(solution);
        assertTrue(solution.size() > 1);
    }

    @Test
    public void testParser() throws IOException {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }

    @Test(expected = JSParserError.class)
    public void testParserError() throws IOException {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
        throw new JSParserError();
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsjshop = new JSJshop();
        BufferedReader reader = jsjshop.getBufferedReader(".", "testFile.txt");
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL("testFile.txt", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain testDomain"));
        JSJshop jsjshop = new JSJshop();
        jsjshop.processToken(tokenizer);
        assertEquals("testDomain", jsjshop.dom().getName());
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain testDomain"));
        JSJshop jsjshop = new JSJshop();
        jsjshop.processToken(tokenizer);
        throw new JSParserError();
    }
}