package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import net.sf.javaml.core.*;
import net.sf.javaml.distance.*;

@RunWith(JUnit4.class)
public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance1, instance2;
 private Instance instanceWithMoreAttributes;
 private Collection<Instance> instancesCollection;
 private Set<Object> classesSet;
 private Object classToAdd;

 @Before
 public void setUp() {
 dataset = new DefaultDataset();
 instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0));
 instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0}, new Double(1.0));
 instanceWithMoreAttributes = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new Double(0.0));
 instancesCollection = new ArrayList<>();
 instancesCollection.add(instance1);
 instancesCollection.add(instance2);
 classesSet = new TreeSet<>();
 }

 @Test
 public void testDefaultDatasetConstructorWithCollection() {
     dataset = new DefaultDataset(instancesCollection);
     assertEquals(2, dataset.size());
     assertEquals(3, dataset.noAttributes());
     assertSame(Collections.unmodifiableSet(dataset.classes()), classesSet);
 }

 @Test
 public void testDefaultDatasetEmptyConstructor() {
     dataset = new DefaultDataset();
     assertEquals(0, dataset.size());
     assertEquals(0, dataset.noAttributes());
     assertNotNull(dataset.classes());
     assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testAddAllMethod() {
     dataset.addAll(instancesCollection);
     assertEquals(2, dataset.size());
     assertEquals(3, dataset.noAttributes());
     assertSame(Collections.unmodifiableSet(dataset.classes()), classesSet);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAllMethodWithNullCollection() {
     dataset.addAll(null);
 }

 @Test
 public void testAddInstanceMethod() {
     dataset.add(instance1);
     assertEquals(1, dataset.size());
     assertEquals(3, dataset.noAttributes());
     assertSame(Collections.unmodifiableSet(dataset.classes()), classesSet);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddInstanceMethodWithNullInstance() {
     dataset.add(null);
 }

 @Test
 public void testAddInstanceMethodWithInstanceWithMoreAttributes() {
     dataset.add(instanceWithMoreAttributes);
     assertEquals(1, dataset.size());
     assertEquals(5, dataset.noAttributes());
     assertSame(Collections.unmodifiableSet(dataset.classes()), classesSet);
 }

 @Test
 public void testCheckMethod() {
     Collection<Instance> instancesCollectionWithMoreAttributesInstance = new ArrayList<>();
     instancesCollectionWithMoreAttributesInstance.add(instanceWithMoreAttributes);
     assertFalse(dataset.check(instancesCollectionWithMoreAttributesInstance));
     assertTrue(dataset.addAll(instancesCollectionWithMoreAttributesInstance));
 }

 @Test
 public void testClearMethod() {
     dataset.add(instance1);
     dataset.add(instance2);
     dataset.clear();
     assertEquals(0, dataset.size());
     assertEquals(0, dataset.noAttributes());
     assertNotNull(dataset.classes());
     assertTrue(dataset.classes().isEmpty());
 }

 @Test
 public void testGetInstanceMethod() {
     dataset.add(instance1);
     dataset.add(instance2);
     assertEquals(instance1, dataset.instance(0));
     assertEquals(instance2, dataset.instance(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceMethodWithNegativeIndex() {
     dataset.add(instance1);
     dataset.add(instance2);
     dataset.getInstance(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetInstanceMethodWithOutOfBoundsIndex() {
     dataset.add(instance1);
     dataset.add(instance2);
     dataset.getInstance(2);
 }

 @Test
 public void testClassesMethod() {
     dataset.add(instance1);
     dataset.add(instance2);
     assertSame(Collections.unmodifiableSet(dataset.classes()), classesSet);
 }

 @Test
 public void testKNearestMethod() {
     dataset.addAll(instancesCollection);
     DistanceMeasure distanceMeasure = new EuclideanDistance();
     Set<Instance> expectedResult = new HashSet<>();
     expectedResult.add(instance1);
     expectedResult.add(instance2);
     Set<Instance> result = dataset.kNearest(2, instance1, distanceMeasure);
     assertEquals(2, result.size());
     assertTrue(result.containsAll(expectedResult));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNullInstance() {
     dataset.addAll(instancesCollection);
     DistanceMeasure distanceMeasure = new EuclideanDistance();
     dataset.kNearest(2, null, distanceMeasure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNullDistanceMeasure() {
     dataset.addAll(instancesCollection);
     Set<Instance> result = dataset.kNearest(2, instance1, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKNearestMethodWithNegativeK() {
     dataset.addAll(instancesCollection);
     DistanceMeasure distanceMeasure = new EuclideanDistance();
     dataset.kNearest(-1, instance1, distanceMeasure);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsMethodWithNegativeNumFolds() {
     dataset.addAll(instancesCollection);
     Random random = new Random();
     dataset.folds(-1, random);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFoldsMethodWithNullRandom() {
     dataset.addAll(instancesCollection);
     dataset.folds(5, null);
 }

 @Test
 public void testFoldsMethod() {
     dataset.addAll(instancesCollection);
     Random random = new Random();
     Dataset[] expectedResult = new Dataset[5];
     for (int i = 0; i < 5; i++) {
         expectedResult[i] = new Fold(dataset, new int[5]);
     }
     Dataset[] result = dataset.folds(5, random);
     assertArrayEquals(expectedResult, result);
 }

 @Test
 public void testNoAttributesMethod() {
     dataset.add(instance1);
     assertEquals(instance1.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndexMethod() {
     dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
     dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
     assertEquals(0, dataset.classIndex(0.0));
     assertEquals(1, dataset.classIndex(1.0));
 }

 @Test
 public void testClassValueMethod() {
     dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(0.0)));
     dataset.add(new DenseInstance(new double[]{1.0, 2.0, 3.0}, new Double(1.0)));
     assertEquals(0.0, dataset.classValue(0));
     assertEquals(1.0, dataset.classValue(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueMethodWithNegativeIndex() {
     dataset.add(instance1);
     dataset.add(instance2);
     dataset.getClassValue(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testClassValueMethodWithOutOfBoundsIndex() {
     dataset.add(instance1);
     dataset.add(instance2);
     dataset.getClassValue(2);
 }

 @Test
 public void testCopyMethod() {
     dataset.add(instance1);
     dataset.add(instance2);
     DefaultDataset copy = dataset.copy();
     assertEquals(dataset.size(), copy.size());
     assertEquals(dataset.noAttributes(), copy.noAttributes());
     assertSame(dataset.classes(), copy.classes());
 }
}