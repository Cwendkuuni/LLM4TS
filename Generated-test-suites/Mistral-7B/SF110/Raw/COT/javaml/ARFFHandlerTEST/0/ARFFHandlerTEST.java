package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;
import org.mockito.Mockito;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFFWithValidFile() throws IOException {
		File file = Mockito.mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.isFile()).thenReturn(true);
		when(file.getAbsolutePath()).thenReturn("path/to/valid/file.arff");

		Dataset expectedOutput = new DefaultDataset();
		expectedOutput.add(new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0}, "class1"));
		expectedOutput.add(new DenseInstance(new double[] {5.0, 6.0, 7.0, 8.0}, "class2"));

		Dataset output = ARFFHandler.loadARFF(file);

		assertEquals(expectedOutput, output);
		verify(file, times(1)).exists();
		verify(file, times(1)).isFile();
		verify(file, times(1)).getAbsolutePath();
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws IOException {
		File file = Mockito.mock(File.class);
		when(file.exists()).thenReturn(false);

		try {
			ARFFHandler.loadARFF(file);
			fail("Expected FileNotFoundException to be thrown.");
		} catch (FileNotFoundException e) {
			// Expected
		}

		verify(file, times(1)).exists();
	}

	@Test
	public void testLoadARFFWithNegativeClassIndex() throws IOException {
		File file = new File("path/to/file.arff");
		Dataset output = ARFFHandler.loadARFF(file, -2);

		assertTrue(output.size() > 0);
		for (Object obj : output) {
			Instance instance = (Instance) obj;
			assertNotNull(instance.classValue());
		}
	}

	@Test
	public void testLoadARFFWithValidClassIndex() throws IOException {
		File file = new File("path/to/file.arff");
		Dataset output = ARFFHandler.loadARFF(file, 2);

		assertTrue(output.size() > 0);
		for (int i = 0; i < output.size(); i++) {
			Instance instance = (Instance) output.instance(i);
			assertEquals(instance.classValue(), output.attribute(output.numAttributes() - 1).value(i));
		}
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidData() throws IOException {
		File file = new File("path/to/file.arff");
		String invalidData = "@data\n1,2,invalidValue";

		LineIterator it = Mockito.mock(LineIterator.class);
		when(it.hasNext()).thenReturn(true);
		when(it.next()).thenReturn(invalidData);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			LineIterator itMock = new LineIterator(br);
			when(itMock.next()).thenReturn(it);

			ARFFHandler.loadARFF(file);
			fail("Expected NumberFormatException to be thrown.");
		}

		verify(it, times(1)).next();
	}

	@Test
	public void testLoadARFFWithNoClassAttribute() throws IOException {
		File file = new File("path/to/file.arff");
		String invalidData = "@data\n1,2,3";

		LineIterator it = Mockito.mock(LineIterator.class);
		when(it.hasNext()).thenReturn(true);
		when(it.next()).thenReturn(invalidData);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			LineIterator itMock = new LineIterator(br);
			when(itMock.next()).thenReturn(it);

			try {
				ARFFHandler.loadARFF(file, 0);
				fail("Expected IllegalArgumentException to be thrown.");
			} catch (IllegalArgumentException e) {
				// Expected
			}

			verify(it, times(1)).next();
		}
	}
}