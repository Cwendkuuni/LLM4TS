package weka.classifiers.bayes.net.search.ci;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.InstancesBayes;
import weka.classifiers.bayes.net.BayesNetFactory;
import weka.classifiers.bayes.net.search.CISearchAlgorithm;
import weka.classifiers.bayes.net.search.SeparationSet;
import weka.core.Instances;
import weka.core.RevisionUtils;

public class ICSSearchAlgorithmTest {

	private ICSSearchAlgorithm icssa;
	private Instances instances;
	private BayesNet bnet;

	@Before
	public void setUp() throws Exception {
		instances = new InstancesBayes(new FileReader("contact-lenses.arff"));
		instances.setClassIndex(instances.numAttributes() - 1);
		bnet = new BayesNetFactory().buildClassifier(instances);
		icssa = new ICSSearchAlgorithm();
		icssa.setMaxCardinality(2);
		icssa.search(bnet, instances);
	}

	@Test
	public void testName() {
		assertNotNull(icssa);
		assertEquals("ICSSearchAlgorithm", icssa.getClass().getName());
		assertEquals(2, icssa.getMaxCardinality());
		assertEquals("weka.classifiers.bayes.net.search.ci.ICSSearchAlgorithm", icssa.getClass().getCanonicalName());
	}

	@Test
	public void testMaxn() {
		assertEquals(instances.numAttributes(), icssa.maxn());
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
		assertNotNull(icssa.m_BayesNet);
		assertNotNull(icssa.m_instances);
		assertNull(icssa.m_ParentSet);
		assertEquals(instances, icssa.m_instances);
		assertEquals(bnet, icssa.m_BayesNet);
	}

	@Test
	public void testCalcDependencyGraph() {
		boolean[][] edges = new boolean[instances.numAttributes() + 1][];
		SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];
		icssa.calcDependencyGraph(edges, sepsets);

		for (int i = 0; i < instances.numAttributes(); i++) {
			for (int j = 0; j < instances.numAttributes(); j++) {
				assertFalse(edges[i][j]);
				assertFalse(edges[j][i]);
			}
			edges[i][i] = true;
		}

