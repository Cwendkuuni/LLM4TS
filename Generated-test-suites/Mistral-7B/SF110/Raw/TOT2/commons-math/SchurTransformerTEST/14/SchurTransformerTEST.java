import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testValidSchurDecomposition() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {2.0, 4.0, 5.0}, {3.0, 5.0, 6.0}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{0.57357825, -0.57357825, 0.81649660}, {-0.57357825, 0.57357825, -0.81649660}, {0, 0, 1}});
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1.85036544, -0.04082184, 2.88661532}, {-0.04082184, 3.03728444, -0.66113445}, {2.88661532, -0.66113445, 3.33462222}});

        RealMatrix actualP = schurTransformer.getP();
        RealMatrix actualT = schurTransformer.getT();

        assertEquals(expectedP, actualP);
        assertEquals(expectedT, actualT);
    }
}