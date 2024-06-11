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
	public void testLoadARFF() throws IOException {
		File file = new File("testData.arff"); // assuming the presence of a valid ARFF file
		Dataset expectedDataset = new DefaultDataset();
		// Add instances to the expectedDataset for validation
		expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1, 3.0}, "Class1"));
		expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, 6.0}, "Class2"));

		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals("Size of loaded dataset should be equal to the expected", expectedDataset.size(), actualDataset.size());
		assertEquals("Loaded dataset should be equal to the expected", expectedDataset, actualDataset);
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() {
		File invalidFile = new File("nonExistentFile.arff");
		ARFFHandler.loadARFF(invalidFile);
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		File file = new File("testDataWithClassIndex.arff"); // assuming the presence of a valid ARFF file with class index
		Dataset expectedDataset = new DefaultDataset();
		// Add instances to the expectedDataset for validation
		expectedDataset.add(new DenseInstance(new double[]{1.0, 2.1, 3.0}, "Class1"));
		expectedDataset.add(new DenseInstance(new double[]{4.0, 5.1, 6.0}, "Class2"));

		Dataset actualDataset = ARFFHandler.loadARFF(file, 1);
		assertEquals("Size of loaded dataset should be equal to the expected", expectedDataset.size(), actualDataset.size());
		assertEquals("Loaded dataset without class column should have one less instance than the expected", expectedDataset.size() - 1, actualDataset.size());
		for (Object instance : actualDataset) {
			DenseInstance denseInstance = (DenseInstance) instance;
			assertNotNull("Loaded dataset should not have null class values", denseInstance.classValue());
		}
		assertEquals("Loaded dataset should be equal to the expected without class column", expectedDataset.instance(0), actualDataset.instance(0));
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithNaNValue() throws IOException {
		File file = new File("testDataWithNaN.arff"); // assuming the presence of a valid ARFF file with NaN values
		Dataset dataset = ARFFHandler.loadARFF(file);
		Instance instance = dataset.instance(0);
		assertNotNull(instance);
		assertNotNull(instance.classValue());
		double[] values = ((DenseInstance)instance).denseData();
		assertEquals(Double.NaN, values[0], 0.0);
		assertEquals(Double.NaN, values[1], 0.0);
		assertEquals(Double.NaN, values[2], 0.0);
	}
}