package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import be.abeel.util.*;
import net.sf.javaml.core.*;
import org.junit.Test;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFNegativeFile() {
 assertNull(ARFFHandler.loadARFF(null));
 }

 @Test
 public void testLoadARFFEmptyFile() {
 File emptyFile = new File("src/test/resources/emptyARFF.arff");
 assertNotNull(ARFFHandler.loadARFF(emptyFile));
 }

 @Test
 public void testLoadARFFWithoutDataMode() {
 File testFile = new File("src/test/resources/testARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(testFile);
 assertNotNull(dataset);
 assertFalse(dataset.size() > 0);
 }

 @Test
 public void testLoadARFFWithDataMode() {
 File testFile = new File("src/test/resources/testARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(testFile);
 assertNotNull(dataset);
assertEquals(12, dataset.size());
}

 @Test
 public void testLoadARFFWithClassIndex() {
 File testFile = new File("src/test/resources/testARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(testFile, 1);
 assertNotNull(dataset);
 assertEquals(11, dataset.size());
}

 @Test
 public void testLoadARFFWithEmptyClassValue() {
 File testFile = new File("src/test/resources/emptyClassValueARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(testFile);
 assertNotNull(dataset);
 assertEquals(5, dataset.size());
}

 @Test
 public void testLoadARFFWithMixedDataTypes() {
 File testFile = new File("src/test/resources/mixedDataTypesARFF.arff");
 Dataset dataset = ARFFHandler.loadARFF(testFile);
 assertNotNull(dataset);
 assertEquals(5, dataset.size());
 Instance instance = dataset.instance(0);
 assertNotNull(instance);
 assertEquals("Alice", instance.value(0));
 assertEquals(27.5, instance.value(1), 0.001);
 assertEquals("Bob", instance.value(2));
 assertEquals(33.0, instance.value(3));
 assertEquals("Carl", instance.value(4));
 assertEquals("X", instance.value(5));
}
}