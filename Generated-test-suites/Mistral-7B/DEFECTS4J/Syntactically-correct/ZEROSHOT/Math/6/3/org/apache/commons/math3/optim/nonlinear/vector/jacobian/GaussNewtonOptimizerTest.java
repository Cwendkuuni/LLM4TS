import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;

import static org.junit.Assert.*;

import java.util.Arrays;

public class GaussNewtonOptimizerTest {

    @org.junit.Test
    public void testGaussNewtonOptimizerWithLUDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        PointVectorValuePair result = optimizer.optimize(targetValues, weightMatrix, startPoint);

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        assertEquals(expectedPoint, result.getPoint(), 0.001);
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testNullTargetValues() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        optimizer.optimize(null, new RealMatrix(3, 3), new double[]{0.0, 0.0, 0.0});
    }

    @org.junit.Test(expected = NullArgumentException.class)
    public void testNullWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        optimizer.optimize(new double[]{0.0, 0.0, 0.0}, null, new double[]{0.0, 0.0, 0.0});
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testEmptyWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        optimizer.optimize(new double[]{0.0}, new RealMatrix(0, 3), new double[]{0.0, 0.0, 0.0});
    }

    @org.junit.Test
    public void testGaussNewtonOptimizerWithQRDecomposition() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        PointVectorValuePair result = optimizer.optimize(targetValues, weightMatrix, startPoint);

        double[] expectedPoint = new double[]{1.0, 1.0, 1.0};
        assertEquals(expectedPoint, result.getPoint(), 0.001);
    }

    @org.junit.Test(expected = ConvergenceException.class)
    public void testUnconvergedIteration() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0},
                {1.0, 1.0, 1.0}
        });
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        optimizer.setMaxIterations(2);
        optimizer.optimize(targetValues, weightMatrix, startPoint);
    }

    @org.junit.Test
    public void testSingularWeightMatrix() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());

        double[] targetValues = new double[]{1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{
                {0.0, 0.0, 1.0},
                {0.0, 1.0, 0.0},
                {1.0, 0.0, 0.0}
        });
        double[] startPoint = new double[]{0.0, 0.0, 0.0};

        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            fail("Expected ConvergenceException");
        } catch (SingularMatrixException e) {
            // Expected
        }
    }
}