package jipa;

import org.junit.Assert;
import org.junit.Test;

import static jipa.Main.opcodes.toOpcode;

public class MainTest {
    @Test
    public void testLoadInstructions() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Assert.assertNotNull(Main.instruction);
        Assert.assertEquals(10, Main.instruction.length);
    }

    @Test
    public void testProcessInstruction_halt() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.processInstruction("halt");
        Assert.assertEquals(0, System.exitCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_invalidOpcode() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.processInstruction("invalidOpcode");
    }

    @Test
    public void testProcessInstruction_out() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "out a";
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertEquals("Hello from MainTest!\n", System.out.getBuffer().toString());
    }

    @Test
    public void testProcessInstruction_in() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "in a";
        int input = 42;
        System.setIn(new ByteArrayInputStream(("" + input).getBytes()));
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertEquals(input, Main.Variable.getVariableValue("a"));
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "mov b a";
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertEquals(0, Main.Variable.getVariableValue("a"));
        Assert.assertEquals(42, Main.Variable.getVariableValue("b"));
    }

    @Test(expected = NullPointerException.class)
    public void testProcessInstruction_mov_nullOperand() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "mov a null";
        Main.processInstruction(Main.instruction[Main.iPtr]);
    }

    @Test
    public void testProcessInstruction_set() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "set a 42";
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertEquals(42, Main.Variable.getVariableValue("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_set_invalidValue() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "set a x";
        Main.processInstruction(Main.instruction[Main.iPtr]);
    }

    @Test
    public void testProcessInstruction_del() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "del a";
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertNull(Main.Variable.getVariableValue("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_del_nonExistentVariable() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "del z";
        Main.processInstruction(Main.instruction[Main.iPtr]);
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "arr a 5";
        Main.processInstruction(Main.instruction[Main.iPtr]);
        Assert.assertEquals(5, Main.Variable.getVariableNumber("a".length() + 1));
        for (int i = 0; i < 5; ++i) {
            Assert.assertEquals("", Main.Variable.getVariableValue("a" + (char) (i + '0')));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessInstruction_arr_invalidSize() {
        String[] args = new String[]{"test.asm"};
        Main.main(args);
        Main.instruction[Main.iPtr] = "arr a 0";
        Main.processInstruction(Main.instruction[Main.iPtr]);
    }

    // Add more test cases for other instructions and edge cases as needed
}