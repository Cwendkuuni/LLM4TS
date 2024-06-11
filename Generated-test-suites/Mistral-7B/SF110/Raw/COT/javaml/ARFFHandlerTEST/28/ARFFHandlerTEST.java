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
	public void testLoadARFFWithValidFile() {
		Dataset expectedResult = createDatasetFromFile("testData/validARFFFile.arff");
		Dataset result = ARFFHandler.loadARFF(new File("testData/validARFFFile.arff"));
		assertEquals(expectedResult, result);
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() {
		ARFFHandler.loadARFF(new File("testData/invalidFile.txt"));
	}

	@Test
	public void testLoadARFFWithClassIndex() {
		Dataset expectedResult = createDatasetFromFile("testData/classIndexARFFFile.arff", 1);
		Dataset result = ARFFHandler.loadARFF(new File("testData/classIndexARFFFile.arff"), 1);
		assertEquals(expectedResult, result);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithNonNumericValue() {
		ARFFHandler.loadARFF(new File("testData/nonNumericValueARFFFile.arff"));
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLoadARFFWithMissingValue() {
		ARFFHandler.loadARFF(new File("testData/missingValueARFFFile.arff"), 1);
	}

	@Test
	public void testLoadARFFWithEmptyFile() {
		Dataset result = ARFFHandler.loadARFF(new File("testData/emptyFile.arff"));
		assertEquals(new DefaultDataset(), result);
	}

	private Dataset createDatasetFromFile(String fileName, int classIndex) {
		Dataset dataset = ARFFHandler.loadARFF(new File(fileName), classIndex);
		return dataset;
	}

	private Dataset createDatasetFromFile(String fileName) {
		return ARFFHandler.loadARFF(new File(fileName));
	}

}