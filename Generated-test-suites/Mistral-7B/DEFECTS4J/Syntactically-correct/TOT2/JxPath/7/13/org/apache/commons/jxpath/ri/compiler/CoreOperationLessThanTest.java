package org.apache.commons.jxpath.ri.compiler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Assert;

public class CoreOperationLessThanTest {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CoreOperationLessThanTest.class);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(CoreOperationLessThanTest.class);
        suite.addTest(CoreOperationLessThanTest1.class);
        suite.addTest(CoreOperationLessThanTest2.class);
        suite.addTest(CoreOperationLessThanTest3.class);
        return suite;
    }

    static class MockInfoSet {
        // empty implementation
    }
}