package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import be.abeel.util.LineIterator;
import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 // given
 File file = new File("testData.arff");
 Dataset expected = new DefaultDataset();
 expected.add(new DenseInstance(new double[]{1.0, 2.1, 3.0}, "class1"));
 expected.add(new DenseInstance(new double[]{4.0, 5.1, 6.0}, "class2"));

 // when
 Dataset actual = ARFFHandler.loadARFF(file);

 // then
 assertEquals("Size of the datasets should be equal", expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 Attribute attribute1 = expected.instance(i).attribute(0);
 Attribute attribute2 = expected.instance(i).attribute(1);
 Attribute attribute3 = expected.instance(i).attribute(2);
 Attribute actualAttribute1 = actual.instance(i).attribute(0);
 Attribute actualAttribute2 = actual.instance(i).attribute(1);
 Attribute actualAttribute3 = actual.instance(i).attribute(2);
 assertEquals("Attributes names should be equal", attribute1.name(), actualAttribute1.name());
 assertEquals("Attributes types should be equal", attribute1.type(), actualAttribute1.type());
 assertEquals("Values should be equal", attribute1.value(i), actualAttribute1.value(i), 0.001);
 assertEquals("Attributes names should be equal", attribute2.name(), actualAttribute2.name());
 assertEquals("Attributes types should be equal", attribute2.type(), actualAttribute2.type());
 assertEquals("Values should be equal", attribute2.value(i), actualAttribute2.value(i), 0.001);
 assertEquals("Attributes names should be equal", attribute3.name(), actualAttribute3.name());
 assertEquals("Attributes types should be equal", attribute3.type(), actualAttribute3.type());
 assertEquals("Values should be equal", attribute3.value(i), actualAttribute3.value(i), 0.001);
 }
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws IOException {
 // given
 File file = new File("nonExistentFile.arff");

 // when & then
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 // given
 File file = new File("testData.arff");
 Dataset expected = new DefaultDataset();
 expected.add(new DenseInstance(new double[]{1.0, 2.1}, "class1"));
 expected.add(new DenseInstance(new double[]{4.0, 5.1}, "class2"));

 // when
 Dataset actual = ARFFHandler.loadARFF(file, 1);

 // then
 assertEquals("Size of the datasets should be equal", expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 Attribute attribute1 = expected.instance(i).attribute(0);
 Attribute attribute2 = expected.instance(i).attribute(1);
 Attribute actualAttribute1 = actual.instance(i).attribute(0);
 Attribute actualAttribute2 = actual.instance(i).attribute(1);
 assertEquals("Attributes names should be equal", attribute1.name(), actualAttribute1.name());
 assertEquals("Attributes types should be equal", attribute1.type(), actualAttribute1.type());
 assertEquals("Values should be equal", attribute1.value(i), actualAttribute1.value(i), 0.001);
 assertEquals("Attribute 1 index should be different in both datasets", 0, actualAttribute1.index());
 assertEquals("Attribute 2 names should be equal", attribute2.name(), actualAttribute2.name());
 assertEquals("Attributes types should be equal", attribute2.type(), actualAttribute2.type());
 assertEquals("Values should be equal", attribute2.value(i), actualAttribute2.value(i), 0.001);
 assertEquals("Attribute 2 index should be the same in both datasets", attribute2.index(), actualAttribute2.index());
 }
 }
}