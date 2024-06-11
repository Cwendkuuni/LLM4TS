package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.UtilIterators;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformer() {
        double[][] inputMatrixData = {
                {1.0, 2.0, 1.0},
                {2.0, 3.0, 2.0},
                {1.0, 2.0, 1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53846072, 0.81649658, 0.0},
                {-0.81649658, 0.53846072, 0.0}, {0.0, 0.0, 1.0}}, false);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.41421356, 0.0, 0.0},
                {0.0, -1.27395364, 1.76265775}, {0.0, 1.76265775, -1.27395364}}, false);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        assertEquals("Invalid P matrix.", expectedP, actualP);
        assertEquals("Invalid T matrix.", expectedT, actualT);

        // Test getPT()
        RealMatrix expectedPt = transformer.getPT();
        RealMatrix actualPt = transformer.getPT();
        assertEquals("Invalid PT matrix.", expectedPt, actualPt);

        // Test getT()
        RealMatrix actualTOrig = transformer.getT();
        RealMatrix expectedTOrig = new Array2DRowRealMatrix(transformer.matrixT, false);
        assertEquals("Invalid original T matrix.", expectedTOrig, actualTOrig);

        // Test transformation convergence
        try {
            SchurTransformer transformerDivergent = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                    {0.0, 0.0, 0.0}, {0.0, 0.0, 1.0}}, false));
            fail("Schur transformer should have failed to converge.");
        } catch (MaxCountExceededException e) {
            // expected
        }

        // Test transformation of singular matrix
        try {
            SchurTransformer transformerSingular = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{0.0, 1.0, 0.0},
                    {1.0, 0.0, 0.0}, {0.0, 0.0, 0.0}}, false));
            fail("Schur transformer should have failed with SingularMatrixException.");
        } catch (SingularMatrixException e) {
            // expected
        }

        // Test transformation of non-square matrix
        try {
            SchurTransformer transformerNonSquare = new SchurTransformer(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                    {3.0, 4.0}}, false));
            fail("Schur transformer should have failed with NonSquareMatrixException.");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSchurTransformerCustomPrecision() {
        double[][] inputMatrixData = {
                {1.0e-10, 2.0e-10, 1.0e-10},
                {2.0e-10, 3.0e-10, 2.0e-10},
                {1.0e-10, 2.0e-10, 1.0e-10}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData, false);

        double epsilon = 1.0e-8;
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{0.53846072, 0.81649658, 0.0},
                {-0.81649658, 0.53846072, 0.0}, {0.0, 0.0, 1.0}}, false);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{1.41421356, 0.0, 0.0},
                {0.0, -1.27395364, 1.76265775}, {0.0, 1.76265775, -1.27395364}}, false);

        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();

        PrecisionUtils.assertEquals("Invalid P matrix.", expectedP, actualP, epsilon);
        PrecisionUtils.assertEquals("Invalid T matrix.", expectedT, actualT, epsilon);
    }
}