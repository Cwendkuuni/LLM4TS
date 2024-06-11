import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.OpenIntToDoubleHashMap;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix copy = original.copy();
        Assert.assertNotSame(original, copy);
        Assert.assertEquals(original, copy);
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 3.0);
            put(1, 4.0);
            put(2, 5.0);
        }});
        OpenMapRealMatrix result = a.add(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 4.0);
            put(1, 6.0);
            put(2, 5.0);
        }});
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAdditionWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix result = a.add(b);
        // This test case should throw an exception
        // since the matrices have different sizes.
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 1.0);
        }});
        OpenMapRealMatrix result = a.subtract(b);
        OpenMapRealMatrix expected = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 0.0);
            put(1, 1.0);
        }});
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 3.0);
            put(1, 4.0);
        }});
        RealMatrix result = a.multiply(b);
        RealMatrix expected = RealMatrixUtils.createMatrix(new double[][]{
                {3.0, 4.0},
                {6.0, 8.0}
        });
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testMultiplicationWithDifferentSizes() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        RealMatrix result = a.multiply(b);
        // This test case should throw an exception
        // since the number of columns of 'a' is
        // different from the number of rows of 'b'.
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 3);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        matrix.addToEntry(0, 0, 3.0);
        Assert.assertEquals(4.0, matrix.getEntry(0, 0), 0.001);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
        }});
        matrix.multiplyEntry(0, 0, 3.0);
        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 0.001);
    }
}