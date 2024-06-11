package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() throws IOException {
		Dataset expectedDataset = new DefaultDataset();
		// Add some instances to expectedDataset for verification purpose
		// ...
		Dataset actualDataset = ARFFHandler.loadARFF(new File("testData.arff"));
		assertEquals("Size of expected and actual dataset should be the same", expectedDataset.numInstances(), actualDataset.numInstances());
		for (int i = 0; i < expectedDataset.numInstances(); i++) {
			Instance expectedInstance = expectedDataset.instance(i);
			Instance actualInstance = actualDataset.instance(i);
			assertEquals("Instance " + i + " should be equal", expectedInstance, actualInstance);
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws IOException {
		ARFFHandler.loadARFF(new File("nonExistentFile.arff"));
	}

	@Test
	public void testLoadARFFWithClassIndexGreaterThanData() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testData.arff"), 10);
		assertEquals("Size of dataset with classIndex greater than data should be the same", dataset.numInstances(), ARFFHandler.loadARFF(new File("testData.arff")).numInstances());
	}

	@Test
	public void testLoadARFFWithClassIndexLessThanData() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testData.arff"), 0);
		assertEquals("Size of dataset with classIndex less than data should be the same", dataset.numInstances(), ARFFHandler.loadARFF(new File("testData.arff")).numInstances());
	}

	@Test
	public void testLoadARFFWithInvalidDataLine() throws IOException {
		File fileWithInvalidDataLine = new File("testDataWithInvalidDataLine.arff");
		try {
			ARFFHandler.loadARFF(fileWithInvalidDataLine);
			fail("Exception should be thrown when loading file with invalid data line.");
		} catch (FileNotFoundException e) {
			// FileNotFoundException is expected when testing invalid file
		} catch (Exception ex) {
			// Other exception than expected, test failed
			fail("Unexpected exception: " + ex.getMessage());
		}
	}

	@Test
	public void testLoadARFFWithEmptyFile() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testDataEmpty.arff"));
		assertEquals("Size of dataset with empty file should be zero", 0, dataset.numInstances());
	}

	@Test
	public void testLoadARFFWithCommentLine() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("testDataWithCommentLine.arff"));
		assertEquals("Size of dataset with comment line should be the same as without comment line", ARFFHandler.loadARFF(new File("testDataWithoutCommentLine.arff")).numInstances(), dataset.numInstances());
	}
}