import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = new int[]{0, 1, 2, 3};
        Fold fold = new Fold(parentDataSet, indices);
        assertEquals(parentDataSet, fold.getParent());
        assertEquals(indices.length, fold.size());
    }
}