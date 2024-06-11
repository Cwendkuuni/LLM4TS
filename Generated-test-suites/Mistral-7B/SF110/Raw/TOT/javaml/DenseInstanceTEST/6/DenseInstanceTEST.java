import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {
    @Test
    public void testDenseInstanceWithDoubleArray() {
        // Given
        double[] doubleArray = new double[] {1.0, 2.0, 3.0};

        // When
        DenseInstance denseInstance = new DenseInstance(doubleArray);

        // Then
        assertEquals(3, denseInstance.noAttributes());
        for (int i = 0; i < 3; ++i) {
            assertEquals(doubleArray[i], denseInstance.value(i), 0.0);
        }
    }
}