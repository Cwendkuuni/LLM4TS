import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

    @Test
    public void testJSJshopMain() {
        String domainFileName = "testDomain.shp";
        String problemFileName = "testProblem.shp";
        JSJshop jsShop = new JSJshop(domainFileName, problemFileName);
        // Test for valid arguments
        String[] args = new String[]{"java", "JSJshop", domainFileName, problemFileName, "one"};
        JSJshop.main(args);
        // Test for invalid arguments number
        args = new String[]{"java", "JSJshop", domainFileName, problemFileName};
        JSJshop.main(args);
        // Test for invalid argument type
        args = new String[]{"java", "JSJshop", domainFileName, problemFileName, "invalid"};
        JSJshop.main(args);
        // Test for invalid level argument
        args = new String[]{"java", "JSJshop", domainFileName, problemFileName, "invalidNumber"};
        JSJshop.main(args);
        // Test for successful execution
        JSUtil.expectPrint("Usage :");
        JSUtil.expectPrint("java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]");
        JSUtil.expectPrint("Usage :");
        JSUtil.expectPrint("java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]");
        JSUtil.expectPrint("Reading file testDomain.shp");
        JSUtil.expectPrint("Domain file parsed successfully");
        JSUtil.expectPrint("Reading file testProblem.shp");
        JSUtil.expectPrint("Problem file parsed successfully");
        // Test for empty plan set
        JSUtil.expectPrint("Solving Problem :TestProblem1");
        JSUtil.expectPrint("0 plans found");
        // Test for successful execution with one plan
        JSUtil.expectPrint("Solving Problem :TestProblem2");
        JSUtil.expectPrint("1 plans found.");
        JSUtil.expectPrint("********* PLANS *******");
        JSUtil.expectPrint("Plan # 1");
        // Test for successful execution with all plans
        JSUtil.expectPrint("Solving Problem :TestProblem3");
        JSUtil.expectPrint("3 plans found.");
        JSUtil.expectPrint("********* PLANS *******");
        JSUtil.expectPrint("Plan # 1");
        JSUtil.expectPrint("Plan # 2");
        JSUtil.expectPrint("Plan # 3");
    }

    @Test
    public void testJSJshopSetFile() {
        String fileName = "testFile.shp";
        String predName = "testPredicate";
        JSJshop jsShop = new JSJshop();
        JSJshopNode tree = jsShop.setFile(fileName, new JSTaskAtom("testPredicate"));
        // Test for null tree
        assertNull(jsShop.getTree());
        // Test for successful execution
        JSUtil.expectPrint("File parsed");
        JSUtil.expectPrint("<- domain");
        JSUtil.expectPrint("<- problem");
        JSUtil.expectPrint("After planning");
        JSUtil.expectPrint("SOLUTION FOUND");
        JSUtil.expectPrint("***************FINAL SOLUTION*******");
        JSUtil.expectPrint("SOLUTION FOUND");
        // Test for empty solution
        JSUtil.expectPrint("NO SOLUTION FOUND");
        // Test for tree structure
        JSUtil.expectPrint("********* tree FINAL SOLUTION*******");
        JSUtil.expectPrint("(init (initial-state))");
        JSUtil.expectPrint("[action1 (precondition1) (effect1)]");
        JSUtil.expectPrint("[action2 (precondition2) (effect2)]");
        JSUtil.expectPrint("[action3 (precondition3) (effect3)]");
    }
}