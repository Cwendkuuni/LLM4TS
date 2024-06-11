import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;
import umd.cs.shop.JSPlanningDomain;
import umd.cs.shop.JSPlanningProblem;
import umd.cs.shop.JSPlan;
import umd.cs.shop.JSJshopNode;
import umd.cs.shop.JSListLogicalAtoms;

public class JSJshopTest {

    @Test
    public void testJSJshopWithValidArguments() {
        String domainFileName = "domain.shp";
        String problemFileName = "problem.shp";

        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        JSPlanningDomain dom = jsJshop.dom();
        JSPlanningProblem prob = jsJshop.prob();
        JSPlan sol = jsJshop.sol();
        JSJshopNode tree = jsJshop.tree();
        JSListLogicalAtoms addList = jsJshop.getAddList();
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        JSPairPlanTSListNodes solution = jsJshop.getSolution();

        // Testing if the correct number of plans are found
        assertEquals(1, probSet.size());

        // Testing if there is a solution for the problem
        assertNotNull(solution);

        // Testing if the solution exists
        assertNotNull(sol);

        // Testing if the solution is not a failure
        assertFalse(sol.isFailure());

        // Testing if the tree is not null
        assertNotNull(tree);

        // Testing if the addList and deleteList are not empty
        assertFalse(addList.isEmpty());
        assertFalse(deleteList.isEmpty());

        // Testing if the addList and deleteList have the correct logical atoms
        // Replace this with actual testing logic based on the logical atoms in the class
        // For example, you can check if specific logical atoms are added or deleted based on the input problem

        // Testing if the JSJshopNode tree is correct
        // Replace this with actual testing logic based on the structure of the tree
    }

    @Test
    public void testJSJshopWithInvalidArguments() {
        String domainFileName = "domain.shp";
        String problemFileName1 = "problem1.shp";
        String problemFileName2 = "problem2.shp";

        // Testing if the wrong number of arguments are passed
        String[] args = {domainFileName, problemFileName1, problemFileName2};
        JSJshop jsJshop = new JSJshop(args);
        assertNull(jsJshop);

        // Testing if the file names are invalid
        String invalidDomainFileName = null;
        String invalidProblemFileName = null;
        args = new String[]{"domain.shp", invalidProblemFileName};
        JSJshop jsJshopInvalidFileName = new JSJshop(args);
        assertNull(jsJshopInvalidFileName);

        args = new String[]{invalidDomainFileName, problemFileName2};
        JSJshop jsJshopInvalidDomainFileName = new JSJshop(args);
        assertNull(jsJshopInvalidDomainFileName);

        // Testing if the file can't be read
        String unreadableFile = "unreadableFile.shp";
        args = new String[]{domainFileName, unreadableFile};
        JSJshop jsJshopUnreadableFile = new JSJshop(args);
        assertNull(jsJshopUnreadableFile);
    }

    @Test
    public void testJSJshopWithEmptyFile() {
        String domainFileName = "emptyDomain.shp";
        String problemFileName = "emptyProblem.shp";

        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);

        // Testing if there is a solution for the problem
        assertNull(jsJshop.getSolution());

        // Testing if the solition plan is not null
        assertNull(jsJshop.sol());
    }

    @Test
    public void testParser() {
        JSJshop jsJshop = new JSJshop();
        jsJshop.testParser();
    }
}