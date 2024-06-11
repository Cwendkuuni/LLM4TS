package jigl.image.ops.levelOps;

import static org.junit.Assert.*;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {

    @Test
    public void testClipConstructorIntInt() {
        Clip clip = new Clip(0, 10);
        assertEquals(0, clip.getInt_min());
        assertEquals(10, clip.getInt_max());
        assertEquals(0.0f, clip.getFloat_min(), 0.01f);
        assertEquals(10.0f, clip.getFloat_max(), 0.01f);
    }

    @Test
    public void testClipConstructorFloatFloat() {
        Clip clip = new Clip(2.5f, 7.5f);
        assertEquals(2, clip.getInt_min());
        assertEquals(7, clip.getInt_max());
        assertEquals(2.5f, clip.getFloat_min(), 0.01f);
        assertEquals(7.5f, clip.getFloat_max(), 0.01f);
    }

    @Test
    public void testGetIntMinDefault() {
        Clip clip = new Clip(new Float(1.5f), new Float(6.5f));
        assertEquals(0, clip.getInt_min());
    }

    @Test
    public void testGetIntMaxDefault() {
        Clip clip = new Clip(new Float(1.5f), new Float(6.5f));
        assertEquals(0, clip.getInt_max());
    }

    @Test
    public void testGetFloatMinDefault() {
        Clip clip = new Clip(new Float(1.5f), new Float(6.5f));
        assertEquals(0.0f, clip.getFloat_min(), 0.01f);
    }

    @Test
    public void testGetFloatMaxDefault() {
        Clip clip = new Clip(new Float(1.5f), new Float(6.5f));
        assertEquals(0.0f, clip.getFloat_max(), 0.01f);
    }

    @Test
    public void testApplyGrayImageIntInt() {
        GrayImage image = new GrayImage(10, 10, new ByteType());
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (byte) (x + y));
            }
        }

        Clip clip = new Clip(1, 5);
        GrayImage result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                int expected = Math.min(Math.max(x + y, 1), 5);
                assertEquals(expected, result.get(x, y).intValue());
            }
        }
    }

    @Test
    public void testApplyRealGrayImageFloatFloat() {
        RealGrayImage image = new RealGrayImage(10, 10, new FloatType());
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.set(x, y, (float) (x + y));
            }
        }

        Clip clip = new Clip(2.5f, 7.5f);
        RealGrayImage result = clip.apply(image);

        for (int y = 0; y < result.Y(); y++) {
            for (int x = 0; x < result.X(); x++) {
                float expected = Math.min(Math.max(x + y, 2.5f), 7.5f);
                assertEquals(expected, result.get(x, y).floatValue(), 0.01f);
            }
        }
    }

    @Test
    public void testApplyColorImageIntInt() {
        ColorImage image = new ColorImage(10, 10, new ByteType(), new ByteType(), new ByteType());
        for (int y = 0; y < image.Y(); y++) {
            for (int x = 0; x < image.X(); x++) {
                image.setPlane(0, x, y, (byte) (x + y));
                image.setPlane(1, x, y, (byte) (x + y));
                image.setPlane(2, x, y, (byte) (x + y));
            }
        }

        Clip clip = new Clip(1, 5);
        ColorImage result = clip.apply(image);

        for (int plane = 0; plane < 3; plane++) {
            for (int y = 0; y < result.Y(); y++) {
                for (int x = 0; x < result.X(); x++) {
                    int expected = Math.min(Math.max(x + y, 1), 5);
                    assertEquals(expected, result.getPlane(plane).get(x, y).byteValue());
                }
            }
        }
    }

    @Test
    public void testMain() {
        try {
            String inputFile = "testImage.jpg";
            String outputFile = "testOutput.jpg";
            Float minValue = new Float(0.1f);
            Float maxValue = new Float(0.3f);
            Clip clip = new Clip(minValue, maxValue);
            GrayImage image = new GrayImage(10, 10, new ByteType());
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    image.set(x, y, (byte) (x + y));
                }
            }
            ImageInputStream is = new FileImageInputStream(inputFile);
            Image image2 = is.read();
            is.close();
            ImageOutputStream os = new FileImageOutputStream(outputFile);
            clip.apply(image2).write(os);
            os.close();
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMainWithInvalidArguments() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            String inputFile = null;
            String outputFile = "testOutput.jpg";
            Float minValue = new Float(0.1f);
            Float maxValue = new Float(0.3f);
            Clip clip = new Clip(minValue, maxValue);
            GrayImage image = new GrayImage(10, 10, new ByteType());
            for (int y = 0; y < image.Y(); y++) {
                for (int x = 0; x < image.X(); x++) {
                    image.set(x, y, (byte) (x + y));
                }
            }
            ImageInputStream is = new FileImageInputStream("invalidFile.jpg");
            Image image2 = is.read();
            is.close();
            ImageOutputStream os = new FileImageOutputStream(outputFile);
            clip.apply(image2).write(os);
            os.close();
        });
    }

    @Test
    public void testMainWithNullImage() {
        assertThrows(NullPointerException.class, () -> {
            String inputFile = "testImage.jpg";
            String outputFile = "testOutput.jpg";
            Float minValue = new Float(0.1f);
            Float maxValue = new Float(0.3f);
            Clip clip = new Clip(minValue, maxValue);
            Image image = null;
            ImageOutputStream os = new FileImageOutputStream(outputFile);
            clip.apply(image).write(os);
            os.close();
        });
    }
}