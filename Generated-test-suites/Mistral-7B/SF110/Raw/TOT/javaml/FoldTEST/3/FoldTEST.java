import net.sf.javaml.core.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test
    public void testInstance() throws Exception {
        // Given: A parent Dataset with some instances
        InstanceDataset parentDataset = new InstanceDataset();
        parentDataset.add(new DenseInstance(new double[]{1.0, 2.0}, 1.0));
        parentDataset.add(new DenseInstance(new double[]{2.0, 3.0}, 2.0));
        parentDataset.add(new DenseInstance(new double[]{3.0, 4.0}, 3.0));

        // When: Creating a Fold with indices from the parent Dataset
        int[] indices = new int[]{1, 2};
        Fold fold = new Fold(parentDataset, indices);

        // Test typical use case: Getting an instance from the fold using a valid index
        Instance instance = fold.instance(0);
        assertEquals(parentDataset.instance(indices[0]), instance);

        // Test edge case: Getting an instance from the fold using the last index
        instance = fold.instance(fold.size() - 1);
        assertEquals(parentDataset.instance(indices[indices.length - 1]), instance);

        // Test error case: Getting an instance from the fold using an invalid index
        try {
            fold.instance(-1);
            fail("Expected IllegalIndexOutOfBoundsException");
        } catch (IllegalArgumentException e) {
            assertEquals("index is out of bounds", e.getMessage());
        }
    }
}