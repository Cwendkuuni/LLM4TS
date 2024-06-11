package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static weka.core.Utils.splitFileIntoInstances;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NetClassifier;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testICSSearchAlgorithm() throws Exception {
        // Test with default options
        BayesNet b = new BayesNet();
        ICSSearchAlgorithm icss = new ICSSearchAlgorithm();
        b.setSearchAlgorithm(icss);
        Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);
        b.buildClassifier(instances);

        // Test with specified maximum cardinality
        ICSSearchAlgorithm icssMax = new ICSSearchAlgorithm();
        icssMax.setMaxCardinality(3);
        BayesNet bMax = new BayesNet();
        bMax.setSearchAlgorithm(icssMax);
        Instances instancesMax = new Instances(new FileReader("data/contact-lenses.arff"));
        instancesMax.setClassIndex(instancesMax.numAttributes() - 1);
        bMax.buildClassifier(instancesMax);

        // Test with empty dataset
        ICSSearchAlgorithm icssEmpty = new ICSSearchAlgorithm();
        Instances instancesEmpty = new Instances(new ByteArrayInputStream(new byte[0]));
        try {
            b = new BayesNet();
            b.setSearchAlgorithm(icssEmpty);
            b.buildClassifier(instancesEmpty);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // Test with null dataset
        ICSSearchAlgorithm icssNull = new ICSSearchAlgorithm();
        Instances instancesNull = null;
        try {
            b = new BayesNet();
            b.setSearchAlgorithm(icssNull);
            b.buildClassifier(instancesNull);
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Pass
        }

        // Test error conditions in existsSepSet
        ICSSearchAlgorithm icssError = new ICSSearchAlgorithm();
        boolean[][] edges = new boolean[3][];
        edges[0] = new boolean[3];
        edges[1] = new boolean[3];
        edges[2] = new boolean[3];
        SeparationSet[][] sepsets = new SeparationSet[3][];
        for (int i = 0; i < 3; i++) {
            sepsets[i] = new SeparationSet[icss.getMaxCardinality()];
            for (int j = 0; j < icss.getMaxCardinality(); j++) {
                sepsets[i][j] = new SeparationSet();
                sepsets[i][j].m_set[j] = -1;
            }
        }

        icssError.calcDependencyGraph(edges, sepsets);

        // Test with negative cardinality
        int cardinalityNeg = -1;
        SeparationSet Z = icssError.existsSepSet(0, 1, cardinalityNeg, edges);
        assertNull(Z);

        // Test with invalid node indices
        int node1 = 3;
        int node2 = 2;
        SeparationSet ZInvalid = icssError.existsSepSet(node1, node2, 2, edges);
        assertNull(ZInvalid);

        // Test with invalid cardinality
        int cardinalityInvalid = 5;
        SeparationSet ZInvalidCard = icssError.existsSepSet(0, 1, cardinalityInvalid, edges);
        assertNull(ZInvalidCard);

        // Test separation set existence
        boolean result = icssError.isConditionalIndependent(1, 0, sepsets[0], 2);
        assertFalse(result);

        // Test separation set non-existence
        edges[0][1] = false;
        result = icssError.isConditionalIndependent(1, 0, sepsets[0], 2);
        assertTrue(result);

        // Test next method
        int x = 0;
        int node3 = 2;
        int node4 = 1;
        x = icssError.next(x, node1, node2, edges);
        assertEquals(node3, x);

        x = icssError.next(x, node1, node2, edges);
        assertEquals(node4, x);

        // Test v-node calculation
        icss.calcVeeNodes(edges, arrows, sepsets);
        boolean[][] arrowsExpected = {
            {false, false, true},
            {false, true, false},
            {true, false, false}
        };
        assertArrayEquals(arrowsExpected, arrows);

        // Test arc direction calculation
        icss.calcArcDirections(edges, arrows);
        boolean[][] arrowsExpected2 = {
            {false, false, true},
            {false, false, false},
            {true, false, false}
        };
        assertArrayEquals(arrowsExpected2, arrows);

        // Test options
        Enumeration<Object> optEnum = icss.listOptions();
        String[] expectedOptions = {"-cardinality <num>", "-U"};
        int i = 0;
        while (optEnum.hasMoreElements()) {
            String option = optEnum.nextElement().toString();
            assertEquals(expectedOptions[i], option);
            i++;
        }

        // Test set options
        String[] options = {"-cardinality 5"};
        icss.setOptions(options);
        assertEquals(5, icss.getMaxCardinality());
    }

    @Test
    public void testListOptions() throws Exception {
        ICSSearchAlgorithm icss = new ICSSearchAlgorithm();
        Enumeration<Object> optEnum = icss.listOptions();
        int i = 0;
        while (optEnum.hasMoreElements()) {
            String option = optEnum.nextElement().toString();
            System.out.println(option);
            i++;
        }
    }
}