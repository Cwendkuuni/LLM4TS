package umd.cs.shop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopWithInvalidArguments() {
        String[] args = {"testDomain.shp", "testProblem.shp", "invalidArgument"};
        JSJshop jsJshop = new JSJshop(args);
    }

    @Test
    public void testJSJshopWithOnePlan() {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblemOnePlan.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        List<JSJshopNode> tree = jsJshop.getTree().getChildren();
        assertEquals(1, tree.size());
    }

    @Test
    public void testJSJshopWithAllPlans() {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblemAllPlans.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        List<JSJshopNode> tree = jsJshop.getTree().getChildren();
        assertEquals(5, tree.size());
    }

    @Test
    public void testSetFile() throws IOException {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblem.shp";
        String fileWithPredicate = "testFileWithPredicate.shp";
        JSTaskAtom predicate = new JSTaskAtom("testPredicate");
        JSJshop jsJshop = new JSJshop();
        JSJshopNode tree = jsJshop.setFile(domainFile, predicate);
        assertNotNull(tree);
    }

    @Test(expected = FileNotFoundException.class)
    public void testSetFileWithInvalidFile() throws IOException {
        String domainFile = "testDomain.shp";
        String problemFile = "testProblem.shp";
        String invalidFile = "invalidFile.shp";
        JSTaskAtom predicate = new JSTaskAtom("testPredicate");
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile(invalidFile, predicate);
    }

    @Test
    public void testParser() {
        JSUtil util = new JSUtil();
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test(expected = IOException.class)
    public void testParserWithInvalidFile() {
        JSUtil util = new JSUtil();
        JSJshop jsJshop = new JSJshop();
        String invalidFile = "invalidFile.shp";
        jsJshop.parserFile(invalidFile);
    }

    @Test
    public void testGetBufferedReader() throws IOException {
        JSUtil util = new JSUtil();
        JSJshop jsJshop = new JSJshop();
        String file = "testFile.shp";
        BufferedReader bufferedReader = jsJshop.getBufferedReader(null, file);
        assertNotNull(bufferedReader);
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetBufferedReaderWithInvalidFile() throws IOException {
        JSUtil util = new JSUtil();
        JSJshop jsJshop = new JSJshop();
        String invalidFile = "invalidFile.shp";
        BufferedReader bufferedReader = jsJshop.getBufferedReader(null, invalidFile);
    }
}