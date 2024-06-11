import org.junit.Test;
import static org.junit.Assert.*;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        assertEquals(parent, fold.parent);
        assertArrayEquals(indices, fold.indices);
    }
}