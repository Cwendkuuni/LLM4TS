import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealArray2D;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix emptyMatrix = new Array2DRowRealMatrix(new RealArray2D.Dense2D(0, 0));
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(emptyMatrix);
        Assert.assertNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix identityMatrix = new Array2DRowRealMatrix(new RealArray2D.Dense2D(2, 2).setIdentity(true));
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(identityMatrix);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(new double[][]{
            {1.0, 0.0},
            {0.0, 1.0}
        }, correlationMatrix.getData(), 0.0);
    }

    @Test
    public void testDeterminantNegative() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, -1.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(new BlockRealMatrix(data));
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(new double[][]{
            {-0.8, 0.6},
            {0.6, -0.8}
        }, correlationMatrix.getData(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataIsNotSquare() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0}};
        new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataHasLessThanTwoColumns() {
        double[][] data = new double[][]{{1.0}};
        new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    @Test(expected = MathException.class)
    public void testInvalidData() {
        double[][] data = new double[][]{{1.0, Integer.MAX_VALUE}, {Integer.MAX_VALUE, 2.0}};
        new PearsonsCorrelation(new BlockRealMatrix(data));
    }

    @Test
    public void testStandardErrors() {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(new BlockRealMatrix(data));
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix standardErrors = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertArrayEquals(new double[][]{
            {0.9486832980505759, 0.9486832980505759},
            {0.9486832980505759, 0.9486832980505759}
        }, correlationMatrix.getData(), 0.0);
        Assert.assertArrayEquals(new double[][]{
            {0.2821073129771672, 0.2821073129771672},
            {0.2821073129771672, 0.2821073129771672}
        }, standardErrors.getData(), 0.0);
    }

    @Test
    public void testPValues() throws MathException {
        double[][] data = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(new BlockRealMatrix(data));
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        RealMatrix pValues = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertArrayEquals(new double[][]{
            {0.1017321101265278, 0.8982678898734722},
            {0.8982678898734722, 0.1017321101265278}
        }, correlationMatrix.getData(), 0.0);
        Assert.assertArrayEquals(new double[][]{
            {0.4523526574068341, 0.5476473425931659},
            {0.5476473425931659, 0.4523526574068341}
        }, pValues.getData(), 0.001);
    }

    @Test
    public void testCovariance() {
        double[][] data = new double[][]{{1, 2, 3, 4}, {2, 3, 4, 5}, {3, 4, 5, 6}, {4, 5, 6, 7}};
        Covariance covariance = new Covariance(data);
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertArrayEquals(new double[][]{
            {1.0, 0.6788074951253167, 0.4224474960421063, 0.3843128528930727},
            {0.6788074951253167, 1.0, 0.6788074951253167, 0.6245172167087784},
            {0.4224474960421063, 0.6788074951253167, 1.0, 0.8174767303993681},
            {0.3843128528930727, 0.6245172167087784, 0.8174767303993681, 1.0}
        }, correlationMatrix.getData(), 0.0);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.8, correlation, 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData1() {
        double[] xArray = {1};
        double[] yArray = {};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData2() {
        double[] xArray = {};
        double[] yArray = {1};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCorrelationData3() {
        double[] xArray = {1, 2};
        double[] yArray = {1, 2, 3};
        PearsonsCorrelation.correlation(xArray, yArray);
    }
}