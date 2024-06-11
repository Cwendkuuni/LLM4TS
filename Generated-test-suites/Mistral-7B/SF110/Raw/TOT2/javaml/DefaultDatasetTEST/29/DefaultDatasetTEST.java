package net.sf.javaml.core.test;

import static org.junit.Assert.*;
import net.sf.javaml.core.*;
import java.util.ArrayList;

public class DefaultDatasetTest {

    @Test
    public void testDefaultDatasetInitWithCollection() {
        Collection<Instance> instances = new ArrayList<Instance>();
        instances.add(new DenseInstance(new double[] {1.0, 2.0}, new Double(1.0)));
        DefaultDataset dataset = new DefaultDataset(instances);
        assertEquals(1, dataset.size());
    }

    @Test
    public void testDefaultDatasetInitWithEmptyCollection() {
        DefaultDataset dataset = new DefaultDataset();
        assertEquals(0, dataset.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultDatasetInitWithNullCollection() {
        DefaultDataset dataset = new DefaultDataset(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInstanceWithMoreAttributes() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] {1.0, 2.0, 3.0}, new Double(1.0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInstanceWithNullClassValue() {
        DefaultDataset dataset = new DefaultDataset();
        dataset.add(new DenseInstance(new double[] {1.0, 2.0}, null));
    }

    // Other test cases for the methods and features of the DefaultDataset class can be added here
}