import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {
    @Test
    public void testDenseInstanceConstructionWithDoubleArray() {
        // Given
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        
        // When
        
        // Then
        assertEquals(3, denseInstance.noAttributes());
        for (int i = 0; i < 3; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }
}