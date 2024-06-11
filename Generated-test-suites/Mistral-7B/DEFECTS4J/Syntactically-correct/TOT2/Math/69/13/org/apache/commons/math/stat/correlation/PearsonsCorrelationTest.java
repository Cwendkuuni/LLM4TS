import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link PearsonsCorrelation} class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test for default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertNull(pearsonCorrelation.getCorrelationMatrix());
        assertEquals(0, pearsonCorrelation.getNObs());
    }

    /**
     * Test for constructor with double[][] data.
     */
    @Test
    public void testConstructorWithData() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        };

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        assertEquals(3, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8888888888888889, 0.8181818181818182},
                {0.8888888888888889, 1.0, 0.8181818181818182},
                {0.8181818181818182, 0.8181818181818182, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for constructor with RealMatrix matrix.
     */
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new RealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        });

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        assertEquals(3, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8888888888888889, 0.8181818181818182},
                {0.8888888888888889, 1.0, 0.8181818181818182},
                {0.8181818181818182, 0.8181818181818182, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for constructor with Covariance covariance.
     */
    @Test
    public void testConstructorWithCovariance() throws MathException {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        };

        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        assertEquals(3, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8888888888888889, 0.8181818181818182},
                {0.8888888888888889, 1.0, 0.8181818181818182},
                {0.8181818181818182, 0.8181818181818182, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for constructor with RealMatrix and number of observations.
     */
    @Test
    public void testConstructorWithCovarianceMatrixAndNumberOfObservations() {
        RealMatrix covarianceMatrix = new RealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 4.0, 6.0},
                {3.0, 6.0, 9.0},
                {4.0, 8.0, 12.0}
        });

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 4);
        assertEquals(4, pearsonCorrelation.getNObs());
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8, 0.64, 0.48},
                {0.8, 1.0, 0.8, 0.64},
                {0.64, 0.8, 1.0, 0.8},
                {0.48, 0.64, 0.8, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for getCorrelationMatrix() method.
     */
    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        };

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8888888888888889, 0.8181818181818182},
                {0.8888888888888889, 1.0, 0.8181818181818182},
                {0.8181818181818182, 0.8181818181818182, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for getCorrelationStandardErrors() method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws MathException {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        };

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = pearsonCorrelation.getCorrelationStandardErrors();
        RealMatrix expectedCorrelationStandardErrors = new RealMatrix(new double[][]{
                {0.4714045238095237, 0.5171428571428572, 0.5171428571428572},
                {0.5171428571428572, 0.4714045238095237, 0.5171428571428572},
                {0.5171428571428572, 0.5171428571428572, 0.4714045238095237}
        });
        Assert.assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors);
    }

    /**
     * Test for getCorrelationPValues() method.
     */
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {2.0, 3.0, 4.0},
                {3.0, 4.0, 5.0}
        };

        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();
        RealMatrix expectedCorrelationPValues = new RealMatrix(new double[][]{
                {0.1533333333333333, 0.0222222222222223, 0.0222222222222223},
                {0.0222222222222223, 0.1533333333333333, 0.0222222222222223},
                {0.0222222222222223, 0.0222222222222223, 0.1533333333333333}
        });
        Assert.assertEquals(expectedCorrelationPValues, correlationPValues);
    }

    /**
     * Test for correlation(double[], double[]) method with valid data.
     */
    @Test
    public void testCorrelationValidData() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {2.0, 3.0, 4.0};

        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8888888888888889, correlation, 0.000000001);
    }

    /**
     * Test for correlation(double[], double[]) method with invalid data.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidData() {
        double[] xArray = {1.0};
        double[] yArray = {2.0, 3.0};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    /**
     * Test for covarianceToCorrelation(RealMatrix) method.
     */
    @Test
    public void testCovarianceToCorrelation() {
        RealMatrix covarianceMatrix = new RealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 3.0}
        });

        RealMatrix correlationMatrix = PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
        RealMatrix expectedCorrelationMatrix = new RealMatrix(new double[][]{
                {1.0, 0.8453180811746665},
                {0.8453180811746665, 1.0}
        });
        Assert.assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }
}