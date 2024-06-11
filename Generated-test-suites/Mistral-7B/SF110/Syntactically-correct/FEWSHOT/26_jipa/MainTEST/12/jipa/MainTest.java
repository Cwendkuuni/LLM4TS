package jipa;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halts() {
        String[] instructions = new String[]{"halt"};
        Main.main(instructions);
        // The test should not reach this point as the program should exit
        fail("Program did not halt as expected.");
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instructions = new String[]{"out", "hello"};
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                assertEquals("hello", s);
            }
        });
        Main.main(instructions);
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instructions = new String[]{"in", "x"};
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{(byte) '42'});
        System.setIn(input);
        String expected = "42";
        Main.Variable variable = new Main.Variable("x");
        Main.main(instructions);
        assertEquals(expected, variable.getValue());
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instructions = new String[]{"mov", "y", "x"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        xVariable.setValue(10);
        Main.main(instructions);
        assertEquals(10, yVariable.getValue());
        assertEquals(0, xVariable.getValue());
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instructions = new String[]{"cpy", "y", "x"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        xVariable.setValue(10);
        Main.main(instructions);
        assertEquals(10, yVariable.getValue());
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instructions = new String[]{"set", "x", "5"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.main(instructions);
        assertEquals(5, xVariable.getValue());
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instructions = new String[]{"del", "x"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.main(instructions);
        assertNull(Main.Variable.getVariableValue("x"));
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instructions = new String[]{"arr", "arr1", "5"};
        Main.main(instructions);
        String[] expected = new String[]{"arr1", "arr10"};
        for (int i = 0; i < expected.length; ++i) {
            assertTrue(Main.Variable.isVariableDefined(expected[i]));
        }
    }

    @Test
    public void testProcessInstruction_var() {
        String[] instructions = new String[]{"var", "x"};
        Main.main(instructions);
        assertTrue(Main.Variable.isVariableDefined("x"));
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instructions = new String[]{"inc", "x"};
        Main.Variable xVariable = new Main.Variable("x");
        xVariable.setValue(10);
        Main.main(instructions);
        assertEquals(11, xVariable.getValue());
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instructions = new String[]{"dec", "x"};
        Main.Variable xVariable = new Main.Variable("x");
        xVariable.setValue(10);
        Main.main(instructions);
        assertEquals(9, xVariable.getValue());
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instructions = new String[]{"add", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(8, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instructions = new String[]{"sub", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(2, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instructions = new String[]{"mul", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(15, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instructions = new String[]{"div", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(10);
        yVariable.setValue(2);
        Main.main(instructions);
        assertEquals(5, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instructions = new String[]{"mod", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(10);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(1, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instructions = new String[]{"xor", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(2, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instructions = new String[]{"or", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(6, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instructions = new String[]{"and", "x", "y", "z"};
        Main.Variable xVariable = new Main.Variable("x");
        Main.Variable yVariable = new Main.Variable("y");
        Main.Variable zVariable = new Main.Variable("z");
        xVariable.setValue(5);
        yVariable.setValue(3);
        Main.main(instructions);
        assertEquals(3, zVariable.getValue());
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instructions = new String[]{"jmp", "5"};
        int[] expectedJumpPoints = new int[]{5, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
        }
    }

    @Test
    public void testProcessInstruction_jnz() {
        String[] instructions = new String[]{"jnz", "0", "5"};
        int[] expectedJumpPoints = new int[]{0, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            if (i != 0) {
                assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
            }
        }
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instructions = new String[]{"jz", "1", "5"};
        int[] expectedJumpPoints = new int[]{1, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            if (i != 0 && i != 1) {
                assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
            }
        }
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instructions = new String[]{"je", "0", "5"};
        int[] expectedJumpPoints = new int[]{0, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            if (i != 0 && i != 1) {
                assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
            }
        }
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instructions = new String[]{"jne", "0", "5"};
        int[] expectedJumpPoints = new int[]{1, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            if (i != 0) {
                assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
            }
        }
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instructions = new String[]{"cmp", "1", "0", "z"};
        int[] expectedJumpPoints = new int[]{0, 5, 5, 5, 5};
        int[] actualJumpPoints = new int[expectedJumpPoints.length];
        int iPtr = 0;
        Main.Variable zVariable = new Main.Variable("z");
        Main.main(instructions);
        for (int i = 0; i < instructions.length; ++i) {
            actualJumpPoints[i] = Main.iPtr;
            if (Main.iPtr == expectedJumpPoints[i]) {
                ++iPtr;
            }
        }
        for (int i = 0; i < expectedJumpPoints.length; ++i) {
            assertEquals(expectedJumpPoints[i], actualJumpPoints[i]);
        }
        // Ensure zVariable has the correct value
        assertEquals(0, zVariable.getValue());
    }
}