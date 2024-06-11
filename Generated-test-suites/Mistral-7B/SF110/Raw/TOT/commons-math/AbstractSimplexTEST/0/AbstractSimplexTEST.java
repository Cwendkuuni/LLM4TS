package org.apache.commons.math3.optim.nonlinear.scalar.noderiv;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonlinearOptimizer;
import org.apache.commons.math3.optim.PointValuePairList;
import org.apache.commons.math3.optim.maximization.MaxEval;
import org.apache.commons.math3.optim.maximization.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractSimplexTest {

    @Test
    public void testAbstractSimplexWithHypercubeSteps() {
        int dimension = 2;
        double sideLength = 10.0;
        AbstractSimplex simplex = new Simplex2D(dimension, sideLength);

        MultivariateFunction function = new AdditionFunction(new ConstantFunction(0.0), new ConstantFunction(1.0));
        InitialGuess initialGuess = new InitialGuess(new double[]{0.0, 0.0});

        simplex.build(initialGuess.getInitialPoint());
        simplex.evaluate(function, null);

        PointValuePair bestPoint = simplex.getPoint(simplext.getSize() - 1);
        double bestValue = bestPoint.getValue();
        double[] bestPointValues = bestPoint.getPointRef();

        NonlinearOptimizer optimizer = new SimplexSolver(function, new PointValuePairList(simplex.getPoints()), GoalType.MINIMIZE, null);
        PointValuePair optimizerResult = optimizer.optimize();

        assertEquals(bestValue, optimizerResult.getValue(), 1e-9);
        assertArrayEquals(bestPointValues, optimizerResult.getPointRef(), 1e-9);
    }

    @Test
    public void testAbstractSimplexWithReferenceSimplex() {
        int dimension = 2;
        double[] referenceSimplex = { {0.0, 0.0}, {1.0, 1.0}, {2.0, 2.0} };
        AbstractSimplex simplex = new Simplex2D(dimension, referenceSimplex);

        MultivariateFunction function = new QuadraticFunction(new double[]{1.0, 1.0}, new double[]{-1.0, -1.0}, 1.0);
        InitialGuess initialGuess = new InitialGuess(new double[]{0.0, 0.0});

        simplex.build(initialGuess.getInitialPoint());
        simplex.evaluate(function, null);

        PointValuePair bestPoint = simplex.getPoint(simplext.getSize() - 1);
        double bestValue = bestPoint.getValue();
        double[] bestPointValues = bestPoint.getPointRef();

        NonlinearOptimizer optimizer = new SimplexSolver(function, new PointValuePairList(simplex.getPoints()), GoalType.MINIMIZE, null);
        PointValuePair optimizerResult = optimizer.optimize();

        assertEquals(bestValue, optimizerResult.getValue(), 1e-9);
        assertArrayEquals(bestPointValues, optimizerResult.getPointRef(), 1e-9);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullArguments() {
        new AbstractSimplex(null);
    }

    @Test(expected = ZeroException.class)
    public void testZeroSteps() {
        int dimension = 1;
        double sideLength = 0.0;
        AbstractSimplex simplex = new Simplex1D(dimension, sideLength);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMismatchedStartConfigurationSize() {
        int dimension = 2;
        double[] steps = {0.1, 0.2, 0.3};
        AbstractSimplex simplex = new AbstractSimplex(dimension, steps);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testMismatchedReferenceSimplexSize() {
        int dimension = 2;
        double[] referenceSimplex = { {0.0, 0.0}, {1.0, 1.0}, {1.0, 1.0} };
        AbstractSimplex simplex = new AbstractSimplex(dimension, referenceSimplex);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEqualVerticesInSameSimplex() {
        int dimension = 2;
        double[] steps = {0.1, 0.1};
        AbstractSimplex simplex = new AbstractSimplex(dimension, steps);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testEqualVerticesInDifferentSimplex() {
        int dimension = 2;
        double[] referenceSimplex = { {0.0, 0.0}, {1.0, 1.0}, {1.0, 1.0} };
        AbstractSimplex simplex = new AbstractSimplex(dimension, referenceSimplex);
    }
}