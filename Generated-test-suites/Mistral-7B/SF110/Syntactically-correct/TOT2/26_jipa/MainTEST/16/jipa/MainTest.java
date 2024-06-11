import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MainTest {

    @Test
    public void testMain() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        assertEquals(0, Main.iPtr);
    }

    @Test
    public void testProcessInstruction_Basic() {
        String instruction = "out 1";
        Main.processInstruction(instruction);
        assertEquals("", Main.instruction[Main.iPtr]);
        assertEquals("1", instruction.substring(0, instruction.indexOf(32)).trim());
        assertEquals("out", opcodes.toOpcode(instruction.substring(0, instruction.indexOf(32)).trim()).name());
        assertEquals("", instruction.substring(instruction.indexOf(32) + 1).trim());
    }

    @Test
    public void testGetFileName_Basic() {
        String fileName = "test.asm";
        String expectedFileName = "test.asm";
        String result = Main.getFileName();
        assertEquals(expectedFileName, result);
    }

    @Test
    public void testRemoveComment_Basic() {
        String line = "// test comment";
        String result = Main.removeComment(line);
        assertEquals("test comment", line);
        assertEquals("test", result);
    }
}