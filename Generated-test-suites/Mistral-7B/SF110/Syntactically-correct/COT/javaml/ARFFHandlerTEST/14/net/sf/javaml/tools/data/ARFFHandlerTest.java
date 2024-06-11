package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Dataset;

import org.junit.Test;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() {
		File file = new File("testData/validARFFFile.arff");
		Dataset expectedResult = new DefaultDataset();
		// Add instances to expectedResult for comparison
		// ...
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertEquals("Size of datasets should be equal", expectedResult.size(), actualResult.size());
		// Compare specific instances or their attributes values if necessary
		// ...
		assertTrue("Datasets are equal", expectedResult.equals(actualResult));
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() {
		File file = new File("testData/invalidFile.txt"); // Replace with an invalid file name
		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithNoClassIndex() {
		File file = new File("testData/ARFFFileWithNoClass.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertTrue("Dataset should have instances", actualResult.size() > 0);
	}

	@Test
	public void testLoadARFFWithClassIndex() {
		File file = new File("testData/ARFFFileWithClass.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file, 1); // Replace with the correct class index
		assertTrue("Dataset should have instances", actualResult.size() > 0);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithNaNValues() {
		File file = new File("testData/ARFFFileWithNaNValue.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertTrue("Dataset should have instances", actualResult.size() > 0);

		// Check for at least one instance with a NaN value
		for (Object instance : actualResult) {
			DenseInstance denseInstance = (DenseInstance) instance;
			for (Object attribute : denseInstance.attributeNames()) {
				if (attribute.toString().equals("class")) {
					continue;
				}
				Double value = (Double) denseInstance.value(attribute);
				assertEquals("Value should be NaN", Double.NaN, value);
			}
		}
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLoadARFFWithNegativeClassIndex() {
		File file = new File("testData/ARFFFile.arff");
		ARFFHandler.loadARFF(file, -1);
	}

	@Test
	public void testLoadARFFWithEmptyLine() {
		File file = new File("testData/ARFFFileWithEmptyLine.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertTrue("Dataset should have instances", actualResult.size() > 0);
	}

	@Test
	public void testLoadARFFWithCommentLine() {
		File file = new File("testData/ARFFFileWithCommentLine.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertTrue("Dataset should have instances", actualResult.size() > 0);
	}

	@Test
	public void testLoadARFFWithMissingDataLine() {
		File file = new File("testData/ARFFFileWithMissingDataLine.arff");
		Dataset actualResult = ARFFHandler.loadARFF(file);
		assertTrue("Dataset should have instances", actualResult.size() > 0);
	}
}