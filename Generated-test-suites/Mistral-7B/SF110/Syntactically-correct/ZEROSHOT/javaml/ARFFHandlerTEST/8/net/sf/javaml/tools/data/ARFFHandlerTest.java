package net.sf.javaml.tools.data;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import be.abeel.util.LineIterator;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFFWithValidFile() throws IOException {
 File file = new File("testData/testARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 Assert.assertNotNull(dataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() {
 File file = new File("testData/nonExistentFile.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndexValid() throws IOException {
 File file = new File("testData/testARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 Instance instance = dataset.instance(0);
 String expectedClassValue = "Class2";
 String actualClassValue = instance.classValue().toString();
 Assert.assertEquals(expectedClassValue, actualClassValue);
 }

 @Test
 public void testLoadARFFWithClassIndexInvalid() throws IOException {
 File file = new File("testData/testARFFFile.arff");
 Dataset dataset = ARFFHandler.loadARFF(file, -1);
 Instance instance = dataset.instance(0);
 String expectedClassValue = "Class1"; // Assuming the first attribute is the class attribute
 String actualClassValue = instance.classValue().toString();
 Assert.assertEquals(expectedClassValue, actualClassValue);
 }

 @Test
 public void testLoadingDataMode() throws IOException {
 File file = new File("testData/testARFFFile.arff");
 LineIterator it = new LineIterator(file);
 it.setSkipBlanks(true);
 it.setCommentIdentifier("%");
 it.setSkipComments(true);
 boolean dataMode = false;
 String line;
 while ((line = it.nextLine()) != null) {
 if (dataMode) {
 String[] arr = line.split(",");
 int length = arr.length;
 for (int i = 0; i < length; i++) {
 double val;
 try {
 val = Double.parseDouble(arr[i]);
 } catch (NumberFormatException e) {
 val = Double.NaN;
 }
 // Assuming the first attribute is the class attribute, so we skip the first value
 if (i > 0) {
 Instance instance = new DenseInstance(new double[]{val}, null);
 // Add some checks on instance if needed
 }
 }
 }
 if (line.equalsIgnoreCase("@data")) {
 dataMode = true;
 }
 }
 it.close();
 }
}