package umd.cs.shop;

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
    public void testEmptyConstruction() {
        JSJshop jsShop = new JSJshop();
        assertNull(jsShop.getTree());
        assertNull(jsShop.getSolution());
        assertNull(jsShop.getAddList());
        assertNull(jsShop.getDeleteList());
    }

    @Test
    public void testDomainAndProblemConstruction() {
        JSJshop jsShop = new JSJshop("domain.shp", "problem.shp");
        assertNotNull(jsShop.getTree());
        assertNotNull(jsShop.getSolution());
        assertNotNull(jsShop.getAddList());
        assertNotNull(jsShop.getDeleteList());
        JSPlanningDomain domain = jsShop.dom();
        JSPlanningProblem problem = jsShop.prob();
        assertEquals(domain.getName(), jsShop.getTree().getName());
        assertEquals(problem.getName(), jsShop.getSolution().planS().tState().name);
    }

    @Test
    public void testFileAndPredicateConstruction() {
        JSJshop jsShop = new JSJshop("file.shp", new JSTaskAtom("p"));
        assertNotNull(jsShop.getTree());
        assertNotNull(jsShop.getSolution());
        assertNotNull(jsShop.getAddList());
        assertNotNull(jsShop.getDeleteList());
    }

    @Test
    public void testSettingFileAndPredicate() {
        JSJshop jsShop = new JSJshop();
        JSJshopNode tree = new JSJshopNode(null, new Vector<>());
        JSJshopNode node = new JSJshopNode(null, new Vector<>());
        node.setName("testNode");
        jsShop.setFile("file.shp", new JSTaskAtom("p"));
        assertEquals(tree, jsShop.getTree());
        JSJshopNode newTree = jsShop.getTree();
        assertEquals(newTree.size(), 1);
        assertEquals(newTree.elementAt(0).getName(), node.getName());
    }

    @Test
    public void testParsingFile() {
        JSJshop jsShop = new JSJshop();
        jsShop.parserFile("file.shp");
        JSJshopNode tree = jsShop.getTree();
        JSListLogicalAtoms addList = jsShop.getAddList();
        JSListLogicalAtoms deleteList = jsShop.getDeleteList();
        assertNotNull(tree);
        assertNotNull(addList);
        assertNotNull(deleteList);
        assertEquals(tree.size(), 1);
        assertEquals(addList.size(), 0);
        assertEquals(deleteList.size(), 0);
    }

    @Test(expected = JSParserError.class)
    public void testParsingFileWithError() {
        JSJshop jsShop = new JSJshop();
        jsShop.parserFile("file_with_error.shp");
    }

    @Test
    public void testGettingBufferedReader() {
        JSJshop jsShop = new JSJshop();
        BufferedReader reader = jsShop.getBufferedReader(".", "file.txt");
        assertNotNull(reader);
        reader.close();
    }

    @Test
    public void testGettingAppletURL() {
        JApplet applet = new JFrame();
        BufferedReader reader = jsShop.getBufferedReader(null, "file.txt", applet);
        assertNotNull(reader);
        reader.close();
    }

    @Test
    public void testGettingAppletURLNullFile() {
        JApplet applet = new JFrame();
        BufferedReader reader = jsShop.getBufferedReader(null, null, applet);
        assertNull(reader);
    }
}