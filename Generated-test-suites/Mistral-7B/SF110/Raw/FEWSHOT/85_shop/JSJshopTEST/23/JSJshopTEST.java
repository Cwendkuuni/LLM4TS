package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop("domainFile.shp", "problemFile.shp");
        assertNotNull(jsjshop.getTree());
        assertNotNull(jsjshop.getSolution());
        JSJshopNode tree = jsjshop.getTree();
        JSPlan sol = jsjshop.getSolution().planS().plan();
        assertEquals(sol.toString(), "InitialState\n" +
                "  Time 0 :\n" +
                "    Achieving(x, a)\n" +
                "    Precondition(x, a)\n" +
                "  Time 1 :\n" +
                "    Achieving(x, b)\n" +
                "    Precondition(x, b)\n" +
                "  Time 2 :\n" +
                "    Achieving(x, c)\n" +
                "    Precondition(x, c)\n" +
                "  Time 3 :\n" +
                "    Achieving(x, goal)\n" +
                "    Precondition(x, goal)\n");
        JSListLogicalAtoms addList = jsjshop.getAddList();
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNotNull(addList);
        assertNotNull(deleteList);
        assertEquals(addList.toString(), "[Achieving(x, a), Achieving(x, b), Achieving(x, c), Achieving(x, goal)]");
        assertEquals(deleteList.toString(), "[Precondition(x, a), Precondition(x, b), Precondition(x, c), Precondition(x, goal)]");
    }

    @Test
    public void testJSJshopWithFile() {
        JSJshop jsjshop = new JSJshop("testFile.shp");
        assertNotNull(jsjshop.getTree());
        assertNotNull(jsjshop.getSolution());
        JSJshopNode tree = jsjshop.getTree();
        JSPlan sol = jsjshop.getSolution().planS().plan();
        // Print the plan, add list, delete list and tree for debugging
        System.out.println("Plan:");
        sol.printPlan();
        System.out.println("Add List:");
        JSListLogicalAtoms addList = jsjshop.getAddList();
        addList.print();
        System.out.println("Delete List:");
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        deleteList.print();
        System.out.println("Tree:");
        tree.print();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopWithInvalidArguments() {
        String[] args = {"domainFile.shp", "problemFile.shp", "three"};
        new JSJshop(args);
    }
}