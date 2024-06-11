package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipIntInt() {
        GrayImage inputImage = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        for (int i = 0; i < inputImage.getWidth() * inputImage.getHeight(); i++) {
            inputImage.set(i % inputImage.getWidth(), i / inputImage.getHeight(), (byte) (i % 10 + 50));
        }

        ROI roi = new ROI(2, 3, 3, 3);
        Clip clip = new Clip(10, 150);
        GrayImage outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expectedValue = Math.max(10, Math.min(150, inputImage.get(x, y) & 0xFF));
                int actualValue = outputImage.get(x, y).intValue();
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipIntIntApplyToGrayImage() {
        GrayImage inputImage = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        for (int i = 0; i < inputImage.getWidth() * inputImage.getHeight(); i++) {
            inputImage.set(i % inputImage.getWidth(), i / inputImage.getHeight(), (byte) (i % 10 + 50));
        }

        ROI roi = new ROI(2, 3, 3, 3);
        Clip clip = new Clip(10, 150);
        GrayImage outputImage = clip.apply(inputImage);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                int expectedValue = Math.max(10, Math.min(150, inputImage.get(x, y) & 0xFF));
                int actualValue = outputImage.get(x, y).intValue();
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipFloatFloat() {
        RealGrayImage inputImage = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        for (int y = 0; y < inputImage.getHeight(); y++) {
            for (int x = 0; x < inputImage.getWidth(); x++) {
                inputImage.set(x, y, 50.0f + x / 10.0f + y / 10.0f);
            }
        }

        ROI roi = new ROI(2, 3, 3, 3);
        Clip clip = new Clip(100.0f, 150.0f);
        RealGrayImage outputImage = clip.apply(inputImage, roi);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expectedValue = Math.max(100.0f, Math.min(150.0f, inputImage.get(x, y)));
                float actualValue = outputImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipFloatFloatApplyToRealGrayImage() {
        RealGrayImage inputImage = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        for (int y = 0; y < inputImage.getHeight(); y++) {
            for (int x = 0; x < inputImage.getWidth(); x++) {
                inputImage.set(x, y, 50.0f + x / 10.0f + y / 10.0f);
            }
        }

        ROI roi = new ROI(2, 3, 3, 3);
        Clip clip = new Clip(100.0f, 150.0f);
        RealGrayImage outputImage = clip.apply(inputImage);

        for (int y = roi.uy(); y <= roi.ly(); ++y) {
            for (int x = roi.ux(); x <= roi.lx(); ++x) {
                float expectedValue = Math.max(100.0f, Math.min(150.0f, inputImage.get(x, y)));
                float actualValue = outputImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipIntIntWithIntMinIntMax() {
        GrayImage inputImage = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        for (int i = 0; i < inputImage.getWidth() * inputImage.getHeight(); i++) {
            inputImage.set(i % inputImage.getWidth(), i / inputImage.getHeight(), (byte) (i % 10 + 50));
        }

        Clip clip = new Clip(50, 150);
        GrayImage outputImage = clip.apply(inputImage);

        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                int expectedValue = Math.max(50, Math.min(150, inputImage.get(x, y) & 0xFF));
                int actualValue = outputImage.get(x, y).intValue();
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipFloatFloatWithIntMinIntMax() {
        RealGrayImage inputImage = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        for (int y = 0; y < inputImage.getHeight(); y++) {
            for (int x = 0; x < inputImage.getWidth(); x++) {
                inputImage.set(x, y, 50.0f + x / 10.0f + y / 10.0f);
            }
        }

        Clip clip = new Clip(50.0f, 150.0f);
        RealGrayImage outputImage = clip.apply(inputImage);

        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                float expectedValue = Math.max(50.0f, Math.min(150.0f, inputImage.get(x, y)));
                float actualValue = outputImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }

    @Test
    public void testClipWithNullImage() {
        Clip clip = new Clip(50.0f, 150.0f);
        assertNull("Clip should not be able to be applied to null image", clip.apply((Image) null));
    }

    @Test
    public void testClipWithNullRoi() {
        GrayImage image = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        Clip clip = new Clip(50, 150);
        assertNull("Clip should not be able to be applied to null roi", clip.apply(image, (ROI) null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithInvalidMinValue() {
        GrayImage image = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        Clip clip = new Clip(-50, 150);
        clip.apply(image, new ROI(0, 0, image.getWidth(), image.getHeight()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithInvalidMaxValue() {
        GrayImage image = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        Clip clip = new Clip(50, -150);
        clip.apply(image, new ROI(0, 0, image.getWidth(), image.getHeight()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithInvalidMinValueFloat() {
        RealGrayImage image = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        Clip clip = new Clip(-50.0f, 150.0f);
        clip.apply(image, new ROI(0, 0, image.getWidth(), image.getHeight()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClipWithInvalidMaxValueFloat() {
        RealGrayImage image = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        Clip clip = new Clip(50.0f, -150.0f);
        clip.apply(image, new ROI(0, 0, image.getWidth(), image.getHeight()));
    }

    @Test
    public void testClipWithValidMinValueAndMaxValue() {
        GrayImage image = new BufferedImageGray(5, 5, DataBuffer.TYPE_BYTE);
        for (int i = 0; i < image.getWidth() * image.getHeight(); i++) {
            image.set(i % image.getWidth(), i / image.getHeight(), (byte) (i % 10 + 50));
        }

        Clip clip = new Clip(10, 50);
        GrayImage outputImage = clip.apply(image);

        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                int expectedValue = Math.min(50, Math.max(10, image.get(x, y) & 0xFF));
                int actualValue = outputImage.get(x, y).intValue();
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testClipWithValidMinValueAndMaxValueFloat() {
        RealGrayImage image = new BufferedRealGray(5, 5, DataBuffer.TYPE_FLOAT);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.set(x, y, 50.0f + x / 10.0f + y / 10.0f);
            }
        }

        Clip clip = new Clip(50.0f, 100.0f);
        RealGrayImage outputImage = clip.apply(image);

        for (int y = 0; y < outputImage.getHeight(); y++) {
            for (int x = 0; x < outputImage.getWidth(); x++) {
                float expectedValue = Math.min(100.0f, Math.max(50.0f, image.get(x, y)));
                float actualValue = outputImage.get(x, y);
                assertEquals("Unexpected pixel value at (" + x + ", " + y + ")", expectedValue, actualValue, 0.001f);
            }
        }
    }
}