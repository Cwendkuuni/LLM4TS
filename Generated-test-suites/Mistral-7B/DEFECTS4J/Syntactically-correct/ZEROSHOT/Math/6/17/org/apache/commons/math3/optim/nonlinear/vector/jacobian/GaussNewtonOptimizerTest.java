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
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleVectorValueChecker;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test default constructor.
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();
        assertNotNull(optimizer);

        // Test constructor with LU decomposition and SimpleVectorValueChecker.
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        assertNotNull(optimizer);

        // Test constructor with QR decomposition and SimpleVectorValueChecker.
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        assertNotNull(optimizer);

        // Test constructor with custom ConvergenceChecker.
        ConvergenceChecker<PointVectorValuePair> convergenceChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair start, PointVectorValuePair current) {
                return current.getValue()[0] <= 1e-10;
            }
        };
        optimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        assertNotNull(optimizer);
    }

    @Test
    public void testDoOptimize() throws Exception {
        // Test with default settings.
        GaussNewtonOptimizer optimizer = new GaussNewtonOptimizer();

        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        double[] startPoint = {1.0, 1.0, 1.0};

        PointVectorValuePair result = optimizer.optimize(targetValues, weightMatrix, startPoint);
        double[] expectedPoint = {1.0, 1.0, 1.0};
        double[] expectedValue = {0.0, 0.0, 0.0};
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-14);
        assertArrayEquals(expectedValue, result.getValue(), 1e-14);

        // Test with LU decomposition.
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        result = optimizer.optimize(targetValues, weightMatrix, startPoint);
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-14);
        assertArrayEquals(expectedValue, result.getValue(), 1e-14);

        // Test with QR decomposition.
        optimizer = new GaussNewtonOptimizer(false, new SimpleVectorValueChecker());
        result = optimizer.optimize(targetValues, weightMatrix, startPoint);
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-14);
        assertArrayEquals(expectedValue, result.getValue(), 1e-14);

        // Test with custom ConvergenceChecker.
        convergenceChecker = new ConvergenceChecker<PointVectorValuePair>() {
            @Override
            public boolean converged(int iteration, PointVectorValuePair start, PointVectorValuePair current) {
                return current.getValue()[0] <= 1e-6;
            }
        };
        optimizer = new GaussNewtonOptimizer(true, convergenceChecker);
        result = optimizer.optimize(targetValues, weightMatrix, startPoint);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
        assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        assertArrayEquals(expectedValue, result.getValue(), 1e-6);

        // Test with null targetValues.
        targetValues = null;
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            fail("NullPointerException expected.");
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with null weightMatrix.
        weightMatrix = null;
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            fail("NullPointerException expected.");
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with null startPoint.
        startPoint = null;
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            fail("NullPointerException expected.");
        } catch (NullArgumentException e) {
            // Expected.
        }

        // Test with singular weightMatrix.
        weightMatrix = new SingularValueDecomposition(new ArrayRealVector(new double[]{-1.0, -1.0}, false)).getMatrixB();
        try {
            optimizer.optimize(targetValues, weightMatrix, startPoint);
            fail("SingularMatrixException expected.");
        } catch (SingularMatrixException e) {
            // Expected.
        }

        // Test with invalid useLU value.
        try {
            optimizer = new GaussNewtonOptimizer(-1, new SimpleVectorValueChecker());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }
}