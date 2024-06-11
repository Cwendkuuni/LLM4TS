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
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for GaussNewtonOptimizer class.
 */
public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private double[] targetValues;
    private double[] initialPoint;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        targetValues = new double[]{-1.0, 2.0};
        initialPoint = new double[]{1.0, 1.0};
        weightMatrix = new BlockRealMatrix(2, 2).setEntry(0, 0, 1.0)
                .setEntry(0, 1, 0.0)
                .setEntry(1, 0, 0.0)
                .setEntry(1, 1, 1.0);

        optimizer = new GaussNewtonOptimizer(true) {
            @Override
            public double[] getTarget() {
                return targetValues;
            }

            @Override
            public double[] getStartPoint() {
                return initialPoint.clone();
            }

            @Override
            public RealMatrix getWeight() {
                return weightMatrix;
            }
        };
        optimizer.setConvergenceChecker(new SimpleVectorValueChecker());
    }

    @Test
    public void testDoOptimize_StandardCase() {
        PointVectorValuePair solution = optimizer.doOptimize();
        double[] expectedSolution = new double[]{-0.9999999999999998, 2.00000000000001};
        Assert.assertArrayEquals(expectedSolution, solution.getPoint(), 0.00001);
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullTarget() {
        optimizer.setTarget(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.doOptimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testDoOptimize_NullWeightMatrix() {
        optimizer.setWeight(null);
        optimizer.doOptimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testDoOptimize_SingularWeightMatrix() {
        RealMatrix singularWeightMatrix = new BlockRealMatrix(2, 2).setEntry(0, 0, 0.0)
                .setEntry(0, 1, 0.0)
                .setEntry(1, 0, 0.0)
                .setEntry(1, 1, 0.0);
        optimizer.setWeight(singularWeightMatrix);
        optimizer.doOptimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testDoOptimize_Divergence() {
        double[] divergentTargetValues = new double[]{-1.0, 0.0};
        optimizer.setTarget(divergentTargetValues);
        optimizer.doOptimize();
    }
}