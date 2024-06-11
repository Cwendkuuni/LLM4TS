import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.7363832545431988, -0.4330127018922193}, {-0.4330127018922193, 0.7363832545431988}});
        RealMatrix actualP = transformer.getP();
        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }
}