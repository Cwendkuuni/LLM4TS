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
		Dataset expected = new DefaultDataset();
		expected.add(new DenseInstance(new double[] {1.0, 2.0, 3.0}, "class1"));
		expected.add(new DenseInstance(new double[] {4.0, 5.0, 6.0}, "class2"));

		File file = new File("test.arff");
		FileWriter writer = new FileWriter(file);
		writer.write("@relation test\n");
		writer.write("@attribute A numeric\n");
		writer.write("@attribute B numeric\n");
		writer.write("@attribute C categorical\n");
		writer.write("@attribute class label\n");
		writer.write("@data\n");
		writer.write("1.0, 2.0, A, class1\n");
		writer.write("4.0, 5.0, B, class2\n");
		writer.close();

		Dataset actual = ARFFHandler.loadARFF(file);
		assertEquals(expected.numInstances(), actual.numInstances());
		assertEquals(expected.attribute(0).isNominal(), actual.attribute(0).isNominal());
		assertEquals(expected.attribute(1).isNominal(), actual.attribute(1).isNominal());
		assertEquals(expected.attribute(2).isNominal(), actual.attribute(2).isNominal());

		for (int i = 0; i < expected.numInstances(); i++) {
			Instance expectedInstance = expected.instance(i);
			Instance actualInstance = actual.instance(i);

			assertArrayEquals(expectedInstance.toArray(), actualInstance.toArray(), 0.001);
		}

		file.deleteOnExit();
	}

	@Test(expected = FileNotFoundException.class)
	public void testLoadARFFWithInvalidFile() throws FileNotFoundException {
		ARFFHandler.loadARFF(new File("non-existent-file.arff"));
	}

	@Test(expected = NumberFormatException.class)
	public void testLoadARFFWithInvalidDataFormat() throws IOException, FileNotFoundException {
		File file = new File("test-invalid.arff");
		FileWriter writer = new FileWriter(file);
		writer.write("@relation test\n");
		writer.write("@attribute A numeric\n");
		writer.write("@attribute B string\n");
		writer.write("@attribute class label\n");
		writer.write("@data\n");
		writer.write("1.0, 2.0, A, class1\n");
		writer.write("4.0, wrong-format, B, class2\n");
		writer.close();

		try {
			ARFFHandler.loadARFF(file);
		} finally {
			file.deleteOnExit();
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testLoadARFFWithNegativeClassIndex() throws IOException {
		File file = new File("test.arff");
		FileWriter writer = new FileWriter(file);
		writer.write("@relation test\n");
		writer.write("@attribute A numeric\n");
		writer.write("@attribute B numeric\n");
		writer.write("@attribute C categorical\n");
		writer.write("@attribute class label\n");
		writer.write("@data\n");
		writer.write("1.0, 2.0, A, class1\n");
		writer.write("4.0, 5.0, B, class2\n");
		writer.close();

		try {
			ARFFHandler.loadARFF(file, -1);
		} finally {
			file.deleteOnExit();
		}
	}
}