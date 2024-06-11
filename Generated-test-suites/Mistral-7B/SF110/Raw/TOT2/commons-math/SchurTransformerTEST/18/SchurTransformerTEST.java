import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {
    @Test
    public void testGetPWithSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 1.0}
        });
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{
                {0.93289852, -0.25886553},
                {0.25886553, 0.93289852}
        });
        RealMatrix actualResult = transformer.getP();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0}
        });
        new SchurTransformer(inputMatrix);
    }

    @Test
    public void testGetTWithSquareMatrix() {
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 1.0}
        });
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(new double[][]{
                {0.31622777, -0.94140105},
                {0.94140105, 0.31622777}
        });
        RealMatrix actualResult = transformer.getT();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxCountExceededException() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {1.0, 1.0}
        });
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        transformer.setMaxIterations(0);
    }
}