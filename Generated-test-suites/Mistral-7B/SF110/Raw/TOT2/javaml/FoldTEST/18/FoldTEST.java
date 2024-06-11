import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Fold;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parentDataSet = new DefaultDataset();
        int[] indices = {0, 1, 2, 3, 4};

        Fold fold = new Fold(parentDataSet, indices);
        assertEquals(parentDataSet, fold.getParent());
        assertEquals(indices.length, fold.size());
    }
}