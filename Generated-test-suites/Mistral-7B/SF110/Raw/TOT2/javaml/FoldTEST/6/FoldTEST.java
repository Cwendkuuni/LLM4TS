import org.junit.Assert;
import org.junit.Test;
import net.sf.javaml.core.Dataset;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FoldTest {

    @Test
    public void testFoldConstructor() {
        Dataset parent = mock(Dataset.class);
        int[] indices = new int[]{0, 1, 2};
        Fold fold = new Fold(parent, indices);

        Assert.assertNotNull(fold);
        Assert.assertEquals(parent, fold.parent);
        Assert.assertArrayEquals(indices, fold.indices);

        verify(parent).getInstance(indices[0]);
        verify(parent).getInstance(indices[1]);
        verify(parent).getInstance(indices[2]);
    }
}