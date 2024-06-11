import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;
import umd.cs.shop.JSUtil;
import umd.cs.shop.JSListPairPlanTStateNodes;
import umd.cs.shop.JSListPairPlanTSListNodes;
import umd.cs.shop.JSListPlanningProblem;

public class JSJshopTest {

    @Test
    public void testJSJshopConstructorWithDomainAndProblemFiles() {
        JSJshop jsJshop = new JSJshop("domainFile.shp", "problemFile.shp");
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testJSJshopConstructorWithDomainFileAndPredicate() {
        JSJshop jsJshop = new JSJshop("domainFile.shp", new JSTaskAtom("p"));
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
    }

    @Test
    public void testParserFileMethod() {
        JSJshop jsJshop = new JSJshop();
        File file = new File("testFile.shp");
        jsJshop.parserFile("testFile.shp");
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = JSParserError.class)
    public void testParserFileMethodWithInvalidFile() {
        JSJshop jsJshop = new JSJshop();
        File file = new File("nonExistentFile.shp");
        jsJshop.parserFile("nonExistentFile.shp");
    }

    @Test
    public void testGetBufferedReaderMethod() {
        JSJshop jsJshop = new JSJshop();
        String dir = ".";
        String file = "testFile.shp";
        BufferedReader reader = jsJshop.getBufferedReader(dir, file);
        assertNotNull(reader);
    }

    @Test
    public void testGetBufferedReaderMethodWithApplet() {
        JSJshop jsJshop = new JSJshop();
        JApplet applet = new JApplet() {};
        String file = "testFile.shp";
        BufferedReader reader = jsJshop.getBufferedReader(null, file, applet);
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURLMethod() {
        JSJshop jsJshop = new JSJshop();
        JApplet applet = new JApplet() {};
        String file = "testFile.shp";
        URL url = jsJshop.getAppletURL(file, applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessTokenMethod() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain myDomain"));
        jsJshop.processToken(tokenizer);
        JSPlanningDomain domain = jsJshop.dom();
        assertEquals("myDomain", domain.getName());
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenMethodWithInvalidToken() {
        JSJshop jsJshop = new JSJshop();
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain %%%"));
        jsJshop.processToken(tokenizer);
    }

    @Test
    public void testDomMethod() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningDomain domain = jsJshop.dom();
        assertNotNull(domain);
    }

    @Test
    public void testProbMethod() {
        JSJshop jsJshop = new JSJshop();
        JSPlanningProblem problem = jsJshop.prob();
        assertNotNull(problem);
    }

    @Test
    public void testSolMethod() {
        JSJshop jsJshop = new JSJshop();
        JSPlan sol = jsJshop.sol();
        assertNull(sol);
    }

    @Test
    public void testTreeMethod() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.tree();
        assertNull(tree);
    }

    @Test
    public void testSetFileMethodWithValidFile() {
        JSJshop jsJshop = new JSJshop();
        String nameFile = "testFile.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSListPairPlanTStateNodes solution = jsJshop.setFile(nameFile, pred);
        assertNotNull(solution);
    }

    @Test
    public void testSetFileMethodWithInvalidFile() {
        JSJshop jsJshop = new JSJshop();
        String nameFile = "nonExistentFile.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSListPairPlanTStateNodes solution = jsJshop.setFile(nameFile, pred);
        assertNull(solution);
    }

    @Test
    public void testGetAddListMethod() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = new JSJshopNode(new JSJshopNode(new JSTerminalNode("p(x)"), new ArrayList<JSJshopNode>()), new ArrayList<JSJshopNode>());
        jsJshop.setTree(tree);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
        assertEquals(0, addList.size());
    }

    @Test
    public void testGetDeleteListMethod() {
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = new JSJshopNode(new JSJshopNode(new JSTerminalNode("p(x)"), new ArrayList<JSJshopNode>()), new ArrayList<JSJshopNode>());
        jsJshop.setTree(tree);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
        assertEquals(0, deleteList.size());
    }
}