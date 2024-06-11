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
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class GaussNewtonOptimizerTest {

    private static final double EPSILON = 1e-6;
    private static final double EPSILON_SQUARE = EPSILON * EPSILON;

    private GaussNewtonOptimizer optimizer;
    private double[] inputData;
    private double[] targetValues;
    private double[] startPoint;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        optimizer = new GaussNewtonOptimizer(true, new SimpleVectorValueChecker());
    }

    @Test
    public void testNullInputData() {
        try {
            optimizer.optimize(null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullTargetValues() {
        try {
            optimizer.optimize(inputData, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullStartPoint() {
        try {
            optimizer.optimize(inputData, targetValues, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNullWeightMatrix() {
        try {
            optimizer.optimize(inputData, targetValues, startPoint, null);
            Assert.fail();
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptimizeWithEmptyData() {
        optimizer.optimize(new double[0], new double[0], startPoint, weightMatrix);
    }

    @Test
    public void testConvergenceWithSimpleExample() {
        inputData = new double[]{1.0, 2.0, 3.0};
        targetValues = new double[]{1.5, 2.5, 3.5};
        startPoint = new double[]{0.0, 0.0, 0.0};
        weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        PointVectorValuePair result = optimizer.optimize(inputData, targetValues, startPoint, weightMatrix);
        Assert.assertArrayEquals(new double[]{1.5, 1.5, 1.5}, result.getPoint(), EPSILON);
    }

    @Test
    public void testConvergenceWithSingularMatrixExample() {
        inputData = new double[]{1.0, 2.0, 3.0};
        targetValues = new double[]{1.5, 2.5, 3.5};
        startPoint = new double[]{0.0, 0.0, 0.0};
        weightMatrix = new BlockRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        try {
            optimizer.optimize(inputData, targetValues, startPoint, weightMatrix);
            Assert.fail();
        } catch (ConvergenceException e) {
            // expected
        }
    }

    @Test
    public void testConvergenceWithLargeSystem() {
        int n = 100;
        int m = 10;
        Random random = new Random();

        double[][] inputDataMatrix = new double[n][m];
        double[] targetValues = new double[n];
        double[] startPoint = new double[m];
        weightMatrix = new BlockRealMatrix(n, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                inputDataMatrix[i][j] = random.nextGaussian();
            }
            targetValues[i] = random.nextGaussian();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                weightMatrix.setEntry(i, j, Math.max(Math.abs(inputDataMatrix[i][j]), 1e-6));
            }
        }

        PointVectorValuePair result = optimizer.optimize(inputDataMatrix, targetValues, startPoint, weightMatrix);
        RealVector point = result.getPoint();
        RealVector expected = new SingularValueDecomposition(new BlockRealMatrix(inputDataMatrix)).getSolution();

        Assert.assertArrayEquals(expected.toArray(), point.toArray(), EPSILON);
    }

    @Test
    public void testPerformanceWithLargeSystem() {
        int n = 10_000;
        int m = 10;
        Random random = new Random();

        double[][] inputDataMatrix = new double[n][m];
        double[] targetValues = new double[n];
        double[] startPoint = new double[m];
        weightMatrix = new BlockRealMatrix(n, m);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                inputDataMatrix[i][j] = random.nextGaussian();
            }
            targetValues[i] = random.nextGaussian();
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                weightMatrix.setEntry(i, j, Math.max(Math.abs(inputDataMatrix[i][j]), 1e-6));
            }
        }

        long startTime = System.nanoTime();
        PointVectorValuePair result = optimizer.optimize(inputDataMatrix, targetValues, startPoint, weightMatrix);
        long endTime = System.nanoTime();

        RealVector point = result.getPoint();
        RealVector expected = new SingularValueDecomposition(new BlockRealMatrix(inputDataMatrix)).getSolution();

        Assert.assertArrayEquals(expected.toArray(), point.toArray(), EPSILON);

        System.out.println("Time spent: " + (endTime - startTime) / 1e9 + "s");
    }
}