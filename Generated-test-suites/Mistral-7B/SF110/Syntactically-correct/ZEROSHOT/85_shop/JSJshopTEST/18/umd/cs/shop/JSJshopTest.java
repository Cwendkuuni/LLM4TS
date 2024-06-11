import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
        
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
        
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
        
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);

        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    @Test
    public void testSetFile() {
        String nameFile = "testFile.shp";
        JSTaskAtom pred = new JSTaskAtom();
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile(nameFile, pred);
        
        JSJshopNode tree = jsJshop.getTree();
        assertNotNull(tree);
        
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(solution);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTreeWhenJSJshopIsNull() {
        JSJshop jsJshop = null;
        jsJshop.getTree();
    }

    @Test(expected = NullPointerException.class)
    public void testGetSolutionWhenJSJshopIsNull() {
        JSJshop jsJshop = null;
        jsJshop.getSolution();
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }
}