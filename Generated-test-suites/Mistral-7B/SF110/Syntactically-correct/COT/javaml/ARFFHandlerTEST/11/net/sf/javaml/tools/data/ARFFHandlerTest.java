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
	public void testLoadARFFWithValidFile() throws IOException {
		File file = new File("testData.arff"); // Replace with a valid ARFF file
		Dataset dataset = ARFFHandler.loadARFF(file);
		assertNotNull(dataset);
		assertTrue(dataset instanceof DefaultDataset);
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws IOException {
		File file = new File("nonExistentFile.arff");
		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		File file = new File("testData.arff"); // Replace with a valid ARFF file
		Dataset dataset = ARFFHandler.loadARFF(file, 2);
		assertNotNull(dataset);
		assertTrue(dataset instanceof DefaultDataset);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidNumber() throws IOException {
		File file = new File("testData.arff"); // Replace with a valid ARFF file
		ARFFHandler.loadARFF(file, 3); // Replace with an invalid index
	}

	@Test
	public void testLoadARFFWithEmptyLine() throws IOException {
		File file = new File("testDataWithEmptyLine.arff"); // Replace with a valid ARFF file containing an empty line
		Dataset dataset = ARFFHandler.loadARFF(file);
		assertNotNull(dataset);
		assertTrue(dataset.size() > 0);
	}

	@Test
	public void testLoadARFFWithMissingDataHeader() throws IOException {
		File file = new File("testDataWithoutDataHeader.arff"); // Replace with a valid ARFF file without '@data' header
		Dataset dataset = ARFFHandler.loadARFF(file);
		assertNotNull(dataset);
		assertTrue(dataset.size() > 0);
	}

}