package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshopConstructorWithDomainAndProblemFiles() {
        JSJshop jsJshop = new JSJshop("domain.txt", "problem.txt");
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorWithInvalidArguments() {
        String[] args = {"domain.txt", "problem.txt", "three"};
        try {
            new JSJshop(args);
        } catch (NumberFormatException e) {
            fail("Unexpected NumberFormatException");
        }
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testJSJshopConstructorWithTaskAtom() {
        JSTaskAtom pred = new JSTaskAtom("p1", new JSTerm("x"), new JSTerm("y"));
        JSJshop jsJshop = new JSJshop("file.txt", pred);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testSetFileMethodWithValidFile() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile("file.txt", new JSTaskAtom("p1", new JSTerm("x"), new JSTerm("y")));
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileMethodWithNullFile() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile(null, new JSTaskAtom("p1", new JSTerm("x"), new JSTerm("y")));
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFileMethodWithNullPredicate() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.setFile("file.txt", null);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParserMethod() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test(expected = FileNotFoundException.class)
    public void testParserFileMethodWithInvalidFile() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("invalidFile.txt");
        fail("Expected FileNotFoundException");
    }
}