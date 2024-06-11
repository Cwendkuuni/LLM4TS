import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Fold;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals(parentDataset, fold.getParent());
        assertArrayEquals(indices, fold.getIndices());
    }
}