package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.Test;

import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop("domain.pl", "problem.pl");
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopWithInvalidArguments() {
        String[] args = {"domain.pl", "problem.pl", "abc"};
        new JSJshop(args);
    }

    @Test
    public void testJSJshopWithFileNotFoundException() {
        String[] args = {"nonExistentFile.pl", "problem.pl"};
        JSJshop jsJshop = null;
        try {
            jsJshop = new JSJshop(args);
        } catch (Exception e) {
            // expected
        }
        assertNull(jsJshop);
    }

    @Test
    public void testJSJshopWithParserError() {
        String[] args = {"parsingError.pl", "problem.pl"};
        JSJshop jsJshop = null;
        try {
            jsJshop = new JSJshop(args);
        } catch (JSParserError e) {
            // expected
        }
        assertNull(jsJshop);
    }

    @Test
    public void testJSJshopParserFile() {
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile("correctFile.pl"));
    }

    @Test(expected = IOException.class)
    public void testJSJshopParserFileWithIOException() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("ioErrorFile.pl");
    }

    @Test(expected = JSParserError.class)
    public void testJSJshopParserFileWithParserError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("parserErrorFile.pl");
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom("TestPredicate");
        JSListLogicalAtoms addList = new JSListLogicalAtoms();
        JSListLogicalAtoms deleteList = new JSListLogicalAtoms();
        JSJshopNode tree = new JSJshopNode(null, addList, deleteList);
        JSJshopNode solutionTree = new JSJshopNode(null, addList, deleteList);
        JSJshopNode expectedTree = new JSJshopNode(tree, new Vector<JSJshopNode>());
        JSPairPlanTSListNodes solution = new JSPairPlanTSListNodes(new JSPairPlanTState(new JSPlan(), new JSTate()), new Vector<JSJshopNode>());
        JSJshop jshop = jsJshop.setFile("correctFile.pl", pred);
        assertEquals(expectedTree, jshop.getTree());
        assertEquals(solution, jshop.getSolution());
        assertEquals(addList, jshop.getAddList());
        assertEquals(deleteList, jshop.getDeleteList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileWithNullFile() {
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom("TestPredicate");
        jsJshop.setFile(null, pred);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileWithNullPredicate() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile("correctFile.pl", null);
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader(".", "correctFile.pl");
        assertNotNull(reader);
    }

    @Test(expected = NullPointerException.class)
    public void testGetBufferedReaderWithNullApplet() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.getBufferedReader(null, "correctFile.pl");
    }
}