package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DistanceMeasure;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.Fold;
import net.sf.javaml.distance.EuclideanDistance;

public class DefaultDatasetTest {

 private DefaultDataset createDefaultDataset() {
 return new DefaultDataset() {
 @Override
 public Instance instance(int index) {
 return new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0));
 }
 };
 }

 @Test
 public void testDefaultConstructor() {
 DefaultDataset ds = new DefaultDataset();
 assertEquals(0, ds.size());
 }

 @Test
 public void testConstructorWithCollection() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(3, ds.size());
 }

 @Test
 public void testAddAll() {
 DefaultDataset ds = new DefaultDataset();
 ds.addAll(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(3, ds.size());
 }

 @Test
 public void testClasses() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(2, ds.classes().size());
 }

 @Test
 public void testClear() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 ds.clear();
 assertEquals(0, ds.size());
 }

 @Test
 public void testKNearest() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{4.0, 5.0, 6.0, 7.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{5.0, 6.0, 7.0, 8.0}, new Double(0.0)));
 }});
 DistanceMeasure dm = new EuclideanDistance();
 Instance testInstance = new DenseInstance(new double[]{3.5, 4.5, 5.5, 6.5}, new Double(0.0));
 List<Instance> result = ds.kNearest(2, testInstance, dm);
 assertEquals(2, result.size());
 }

 @Test
 public void testFolds() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{4.0, 5.0, 6.0, 7.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{5.0, 6.0, 7.0, 8.0}, new Double(0.0)));
 }});
 Random random = new Random(0);
 Dataset[] result = ds.folds(5, random);
 assertEquals(5, result.length);
 }

 @Test
 public void testNoAttributes() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(4, ds.noAttributes());
 }

 @Test
 public void testClassIndex() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(0, ds.classIndex(0.0));
 assertEquals(1, ds.classIndex(1.0));
 }

 @Test
 public void testClassValue() {
 DefaultDataset ds = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 assertEquals(0.0, ds.classValue(0));
 assertEquals(1.0, ds.classValue(1));
 }

 @Test
 public void testCopy() {
 DefaultDataset ds1 = new DefaultDataset(new ArrayList<Instance>() {{
 add(new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, new Double(0.0)));
 add(new DenseInstance(new double[]{2.0, 3.0, 4.0, 5.0}, new Double(1.0)));
 add(new DenseInstance(new double[]{3.0, 4.0, 5.0, 6.0}, new Double(0.0)));
 }});
 DefaultDataset ds2 = ds1.copy();
 assertEquals(ds1.size(), ds2.size());
 }
}