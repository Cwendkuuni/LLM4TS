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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Test;
import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerWithLUDecomposition() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] initialPoint = {1.0, 1.0, 1.0};
        ConvergenceChecker<PointValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);

        PointValuePair result = optimizer.optimize(targetValues, weightMatrix, initialPoint);
        assertArrayEquals(new double[]{2.0, 2.0, 2.0}, result.getPoint(), 0.0);
        assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    @Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] initialPoint = {1.0, 1.0, 1.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, checker);

        PointVectorValuePair result = optimizer.optimize(targetValues, weightMatrix, initialPoint);
        assertArrayEquals(new double[]{2.0, 2.0, 2.0}, result.getPoint(), 0.0);
        assertEquals(0.0, optimizer.getCost(), 0.0);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullTargetValues() {
        double[] targetValues = null;
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] initialPoint = {1.0, 1.0, 1.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.optimize(targetValues, weightMatrix, initialPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = null;
        double[] initialPoint = {1.0, 1.0, 1.0};
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.optimize(targetValues, weightMatrix, initialPoint);
    }

    @Test(expected = NullArgumentException.class)
    public void testNullInitialPoint() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] initialPoint = null;
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.optimize(targetValues, weightMatrix, initialPoint);
    }

    @Test(expected = ConvergenceException.class)
    public void testDivergentOptimizer() {
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] initialPoint = new double[100];
        ConvergenceChecker<PointVectorValuePair> checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, checker);
        optimizer.optimize(targetValues, weightMatrix, initialPoint);
    }
}