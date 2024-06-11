import static org.junit.Assert.*;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm;
import weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm.SeparationSet;

import org.junit.Before;
import org.junit.Test;

public class ICSSearchAlgorithmTest {
    private ICSSearchAlgorithm algorithm;

    @Before
    public void setUp() {
        algorithm = new ICSSearchAlgorithm();
    }

    @Test
    public void testName() {
        assertEquals("expected name for attribute 0", "attributeName0", algorithm.name(0));
    }

    @Test
    public void testMaxn() {
        assertEquals("number of attributes should be 3", 3, algorithm.maxn());
    }

    @Test
    public void testSetMaxCardinality() {
        algorithm.setMaxCardinality(3);
        assertEquals("max cardinality should be 3", 3, algorithm.getMaxCardinality());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCardinalityNegative() {
        algorithm.setMaxCardinality(-1);
    }

    @Test
    public void testSearch() throws Exception {
        // Add your test code here. The algorithm instance (algorithm) is set up in setUp() method.
    }

    @Test
    public void testCalcDependencyGraph() {
        // Add your test code here.
    }

    @Test
    public void testExistsSepSet() {
        // Add your test code here.
    }

    @Test
    public void testNext() {
        // Add your test code here.
    }

    @Test
    public void testCalcVeeNodes() {
        // Add your test code here.
    }

    @Test
    public void testCalcArcDirections() {
        // Add your test code here.
    }

    @Test
    public void testListOptions() {
        // Add your test code here.
    }

    @Test
    public void testSetOptions() throws Exception {
        // Add your test code here.
    }

    @Test
    public void testGetOptions() {
        // Add your test code here.
    }

    @Test
    public void testMaxCardinalityTipText() {
        // Add your test code here.
    }

    @Test
    public void testGlobalInfo() {
        // Add your test code here.
    }

    @Test
    public void testGetRevision() {
        // Add your test code here.
    }

    @Test
    public void testMain() {
        // Add your test code here.
    }

    @Test
    public void testSeparationSetContains() {
        SeparationSet set = new SeparationSet();
        assertFalse("empty set should not contain an item", set.contains(1));

        set.m_set[0] = 1;
        assertTrue("set containing an item should contain that item", set.contains(1));

        set.m_set[1] = 2;
        assertFalse("set containing an item should not contain another item", set.contains(2));
    }
}