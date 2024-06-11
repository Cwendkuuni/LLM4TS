package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.levelOps.Clip;
import jigl.image.ops.levelOps.ROI;
import jigl.image.types.GrayImage;
import jigl.image.types.RealGrayImage;
import jigl.image.IO.Image;
import jigl.image.IO.ImageInputStream;
import jigl.image.IO.ImageOutputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClipTest {

    @Test
    public void testClipConstructorWithIntMinAndIntMax() {
        Clip clip = new Clip(1, 10);
        assertEquals(1, clip.int_min);
        assertEquals(10, clip.int_max);
        assertNull(clip.float_min);
        assertNull(clip.float_max);
    }

    @Test
    public void testClipConstructorWithFloatMinAndFloatMax() {
        Clip clip = new Clip(1.5f, 7.5f);
        assertEquals(1, clip.int_min);
        assertEquals(7, clip.int_max);
        assertEquals(1.5f, clip.float_min);
        assertEquals(7.5f, clip.float_max);
    }

    @Test
    public void testApplyGrayImageWithIntMinAndIntMax() {
        Clip clip = new Clip(1, 10);
        GrayImage image = mock(GrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn((byte) 150);

        Image result = clip.apply(image);

        byte[] expectedResult = new byte[image.X() * image.Y()];
        for (int i = 0; i < expectedResult.length; i++) {
            expectedResult[i] = (byte) Math.min(Math.max(1, i / 10), 10);
        }

        assertArrayEquals(expectedResult, ((GrayImage) result).getPixels());
    }

    @Test
    public void testApplyRealGrayImageWithFloatMinAndFloatMax() {
        Clip clip = new Clip(1.5f, 7.5f);
        RealGrayImage image = mock(RealGrayImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn((float) 3.14159265358979323846);

        Image result = clip.apply(image);

        float[] expectedResult = new float[image.X() * image.Y()];
        for (int i = 0; i < expectedResult.length; i++) {
            expectedResult[i] = Math.min(Math.max(1.5f, i / 10.0f), 7.5f);
        }

        assertArrayEquals(expectedResult, ((RealGrayImage) result).getPixels(), 0.001f);
    }

    @Test
    public void testApplyColorImageWithIntMinAndIntMax() {
        Clip clip = new Clip(1, 10);
        ColorImage image = mock(ColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        when(image.getPlane(Mockito.anyInt())).thenReturn(mock(GrayImage.class));

        Image result = clip.apply(image);

        for (int i = 0; i < image.numPlanes(); i++) {
            GrayImage plane = (GrayImage) image.getPlane(i);
            assertArrayEquals(getExpectedResult(1, 10, plane.X(), plane.Y()), plane.getPixels());
        }
    }

    @Test
    public void testApplyRealColorImageWithFloatMinAndFloatMax() {
        Clip clip = new Clip(1.5f, 7.5f);
        RealColorImage image = mock(RealColorImage.class);
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);

        when(image.getPlane(Mockito.anyInt())).thenReturn(mock(RealGrayImage.class));

        Image result = clip.apply(image);

        for (int i = 0; i < image.numPlanes(); i++) {
            RealGrayImage plane = (RealGrayImage) image.getPlane(i);
            assertArrayEquals(getExpectedResult(1.5f, 7.5f, plane.X(), plane.Y()), plane.getPixels(), 0.001f);
        }
    }

    private byte[] getExpectedResult(int min, int max, int width, int height) {
        byte[] result = new byte[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                byte value = (byte) Math.min(Math.max(min, index / 10), max);
                result[index] = value;
            }
        }
        return result;
    }

    private float[] getExpectedResult(float min, float max, int width, int height) {
        float[] result = new float[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                float value = Math.min(Math.max(min, index / 10.0f), max);
                result[index] = value;
            }
        }
        return result;
    }
}