import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{0.535511238, -0.850650808}, {-0.850650808, 0.535511238}});
        RealMatrix actualMatrix = transformer.getP();
        assertEquals(expectedMatrix, actualMatrix);
    }
}