package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        // Test initialization with valid input
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 9.0}
        });
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{
                {0.18035430342793923, -0.7453086788633882, 0.6787876239105261},
                {-0.7453086788633882, 0.13870918728329958, 0.6123213829331865},
                {0.6787876239105261, 0.6123213829331865, 0.3730638693474227}
        });
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {0.0, 3.453662802308392, 2.761552354365241},
                {0.0, 0.0, 5.888435312594632}
        });
        RealMatrix expectedPt = transformer.getPT();
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        RealMatrix actualPt = transformer.getPT();

        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 0.0);

        // Test initialization with non-square matrix
        try {
            inputMatrix = new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {2.0, 3.0},
                    {3.0, 4.0}
            });
            new SchurTransformer(inputMatrix);
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }

        // Test convergence failure
        inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 9.0},
                {0.0, 1.0, 1.0}
        });
        try {
            transformer = new SchurTransformer(inputMatrix);
            transformer.transform();
            Assert.fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }

        // Test getP() method with cached result
        transformer = new SchurTransformer(inputMatrix);
        actualP = transformer.getP();
        Assert.assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);

        // Test getPT() method with cached result
        actualPt = transformer.getPT();
        Assert.assertArrayEquals(expectedPt.getData(), actualPt.getData(), 0.0);

        // Test getT() method with cached result
        actualT = transformer.getT();
        Assert.assertArrayEquals(expectedT.getData(), actualT.getData(), 0.0);
    }
}