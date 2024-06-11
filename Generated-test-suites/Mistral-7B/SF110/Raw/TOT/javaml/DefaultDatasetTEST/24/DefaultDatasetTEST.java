import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;
import java.util.*;

public class DefaultDatasetTest {
    //... (previous imports and test setup methods)

    @Test
    public void testAddAllWithInstances() {
        // Given
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);
        Collection<Instance> instances = new ArrayList<>();
        instances.add(instance1);
        instances.add(instance2);

        DefaultDataset dataset = new DefaultDataset();

        // When
        dataset.addAll(instances);

        // Then
        assertEquals(2, dataset.size());
        assertEquals(instance1, dataset.instance(0));
        assertEquals(instance2, dataset.instance(1));
        assertEquals(3, dataset.noAttributes());
        assertTrue(dataset.classes().contains(instance1.classValue()));
        assertTrue(dataset.classes().contains(instance2.classValue()));
    }
}