package net.sf.javaml.tools.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFF() throws IOException {
		// Test loading ARFF file with no specified class index
		Dataset dataset = ARFFHandler.loadARFF(new File("testData.arff"));
		assertEquals(10, dataset.numInstances());
		assertEquals(5, dataset.numAttributes());

		// Test loading ARFF file with specified class index
		Dataset datasetWithClassIndex = ARFFHandler.loadARFF(new File("testData.arff"), 4);
		assertEquals(10, datasetWithClassIndex.numInstances());
		assertEquals(5, datasetWithClassIndex.numAttributes());
		assertEquals("classLabel", datasetWithClassIndex.attribute(4).name());
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFFileNotFound() throws FileNotFoundException {
		ARFFHandler.loadARFF(new File("nonexistentfile.arff"));
	}

	@Test
	public void testLoadARFFInvalidData() {
		assertTrue(ARFFHandler.loadARFF(new File("testDataInvalid.arff")) instanceof EmptyDataset);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFInvalidNumberFormat() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testDataInvalidNumberFormat.arff"));
		fail("Expected NumberFormatException but test passed");
	}

	@Test
	public void testLoadARFFWithMissingData() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testDataMissingData.arff"));
		assertEquals(10, dataset.numInstances());
		assertEquals(5, dataset.numAttributes());
		Instance instance = dataset.instance(0);
		assertEquals(3.0, instance.value(0), 0.0);
		assertEquals("", instance.value(1));
		assertEquals(Double.NaN, instance.value(2));
		assertEquals("classLabel1", instance.value(3));
		assertEquals(Double.NaN, instance.value(4));
	}

	@Test
	public void testLoadARFFWithEmptyValue() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testDataEmptyValue.arff"));
		assertEquals(10, dataset.numInstances());
		assertEquals(5, dataset.numAttributes());
		Instance instance = dataset.instance(0);
		assertEquals(3.0, instance.value(0), 0.0);
		assertEquals("", instance.value(1));
		assertEquals(Double.NaN, instance.value(2));
		assertEquals("classLabel1", instance.value(3));
		assertEquals("", instance.value(4));
	}
}