package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;

import org.junit.Test;

public class ConnectedComponentsTest {

	@Test
	public void testConnectedComponents() {
		// Test ConnectedComponents constructor with valid arguments
		ConnectedComponents cc = new ConnectedComponents(0, 0);
		assertNotNull(cc);
		ConnectedComponents cc1 = new ConnectedComponents(1, 0);
		assertNotNull(cc1);

		// Test ConnectedComponents constructor with invalid arguments
		try {
			new ConnectedComponents(2, 0);
			fail("Expected InvalidArgumentException");
		} catch (ConnectedComponents.InvalidArgumentException e) {
			// Expected
		}
		try {
			new ConnectedComponents(0, 2);
			fail("Expected InvalidArgumentException");
		} catch (ConnectedComponents.InvalidArgumentException e) {
			// Expected
		}

		// Test apply method with 0-connected components
		BinaryImage binaryImage = new BinaryImage(new int[][] {{0, 1, 1, 1, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 0}, {1, 1, 1, 0, 0}});
		ConnectedComponents cc2 = new ConnectedComponents(0, 0);
		GrayImage expectedImage0Connected = new GrayImage(binaryImage.X(), binaryImage.Y());
		expectedImage0Connected.set(0, 0, Integer.valueOf(1));
		expectedImage0Connected.set(1, 0, Integer.valueOf(1));
		expectedImage0Connected.set(2, 0, Integer.valueOf(1));
		expectedImage0Connected.set(3, 0, Integer.valueOf(1));
		expectedImage0Connected.set(4, 0, Integer.valueOf(1));
		expectedImage0Connected.set(0, 1, Integer.valueOf(1));
		expectedImage0Connected.set(1, 1, Integer.valueOf(1));
		expectedImage0Connected.set(2, 1, Integer.valueOf(1));
		expectedImage0Connected.set(3, 1, Integer.valueOf(1));
		expectedImage0Connected.set(4, 1, Integer.valueOf(1));
		expectedImage0Connected.set(1, 2, Integer.valueOf(1));
		expectedImage0Connected.set(2, 2, Integer.valueOf(1));
		expectedImage0Connected.set(3, 2, Integer.valueOf(1));
		expectedImage0Connected.set(4, 2, Integer.valueOf(1));
		expectedImage0Connected.set(1, 3, Integer.valueOf(1));
		expectedImage0Connected.set(2, 3, Integer.valueOf(1));
		expectedImage0Connected.set(3, 3, Integer.valueOf(1));
		expectedImage0Connected.set(4, 3, Integer.valueOf(1));
		expectedImage0Connected.set(1, 4, Integer.valueOf(1));
		expectedImage0Connected.set(2, 4, Integer.valueOf(1));
		expectedImage0Connected.set(3, 4, Integer.valueOf(1));
		expectedImage0Connected.set(4, 4, Integer.valueOf(1));
		GrayImage actualImage = cc2.apply(binaryImage);
		assertArrayEquals(expectedImage0Connected.getData(), actualImage.getData());

		// Test apply method with 4-connected components
		BinaryImage binaryImage4Connected = new BinaryImage(new int[][] {{0, 1, 1, 1, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 0}, {1, 1, 1, 0, 0}});
		ConnectedComponents cc3 = new ConnectedComponents(4, 0);
		GrayImage expectedImage4Connected = new GrayImage(binaryImage.X(), binaryImage.Y());
		expectedImage4Connected.set(0, 0, Integer.valueOf(1));
		expectedImage4Connected.set(1, 0, Integer.valueOf(1));
		expectedImage4Connected.set(1, 1, Integer.valueOf(1));
		expectedImage4Connected.set(2, 0, Integer.valueOf(1));
		expectedImage4Connected.set(2, 1, Integer.valueOf(1));
		expectedImage4Connected.set(3, 0, Integer.valueOf(1));
		expectedImage4Connected.set(3, 1, Integer.valueOf(1));
		expectedImage4Connected.set(4, 0, Integer.valueOf(1));
		expectedImage4Connected.set(4, 1, Integer.valueOf(1));
		expectedImage4Connected.set(1, 2, Integer.valueOf(1));
		expectedImage4Connected.set(2, 2, Integer.valueOf(1));
		expectedImage4Connected.set(1, 3, Integer.valueOf(1));
		expectedImage4Connected.set(2, 3, Integer.valueOf(1));
		expectedImage4Connected.set(1, 4, Integer.valueOf(1));
		expectedImage4Connected.set(2, 4, Integer.valueOf(1));
		expectedImage4Connected.set(3, 1, Integer.valueOf(1));
		expectedImage4Connected.set(3, 2, Integer.valueOf(1));
		expectedImage4Connected.set(3, 3, Integer.valueOf(1));
		expectedImage4Connected.set(3, 4, Integer.valueOf(1));
		GrayImage actualImage4Connected = cc3.apply(binaryImage4Connected);
		assertArrayEquals(expectedImage4Connected.getData(), actualImage4Connected.getData());

		// Test apply method with 8-connected components
		BinaryImage binaryImage8Connected = new BinaryImage(new int[][] {{0, 1, 1, 1, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 0}, {1, 1, 1, 0, 0}});
		ConnectedComponents cc4 = new ConnectedComponents(8, 0);
		GrayImage expectedImage8Connected = new GrayImage(binaryImage.X(), binaryImage.Y());
		expectedImage8Connected.set(0, 0, Integer.valueOf(1));
		expectedImage8Connected.set(1, 0, Integer.valueOf(1));
		expectedImage8Connected.set(1, 1, Integer.valueOf(1));
		expectedImage8Connected.set(0, 1, Integer.valueOf(1));
		expectedImage8Connected.set(1, 1, Integer.valueOf(1));
		expectedImage8Connected.set(1, 2, Integer.valueOf(1));
		expectedImage8Connected.set(2, 0, Integer.valueOf(1));
		expectedImage8Connected.set(2, 1, Integer.valueOf(1));
		expectedImage8Connected.set(2, 2, Integer.valueOf(1));
		expectedImage8Connected.set(3, 0, Integer.valueOf(1));
		expectedImage8Connected.set(3, 1, Integer.valueOf(1));
		expectedImage8Connected.set(3, 2, Integer.valueOf(1));
		expectedImage8Connected.set(4, 0, Integer.valueOf(1));
		expectedImage8Connected.set(4, 1, Integer.valueOf(1));
		expectedImage8Connected.set(4, 2, Integer.valueOf(1));
		expectedImage8Connected.set(1, 3, Integer.valueOf(1));
		expectedImage8Connected.set(2, 3, Integer.valueOf(1));
		expectedImage8Connected.set(3, 3, Integer.valueOf(1));
		expectedImage8Connected.set(4, 3, Integer.valueOf(1));
		expectedImage8Connected.set(1, 4, Integer.valueOf(1));
		expectedImage8Connected.set(2, 4, Integer.valueOf(1));
		expectedImage8Connected.set(3, 4, Integer.valueOf(1));
		expectedImage8Connected.set(4, 4, Integer.valueOf(1));
		GrayImage actualImage8Connected = cc4.apply(binaryImage8Connected);
		assertArrayEquals(expectedImage8Connected.getData(), actualImage8Connected.getData());
	}
}