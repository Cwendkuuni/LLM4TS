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
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.UnconstrainedOptimizationException;
import org.apache.commons.math3.optim.nonlinear.function.Functions;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.math3.optim.Points.*;

/**
 * JUnit Test file for {@link GaussNewtonOptimizer} class.
 */
public class GaussNewtonOptimizerTest {

    @Test
    public void testGaussNewtonOptimizer() {
        // Test with LU decomposition.
        GaussNewtonOptimizer optimizer1 = new GaussNewtonOptimizer(true);
        double[] targetValues = {2.0, 2.0};
        RealMatrix weightMatrix = new BlockRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        double[] startPoint = {1.0, 1.0};
        PointVectorValuePair solution1 = optimizer1.optimize(new GeneralRealValuedFunction() {
            @Override
            public double value(RealVector x) {
                double[] xArray = x.toArray();
                return Math.pow(Functions.subtract(xArray, new double[]{1.0, 1.0}), 2.0).sum();
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                return new ArrayRealVector(new double[]{2.0 * (xArray[0] - 1.0), 2.0 * (xArray[1] - 1.0)});
            }
        }, targetValues, weightMatrix, startPoint);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, solution1.getPoint(), 1e-11);

        // Test with QR decomposition.
        GaussNewtonOptimizer optimizer2 = new GaussNewtonOptimizer(false);
        PointVectorValuePair solution2 = optimizer2.optimize(new GeneralRealValuedFunction() {
            @Override
            public double value(RealVector x) {
                double[] xArray = x.toArray();
                return Math.pow(Functions.subtract(xArray, new double[]{1.0, 1.0}), 2.0).sum();
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                return new ArrayRealVector(new double[]{2.0 * (xArray[0] - 1.0), 2.0 * (xArray[1] - 1.0)});
            }
        }, targetValues, weightMatrix, startPoint);
        Assert.assertArrayEquals(new double[]{1.0, 1.0}, solution2.getPoint(), 1e-11);

        // Test with incorrect input arguments.
        GaussNewtonOptimizer optimizer3 = new GaussNewtonOptimizer(true);
        try {
            optimizer3.optimize(null, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (NullArgumentException e) {
            // Expected.
        }

        try {
            optimizer3.optimize(new GeneralRealValuedFunction() {
                @Override
                public double value(RealVector x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new UnsupportedOperationException();
                }
            }, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (UnsupportedOperationException e) {
            // Expected.
        }

        try {
            optimizer3.optimize(new GeneralRealValuedFunction() {
                @Override
                public double value(RealVector x) {
                    return 0.0;
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new SingularValueDecomposition.SingularValueDecompositionException("Singular matrix");
                }
            }, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (SingularMatrixException e) {
            // Expected.
        }

        try {
            optimizer3.optimize(new GeneralRealValuedFunction() {
                @Override
                public double value(RealVector x) {
                    return 0.0;
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new ConvergenceException(LocalizedFormats.ITERATION_LIMIT_EXCEEDED);
                }
            }, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (ConvergenceException e) {
            // Expected.
        }

        try {
            optimizer3.optimize(new GeneralRealValuedFunction() {
                @Override
                public double value(RealVector x) {
                    return Math.log(x.getEntry(0));
                }

                @Override
                public RealVector gradient(RealVector x) {
                    return new ArrayRealVector(new double[]{1.0 / x.getEntry(0)});
                }
            }, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (MathInternalError e) {
            // Expected.
        }

        try {
            optimizer3.optimize(new GeneralRealValuedFunction() {
                @Override
                public double value(RealVector x) {
                    return 0.0;
                }

                @Override
                public RealVector gradient(RealVector x) {
                    throw new ArrayIndexOutOfBoundsException(1);
                }
            }, targetValues, weightMatrix, startPoint);
            Assert.fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected.
        }

        // Test convergence checking.
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(1e-11);
        GaussNewtonOptimizer optimizer4 = new GaussNewtonOptimizer(true, checker);
        targetValues = new double[]{0.1, 0.2};
        weightMatrix = new BlockRealMatrix(new double[][]{{1.0 / 100.0, 0.0}, {0.0, 1.0 / 100.0}});
        startPoint = new double[]{1.0, 1.0};
        PointVectorValuePair solution4 = optimizer4.optimize(new GeneralRealValuedFunction() {
            @Override
            public double value(RealVector x) {
                double[] xArray = x.toArray();
                return Math.pow(Functions.subtract(xArray, new double[]{0.1, 0.2}), 2.0).sum();
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                return new ArrayRealVector(new double[]{2.0 * (xArray[0] - 0.1), 2.0 * (xArray[1] - 0.2)});
            }
        }, targetValues, weightMatrix, startPoint);
        Assert.assertArrayEquals(new double[]{0.1, 0.2}, solution4.getPoint(), 1e-11);

        checker = new SimpleVectorValueChecker(1e-8);
        GaussNewtonOptimizer optimizer5 = new GaussNewtonOptimizer(true, checker);
        PointVectorValuePair solution5 = optimizer5.optimize(new GeneralRealValuedFunction() {
            @Override
            public double value(RealVector x) {
                double[] xArray = x.toArray();
                return Math.pow(Functions.subtract(xArray, new double[]{1.0, 1.0}), 2.0).sum();
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                return new ArrayRealVector(new double[]{2.0 * (xArray[0] - 1.0), 2.0 * (xArray[1] - 1.0)});
            }
        }, targetValues, weightMatrix, startPoint);
        Assert.assertTrue(checker.isConverged(solution5));

        checker = new SimpleVectorValueChecker(1e-6);
        GaussNewtonOptimizer optimizer6 = new GaussNewtonOptimizer(true, checker);
        PointVectorValuePair solution6 = optimizer6.optimize(new GeneralRealValuedFunction() {
            @Override
            public double value(RealVector x) {
                double[] xArray = x.toArray();
                return Math.pow(Functions.subtract(xArray, new double[]{1.0, 1.0}), 2.0).sum();
            }

            @Override
            public RealVector gradient(RealVector x) {
                double[] xArray = x.toArray();
                return new ArrayRealVector(new double[]{2.0 * (xArray[0] - 1.0), 2.0 * (xArray[1] - 1.0)});
            }
        }, targetValues, weightMatrix, startPoint);
        Assert.assertFalse(checker.isConverged(solution6));
    }
}