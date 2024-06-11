package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import net.sf.javaml.distance.*;
import net.sf.javaml.instance.*;
import org.junit.*;

public class DefaultDatasetTest {

 private DefaultDataset dataset;
 private Instance instance;
 private DistanceMeasure distanceMock;

 @Before
 public void setUp() {
 instance = mock(Instance.class);
 distanceMock = mock(DistanceMeasure.class);
 dataset = new DefaultDataset();
 }

 @Test
 public void testAddAll() {
 Collection<Instance> instances = Arrays.asList(instance, instance, instance);
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testAddAllWithCheck() {
 Collection<Instance> instances = Arrays.asList(instance, instance, instance);
 dataset.check(instances);
 dataset.addAll(instances);
 assertEquals(instances.size(), dataset.size());
 }

 @Test
 public void testAddInstance() {
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testAddInstanceWithCheck() {
 dataset.check(instance);
 dataset.add(instance);
 assertEquals(1, dataset.size());
 }

 @Test
 public void testClear() {
 dataset.clear();
 assertEquals(0, dataset.size());
 }

 @Test
 public void testClasses() {
 Instance instanceWithClass = createInstanceWithClass("label1");
 Instance instanceWithoutClass = new DenseInstance(new double[5], new double[1]);
 dataset.add(instanceWithClass);
 dataset.add(instanceWithoutClass);
 SortedSet<Object> classes = dataset.classes();
 assertEquals(1, classes.size());
 assertEquals("label1", classes.first());
 }

 @Test
 public void testKNearest() {
 Instance queryInstance = createInstanceWithClass("label1");
 when(distanceMock.measure(instanceWithClass, queryInstance)).thenReturn(1.0);
 when(distanceMock.measure(instanceWithNoClass, queryInstance)).thenReturn(2.0);
 dataset.add(instanceWithClass);
 dataset.add(instanceWithNoClass);
 Set<Instance> nearestInstances = dataset.kNearest(2, queryInstance, distanceMock);
 assertEquals(2, nearestInstances.size());
 assertTrue(nearestInstances.contains(instanceWithClass));
 assertTrue(nearestInstances.contains(instanceWithNoClass));
 }

 @Test
 public void testNoAttributes() {
 Instance instanceWithTwoAttributes = new DenseInstance(new double[]{1.0, 2.0}, new double[]{1.0});
 Instance instanceWithOneAttribute = new DenseInstance(new double[]{1.0}, new double[]{1.0});
 dataset.add(instanceWithTwoAttributes);
 dataset.add(instanceWithOneAttribute);
 assertEquals(instanceWithTwoAttributes.noAttributes(), dataset.noAttributes());
 }

 @Test
 public void testClassIndex() {
 dataset.add(instanceWithClass("label1"));
 dataset.add(instanceWithClass("label2"));
 assertEquals(1, dataset.classIndex("label1"));
 assertEquals(-1, dataset.classIndex("label3"));
 }

 @Test
 public void testClassValue() {
 dataset.add(instanceWithClass("label1"));
 assertEquals("label1", dataset.classValue(0));
 }

 @Test
 public void testCopy() {
 DefaultDataset copy = dataset.copy();
 copy.add(instance);
 assertFalse(dataset.contains(instance));
 assertTrue(copy.contains(instance));
 }

 private Instance createInstanceWithClass(String label) {
 Instance instance = new DenseInstance(new double[5], new double[1]);
 instance.setClassValue(label);
 return instance;
 }

 @After
 public void tearDown() {
 reset(instance);
 reset(distanceMock);
 }
}