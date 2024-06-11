import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        // Test with default settings.
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}});
        double[] startPoint = {0.0, 0.0, 0.0};
        PointVectorValuePair solution = optimizer.optimize(targetValues, weightMatrix, startPoint);
        double[] expectedSolution = {1.0, 1.0, 1.0};
        assertEquals(expectedSolution[0], solution.getPoint()[0], 0.001);
        assertEquals(expectedSolution[1], solution.getPoint()[1], 0.001);
        assertEquals(expectedSolution[2], solution.getPoint()[2], 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);

        // Test with LU decomposition and a custom convergence checker.
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker(1e-6, 1e-6));
        solution = optimizer.optimize(targetValues, weightMatrix, startPoint);
        assertArrayEquals(expectedSolution, solution.getPoint(), 0.001);
        assertEquals(0.0, solution.getValue(), 0.001);

        // Test with QR decomposition and a null convergence checker.
        optimizer = new GaussNewtonOptimizer(false, null);
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            throw new AssertionError("A NullPointerException should have been thrown.");
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with a singular weight matrix.
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            throw new AssertionError("A SingularMatrixException should have been thrown.");
        } catch (SingularMatrixException e) {
            // Expected.
        }

        // Test with a non-square weight matrix.
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {0.0, 1.0}});
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            throw new AssertionError("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected.
        }

        // Test with a non-positive definite weight matrix.
        weightMatrix = new BlockRealMatrix(new double[][]{{-1.0, 0.0}, {0.0, -1.0}});
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            throw new AssertionError("An IllegalStateException should have been thrown.");
        } catch (IllegalStateException e) {
            // Expected.
        }
    }
}