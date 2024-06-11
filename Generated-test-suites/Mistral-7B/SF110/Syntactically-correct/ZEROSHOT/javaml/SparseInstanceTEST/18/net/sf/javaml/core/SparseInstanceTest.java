package test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

 private static final double DEFAULT_VALUE = 0.0;

 @org.junit.BeforeClass
 static void setUpBeforeClass() throws Exception {
 }

 @org.junit.AfterClass
 static void tearDownAfterClass() throws Exception {
 }

 @org.junit.Before
 static void setUp() throws Exception {
 }

 @org.junit.After
 static void tearDown() throws Exception {
 }

 @org.junit.Test
 public void testEmptyConstructor() {
 SparseInstance instance = new SparseInstance();
 assertEquals(-1, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @org.junit.Test
 public void testIntConstructor() {
 SparseInstance instance = new SparseInstance(5);
 assertEquals(5, instance.noAttributes());
 assertTrue(instance.isEmpty());
 }

 @org.junit.Test
 public void testDoubleArrayConstructorNoDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.isEmpty());
 Arrays.stream(datavector).forEach(d -> {
 assertTrue(instance.containsKey(Integer.valueOf((int) (d < 0 ? -1 : d))));
 assertEquals(d, instance.get(Integer.valueOf((int) (d < 0 ? -1 : d))), 0.0);
 });
 }

 @org.junit.Test
 public void testDoubleArrayConstructorWithDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertEquals(3, instance.noAttributes());
 assertFalse(instance.isEmpty());
 Arrays.stream(datavector).forEach(d -> {
 if (d != DEFAULT_VALUE) {
 assertTrue(instance.containsKey(Integer.valueOf((int) (d < 0 ? -1 : d))));
 assertEquals(d, instance.get(Integer.valueOf((int) (d < 0 ? -1 : d))), 0.0);
 } else {
 assertFalse(instance.containsKey(Integer.valueOf((int) (d < 0 ? -1 : d))));
 assertEquals(DEFAULT_VALUE, instance.get(Integer.valueOf((int) (d < 0 ? -1 : d))), 0.0);
 }
 });
 }

 @org.junit.Test
 public void testSetters() {
 SparseInstance instance = new SparseInstance();
 instance.setNoAttributes(5);
 assertEquals(5, instance.noAttributes());
 instance.setDefaultValue(1.0);
 assertEquals(1.0, instance.defaultValue, 0.0);
 }

 @org.junit.Test
 public void testMethodsForEmptyInstance() {
 SparseInstance instance = new SparseInstance();
 assertTrue(instance.isEmpty());
 assertEquals(-1, instance.noAttributes());
 assertTrue(instance.containsKey(null));
 assertEquals(DEFAULT_VALUE, instance.get(null), 0.0);
 }

 @org.junit.Test
 public void testMethodsForNonEmptyInstance() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector);
 assertFalse(instance.isEmpty());
 assertEquals(3, instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(3, entryList.size());
 entryList.forEach(entry -> {
 Integer key = entry.getKey();
 Double value = entry.getValue();
 assertTrue(instance.containsKey(key));
 assertEquals(value, instance.get(key), 0.0);
 });
 assertTrue(instance.values().containsAll(entryList));
 assertTrue(instance.keySet().containsAll(new HashSet<>(Arrays.asList(new Integer[]{0, 1, 2}))));
 }

 @org.junit.Test
 public void testMethodsForInstanceWithDefaultValue() {
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE);
 assertFalse(instance.isEmpty());
 assertEquals(3, instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(3, entryList.size());
 Arrays.stream(datavector).filter(d -> d != DEFAULT_VALUE).forEach(d -> {
 Integer key = Integer.valueOf((int) (d < 0 ? -1 : d));
 assertTrue(instance.containsKey(key));
 assertEquals(d, instance.get(key), 0.0);
 });
 entryList.forEach(entry -> {
 Double value = entry.getValue();
 if (value.equals(DEFAULT_VALUE)) {
 assertTrue(instance.containsKey(entry.getKey()));
 assertEquals(DEFAULT_VALUE, instance.get(entry.getKey()), 0.0);
 } else {
 assertFalse(instance.containsValue(DEFAULT_VALUE));
 }
 });
 assertTrue(instance.values().containsAll(entryList));
 assertTrue(instance.keySet().containsAll(new HashSet<>(Arrays.asList(new Integer[]{0, 1, 2}))));
 }

 @org.junit.Test
 public void testMethodsForInstanceWithClassValue() {
 String className = "testClass";
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, className.getClass());
 assertFalse(instance.isEmpty());
 assertEquals(3, instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(3, entryList.size());
 Arrays.stream(datavector).filter(d -> d != DEFAULT_VALUE).forEach(d -> {
 Integer key = Integer.valueOf((int) (d < 0 ? -1 : d));
 assertTrue(instance.containsKey(key));
 assertEquals(d, instance.get(key), 0.0);
 });
 entryList.forEach(entry -> {
 Double value = entry.getValue();
 if (value.equals(DEFAULT_VALUE)) {
 assertTrue(instance.containsKey(entry.getKey()));
 assertEquals(DEFAULT_VALUE, instance.get(entry.getKey()), 0.0);
 } else {
 assertFalse(instance.containsValue(DEFAULT_VALUE));
 }
 });
 assertEquals(className, instance.getClassValue());
 assertTrue(instance.toString().contains(className.getClass().getName()));
 }

 @org.junit.Test
 public void testMethodsForInstanceWithCopy() {
 String className = "testClass";
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, className.getClass());
 Instance copiedInstance = instance.copy();
 assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
 assertEquals(instance.data, copiedInstance.data);
 assertEquals(instance.defaultValue, copiedInstance.defaultValue, 0.0);
 assertEquals(instance.classValue(), copiedInstance.classValue());
 }

 @org.junit.Test
 public void testRemoveAttribute() {
 String className = "testClass";
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, className.getClass());
 int removeIndex = 1;
 instance.removeAttribute(removeIndex);
 assertEquals(instance.noAttributes() - 1, instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(instance.noAttributes(), entryList.size());
 Integer removeKey = Integer.valueOf(removeIndex);
 assertFalse(instance.containsKey(removeKey));
 }

 @org.junit.Test
 public void testRemoveAttributes() {
 String className = "testClass";
 double[] datavector = new double[]{1.0, 2.0, 3.0};
 SparseInstance instance = new SparseInstance(datavector, DEFAULT_VALUE, className.getClass());
 Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(new Integer[]{1, 2}));
 instance.removeAttributes(indicesToRemove);
 assertEquals(instance.noAttributes() - indicesToRemove.size(), instance.noAttributes());
 Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
 List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
 assertEquals(instance.noAttributes(), entryList.size());
 indicesToRemove.forEach(removeIndex -> {
 Integer removeKey = Integer.valueOf(removeIndex);
 assertFalse(instance.containsKey(removeKey));
 });
 }
}