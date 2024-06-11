package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.*;

import org.junit.Test;
import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testExistenceOfSetZ() {
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(2);
        algorithm.search(bayesNet, instances);

        boolean[][] edges = algorithm.getEdges();
        SeparationSet[][] sepSets = algorithm.getSeparationSets();

        for (int iNode1 = 0; iNode1 < instances.numAttributes(); ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < instances.numAttributes(); ++iNode2) {
                if (edges[iNode1][iNode2]) {
                    SeparationSet setZ = algorithm.existsSepSet(iNode1, iNode2, 2, edges);
                    assertNotNull(setZ);
                }
            }
        }
    }

    @Test
    public void testNextInSearchForSetZ() {
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(3);
        algorithm.search(bayesNet, instances);

        int nextNode = algorithm.next(-1, 0, 1, algorithm.getEdges().clone());
        assertEquals(1, nextNode);

        nextNode = algorithm.next(0, 0, 1, algorithm.getEdges().clone());
        assertEquals(2, nextNode);

        nextNode = algorithm.next(1, 0, 1, algorithm.getEdges().clone());
        assertEquals(3, nextNode);

        nextNode = algorithm.next(2, 0, 1, algorithm.getEdges().clone());
        assertEquals(-1, nextNode);

        nextNode = algorithm.next(0, 1, 1, algorithm.getEdges().clone());
        assertEquals(2, nextNode);

        nextNode = algorithm.next(1, 1, 1, algorithm.getEdges().clone());
        assertEquals(0, nextNode);

        nextNode = algorithm.next(2, 1, 1, algorithm.getEdges().clone());
        assertEquals(3, nextNode);

        nextNode = algorithm.next(3, 1, 1, algorithm.getEdges().clone());
        assertEquals(-1, nextNode);
    }

    @Test
    public void testIsConditionalIndependent() {
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(3);
        algorithm.search(bayesNet, instances);

        boolean isConditionalIndependent = algorithm.isConditionalIndependent(1, 0, new int[]{0, 1, 2}, 2);
        assertFalse(isConditionalIndependent);

        isConditionalIndependent = algorithm.isConditionalIndependent(1, 2, new int[]{0, 1, 2}, 2);
        assertTrue(isConditionalIndependent);

        isConditionalIndependent = algorithm.isConditionalIndependent(2, 0, new int[]{0, 1, 2}, 2);
        assertTrue(isConditionalIndependent);

        isConditionalIndependent = algorithm.isConditionalIndependent(2, 1, new int[]{0, 1, 2}, 2);
        assertFalse(isConditionalIndependent);
    }

    @Test
    public void testCalcDependencyGraph() {
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(3);
        algorithm.search(bayesNet, instances);

        boolean[][] edges = algorithm.getEdges();

        // Check for self-loops
        for (int i = 0; i < instances.numAttributes(); ++i) {
            assertTrue(!edges[i][i]);
        }

        // Check for edges between all nodes
        for (int iNode1 = 0; iNode1 < instances.numAttributes(); ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < instances.numAttributes(); ++iNode2) {
                assertTrue(edges[iNode1][iNode2] || edges[iNode2][iNode1]);
            }
        }
    }

    @Test
    public void testCalcArcDirections() {
        BayesNet bayesNet = new BayesNet();
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        algorithm.setMaxCardinality(3);
        algorithm.search(bayesNet, instances);

        boolean[][] arrows = algorithm.getArrowDirections();

        // Check all nodes have at least one incoming arrow
        for (int iNode = 0; iNode < instances.numAttributes(); ++iNode) {
            int incomingArrows = 0;
            for (int jNode = 0; jNode < instances.numAttributes(); ++jNode) {
                if (arrows[jNode][iNode]) {
                    incomingArrows++;
                }
            }
            assertTrue(incomingArrows > 0);
        }

        // Check all nodes have at most one outgoing arrow
        for (int iNode = 0; iNode < instances.numAttributes(); ++iNode) {
            int outgoingArrows = 0;
            for (int jNode = 0; jNode < instances.numAttributes(); ++jNode) {
                if (arrows[iNode][jNode]) {
                    outgoingArrows++;
                }
            }
            assertTrue(outgoingArrows <= 1);
        }
    }

    @Test
    public void testListOptions() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        String[] options = algorithm.listOptions().toArray(new String[algorithm.listOptions().size()]);

        String expectedOption = "\tWhen determining whether an edge exists a search is performed \n\tfor a set Z that separates the nodes. MaxCardinality determines \n\tthe maximum size of the set Z. This greatly influences the \n\tlength of the search. (default 2)";
        String option = null;

        for (String opt : options) {
            if (opt.startsWith("cardinality")) {
                option = opt;
                break;
            }
        }

        assertEquals(expectedOption, option);
    }

    @Test
    public void testSetOptions() throws Exception {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        String[] options = {"-cardinality", "3"};
        algorithm.setOptions(options);

        assertEquals(3, algorithm.getMaxCardinality());
    }

    @Test
    public void testGetOptions() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();
        String[] options = {"-cardinality", "3"};
        algorithm.setOptions(options);

        String[] expectedOptions = {"-cardinality", "3"};
        assertArrayEquals(expectedOptions, algorithm.getOptions());
    }

    @Test
    public void testMaxCardinalityTipText() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();

        String expectedTipText = "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. Default value is 2.";
        String tipText = algorithm.maxCardinalityTipText();

        assertEquals(expectedTipText, tipText);
    }

    @Test
    public void testGlobalInfo() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();

        String expectedGlobalInfo = "This Bayes Network learning algorithm uses conditional independence tests to find a skeleton, finds V-nodes and applies a set of rules to find the directions of the remaining arrows.";
        String globalInfo = algorithm.globalInfo();

        assertEquals(expectedGlobalInfo, globalInfo);
    }

    @Test
    public void testGetRevision() {
        ICSSearchAlgorithm algorithm = new ICSSearchAlgorithm();

        String expectedRevision = "8034";
        String revision = algorithm.getRevision();

        assertEquals(expectedRevision, revision.substring(0, revision.indexOf(" ")));
    }

    @Test
    public void testMain() throws Exception {
        BayesNet b = new BayesNet();
        b.setSearchAlgorithm(new ICSSearchAlgorithm());
        Instances instances = new Instances(new FileReader("src/test/resources/data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        b.buildClassifier(instances);

        String expectedOutput = "..........."; // The output is trimmed here for brevity
        String output = b.toString();

        String[] expectedLines = output.split("\n");
        int lineCount = expectedLines.length;

        String line;
        for (int i = 0; i < lineCount; ++i) {
            line = expectedLines[i];
            assertTrue(output.contains(line));
        }
    }
}