package jigl.image.ops;

import static org.junit.Assert.*;
import jigl.image.*;

import org.junit.Test;

public class ConnectedComponentsTest {

 @Test
 public void testConnectedComponents_0Connected() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 0, 0, 0, 1},
 {1, 0, 1, 0, 1},
 {1, 0, 1, 0, 1},
 {1, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 5);
 assertEquals(result.Y(), 5);
 int[] expected = {1, 2, 3, 3, 1};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_1Connected() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 0, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 3);
 assertEquals(result.Y(), 3);
 int[] expected = {1, 2, 1};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_0Connected_Background() {
 BinaryImage image = new BinaryImage(new int[][]{
 {0, 1, 1, 1, 1},
 {1, 0, 0, 0, 1},
 {1, 0, 1, 0, 1},
 {1, 0, 1, 0, 1},
 {1, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 5);
 assertEquals(result.Y(), 5);
 int[] expected = {0, 1, 2, 3, 1};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_1Connected_Background() {
 BinaryImage image = new BinaryImage(new int[][]{
 {0, 1, 1, 1, 1},
 {1, 0, 1, 1, 1},
 {0, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 3);
 assertEquals(result.Y(), 3);
 int[] expected = {0, 2, 0};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_0Connected_Empty() {
 BinaryImage image = new BinaryImage(new int[][]{
 {0}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 1);
 assertEquals(result.Y(), 1);
 int[] expected = {0};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_1Connected_Empty() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 1);
 assertEquals(result.Y(), 1);
 int[] expected = {0};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_1Connected_DifferentNeighbors() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 2, 2, 2, 1},
 {1, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 3);
 assertEquals(result.Y(), 3);
 int[] expected = {1, 2, 1};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_1Connected_DifferentNeighbors_Background() {
 BinaryImage image = new BinaryImage(new int[][]{
 {0, 1, 1, 1, 1},
 {1, 2, 2, 2, 1},
 {0, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 3);
 assertEquals(result.Y(), 3);
 int[] expected = {0, 2, 0};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

 @Test
 public void testConnectedComponents_8Connected() {
 BinaryImage image = new BinaryImage(new int[][]{
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1},
 {1, 1, 1, 1, 1}
 });
 ConnectedComponents op = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
 GrayImage result = op.apply(image);
 assertEquals(result.X(), 3);
 assertEquals(result.Y(), 3);
 int[] expected = {1, 1, 1};
 for (int x = 0; x < result.X(); ++x) {
 for (int y = 0; y < result.Y(); ++y) {
 int value = result.get(x, y).intValue();
 assertEquals(expected[x * result.X() + y], value);
 }
 }
 }

}