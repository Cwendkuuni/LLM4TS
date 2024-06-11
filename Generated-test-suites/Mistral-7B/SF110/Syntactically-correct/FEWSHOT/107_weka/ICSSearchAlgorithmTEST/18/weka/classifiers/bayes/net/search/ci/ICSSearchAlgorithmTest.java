package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import org.junit.Test;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import static org.mockito.Mockito.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testName() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Instances instances = mock(Instances.class);
        when(instances.attribute(anyInt())).thenReturn(mock(Attribute.class));
        assertEquals("attributeName", icssa.name(0));
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Instances instances = mock(Instances.class);
        when(instances.numAttributes()).thenReturn(5);
        assertEquals(5, icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        icssa.setMaxCardinality(4);
        assertEquals(4, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        icssa.setMaxCardinality(5);
        assertEquals(5, icssa.getMaxCardinality());
    }

    @Test
    public void testSearch() throws Exception {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        BayesNet bayesNet = mock(BayesNet.class);
        Instances instances = mock(Instances.class);
        when(instances.attribute(anyInt())).thenReturn(mock(Attribute.class));
        doNothing().when(icssa).calcDependencyGraph(anyBooleanArrayArray(), any(SeparationSet[][].class));
        doNothing().when(icssa).calcVeeNodes(anyBooleanArrayArray(), anyBooleanArrayArray(), any(SeparationSet[][].class));
        doNothing().when(icssa).calcArcDirections(anyBooleanArrayArray(), anyBooleanArrayArray());
        icssa.search(bayesNet, instances);
        verify(bayesNet).getParentSet(anyInt());
    }

    @Test
    public void testCalcDependencyGraph() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        SeparationSet[][] sepsets = new SeparationSet[10][];
        icssa.calcDependencyGraph(edges, sepsets);
        // Add more assertions here
    }

    @Test
    public void testExistsSepSet() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        SeparationSet Z = new SeparationSet();
        Z.m_set[0] = 1;
        Z.m_set[1] = 2;
        SeparationSet oSepSet = icssa.existsSepSet(0, 1, 2, edges);
        assertEquals(Z, oSepSet);
    }

    @Test
    public void testNext() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        int x = icssa.next(0, 1, 2, edges);
        assertEquals(3, x);
    }

    @Test
    public void testCalcVeeNodes() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        boolean[][] arrows = new boolean[10][];
        SeparationSet[][] sepsets = new SeparationSet[10][];
        icssa.calcVeeNodes(edges, arrows, sepsets);
        // Add more assertions here
    }

    @Test
    public void testCalcArcDirections() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[10][];
        boolean[][] arrows = new boolean[10][];
        icssa.calcArcDirections(edges, arrows);
        // Add more assertions here
    }

    @Test
    public void testListOptions() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Object[] options = icssa.listOptions().toArray();
        assertEquals(2, options.length);
    }

    @Test
    public void testSetOptions() throws Exception {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        String[] options = {"-cardinality 3"};
        icssa.setOptions(options);
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Object[] options = icssa.getOptions();
        assertEquals(3, options.length);
        assertEquals("-cardinality", options[2].toString());
    }

    @Test
    public void testMaxCardinalityTipText() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
    }

    @Test
    public void testGlobalInfo() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
    }

    @Test
    public void testGetRevision() {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        assertEquals("$Revision: 8034 $", icssa.getRevision());
    }

    @Test
    public void testMain() {
        try {
            main((String[]) args);
        } catch (Exception e) {
            e.printStackTrace();
            fail("The main method should not throw an exception.");
        }
    }
}