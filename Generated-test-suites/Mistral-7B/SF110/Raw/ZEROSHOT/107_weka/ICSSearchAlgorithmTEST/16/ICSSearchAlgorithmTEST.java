package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.core.*;
import java.util.*;

public class ICSSearchAlgorithmTest {

    private ICSSearchAlgorithm icssa;
    private Instances instances;

    @Before
    public void setUp() throws Exception {
        icssa = new ICSSearchAlgorithm();
        instances = new Instances(new FileReader("contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        icssa.setInstances(instances);
    }

    @Test
    public void testName() {
        for (int i = 0; i < instances.numAttributes(); ++i) {
            String name = icssa.name(i);
            assertEquals("Attribute name should be correct", instances.attribute(i).name(), name);
        }
    }

    @Test
    public void testMaxn() {
        int maxn = icssa.maxn();
        assertEquals("Number of attributes should match", instances.numAttributes(), maxn);
    }

    @Test
    public void testSetMaxCardinality() {
        icssa.setMaxCardinality(5);
        assertEquals("Max cardinality should be set to 5", 5, icssa.getMaxCardinality());
    }

    @Test
    public void testGetMaxCardinality() {
        assertEquals("Default max cardinality should be 2", 2, icssa.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxCardinality() {
        icssa.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        BayesNet bayesNet = new BayesNet();
        icssa.search(bayesNet, instances);
        assertNotNull("BayesNet should not be null", bayesNet);
    }

    @Test
    public void testCalcDependencyGraph() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];

        icssa.calcDependencyGraph(edges, sepsets);

        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = i + 1; j < instances.numAttributes(); ++j) {
                assertEquals("Dependency graph should not have edge if not present in the algorithm", false, edges[i][j]);
                assertEquals("Dependency graph should have edge if present in the algorithm", true, edges[j][i]);
            }
        }

        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                if (sepsets[i][j] != null) {
                    assertNotNull("Separation set should not be null", sepsets[i][j]);
                    int[] set = sepsets[i][j].m_set;
                    for (int k = 0; k < sepsets[i][j].m_set.length; ++k) {
                        assertEquals("Set element should be valid attribute index", i + k, set[k]);
                    }
                }
                else {
                    assertNull("Separation set should be null if not present in the algorithm");
                }
            }
        }
    }

    @Test
    public void testExistsSepSet() {
        int iNode1 = 0;
        int iNode2 = 1;
        int nCardinality = 2;
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];

        icssa.calcDependencyGraph(edges, null);

        SeparationSet sepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);

        if (sepSet != null) {
            int[] set = sepSet.m_set;
            for (int k = 0; k < sepSet.m_set.length; ++k) {
                assertTrue("Set element should be valid attribute index", instances.numAttributes() > set[k] && set[k] >= 0);
                assertEquals("Set element should not be iNode1 or iNode2", iNode1 != set[k] && iNode2 != set[k], true);
            }
        }
        else {
            assertNull("Separation set should be null if not found");
        }
    }

    @Test
    public void testCalcVeeNodes() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];

        icssa.calcDependencyGraph(edges, null);
        icssa.calcVeeNodes(edges, arrows, null);

        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                if (i == j) {
                    assertFalse("Arrow should not point to itself", arrows[i][j]);
                }
                else {
                    if (edges[i][j]) {
                        assertTrue("Arrow should point from iNode1 to iNode2 if there is an edge", arrows[i][j] || arrows[j][i]);
                    }
                    else {
                        assertFalse("Arrow should not point from iNode1 to iNode2 if there is no edge", arrows[i][j] || arrows[j][i]);
                    }
                }
            }
        }
    }

    @Test
    public void testCalcArcDirections() {
        boolean[][] edges = new boolean[instances.numAttributes() + 1][];
        boolean[][] arrows = new boolean[instances.numAttributes() + 1][];

        icssa.calcDependencyGraph(edges, null);
        icssa.calcArcDirections(edges, arrows);

        for (int i = 0; i < instances.numAttributes(); ++i) {
            for (int j = 0; j < instances.numAttributes(); ++j) {
                if (i != j && edges[i][j]) {
                    assertTrue("Arrow should point from parent to child if there is a dependency", arrows[i][j] || arrows[j][i]);
                }
            }
        }
    }

    @Test
    public void testListOptions() {
        List<Option> options = icssa.listOptions();
        Option option = null;

        for (Option opt : options) {
            if (opt.name.equals("cardinality")) {
                option = opt;
                break;
            }
        }

        assertNotNull("Option 'cardinality' should be present in the list", option);
        assertEquals("Option 'cardinality' should have correct argument type", Option.TYPE_INT, option.type);
    }

    @Test
    public void testSetOptions() throws Exception {
        icssa.setOptions(new String[]{"-cardinality", "3"});
        assertEquals("Max cardinality should be set to 3", 3, icssa.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        String[] options = icssa.getOptions();
        String optionStr = "";

        for (String opt : options) {
            optionStr += opt + " ";
        }

        assertEquals("Options string should contain '-cardinality 2'", "-cardinality 2", optionStr);
    }

    @Test
    public void testMaxCardinalityTipText() {
        String tipText = icssa.maxCardinalityTipText();
        assertTrue("Max cardinality tip text should contain 'MaxCardinality determines the maximum size of the set Z' and 'default value is 2'", tipText.contains("MaxCardinality determines the maximum size of the set Z") && tipText.contains("default value is 2"));
    }

    @Test
    public void testGlobalInfo() {
        String globalInfo = icssa.globalInfo();
        assertTrue("Global info string should contain 'This Bayes Network learning algorithm uses conditional independence tests to find a skeleton' and 'finds V-nodes and applies a set of rules to find the directions of the remaining arrows'", globalInfo.contains("This Bayes Network learning algorithm uses conditional independence tests to find a skeleton") && globalInfo.contains("finds V-nodes and applies a set of rules to find the directions of the remaining arrows"));
    }
}