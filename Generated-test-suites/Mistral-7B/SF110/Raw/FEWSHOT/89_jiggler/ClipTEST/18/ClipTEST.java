package jigl.image.ops.levelOps;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import jigl.image.ops.SimpleOperator;
import jigl.image.ops.GrayImage;
import jigl.image.ops.ROI;
import jigl.image.types.IntType;
import jigl.image.types.FloatType;
import jigl.image.io.ImageInputStream;
import jigl.image.io.ImageOutputStream;

@RunWith(MockitoJUnitRunner.class)
public class ClipTest {

    @Test
    public void testClipWithIntMinIntMax() {
        final int min = 10;
        final int max = 20;
        final Clip clip = new Clip(min, max);

        final GrayImage image = Mockito.mock(GrayImage.class);
        Mockito.when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Integer.valueOf(42));
        Mockito.when(image.set(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(IntType.class))).thenAnswer(i -> i.getArguments()[2]);

        clip.apply(image);

        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Integer.valueOf(min)));
        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Integer.valueOf(max)));
    }

    @Test
    public void testClipWithFloatMinFloatMax() {
        final float min = 10.5f;
        final float max = 21.0f;
        final Clip clip = new Clip(min, max);

        final GrayImage image = Mockito.mock(GrayImage.class);
        Mockito.when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Float.valueOf(42.0f));
        Mockito.when(image.set(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(FloatType.class))).thenAnswer(i -> i.getArguments()[2]);

        clip.apply(image);

        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(min)));
        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(max)));
    }

    @Test
    public void testClipWithNullImage() {
        final int min = 10;
        final int max = 20;
        final Clip clip = new Clip(min, max);

        clip.apply(null);

        // No verification since we cannot mock null image
    }

    @Test
    public void testClipWithIntMinIntMaxInRealGrayImage() {
        final int min = 10;
        final int max = 20;
        final Clip clip = new Clip(min, max);

        final RealGrayImage image = Mockito.mock(RealGrayImage.class);
        Mockito.when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Float.valueOf(42.0f));
        Mockito.when(image.set(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(FloatType.class))).thenAnswer(i -> i.getArguments()[2]);

        clip.apply(image);

        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(min)));
        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(max)));
    }

    @Test
    public void testClipWithFloatMinFloatMaxInRealGrayImage() {
        final float min = 10.5f;
        final float max = 21.0f;
        final Clip clip = new Clip(min, max);

        final RealGrayImage image = Mockito.mock(RealGrayImage.class);
        Mockito.when(image.get(Mockito.anyInt(), Mockito.anyInt())).thenReturn(Float.valueOf(42.5f));
        Mockito.when(image.set(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(FloatType.class))).thenAnswer(i -> i.getArguments()[2]);

        clip.apply(image);

        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(min)));
        Mockito.verify(image, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(max)));
    }

    @Test
    public void testClipWithNullRealGrayImage() {
        final float min = 10.5f;
        final float max = 21.0f;
        final Clip clip = new Clip(min, max);

        clip.apply(null);

        // No verification since we cannot mock null real gray image
    }

    @Test
    public void testClipWithIntMinIntMaxInColorImage() {
        final int min = 10;
        final int max = 20;
        final Clip clip = new Clip(min, max);

        final ColorImage image = Mockito.mock(ColorImage.class);
        Mockito.when(image.plane(Mockito.anyInt())).thenReturn(Mockito.mock(GrayImage.class));

        clip.apply(image);

        for (int plane = 0; plane < 3; plane++) {
            final GrayImage planeImage = Mockito.mock(GrayImage.class);
            Mockito.when(image.plane(plane)).thenReturn(planeImage);

            Mockito.verify(planeImage, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Integer.valueOf(min)));
            Mockito.verify(planeImage, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Integer.valueOf(max)));
        }
    }

    @Test
    public void testClipWithFloatMinFloatMaxInColorImage() {
        final float min = 10.5f;
        final float max = 21.0f;
        final Clip clip = new Clip(min, max);

        final ColorImage image = Mockito.mock(ColorImage.class);
        Mockito.when(image.plane(Mockito.anyInt())).thenReturn(Mockito.mock(RealGrayImage.class));

        clip.apply(image);

        for (int plane = 0; plane < 3; plane++) {
            final RealGrayImage planeImage = Mockito.mock(RealGrayImage.class);
            Mockito.when(image.plane(plane)).thenReturn(planeImage);

            Mockito.verify(planeImage, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(min)));
            Mockito.verify(planeImage, Mockito.times(image.X() * image.Y())).set(Mockito.anyInt(), Mockito.anyInt(), Mockito.eq(Float.valueOf(max)));
        }
    }

    @Test
    public void testClipMainMethod() throws Exception {
        final String inputFile = "input.pgm";
        final String outputFile = "output.pgm";
        final float min = 10.5f;
        final float max = 21.0f;

        Image image = null;
        Image image2 = null;
        final ImageInputStream is = Mockito.mock(ImageInputStream.class);
        final ImageOutputStream os = Mockito.mock(ImageOutputStream.class);
        Mockito.when(is.read()).thenReturn(image);
        Mockito.when(os.write(Mockito.any(Image.class))).thenAnswer(i -> image2);

        final Clip clip = new Clip(min, max);
        clip.main(new String[]{"", inputFile, "", outputFile, ""});

        Mockito.verify(is, Mockito.times(1)).read();
        Mockito.verify(image, Mockito.times(1)).apply(Mockito.any(Clip.class));
        Mockito.verify(os, Mockito.times(1)).write(Mockito.eq(image2));
        Mockito.verify(os, Mockito.times(1)).close();
    }

    @Test
    public void testClipMainMethodWithInvalidArguments() {
        final String inputFile = "input.pgm";
        final String outputFile = "output.pgm";
        final float min = 10.5f;
        final float max = 21.0f;

        Image image = null;
        final ImageInputStream is = Mockito.mock(ImageInputStream.class);
        Mockito.when(is.read()).thenReturn(image);

        try {
            final Clip clip = new Clip(min, max);
            clip.main(new String[]{null, inputFile, null, outputFile, ""});
            assertNull(clip);
        } catch (Exception e) {
            // Expected
        }

        Mockito.verify(is, Mockito.times(0)).read();
    }
}