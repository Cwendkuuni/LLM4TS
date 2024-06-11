import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parentDataset = new DefaultDataset();
        int[] indices = new int[] {0, 1, 2};
        Fold fold = new Fold(parentDataset, indices);
        assertEquals("size of fold should match indices length", indices.length, fold.size());
        assertEquals("parent dataset should be the same", parentDataset, fold.getParent());
    }
}