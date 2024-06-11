import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = TokenMgrError.addEscapes(input);
        assertEquals(expectedOutput, output);
    }
}