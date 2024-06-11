import org.junit.Test;
import static org.junit.Assert.*;

public class IndexedStringTest {

 private IndexedString is1;
 private IndexedString is2;
 private char[] target1;
 private char[] target2;
 private char[] target3;
 private char[] errorTarget;

 @Test
 public void testConstructor() {
 is1 = new IndexedString("hello");
 assertEquals("hello".length(), is1.values.length);
 }

 @Test
 public void testCount() {
 is1 = new IndexedString("hello");
 assertEquals(1, is1.count('h'));
 assertEquals(1, is1.count('e'));
 assertEquals(1, is1.count('l'));
 assertEquals(1, is1.count('o'));
 }

 @Test
 public void testIndexOfHappyPath() {
 is1 = new IndexedString("hello");
 target1 = new char[]{'h', 'e', 'l', 'l'};
 int index = is1.indexOf(target1);
 assertEquals(0, index);
 }

 @Test
 public void testIndexOfEdgeCase() {
 is1 = new IndexedString("hello");
 target1 = new char[]{'h', 'e', 'l', 'o', 'l'};
 int index = is1.indexOf(target1);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario1() {
 is1 = new IndexedString("hello");
 errorTarget = new char[1000];
 try {
 is1.indexOf(errorTarget);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario2() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'z'};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario3() {
 is1 = new IndexedString("hello");
 errorTarget = new char[0];
 try {
 is1.indexOf(errorTarget);
 fail("Expected ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario4() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', '\u0101'};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario5() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', (char)0};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario6() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', 'l', (char)0};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario7() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', 'l', 'l'};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario8() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', 'l', 'l', (char)0};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario9() {
 is1 = new IndexedString("hello");
 errorTarget = new char[1000];
 try {
 is1.indexOf(errorTarget);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario10() {
 is1 = new IndexedString("hello");
 errorTarget = new char[1000];
 try {
 is1.indexOf(errorTarget);
 fail("Expected ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario11() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', 'l', 'l', '\u0101'};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario12() {
 is1 = new IndexedString("hello");
 errorTarget = new char[]{'h', 'e', 'l', 'l', 'l', '\u0101', (char)0};
 int index = is1.indexOf(errorTarget);
 assertEquals(-1, index);
 }

 @Test
 public void testIndexOfErrorScenario13() {
 is1 = new IndexedString("hello");
 errorTarget = new char[1000];
 try {
 is1.indexOf(errorTarget);
 fail("Expected ArrayIndexOutOfBoundsException");
 } catch (ArrayIndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario14() {
 is1 = new IndexedString("hello");
 errorTarget = new char[1000];
 try {
 is1.indexOf(errorTarget);
 fail("Expected ArrayStoreException");
 } catch (ArrayStoreException e) {
 // expected
 }
 }

 @Test
 public void testIndexOfErrorScenario15() {
 is2 = new IndexedString("hello world");
 target2 = new char[]{'w', 'o', 'r', 'l'};
 int index = is2.indexOf(target2);
 assertEquals(-1, index);
 }
}