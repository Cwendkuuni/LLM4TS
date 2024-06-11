package jigl.image.ops.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import jigl.image.exceptions.InvalidArgumentException;
import jigl.image.ops.ConnectedComponents;
import jigl.image.ops.ConnectedComponents.FOUR_CONNECTED;
import jigl.image.ops.ConnectedComponents.EIGHT_CONNECTED;
import jigl.image.types.GrayImage;
import jigl.image.types.SimpleImage;

public class ConnectedComponentsTest {

	@Test
	public void testConnectedComponentsConstructor() {
		ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
		assertEquals(FOour_CONNECTED, cc.getNeighbors());
		assertEquals(0, cc.getBackground());

		ConnectedComponents cc1 = new ConnectedComponents(EIGHT_CONNECTED, 1);
		assertEquals(EIGHT_CONNECTED, cc1.getNeighbors());
		assertEquals(1, cc1.getBackground());
	}

	@Test
	public void testInvalidArgumentException() {
		assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(2, 2));
		assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(-1, 2));
		assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(1, -1));
	}

	@Test
	public void testConnectedComponentsWithFourConnected() {
		SimpleImage binaryImage = new SimpleImage(4, 4, 1, new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 });
		ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
		GrayImage result = cc.apply(new BinaryImage(binaryImage));

		assertEquals(3, result.getPixelCount());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(2), result.get(1, 1));
		assertEquals(Integer.valueOf(3), result.get(2, 2));
	}

	@Test
	public void testConnectedComponentsWithEightConnected() {
		SimpleImage binaryImage = new SimpleImage(5, 5, 1, new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 });
		ConnectedComponents cc = new ConnectedComponents(EIGHT_CONNECTED, 0);
		GrayImage result = cc.apply(new BinaryImage(binaryImage));

		assertEquals(5, result.getPixelCount());
		assertEquals(Integer.valueOf(1), result.get(0, 0));
		assertEquals(Integer.valueOf(2), result.get(1, 1));
		assertEquals(Integer.valueOf(3), result.get(2, 2));
		assertEquals(Integer.valueOf(4), result.get(3, 3));
		assertEquals(Integer.valueOf(5), result.get(4, 4));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyWithInvalidImageSize() {
		SimpleImage binaryImage = new SimpleImage(-1, 1, 1, new int[] { 1 });
		new ConnectedComponents(FOUR_CONNECTED, 0).apply(new BinaryImage(binaryImage));
	}

	@Test
	public void testApplyWithEmptyImage() {
		SimpleImage binaryImage = new SimpleImage(0, 0, 1, new int[] {});
		ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
		GrayImage result = cc.apply(new BinaryImage(binaryImage));

		assertEquals(0, result.getPixelCount());
		assertEquals(Integer.valueOf(0), result.get(0, 0));
	}

	@Test
	public void testApplyWithAllBackground() {
		SimpleImage binaryImage = new SimpleImage(3, 3, 1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 });
		ConnectedComponents cc = new ConnectedComponents(FOUR_CONNECTED, 0);
		GrayImage result = cc.apply(new BinaryImage(binaryImage));

		assertEquals(1, result.getPixelCount());
		assertEquals(Integer.valueOf(0), result.get(0, 0));
	}

}