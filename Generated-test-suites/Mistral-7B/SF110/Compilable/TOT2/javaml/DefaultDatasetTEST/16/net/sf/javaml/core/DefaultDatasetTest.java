package net.sf.javaml.core;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DefaultDatasetTest {

    @Test
    public void testAddAll() {
        DefaultDataset dataset = new DefaultDataset();
        List<Instance> instances = new ArrayList<>();
        Instance instance = new DenseInstance(new double[] { 1.0, 2.0 }, new String[] { "class1" });
        instances.add(instance);

        dataset.addAll(instances);
        assertEquals(1, dataset.size());
        assertEquals("class1", dataset.instance(0).classValue());
        
        Instance nullInstance = null;
        List<Instance> nullInstances = new ArrayList<>();
        nullInstances.add(nullInstance);
        assertThrows(NullPointerException.class, () -> dataset.addAll(nullInstances));
    }
}