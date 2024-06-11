package umd.cs.shop;

import static org.junit.Assert.*;

import org.junit.Test;

public class JSJshopTest {

    

    @Test
    public void testParser() {
        final JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }

    @Test(expected = JSParserError.class)
    public void testParserError() {
        final JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile("invalidFile.shp");
    }

    @Test
    public void testMain() {
        // Given: Create a JSJshop instance with valid arguments
        final String domainFileName = "validDomainFile.shp";
        final String problemFileName = "validProblemFile.shp";
        final JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);

        // When: The JSJshop constructor is called with valid arguments
        // Then: The JSPlanningDomain and JSPlanningProblem are parsed and initialized
        assertTrue(jsJshop.dom() != null);
        assertTrue(jsJshop.prob() != null);

        // Given: Create a JSJshop instance with invalid arguments
        final String invalidArg = "invalidArg";
        try {
            new JSJshop(new String[]{"", invalidArg});
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Then: A NumberFormatException is thrown
        }

        // Given: Create a JSJshop instance with valid arguments and set flagLevel to 1
        final String domainFileName1 = "domainFileName1.shp";
        final String problemFileName1 = "problemFileName1.shp";
        final JSJshop jsJshop1 = new JSJshop(domainFileName1, problemFileName1);
        jsJshop1.setFlagLevel(1);

        // When: The constructor is called with valid arguments and flagLevel set to 1
        // Then: Only one plan is generated and printed
        JSUtil stub = new JSUtil() {
            @Override
            public void println(String message) {
                int count = getLines().size();
                assertEquals("Plan # " + (count + 1) + " found.", message);
            }
        };
        System.setOut(stub);
        new JSJshop(domainFileName1, problemFileName1);
        assertEquals(1, jsJshop1.getSolution().size());

        // Given: Create a JSJshop instance with valid arguments and set flagLevel to all
        final String domainFileName2 = "domainFileName2.shp";
        final String problemFileName2 = "problemFileName2.shp";
        final JSJshop jsJshop2 = new JSJshop(domainFileName2, problemFileName2);
        jsJshop2.setFlagLevel(JSJshopVars.ALL_PLANS);

        // When: The constructor is called with valid arguments and flagLevel set to all
        // Then: All plans are generated and printed
        stub = new JSUtil() {
            @Override
            public void println(String message) {
                int count = getLines().size();
                if (message.startsWith("Plan # ")) {
                    assertEquals("Plan # " + (count + 1) + " found.", message);
                } else if (message.startsWith("********* PLANS *******")) {
                    int plansCount = Integer.parseInt(message.split(" ")[1].trim().replaceAll("[^0-9]", ""));
                    assertEquals(plansCount, jsJshop2.getSolution().size());
                }
            }
        };
        System.setOut(stub);
        new JSJshop(domainFileName2, problemFileName2);
        assertEquals(jsJshop2.getSolution().size(), jsJshop2.probSet.size());

        // Given: Create a JSJshop instance with valid arguments (domainFile and problemFile) and a JSTaskAtom (pred)
        final String domainFileName3 = "domainFileName3.shp";
        final String problemFileName3 = "problemFileName3.shp";
        final JSTaskAtom pred = new JSTaskAtom("pred");
        final JSJshop jsJshop3 = new JSJshop(domainFileName3, problemFileName3, pred);

        // When: The constructor is called with valid arguments (domainFile, problemFile, and pred)
        // Then: The JSPlanningDomain, JSPlanningProblem, and JSJshopNode tree are parsed and initialized
        assertTrue(jsJshop3.dom() != null);
        assertTrue(jsJshop3.prob() != null);
        assertTrue(jsJshop3.getTree() != null);
    }

    @Test
    public void testSetFile() {
        final String domainFileName = "domainFileName.shp";
        final String problemFileName = "problemFileName.shp";
        final JSJshop jsJshop = new JSJshop();
        jsJshop.parserFile(domainFileName);
        jsJshop.parserFile(problemFileName);

        // Given: Call setFile method with valid arguments
        final JSTaskAtom pred = new JSTaskAtom("pred");
        final JSPairPlanTSListNodes solution = jsJshop.setFile(domainFileName, pred);

        // When: The setFile method is called with valid arguments
        // Then: The JSPlanningDomain, JSPlanningProblem, JSPlan sol, JSPairPlanTSListNodes solution, and JSJshopNode tree are initialized
        assertTrue(solution != null);
        assertTrue(solution.planS().plan() != null);
        assertTrue(solution.planS().tState().addList() != null);
        assertTrue(solution.planS().tState().deleteList() != null);
        assertTrue(solution.planS().tState().addList().size() > 0);
        assertTrue(solution.planS().tState().deleteList().size() > 0);
        assertTrue(jsJshop.getTree() != null);

        // Given: Call setFile method with invalid arguments (null domainFile)
        try {
            jsJshop.setFile(null, pred);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Then: A NullPointerException is thrown
        }

        // Given: Call setFile method with invalid arguments (null pred)
        try {
            jsJshop.setFile(domainFileName, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Then: A NullPointerException is thrown
        }
    }

    
}