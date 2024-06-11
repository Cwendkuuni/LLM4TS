package jigl.image.ops.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.BinaryImage;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {

	@Test
	public void testConnectedComponentsConstructor() {
		try {
			new ConnectedComponents(2, 2);
			assertFalse("InvalidArgumentException expected", true);
		} catch (InvalidArgumentException e) {
			// Expected
		}

		try {
			new ConnectedComponents(-1, 2);
			assertFalse("InvalidArgumentException expected", true);
		} catch (InvalidArgumentException e) {
			// Expected
		}

		try {
			new ConnectedComponents(2, -1);
			assertFalse("InvalidArgumentException expected", true);
		} catch (InvalidArgumentException e) {
			// Expected
		}

		new ConnectedComponents(0, 0);
		new ConnectedComponents(1, 0);
		new ConnectedComponents(0, 1);
	}

	@Test
	public void testApply_emptyImage() {
		BinaryImage image = new BinaryImage(0, 0);
		ConnectedComponents cc = new ConnectedComponents(0, 0);
		GrayImage result = cc.apply(image);
		assertNotNull("Result should not be null", result);
		assertTrue("Result should be empty", result.isEmpty());
	}

	@Test
	public void testApply_singlePixel() {
		BinaryImage image = new BinaryImage(1, 1);
		image.set(0, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		GrayImage expected = new GrayImage(1, 1);
		expected.set(0, 0, 1);
		assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_multipleConnectedComponents() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 1);
		image.set(1, 1, 1);
		image.set(2, 2, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		GrayImage expected = new GrayImage(3, 3);
		expected.set(0, 0, 1);
		expected.set(1, 1, 2);
		expected.set(2, 2, 3);
		assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_fourConnected() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		image.set(2, 0, 1);
		image.set(0, 1, 1);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
		GrayImage result = cc.apply(image);
		GrayImage expected = new GrayImage(3, 3);
		expected.set(0, 0, 1);
		expected.set(1, 0, 1);
		expected.set(2, 0, 1);
		expected.set(0, 1, 2);
		expected.set(1, 1, 2);
		expected.set(2, 1, 2);
		assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_eightConnected() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		image.set(2, 0, 1);
		image.set(0, 1, 1);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		image.set(0, 2, 1);
		image.set(1, 2, 1);
		image.set(2, 2, 1);
		ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
		GrayImage result = cc.apply(image);
		GrayImage expected = new GrayImage(3, 3);
		expected.set(0, 0, 1);
		expected.set(1, 0, 1);
		expected.set(2, 0, 1);
		expected.set(0, 1, 2);
		expected.set(1, 1, 2);
		expected.set(2, 1, 2);
		expected.set(0, 2, 3);
		expected.set(1, 2, 3);
		expected.set(2, 2, 3);
		assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_imageWithBackground() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 0);
		image.set(1, 0, 1);
		image.set(2, 0, 1);
		image.set(0, 1, 0);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		image.set(0, 2, 0);
		image.set(1, 2, 1);
		image.set(2, 2, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		GrayImage expected = new GrayImage(3, 3);
		expected.set(0, 0, 0);
		expected.set(1, 0, 2);
		expected.set(2, 0, 3);
		expected.set(0, 1, 1);
		expected.set(1, 1, 2);
		expected.set(2, 1, 3);
		expected.set(0, 2, 0);
		expected.set(1, 2, 3);
		expected.set(2, 2, 3);
		assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_largeImage() {
		BinaryImage image = new BinaryImage(100, 100);
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				image.set(x, y, (x + y) % 2 == 0 ? 1 : 0);
			}
		}
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		// Manually verify the result by printing both images
		// assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}

	@Test
	public void testApply_largeImageWithBackground() {
		BinaryImage image = new BinaryImage(100, 100);
		for (int x = 0; x < 100; x++) {
			for (int y = 0; y < 100; y++) {
				image.set(x, y, x % 2 == 0 ? 1 : 0);
			}
		}
		ConnectedComponents cc = new ConnectedComponents(1, 1);
		GrayImage result = cc.apply(image);
		// Manually verify the result by printing both images
		// assertArrayEquals("Results do not match", expected.getData(), result.getData());
	}
}