package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

public class SchurTransformerTest {

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformerWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0, 5.0}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testSchurTransformer() {
        double[][] matrix = {{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix outputP = transformer.getP();
        RealMatrix outputT = transformer.getT();
        RealMatrix outputPt = transformer.getPT();

        // Check that the output matrices are correct
        double[][] expectedP = {{0.538462248, 0.847457959, 0.262903395},
                {0.847457959, 0.942809215, -0.341667573},
                {0.262903395, -0.341667573, 0.734395943}};
       assertEquals(MatrixUtils.createRealMatrix(expectedP), outputP);

        double[][] expectedT = {{1.0, 2.532293411, 3.717043753},
                {2.532293411, 4.176933125, 4.823734021},
                {3.717043753, 4.823734021, 5.87151629}};
        assertEquals(MatrixUtils.createRealMatrix(expectedT), outputT);

        double[][] expectedPt = {{0.538462248, 0.262903395, 0.847457959},
                {0.847457959, 0.341667573, 0.942809215},
                {0.262903395, 0.942809215, 0.734395943}};
        assertEquals(MatrixUtils.createRealMatrix(expectedPt), outputPt);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSchurTransformerWithMaxCountExceededException() {
        double[][] matrix = {{1.0, 0.1}, {0.1, 1.0}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        transformer.transform();
    }
}