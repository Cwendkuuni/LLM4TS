package net.sf.javaml.tools.data.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/valid/arff/file.arff"));
		assertNotNull(dataset);
		assertFalse(dataset.isEmpty());
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/non-existent/file.arff"));
		fail("Expected FileNotFoundException");
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file.arff"), 2);
		assertNotNull(dataset);
		assertFalse(dataset.isEmpty());
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidNumberFormat() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file_with_invalid_number.arff"), 1);
		fail("Expected NumberFormatException");
	}

	@Test
	public void testLoadARFFWithEmptyFile() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/empty/arff/file.arff"));
		assertNotNull(dataset);
		assertTrue(dataset.isEmpty());
	}

	@Test
	public void testLoadARFFWithOnlyHeader() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file_with_only_header.arff"));
		assertNotNull(dataset);
		assertTrue(dataset.isEmpty());
	}

	@Test
	public void testLoadARFFWithMissingDataModeLine() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file_missing_datamode.arff"));
		assertNotNull(dataset);
		assertFalse(dataset.isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLoadARFFWithNegativeClassIndex() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file.arff"), -1);
		fail("Expected IllegalArgumentException");
	}

	@Test
	public void testLoadARFFWithClassIndexGreaterThanDataSize() throws IOException {
		Dataset dataset = ARFFHandler.loadARFF(new File("path/to/arff/file.arff"), 10);
		assertNotNull(dataset);
		assertFalse(dataset.isEmpty());
		Instance instance = dataset.instance(0);
		assertEquals(instance.classValue().isMissing(), dataset.classAttribute().isNominal());
	}

}