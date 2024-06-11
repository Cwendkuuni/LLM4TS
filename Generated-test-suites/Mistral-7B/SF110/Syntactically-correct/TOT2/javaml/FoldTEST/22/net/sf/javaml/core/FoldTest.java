import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FoldTest {
    @Test
    public void testInstance() {
        Dataset parent = new DefaultDataset();
        int[] indices = {0, 1, 2};
        Fold fold = new Fold(parent, indices);
        Instance expectedInstance = parent.instance(0); // replace with an actual instance from the parent dataset
        Instance actualInstance = fold.instance(0);
        assertEquals(expectedInstance, actualInstance);
    }
}