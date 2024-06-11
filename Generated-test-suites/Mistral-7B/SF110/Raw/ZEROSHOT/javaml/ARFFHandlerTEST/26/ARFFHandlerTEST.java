package net.sf.javaml.tools.data;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.rule.TemporaryFolder;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import net.sf.javaml.core.Attribute;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

 private static final String TEST_ARFF_FILE_NAME = "testARFFFile.arff";
 private static File testFile;

 @BeforeClass
 public static void setup() throws IOException {
 testFile = new TemporaryFolder().newFile(TEST_ARFF_FILE_NAME);
 FileWriter writer = new FileWriter(testFile);
 writer.write("@relation testRelation\n" +
 "@attribute attribute1 numeric\n" +
 "@attribute attribute2 nominal\n" +
 "@attribute attribute3 numeric\n" +
 "@attribute class label\n" +
 "@data\n" +
 "1.0, red, 2.5, A\n" +
 "3.0, blue, NaN, B\n" +
 "4.0, red, 5.0, A\n" +
 "NaN, green, 3.5, C\n" +
 "0.5, blue, 6.0, B\n");
 writer.close();
 }

 @Test
 public void testLoadARFF() throws Exception {
 Dataset ds = ARFFHandler.loadARFF(testFile);
 assertEquals(5, ds.numInstances());
 assertEquals(4, ds.numAttributes());
 List<Attribute> attributes = ds.attributeNames();
 assertEquals(3, attributes.size() - 1);
 Attribute attribute1 = attributes.get(0);
 assertEquals("attribute1", attribute1.name());
 Attribute attribute3 = attributes.get(2);
 assertEquals("attribute3", attribute3.name());
 Attribute classAttribute = attributes.get(3);
 assertEquals("class", classAttribute.name());
 Instance instance1 = ds.instance(0);
 assertEquals(3.0, instance1.value(1), 0.0);
 assertEquals("blue", instance1.value(2));
 assertEquals(2.5, instance1.value(3), 0.0);
 assertEquals("B", instance1.value(ds.classIndex()));
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws Exception {
 ARFFHandler.loadARFF(new File("nonexistentFile.arff"));
 fail("FileNotFoundException was expected but not thrown.");
 }

 @Test
 public void testLoadARFFWithInvalidClassIndex() throws Exception {
 try {
 Dataset ds = ARFFHandler.loadARFF(testFile, 5);
 fail("An IllegalArgumentException was expected but not thrown.");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testLoadARFFWithClassIndex() throws Exception {
 Dataset ds = ARFFHandler.loadARFF(testFile, 2);
 assertEquals(5, ds.numInstances());
 assertEquals(4, ds.numAttributes());
 List<Attribute> attributes = ds.attributeNames();
 assertEquals(3, attributes.size() - 1);
 Attribute attribute1 = attributes.get(0);
 assertEquals("attribute1", attribute1.name());
 Attribute attribute3 = attributes.get(2);
 assertEquals("attribute3", attribute3.name());
 Attribute classAttribute = attributes.get(3);
 assertEquals("class", classAttribute.name());
 for (int i = 0; i < ds.numInstances(); i++) {
 Instance instance = ds.instance(i);
 if (instance.value(ds.classIndex()) instanceof String) {
 assertEquals("B", instance.value(ds.classIndex()));
 } else {
 assertEquals(i % 2 == 0 ? 2 : 1, (int)instance.value(ds.classIndex()));
 }
 }
 }
}