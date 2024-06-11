package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import jigl.image.ops.Clip;
import jigl.image.ops.GrayImage;
import jigl.image.ops.Image;
import jigl.image.ops.ImageFactory;
import jigl.image.ops.ImageType;
import jigl.image.ops.RealGrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.ByteType;
import jigl.image.types.FloatType;

@RunWith(Parameterized.class)
public class ClipTest {

    private int width;
    private int height;
    private ByteType byteType;
    private FloatType floatType;

    public ClipTest(int width, int height, ByteType byteType, FloatType floatType) {
        this.width = width;
        this.height = height;
        this.byteType = byteType;
        this.floatType = floatType;
    }

    @Parameterized.Parameters(name = "{index}: test Clip with image of size {0}x{1} and {2} type")
    public static Iterable<Object[]> data() {
        return new Iterator<Object[]>() {
            @Override
            public Object[] next() {
                int width = new Random().nextInt(10) + 1;
                int height = new Random().nextInt(10) + 1;
                ByteType byteType = ImageType.UINT8;
                FloatType floatType = ImageType.FLOAT32;
                return new Object[] { width, height, byteType, floatType };
            }
        }.asIterable();
    }

    @Test
    public void testCreate() {
        Image image = ImageFactory.create(ImageType.UINT8, width, height);
        Clip clip = new Clip(this.floatType.getValue(1.0f), this.floatType.getValue(2.0f));
        assertNull(clip.int_min);
        assertNull(clip.int_max);
        assertNull(clip.float_min);
        assertNull(clip.float_max);

        clip = new Clip(this.int_min, this.int_max);
        assertNull(clip.float_min);
        assertNull(clip.float_max);
        assertEquals(this.int_min, clip.int_min);
        assertEquals(this.int_max, clip.int_max);

        clip = new Clip(this.float_min, this.float_max);
        assertEquals(this.float_min, clip.float_min);
        assertEquals(this.float_max, clip.float_max);
        assertNull(clip.int_min);
        assertNull(clip.int_max);
    }

    @Test
    public void testApplyGray() throws IOException {
        GrayImage image = (GrayImage)ImageFactory.create(ImageType.UINT8, this.width, this.height);
        Random rand = new Random();
        image.set(new ROI(0, 0, this.width, this.height), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        Clip clip = new Clip(this.floatType.getValue(0.0f), this.floatType.getValue(5.0f));
        Image result = clip.apply(image);
        assertTrue(result instanceof GrayImage);

        GrayImage expected = (GrayImage)ImageFactory.create(ImageType.UINT8, this.width, this.height);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                int value = image.get(x, y).intValue();
                int expectedValue = Math.max(Math.min(value, (int)(this.int_max * 255.0f / 256.0f)), (int)(this.int_min * 255.0f / 256.0f));
                expected.set(x, y, Integer.valueOf(expectedValue));
            }
        }

        assertArrayEquals(expected.pixels(), result.pixels());
    }

    @Test
    public void testApplyRealGray() throws IOException {
        RealGrayImage image = (RealGrayImage)ImageFactory.create(ImageType.FLOAT32, this.width, this.height);
        Random rand = new Random();
        image.set(new ROI(0, 0, this.width, this.height), rand.nextFloat(), rand.nextFloat(), rand.nextFloat());

        Clip clip = new Clip(this.float_min, this.float_max);
        Image result = clip.apply(image);
        assertTrue(result instanceof RealGrayImage);

        RealGrayImage expected = (RealGrayImage)ImageFactory.create(ImageType.FLOAT32, this.width, this.height);
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                float value = image.get(x, y).floatValue();
                float expectedValue = Math.max(Math.min(value, this.float_max), this.float_min);
                expected.set(x, y, Float.valueOf(expectedValue));
            }
        }

        assertArrayEquals(expected.pixels(), result.pixels(), 0.001f);
    }

    @Test
    public void testApplyColor() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_3BYTE_BGR);
        DataBufferByte dataBufferByte = (DataBufferByte)bufferedImage.getRaster().getDataBuffer();
        byte[] pixels = dataBufferByte.getData();
        Random rand = new Random();
        for (int i = 0; i < pixels.length; i += 3) {
            pixels[i] = (byte)new Random().nextInt(256);
            pixels[i + 1] = (byte)new Random().nextInt(256);
            pixels[i + 2] = (byte)new Random().nextInt(256);
        }

        Image image = ImageFactory.create(ImageType.UINT8, this.width, this.height, ImageType.RGB, null, pixels);
        Clip clip = new Clip(this.floatType.getValue(0.5f), this.floatType.getValue(1.5f));
        Image result = clip.apply(image);

        assertTrue(result instanceof ColorImage);
        ColorImage expected = (ColorImage)ImageFactory.create(ImageType.UINT8, this.width, this.height, ImageType.RGB, null, pixels);
        clip.apply(expected, new ROI(0, 0, this.width, this.height));
        assertArrayEquals(expected.pixels(), result.pixels());
    }

    @Test
    public void testApplyRealColor() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_FLOAT_ARGB);
        DataBufferByte dataBufferByte = (DataBufferByte)bufferedImage.getRaster().getDataBuffer();
        byte[] pixels = dataBufferByte.getData();
        Random rand = new Random();
        for (int i = 0; i < pixels.length; i += 4) {
            pixels[i] = (byte)(rand.nextFloat() * 255.0f);
            pixels[i + 1] = (byte)(rand.nextFloat() * 255.0f);
            pixels[i + 2] = (byte)(rand.nextFloat() * 255.0f);
            pixels[i + 3] = (byte)(rand.nextFloat() * 255.0f);
        }

        Image image = ImageFactory.create(ImageType.FLOAT32, this.width, this.height, ImageType.RGBA, null, pixels);
        Clip clip = new Clip(this.float_min, this.float_max);
        Image result = clip.apply(image);

        assertTrue(result instanceof RealColorImage);
        RealColorImage expected = (RealColorImage)ImageFactory.create(ImageType.FLOAT32, this.width, this.height, ImageType.RGBA, null, pixels);
        clip.apply(expected, new ROI(0, 0, this.width, this.height));
        assertArrayEquals(expected.pixels(), result.pixels(), 0.01f);
    }

    @Test
    public void testMain() {
        String inputFile = "test.png";
        String outputFile = "output.png";
        Float f_val1 = Float.valueOf(1.0f);
        Float f_val2 = Float.valueOf(2.0f);

        Image image = null;
        try {
            image = new ImageInputStream(inputFile).read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = new ImageOutputStream(byteArrayOutputStream);
        Clip clip = new Clip(f_val1, f_val2);
        clip.apply(image).write(imageOutputStream);

        try {
            Image image2 = new ImageInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).read();
            assertTrue(image2 instanceof GrayImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(outputFile).createNewFile();
            ImageOutputStream imageOutputStream2 = new ImageOutputStream(new File(outputFile));
            clip.apply(image).write(imageOutputStream2);
            imageOutputStream2.close();
            assertTrue(new File(outputFile).exists());
            new File(outputFile).delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}