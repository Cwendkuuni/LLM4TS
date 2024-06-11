package net.sf.javaml.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.EuclideanDistance;

import org.junit.Test;

public class FoldTest {

	@Test
	public void testFold() {
		// Test constructor
		Dataset parent = new DefaultDataset();
		Instance inst1 = new DefaultInstance(new double[] { 1.0, 2.0 });
		Instance inst2 = new DefaultInstance(new double[] { 3.0, 4.0 });
		parent.add(inst1);
		parent.add(inst2);

		int[] indices = new int[] { 0, 1 };
		Fold fold = new Fold(parent, indices);

		assertSame(parent, fold.parent);
		assertSame(indices, fold.indices);
		assertFalse(fold.canAdd(inst1));
		assertFalse(fold.canAdd(inst2));

		// Test methods inherited from Dataset
		Set<Object> classes = fold.classes();
		assertTrue(classes.equals(parent.classes()));

		assertNull(fold.folds(2, new Random()));
		Instance instance = fold.instance(0);
		assertSame(inst1, instance);
		assertNull(fold.kNearest(2, inst1, new EuclideanDistance()));

		// Test add methods
		assertFalse(fold.add(inst1));
		assertFalse(fold.add(inst2));

		//Test size()
		assertTrue(fold.size() == 2);

		// Test toArray() and toArray(T[])
		Instance[] instances = (Instance[]) fold.toArray();
		assertSame(inst1, instances[0]);
		assertSame(inst2, instances[1]);
		Instance[] instances2 = (Instance[]) fold.toArray(new Instance[2]);
		assertSame(instances[0], instances2[0]);
		assertSame(instances[1], instances2[1]);

		// Test noAttributes() method
		assertTrue(fold.noAttributes() == parent.noAttributes());

		// Test copy() method
		Dataset copy = fold.copy();
		assertSame(fold.size(), copy.size());
		for (Instance instance2 : copy) {
			Instance instance3 = fold.instance(fold.indexOf(instance2));
			assertSame(instance2, instance3);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testUnsupportedMethods() {
		Fold fold = new Fold(new DefaultDataset(), new int[0]);

		fold.add(new DefaultInstance(new double[1]));
		fold.addAll(Arrays.asList(new DefaultInstance[0]));
		fold.addAll(2, Arrays.asList(new DefaultInstance[0]));
		fold.clear();
		fold.contains(new DefaultInstance(new double[1]));
		fold.containsAll(Arrays.asList(new DefaultInstance[0]));
		fold.get(0);
		fold.indexOf(new DefaultInstance(new double[1]));
		fold.isEmpty();
		fold.iterator();
		fold.listIterator();
		fold.listIterator(1);
		fold.remove(new DefaultInstance(new double[1]));
		fold.remove(0);
		fold.removeAll(Collections.singletonList(new DefaultInstance(new double[1])));
		fold.retainAll(Collections.singletonList(new DefaultInstance(new double[1])));
		fold.set(0, new DefaultInstance(new double[1]));
		fold.subList(1, 2);
	}
}