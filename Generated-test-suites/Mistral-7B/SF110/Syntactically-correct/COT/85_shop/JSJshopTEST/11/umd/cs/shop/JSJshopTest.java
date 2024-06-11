package umd.cs.shop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    JSJshop jsjshop;

    @Before
    public void setUp() {
        jsjshop = new JSJshop();
    }

    @After
    public void tearDown() {
        jsjshop = null;
    }

    @Test
    public void testJSJshopConstructor() {
        jsjshop = new JSJshop();
        assertNotNull(jsjshop.probSet);
        assertNull(jsjshop.tree);
        assertNull(jsjshop.solution);
    }

    @Test
    public void testJSJshopConstructorWithDomainFileAndProblemFile() {
        String domainFile = "domain.shp";
        String problemFile = "problem.shp";
        jsjshop = new JSJshop(domainFile, problemFile);
        assertNotNull(jsjshop.probSet);
        assertNull(jsjshop.tree);
        assertNull(jsjshop.solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopConstructorWithInvalidArguments() {
        String[] args = {"domain.shp", "problem.shp", "invalid"};
        jsjshop = new JSJshop(args);
    }

    @Test(expected = NumberFormatException.class)
    public void testJSJshopConstructorWithInvalidNumberFormat() {
        String[] args = {"domain.shp", "problem.shp", "invalidNumber"};
        jsjshop = new JSJshop(args);
    }

    @Test
    public void testParserFile() {
        String libraryFile = "library.shp";
        assertTrue(jsjshop.parserFile(libraryFile));
    }

    @Test(expected = IOException.class)
    public void testParserFileWithIOException() {
        String libraryFile = "nonexistentfile.shp";
        assertFalse(jsjshop.parserFile(libraryFile));
    }

    @Test(expected = JSParserError.class)
    public void testParserFileWithParserError() {
        String libraryFile = "parsingerrorfile.shp";
        assertFalse(jsjshop.parserFile(libraryFile));
    }

    // Add more test cases for other methods and edge cases as needed
}