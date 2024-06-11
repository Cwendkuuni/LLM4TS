import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizerConstructor() {
        // Test default constructor
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertFalse(optimizer.isUseLU());
        assertTrue(optimizer.getConvergenceChecker() instanceof SimpleVectorValueChecker);

        // Test constructor with useLU = true
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer(true);
        assertTrue(optimizer1.isUseLU());
        assertTrue(optimizer1.getConvergenceChecker() instanceof SimpleVectorValueChecker);

        // Test constructor with useLU = false and custom ConvergenceChecker
        ConvergenceChecker<PointVectorValuePair> checker = new MyCustomConvergenceChecker();
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false, checker);
        assertFalse(optimizer2.isUseLU());
        assertEquals(checker, optimizer2.getConvergenceChecker());
    }

    @Test
    public void testGaussNewtonOptimizerOptimize() throws ConvergenceException, SingularMatrixException, NullArgumentException {
        // Prepare data
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});

        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        // Test optimization
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.5, 1.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);

        // Test exception when using a null convergence checker
        GaussNewtonOptimizer optimizerNullChecker = new GaussNewtonOptimizer(true);
        optimizerNullChecker.setConvergenceChecker(null);
        try {
            optimizerNullChecker.optimize();
            fail("Expected ConvergenceException");
        } catch (NullArgumentException e) {
            // Expected
        }

        // Test exception when using a singluar weight matrix
        RealMatrix singularWeightMatrix = new SingularValueDecomposition(new ArrayRealVector(new double[]{1.0, 1.0}, false)).getSolve(new ArrayRealVector(new double[]{1.0, 1.0}, false)).getMatrix().transpose();
        GaussNewtonOptimizer optimizerSingularWeight = new GaussNewtonOptimizer(true);
        optimizerSingularWeight.setTarget(targetValues);
        optimizerSingularWeight.setStartPoint(startPoint);
        optimizerSingularWeight.setWeight(singularWeightMatrix);
        try {
            optimizerSingularWeight.optimize();
            fail("Expected SingularMatrixException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGaussNewtonOptimizerOptimizeWithDifferentWeights() throws ConvergenceException, SingularMatrixException, NullArgumentException {
        // Prepare data
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1000.0}});

        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);

        // Test optimization
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.0, 2.0};
        Assert.assertArrayEquals(expectedResult, result.getPoint(), 0.001);
    }

    @Test
    public void testGaussNewtonOptimizerWithCustomConvergenceChecker() throws ConvergenceException, SingularMatrixException, NullArgumentException {
        // Prepare data
        double[] targetValues = {1.0, 2.0};
        double[] startPoint = {0.0, 0.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        ConvergenceChecker<PointVectorValuePair> customChecker = new MyCustomConvergenceChecker();

        // Create optimizer
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(targetValues);
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
        optimizer.setConvergenceChecker(customChecker);

        // Test optimization
        PointVectorValuePair result = optimizer.optimize();
        double[] expectedResult = {1.5, 1.5};
        assertArrayEquals(expectedResult, result.getPoint(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerWithNullTarget() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setTarget(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerWithNullStartPoint() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testGaussNewtonOptimizerWithNullWeight() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true);
        optimizer.setWeight(null);
        optimizer.optimize();
    }
}