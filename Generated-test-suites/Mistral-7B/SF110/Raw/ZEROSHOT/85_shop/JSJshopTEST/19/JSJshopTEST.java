package test;

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop();
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
        assertNull(jsJshop.getAddList());
        assertNull(jsJshop.getDeleteList());

        JSJshop jsJshop1 = new JSJshop("domain.shp", "problem.shp");
        assertNotNull(jsJshop1.getTree());
        assertNotNull(jsJshop1.getSolution());
        assertNotNull(jsJshop1.getAddList());
        assertNotNull(jsJshop1.getDeleteList());

        JSJshop jsJshop2 = new JSJshop("domain.shp", "problem.shp", "all");
        JSJshopNode tree2 = jsJshop2.getTree();
        JSPairPlanTSListNodes solution2 = jsJshop2.getSolution();
        JSListLogicalAtoms addList2 = jsJshop2.getAddList();
        JSListLogicalAtoms deleteList2 = jsJshop2.getDeleteList();
        int sizeAddList2 = addList2.size();
        int sizeDeleteList2 = deleteList2.size();

        assertEquals(sizeAddList2, sizeDeleteList2);
        assertEquals(sizeAddList2 + sizeDeleteList2, tree2.getChildren().size());

        JSJshop jsJshop3 = new JSJshop("domain.shp", new JSTaskAtom("p"));
        JSJshopNode tree3 = jsJshop3.getTree();

        // Add more test cases here
    }

    // Add more test methods here if needed
}