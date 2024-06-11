package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

    @Test
    public void testJSJshop() {
        JSJshop jsjshop = new JSJshop();
        assertNull(jsjshop.getTree());
        assertNull(jsjshop.getSolution());
    }

    @Test
    public void testJSJshopWithDomainAndProblemFiles() {
        String domainFileName = "domainFile.shp";
        String problemFileName = "problemFile.shp";
        JSJshop jsjshop = new JSJshop(domainFileName, problemFileName);
        JSPlanningDomain dom = jsjshop.dom();
        JSPlanningProblem prob = jsjshop.prob();
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();

        assertNotNull(dom);
        assertNotNull(prob);
        assertNotNull(tree);
        assertNotNull(solution);

        // Add more assertions to check the contents of domain, problem, tree and solution
    }

    @Test
    public void testJSJshopWithFileAndPredicate() {
        String nameFile = "nameFile.shp";
        JSTaskAtom pred = new JSTaskAtom("predicate");
        JSJshop jsjshop = new JSJshop(nameFile, pred);
        JSJshopNode tree = jsjshop.getTree();
        JSPairPlanTSListNodes solution = jsjshop.getSolution();

        assertNotNull(tree);
        assertNotNull(solution);

        // Add more assertions to check the contents of tree and solution
    }
}