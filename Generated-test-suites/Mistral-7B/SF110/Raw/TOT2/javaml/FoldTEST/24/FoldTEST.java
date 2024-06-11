import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class FoldTest {
    @Test
    public void testFoldConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals("Expected parent to be set correctly", parent, fold.parent);
        assertEquals("Expected indices to be set correctly", indices, fold.indices);
    }

    @Test
    public void testGetClass() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals("Expected Fold class", Fold.class, fold.getClass());
    }

    @Test
    public void testSize() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals("Expected size to be equal to number of indices", 3, fold.size());
    }
}