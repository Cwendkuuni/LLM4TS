import static org.junit.Assert.*;

import org.junit.Test;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "abc\0def\b\tefg\n\thij\nfkl\r\tmno\bqrst\'\'\tuvw\fxyz\\ABC";
        String expected = "abc\\0def\\b\\tefg\\n\\thij\\n\rfkł\r\tmno\\bqrst\\\'\\'\tuvw\\fxyz\\\\ABC";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }
}