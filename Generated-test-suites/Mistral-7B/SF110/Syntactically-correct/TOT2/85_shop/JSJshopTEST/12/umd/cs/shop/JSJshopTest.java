package umd.cs.shop;

import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        String[] args = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop jsjshop = new JSJshop(args);
        assertTrue(jsjshop instanceof JSJshop);

        String[] argsInvalid = {"domainDef-file-name", "problemDef-file-name", "invalidArg"};
        try {
            new JSJshop(argsInvalid);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            // expected exception
        }
    }

    @Test
    public void testParserFile() {
        String libraryFile = "testFile.shp";
        assertTrue(new JSJshop().parserFile(libraryFile));
    }

    @Test
    public void testConstructor() {
        JSJshop jsjshop = new JSJshop();
        assertTrue(jsjshop instanceof JSJshop);

        String file = "testFile.shp";
        JSTaskAtom pred = new JSTaskAtom("testPredicate");
        JSJshop jsjshop2 = new JSJshop(file, pred);
        assertTrue(jsjshop2 instanceof JSJshop);
    }

    @Test
    public void testGetters() {
        String domainDefFileName = "domainDef-file-name";
        String problemDefFileName = "problemDef-file-name";
        JSJshop jsjshop = new JSJshop(domainDefFileName, problemDefFileName);

        JSPlanningDomain dom = jsjshop.dom();
        JSPlanningProblem prob = jsjshop.prob();
        JSListPlanningProblem probSet = jsjshop.probSet;
        JSPlan sol = jsjshop.sol;
        JSJshopNode tree = jsjshop.tree;
        JSPairPlanTSListNodes solution = jsjshop.solution;

        assertNotNull(dom);
        assertNotNull(prob);
        assertNotNull(probSet);
        assertNotNull(sol);
        assertNotNull(tree);
        assertNotNull(solution);
    }
}