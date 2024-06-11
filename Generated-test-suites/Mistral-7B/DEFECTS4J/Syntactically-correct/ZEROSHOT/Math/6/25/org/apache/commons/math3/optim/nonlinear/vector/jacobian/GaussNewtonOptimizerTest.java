import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;
    private RealMatrix weightMatrix;
    private double[] targetValues;
    private double[] startPoint;

    @Before
    public void setUp() {
        targetValues = new double[]{0.5, 2.0, 3.5};
        weightMatrix = MatrixUtils.identityMatrix(targetValues.length);
        startPoint = new double[]{1.0, 1.0, 1.0};
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
        optimizer.setFunction(new SimpleRegressionFunction(targetValues));
        optimizer.setStartPoint(startPoint);
        optimizer.setWeight(weightMatrix);
    }

    @Test
    public void testOptimizeSuccess() throws Exception {
        optimizer.optimize();
        PointValuePair result = optimizer.getPointValuePair();
        RealVector point = result.getPoint();
        double[] expectedPoint = {1.150261412414137, 1.786620613561695, 2.557587423163131};
        Assert.assertArrayEquals(expectedPoint, point.toArray(), 1e-6);
        Assert.assertEquals(1.675285971186749E-8, optimizer.getCost(), 1e-8);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullStartPoint() {
        optimizer.setStartPoint(null);
        optimizer.optimize();
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullWeightMatrix() {
        optimizer.setWeight(null);
        optimizer.optimize();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeNoFunctionSet() {
        optimizer.optimize();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws Exception {
        optimizer.setFunction(new NonConvergentFunction());
        optimizer.optimize();
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularLinearProblem() throws Exception {
        RealMatrix weightMatrixSingular = MatrixUtils.createRealSymmetricMatrix(new double[][]{{1, 0}, {0, 0}});
        optimizer.setWeight(weightMatrixSingular);
        optimizer.optimize();
    }
}

class SimpleRegressionFunction implements GeneralRealValuedFunction {

    private final double[] targetValues;

    public SimpleRegressionFunction(double[] targetValues) {
        this.targetValues = targetValues;
    }

    @Override
    public double value(RealVector point) {
        double[] x = point.toArray();
        double sum = 0;
        for (int i = 0; i < x.length; ++i) {
            sum += Math.pow(x[i] - targetValues[i], 2);
        }
        return sum;
    }

    @Override
    public RealVector gradient(RealVector point) {
        double[] x = point.toArray();
        double[] grad = new double[x.length];
        for (int i = 0; i < x.length; ++i) {
            grad[i] = 2 * (x[i] - targetValues[i]);
        }
        return new ArrayRealVector(grad, false);
    }
}

class NonConvergentFunction implements GeneralRealValuedFunction {

    @Override
    public double value(RealVector point) {
        throw new RuntimeException("Non-convergent function");
    }

    @Override
    public RealVector gradient(RealVector point) {
        throw new RuntimeException("Non-convergent function");
    }
}