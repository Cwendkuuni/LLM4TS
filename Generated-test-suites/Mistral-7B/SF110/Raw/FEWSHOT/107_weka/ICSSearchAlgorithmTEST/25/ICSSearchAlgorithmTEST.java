package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import weka.classifiers.bayes.*;
import weka.classifiers.bayes.net.*;
import java.util.*;
import weka.classifiers.bayes.net.search.*;
import java.io.*;
import weka.core.*;

public class ICSSearchAlgorithmTest {

    private Instances m_instances;
    private BayesNet m_bayesNet;
    private ICSSearchAlgorithm m_algorithm;

    @Before
    public void setUp() {
        m_instances = Mockito.mock(Instances.class);
        m_bayesNet = Mockito.mock(BayesNet.class);
        m_algorithm = new ICSSearchAlgorithm();
        m_algorithm.setInstances(m_instances);
        m_algorithm.setBayesNet(m_bayesNet);
    }

    @Test
    public void testSetAndGetMaxCardinality() {
        m_algorithm.setMaxCardinality(3);
        assertEquals(3, m_algorithm.getMaxCardinality());
    }

    @Test
    public void testName() {
        when(m_instances.attribute(0)).thenReturn(new Attribute("A"));
        when(m_instances.attribute(1)).thenReturn(new Attribute("B"));
        when(m_instances.attribute(2)).thenReturn(new Attribute("C"));

        assertEquals("A", m_algorithm.name(0));
        assertEquals("B", m_algorithm.name(1));
        assertEquals("C", m_algorithm.name(2));
    }

    @Test
    public void testSearch() throws Exception {
        when(m_instances.numAttributes()).thenReturn(3);

        m_algorithm.search(m_bayesNet, m_instances);

        verify(m_bayesNet, times(1)).getParentSet(0);
        verify(m_bayesNet, times(3)).getParentSet(1);
        verify(m_bayesNet, times(3)).getParentSet(2);

        for (int i = 0; i < 3; ++i) {
            ParentSet parentSet = m_bayesNet.getParentSet(i);
            assertEquals(0, parentSet.getNrOfParents());
            for (int j = 0; j < 3; ++j) {
                if (m_algorithm.arrows[j][i]) {
                    parentSet.addParent(j, m_instances);
                }
            }
        }
    }

    @Test(expected = Exception.class)
    public void testSearchWithNullInstances() throws Exception {
        m_algorithm.setInstances(null);
        m_algorithm.search(m_bayesNet, m_instances);
    }

    @Test(expected = Exception.class)
    public void testSearchWithNullBayesNet() throws Exception {
        m_algorithm.setBayesNet(null);
        m_algorithm.search(m_bayesNet, m_instances);
    }
}