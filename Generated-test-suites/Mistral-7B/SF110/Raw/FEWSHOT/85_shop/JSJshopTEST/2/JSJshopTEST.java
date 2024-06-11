package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSPairPlanTSListNodes;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

    @Test
    public void testJSJshopInstance() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.probSet);
        assertNull(jsjshop.tree);
        assertNull(jsjshop.sol);
        JSJshop jsjshop1 = new JSJshop("domainFile.shp", "problemFile.shp");
        assertNotNull(jsjshop1.probSet);
        assertNull(jsjshop1.tree);
        assertNull(jsjshop1.sol);
    }

    @Test
    public void testJSJshopReadFiles() {
        JSJshop jsjshop = new JSJshop("domainFile.shp", "problemFile.shp");
        JSPlanningDomain dom = jsjshop.dom();
        JSPlanningProblem prob = jsjshop.prob();
        assertNotNull(dom);
        assertNotNull(prob);
        // Add more assertions to check the contents of dom and prob
    }

    @Test
    public void testJSJshopGetSolution() {
        JSJshop jsjshop = new JSJshop("domainFile.shp", "problemFile.shp");
        JSPairPlanTSListNodes solution = jsjshop.getSolution();
        assertNotNull(solution);
        JSPlan plan = solution.planS().plan();
        assertNotNull(plan);
        // Add more assertions to check the solution
    }

    @Test
    public void testJSJshopGetAddListAndDeleteList() {
        JSJshop jsjshop = new JSJshop("domainFile.shp", "problemFile.shp");
        JSListLogicalAtoms addList = jsjshop.getAddList();
        assertNotNull(addList);
        // Add more assertions to check the addList
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNotNull(deleteList);
        // Add more assertions to check the deleteList
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopInvalidArguments() {
        String[] args = {"domainFile.shp"};
        JSJshop jsjshop = new JSJshop(args);
        fail("Should throw an IllegalArgumentException with the message: 'Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]'");
    }

    @Test(expected = NumberFormatException.class)
    public void testJSJshopInvalidNumberFormat() {
        String[] args = {"domainFile.shp", "problemFile.shp", "3a"};
        JSJshop jsjshop = new JSJshop(args);
        fail("Should throw a NumberFormatException");
    }

    // Add more test methods for other functionalities in JSJshop class
}