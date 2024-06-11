package umd.cs.shop.test;

import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshopParsing() throws IOException {
        JSJshop jsJshop = new JSJshop("domain.shp", "problem.shp");
        assertNotNull(jsJshop);
        assertTrue(jsJshop.getTree() != null);
        assertTrue(jsJshop.getSolution() != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopInvalidArguments() {
        new JSJshop();
    }

    @Test
    public void testJSJshopFileReading() throws IOException {
        JSJshop jsJshop = new JSJshop("nonexistentfile.shp", "problem.shp");
        assertNull(jsJshop);
    }

    @Test
    public void testJSJshopFileReadingWithTaskAtom() throws IOException {
        JSTaskAtom pred = new JSTaskAtom("p");
        JSJshop jsJshop = new JSJshop("file.shp", pred);
        assertNotNull(jsJshop);
        assertTrue(jsJshop.getTree() != null);
        assertTrue(jsJshop.getSolution() != null);
    }

    @Test
    public void testJSJshopGetAddList() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        List<JSLogicalAtom> addList = jsJshop.getAddList();
        assertNotNull(addList);
        assertTrue(addList.size() > 0);
    }

    @Test
    public void testJSJshopGetDeleteList() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        JSJshop jsJshop = new JSJshop(domainFile, problemFile);
        List<JSLogicalAtom> deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
        assertTrue(deleteList.size() > 0);
    }
}