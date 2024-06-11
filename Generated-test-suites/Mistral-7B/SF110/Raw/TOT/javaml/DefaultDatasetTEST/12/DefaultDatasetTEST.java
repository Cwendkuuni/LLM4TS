import junit.framework.Assert;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDataset() {
        DefaultDataset dataset = new DefaultDataset();
        Assert.assertEquals(0, dataset.size());
        Assert.assertNotNull(dataset.classes());

        Instance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        dataset.add(instance);
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals(1.0, dataset.classes().size());
        Assert.assertEquals(instance.classValue(), dataset.classes().first());
    }

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        Instance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, 1.0);
        Instance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, 2.0);

        dataset.addAll(Arrays.asList(instance1, instance2));
        Assert.assertEquals(2, dataset.size());
        Assert.assertEquals(2.0, dataset.classes().size());
    }
}