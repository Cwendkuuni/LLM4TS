package net.sf.javaml.core;

import static org.junit.Assert.*;

import org.junit.Test;

import net.sf.javaml.distance.DistanceMeasure;
import net.sf.javaml.distance.euclidean.EuclideanDistance;
import net.sf.javaml.instance.Instance;
import net.sf.javaml.instance.InstanceFactory;
import net.sf.javaml.instance.Instances;
import net.sf.javaml.instance.dataset.DefaultDataset;
import net.sf.javaml.instance.dataset.Fold;

public class FoldTest {

 @Test
 public void testFold() {
 Dataset parentDataset = new DefaultDataset();
 Instance instance1 = InstanceFactory.build(new double[]{1.0, 2.0}, new double[]{1.0, 1.1});
 Instance instance2 = InstanceFactory.build(new double[]{2.0, 3.0}, new double[]{2.0, 2.1});
 parentDataset.add(instance1);
 parentDataset.add(instance2);

 int[] indices = new int[]{0, 1};
 Fold fold = new Fold(parentDataset, indices);

 assertEquals(parentDataset.size(), fold.size());

 for (int i = 0; i < fold.size(); i++) {
 Instance testInstance = fold.instance(i);
 assertEquals(parentDataset.instance(indices[i]), testInstance);
 }

 DistanceMeasure distanceMeasure = new EuclideanDistance();
 Set<Instance> kNearestInstances = fold.kNearest(1, instance1, distanceMeasure);
 Instance nearestInstance = kNearestInstances.iterator().next();
 Instance expectedNearestInstance = parentDataset.instance(indices[0]);
 assertEquals(expectedNearestInstance, nearestInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedMethods() {
 Fold fold = new Fold(null, null);
 fold.add(null);
 fold.addAll(null);
 fold.addAll(0, null);
 fold.clear();
 fold.contains(null);
 fold.containsAll(null);
 fold.remove(null);
 fold.set(0, null);
 }

 @Test
 public void testFoldIterator() {
 Dataset parentDataset = new DefaultDataset();
 Instance instance1 = InstanceFactory.build(new double[]{1.0, 2.0}, new double[]{1.0, 1.1});
 Instance instance2 = InstanceFactory.build(new double[]{2.0, 3.0}, new double[]{2.0, 2.1});
 Instance instance3 = InstanceFactory.build(new double[]{3.0, 4.0}, new double[]{3.0, 3.1});
 parentDataset.add(instance1);
 parentDataset.add(instance2);
 parentDataset.add(instance3);

 int[] indices = new int[]{1, 2, 0};
 Fold fold = new Fold(parentDataset, indices);

 Fold.FoldIterator iterator = fold.iterator();
 Instance testInstance = iterator.next();
 assertEquals(instance2, testInstance);
 testInstance = iterator.next();
 assertEquals(instance3, testInstance);
 testInstance = iterator.next();
 assertEquals(instance1, testInstance);
 }
}