package jigl.image.ops.levelOps;

import org.junit.Assert;
import org.junit.Test;
import jigl.image.ops.*;
import jigl.image.*;
import jigl.image.types.*;
import jigl.image.io.*;

public class ClipTest {
   @Test
   public void testClipIntConstructor() {
      int min = 5;
      int max = 10;
      Clip clip = new Clip(min, max);

      Assert.assertEquals(min, clip.int_min);
      Assert.assertEquals(max, clip.int_max);
      Assert.assertNull(clip.float_min);
      Assert.assertNull(clip.float_max);
   }

   @Test
   public void testApplyGrayImageWithIntegerROI() {
      int width = 5;
      int height = 5;
      GrayImage image = new GrayImage(width, height);
      image.set(0, 0, (byte) 255);
      image.set(1, 1, (byte) 128);
      image.set(2, 2, (byte) 64);

      int min = 100;
      int max = 150;
      Clip clip = new Clip(min, max);

      ROI roi = new ROI(1, 1, 3, 3);
      GrayImage expectedOutput = new GrayImage(3, 3);
      expectedOutput.set(0, 0, (byte) 128);
      expectedOutput.set(0, 1, (byte) 128);
      expectedOutput.set(0, 2, (byte) 128);
      expectedOutput.set(1, 0, (byte) 128);
      expectedOutput.set(1, 1, (byte) 128);
      expectedOutput.set(1, 2, (byte) 128);
      expectedOutput.set(2, 0, (byte) 128);
      expectedOutput.set(2, 1, (byte) 128);
      expectedOutput.set(2, 2, (byte) 128);

      GrayImage output = (GrayImage) clip.apply(image, roi);

      Assert.assertNotNull(output);
      Assert.assertEquals(expectedOutput.getImageData(), output.getImageData());
   }

   @Test
   public void testApplyColorImageWithFloatROI() {
      int width = 5;
      int height = 5;
      ColorImage image = new ColorImage(width, height, 3);
      image.setPlane(0, new GrayImage(width, height));
      image.setPlane(1, new GrayImage(width, height));
      image.setPlane(2, new GrayImage(width, height));

      ColorImage testImage = new ColorImage(width, height, 3);
      testImage.setPlane(0, new GrayImage(width, height));
      testImage.setPlane(1, new GrayImage(width, height));
      testImage.setPlane(2, new GrayImage(width, height));

      for (int i = 0; i < width * height; i++) {
         testImage.set(i, 0, (byte) (i % 10 + 1));
         testImage.set(i, 1, (byte) (i % 10 + 1));
         testImage.set(i, 2, (byte) (i % 10 + 1));
      }

      float min = 0.5f;
      float max = 1.5f;
      Clip clip = new Clip(min, max);

      ROI roi = new ROI(1.5f, 1.5f, 3.0f, 3.0f);
      ColorImage expectedOutput = new ColorImage(3, 3, 3);
      expectedOutput.setPlane(0, new GrayImage(3, 3));
      expectedOutput.setPlane(1, new GrayImage(3, 3));
      expectedOutput.setPlane(2, new GrayImage(3, 3));

      // Set expected output values for the ColorImage test case

      ColorImage output = (ColorImage) clip.apply(testImage, roi);

      Assert.assertNotNull(output);
      Assert.assertEquals(expectedOutput.getPlane(0).getImageData(), output.getPlane(0).getImageData());
      Assert.assertEquals(expectedOutput.getPlane(1).getImageData(), output.getPlane(1).getImageData());
      Assert.assertEquals(expectedOutput.getPlane(2).getImageData(), output.getPlane(2).getImageData());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testClipIntConstructorWithInvalidInput() {
      int min = Integer.MIN_VALUE;
      int max = Integer.MAX_VALUE;
      new Clip(min, max);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testApplyGrayImageWithInvalidROI() {
      int width = 5;
      int height = 5;
      GrayImage image = new GrayImage(width, height);

      int min = 100;
      int max = 150;
      Clip clip = new Clip(min, max);

      ROI roi = new ROI(-1, -1, 3, 3);
      clip.apply(image, roi);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testApplyColorImageWithInvalidROI() {
      int width = 5;
      int height = 5;
      ColorImage image = new ColorImage(width, height, 3);
      image.setPlane(0, new GrayImage(width, height));
      image.setPlane(1, new GrayImage(width, height));
      image.setPlane(2, new GrayImage(width, height));

      float min = 0.5f;
      float max = 1.5f;
      Clip clip = new Clip(min, max);

      ROI roi = new ROI(-1.5f, -1.5f, 3.0f, 3.0f);
      clip.apply(image, roi);
   }
}