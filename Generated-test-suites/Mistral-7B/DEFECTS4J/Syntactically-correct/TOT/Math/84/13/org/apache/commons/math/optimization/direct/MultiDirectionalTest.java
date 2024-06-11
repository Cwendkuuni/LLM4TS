package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.functions.PolynomialFunction;
import org.apache.commons.math3.optim.nonlinear.functions.UnivariateFunction;
import org.apache.commons.math3.special.E;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

public class MultiDirectionalTest {

    @Test
    public void testMultiDirectionalOptimizerDefaultConstructor() {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-1.0, 1.0);
        double x = 0.5;
        UnivariateFunction function = new PolynomialFunction(3, -1.0, 1.0, 1.0, -1.0);
        try {
            PointValuePair result = optimizer.optimize(function, bounds);
            Assert.assertEquals(E.E_E, result.getValue(), 1e-12);
            Assert.assertEquals(x, result.getPoint().getEntry(0), 1e-12);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testMultiDirectionalOptimizerCustomConstructor() {
        MultiDirectional optimizer = new MultiDirectional(3.0, 0.8);
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        double x = 1.5;
        UnivariateFunction function = new PolynomialFunction(5, -5.0, 5.0, 1.0, -5.0);
        try {
            PointValuePair result = optimizer.optimize(function, bounds);
            Assert.assertEquals(E.E_E, result.getValue(), 1e-12);
            Assert.assertEquals(x, result.getPoint().getEntry(0), 1e-12);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException |
               NoBracketingException | MaxEvaluationsExceededException | NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testMultiDirectionalOptimizerReflect() {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        double x = 2.0;
        UnivariateFunction function = new PolynomialFunction(3, -10.0, 10.0, 1.0, -10.0);
        try {
            PointValuePair[] initialSimplexFinal = optimizer.optimize(function, bounds, 20);
            RealPointValuePair initialBest = initialSimplexFinal[0];
            Comparator<RealPointValuePair> comparator = Arrays.asList(initialSimplexFinal).stream().sorted(Comparator.comparing(RealPointValuePair::getValue)).findFirst().get();
            RealPointValuePair reflected = optimizer.reflect(comparator);
            Assert.assertNotEquals(initialBest, reflected);
            Assert.assertTrue(reflected.getValue() < initialBest.getValue());
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException |
               NoBracketingException | MaxEvaluationsExceededException | NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test
    public void testMultiDirectionalOptimizerContract() {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        double x = 3.5;
        UnivariateFunction function = new PolynomialFunction(3, -10.0, 10.0, 1.0, -10.0);
        try {
            PointValuePair[] initialSimplexFinal = optimizer.optimize(function, bounds, 20);
            RealPointValuePair initialBest = initialSimplexFinal[0];
            Comparator<RealPointValuePair> comparator = Arrays.asList(initialSimplexFinal).stream().sorted(Comparator.comparing(RealPointValuePair::getValue)).findLast().get();
            RealPointValuePair contracted = optimizer.contract(comparator);
            Assert.assertNotEquals(initialBest, contracted);
            Assert.assertTrue(contracted.getValue() > initialBest.getValue());
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException |
               NoBracketingException | MaxEvaluationsExceededException | NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test(expected = UnboundedDomainException.class)
    public void testMultiDirectionalOptimizerBoundaryCase() {
        MultiDirectional optimizer = new MultiDirectional();
        SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        UnivariateFunction function = new PolynomialFunction(3, -10.0, 10.0, 1.0, -10.0);
        try {
            optimizer.optimize(function, bounds, 20);
        } catch (FunctionEvaluationException | OptimizationException | IllegalArgumentException |
               NoBracketingException | MaxEvaluationsExceededException | NumberIsTooSmallException e) {
            // expected
        }
    }

}