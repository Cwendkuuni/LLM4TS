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
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconstrainedOptimizationProblem;
import org.apache.commons.math3.optim.nonlinear.simple.GaussNewtonOptimizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GaussNewtonOptimizerTest {

    private GaussNewtonOptimizer optimizer;

    @Before
    public void setup() {
        double[] coefficients = new double[]{1.0, 2.0, 3.0, 4.0};
        RealMatrix weight = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        RealVector startPoint = new ArrayRealVector(new double[]{1.0, 0.5, 1.5, 1.2});
        PointValuePair expectedResult = new PointValuePair(new ArrayRealVector(new double[]{1.5, 0.5, 1.5, 1.5}), 0.0);

        UnconstrainedOptimizationProblem problem = new UnconstrainedOptimizationProblem() {
            @Override
            public PointValuePair value(RealVector x) {
                double[] xArray = x.toArray();
                double[] targetValues = new double[]{Math.pow(xArray[0] - 1.0, 2.0) + Math.pow(xArray[1] - 0.5, 2.0) + Math.pow(xArray[2] - 1.5, 2.0) + Math.pow(xArray[3] - 1.2, 2.0),
                        2.0 * (xArray[0] - 1.0) * (xArray[0] - 1.0) + 2.0 * (xArray[1] - 0.5) * (xArray[1] - 0.5) + 2.0 * (xArray[2] - 1.5) * (xArray[2] - 1.5) + 2.0 * (xArray[3] - 1.2) * (xArray[3] - 1.2)};
                return new PointValuePair(new ArrayRealVector(x), targetValues[0]);
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                double[] gradient = new double[xArray.length];
                for (int i = 0; i < xArray.length; i++) {
                    gradient[i] = 4.0 * (xArray[i] - 1.0);
                }
                return new ArrayRealVector(gradient);
            }

            @Override
            public RealMatrix jacobian(RealVector x) {
                double[] xArray = x.toArray();
                double[][] jacobian = new double[1][xArray.length];
                for (int i = 0; i < xArray.length; i++) {
                    jacobian[0][i] = 4.0 * xArray[i];
                }
                return new BlockRealMatrix(jacobian);
            }
        };

        optimizer = new GaussNewtonOptimizer(coefficients.length, new SimpleVectorValueChecker(1.0e-8));
        optimizer.setMaxEval(new MaxEval(200));
        optimizer.setInitialGuess(new InitialGuess(startPoint));
        optimizer.optimize(problem);
    }

    @Test
    public void testGaussNewtonOptimizer() {
        Assert.assertArrayEquals(optimizer.getPoint().toArray(), expectedResult.getPoint().toArray(), 0.0);
        Assert.assertEquals(optimizer.getValue(), expectedResult.getValue(), 0.0);

        try {
            optimizer.optimize();
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            optimizer.optimize(null);
        } catch (NullArgumentException e) {
            // Expected
        }

        try {
            optimizer.optimize(new UnconstrainedOptimizationProblem() {
                @Override
                public PointValuePair value(RealVector x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public RealMatrix jacobian(RealVector x) {
                    throw new UnsupportedOperationException();
                }
            });
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        try {
            optimizer.optimize(new UnconstrainedOptimizationProblem() {
                @Override
                public PointValuePair value(RealVector x) {
                    double[] xArray = x.toArray();
                    double[] targetValues = new double[]{Math.pow(xArray[0] - 1.0, 2.0) + Math.pow(xArray[1] - 0.5, 2.0), Math.pow(xArray[0] - 1.0, 2.0) + Math.pow(xArray[1] - 0.5, 2.0)};
                    return new PointValuePair(new ArrayRealVector(x), targetValues[0]);
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public RealMatrix jacobian(RealVector x) {
                    SingularValueDecomposition svd = new SingularValueDecomposition(new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}}));
                    return svd.getMoorePenroseInverse();
                }
            });
        } catch (SingularMatrixException e) {
            // Expected
        }
    }
}