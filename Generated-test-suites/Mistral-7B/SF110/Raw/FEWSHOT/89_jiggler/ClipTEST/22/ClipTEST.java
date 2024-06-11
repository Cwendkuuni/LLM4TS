package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import org.junit.Test;

import jigl.image.*;
import jigl.image.ops.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage image = new ByteInterleavedSample(3, 3, 1, 1, new byte[]{1, 2, 3, 2, 1, 3, 2, 1, 3});
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(5, 7);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expected = Math.max(Math.min(image.get(x, y), 7), 5);
                int actual = ((Integer) result.get(x, y)).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipIntIntApplyToGrayImage() {
        GrayImage image = new ByteInterleavedSample(3, 3, 1, 1, new byte[]{1, 2, 3, 2, 1, 3, 2, 1, 3});
        Clip clip = new Clip(5, 7);

        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                int expected = Math.max(Math.min(image.get(x, y), 7), 5);
                int actual = ((Integer) result.get(x, y)).intValue();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testClipIntIntApplyToColorImage() {
        ColorImage image = new ByteInterleavedSample(3, 3, 3, 1, new byte[]{}, new byte[]{}, new byte[]{}, new byte[]{1, 2, 3, 2, 1, 3, 2, 1, 3, 1, 2, 3, 2, 1, 3});
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(5, 7);

        Image result = clip.apply(image, roi);

        for (int plane = 0; plane < image.nPlanes(); ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    int expected = Math.max(Math.min(image.get(x, y, plane), 7), 5);
                    int actual = ((Integer) result.get(x, y, plane)).intValue();
                    assertEquals(expected, actual);
                }
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage image = new FloatInterleavedSample(3, 3, 1, 1, new float[]{1.f, 2.f, 3.f, 2.f, 1.f, 3.f, 2.f, 1.f, 3.f});
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(5.5f, 7.5f);

        Image result = clip.apply(image, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expected = Math.max(Math.min(image.get(x, y), 7.5f), 5.5f);
                float actual = ((Float) result.get(x, y)).floatValue();
                assertEquals(expected, actual, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyToRealGrayImage() {
        RealGrayImage image = new FloatInterleavedSample(3, 3, 1, 1, new float[]{1.f, 2.f, 3.f, 2.f, 1.f, 3.f, 2.f, 1.f, 3.f});
        Clip clip = new Clip(5.5f, 7.5f);

        Image result = clip.apply(image);

        for (int y = 0; y < image.Y(); ++y) {
            for (int x = 0; x < image.X(); ++x) {
                float expected = Math.max(Math.min(image.get(x, y), 7.5f), 5.5f);
                float actual = ((Float) result.get(x, y)).floatValue();
                assertEquals(expected, actual, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyToColorImage() {
        RealColorImage image = new FloatInterleavedSample(3, 3, 3, 1, new float[]{}, new float[]{}, new float[]{}, new float[]{1.f, 2.f, 3.f, 2.f, 1.f, 3.f, 2.f, 1.f, 3.f, 1.f, 2.f, 3.f, 2.f, 1.f, 3.f});
        ROI roi = new ROI(0, 0, image.X() - 1, image.Y() - 1);
        Clip clip = new Clip(5.5f, 7.5f);

        Image result = clip.apply(image, roi);

        for (int plane = 0; plane < image.nPlanes(); ++plane) {
            for (int y = roi.uy(); y <= roi.ly(); ++y) {
                for (int x = roi.ux(); x <= roi.lx(); ++x) {
                    float expected = Math.max(Math.min(image.get(x, y, plane), 7.5f), 5.5f);
                    float actual = ((Float) result.get(x, y, plane)).floatValue();
                    assertEquals(expected, actual, 0.001f);
                }
            }
        }
    }
}