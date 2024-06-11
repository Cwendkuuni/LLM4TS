package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealConvergenceChecker;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.function.GoalType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private FunctionFunction testFunction;
    private RealConvergenceChecker convergenceChecker;

    @Before
    public void setUp() {
        testFunction = new FunctionFunction(Math::pow, new SimpleBounds(-5, 5), GoalType.MINIMIZE);
        optimizer = new MultiDirectional();
        convergenceChecker = new RealConvergenceChecker(1.0e-8, 100);
    }

    @Test
    public void testDefaultConstructor() {
        optimizer = new MultiDirectional();
        assertDefaultValues(optimizer);
    }

    @Test
    public void testCustomConstructor() {
        optimizer = new MultiDirectional(3.0, 0.7);
        assertCustomValues(optimizer);
    }

    @Test
    public void testIterateSimplexWithSimpleBounds() throws Exception {
        optimizer.setMaxFunctionEvaluations(100);
        optimizer.optimize(testFunction, convergenceChecker);
        PointValuePair result = optimizer.getSolution();
        double expectedMinimum = Math.pow(2.71828, 2.0);
        assertEquals("Incorrect minimum value.", expectedMinimum, result.getPoint()[0], 1.0e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testIterateSimplexWithMaxFunctionEvaluationsReached() throws Exception {
        optimizer.setMaxFunctionEvaluations(1);
        optimizer.optimize(testFunction, convergenceChecker);
    }

    @Test(expected = NoBracketingException.class)
    public void testIterateSimplexWithNoBracketing() throws Exception {
        optimizer.setFunction(new FunctionFunction(Math::sin, new SimpleBounds(0, 0), GoalType.MINIMIZE));
        optimizer.optimize(testFunction, convergenceChecker);
    }

    private void assertDefaultValues(MultiDirectional optimizer) {
        assertEquals("Incorrect expansion coefficient.", 2.0, optimizer.getKhi(), 1.0e-6);
        assertEquals("Incorrect contraction coefficient.", 0.5, optimizer.getGamma(), 1.0e-6);
    }

    private void assertCustomValues(MultiDirectional optimizer) {
        assertEquals("Incorrect expansion coefficient.", 3.0, optimizer.getKhi(), 1.0e-6);
        assertEquals("Incorrect contraction coefficient.", 0.7, optimizer.getGamma(), 1.0e-6);
    }

}