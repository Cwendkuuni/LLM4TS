package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import org.junit.Test;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ICSSearchAlgorithmTest {

    @Test
    public void testSearch() throws Exception {
        BayesNet bayesNet = mock(BayesNet.class);
        Instances instances = mock(Instances.class);
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();

        when(instances.numAttributes()).thenReturn(5);
        when(bayesNet.getParentSet((int) 0)).thenReturn(new ParentSet());
        when(bayesNet.getParentSet((int) 1)).thenReturn(new ParentSet());
        when(bayesNet.getParentSet((int) 2)).thenReturn(new ParentSet());
        when(bayesNet.getParentSet((int) 3)).thenReturn(new ParentSet());
        when(bayesNet.getParentSet((int) 4)).thenReturn(new ParentSet());

        icssa.search(bayesNet, instances);

        ParentSet parentSet0 = bayesNet.getParentSet((int) 0);
        ParentSet parentSet1 = bayesNet.getParentSet((int) 1);
        ParentSet parentSet2 = bayesNet.getParentSet((int) 2);
        ParentSet parentSet3 = bayesNet.getParentSet((int) 3);
        ParentSet parentSet4 = bayesNet.getParentSet((int) 4);

        // Check parent sets
        assertEquals(0, parentSet0.getNrOfParents());
        assertEquals(1, parentSet1.getNrOfParents());
        assertEquals(2, parentSet2.getNrOfParents());
        assertEquals(0, parentSet3.getNrOfParents());
        assertEquals(1, parentSet4.getNrOfParents());

        // Check parents for each node
        int[] parents1 = parentSet1.getParentIndices();
        int[] parents2 = parentSet2.getParentIndices();
        int[] parents3 = parentSet3.getParentIndices();
        int[] parents4 = parentSet4.getParentIndices();

        assertArrayEquals(new int[]{0}, parents1);
        assertArrayEquals(new int[]{1, 2}, parents2);
        assertArrayEquals(new int[]{}, parents3);
        assertArrayEquals(new int[]{3}, parents4);
    }

    @Test
    public void testName() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals("?", icssa.name(0));
        assertEquals("age", icssa.name(1));
        assertEquals("gender", icssa.name(2));
        assertEquals("lensPrescription", icssa.name(3));
        assertEquals("outcome", icssa.name(4));
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals(5, icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals(2, icssa.getMaxCardinality());
    }
}