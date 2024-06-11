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
    public void testMain() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        JSJshopNode tree = jsJshop.getTree();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        assertNotNull(tree);
        assertNotNull(solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArguments() {
        String[] args = {"testDomain.shp", "testProblem.shp", "invalidArg"};
        JSJshop jsJshop = new JSJshop(args);
    }

    @Test
    public void testSetFile() {
        String fileName = "testFile.shp";
        String pred = null;
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile(fileName, pred);
        assertNotNull(tree);
    }

    @Test
    public void testGetAddListAndDeleteList() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        JSJshopNode tree = jsJshop.getTree();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(addList);
        assertNotNull(deleteList);
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testParserFile() {
        String libraryFile = "testLibrary.shp";
        JSJshop jsJshop = new JSJshop();
        boolean result = jsJshop.parserFile(libraryFile);
        assertTrue(result);
    }

    @Test
    public void testGetBufferedReader() {
        String dir = "src";
        String file = "testFile.shp";
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader(dir, file);
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        String file = "testFile.shp";
        URL url = JSJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        String input = "( defdomain testDomain \n" +
                "(:domain-name testDomain) \n" +
                "(:requirements ( require (intervals) )) \n" +
                "(:types \n" +
                "(:type :type1 \n" +
                "(:properties ( numberic ) ) \n" +
                "(:predicates ( :action action1 :init init1 :test test1 ) ) \n" +
                "(:function-names ( action1 init1 test1 ) ) \n" +
                "(:duration 1.0) \n" +
                ")) \n" +
                ")";
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(input));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        JSPlanningDomain domain = jsJshop.dom();
        assertEquals("testDomain", domain.getName());
    }
}