import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;

public class JSJshopTest {

    @Test
    public void testJSJshopWithValidArguments() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test
    public void testJSJshopWithInvalidArguments() {
        String[] args = {"domain.shp", "problem.shp", "invalid"};
        try {
            new JSJshop(args);
            fail("Expected an exception to be thrown with invalid arguments");
        } catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testJSJshopWithEmptyFiles() {
        String domainFileName = "emptyDomain.shp";
        String problemFileName = "emptyProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
    }

    @Test
    public void testJSJshopWithInvalidFile() {
        String domainFileName = "nonexistentFile.shp";
        String problemFileName = "problem.shp";
        try {
            new JSJshop(domainFileName, problemFileName);
            fail("Expected an exception to be thrown with invalid file");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testJSJshopWithDomainFileParsingError() {
        String domainFileName = "invalidDomain.shp";
        String problemFileName = "problem.shp";
        try {
            new JSJshop(domainFileName, problemFileName);
            fail("Expected an exception to be thrown with domain file parsing error");
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testJSJshopWithProblemFileParsingError() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "invalidProblem.shp";
        try {
            new JSJshop(domainFileName, problemFileName);
            fail("Expected an exception to be thrown with problem file parsing error");
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testJSJshopWithInvalidPredicate() {
        String domainFileName = "domain.shp";
        String problemFileName = "problemWithInvalidPredicate.shp";
        try {
            new JSJshop(domainFileName, problemFileName);
            fail("Expected an exception to be thrown with invalid predicate");
        } catch (JSParserError e) {
            assertTrue(true);
        }
    }

    @Test
    public void testJSJshopWithSolution() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getSolution());
        assertTrue(jsJshop.getSolution().size() > 0);
    }

    @Test
    public void testJSJshopWithMultipleSolutions() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblemMultipleSolutions.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getSolution());
        assertTrue(jsJshop.getSolution().size() > 0);
    }
}