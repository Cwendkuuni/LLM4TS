package org.apache.commons.math.stat.correlation;

import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testCorrelationMatrixFromData() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.828427536231884, 0.866025403784439},
                {0.828427536231884, 1.0, 0.866025403784439},
                {0.866025403784439, 0.866025403784439, 1.0}});

        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationMatrixFromCovariance() {
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][]{{1.1, 0.3, 0.2},
                {0.3, 1.5, 0.4},
                {0.2, 0.4, 2.5}});

        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.632450178724431, 0.535533852278856},
                {0.632450178724431, 1.0, 0.535533852278856},
                {0.535533852278856, 0.535533852278856, 1.0}});

        RealMatrix actual = pearsonsCorrelation.getCorrelationMatrix();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationStandardErrors() {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.773773171462391, 0.734385555754736},
                {0.734385555754736, 0.773773171462391}});

        RealMatrix actual = pearsonsCorrelation.getCorrelationStandardErrors();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1.0, 2.0, 3.0}, {2.1, 3.1, 4.1}, {3.2, 4.2, 5.2}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.292839473786773, 0.288031366966944},
                {0.288031366966944, 0.292839473786773}});

        RealMatrix actual = pearsonsCorrelation.getCorrelationPValues();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalArgumentException() {
        double[][] data = {{1.0}, {2.0, 3.0}};
        new PearsonsCorrelation(data);
    }

    @Test(expected = MathRuntimeException.class)
    public void testIllegalArgumentException2() {
        double[][] data = {{1.0, 2.0}, {2.0}};
        new PearsonsCorrelation(data);
    }
}