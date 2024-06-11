package net.sf.javaml.tools.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

@RunWith(JUnit4.class)
public class ARFFHandlerTest {

 @Test
 public void testLoadARFFEmptyFile() {
 try {
 Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/emptyARFF.arff"));
 assertEquals(0, dataset.numInstances());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testLoadARFFSimpleFile() {
 try {
 Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/simpleARFF.arff"));
 assertEquals(2, dataset.numInstances());
 Instance instance1 = dataset.instance(0);
 Instance instance2 = dataset.instance(1);
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, instance1.toArray(), 0.001);
 assertArrayEquals(new double[]{4.0, 5.0, 6.0}, instance2.toArray(), 0.001);
 assertNull(instance1.classValue());
 assertNull(instance2.classValue());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testLoadARFFWithClassIndex() {
 try {
 Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/simpleARFF.arff"), 1);
 assertEquals(2, dataset.numInstances());
 Instance instance1 = dataset.instance(0);
 Instance instance2 = dataset.instance(1);
 assertArrayEquals(new double[]{1.0, 2.0}, instance1.toArray(new double[2]), 0.001);
 assertArrayEquals(new double[]{3.0, 5.0}, instance2.toArray(new double[2]), 0.001);
 assertEquals("A", instance1.classValue());
 assertEquals("B", instance2.classValue());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testLoadARFFWithMissingClassColumn() {
 try {
 Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/simpleARFFNoClass.arff"));
 assertEquals(2, dataset.numInstances());
 assertTrue(dataset.classNames().length == 0);
 for (Instance instance : dataset) {
 assertArrayEquals(new double[]{1.0, 2.0, 3.0}, instance.toArray(), 0.001);
 assertNull(instance.classValue());
 }
 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testLoadARFFWithMultipleClasses() {
 try {
 Dataset dataset = ARFFHandler.loadARFF(new File("src/test/resources/simpleARFFMultipleClasses.arff"));
 assertEquals(2, dataset.numInstances());
 assertEquals(2, dataset.classNames().length);
 Instance instance1 = dataset.instance(0);
 Instance instance2 = dataset.instance(1);
 assertArrayEquals(new double[]{1.0, 2.0}, instance1.toArray(new double[2]), 0.001);
 assertArrayEquals(new double[]{3.0, 5.0}, instance2.toArray(new double[2]), 0.001);
 assertEquals("A", instance1.classValue());
 assertEquals("B", instance2.classValue());
 } catch (IOException e) {
 e.printStackTrace();
 fail("Exception should not be thrown");
 }
 }

 @Test
 public void testLoadARFFWithInvalidFormat() {
 File invalidFile = new File("src/test/resources/invalidARFF.arff");
 try {
 ARFFHandler.loadARFF(invalidFile);
 fail("IOException should be thrown");
 } catch (IOException e) {
 // expected
 } finally {
 if (invalidFile.exists()) {
 invalidFile.delete();
 }
 }
 }
}