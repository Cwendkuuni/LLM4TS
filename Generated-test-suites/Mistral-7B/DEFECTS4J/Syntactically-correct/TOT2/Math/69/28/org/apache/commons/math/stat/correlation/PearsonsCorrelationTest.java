import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThrows;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        assertEquals(0, pearsonCorrelation.getCorrelationMatrix().getRowDimension());
        assertEquals(0, pearsonCorrelation.getCorrelationMatrix().getColumnDimension());
    }

    @Test
    public void testDataConstructor() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8495581395245732, 0.886226925452758},
                {0.8495581395245732, 1.0, 0.886226925452758},
                {0.886226925452758, 0.886226925452758, 1.0}});

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.001);
    }

    @Test
    public void testMatrixConstructor() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8495581395245732, 0.886226925452758},
                {0.8495581395245732, 1.0, 0.886226925452758},
                {0.886226925452758, 0.886226925452758, 1.0}});

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.001);
    }

    @Test
    public void testCovarianceConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2, 0},
                {2, 3, 1},
                {0, 1, 2}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8164965809277261, 0.4714045228269731},
                {0.8164965809277261, 1.0, 0.712890126966253},
                {0.4714045228269731, 0.712890126966253, 1.0}});

        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.001);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidDataConstructor() {
        double[][] data = new double[][]{{1, 2}, {4, 5}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2},
                {4, 5}});
        new PearsonsCorrelation(covarianceMatrix, 3);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.8495581395245732, 0.886226925452758},
                {0.8495581395245732, 1.0, 0.886226925452758},
                {0.886226925452758, 0.886226925452758, 1.0}});

        assertEquals(3, correlationMatrix.getRowDimension());
        assertEquals(3, correlationMatrix.getColumnDimension());
        assertArrayEquals(expectedCorrelationMatrix.getData(), correlationMatrix.getData(), 0.001);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix standardErrors = pearsonCorrelation.getCorrelationStandardErrors();

        assertEquals(3, standardErrors.getRowDimension());
        assertEquals(3, standardErrors.getColumnDimension());
        assertEquals(0.4359140361836225, standardErrors.getEntry(0, 0), 0.001);
        assertEquals(0.4359140361836225, standardErrors.getEntry(1, 0), 0.001);
        assertEquals(0.5447281433306672, standardErrors.getEntry(0, 1), 0.001);
        assertEquals(0.5447281433306672, standardErrors.getEntry(1, 1), 0.001);
        assertEquals(0.5447281433306672, standardErrors.getEntry(2, 1), 0.001);
        assertEquals(0.4359140361836225, standardErrors.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix correlationPValues = pearsonCorrelation.getCorrelationPValues();

        assertEquals(3, correlationPValues.getRowDimension());
        assertEquals(3, correlationPValues.getColumnDimension());
        assertEquals(0.0174866825861364, correlationPValues.getEntry(0, 0), 0.001);
        assertEquals(0.0174866825861364, correlationPValues.getEntry(1, 0), 0.001);
        assertEquals(0.016632253816002176, correlationPValues.getEntry(0, 1), 0.001);
        assertEquals(0.016632253816002176, correlationPValues.getEntry(1, 1), 0.001);
        assertEquals(0.016632253816002176, correlationPValues.getEntry(1, 2), 0.001);
        assertEquals(0.0174866825861364, correlationPValues.getEntry(2, 0), 0.001);
        assertEquals(0.016632253816002176, correlationPValues.getEntry(2, 1), 0.001);
        assertEquals(0.016632253816002176, correlationPValues.getEntry(2, 2), 0.001);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3, 4, 5};
        double[] yArray = {2, 4, 6, 8, 10};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        assertEquals(0.8, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {2};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationDataSize() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {2, 4, 6, 8, 10, 12};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = MathRuntimeException.class)
    public void testInvalidCovarianceMatrix() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{
                {1, 2},
                {4, 5},
                {0, 1}});
        PearsonsCorrelation.covarianceToCorrelation(covarianceMatrix);
    }
}