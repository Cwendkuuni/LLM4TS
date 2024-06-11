import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimplePointsValueChecker;
import org.apache.commons.math3.optim.unconstrained.GaussNewtonOptimizer;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with LU decomposition
        GaussNewtonOptimizer optimizerLU = new GaussNewtonOptimizer(true);
        double[] targetValues = {1.0, 2.0, 3.0};
        RealMatrix weightMatrix = MatrixUtils.identityMatrix(3);
        double[] startPoint = {0.0, 0.0, 0.0};
        PointValuePair solutionLU = optimizerLU.optimize(targetValues, weightMatrix, startPoint);
        Assert.assertEquals(0.0, solutionLU.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solutionLU.getPoint(), 1e-6);

        // Test with QR decomposition
        GaussNewtonOptimizer optimizerQR = new GaussNewtonOptimizer(false);
        PointValuePair solutionQR = optimizerQR.optimize(targetValues, weightMatrix, startPoint);
        Assert.assertEquals(0.0, solutionQR.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solutionQR.getPoint(), 1e-6);

        // Test convergence checker
        GaussNewtonOptimizer optimizerChecker = new GaussNewtonOptimizer(true, new SimplePointsValueChecker(1e-5, 1e-5));
        double[] targetValuesChecker = {1.0, 2.0, 3.0};
        RealMatrix weightMatrixChecker = MatrixUtils.identityMatrix(3);
        double[] startPointChecker = {0.0, 0.0, 0.0};
        PointValuePair solutionChecker = optimizerChecker.optimize(targetValuesChecker, weightMatrixChecker, startPointChecker);
        Assert.assertEquals(0.0, solutionChecker.getValue(), 1e-6);
        Assert.assertArrayEquals(new double[]{1.0, 1.0, 1.0}, solutionChecker.getPoint(), 1e-6);
    }
}