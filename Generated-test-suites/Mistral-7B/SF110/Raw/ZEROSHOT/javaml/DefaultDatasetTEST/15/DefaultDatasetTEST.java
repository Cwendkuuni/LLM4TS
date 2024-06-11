package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.javaml.distance.EuclideanDistance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.InstanceLabeled;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.tools.data.WekaDataSource;

import org.junit.Test;

public class DefaultDatasetTest {

	@Test
	public void testEmptyConstructor() {
		DefaultDataset dataset = new DefaultDataset();
		assertTrue(dataset.isEmpty());
		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes());
		assertNull(dataset.classes());
	}

	@Test
	public void testCollectionConstructor() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		assertEquals(2, dataset.size());
		assertEquals(3, dataset.maxAttributes());
		assertEquals(new TreeSet<>(Arrays.asList(0.0, 1.0)), dataset.classes());

		Instance instance = dataset.instance(0);
		assertEquals(new double[] { 1.0, 2.0, 3.0 }, instance.toArray());
		assertEquals(0, instance.classValue());
	}

	@Test
	public void testAddAll() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));

		DefaultDataset dataset = new DefaultDataset();
		dataset.addAll(instances);

		assertEquals(2, dataset.size());
		assertEquals(3, dataset.maxAttributes());
		assertEquals(new TreeSet<>(Arrays.asList(0.0, 1.0)), dataset.classes());

		Instance instance = dataset.instance(0);
		assertEquals(new double[] { 1.0, 2.0, 3.0 }, instance.toArray());
		assertEquals(0, instance.classValue());
	}

	@Test
	public void testClear() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		dataset.clear();

		assertEquals(0, dataset.size());
		assertEquals(0, dataset.maxAttributes());
		assertNull(dataset.classes());
	}

	@Test
	public void testAdd() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));

		DefaultDataset dataset = new DefaultDataset();
		Instance instance = InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1);
		dataset.add(instance);

		assertEquals(2, dataset.size());
		assertEquals(3, dataset.maxAttributes());
		assertEquals(new TreeSet<>(Arrays.asList(0.0, 1.0)), dataset.classes());

		Instance addedInstance = dataset.instance(1);
		assertEquals(new double[] { 4.0, 5.0, 6.0 }, addedInstance.toArray());
		assertEquals(1, addedInstance.classValue());
	}

	@Test
	public void testRemove() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		dataset.remove(dataset.instance(1));

		assertEquals(1, dataset.size());
		assertEquals(3, dataset.maxAttributes());
		assertEquals(new TreeSet<>(Arrays.asList(0.0)), dataset.classes());

		assertFalse(dataset.contains(dataset.instance(1)));
	}

	@Test
	public void testGet() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		Instance instance = dataset.instance(0);
		assertEquals(new double[] { 1.0, 2.0, 3.0 }, instance.toArray());
		assertEquals(0, instance.classValue());
	}

	@Test
	public void testClasses() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		assertEquals(new TreeSet<>(Arrays.asList(0.0, 1.0)), dataset.classes());
	}

	@Test
	public void testKNearest() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 2.0, 3.0, 4.0 }, 1));
		instances.add(InstanceFactory.createLabeled(new double[] { 3.0, 4.0, 5.0 }, 1));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		instances.add(InstanceFactory.createLabeled(new double[] { 5.0, 6.0, 7.0 }, 0));
		DefaultDataset dataset = new DefaultDataset(instances);

		Instance query = InstanceFactory.createLabeled(new double[] { 4.5, 5.5, 6.5 }, 1);

		List<Instance> result = dataset.kNearest(2, query, new EuclideanDistance());
		Collections.sort(result, new Comparator<Instance>() {
			@Override
			public int compare(Instance o1, Instance o2) {
				return Double.compare(o1.distance(query), o2.distance(query));
			}
		});

		assertEquals(2, result.size());
		assertEquals(instances.get(2), result.get(0));
		assertEquals(instances.get(3), result.get(1));
	}

	@Test
	public void testFolds() {
		List<Instance> instances = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			instances.add(InstanceFactory.createLabeled(new double[] { Math.random(), Math.random(), Math.random() }, Math.random()));
		}
		DefaultDataset dataset = new DefaultDataset(instances);

		Dataset[] folds = dataset.folds(5, new Random());

		assertEquals(5, folds.length);
		for (Dataset fold : folds) {
			assertTrue(fold instanceof Fold);
			assertEquals(dataset.size() / 5, fold.size());
		}
	}

	@Test
	public void testNoAttributes() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		assertEquals(3, dataset.noAttributes());
	}

	@Test
	public void testClassIndex() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		assertEquals(0, dataset.classIndex(0.0));
		assertEquals(1, dataset.classIndex(1.0));
		assertNull(dataset.classIndex(2.0));
	}

	@Test
	public void testClassValue() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset dataset = new DefaultDataset(instances);

		assertEquals(0, dataset.classValue(0));
		assertEquals(1, dataset.classValue(1));
		assertNull(dataset.classValue(2));
	}

	@Test
	public void testCopy() {
		List<Instance> instances = new ArrayList<>();
		instances.add(InstanceFactory.createLabeled(new double[] { 1.0, 2.0, 3.0 }, 0));
		instances.add(InstanceFactory.createLabeled(new double[] { 4.0, 5.0, 6.0 }, 1));
		DefaultDataset orgDataset = new DefaultDataset(instances);

		DefaultDataset copyDataset = orgDataset.copy();

		assertEquals(orgDataset.size(), copyDataset.size());
		assertEquals(orgDataset.maxAttributes(), copyDataset.maxAttributes());
		assertEquals(orgDataset.classes(), copyDataset.classes());
		for (int i = 0; i < orgDataset.size(); ++i) {
			Instance orgInstance = orgDataset.instance(i);
			Instance copyInstance = copyDataset.instance(i);
			assertEquals(orgInstance.toArray(), copyInstance.toArray());
			assertEquals(orgInstance.classValue(), copyInstance.classValue());
		}
	}

	@Test
	public void testLoadFromDataSource() throws Exception {
		String dataFile = "data/iris.arff";
		WekaDataSource source = new WekaDataSource(dataFile);
		DefaultDataset dataset = new DefaultDataset( source.getDataSet() );

		assertEquals(150, dataset.size());
		assertEquals(4, dataset.maxAttributes());
		assertEquals(3, dataset.classes().size());
	}
}