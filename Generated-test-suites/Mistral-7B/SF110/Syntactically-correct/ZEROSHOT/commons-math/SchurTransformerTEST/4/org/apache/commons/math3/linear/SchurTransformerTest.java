package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;

import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        RealMatrix inputMatrix = RealMatrixFactory.identity(5);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);

        RealMatrix expectedP = RealMatrixFactory.identity(5);
        RealMatrix actualP = transformer.getP();
        assertTrue(expectedP.equals(actualP, 1e-12));

        RealMatrix expectedT = new RealMatrix(new double[][]{
            {1.0, 0.0, 0.0, 0.0, 0.0},
            {0.0, 1.0, 0.0, 0.0, 0.0},
            {0.0, 0.0, 1.0, 0.0, 0.0},
            {0.0, 0.0, 0.0, 1.0, 0.0},
            {0.0, 0.0, 0.0, 0.0, 1.0}
        });
        RealMatrix actualT = transformer.getT();
        assertTrue(expectedT.equals(actualT, 1e-12));

        RealMatrix expectedPt = RealMatrixFactory.identity(5);
        RealMatrix actualPt = transformer.getPT();
        assertTrue(expectedPt.equals(actualPt, 1e-12));

        try {
            SchurTransformer transformer2 = new SchurTransformer(RealMatrixFactory.identity(3));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }

        try {
            SchurTransformer transformer3 = new SchurTransformer(RealMatrixFactory.singular(3, 2));
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // expected
        }
    }
}