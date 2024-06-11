package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testParserFile() {
        JSJshop jsJshop = new JSJshop();
        assertTrue(jsJshop.parserFile("testFile.shp"));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithError() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("testFileWithError.shp");
    }

    @Test
    public void testGetBufferedReader() {
        JSJshop jsJshop = new JSJshop();
        BufferedReader reader = jsJshop.getBufferedReader("testDir", "testFile.txt");
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = JSJshop.getAppletURL("testFile.txt", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain MyDomain"));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
        assertEquals("MyDomain", jsJshop.dom.getName());
    }

    @Test(expected = JSParserError.class)
    public void testProcessTokenWithError() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("defdomain MyDomain %%%"));
        JSJshop jsJshop = new JSJshop();
        jsJshop.processToken(tokenizer);
    }

    @Test
    public void testNewJSJshop() {
        JSJshop jsJshop = new JSJshop("testDomainFile.shp", "testProblemFile.shp");
        assertNotNull(jsJshop.tree);
        assertNotNull(jsJshop.solution);
    }

    @Test
    public void testNewJSJshopWithError() {
        JSJshop jsJshop = new JSJshop("testDomainFile.shp", "testProblemFileWithError.shp");
        assertNull(jsJshop.tree);
        assertNull(jsJshop.solution);
    }

    @Test
    public void testGetAddList() {
        JSJshop jsJshop = new JSJshop("testDomainFile.shp", "testProblemFile.shp");
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSJshop jsJshop = new JSJshop("testDomainFile.shp", "testProblemFile.shp");
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }
}