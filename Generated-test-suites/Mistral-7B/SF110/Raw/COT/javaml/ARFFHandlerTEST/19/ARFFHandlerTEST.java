package net.sf.javaml.tools.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import be.abeel.util.LineIterator;

import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.tools.data.ARFFHandler;

import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class ARFFHandlerTest {

	@Test
	public void testLoadARFF() throws IOException {
		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.isFile()).thenReturn(true);
		when(file.getAbsolutePath()).thenReturn("path/to/file.arff");

		Dataset dataset = ARFFHandler.loadARFF(file);
		verify(file, times(1)).exists();
		verify(file, times(1)).isFile();
		verify(file, times(1)).getAbsolutePath();

		assertNotNull(dataset);
		assertEquals(10, dataset.size()); // assume there are 10 instances in the file
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
		File file = mock(File.class);
		when(file.exists()).thenReturn(false);

		ARFFHandler.loadARFF(file);
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		File file = mock(File.class);
		when(file.exists()).thenReturn(true);
		when(file.isFile()).thenReturn(true);
		when(file.getAbsolutePath()).thenReturn("path/to/file.arff");

		Dataset dataset = ARFFHandler.loadARFF(file, 2);
		verify(file, times(1)).exists();
		verify(file, times(1)).isFile();
		verify(file, times(1)).getAbsolutePath();

		assertNotNull(dataset);
		assertEquals(9, dataset.size()); // assume there are 10 instances in the file, but skip the last one (class index = 2)
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidDoubleValue() throws IOException {
		File file = new File("path/to/file.arff");
		LineIterator it = mock(LineIterator.class);
		when(it.hasNext()).thenReturn(true);
		when(it.next()).thenReturn("@data\n1,Name,Age,Weight,Class\n1,John,30,170.5,apple");

		ARFFHandler.loadARFF(file);

		verify(it, times(1)).hasNext();
		verify(it, times(1)).next();

		verifyThrowable(NumberFormatException.class, () -> ARFFHandler.loadARFF(file, 0));
	}
}