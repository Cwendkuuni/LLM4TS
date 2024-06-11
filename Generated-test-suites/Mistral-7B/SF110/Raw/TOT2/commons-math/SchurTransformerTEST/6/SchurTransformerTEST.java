import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix inputMatrix = Array2DRowRealMatrix.identity(3); // Create a 3x3 identity matrix as an example
        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix expectedP = Array2DRowRealMatrix.identity(3); // Expected result: identity matrix
        RealMatrix actualP = transformer.getP();

        assertEquals(expectedP, actualP); // Test the correctness of the returned RealMatrix
    }
}