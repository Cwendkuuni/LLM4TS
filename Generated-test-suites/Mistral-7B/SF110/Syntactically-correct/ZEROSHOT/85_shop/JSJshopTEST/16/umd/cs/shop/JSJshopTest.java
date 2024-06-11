package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSJshopTest {

    private JSJshop jsJshop;
    private String domainFileName = "domain.shp";
    private String problemFileName = "problem.shp";

    @Before
    public void setUp() {
        jsJshop = new JSJshop();
    }

    @Test
    public void testMain() {
        String[] args = new String[]{"domain.shp", "problem.shp"};
        JSJshop.main(args);
        assertTrue(jsJshop.probSet.size() > 0);
    }

    @Test
    public void testConstructorWithDomainAndProblemFiles() {
        jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.dom);
        assertNotNull(jsJshop.prob);
        assertNotNull(jsJshop.probSet);
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidArgumentsInConstructor() {
        String[] args = new String[]{"domain.shp", "problem.shp", "invalidArgument"};
        new JSJshop(args);
    }

    @Test
    public void testSetFile() throws IOException {
        String fileName = "file.shp";
        JSTaskAtom pred = null;
        JSJshopNode expectedTree = null;
        JSListLogicalAtoms addList = new ArrayList<>();
        JSListLogicalAtoms deleteList = new ArrayList<>();

        jsJshop.setFile(fileName, pred);

        assertNotNull(jsJshop.tree);
        assertNotNull(jsJshop.solution);
        assertEquals(addList.size(), jsJshop.getAddList().size());
        assertEquals(deleteList.size(), jsJshop.getDeleteList().size());

        expectedTree = jsJshop.getTree();
    }

    @Test
    public void testParser() {
        jsJshop.testParser();
    }

    @Test
    public void testBufferedReader() throws IOException {
        String dir = ".";
        String file = "file.shp";
        JSJshop.BufferedReader reader = jsJshop.getBufferedReader(dir, file);
        assertNotNull(reader);
    }

    @Test
    public void testGetAppletURL() {
        JApplet applet = new JFrame();
        URL url = jsJshop.getAppletURL(null, applet);
        assertNull(url);

        url = jsJshop.getAppletURL("file.html", applet);
        assertNotNull(url);
    }

    @Test
    public void testProcessToken() throws IOException {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("(defdomain mydomain (:domain-package mypackage) (:import-from java.util.Collections))"));
        jsJshop.processToken(tokenizer);
        assertTrue(tokenizer.ttype == JSJshopVars.leftBrac);
        tokenizer.nextToken();
        assertTrue(tokenizer.ttype == JSJshopVars.identifier);
        tokenizer.nextToken();
        assertTrue(tokenizer.ttype == JSJshopVars.leftPar);
        tokenizer.nextToken();
        assertTrue(tokenizer.ttype == JSJshopVars.identifier);
        tokenizer.nextToken();
        assertTrue(tokenizer.ttype == JSJshopVars.colon);
        tokenizer.nextToken();
        assertTrue(tokenizer.ttype == JSJshopVars.identifier);
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();
        tokenizer.nextToken();

        jsJshop.parserFile(domainFileName);
    }
}