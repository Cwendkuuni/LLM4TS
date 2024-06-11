package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.types.GrayImage;
import jigl.image.types.BinaryImage;

import org.junit.Test;

public class ConnectedComponentsTest {

	@Test
	public void testConnectedComponentsValidArguments() {
		try {
			ConnectedComponents cc = new ConnectedComponents(0, 0);
			assertNotNull(cc);
		} catch (InvalidArgumentException e) {
			fail("Expected no exception for valid arguments");
		}

		try {
			ConnectedComponents cc = new ConnectedComponents(1, 0);
			assertNotNull(cc);
		} catch (InvalidArgumentException e) {
			fail("Expected no exception for valid arguments");
		}

		try {
			ConnectedComponents cc = new ConnectedComponents(0, 1);
			assertNotNull(cc);
		} catch (InvalidArgumentException e) {
			fail("Expected no exception for valid arguments");
		}
	}

	@Test(expected = InvalidArgumentException.class)
	public void testConnectedComponentsInvalidArguments() {
		new ConnectedComponents(2, 0);
	}

	@Test
	public void testConnectedComponentsEmptyImage() {
		BinaryImage image = new BinaryImage(0, 0);
		ConnectedComponents cc = new ConnectedComponents(0, 0);
		GrayImage result = cc.apply(image);
		assertEquals(0, result.X());
		assertEquals(0, result.Y());
	}

	@Test
	public void testConnectedComponentsSinglePixel() {
		BinaryImage image = new BinaryImage(1, 1);
		image.set(0, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(0, 0);
		GrayImage result = cc.apply(image);
		assertEquals(1, result.X());
		assertEquals(1, result.Y());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
	}

	@Test
	public void testConnectedComponentsMultipleComponents() {
		BinaryImage image = new BinaryImage(5, 5);
		image.set(0, 0, 1);
		image.set(1, 1, 1);
		image.set(3, 3, 1);
		image.set(4, 4, 1);
		ConnectedComponents cc = new ConnectedComponents(0, 0);
		GrayImage result = cc.apply(image);
		assertEquals(5, result.X());
		assertEquals(5, result.Y());
		// Components are labelled in order they were encountered
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(2), result.get(1, 1));
		assertEquals(Integer.valueOf(3), result.get(3, 3));
		assertEquals(Integer.valueOf(4), result.get(4, 4));
	}

	@Test
	public void testConnectedComponentsFourConnected() {
		BinaryImage image = new BinaryImage(5, 5);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		image.set(3, 1, 1);
		image.set(4, 1, 1);
		image.set(0, 2, 1);
		image.set(1, 2, 1);
		image.set(1, 3, 1);
		image.set(2, 3, 1);
		image.set(3, 3, 1);
		ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
		GrayImage result = cc.apply(image);
		assertEquals(5, result.X());
		assertEquals(5, result.Y());
		// Components are connected 4-neighbors
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 1));
		assertEquals(Integer.valueOf(1), result.get(3, 1));
		assertEquals(Integer.valueOf(1), result.get(4, 1));
		assertEquals(Integer.valueOf(1), result.get(0, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 3));
		assertEquals(Integer.valueOf(1), result.get(2, 3));
		assertEquals(Integer.valueOf(1), result.get(3, 3));
	}

	@Test
	public void testConnectedComponentsEightConnected() {
		BinaryImage image = new BinaryImage(5, 5);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		image.set(3, 1, 1);
		image.set(4, 1, 1);
		image.set(0, 2, 1);
		image.set(1, 2, 1);
		image.set(1, 3, 1);
		image.set(2, 3, 1);
		image.set(3, 3, 1);
		image.set(4, 3, 1);
		image.set(0, 4, 1);
		image.set(1, 4, 1);
		image.set(1, 5, 1);
		image.set(2, 5, 1);
		image.set(3, 5, 1);
		image.set(4, 5, 1);
		ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
		GrayImage result = cc.apply(image);
		assertEquals(5, result.X());
		assertEquals(5, result.Y());
		// Components are connected 8-neighbors
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 1));
		assertEquals(Integer.valueOf(1), result.get(3, 1));
		assertEquals(Integer.valueOf(1), result.get(4, 1));
		assertEquals(Integer.valueOf(1), result.get(0, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 3));
		assertEquals(Integer.valueOf(1), result.get(2, 3));
		assertEquals(Integer.valueOf(1), result.get(3, 3));
		assertEquals(Integer.valueOf(1), result.get(4, 3));
		assertEquals(Integer.valueOf(1), result.get(0, 4));
		assertEquals(Integer.valueOf(1), result.get(1, 4));
		assertEquals(Integer.valueOf(1), result.get(1, 5));
		assertEquals(Integer.valueOf(1), result.get(2, 5));
		assertEquals(Integer.valueOf(1), result.get(3, 5));
		assertEquals(Integer.valueOf(1), result.get(4, 5));
	}

	@Test
	public void testConnectedComponentsBackground() {
		BinaryImage image = new BinaryImage(5, 5);
		image.set(0, 0, 0);
		image.set(1, 0, 1);
		image.set(1, 1, 1);
		image.set(2, 1, 1);
		image.set(3, 1, 1);
		image.set(4, 1, 1);
		image.set(0, 2, 1);
		image.set(1, 2, 1);
		image.set(1, 3, 1);
		image.set(2, 3, 1);
		image.set(3, 3, 1);
		image.set(4, 3, 1);
		image.set(0, 4, 1);
		image.set(1, 4, 1);
		image.set(1, 5, 1);
		image.set(2, 5, 1);
		image.set(3, 5, 1);
		image.set(4, 5, 1);
		ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
		GrayImage result = cc.apply(image);
		assertEquals(5, result.X());
		assertEquals(5, result.Y());
		// Background pixels are assigned component 0
		assertEquals(Integer.valueOf(0), result.get(0, 0));
		assertEquals(Integer.valueOf(0), result.get(1, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 1));
		assertEquals(Integer.valueOf(1), result.get(3, 1));
		assertEquals(Integer.valueOf(1), result.get(4, 1));
		assertEquals(Integer.valueOf(0), result.get(0, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 2));
		assertEquals(Integer.valueOf(1), result.get(1, 3));
		assertEquals(Integer.valueOf(1), result.get(2, 3));
		assertEquals(Integer.valueOf(1), result.get(3, 3));
		assertEquals(Integer.valueOf(1), result.get(4, 3));
		assertEquals(Integer.valueOf(0), result.get(0, 4));
		assertEquals(Integer.valueOf(1), result.get(1, 4));
		assertEquals(Integer.valueOf(1), result.get(1, 5));
		assertEquals(Integer.valueOf(1), result.get(2, 5));
		assertEquals(Integer.valueOf(1), result.get(3, 5));
		assertEquals(Integer.valueOf(1), result.get(4, 5));
	}
}