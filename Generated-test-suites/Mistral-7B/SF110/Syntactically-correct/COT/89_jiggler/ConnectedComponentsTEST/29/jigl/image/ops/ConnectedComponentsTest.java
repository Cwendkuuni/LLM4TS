package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

import org.junit.Test;

public class ConnectedComponentsTest {

	@Test
	public void testConnectedComponentsConstructor() {
		try {
			new ConnectedComponents(2, 1);
			fail("Expected InvalidArgumentException");
		} catch (InvalidArgumentException e) {
			//PASS
		}

		try {
			new ConnectedComponents(0, 2);
			fail("Expected InvalidArgumentException");
		} catch (InvalidArgumentException e) {
			//PASS
		}

		new ConnectedComponents(0, 0);
		new ConnectedComponents(1, 0);
		new ConnectedComponents(1, 1);
	}

	@Test
	public void testApply_EmptyImage() {
		BinaryImage image = new BinaryImage(0, 0);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		assertEquals(0, result.X());
		assertEquals(0, result.Y());
	}

	@Test
	public void testApply_SinglePixel() {
		BinaryImage image = new BinaryImage(1, 1);
		image.set(0, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		assertEquals(1, result.X());
		assertEquals(1, result.Y());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
	}

	@Test
	public void testApply_TwoConnectedPixels() {
		BinaryImage image = new BinaryImage(2, 2);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		assertEquals(2, result.X());
		assertEquals(2, result.Y());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 0));
	}

	@Test
	public void testApply_MultipleComponents() {
		BinaryImage image = new BinaryImage(5, 5);
		image.set(1, 1, 1);
		image.set(2, 2, 1);
		image.set(3, 3, 1);
		image.set(4, 4, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 0);
		GrayImage result = cc.apply(image);
		assertEquals(5, result.X());
		assertEquals(5, result.Y());
		assertEquals(Integer.valueOf(1), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 2));
		assertEquals(Integer.valueOf(1), result.get(3, 3));
		assertEquals(Integer.valueOf(1), result.get(4, 4));
	}

	@Test
	public void testApply_SameValueNeighbors() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 1);
		image.set(0, 1, 1);
		image.set(0, 2, 1);
		image.set(1, 0, 1);
		image.set(1, 1, 1);
		image.set(1, 2, 1);
		image.set(2, 0, 1);
		image.set(2, 1, 1);
		image.set(2, 2, 1);
		ConnectedComponents cc = new ConnectedComponents(8, 0);
		GrayImage result = cc.apply(image);
		assertEquals(3, result.X());
		assertEquals(3, result.Y());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(1), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 2));
	}

	@Test
	public void testApply_DifferentValueNeighbors() {
		BinaryImage image = new BinaryImage(3, 3);
		image.set(0, 0, 1);
		image.set(0, 1, 2);
		image.set(0, 2, 1);
		image.set(1, 0, 2);
		image.set(1, 1, 1);
		image.set(1, 2, 2);
		image.set(2, 0, 1);
		image.set(2, 1, 2);
		image.set(2, 2, 1);
		ConnectedComponents cc = new ConnectedComponents(8, 0);
		GrayImage result = cc.apply(image);
		assertEquals(3, result.X());
		assertEquals(3, result.Y());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(2), result.get(1, 1));
		assertEquals(Integer.valueOf(1), result.get(2, 2));
	}

	@Test(expected = InvalidArgumentException.class)
	public void testApply_InvalidNeighbors() {
		BinaryImage image = new BinaryImage(2, 2);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(3, 0);
		cc.apply(image);
		fail("Expected InvalidArgumentException");
	}

	@Test(expected = InvalidArgumentException.class)
	public void testApply_InvalidBackground() {
		BinaryImage image = new BinaryImage(2, 2);
		image.set(0, 0, 1);
		image.set(1, 0, 1);
		ConnectedComponents cc = new ConnectedComponents(1, 2);
		cc.apply(image);
		fail("Expected InvalidArgumentException");
	}
}