package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage inputImage = new ByteImageFactory().createByteImage(5, 5, 1);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) i % 255));
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(10, 200);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expectedValue = Math.max(10, Math.min(x % inputImage.X(), y % inputImage.Y()));
                int actualValue = ((ByteImage) outputImage).get(x, y).intValue();
                assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipIntIntWithZeroValues() {
        GrayImage inputImage = new ByteImageFactory().createByteImage(5, 5, 1);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) (i % 255 < 128 ? i % 255 : 0)));
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(10, 200);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expectedValue = Math.max(10, Math.min(x % inputImage.X(), y % inputImage.Y()));
                int actualValue = ((ByteImage) outputImage).get(x, y).intValue();
                if (expectedValue == 0) {
                    assertNull("Expected null, but was: " + actualValue, actualValue);
                } else {
                    assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue);
                }
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = new ByteImageFactory().createRealByteImage(5, 5, 1);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) i % 255));
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Float.valueOf(i % 255 / 255.0f));
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(0.1f, 0.9f);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expectedValue = Math.max(0.1f, Math.min(x % inputImage.X(), y % inputImage.Y()) / 255.0f);
                float actualValue = ((RealGrayImage) outputImage).get(x, y).floatValue();
                assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatWithZeroValues() {
        RealGrayImage inputImage = new ByteImageFactory().createRealByteImage(5, 5, 1);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) (i % 255 < 128 ? i % 255 : 0)));
            inputImage.set(i % inputImage.X(), i / inputImage.X(), Float.valueOf((i % 255 < 128) ? (float) i / 255.0f : 0.0f));
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(0.1f, 0.9f);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expectedValue = Math.max(0.1f, Math.min(x % inputImage.X(), y % inputImage.Y()) / 255.0f);
                float actualValue = ((RealGrayImage) outputImage).get(x, y).floatValue();
                if (expectedValue == 0.0f) {
                    assertNull("Expected null, but was: " + actualValue, actualValue);
                } else {
                    assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue, 0.001f);
                }
            }
        }
    }

    @Test
    public void testClipColorImageIntInt() {
        ColorImage inputImage = new ByteMultiComponentImageFactory().createMultiComponentByteImage(5, 5, 3);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            for (int band = 0; band < 3; band++) {
                inputImage.setPlane(band, i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) i % 255));
            }
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(10, 200);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                for (int band = 0; band < 3; band++) {
                    int expectedValue = Math.max(10, Math.min(x % inputImage.X(), y % inputImage.Y()));
                    int actualValue = ((ByteImage) outputImage.getPlane(band)).get(x, y).intValue();
                    assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue);
                }
            }
        }
    }

    @Test
    public void testClipColorImageFloatFloat() {
        RealColorImage inputImage = new ByteMultiComponentImageFactory().createMultiComponentRealByteImage(5, 5, 3);
        for (int i = 0; i < inputImage.X() * inputImage.Y(); i++) {
            for (int band = 0; band < 3; band++) {
                inputImage.setPlane(band, i % inputImage.X(), i / inputImage.X(), Byte.valueOf((byte) i % 255));
                inputImage.setPlane(band, i % inputImage.X(), i / inputImage.X(), Float.valueOf(((float) i % 255) / 255.0f));
            }
        }

        ROI roi = new ROI(1, 1, 3, 3);

        Clip clip = new Clip(0.1f, 0.9f);
        Image outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                for (int band = 0; band < 3; band++) {
                    float expectedValue = Math.max(0.1f, Math.min(x % inputImage.X(), y % inputImage.Y()) / 255.0f);
                    float actualValue = ((RealGrayImage) outputImage.getPlane(band)).get(x, y).floatValue();
                    assertEquals("Expected value: " + expectedValue + ", but was: " + actualValue, expectedValue, actualValue, 0.001f);
                }
            }
        }
    }
}