import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;
import weka.core.Instances;
import weka.core.RevisionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;

    @Parameters
    public static List<Object[]> data() {
        Instances data = new Instances(new FileReader("path/to/data.arff")); // Update with the path to your data file
        data.setClassIndex(data.numAttributes() - 1);
        return Arrays.asList(new Object[][]{
                {new ICSSearchAlgorithm(), data}
        });
    }

    public ICSSearchAlgorithmTest(ICSSearchAlgorithm icssa, Instances instances) {
        this.icssa = icssa;
        this.instances = instances;
    }

    @Test
    public void testName() {
        for (int i = 0; i < icssa.maxn(); ++i) {
            String name = icssa.name(i);
            assertEquals(instances.attribute(i).name(), name);
        }
    }

    @Test
    public void testMaxn() {
        int maxn = icssa.maxn();
        assertEquals(instances.numAttributes(), maxn);
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals(2, icssa.getMaxCardinality());
    }

    @Test
    public void testSearch() throws Exception {
        BayesNet bayesNet = new BayesNet();
        icssa.search(bayesNet, instances);
        // Add asserts for the behavior of the search method
    }

    @Test
    public void testCalcDependencyGraph() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        // Add asserts for the behavior of the calcDependencyGraph method
    }

    @Test
    public void testExistsSepSet() {
        int iNode1 = 0, iNode2 = 1;
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                edges[i][j] = (i != j);
            }
        }
        SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, 2, edges);
        assertNotNull(sepSet);
        // Add asserts for the behavior of the existsSepSet method
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextWithInvalidIndices() {
        int x = icssa.next(-1, 0, 1, edges);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextWithInvalidIndices2() {
        int x = icssa.next(0, 0, 1, edges);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextWithInvalidEdges() {
        edges[0][1] = false;
        int x = icssa.next(0, 0, 1, edges);
    }

    @Test
    public void testCalcVeeNodes() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        icssa.calcVeeNodes(edges, arrows, sepsets);
        // Add asserts for the behavior of the calcVeeNodes method
    }

    @Test
    public void testCalcArcDirections() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
        icssa.calcDependencyGraph(edges, sepsets);
        icssa.calcVeeNodes(edges, arrows, sepsets);
        icssa.calcArcDirections(edges, arrows);
        // Add asserts for the behavior of the calcArcDirections method
    }

    @Test
    public void testListOptions() {
        String[] options = icssa.listOptions();
        assertEquals(2, options.length);
        assertEquals("-cardinality <num>", options[1]);
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality 4"});
        assertEquals(4, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        String[] options = icssa.getOptions();
        assertEquals(3, options.length);
        assertEquals("-cardinality", options[1]);
    }

    @Test
    public void testMaxCardinalityTipText() {
        assertEquals("When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.", icssa.maxCardinalityTipText());
    }

    @Test
    public void testGlobalInfo() {
        assertEquals("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.", icssa.globalInfo());
    }

    @Test
    public void testGetRevision() {
        assertEquals(RevisionUtils.extract("$Revision: 8034 $"), icssa.getRevision());
    }

    @Test
    public void testConstructor() {
        ICSSearchAlgorithm icssa2 = new ICSSearchAlgorithm();
        assertEquals(2, icssa2.getMaxCardinality());
    }

    @Test
    public void testSeparationSetClass() {
        SeparationSet sepset = new SeparationSet();
        assertNull(sepset.m_set);
        sepset.m_set = new int[1];
        assertFalse(sepset.contains(0));
        sepset.m_set[0] = 0;
        assertTrue(sepset.contains(0));
    }
}