package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.PrecisionUtils;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;

public class SchurTransformerTest
{
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3, 4}});
        new SchurTransformer(matrix);
    }

    @Test
    public void testGetP()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }

    @Test
    public void testGetPT()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT.transpose());
    }

    @Test
    public void testGetT()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{3.7416573867739413, -0.223606797749979, 0.4472135954999586},
                {-0.223606797749979, 2.715575615628623, -0.0488000713358027},
                {0.4472135954999586, -0.0488000713358027, 1.7313150555832225}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testTransform()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        RealMatrix expectedT = new Array2DRowRealMatrix(new double[][]{{3.7416573867739413, -0.223606797749979, 0.4472135954999586},
                {-0.223606797749979, 2.715575615628623, -0.0488000713358027},
                {0.4472135954999586, -0.0488000713358027, 1.7313150555832225}});
        RealMatrix actualP = transformer.getP();
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }

    @Test
    public void testGetNorm()
    {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double expectedNorm = MathUtils.sqrt(Math.pow(3.7416573867739413, 2) + Math.pow(-0.223606797749979, 2) + Math.pow(0.4472135954999586, 2)
                + Math.pow(2.715575615628623, 2) + Math.pow(-0.0488000713358027, 2) + Math.pow(1.7313150555832225, 2)
                + Math.pow(-1.120208369973594, 2) + Math.pow(-0.0488000713358027, 2) + Math.pow(0.7745966692336634, 2)
                + Math.pow(-0.7745966692336634, 2) + Math.pow(0.4330127018922193, 2) + Math.pow(0.4330127018922193, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2)
                + Math.pow(0.0, 2) + Math.pow(0.0, 2) + Math.pow(0.0, 2));
        double actualNorm = transformer.getNorm();
        assertEquals(expectedNorm, actualNorm, Precision.SAFE_EPSILON);
    }

    @Test
    public void testFindSmallSubDiagonalElement()
    {
        double[][] matrix = new double[][]{{1, 2, 3, 4, 5}, {2, 3, 4, 5, 6}, {3, 4, 5, 6, 7}, {4, 5, 6, 7, 8}, {5, 6, 7, 8, 9}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        int expectedResult = 3;
        int actualResult = transformer.findSmallSubDiagonalElement(3, transformer.getNorm());
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransform_MaxIterationReached()
    {
        double[][] matrix = new double[][]{{1, 2, 3}, {2, 3, 4}, {3, 4, 5}};
        SchurTransformer transformer = new SchurTransformer(new Array2DRowRealMatrix(matrix));
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
        transformer.transform();
    }
}