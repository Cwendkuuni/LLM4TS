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
		File file = new File("path/to/your/arff/file.arff"); // Replace with the path to your ARFF file
		Dataset expectedDataset = new DefaultDataset(); // Replace with the expected dataset for your ARFF file
		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals(expectedDataset, actualDataset);
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithNotFoundException() throws IOException {
		File file = new File("path/to/non-existing/file.arff"); // Replace with the path to a non-existing file
		ARFFHandler.loadARFF(file);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithNumberFormatException() throws IOException {
		File file = new File("path/to/your/arff/file.arff"); // Replace with the path to your ARFF file
		String malformedLine = "@data,A,B,C,D,E,F,G,H,I,J,K,L,M,N%@data\n1.0,A,B,C,D,E,F,G,H,I,J,K,L,M,N\n2.0,A,B,C,D,E,F,G,H,I,J,K,L,M,N%@data\n3.0,A,B,C,D,E,F,G,H,I,J,K,L,M,N@data"; // Replace with a malformed ARFF file
		LineIterator it = new LineIterator(new StringReader(malformedLine));
		ARFFHandler.loadARFF(new File("tempFile.arff"), it); // Create a temporary file to save the malformed data
		it.close();
		File tempFile = new File("tempFile.arff");
		Dataset actualDataset = ARFFHandler.loadARFF(tempFile);
		tempFile.delete();
	}

	@Test
	public void testLoadARFFWithClassIndex() throws IOException {
		File file = new File("path/to/your/arff/file.arff"); // Replace with the path to your ARFF file
		int classIndex = 2; // Replace with the index of the class attribute
		Dataset expectedDataset = new DefaultDataset(); // Replace with the expected dataset for your ARFF file
		Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
		assertEquals(expectedDataset, actualDataset);
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLoadARFFWithArrayIndexOutOfBoundsException() throws IOException {
		File file = new File("path/to/your/arff/file.arff"); // Replace with the path to your ARFF file
		int classIndex = -1; // Index out of bounds
		ARFFHandler.loadARFF(file, classIndex);
	}

	@Test
	public void testLoadARFFWithEmptyFile() throws IOException {
		File file = new File("path/to/your/empty/file.arff"); // Replace with the path to an empty ARFF file
		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals(new DefaultDataset(), actualDataset);
	}

	@Test
	public void testLoadARFFWithOnlyHeader() throws IOException {
		File file = new File("path/to/your/arff/file_only_header.arff"); // Replace with the path to an ARFF file with only the header
		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals(new DefaultDataset(), actualDataset);
	}
}