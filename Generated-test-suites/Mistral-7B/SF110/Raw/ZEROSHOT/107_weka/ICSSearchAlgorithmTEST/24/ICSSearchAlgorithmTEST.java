package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testICSSearchAlgorithm() throws Exception {
        ICSSearchAlgorithm icssa = new ICSSearchAlgorithm();
        Instances instances = new Instances(new FileReader("data/contact-lenses.arff"));
        instances.setClassIndex(instances.numAttributes() - 1);

        BayesNet bayesNet = new BayesNet();
        bayesNet.setSearchAlgorithm(icssa);
        bayesNet.buildClassifier(instances);

        // Test setMaxCardinality method
        icssa.setMaxCardinality(3);
        assertEquals(3, icssa.getMaxCardinality());

        // Test name method
        Instances instancesTest = new Instances(new ByteArrayInputStream(new byte[]{"S,A,B,C,Class\n", "1,a,b,1,0\n", "1,a,b,1,1\n", "1,a,b,0,0\n", "1,a,b,0,1\n", "1,a,c,1,0\n", "1,a,c,1,1\n", "1,a,c,0,0\n", "1,a,c,0,1\n", "1,b,b,1,0\n", "1,b,b,1,1\n", "1,b,b,0,0\n", "1,b,b,0,1\n", "1,b,c,1,0\n", "1,b,c,1,1\n", "1,b,c,0,0\n", "1,b,c,0,1\n", "1,c,c,1,0\n", "1,c,c,1,1\n", "1,c,c,0,0\n", "1,c,c,0,1\n"}.toString().getBytes()));
        instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
        BayesNet bayesNetTest = new BayesNet();
        bayesNetTest.setSearchAlgorithm(icssa);
        bayesNetTest.buildClassifier(instancesTest);
        for (int i = 0; i < icssa.maxn(); i++) {
            assertEquals(instancesTest.attribute(i).name(), icssa.name(i));
        }

        // Test search method
        Instances instancesForTest = new Instances(new ByteArrayInputStream(new byte[]{"S,A,B,C,Class\n", "1,a,b,1,0\n", "1,a,b,1,1\n", "1,a,b,0,0\n", "1,a,b,0,1\n", "1,a,c,1,0\n", "1,a,c,1,1\n", "1,a,c,0,0\n", "1,a,c,0,1\n", "1,b,b,1,0\n", "1,b,b,1,1\n", "1,b,b,0,0\n", "1,b,b,0,1\n", "1,b,c,1,0\n", "1,b,c,1,1\n", "1,b,c,0,0\n", "1,b,c,0,1\n", "1,c,c,1,0\n", "1,c,c,1,1\n", "1,c,c,0,0\n", "1,c,c,0,1\n"}.toString().getBytes()));
        instancesForTest.setClassIndex(instancesForTest.numAttributes() - 1);
        ICSSearchAlgorithm icssaForTest = new ICSSearchAlgorithm();
        icssaForTest.setMaxCardinality(3);
        bayesNetTest.setSearchAlgorithm(icssaForTest);
        icssaForTest.search(bayesNetTest, instancesForTest);

        // Test calcDependencyGraph method
        boolean[][] edges = new boolean[icssaForTest.maxn() + 1][];
        SeparationSet[][] sepsets = new SeparationSet[icssaForTest.maxn() + 1][];
        for (int iNode = 0; iNode < icssaForTest.maxn() + 1; ++iNode) {
            edges[iNode] = new boolean[icssaForTest.maxn()];
            sepsets[iNode] = new SeparationSet[icssaForTest.maxn()];
        }
        icssaForTest.calcDependencyGraph(edges, sepsets);
        for (int iNode1 = 0; iNode1 < icssaForTest.maxn() - 1; ++iNode1) {
            for (int iNode2 = iNode1 + 1; iNode2 < icssaForTest.maxn(); ++iNode2) {
                assertFalse(edges[iNode1][iNode2]);
                assertFalse(edges[iNode2][iNode1]);
            }
        }
    }
}

class SeparationSet {
    public int[] m_set;

    public SeparationSet() {
        this.m_set = new int[ICSSearchAlgorithmTest.ICSSearchAlgorithm.this.getMaxCardinality() + 1];
    }

    public boolean contains(final int nItem) {
        for (int iItem = 0; iItem < ICSSearchAlgorithmTest.ICSSearchAlgorithm.this.getMaxCardinality() && this.m_set[iItem] != -1; ++iItem) {
            if (this.m_set[iItem] == nItem) {
                return true;
            }
        }
        return false;
    }
}