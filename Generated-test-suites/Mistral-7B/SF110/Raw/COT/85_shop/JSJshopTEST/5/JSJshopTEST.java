package umd.cs.shop.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

    private JSJshop jsShop;

    @Before
    public void setUp() {
        jsShop = new JSJshop();
    }

    @Test
    public void testJSJshopConstructor() {
        assertNotNull(jsShop.dom);
        assertNotNull(jsShop.probSet);
        assertNull(jsShop.tree);
        assertNull(jsShop.sol);
        assertNull(jsShop.solution);
    }

    @Test
    public void testJSJshopParserFile() {
        String domainFilePath = "path/to/domain/file.shp";
        String problemFilePath = "path/to/problem/file.shp";
        jsShop.parserFile(domainFilePath);
        jsShop.parserFile(problemFilePath);
        assertTrue(jsShop.probSet.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopParserFileInvalidFile() {
        String invalidFilePath = "path/to/invalid/file";
        jsShop.parserFile(invalidFilePath);
    }

    @Test
    public void testJSJshopSetFile() {
        String nameFile = "path/to/file.shp";
        JSTaskAtom pred = null; // Add the implementation of JSTaskAtom if needed
        JSJshopNode tree = jsShop.setFile(nameFile, pred);
        assertNotNull(tree);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopSetFileInvalidFile() {
        String invalidFilePath = "path/to/invalid/file";
        JSTaskAtom pred = null;
        jsShop.setFile(invalidFilePath, pred);
    }

    @Test
    public void testJSJshopGetTree() {
        String domainFilePath = "path/to/domain/file.shp";
        String problemFilePath = "path/to/problem/file.shp";
        JSJshopNode tree = jsShop.setFile(domainFilePath, null).getTree();
        assertNotNull(tree);
    }

    @Test
    public void testJSJshopGetSolution() {
        String domainFilePath = "path/to/domain/file.shp";
        String problemFilePath = "path/to/problem/file.shp";
        JSJshopNode tree = jsShop.setFile(domainFilePath, null).getTree();
        JSPairPlanTSListNodes solution = jsShop.getSolution();
        assertNotNull(solution);
    }

    @Test
    public void testJSJshopMain() {
        String domainFilePath = "path/to/domain/file.shp";
        String problemFilePath = "path/to/problem/file.shp";
        String[] args = {"path/to/domain/file.shp", "path/to/problem/file.shp"};
        JSJshop.main(args);
        assertTrue(JSJshopVars.allPlans); // Assuming JSJshopVars is accessible in the test
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopMainInvalidArgNumber() {
        String domainFilePath = "path/to/domain/file.shp";
        String problemFilePath = "path/to/problem/file.shp";
        String[] args = {"path/to/domain/file.shp", "path/to/problem/file.shp", "invalidArg"};
        JSJshop.main(args);
    }
}