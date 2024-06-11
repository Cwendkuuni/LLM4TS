import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;

public class FoldTest {

    @org.junit.Test
    public void testInstanceMethod() {
        Dataset parentDataSet = new DefaultDataset(); // or any other valid Dataset
        Fold fold = new Fold(parentDataSet, new int[]{0, 1, 2, 3, 4});

        // Test valid index
        Instance expectedInstance = parentDataSet.instance(0);
        Instance actualInstance = fold.instance(0);
        assertEquals(expectedInstance, actualInstance);

        // Test invalid index
        assertThrows(org.junit.Assert.IndexOutOfBoundsException.class, () -> fold.instance(5));
    }
}