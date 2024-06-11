package jigl.image.ops.levelOps;

import static org.junit.Assert.*;

import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipConstructorIntInt() {
        Clip clip = new Clip(5, 10);
        assertEquals(5, clip.int_min);
        assertEquals(10, clip.int_max);
        assertEquals(5.0f, clip.float_min, 0.01f);
        assertEquals(10.0f, clip.float_max, 0.01f);
    }

    @Test
    public void testClipConstructorFloatFloat() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals(2, clip.int_min);
        assertEquals(8, clip.int_max);
        assertEquals(2.5f, clip.float_min, 0.01f);
        assertEquals(7.5f, clip.float_max, 0.01f);
    }

    @Test
    public void testApplyGrayImage() {
        GrayImage image = new ByteGrayImage(new int[1][1], new ImageMetadata());
        image.set(0, 0, (byte) 127);

        Clip clip = new Clip(0, 128);
        Image result = clip.apply(image);

        assertEquals(result.getClass(), image.getClass());
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                int expectedValue = Math.min(Math.max(image.get(x, y), clip.int_min), clip.int_max);
                int actualValue = ((GrayImage) result).get(x, y).intValue();
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test
    public void testApplyRealGrayImage() {
        RealGrayImage image = new FloatGrayImage(new float[1][1], new ImageMetadata());
        image.set(0, 0, 0.5f);

        Clip clip = new Clip(0.25f, 0.75f);
        Image result = clip.apply(image);

        assertEquals(result.getClass(), image.getClass());
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                float expectedValue = Math.min(Math.max(image.get(x, y), clip.float_min), clip.float_max);
                float actualValue = ((RealGrayImage) result).get(x, y).floatValue();
                assertEquals(expectedValue, actualValue, 0.01f);
            }
        }
    }

    @Test
    public void testApplyColorImage() {
        ColorImage image = new RGBImage(new byte[1][1][3], new ImageMetadata());
        image.setPlane(0, new ByteGrayImage(new int[1][1], new ImageMetadata()));
        image.setPlane(1, new ByteGrayImage(new int[1][1], new ImageMetadata()));
        image.setPlane(2, new ByteGrayImage(new int[1][1], new ImageMetadata()));

        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < image.X(); x++) {
                for (int y = 0; y < image.Y(); y++) {
                    byte expectedValue = Math.min(Math.max(image.get(x, y, i), 0), 255);
                    byte actualValue = ((RGBImage) image.clone()).getPlane(i).get(x, y);
                    assertEquals(expectedValue, actualValue);
                }
            }
        }

        Clip clip = new Clip(50, 150);
        Image result = clip.apply(image);

        assertEquals(result.getClass(), image.getClass());
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    byte expectedValue = Math.min(Math.max(((GrayImage) result.getPlane(plane)).get(x, y).intValue(), clip.int_min), clip.int_max);
                    byte actualValue = ((RGBImage) result).getPlane(plane).get(x, y);
                    assertEquals(expectedValue, actualValue);
                }
            }
        }
    }

    @Test
    public void testApplyRealColorImage() {
        RealColorImage image = new FloatRGBImage(new float[1][1][3], new ImageMetadata());
        image.setPlane(0, new FloatGrayImage(new float[1][1], new ImageMetadata()));
        image.setPlane(1, new FloatGrayImage(new float[1][1], new ImageMetadata()));
        image.setPlane(2, new FloatGrayImage(new float[1][1], new ImageMetadata()));

        for (int i = 0; i < 3; i++) {
            for (int x = 0; x < image.X(); x++) {
                for (int y = 0; y < image.Y(); y++) {
                    float expectedValue = Math.min(Math.max(image.get(x, y, i), 0.0f), 1.0f);
                    float actualValue = ((RealRGBImage) image.clone()).getPlane(i).get(x, y).floatValue();
                    assertEquals(expectedValue, actualValue, 0.01f);
                }
            }
        }

        Clip clip = new Clip(0.25f, 0.75f);
        Image result = clip.apply(image);

        assertEquals(result.getClass(), image.getClass());
        assertEquals(image.X(), result.X());
        assertEquals(image.Y(), result.Y());

        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    float expectedValue = Math.min(Math.max(((RealGrayImage) result.getPlane(plane)).get(x, y).floatValue(), clip.float_min), clip.float_max);
                    float actualValue = ((RealRGBImage) result).getPlane(plane).get(x, y).floatValue();
                    assertEquals(expectedValue, actualValue, 0.01f);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullImage() {
        Clip clip = new Clip(5, 10);
        clip.apply(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyWithNullROI() {
        GrayImage image = new ByteGrayImage(new int[1][1], new ImageMetadata());
        Clip clip = new Clip(5, 10);
        clip.apply(image, null);
    }

    @Test
    public void testMainMethod() {
        try {
            String inputFilePath = "test_image.jpg";
            String outputFilePath = "output_image.jpg";
            float minValue = 0.25f;
            float maxValue = 0.75f;

            Clip clip = new Clip(minValue, maxValue);

            Image inputImage = new FileImage(new File(inputFilePath));
            Image outputImage = new FileImage(new File(outputFilePath));

            Clip.main(new String[]{"java", "Clip", inputFilePath, outputFilePath, String.valueOf(minValue), String.valueOf(maxValue)});

            // Check if the output image is saved correctly
            // Add your assertions here based on the expected output image
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}