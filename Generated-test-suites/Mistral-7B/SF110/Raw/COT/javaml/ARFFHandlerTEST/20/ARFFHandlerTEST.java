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
	public void testLoadARFFFile() throws IOException {
		File file = new File("test.arff"); // Replace with the actual file path
		Dataset expectedDataset = createTestDataset();
		Dataset actualDataset = ARFFHandler.loadARFF(file);
		assertEquals(expectedDataset, actualDataset);
	}

	@Test
	public void testLoadARFFFileWithClassIndex() throws IOException {
		File file = new File("test.arff"); // Replace with the actual file path
		Dataset expectedDataset = createTestDataset();
		int classIndex = 2; // Replace with the actual class index
		Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
		assertEquals(expectedDataset, actualDataset);
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFFileWithInvalidFile() throws FileNotFoundException {
		File file = new File("invalidFile.arff"); // Replace with the actual file path
		ARFFHandler.loadARFF(file);
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFFileWithInvalidNumberFormat() throws IOException {
		File file = new File("test.arff"); // Replace with the actual file path
		String invalidLine = "@data,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,Z1%"; // Replace with the actual invalid line
		LineIterator it = new LineIterator(new FileInputStream(file));
		it.setSkipBlanks(true);
		it.setCommentIdentifier("%");
		it.setSkipComments(true);
		it.next(); // Move to the next line after "@data"
		it.setInput(new ByteArrayInputStream(invalidLine.getBytes()));
		String line = it.nextLine();
		String[] arr = line.split(",");
		ARFFHandler.loadARFF(it, -1); // Call the method with the LineIterator instead of a File
	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void testLoadARFFFileWithInvalidClassIndex() throws IOException {
		File file = new File("test.arff"); // Replace with the actual file path
		int classIndex = -1; // Invalid class index
		Dataset actualDataset = ARFFHandler.loadARFF(file, classIndex);
	}

	private Dataset createTestDataset() {
		// Create a test dataset for verification
		// Replace this with the expected test dataset
		return new DefaultDataset();
	}
}