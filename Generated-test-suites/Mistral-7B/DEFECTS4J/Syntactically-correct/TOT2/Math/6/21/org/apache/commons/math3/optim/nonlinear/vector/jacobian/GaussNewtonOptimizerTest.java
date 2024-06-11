package org.apache.commons.math3.optimization.general;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link GaussNewtonOptimizer}.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private PointVectorValuePair expectedResult;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        expectedResult = new PointVectorValuePair(new double[]{1.0, 2.0}, new double[]{0.0, 0.0});
    }

    @Test
    public void testOptimizationWithSimpleData() {
        double[] targetValues = {3.0, 5.0};
        double[] startPoint = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        PointVectorValuePair result = optimizer.optimize();

        Assert.assertEquals(expectedResult.getPoint(), result.getPoint(), 0.001);
        Assert.assertEquals(expectedResult.getValue(), result.getValue(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularWeightMatrix() {
        double[] targetValues = {3.0, 5.0};
        double[] startPoint = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testNonConvergence() {
        double[] targetValues = {3.0, 5.0};
        double[] startPoint = {1.0, 1.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        ConvergenceChecker<PointVectorValuePair> checker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair previous, PointVectorValuePair current) {
                return false;
            }
        };

        optimizer.setConvergenceChecker(checker);

        optimizer.optimize();
    }
}