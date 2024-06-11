package org.apache.commons.math3.linear;
import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testRectangularCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        // Add assertions here to check the expected behavior of the constructor
    }
}