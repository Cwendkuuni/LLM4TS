package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsJshop = new JSJshop();
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
        assertNull(jsJshop.getAddList());
        assertNull(jsJshop.getDeleteList());

        JSJshop jsJshop1 = new JSJshop("domain.shp", "problem.shp");
        assertEquals(0, jsJshop1.getSolution().size());
        assertNull(jsJshop1.getTree());

        JSJshop jsJshop2 = new JSJshop("domain.shp", "problem.shp", "one");
        assertNotNull(jsJshop2.getTree());
        assertNotNull(jsJshop2.getSolution());
        assertFalse(jsJshop2.sol().isFailure());
        assertEquals(1, jsJshop2.getSolution().size());
        JSJshopNode tree = jsJshop2.getTree();
        assertNotNull(tree);
        tree.print();

        JSJshop jsJshop3 = new JSJshop("domain.shp", "problem.shp", "all");
        assertEquals(jsJshop2.getSolution().size(), jsJshop3.getSolution().size());
        for (int i = 0; i < jsJshop3.getSolution().size(); ++i) {
            JSPairPlanTSListNodes pair = jsJshop3.getSolution().elementAt(i);
            JSPairPlanTSListNodes pair1 = jsJshop2.getSolution().elementAt(i);
            assertEquals(pair.size(), pair1.size());
            for (int j = 0; j < pair.size(); ++j) {
                JSPairPlanTSNodes state = pair.elementAt(j);
                JSPairPlanTSNodes state1 = pair1.elementAt(j);
                assertEquals(state.state().toString(), state1.state().toString());
            }
        }
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test(expected = JSParserError.class)
    public void testParserError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("error.shp");
    }

    @Test
    public void testGetBufferedReader() {
        String dir = "src/test/resources/umd/cs/shop/";
        BufferedReader br = JSJshop.getBufferedReader(dir, "domain.shp");
        assertNotNull(br);
        try {
            String line = br.readLine();
            assertNotNull(line);
        } catch (IOException e) {
            fail("Unexpected IOException");
        } finally {
            try {
                br.close();
            } catch (IOException e) {
            }
        }
    }

    @Test
    public void testGetAppletURL() {
        JFrame frame = new JFrame();
        frame.setSize(640, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        JSApplet applet = new JSApplet();
        applet.init();
        URL url = JSJshop.getAppletURL("domain.shp", applet);
        assertNotNull(url);
        frame.dispose();
    }
}