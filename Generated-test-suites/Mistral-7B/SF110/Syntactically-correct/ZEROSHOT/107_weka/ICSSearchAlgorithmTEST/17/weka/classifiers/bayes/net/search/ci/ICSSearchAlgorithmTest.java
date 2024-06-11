package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;
    private BayesNet bayesNet;

    @Before
    public void setUp() throws Exception {
        icssa = new ICSSearchAlgorithm();
        bayesNet = new BayesNet();
        instances = new Instances(new FileReader("data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa.setInstances(instances);
        icssa.setMaxCardinality(3);
        bayesNet.setSearchAlgorithm(icssa);
    }

    @Test
    public void testName() {
        for (int i = 0; i < instances.numAttributes(); ++i) {
            String name = icssa.name(i);
            assertEquals("Attribute name not correct", instances.attribute(i).name(), name);
        }
    }

    @Test
    public void testMaxn() {
        assertEquals("Maximum number of nodes not correct", instances.numAttributes(), icssa.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(4);
        assertEquals("Maximum cardinality not set correctly", 4, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals("Maximum cardinality not returned correctly", icssa.getMaxCardinality(), icssa.maxn() / 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxCardinality() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        icssa.search(bayesNet, instances);
        // Check number of parents for each node
        for (int i = 0; i < instances.numAttributes(); ++i) {
            ParentSet parents = bayesNet.getParentSet(i);
            assertEquals("Number of parents not correct for node " + instances.attribute(i).name(), 0, parents.getNrOfParents());
        }
        // Check that edges have been set correctly
        boolean[][] edges = bayesNet.getStructure().getEdges();
        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                assertTrue("Edge from node " + instances.attribute(i).name() + " to node " + instances.attribute(j).name() + " not found", edges[i][j] || edges[j][i]);
            }
        }
    }

    @Test
    public void testCalcDependencyGraph() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        // Check that edges have been set correctly for non-separable nodes
        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                if (i != j) {
                    assertFalse("Edge from node " + instances.attribute(i).name() + " to node " + instances.attribute(j).name() + " should not be present", edges[i][j]);
                    assertFalse("Edge from node " + instances.attribute(j).name() + " to node " + instances.attribute(i).name() + " should not be present", edges[j][i]);
                }
            }
        }
        // Check that edges have been set correctly for separable nodes
        SeparationSet set = icssa.existsSepSet(0, 1, 2, edges);
        assertTrue("Expected separation set for nodes 0 and 1 not found", set != null);
        assertEquals("Size of separation set not correct", 2, set.m_set.length);
        assertEquals("First element of separation set not correct", 2, set.m_set[0]);
        assertEquals("Second element of separation set not correct", 3, set.m_set[1]);
    }

    @Test
    public void testNext() {
        int x = icssa.next(-1, 0, 1, new boolean[instances.numAttributes() + 1][]);
        assertEquals("Next value not correct for node 0 and node 1", 2, x);
        x = icssa.next(2, 0, 1, new boolean[instances.numAttributes() + 1][]);
        assertEquals("Next value not correct for node 0 and node 1 starting from 2", 4, x);
        x = icssa.next(instances.numAttributes(), 0, 1, new boolean[instances.numAttributes() + 1][]);
        assertEquals("Next value not correct for node 0 and node 1 starting from the last node", -1, x);
    }

    @Test
    public void testExistsSepSet() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        SeparationSet set = icssa.existsSepSet(0, 1, 2, edges);
        assertNull("Unexpected separation set for nodes 0 and 1 with cardinality 3", set);
        set = icssa.existsSepSet(0, 1, 2, edges);
        assertNotNull("No separation set found for nodes 0 and 1 with cardinality 2", set);
        set = icssa.existsSepSet(2, 1, 2, edges);
        assertNotNull("No separation set found for nodes 1 and 2 with cardinality 2", set);
    }

    @Test
    public void testCalcVeeNodes() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        icssa.calcVeeNodes(edges, arrows, sepsets);
        // Check that arrows have been set correctly for connected nodes
        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                if (i != j && edges[i][j]) {
                    assertTrue("Arrow from node " + instances.attribute(i).name() + " to node " + instances.attribute(j).name() + " not found", arrows[i][j]);
                    assertTrue("Arrow from node " + instances.attribute(j).name() + " to node " + instances.attribute(i).name() + " not found", arrows[j][i]);
                }
            }
        }
        // Check that arrows have been set correctly for disconnected nodes
        assertFalse("Arrow from node 0 to node 3 not found", arrows[0][3]);
        assertFalse("Arrow from node 3 to node 0 not found", arrows[3][0]);
    }

    @Test
    public void testCalcArcDirections() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        icssa.calcVeeNodes(edges, arrows, sepsets);
        icssa.calcArcDirections(edges, arrows);
        // Check that arrows have been set correctly for nodes with two parents
        int parent1 = 0;
        int parent2 = 1;
        for (int i = 0; i < instances.numAttributes(); ++i) {
            if (i == parent1 || i == parent2) {
                continue;
            }
            assertTrue("Arrow from node " + instances.attribute(i).name() + " to node " + instances.attribute(parent1).name() + " not found", arrows[i][parent1]);
            assertTrue("Arrow from node " + instances.attribute(i).name() + " to node " + instances.attribute(parent2).name() + " not found", arrows[i][parent2]);
        }
        // Check that arrows have been set correctly for nodes with no parents
        for (int i = 2; i < instances.numAttributes(); ++i) {
            assertFalse("Arrow from node " + instances.attribute(0).name() + " to node " + instances.attribute(i).name() + " found", arrows[0][i]);
            assertFalse("Arrow from node " + instances.attribute(i).name() + " to node " + instances.attribute(0).name() + " found", arrows[i][0]);
        }
    }

    @Test
    public void testListOptions() {
        String[] options = icssa.listOptions().toArray(new String[0]);
        String expected = "-cardinality 2";
        int i = 0;
        while (i < options.length) {
            if (options[i].contains("cardinality")) {
                assertEquals("Option value not correct", expected, options[i]);
                break;
            }
            i++;
        }
    }

    @Test
    public void testSetOptions() throws Exception {
        String[] options = {"-cardinality 4"};
        icssa.setOptions(options);
        assertEquals("Maximum cardinality not set correctly", 4, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        String[] options = icssa.getOptions();
        String expected = "-cardinality 2";
        int i = 0;
        while (i < options.length && !options[i].equals("-cardinality")) {
            i++;
        }
        assertEquals("Option not present", i < options.length, true);
        assertEquals("Option value not correct", expected, options[i]);
    }
}

class SeparationSet implements Cloneable {
    public int[] m_set;

    public SeparationSet() {
        this.m_set = new int[ICSSearchAlgorithm.this.getMaxCardinality() + 1];
    }

    public boolean contains(int nItem) {
        for (int iItem = 0; iItem < ICSSearchAlgorithm.this.getMaxCardinality() && this.m_set[iItem] != -1; ++iItem) {
            if (this.m_set[iItem] == nItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SeparationSet cloned = (SeparationSet) super.clone();
        cloned.m_set = this.m_set.clone();
        return cloned;
    }

    @Override
    public String getRevision() {
        return RevisionUtils.extract("$Revision: 8034 $");
    }
}