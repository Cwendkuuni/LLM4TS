package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.001;

        // When
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // Then
        assertEquals(3, decomposition.getRank());
        assertNotNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrix() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // When
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.632455532, 1.0, 0.0}, {0.422580645, 0.816496581, 1.0}});
        RealMatrix root = decomposition.getRootMatrix();

        // Then
        assertMatrixEquals(expectedRoot, root);
    }

    @Test
    public void testGetRank() {
        // Given
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        double small = 0.001;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        // When
        int rank = decomposition.getRank();

        // Then
        assertEquals(3, rank);
    }
}