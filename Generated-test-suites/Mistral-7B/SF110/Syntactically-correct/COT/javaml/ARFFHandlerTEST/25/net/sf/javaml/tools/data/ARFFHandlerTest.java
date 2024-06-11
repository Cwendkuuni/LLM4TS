package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() throws IOException {
		File file = new File("testData/validARFFFile.arff");
		Dataset expectedData = new DefaultDataset();
		// Add instances with expected data here for verification
		Dataset actualData = ARFFHandler.loadARFF(file);
		assertEquals("Size of expected and actual datasets should be equal", expectedData.numInstances(), actualData.numInstances());
		for (int i = 0; i < expectedData.numInstances(); i++) {
			Instance expectedInstance = expectedData.instance(i);
			Instance actualInstance = actualData.instance(i);
			assertEquals("Instance " + i + " values should be equal", expectedInstance.toString(), actualInstance.toString());
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
		File file = new File("testData/invalidFile.txt"); // Replace with an invalid file name
		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		File file = new File("testData/classIndexARFFFile.arff");
		Dataset actualData = ARFFHandler.loadARFF(file, 2);
		// Add expected data verification code here
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidNumberFormat() throws IOException, NumberFormatException {
		File file = new File("testData/numberFormatInvalidARFFFile.arff");
		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithEmptyFile() throws IOException {
		File file = new File("testData/emptyARFFFile.arff");
		Dataset actualData = ARFFHandler.loadARFF(file);
		assertEquals("Size of expected and actual datasets should be equal", 0, actualData.numInstances());
	}

	@Test
	public void testLoadARFFWithMissingDataModeLine() throws IOException {
		File file = new File("testData/missingDataModeLineARFFFile.arff");
		try {
			ARFFHandler.loadARFF(file);
			fail("Expected FileNotFoundException but no exception was thrown");
		} catch (FileNotFoundException e) {
			// Expected
		}
	}
}