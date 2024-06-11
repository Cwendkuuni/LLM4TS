import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    @Test
    public void testGetP() {
        RealMatrix inputMatrix = Array2DRowRealMatrix.identity(5);
        SchurTransformer schurTransformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = Array2DRowRealMatrix.identity(5);
        RealMatrix actualP = schurTransformer.getP();

        assertArrayEquals(expectedP.getData(), actualP.getData(), 0.0);
    }
}