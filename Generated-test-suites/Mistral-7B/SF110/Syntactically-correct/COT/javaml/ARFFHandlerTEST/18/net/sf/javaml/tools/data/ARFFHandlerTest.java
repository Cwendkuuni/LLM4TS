package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

/**
 * Test class for ARFFHandler.
 */
public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() {
		File file = new File("path/to/your/valid/arff/file.arff");
		Dataset expected = new DefaultDataset();
		// Add instances and expected values to the Dataset 'expected' for verification
		Dataset actual = ARFFHandler.loadARFF(file);
		assertEquals("Size of datasets should be equal", expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance instanceExpected = expected.instance(i);
			Instance instanceActual = actual.instance(i);
			assertArrayEquals("Values of instances should be equal", instanceExpected.toArray(), instanceActual.toArray());
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() {
		File file = new File("path/to/your/invalid/file.txt"); // Replace with an invalid file path
		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithClassIndex() {
		File file = new File("path/to/your/arff/file.arff");
		int classIndex = 2; // Assuming class attribute index is 2
		Dataset expected = new DefaultDataset();
		// Add instances and expected values to the Dataset 'expected' for verification
		Dataset actual = ARFFHandler.loadARFF(file, classIndex);
		assertEquals("Size of datasets should be equal", expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance instanceExpected = expected.instance(i);
			Instance instanceActual = actual.instance(i);
			// Adjust the array indices in assertArrayEquals for class attribute adjustment
			assertArrayEquals("Values of instances (excluding class attribute) should be equal", Arrays.copyOfRange(instanceExpected.toArray(), 0, instanceExpected.toArray().length - 1), Arrays.copyOfRange(instanceActual.toArray(), 0, instanceActual.toArray().length - 1));
			assertEquals("Class attribute of instances should be equal", instanceExpected.classValue(), instanceActual.classValue());
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidNumericValue() {
		File file = new File("path/to/your/arff/file.arff");
		Dataset dataset = ARFFHandler.loadARFF(file);
		// Ensure the dataset has at least one instance
		Instance instance = dataset.instance(0);
		// Assuming the third value (index 2) is non-numeric
		String invalidNumericValue = "invalid";
		instance.setValue(2, invalidNumericValue);
		ARFFHandler.loadARFF(file); // The line above should trigger NumberFormatException
	}
}