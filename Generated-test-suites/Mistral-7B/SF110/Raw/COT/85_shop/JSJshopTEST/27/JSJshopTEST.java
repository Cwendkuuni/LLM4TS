package umd.cs.shop.test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSTaskAtom;

public class JSJshopTest {

    

    @Test
    public void testJSJshopConstructor() {
        JSJshop jsJshop = new JSJshop();
        assertNotNull(jsJshop.dom);
        assertNull(jsJshop.tree);
        assertNull(jsJshop.prob);
        assertNull(jsJshop.sol);
        assertNull(jsJshop.solution);
    }

    @Test
    public void testJSJshopConstructorWithDomainAndProblemFiles() {
        String domainFileName = "domainFile.shp";
        String problemFileName = "problemFile.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.dom);
        assertNotNull(jsJshop.tree);
        assertNotNull(jsJshop.prob);
        assertNotNull(jsJshop.sol);
        assertNotNull(jsJshop.solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorWithInvalidArguments() {
        String[] args = {"args1", "args2", "args3"};
        JSJshop jsJshop = new JSJshop(args);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testJSJshopParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testJSJshopParserFile() {
        JSJshop jsJshop = new JSJshop();
        String file = "file.shp";
        jsJshop.parserFile(file);
    }

    @Test(expected = IOException.class)
    public void testJSJshopParserFileFailure() {
        JSJshop jsJshop = new JSJshop();
        String file = "nonexistentFile.shp";
        jsJshop.parserFile(file);
        fail("Should throw IOException");
    }

    @Test
    public void testJSJshopSetFile() {
        String file = "file.shp";
        JSJshop jsJshop = new JSJshop();
        JSTaskAtom pred = new JSTaskAtom("predicate");
        JSPairPlanTSListNodes solution = jsJshop.setFile(file, pred);
        assertNotNull(solution);
    }

    @Test
    public void testJSJshopGetTree() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
    }

    @Test
    public void testJSJshopGetSolution() {
        JSJshop jsJshop = new JSJshop();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
    }

    @Test
    public void testJSJshopGetAddList() {
        JSJshop jsJshop = new JSJshop();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testJSJshopGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testJSJshopMain() {
        String domainFileName = "domainFile.shp";
        String problemFileName = "problemFile.shp";
        String[] args = {domainFileName, problemFileName};
        JSJshop.main(args);
    }

    
}