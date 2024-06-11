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
		Dataset expected = new DefaultDataset();
		expected.add(new DenseInstance(new double[] {1.0, 2.0, "A"}, "ClassLabel1"));
		expected.add(new DenseInstance(new double[] {3.0, 4.0, "ClassLabel2"}, "ClassLabel2"));

		Dataset actual = ARFFHandler.loadARFF(new File("testData/validARFFFile.arff"));
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance expectedInstance = expected.instance(i);
			Instance actualInstance = actual.instance(i);
			assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
		}
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		Dataset expected = new DefaultDataset();
		expected.add(new DenseInstance(new double[] {1.0, 2.0}, "ClassLabel1"));
		expected.add(new DenseInstance(new double[] {3.0, 4.0}, "ClassLabel2"));

		Dataset actual = ARFFHandler.loadARFF(new File("testData/validARFFFile.arff"), 1);
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance expectedInstance = expected.instance(i);
			Instance actualInstance = actual.instance(i);
			assertArrayEquals(expectedInstance.toArray(new double[1]), actualInstance.toArray(new double[1]), 0.001);
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
		ARFFHandler.loadARFF(new File("testData/invalidFile.arff"));
	}

	@Test
	public void testLoadARFFWithEmptyFile() throws IOException {
		Dataset actual = ARFFHandler.loadARFF(new File("testData/emptyFile.arff"));
		assertEquals(0, actual.size());
	}

	@Test
	public void testLoadARFFWithMissingDataModeLine() throws IOException {
		Dataset expected = new DefaultDataset();
		expected.add(new DenseInstance(new double[] {1.0, 2.0, "ClassLabel1"}, "ClassLabel1"));

		Dataset actual = ARFFHandler.loadARFF(new File("testData/missingDataModeLineFile.arff"));
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance expectedInstance = expected.instance(i);
			Instance actualInstance = actual.instance(i);
			assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidNumberInDataLine() throws IOException {
		ARFFHandler.loadARFF(new File("testData/invalidNumberInDataLineFile.arff"));
	}

	@Test
	public void testLoadARFFWithInvalidClassValueInDataLine() throws IOException {
		Dataset expected = new DefaultDataset();
		expected.add(new DenseInstance(new double[] {1.0, 2.0}, "ClassLabel1"));
		expected.add(new DenseInstance(new double[] {3.0, 4.0}, "ClassLabel2"));
		expected.add(new DenseInstance(new double[] {5.0, 6.0}, null));

		Dataset actual = ARFFHandler.loadARFF(new File("testData/invalidClassValueInDataLineFile.arff"));
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Instance expectedInstance = expected.instance(i);
			Instance actualInstance = actual.instance(i);
			assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
		}
	}

	@Test
	public void testLoadARFFWithInvalidCommentIdentifier() throws IOException {
		ARFFHandler handler = new ARFFHandler();
		handler.setCommentIdentifier("#");
		try {
			Dataset actual = handler.loadARFF(new File("testData/validARFFFile.arff"));
			fail();
		} catch (FileNotFoundException e) {
			// Expected
		}
		handler.setCommentIdentifier("%");
	}
}