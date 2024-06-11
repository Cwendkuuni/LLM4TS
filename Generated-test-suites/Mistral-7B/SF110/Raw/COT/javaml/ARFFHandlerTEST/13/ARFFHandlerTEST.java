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

	// Test method 1: Test loading ARFF file with valid data
	@Test
	public void testLoadARFFValidData() {
		File file = new File("path/to/valid/arff/file.arff");
		Dataset expectedDataset = new DefaultDataset();
		// Add instances to expectedDataset for verification
		expectedDataset.add(new DenseInstance(new double[] {1.0, 2.0, 3.0, "class1"}));
		expectedDataset.add(new DenseInstance(new double[] {4.0, 5.0, 6.0, "class2"}));
		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals(expectedDataset, actualDataset);
	}

	// Test method 2: Test loading ARFF file with classIndex set to a valid index
	@Test
	public void testLoadARFFValidClassIndex() {
		File file = new File("path/to/arff/file.arff");
		Dataset expectedDataset = new DefaultDataset();
		// Add instances to expectedDataset for verification
		expectedDataset.add(new DenseInstance(new double[] {1.0, 2.0, 3.0, "class1"}, new String[] {"Label"}));
		expectedDataset.add(new DenseInstance(new double[] {4.0, 5.0, 6.0, "class2"}, new String[] {"Label"}));
		Dataset actualDataset = ARFFHandler.loadARFF(file, 3);
		assertEquals(expectedDataset, actualDataset);
	}

	// Test method 3: Test loading ARFF file with FileNotFoundException
	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFFileNotFoundException() {
		File file = new File("path/to/non-existent/file.arff");
		ARFFHandler.loadARFF(file);
	}

	// Test method 4: Test loading ARFF file with null File argument
	@Test(expected = NullPointerException.class)
	public void testLoadARFFNullFileArgument() {
		ARFFHandler.loadARFF(null);
	}

	// Test method 5: Test loading ARFF file with invalid classIndex
	@Test(expected = IllegalArgumentException.class)
	public void testLoadARFFInvalidClassIndex() {
		File file = new File("path/to/arff/file.arff");
		ARFFHandler.loadARFF(file, -1);
	}

	// Test method 6: Test loading ARFF file with incorrect format
	@Test(expected = IllegalArgumentException.class)
	public void testLoadARFFIncorrectFormat() {
		File file = new File("path/to/invalid/format/file.arff");
		ARFFHandler.loadARFF(file);
	}
}