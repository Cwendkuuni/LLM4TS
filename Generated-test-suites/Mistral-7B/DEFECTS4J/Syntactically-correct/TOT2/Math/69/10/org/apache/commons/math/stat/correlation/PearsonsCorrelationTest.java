import org.apache.commons.math3.LinearAlgebra.MatrixUtils;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.getCorrelationMatrix());
        Assert.assertEquals(0, pearsonCorrelation.getnObs());
    }

    @Test
    public void testTwoDArrayConstructor() {
        double[][] data = { {1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.8333333333333333, 0.8148148148148148},
                {0.8333333333333333, 1.0, 0.8333333333333333},
                {0.8148148148148148, 0.8333333333333333, 1.0}
        });

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
        Assert.assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testRealMatrixConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 1.5, 2.0},
                {2.5, 3.0, 3.5},
                {3.0, 4.0, 4.5}
        });

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.8333333333333333, 0.822471875},
                {0.8333333333333333, 1.0, 0.8571428571428571},
                {0.822471875, 0.8571428571428571, 1.0}
        });

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
        Assert.assertEquals(3, pearsonCorrelation.getnObs());
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {3.0, 2.5},
                {2.5, 4.0}
        });

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 2);

        RealMatrix expectedCorrelationMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.8333333333333333},
                {0.8333333333333333, 1.0}
        });

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
        Assert.assertEquals(2, pearsonCorrelation.getnObs());
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = { {1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedCorrelationStandardErrors = MatrixUtils.createRealMatrix(new double[][]{
                {0.4898979497060273, 0.4640845751914769},
                {0.4640845751914769, 0.4898979497060273}
        });

        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = { {1.0, 2.0, 3.0}, {2.0, 3.0, 4.0}, {3.0, 4.0, 5.0} };
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);

        RealMatrix expectedCorrelationPValues = MatrixUtils.createRealMatrix(new double[][]{
                {0.999802531896076, 0.9996682658765467},
                {0.9996682658765467, 0.999802531896076}
        });

        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        Assert.assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationNegativeData() {
        double[][] data = { {-1.0, 2.0}, {2.0, -3.0} };
        new PearsonsCorrelation(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationSingularMatrix() {
        double[][] data = { {1.0, 2.0}, {2.0, 1.0} };
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathException.class)
    public void testCorrelationPValuesSingularMatrix() throws MathException {
        double[][] data = { {1.0, 2.0}, {2.0, 1.0} };
        new PearsonsCorrelation(data).getCorrelationPValues();
    }
}