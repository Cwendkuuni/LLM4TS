package umd.cs.shop.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSUtil;

public class JSJshopTest {

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop("domain.shp", "problem.shp");
        JSPlanningDomain dom = jsJshop.dom();
        JSPlanningProblem prob = jsJshop.prob();
        JSPlan sol = jsJshop.sol();
        JSJshopNode tree = jsJshop.tree();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();

        // Check if domain and problem are initialized correctly
        assertNotNull(dom);
        assertNotNull(prob);

        // Check if sol and solution are initialized correctly
        assertNotNull(sol);
        assertNotNull(solution);
        assertEquals(sol, solution.planS().plan());

        // Check if tree is initialized correctly
        assertNotNull(tree);

        // Check if addList and deleteList are initialized correctly
        assertNotNull(addList);
        assertNotNull(deleteList);

        // Check if the size of addList and deleteList are as expected
        int size = 0;
        if (addList != null) {
            size += addList.size();
        }
        if (deleteList != null) {
            size += deleteList.size();
        }
        assertEquals(size, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationWithInvalidArguments() {
        new JSJshop();
    }

    @Test(expected = NumberFormatException.class)
    public void testInitializationWithInvalidIntegerArgument() {
        new JSJshop("args.txt", "2ab");
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        String filePath = "testFile.txt";
        BufferedReader reader = jsJshop.getBufferedReader(".", filePath);
        assertNotNull(reader);

        try {
            String line = reader.readLine();
            assertNotNull(line);
            reader.close();
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetBufferedReaderWithNullFile() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader(null, "testFile.txt");
        assertNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL("testFile.txt", applet);
        assertNotNull(url);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAppletURLWithNullApplet() {
        URL url = JSJshop.getAppletURL(null, new JFrame());
        assertNull(url);
    }

    @Test
    public void testProcessToken() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain domain.shp"));
        jsJshop.processToken(tokenizer);
        JSPlanningDomain domain = jsJshop.dom();
        assertNotNull(domain);
    }
}