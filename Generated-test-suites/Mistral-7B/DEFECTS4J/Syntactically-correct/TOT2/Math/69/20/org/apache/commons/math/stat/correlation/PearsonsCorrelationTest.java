import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for the PearsonsCorrelation class.
 */
public class PearsonsCorrelationTest {

    /**
     * Test for the constructor without data.
     */
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pc = new PearsonsCorrelation();
        assertNull(pc.getCorrelationMatrix());
        assertEquals(0, pc.getNObs());
    }

    /**
     * Test for the constructor from a double array.
     */
    @Test
    public void testFromDoubleArray() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888889, 0.774596669243601},
                {0.8888888888888889, 1.0, 0.9511224801556491},
                {0.774596669243601, 0.9511224801556491, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(6.0, pc.getNObs());
    }

    /**
     * Test for the constructor from a RealMatrix.
     */
    @Test
    public void testFromRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        PearsonsCorrelation pc = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888889, 0.774596669243601},
                {0.8888888888888889, 1.0, 0.9511224801556491},
                {0.774596669243601, 0.9511224801556491, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
        assertEquals(6.0, pc.getNObs());
    }

    /**
     * Test for the getCorrelationStandardErrors method.
     */
    @Test
    public void testGetCorrelationStandardErrors() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationStandardErrors = pc.getCorrelationStandardErrors();
        double[][] expectedCorrelationStandardErrors = new double[][]{{0.4898979591836735, 0.3872983344262296, 0.31622776601683796},
                {0.3872983344262296, 0.4898979591836735, 0.4494897959183673},
                {0.31622776601683796, 0.4494897959183673, 0.4898979591836735}};
        assertEquals(expectedCorrelationStandardErrors, correlationStandardErrors.getData());
    }

    /**
     * Test for the getCorrelationPValues method.
     */
    @Test
    public void testGetCorrelationPValues() throws Exception {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        PearsonsCorrelation pc = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pc.getCorrelationMatrix();
        RealMatrix correlationPValues = pc.getCorrelationPValues();
        double[][] expectedCorrelationPValues = new double[][]{{1.0, 0.0123162343056252, 0.001360465468509224},
                {0.0123162343056252, 1.0, 0.005279988561946344},
                {0.001360465468509224, 0.005279988561946344, 1.0}};
        assertEquals(expectedCorrelationPValues, correlationPValues.getData());
    }

    /**
     * Test for the correlation method with two arrays.
     */
    @Test
    public void testCorrelationWithArrays() {
        double[] xArray = {1.0, 2.0, 3.0};
        double[] yArray = {4.0, 5.0, 6.0};
        PearsonsCorrelation pc = new PearsonsCorrelation();
        double correlation = pc.correlation(xArray, yArray);
        double expectedCorrelation = 0.8888888888888889;
        assertEquals(expectedCorrelation, correlation, 0.001);
    }

    /**
     * Test for the covarianceToCorrelation method.
     */
    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{2.25, 1.5, 1.5}, {1.5, 3.25, 2.5}, {1.5, 2.5, 4.25}});
        RealMatrix correlationMatrix = new PearsonsCorrelation().covarianceToCorrelation(covarianceMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8888888888888889, 0.774596669243601},
                {0.8888888888888889, 1.0, 0.9511224801556491},
                {0.774596669243601, 0.9511224801556491, 1.0}});
        assertEquals(expectedCorrelationMatrix, correlationMatrix);
    }

    /**
     * Test for the checkSufficientData method.
     */
    @Test(expected = MathRuntimeException.class)
    public void testCheckSufficientData() {
        new PearsonsCorrelation(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}}));
    }
}