import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Random;

import org.junit.Test;

public class BlockTest {
    @Test
    public void testCharAt() {
        Block block = new Block(new Scheme(new SchemeData(new int[] { 5, 5 }, new char[][] {
                "abcde".toCharArray(), "fghij".toCharArray(), "klmno".toCharArray(), "pqrst".toCharArray(), "uvwxy".toCharArray() })), 1, 1, 3, 4);
        Random rand = new Random();
        int x = rand.nextInt(3) + 1;
        int y = rand.nextInt(4) + 1;
        char expectedChar = block.charAt(x, y);
        char actualChar = block.charAt(x, y);
        assertEquals(expectedChar, actualChar);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharAt_OutOfBlock() {
        Block block = new Block(new Scheme(new SchemeData(new int[] { 5, 5 }, new char[][] {
                "abcde".toCharArray(), "fghij".toCharArray(), "klmno".toCharArray(), "pqrst".toCharArray(), "uvwxy".toCharArray() })), 1, 1, 3, 4);
        int x = 4;
        int y = 5;
        block.charAt(x, y);
    }
}