		for (int i = 0; i < instances.numAttributes(); i++) {
			for (int j = 0; j < instances.numAttributes(); j++) {
				if (icssa.existsSepSet(i, j, 1, edges) != null) {
					assertTrue(edges[i][j] == false && edges[j][i] == false);
					assertNotNull(sepsets[i][j]);
					assertNotNull(sepsets[j][i]);
					assertEquals(1, sepsets[i][j].m_set.length);
					assertEquals(i, sepsets[i][j].m_set[0]);
					assertEquals(j, sepsets[j][i].m_set[0]);
				}
			}
		}
	}

	@Test
	public void testExistsSepSet() {
		SeparationSet sepset = new SeparationSet();
		List<Integer> list = new ArrayList<>();
		list.add(instances.attribute(1).index());
		sepset.m_set = list.stream().mapToInt(i -> i).toArray();

		int iNode1 = instances.attribute(1).index();
		int iNode2 = instances.attribute(2).index();

		boolean[][] edges = new boolean[instances.numAttributes() + 1][];
		for (int i = 0; i < instances.numAttributes(); i++) {
			edges[i][i] = true;
		}
		edges[iNode1][iNode2] = true;

		SeparationSet result = icssa.existsSepSet(iNode1, iNode2, 1, edges);
		assertNotNull(result);
		assertArrayEquals(new int[] { iNode1 }, result.m_set);
		assertArrayEquals(new int[] { iNode2 }, icssa.sepsets[iNode1][iNode2].m_set);
		assertArrayEquals(new int[] { iNode2 }, icssa.sepsets[iNode2][iNode1].m_set);
	}

	@Test
	public void testNext() {
		int x = -1;
		int iNode1 = instances.attribute(1).index();
		int iNode2 = instances.attribute(2).index();

		boolean[][] edges = new boolean[instances.numAttributes() + 1][];
		for (int i = 0; i < instances.numAttributes(); i++) {
			edges[i][i] = true;
			edges[i][iNode1] = true;
			edges[i][iNode2] = true;
			edges[iNode1][iNode2] = true;
		}

		assertEquals(instances.numAttributes() - 1, icssa.next(x, iNode1, iNode2, edges));
		assertEquals(instances.numAttributes(), icssa.next(instances.numAttributes() - 1, iNode1, iNode2, edges));
		assertEquals(instances.numAttributes() - 1, icssa.next(instances.numAttributes() - 1, iNode1, iNode2, edges));
		assertEquals(instances.numAttributes() - 1, icssa.next(instances.numAttributes() - 1, iNode1, iNode2, edges));
		assertEquals(instances.numAttributes(), icssa.next(instances.numAttributes() - 1, iNode1, iNode2, edges));
		assertEquals(iNode2, icssa.next(instances.numAttributes(), iNode1, iNode2, edges));
	}

	@Test
	public void testCalcVeeNodes() {
		boolean[][] edges = new boolean[instances.numAttributes() + 1][];
		boolean[][] arrows = new boolean[instances.numAttributes() + 1][];
		SeparationSet[][] sepsets = new SeparationSet[instances.numAttributes() + 1][];

		icssa.calcDependencyGraph(edges, sepsets);
		icssa.calcVeeNodes(edges, arrows, sepsets);
		icssa.calcArcDirections(edges, arrows);

		for (int i = 0; i < instances.numAttributes(); i++) {
			for (int j = 0; j < instances.numAttributes(); j++) {
				if (i != j) {
					assertFalse(arrows[i][j]);
					assertFalse(arrows[j][i]);
				}
			}
			for (int k = 0; k < instances.numAttributes(); k++) {
				if (edges[i][k] && edges[j][k] && !sepsets[i][j].contains(k)) {
					assertTrue(arrows[i][k]);
					assertTrue(arrows[j][k]);
				}
			}
		}
	}

	@Test
	public void testCalcArcDirections() {
		boolean[][] edges = new boolean[instances.numAttributes() + 1][];
		boolean[][] arrows = new boolean[instances.numAttributes() + 1][];

		icssa.calcDependencyGraph(edges, null);
		icssa.calcVeeNodes(edges, arrows, null);
		icssa.calcArcDirections(edges, arrows);

		for (int i = 0; i < instances.numAttributes(); i++) {
			for (int j = 0; j < instances.numAttributes(); j++) {
				if (i != j && edges[i][j]) {
					assertTrue(arrows[i][j]);
					assertTrue(arrows[j][i]);
				}
			}
		}
	}

	@Test
	public void testListOptions() {
		List<Option> options = new ArrayList<>();
		options.add(new Option("", "cardinality", 1, "When determining whether an edge exists a search is performed for a set Z that separates the nodes. MaxCardinality determines the maximum size of the set Z. This greatly influences the length of the search. (default 2)"));
		assertEquals(options.toArray(new Option[options.size()]), icssa.listOptions());
	}

	@Test
	public void testSetOptions() throws Exception {
		icssa.setOptions(new String[] { "-cardinality", "3" });
		assertEquals(3, icssa.getMaxCardinality());
	}

	@Test
	public void testGetOptions() {
		List<String> options = new ArrayList<>();
		options.add("-cardinality");
		options.add("3");
		assertEquals(options.toArray(new String[options.size()]), icssa.getOptions());
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
}

class SeparationSet {
	int[] m_set;

	public SeparationSet() {
		this.m_set = new int[1];
		this.m_set[0] = -1;
	}

	public boolean contains(int nItem) {
		for (int i = 0; i < m_set.length && m_set[i] != -1; i++) {
			if (m_set[i] == nItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getRevision() {
		return RevisionUtils.extract("$Revision: 8034 $");
	}
}