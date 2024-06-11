package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSListPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSTaskAtom;
import umd.cs.shop.JSUtil;

public class JSJshopTest {
    private JSJshop jsJshop;

    @Before
    public void setUp() {
        jsJshop = new JSJshop();
    }

    

    @Test
    public void testJSJshopConstructor() {
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorWrongNumberOfArguments() {
        new JSJshop(); // Should throw an IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorInvalidArguments() {
        String[] args = {"domain.shp", "problem.shp", "invalid", "number"};
        new JSJshop(args); // Should throw an IllegalArgumentException
    }

    @Test
    public void testJSJshopParserFile() {
        String fileName = "test.shp";
        jsJshop.parserFile(fileName);
        assertTrue(jsJshop.dom != null);
        assertTrue(jsJshop.probSet.size() > 0);
    }

    @Test(expected = FileNotFoundException.class)
    public void testJSJshopParserFileNotFound() {
        String fileName = "nonexistent.shp";
        jsJshop.parserFile(fileName);
    }

    @Test
    public void testJSJshopSetFile() {
        String fileName = "test.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        JSPairPlanTSListNodes solution = jsJshop.setFile(fileName, pred);
        assertNotNull(solution);
        assertEquals(1, solution.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopSetFileNullFile() {
        String fileName = null;
        jsJshop.setFile(fileName, new JSTaskAtom("testPredicate"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopSetFileNullPredicate() {
        String fileName = "test.shp";
        jsJshop.setFile(fileName, null);
    }

    @Test
    public void testJSJshopGetAddList() {
        String fileName = "test.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        jsJshop.setFile(fileName, pred);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
        assertEquals(0, addList.size());
    }

    @Test
    public void testJSJshopGetDeleteList() {
        String fileName = "test.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        jsJshop.setFile(fileName, pred);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
        assertEquals(0, deleteList.size());
    }

    @Test
    public void testJSJshopGetTree() {
        String fileName = "test.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        jsJshop.setFile(fileName, pred);
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
        assertTrue(tree.isLeaf());
    }

    @Test
    public void testJSJshopGetSolution() {
        String fileName = "test.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        jsJshop.setFile(fileName, pred);
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
        assertEquals(1, solution.size());
    }

    @Test
    public void testJSJshopTestParser() {
        jsJshop.testParser();
    }

    
}