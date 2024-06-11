
// JSJshopTest.java

import org.junit.Test;
import static org.junit.Assert.*;
import umd.cs.shop.*;

public class JSJshopTest {

    @Test
    public void testMain() {
        JSJshop test = new JSJshop();
        String[] args = {"test.domain", "test.problem"};
        try {
            test.main(args);
        } catch (Exception e) {
            assertEquals("Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
                    "[number-of-plans] can be 'one' or 'all'. The default value is 'one'", e.getMessage());
        }
    }

    // Test remaining methods here

}
