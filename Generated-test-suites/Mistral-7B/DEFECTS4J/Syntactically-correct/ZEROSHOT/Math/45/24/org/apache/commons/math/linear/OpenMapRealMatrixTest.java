import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstruction() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        Assert.assertEquals(rowDimension, matrix.getRowDimension());
        Assert.assertEquals(columnDimension, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix copy = matrix.copy();
        Assert.assertNotSame(matrix, copy);
        Assert.assertEquals(rowDimension, copy.getRowDimension());
        Assert.assertEquals(columnDimension, copy.getColumnDimension());
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                Assert.assertNull(matrix.getEntry(i, j));
                Assert.assertNull(copy.getEntry(i, j));
            }
        }
    }

    @Test
    public void testAddition() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension, columnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                matrix1.setEntry(i, j, i * columnDimension + j);
                matrix2.setEntry(i, j, j * columnDimension);
            }
        }

        OpenMapRealMatrix result = matrix1.add(matrix2);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                Assert.assertEquals(
                        i * columnDimension + j + j * columnDimension,
                        result.getEntry(i, j),
                        Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testAdditionDimensionMismatch() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension - 1; j++) {
                matrix1.setEntry(i, j, i * columnDimension + j);
                matrix2.setEntry(i, j, j * columnDimension);
            }
        }

        matrix1.add(matrix2);
    }

    @Test
    public void testSubtraction() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension, columnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                matrix1.setEntry(i, j, i * columnDimension + j);
                matrix2.setEntry(i, j, j * columnDimension);
            }
        }

        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                Assert.assertEquals(
                        (i - 1) * columnDimension + j,
                        result.getEntry(i, j),
                        Precision.SAFE_EPSILON);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testSubtractionDimensionMismatch() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(rowDimension, columnDimension - 1);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension - 1; j++) {
                matrix1.setEntry(i, j, i * columnDimension + j);
                matrix2.setEntry(i, j, j * columnDimension);
            }
        }

        matrix1.subtract(matrix2);
    }

    @Test
    public void testMultiplication() {
        final int rowDimension = 2;
        final int columnDimension = 2;
        final int outColumnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(columnDimension, outColumnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                matrix1.setEntry(i, j, j * columnDimension + i);
            }
        }
        for (int i = 0; i < columnDimension; i++) {
            for (int j = 0; j < outColumnDimension; j++) {
                matrix2.setEntry(i, j, i * outColumnDimension + j);
            }
        }

        RealMatrix result = matrix1.multiply(matrix2);
        BlockRealMatrix blockResult = (BlockRealMatrix) result;
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < outColumnDimension; j++) {
                OpenMapRealMatrix subResult = (OpenMapRealMatrix) blockResult.getSubMatrix(i, 0, 1, outColumnDimension);
                for (int k = 0; k < columnDimension; k++) {
                    Assert.assertEquals(
                            i * columnDimension + k * outColumnDimension + j,
                            subResult.getEntry(0, j),
                            Precision.SAFE_EPSILON);
                }
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testMultiplicationDimensionMismatch() {
        final int rowDimension = 2;
        final int columnDimension = 2;
        final int outColumnDimension = 3;
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(columnDimension, outColumnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                matrix1.setEntry(i, j, j * columnDimension + i);
            }
        }
        for (int i = 0; i < columnDimension; i++) {
            for (int j = 0; j < outColumnDimension; j++) {
                matrix2.setEntry(i, j, i * outColumnDimension + j);
            }
        }

        matrix1.multiply(matrix2);
    }

    @Test
    public void testGetEntry() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                matrix.setEntry(i, j, i * columnDimension + j);
            }
        }

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                Assert.assertEquals(
                        i * columnDimension + j,
                        matrix.getEntry(i, j),
                        Precision.SAFE_EPSILON);
            }
        }
    }

    @Test
    public void testSetEntry() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);

        matrix.setEntry(0, 0, 1.2);
        Assert.assertEquals(1.2, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);

        matrix.setEntry(1, 1, 3.14159265);
        Assert.assertEquals(3.14159265, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    @Test
    public void testAddToEntry() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);

        matrix.addToEntry(0, 0, 1.2);
        Assert.assertEquals(1.2, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);

        matrix.addToEntry(0, 0, -1.2);
        Assert.assertEquals(0, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);

        matrix.addToEntry(1, 1, 3.14159265);
        Assert.assertEquals(3.14159265, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }

    @Test
    public void testMultiplyEntry() {
        final int rowDimension = 2;
        final int columnDimension = 3;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);

        matrix.multiplyEntry(0, 0, 2.718281828);
        Assert.assertEquals(2.718281828, matrix.getEntry(0, 0), Precision.SAFE_EPSILON);

        matrix.multiplyEntry(1, 1, 0.5);
        Assert.assertEquals(0.5, matrix.getEntry(1, 1), Precision.SAFE_EPSILON);
    }
}