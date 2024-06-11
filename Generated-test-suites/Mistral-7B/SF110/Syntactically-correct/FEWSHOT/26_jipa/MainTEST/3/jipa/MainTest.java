package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_mov() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"mov A 0"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varZero = new Variable("0");

        i.processInstruction(instruction[0]);

        assertEquals(0, varA.getValue());
        assertEquals(0, varZero.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_mov_with_operand() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"mov B A"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);

        assertEquals(0, varA.getValue());
        assertEquals(0, varB.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_set() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"set A 5"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);

        assertEquals(5, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_set_with_operand() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"set B 10"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);

        assertEquals(10, varB.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_inc() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"inc A"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);

        assertEquals(1, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_dec() {
        Main.TOTAL_INSTRUCTIONS = 1;
        String[] instruction = new String[]{"dec A"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);

        assertEquals(-1, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_add() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 5", "add A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(6, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_sub() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 7", "sub A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(4, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_mul() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 5", "mul A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(25, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_div() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 10", "div A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(2, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_mod() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 7", "mod A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(1, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_xor() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 1", "xor A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(-1, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_or() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 1", "or A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(2, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_and() {
        Main.TOTAL_INSTRUCTIONS = 2;
        String[] instruction = new String[]{"mov A 1", "and A B"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(0, varA.getValue());
        assertEquals(1, i.iPtr);
    }

    @Test
    public void testProcessInstruction_jmp() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 1", "jmp label1", "label1: mov A 2"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(2, varA.getValue());
        assertEquals(2, i.iPtr);
    }

    @Test
    public void testProcessInstruction_jnz() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 1", "jnz label1: jmp label2", "label1: mov A 2", "label2: mov A 3"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(3, varA.getValue());
        assertEquals(3, i.iPtr);
    }

    @Test
    public void testProcessInstruction_jz() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 1", "jz label1: jmp label2", "label1: mov A 2", "label2: mov A 3"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(1, varA.getValue());
        assertEquals(2, i.iPtr);
    }

    @Test
    public void testProcessInstruction_je() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 1", "mov B 1", "je label1: jmp label2", "label1: mov A 2", "label2: mov A 3"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(1, varA.getValue());
        assertEquals(2, i.iPtr);
    }

    @Test
    public void testProcessInstruction_jne() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 1", "mov B 2", "jne label1: jmp label2", "label1: mov A 2", "label2: mov A 3"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(1, varA.getValue());
        assertEquals(3, i.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 5", "mov B 2", "cmp A B", "jz label1: jmp label2"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(5, varA.getValue());
        assertEquals(0, varB.getValue());
        assertEquals(1, i.iPtr);

        i.processInstruction(instruction[2]);

        assertEquals(0, i.iPtr);
    }

    @Test
    public void testProcessInstruction_cmp_false() {
        Main.TOTAL_INSTRUCTIONS = 3;
        String[] instruction = new String[]{"mov A 2", "mov B 5", "cmp A B", "jz label1: jmp label2"};
        Main.instruction = instruction;
        Main i = new Main();
        Variable varA = new Variable("A");
        Variable varB = new Variable("B");

        i.processInstruction(instruction[0]);
        i.processInstruction(instruction[1]);

        assertEquals(2, varA.getValue());
        assertEquals(5, varB.getValue());
        assertEquals(1, i.iPtr);

        i.processInstruction(instruction[2]);

        assertEquals(2, i.iPtr);
    }

    @Test
    public void testLoadInstructions() {
        String fileName = "test/instructions.txt";
        Main i = new Main();
        i.loadInstructions(fileName);
        assertEquals(5, i.instruction.length);
    }
}