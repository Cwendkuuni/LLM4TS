import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.InstanceFactory;
import net.sf.javaml.core.DefaultInstance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Before;
import org.junit.Test;

public class DefaultDatasetTest {

    private DefaultDataset dataset;
    private InstanceFactory instanceFactory;
    private List<Instance> instances;

    @Before
    public void setUp() {
        instanceFactory = new DefaultInstanceFactory();
        instances = Arrays.asList(
                instanceFactory.createInstance(new double[] {1.0, 2.0, 3.0}, new double[] {0.0}),
                instanceFactory.createInstance(new double[] {2.0, 2.0, 3.0}, new double[] {1.0}),
                instanceFactory.createInstance(new double[] {3.0, 2.0, 3.0}, new double[] {2.0})
        );
    }

    @Test
    public void testDefaultDatasetConstructionWithCollection() {
        dataset = new DefaultDataset(instances);
        assertEquals(instances.size(), dataset.size());
        for (Instance instance : instances) {
            Instance instanceInDataset = dataset.instance(dataset.indexOf(instance));
            assertEquals(instance, instanceInDataset);
        }

        // Add some new instances
        Instance newInstance1 = instanceFactory.createInstance(new double[] {4.0, 2.0, 3.0}, new double[] {3.0});
        Instance newInstance2 = instanceFactory.createInstance(new double[] {5.0, 2.0, 3.0}, new double[] {4.0});
        dataset.add(newInstance1);
        dataset.add(newInstance2);

        assertEquals(instances.size() + 2, dataset.size());
        Instance newInstance1InDataset = dataset.instance(dataset.size() - 1);
        Instance newInstance2InDataset = dataset.instance(dataset.size() - 2);
        assertEquals(newInstance1, newInstance1InDataset);
        assertEquals(newInstance2, newInstance2InDataset);
    }
}