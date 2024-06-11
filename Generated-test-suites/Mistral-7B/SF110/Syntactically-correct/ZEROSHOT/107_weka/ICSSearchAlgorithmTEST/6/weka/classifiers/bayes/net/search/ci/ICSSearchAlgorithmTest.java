package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.net.InstancesBayes;
import weka.classifiers.bayes.net.SearchAlgorithm;
import weka.classifiers.bayes.net.search.CISearchAlgorithm;
import weka.classifiers.bayes.net.search.SeparationSet;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class ICSSearchAlgorithmTest {

	private ICSSearchAlgorithm icssa;
	private Instances instances;
	private BayesNet bn;

	@Before
	public void setUp() throws Exception {
		icssa = new ICSSearchAlgorithm();
		icssa.setMaxCardinality(2);
		instances = new Instances(new FileReader("contact-lenses.arff"));
		instances.setClassIndex(instances.numAttributes() - 1);
		bn = new BayesNet();
		bn.setSearchAlgorithm(icssa);
	}

	@Test
	public void testName() {
		int iAttribute = 0;
		assertEquals("pupilSize", icssa.name(iAttribute));
	}

	@Test
	public void testMaxn() {
		assertEquals(8, icssa.maxn());
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
		icssa.search(bn, instances);
		assertNotNull(icssa.m_BayesNet);
		assertNotNull(icssa.m_instances);
	}

	@Test
	public void testCalcDependencyGraph() throws Exception {
		boolean[][] edges = new boolean[8][];
		SeparationSet[][] sepsets = new SeparationSet[8][];
		icssa.calcDependencyGraph(edges, sepsets);
		// Add check for edges here
		// Add check for sepsets here
	}

	@Test
	public void testExistsSepSet() throws Exception {
		final int iNode1 = 0;
		final int iNode2 = 1;
		final int nCardinality = 1;
		final boolean[][] edges = new boolean[8][];
		SeparationSet oSepSet = icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
		assertNotNull(oSepSet);
		assertEquals(iNode1, oSepSet.m_set[0]);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExistsSepSetInvalidCardinality() throws Exception {
		final int iNode1 = 0;
		final int iNode2 = 1;
		final int nCardinality = -1;
		final boolean[][] edges = new boolean[8][];
		icssa.existsSepSet(iNode1, iNode2, nCardinality, edges);
	}

	@Test
	public void testNext() {
		int x = icssa.next(-1, 0, 1, new boolean[8][]);
		assertEquals(2, x);
	}

	@Test
	public void testCalcVeeNodes() throws Exception {
		boolean[][] edges = new boolean[8][];
		boolean[][] arrows = new boolean[8][];
		SeparationSet[][] sepsets = new SeparationSet[8][];
		icssa.calcVeeNodes(edges, arrows, sepsets);
		// Add check for arrows here
	}

	@Test
	public void testCalcArcDirections() throws Exception {
		boolean[][] edges = new boolean[8][];
		boolean[][] arrows = new boolean[8][];
		icssa.calcArcDirections(edges, arrows);
		// Add check for arrows here
	}

	@Test
	public void testListOptions() {
		Vector<Object> options = (Vector<Object>) icssa.listOptions();
		assertEquals(5, options.size());
		assertEquals("-cardinality <num>", options.get(3).toString());
	}

	@Test
	public void testSetOptions() throws Exception {
		icssa.setOptions(new String[]{"-cardinality 3"});
		assertEquals(3, icssa.getMaxCardinality());
	}

	@Test
	public void testGetOptions() {
		String[] options = icssa.getOptions();
		assertEquals(5, options.length);
		assertEquals("-cardinality", options[2].substring(0, 9));
		assertEquals("3", options[2].substring(10));
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
		assertEquals("$Revision: 8034 $", icssa.getRevision());
	}

	@Test
	public void testMain() {
		try {
			bn.buildClassifier(instances);
			assertNotNull(bn);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSeparationSet() {
		SeparationSet sepSet = new SeparationSet();
		sepSet.m_set = new int[icssa.getMaxCardinality() + 1];
		assertTrue(sepSet.contains(0));
		assertTrue(sepSet.contains(1));
		assertFalse(sepSet.contains(2));
	}
}