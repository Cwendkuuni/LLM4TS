package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFFile() throws IOException {
		Dataset expectedDataset = createSampleDataset();
		Dataset actualDataset = ARFFHandler.loadARFF(new File("input/sample.arff"));
		assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
		assertEquals(expectedDataset.numAttributes(), actualDataset.numAttributes());
		for (int i = 0; i < expectedDataset.numInstances(); i++) {
			Instance expectedInstance = expectedDataset.instance(i);
			Instance actualInstance = actualDataset.instance(i);
			assertEquals(expectedInstance.classValue(), actualInstance.classValue());
			assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
		}
	}

	@Test
	public void testLoadARFFFileWithClassIndex() throws IOException {
		Dataset expectedDataset = createSampleDataset();
		Dataset actualDataset = ARFFHandler.loadARFF(new File("input/sample.arff"), 2);
		assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
		assertEquals(expectedDataset.numAttributes(), actualDataset.numAttributes() - 1);
		for (int i = 0; i < expectedDataset.numInstances(); i++) {
			Instance expectedInstance = expectedDataset.instance(i);
			Instance actualInstance = actualDataset.instance(i);
			assertArrayEquals(expectedInstance.toArray(new double[expectedInstance.numValues()-1]), actualInstance.toArray(new double[actualInstance.numValues()-1]), 0.001);
			assertEquals(expectedInstance.classValue(), actualInstance.classValue());
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFFileWithInvalidFile() throws IOException {
		ARFFHandler.loadARFF(new File("input/invalidFile.txt"));
	}

	@Test
	public void testLoadARFFFileWithEmptyLines() throws IOException {
		Dataset expectedDataset = new DefaultDataset();
		Dataset actualDataset = ARFFHandler.loadARFF(new File("input/emptyLines.arff"));
		assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
		assertEquals(expectedDataset.numAttributes(), actualDataset.numAttributes());
		assertTrue(actualDataset.isEmpty());
	}

	@Test
	public void testLoadARFFFileWithMissingDataModeLine() throws IOException {
		Dataset expectedDataset = new DefaultDataset();
		Dataset actualDataset = ARFFHandler.loadARFF(new File("input/missingDataModeLine.arff"));
		assertEquals(expectedDataset.numInstances(), actualDataset.numInstances());
		assertEquals(expectedDataset.numAttributes(), actualDataset.numAttributes());
		assertTrue(actualDataset.isEmpty());
	}

	@Test
	public void testLoadARFFFileWithInvalidClassIndex() throws IOException {
		Dataset expectedDataset = createSampleDataset();
		try {
			ARFFHandler.loadARFF(new File("input/sample.arff"), expectedDataset.numAttributes());
			fail("Expected FileNotFoundException, but no exception thrown.");
		} catch (FileNotFoundException e) {
			// expected
		}
	}

	@Test
	public void testLoadARFFFileWithNullFile() {
		try {
			ARFFHandler.loadARFF(null);
			fail("Expected IllegalArgumentException, but no exception thrown.");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	private Dataset createSampleDataset() {
		double[][] data = {
			{1.0, 2.0, "A"},
			{1.5, 2.5, "B"},
			{2.0, 3.0, "C"},
			{2.5, 3.5, "A"}
		};
		Attribute attribute0 = new Attribute("A0");
		Attribute attribute1 = new Attribute("A1", DoubleType.INSTANCE);
		Instance instance1 = new DenseInstance(data[0]);
		Instance instance2 = new DenseInstance(data[1]);
		Instance instance3 = new DenseInstance(data[2]);
		Instance instance4 = new DenseInstance(data[3]);
		return new DefaultDataset(new Attribute[] {attribute0, attribute1}, new Instance[] {instance1, instance2, instance3, instance4});
	}
}