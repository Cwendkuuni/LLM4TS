package jigl.image.ops.test;

import static org.junit.Assert.*;

import jigl.image.*;
import jigl.image.ops.ConnectedComponents;
import jigl.image.types.*;
import org.junit.Test;

public class ConnectedComponentsTest {

 private ConnectedComponents cc;

 @Test
 public void testConnectedComponentsConstructor() {
 cc = new ConnectedComponents(0, 0);
 assertNotNull(cc);
 CC_testMethods(cc, new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}));

 cc = new ConnectedComponents(1, 0);
 assertNotNull(cc);
 CC_testMethods(cc, new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}));

 cc = new ConnectedComponents(0, 1);
 assertNotNull(cc);
 CC_testMethods(cc, new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}));

 cc = new ConnectedComponents(1, 1);
 assertNotNull(cc);
 CC_testMethods(cc, new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}}));

 cc = new ConnectedComponents(1, 2); // invalid argument
 assertThrows(InvalidArgumentException.class, () -> new ConnectedComponents(1, 2));
 }

 @Test
 public void testApply_ZeroConnectedComponents() {
 BinaryImage input = new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
 cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);
 ByteArrayDataSource outputDataSource = new ByteArrayDataSource(output.getImageData(), "image/x-raw");
 byte[] expectedOutput = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 byte[] actualOutput = new byte[outputDataSource.length()];
 outputDataSource.read(0, actualOutput, 0, outputDataSource.length());
 assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testApply_FourConnectedComponents() {
 BinaryImage input = new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
 cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);
 ByteArrayDataSource outputDataSource = new ByteArrayDataSource(output.getImageData(), "image/x-raw");
 byte[] expectedOutput = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 byte[] actualOutput = new byte[outputDataSource.length()];
 outputDataSource.read(0, actualOutput, 0, outputDataSource.length());
 assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testApply_EightConnectedComponents() {
 BinaryImage input = new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
 cc = new ConnectedComponents(1, 0);
 GrayImage output = cc.apply(input);
 ByteArrayDataSource outputDataSource = new ByteArrayDataSource(output.getImageData(), "image/x-raw");
 byte[] expectedOutput = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
 byte[] actualOutput = new byte[outputDataSource.length()];
 outputDataSource.read(0, actualOutput, 0, outputDataSource.length());
 assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testApply_EdgeCases_InvalidImage() {
 assertThrows(NullPointerException.class, () -> cc.apply(null));
 assertThrows(IllegalArgumentException.class, () -> cc.apply(new BinaryImage(1, 1, null)));
 assertThrows(IllegalArgumentException.class, () -> cc.apply(new BinaryImage(-1, 1, new byte[1][1])));
 }

 @Test
 public void testApply_EdgeCases_EmptyImage() {
 BinaryImage emptyImage = new BinaryImage(0, 0, new byte[0][0]);
 assertThrows(InvalidImageException.class, () -> cc.apply(emptyImage));
 }

 @Test
 public void testApply_EdgeCases_EmptyBlock() {
 BinaryImage input = new BinaryImage(1, 1, new byte[][]{{1}});
 cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);
 assertNull(output);
 }

 @Test
 public void testApply_EdgeCases_SingleBlock() {
 BinaryImage input = new BinaryImage(1, 1, new byte[][]{{1}});
 cc = new ConnectedComponents(1, 0);
 GrayImage output = cc.apply(input);
 ByteArrayDataSource outputDataSource = new ByteArrayDataSource(output.getImageData(), "image/x-raw");
 byte[] expectedOutput = { 1 };
 byte[] actualOutput = new byte[outputDataSource.length()];
 outputDataSource.read(0, actualOutput, 0, outputDataSource.length());
 assertArrayEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testApply_EdgeCases_MultipleBlocks() {
 BinaryImage input = new BinaryImage(5, 5, new byte[][]{{1, 1, 1, 1, 1},
         {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 1}});
 byte[] block1 = { 1, 1, 1, 1, 1 };
 byte[] block2 = { 0, 0, 0, 0, 0 };
 byte[] block3 = { 1, 1, 1, 1, 1 };
 int x = 1;
 int y = 1;
 int width = 3;
 int height = 3;
 GrayImage expectedOutput = new GrayImage(input.X() / width, input.Y() / height, new byte[input.X() / width][input.Y() / height]);
 for (int i = 0; i < input.X() / width; ++i) {
 for (int j = 0; j < input.Y() / height; ++j) {
 byte[] outputBlock = expectedOutput.getImageData()[i * input.Y() / height + j];
 int blockIndex = 0;
 if (i * width + x < input.X() && j * height + y < input.Y()) {
 byte[] inputBlock = input.getImageData()[i * input.Y() / height + j];
 for (int k = 0; k < width * height; ++k) {
 if (block1[k] != inputBlock[k]) {
 ++blockIndex;
 }
 }
 outputBlock[blockIndex] = (byte) (blockIndex + 1);
 }
 else {
 outputBlock[0] = (byte) 0;
 }
 }
 }
 cc = new ConnectedComponents(0, 0);
 GrayImage output = cc.apply(input);
 ByteArrayDataSource outputDataSource = new ByteArrayDataSource(output.getImageData(), "image/x-raw");
 for (int i = 0; i < expectedOutput.getWidth(); ++i) {
 for (int j = 0; j < expectedOutput.getHeight(); ++j) {
 byte[] expectedBlock = expectedOutput.getImageData()[i * expectedOutput.getHeight() + j];
 byte[] actualBlock = new byte[outputDataSource.read(i * output.getWidth() + j, new byte[output.getWidth()], 0, output.getWidth())];
 assertArrayEquals(expectedBlock, actualBlock);
 }
 }
 }

 private void CC_testMethods(ConnectedComponents cc, BinaryImage input) {
 // test methods not applicable for ConnectedComponents class
 }
}