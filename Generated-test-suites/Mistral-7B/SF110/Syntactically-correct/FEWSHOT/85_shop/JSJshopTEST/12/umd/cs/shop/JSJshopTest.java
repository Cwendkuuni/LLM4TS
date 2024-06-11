package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        assert(jsJshop.parserFile("testFile.shp"));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFileWithError.shp");
    }

    @Test
    public void testSetFile() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile("testFile.shp", null).getTree();
        assert(tree != null);
        JSJshopNode node = tree.getChild(0);
        assertEquals("predicate1", node.getLabel());
        JSJshopNode node2 = tree.getChild(1);
        assertEquals("predicate2", node2.getLabel());
    }

    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile("testFile.shp", null).getTree();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertEquals(2, addList.size());
        JSLogicalAtom atom1 = addList.get(0);
        JSLogicalAtom atom2 = addList.get(1);
        assertEquals("p(a)", atom1.getAtomString());
        assertEquals("p(b)", atom2.getAtomString());
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile("testFile.shp", null).getTree();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertEquals(0, deleteList.size());
    }

    @Test
    public void testMainWithValidArguments() {
        String[] args = new String[]{"domain.shp", "problem.shp"};
        JSJshop.main(args);
    }

    @Test
    public void testMainWithInvalidArguments() {
        String[] args = new String[]{"domain.shp", "problem.shp", "three"};
        JSJshop jsJshop = new JSJshop();
        try {
            jsJshop.main(args);
            fail();
        } catch (Exception e) {
            // Expected
        }
    }
}