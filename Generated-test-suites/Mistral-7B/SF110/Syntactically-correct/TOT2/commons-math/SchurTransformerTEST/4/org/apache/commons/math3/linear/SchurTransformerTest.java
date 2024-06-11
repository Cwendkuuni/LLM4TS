import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformerInitialization() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix p = transformer.getP();
        RealMatrix t = transformer.getT();

        // Check if the dimensions of the matrices are correct
        assertEquals(2, p.getRowDimension());
        assertEquals(2, p.getColumnDimension());
        assertEquals(2, t.getRowDimension());
        assertEquals(2, t.getColumnDimension());
    }
}