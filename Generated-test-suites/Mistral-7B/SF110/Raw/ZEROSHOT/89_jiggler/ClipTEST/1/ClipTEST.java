package jigl.image.ops.levelOps.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.ROI;
import jigl.image.ops.SimpleOperator;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;

public class ClipTest {

	@Test
	public void testConstructorWithIntMinAndIntMax() {
		Clip clip = new Clip(5, 10);
		assertNotNull(clip);
		assertEquals(5, clip.getInt_min());
		assertEquals(10, clip.getInt_max());
		assertFalse(Float.floatEquals(clip.getFloat_min(), 0.0f));
		assertFalse(Float.floatEquals(clip.getFloat_max(), 0.0f));
	}

	@Test
	public void testConstructorWithFloatMinAndFloatMax() {
		Clip clip = new Clip(2.5f, 7.5f);
		assertNotNull(clip);
		assertEquals(2.5f, clip.getFloat_min(), 0.001f);
		assertEquals(7.5f, clip.getFloat_max(), 0.001f);
		assertFalse(clip.getInt_min() == 0);
		assertFalse(clip.getInt_max() == 0);
	}

	@Test
	public void testApplyGrayImage() {
		GrayImage image = new GrayImage(3, 3, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		Clip clip = new Clip(3, 6);
		GrayImage result = clip.apply(image);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());

		for (int y = 0; y < result.Y(); ++y) {
			for (int x = 0; x < result.X(); ++x) {
				int expected = Math.min(Math.max(image.get(x, y), 3), 6);
				int actual = result.get(x, y);
				assertEquals(expected, actual);
			}
		}
	}

	@Test
	public void testApplyRealGrayImage() {
		RealGrayImage image = new RealGrayImage(3, 3, new float[] { 1.1f, 2.2f, 3.3f, 4.4f, 5.5f, 6.6f, 7.7f, 8.8f, 9.9f });
		Clip clip = new Clip(3.5f, 6.5f);
		RealGrayImage result = clip.apply(image);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());

		for (int y = 0; y < result.Y(); ++y) {
			for (int x = 0; x < result.X(); ++x) {
				float expected = Math.min(Math.max(image.get(x, y), 3.5f), 6.5f);
				float actual = result.get(x, y);
				assertEquals(expected, actual, 0.001f);
			}
		}
	}

	@Test
	public void testApplyColorImage() {
		ColorImage image = new ColorImage(3, 3, new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 }, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 }, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3 });
		Clip clip = new Clip(3, 6);
		ColorImage result = clip.apply(image);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());
		assertEquals(image.getPlane(0).size(), result.getPlane(0).size());
		assertEquals(image.getPlane(1).size(), result.getPlane(1).size());
		assertEquals(image.getPlane(2).size(), result.getPlane(2).size());

		for (int y = 0; y < result.Y(); ++y) {
			for (int x = 0; x < result.X(); ++x) {
				int r = Math.min(Math.max(image.getPlane(0).get(x, y), 3), 6);
				int g = Math.min(Math.max(image.getPlane(1).get(x, y), 3), 6);
				int b = Math.min(Math.max(image.getPlane(2).get(x, y), 3), 6);

				int[] expected = new int[] { r, g, b };
				int[] actual = result.getPlane(0).getRow(y, new int[result.X()]);
				assertArrayEquals(expected, actual);
			}
		}
	}

	@Test
	public void testApplyRealColorImage() {
		RealColorImage image = new RealColorImage(3, 3, new float[][] {
				new float[] { 1.1f, 1.2f, 1.3f },
				new float[] { 2.1f, 2.2f, 2.3f },
				new float[] { 3.1f, 3.2f, 3.3f }
		});
		Clip clip = new Clip(3.5f, 6.5f);
		RealColorImage result = clip.apply(image);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());
		assertEquals(image.getPlane(0).size(), result.getPlane(0).size());
		assertEquals(image.getPlane(1).size(), result.getPlane(1).size());
		assertEquals(image.getPlane(2).size(), result.getPlane(2).size());

		for (int y = 0; y < result.Y(); ++y) {
			for (int x = 0; x < result.X(); ++x) {
				float[] expected = new float[] { Math.min(Math.max(image.getPlane(0).get(x, y)[0], 3.5f), 6.5f),
						Math.min(Math.max(image.getPlane(0).get(x, y)[1], 3.5f), 6.5f),
						Math.min(Math.max(image.getPlane(0).get(x, y)[2], 3.5f), 6.5f) };
				float[] actual = result.getPlane(0).getRow(y, new float[result.X()]);
				assertArrayEquals(expected, actual, 0.001f);
			}
		}
	}

	@Test
	public void testApplyWithROI() {
		GrayImage image = new GrayImage(3, 3, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		Clip clip = new Clip(3, 6);

		ROI roi = new ROI(1, 1, 2, 2);
		GrayImage result = clip.apply(image, roi);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());

		for (int y = roi.uy(); y <= roi.ly(); ++y) {
			for (int x = roi.ux(); x <= roi.lx(); ++x) {
				int expected = Math.min(Math.max(image.get(x, y), 3), 6);
				int actual = result.get(x, y);
				assertEquals(expected, actual);
			}
		}
	}

	@Test
	public void testApplyWithRealROI() {
		RealGrayImage image = new RealGrayImage(3, 3, new float[] { 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f });
		Clip clip = new Clip(3.5f, 6.5f);

		ROI roi = new ROI(1.5f, 1.5f, 2, 2);
		RealGrayImage result = clip.apply(image, roi);

		assertNotNull(result);
		assertEquals(image.X(), result.X());
		assertEquals(image.Y(), result.Y());
		assertEquals(image.size(), result.size());

		for (int y = roi.uy(); y <= roi.ly(); ++y) {
			for (int x = roi.ux(); x <= roi.lx(); ++x) {
				float expected = Math.min(Math.max(image.get(x, y), 3.5f), 6.5f);
				float actual = result.get(x, y);
				assertEquals(expected, actual, 0.001f);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyWithNegativeIntMin() {
		GrayImage image = new GrayImage(3, 3, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		Clip clip = new Clip(3, 6);
		clip.setInt_min(-1);
		clip.apply(image);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyWithNegativeIntMax() {
		GrayImage image = new GrayImage(3, 3, new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 });
		Clip clip = new Clip(3, 6);
		clip.setInt_max(-1);
		clip.apply(image);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyWithNegativeFloatMin() {
		RealGrayImage image = new RealGrayImage(3, 3, new float[] { 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f });
		Clip clip = new Clip(3.5f, 6.5f);
		clip.setFloat_min(-1.5f);
		clip.apply(image);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testApplyWithNegativeFloatMax() {
		RealGrayImage image = new RealGrayImage(3, 3, new float[] { 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f });
		Clip clip = new Clip(3.5f, 6.5f);
		clip.setFloat_max(-1.5f);
		clip.apply(image);
	}

	@Test
	public void testMain() {
		try {
			Image image = null;
			String inputfile = "test.jpg";
			Image image2 = null;
			Clip clip = new Clip(0.5f, 1.5f);

			// This test is not comprehensive as it depends on the actual implementation
			// of ImageInputStream and ImageOutputStream.
			// It's intended to just check the sequence of method calls.
			clip.main(new String[] { "0.5", "1.5", inputfile, "output.jpg" });
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception thrown in main method.");
		}
	}
}