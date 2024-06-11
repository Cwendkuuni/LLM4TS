package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.ROI;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.types.Image;
import jigl.image.types.ColorImage;
import jigl.image.types.RealColorImage;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

import org.junit.Test;

public class ClipTest {

    @Test
    public void testConstructorIntInt() {
        Clip clip = new Clip(1, 2);
        assertTrue(clip.int_min == 1);
        assertTrue(clip.int_max == 2);
        assertFalse(clip.float_min == 0.0f);
        assertFalse(clip.float_max == 0.0f);
    }

    @Test
    public void testConstructorFloatFloat() {
        Clip clip = new Clip(1.5f, 2.5f);
        assertFalse(clip.int_min == 1);
        assertFalse(clip.int_max == 2);
        assertTrue(clip.float_min == 1.5f);
        assertTrue(clip.float_max == 2.5f);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = mock(GrayImage.class);
        ROI roi = new ROI(0, 0, 10, 10);
        Clip clip = new Clip(1, 2);

        when(image.get(0, 0)).thenReturn((Integer) 3);
        when(image.get(5, 5)).thenReturn((Integer) 5);
        when(image.get(10, 10)).thenReturn((Integer) 7);

        Image expectedImage = new GrayImage(10, 10);
        expectedImage.set(0, 0, Integer.valueOf(1));
        expectedImage.set(5, 5, Integer.valueOf(2));
        expectedImage.set(10, 10, Integer.valueOf(2));

        Image imageResult = clip.apply(image, roi);
        assertArrayEquals(expectedImage.getData(), imageResult.getData());
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = mock(RealGrayImage.class);
        ROI roi = new ROI(0, 0, 10, 10);
        Clip clip = new Clip(1.5f, 2.5f);

        when(image.get(0, 0)).thenReturn(1.0f);
        when(image.get(5, 5)).thenReturn(3.5f);
        when(image.get(10, 10)).thenReturn(6.0f);

        RealGrayImage expectedImage = new RealGrayImage(10, 10);
        expectedImage.set(0, 0, Float.valueOf(1.5f));
        expectedImage.set(5, 5, Float.valueOf(2.0f));
        expectedImage.set(10, 10, Float.valueOf(2.0f));

        RealGrayImage imageResult = clip.apply(image, roi);
        assertArrayEquals(expectedImage.getData(), imageResult.getData(), 0.001f);
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = mock(ColorImage.class);
        ROI roi = new ROI(0, 0, 10, 10);
        Clip clip = new Clip(1, 2);

        when(image.getPlane(0).get(0, 0)).thenReturn((Integer) 3);
        when(image.getPlane(1).get(5, 5)).thenReturn((Integer) 5);
        when(image.getPlane(2).get(10, 10)).thenReturn((Integer) 7);

        ColorImage expectedImage = new ColorImage(10, 10, new GrayImage(10, 10), new GrayImage(10, 10), new GrayImage(10, 10));
        expectedImage.setPlane(0, 0, new GrayImage(10, 10));
        expectedImage.getPlane(0).set(0, 0, Integer.valueOf(1));
        expectedImage.getPlane(0).set(5, 5, Integer.valueOf(2));
        expectedImage.getPlane(0).set(10, 10, Integer.valueOf(2));

        ColorImage imageResult = clip.apply(image, roi);
        assertTrue(imageResult.equals(expectedImage));
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = mock(RealColorImage.class);
        ROI roi = new ROI(0, 0, 10, 10);
        Clip clip = new Clip(1.5f, 2.5f);

        when(image.getPlane(0).get(0, 0)).thenReturn(1.0f);
        when(image.getPlane(1).get(5, 5)).thenReturn(3.5f);
        when(image.getPlane(2).get(10, 10)).thenReturn(6.0f);

        RealColorImage expectedImage = new RealColorImage(10, 10, new RealGrayImage(10, 10), new RealGrayImage(10, 10), new RealGrayImage(10, 10));
        expectedImage.setPlane(0, 0, new RealGrayImage(10, 10));
        expectedImage.getPlane(0).set(0, 0, Float.valueOf(1.5f));
        expectedImage.getPlane(0).set(5, 5, Float.valueOf(2.0f));
        expectedImage.getPlane(0).set(10, 10, Float.valueOf(2.0f));

        RealColorImage imageResult = clip.apply(image, roi);
        assertTrue(imageResult.equals(expectedImage));
    }
}