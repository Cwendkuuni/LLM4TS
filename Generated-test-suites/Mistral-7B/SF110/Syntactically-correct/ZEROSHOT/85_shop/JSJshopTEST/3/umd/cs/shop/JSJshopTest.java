package umd.cs.shop.test;

import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

 @Test
 public void testJSJshop() {
 JSJshop jsjshop = new JSJshop();
 JSPlanningDomain dom = jsjshop.dom();
 JSPlanningProblem prob = jsjshop.prob();
 JSPlan sol = jsjshop.sol();
 JSJshopNode tree = jsjshop.tree();
 JSListLogicalAtoms addList = jsjshop.getAddList();
 JSListLogicalAtoms deleteList = jsjshop.getDeleteList();
 JSJshopNode expectedTree = new JSJshopNode(null, new Vector<>());

 assertTrue(jsjshop instanceof JSJshop);
 assertNull(jsjshop.getTree());
 assertNull(jsjshop.getSolution());
 assertNull(jsjshop.getAddList());
 assertNull(jsjshop.getDeleteList());

 // Test empty constructor
 jsjshop = new JSJshop();
 assertNull(jsjshop.getTree());
 assertNull(jsjshop.getSolution());
 assertNull(jsjshop.getAddList());
 assertNull(jsjshop.getDeleteList());

 // Test constructor with domain and problem files
 jsjshop = new JSJshop("domain.shp", "problem.shp");
 assertNotNull(jsjshop.getTree());
 assertNotNull(jsjshop.getSolution());
 assertNotNull(jsjshop.getAddList());
 assertNotNull(jsjshop.getDeleteList());
 assertEquals(dom, jsjshop.dom());
 assertEquals(prob, jsjshop.prob());
 assertEquals(sol, jsjshop.sol());
 assertEquals(tree, jsjshop.tree());
 assertEquals(addList, jsjshop.getAddList());
 assertEquals(deleteList, jsjshop.getDeleteList());

 // Test constructor with file and predicate
 jsjshop = new JSJshop("file.shp", new JSTaskAtom("p"));
 assertNotNull(jsjshop.getTree());
 assertNotNull(jsjshop.getSolution());
 assertNotNull(jsjshop.getAddList());
 assertNotNull(jsjshop.getDeleteList());

 // Test getters
 assertEquals(dom, jsjshop.dom());
 assertEquals(prob, jsjshop.prob());
 assertEquals(sol, jsjshop.sol());
 assertEquals(tree, jsjshop.tree());
 assertEquals(addList, jsjshop.getAddList());
 assertEquals(deleteList, jsjshop.getDeleteList());

 // Test setFile method
 jsjshop.setFile("file.shp", new JSTaskAtom("p"));
 JSJshopNode expectedTreeNode = new JSJshopNode(new JSJshopNode(null, new Vector<>()), new Vector<>());
 expectedTree.setParent(null);
 expectedTree.setChildren(new Vector<>());
 expectedTreeNode.setParent(expectedTree);
 expectedTreeNode.setChildren(new Vector<>());
 assertEquals(expectedTreeNode, tree.getChildren().elementAt(0));
 assertEquals(expectedTree, tree);

 // Test getTree method
 assertEquals(tree, jsjshop.tree());

 // Test getSolution method
 assertEquals(sol, jsjshop.getSolution());

 // Test getAddList method
 assertEquals(addList, jsjshop.getAddList());

 // Test getDeleteList method
 assertEquals(deleteList, jsjshop.getDeleteList());
 }

}