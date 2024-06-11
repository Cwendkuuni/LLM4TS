package org.apache.commons.math3.optimization.base;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR> {

    private BaseOptimizer<PAIR> optimizer;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @Before
    public void setup() {
        optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        convergenceChecker = new ConvergenceChecker<PAIR>() {
            @Override
            public boolean checkConvergence(PAIR currentValue) {
                return false;
            }
        };
        optimizer.setConvergenceChecker(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(10, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        optimizer.optimize(new Object(), new Object()); // Simulate optimization
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations); // Should be 0 before optimization

        optimizer.optimize(new Object(), new Object()); // Perform optimization
        evaluations = optimizer.getEvaluations();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(maxEvaluations, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> checker = optimizer.getConvergenceChecker();
        assertSame(convergenceChecker, checker);
    }
}