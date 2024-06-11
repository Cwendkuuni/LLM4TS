package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

 @Test
 public void testLoadARFF() throws IOException {
 File file = new File("testData.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertNotNull(dataset);
 }

 @Test(expected = FileNotFoundException.class)
 public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
 File file = new File("nonExistentFile.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithClassIndex() throws IOException {
 File file = new File("testData.arff");
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 assertNotNull(dataset);
 }

 @Test(expected = NumberFormatException.class)
 public void testLoadARFFWithInvalidDataType() throws IOException {
 File file = new File("testDataWithInvalidType.arff");
 ARFFHandler.loadARFF(file);
 }

 @Test
 public void testLoadARFFWithDataMode() throws IOException {
 File file = new File("testData.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertTrue(dataset.size() > 0);
 }

 @Test
 public void testLoadARFFWithDataModeAndClassIndex() throws IOException {
 File file = new File("testData.arff");
 Dataset dataset = ARFFHandler.loadARFF(file, 2);
 assertTrue(dataset.size() > 0);
 }

 @Test
 public void testLoadARFFWithEmptyData() throws IOException {
 File file = new File("testDataEmpty.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertTrue(dataset.size() == 0);
 }

 @Test
 public void testLoadARFFWithMixedDataTypes() throws IOException {
 File file = new File("testDataMixedTypes.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertTrue(dataset.size() > 0);
 }

 @Test
 public void testLoadARFFWithStringClassValue() throws IOException {
 File file = new File("testDataStringClassValue.arff");
 Dataset dataset = ARFFHandler.loadARFF(file);
 assertTrue(dataset.size() > 0);
 }
}