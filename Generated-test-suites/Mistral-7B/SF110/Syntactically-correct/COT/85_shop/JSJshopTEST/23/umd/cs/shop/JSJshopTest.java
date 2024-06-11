import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSJshopTest {
    

    @Test
    public void testJSJshopMain() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        assertNotNull(jsJshop.getTree());
        assertNotNull(jsJshop.getSolution());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopMainInvalidArguments() {
        String[] args = {"test_domain.shp", "test_problem.shp", "invalid_argument"};
        JSJshop jsJshop = new JSJshop(args);
    }

    @Test
    public void testJSJshopConstructor() {
        String domainFileName = "test_domain.shp";
        JSJshop jsJshop = new JSJshop(domainFileName);
        assertNull(jsJshop.getTree());
        assertNull(jsJshop.getSolution());
    }

    @Test
    public void testJSJshopSetFile() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        String file = "test_file.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        JSPairPlanTSListNodes solution = jsJshop.setFile(file, pred);
        assertNotNull(solution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJSJshopSetFileNullFile() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        jsJshop.setFile(null, pred);
    }

    @Test
    public void testJSJshopSetFileNullPredicate() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        String file = "test_file.shp";
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        jsJshop.setFile(file, null);
    }

    @Test
    public void testJSJshopGetAddList() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        String file = "test_file.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        jsJshop.setFile(file, pred);
        JSListLogicalAtoms addList = jsJshop.getAddList();
        assertNotNull(addList);
    }

    @Test
    public void testJSJshopGetDeleteList() {
        String domainFileName = "test_domain.shp";
        String problemFileName = "test_problem.shp";
        String file = "test_file.shp";
        JSTaskAtom pred = new JSTaskAtom("p");
        JSJshop jsJshop = new JSJshop(domainFileName, problemFileName);
        jsJshop.setFile(file, pred);
        JSListLogicalAtoms deleteList = jsJshop.getDeleteList();
        assertNotNull(deleteList);
    }

    
}