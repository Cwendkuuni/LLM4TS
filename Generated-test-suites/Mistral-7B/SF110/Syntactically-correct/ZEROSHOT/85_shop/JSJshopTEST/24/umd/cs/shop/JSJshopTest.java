package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop("domain.shp", "problem.shp");
        assertNotNull(jsjshop.getTree());
        assertNotNull(jsjshop.getSolution());
        JSJshopNode tree = jsjshop.getTree();
        JSJshopNode node = tree.getChild(0);
        assertEquals("Expected first child name to be 'Action1'.", "Action1", node.getName());
        JSListLogicalAtoms addList = jsjshop.getAddList();
        JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
        assertNotNull(addList);
        assertNotNull(deleteList);
        JSListLogicalAtoms expectedAddList = new JSListLogicalAtoms();
        JSListLogicalAtoms expectedDeleteList = new JSListLogicalAtoms();
        // Add and delete lists verification here
        assertEquals(expectedAddList.size(), addList.size());
        assertEquals(expectedDeleteList.size(), deleteList.size());
        for (int i = 0; i < addList.size(); i++) {
            assertEquals(expectedAddList.get(i), addList.get(i));
        }
        for (int i = 0; i < deleteList.size(); i++) {
            assertEquals(expectedDeleteList.get(i), deleteList.get(i));
        }
    }

    @Test(expected = JSParserError.class)
    public void testParserError() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.parserFile("invalidFile.shp");
    }

    @Test
    public void testParser() {
        JSJshop jsjshop = new JSJshop();
        jsjshop.testParser();
    }
}