package jipa;

import static org.junit.Assert.*;
import org.junit.Test;

public class MainTest {

    @Test
    public void testProcessInstruction_halt() {
        String[] instruction = {"halt"};
        Main.main(instruction);
        assertEquals(0, Main.iPtr); // Check if the program has been terminated
    }

    @Test
    public void testProcessInstruction_out() {
        String[] instruction = {"out", "msg"};
        System.out.println("msg"); // Set up the mocked output
        Main.main(instruction);
        assertEquals("msg", System.out.getHistogram().get(0).getFormattedValue()); // Check the output
    }

    @Test
    public void testProcessInstruction_in() {
        String[] instruction = {"in", "var1"};
        int input = 10;
        int result = Main.readValue();
        assertEquals(input, result); // Set up the mocked input
        Main.Variable.setVariableValue("var1", result);
        String[] testInstruction = {"mov", "var2", "var1"};
        Main.main(testInstruction);
        assertEquals(input, Main.Variable.getVariableValue("var2")); // Check the result
    }

    @Test
    public void testProcessInstruction_mov() {
        String[] instruction = {"mov", "var3", "var1"};
        int value1 = 10;
        int value2 = 20;
        Main.Variable.setVariableValue("var1", value1);
        Main.Variable.setVariableValue("var3", value2);
        Main.main(instruction);
        assertEquals(value1, Main.Variable.getVariableValue("var1")); // Check the original value
        assertEquals(value2, Main.Variable.getVariableValue("var3")); // Check the destination value
    }

    @Test
    public void testProcessInstruction_cpy() {
        String[] instruction = {"cpy", "var3", "var1"};
        int value1 = 10;
        Main.Variable.setVariableValue("var1", value1);
        Main.main(instruction);
        assertEquals(value1, Main.Variable.getVariableValue("var3")); // Check the copied value
    }

    @Test
    public void testProcessInstruction_set() {
        String[] instruction = {"set", "var1", "5"};
        Main.Variable.setVariableValue("var1", 0);
        Main.main(instruction);
        assertEquals(5, Main.Variable.getVariableValue("var1")); // Check the set value
    }

    @Test
    public void testProcessInstruction_del() {
        String[] instruction = {"del", "var1"};
        Main.Variable.setVariableValue("var1", 10);
        Main.main(instruction);
        assertNull(Main.Variable.getVariableValue("var1")); // Check the deleted variable
    }

    @Test
    public void testProcessInstruction_arr() {
        String[] instruction = {"arr", "var4", "3"};
        Main.Variable.setVariableValue("var4", 0);
        Main.main(instruction);
        assertEquals(3, Main.Variable.getVariableCount()); // Check the number of array elements
        assertEquals(0, Main.Variable.getVariableValue("var4")); // Check the first element value
        assertEquals(0, Main.Variable.getVariableValue("var4_1")); // Check the second element value
        assertEquals(0, Main.Variable.getVariableValue("var4_2")); // Check the third element value
    }

    @Test
    public void testProcessInstruction_var() {
        String[] instruction = {"var", "var5"};
        Main.Variable.setVariableValue("var5", 10);
        Main.main(instruction);
        assertEquals(11, Main.Variable.getVariableCount()); // Check the number of variables
        assertEquals(10, Main.Variable.getVariableValue("var5")); // Check the variable value
    }

    @Test
    public void testProcessInstruction_inc() {
        String[] instruction = {"inc", "var1"};
        Main.Variable.setVariableValue("var1", 5);
        Main.main(instruction);
        assertEquals(6, Main.Variable.getVariableValue("var1")); // Check the incremented value
    }

    @Test
    public void testProcessInstruction_dec() {
        String[] instruction = {"dec", "var1"};
        Main.Variable.setVariableValue("var1", 10);
        Main.main(instruction);
        assertEquals(9, Main.Variable.getVariableValue("var1")); // Check the decremented value
    }

    @Test
    public void testProcessInstruction_add() {
        String[] instruction = {"add", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 10);
        Main.Variable.setVariableValue("var2", 5);
        Main.main(instruction);
        assertEquals(15, Main.Variable.getVariableValue("var1")); // Check the sum of variables
    }

    @Test
    public void testProcessInstruction_sub() {
        String[] instruction = {"sub", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 15);
        Main.Variable.setVariableValue("var2", 10);
        Main.main(instruction);
        assertEquals(5, Main.Variable.getVariableValue("var1")); // Check the difference of variables
    }

    @Test
    public void testProcessInstruction_mul() {
        String[] instruction = {"mul", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 15);
        Main.Variable.setVariableValue("var2", 5);
        Main.main(instruction);
        assertEquals(75, Main.Variable.getVariableValue("var1")); // Check the product of variables
    }

    @Test
    public void testProcessInstruction_div() {
        String[] instruction = {"div", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 30);
        Main.Variable.setVariableValue("var2", 5);
        Main.main(instruction);
        assertEquals(6, Main.Variable.getVariableValue("var1")); // Check the quotient of variables
    }

    @Test
    public void testProcessInstruction_mod() {
        String[] instruction = {"mod", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 35);
        Main.Variable.setVariableValue("var2", 5);
        Main.main(instruction);
        assertEquals(0, Main.Variable.getVariableValue("var1")); // Check the modulus of variables
    }

    @Test
    public void testProcessInstruction_xor() {
        String[] instruction = {"xor", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 10);
        Main.main(instruction);
        assertEquals(3, Main.Variable.getVariableValue("var1")); // Check the XOR of variables
    }

    @Test
    public void testProcessInstruction_or() {
        String[] instruction = {"or", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 3);
        Main.main(instruction);
        assertEquals(7, Main.Variable.getVariableValue("var1")); // Check the OR of variables
    }

    @Test
    public void testProcessInstruction_and() {
        String[] instruction = {"and", "var1", "var2", "5"};
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 3);
        Main.main(instruction);
        assertEquals(3, Main.Variable.getVariableValue("var1")); // Check the AND of variables
    }

    @Test
    public void testProcessInstruction_jmp() {
        String[] instruction = {"jmp", "label1"};
        String[] testInstruction = {"mov", "var1", "5", "jmp", "label2", "add", "var1", "var1", "1", "label1"};
        Main.Variable.setVariableValue("label1", 2);
        Main.Variable.setVariableValue("label2", 5);
        Main.Variable.setVariableValue("var1", 0);
        Main.main(testInstruction);
        assertEquals(10, Main.Variable.getVariableValue("var1")); // Check the destination of JMP
    }

    @Test
    public void testProcessInstruction_jz() {
        String[] instruction = {"jz", "label1"};
        String[] testInstruction = {"mov", "var1", "1", "jz", "label2", "mov", "var1", "var1", "1", "label1"};
        Main.Variable.setVariableValue("label1", 2);
        Main.Variable.setVariableValue("label2", 5);
        Main.Variable.setVariableValue("var1", 0);
        Main.main(testInstruction);
        assertEquals(1, Main.Variable.getVariableValue("var1")); // Check the destination of JZ
    }

    @Test
    public void testProcessInstruction_je() {
        String[] instruction = {"je", "label1"};
        String[] testInstruction = {"mov", "var1", "5", "mov", "var2", "3", "cmp", "var1", "var2", "label1", "mov", "var1", "var1", "1"};
        Main.Variable.setVariableValue("label1", 2);
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 3);
        Main.main(testInstruction);
        assertEquals(1, Main.Variable.getVariableValue("var1")); // Check the destination of JE
    }

    @Test
    public void testProcessInstruction_jne() {
        String[] instruction = {"jne", "label1"};
        String[] testInstruction = {"mov", "var1", "5", "mov", "var2", "3", "cmp", "var1", "var2", "jne", "label2", "mov", "var1", "var1", "1", "label1"};
        Main.Variable.setVariableValue("label1", 2);
        Main.Variable.setVariableValue("label2", 5);
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 3);
        Main.main(testInstruction);
        assertEquals(0, Main.Variable.getVariableValue("var1")); // Check the destination of JNE
    }

    @Test
    public void testProcessInstruction_cmp() {
        String[] instruction = {"cmp", "var1", "var2", "label1"};
        String[] testInstruction = {"mov", "var1", "5", "mov", "var2", "3", "cmp", "var1", "var2", "jz", "label2", "mov", "var1", "var1", "1", "label1", "mov", "var1", "var1", "1", "label2"};
        Main.Variable.setVariableValue("label1", 2);
        Main.Variable.setVariableValue("label2", 5);
        Main.Variable.setVariableValue("var1", 5);
        Main.Variable.setVariableValue("var2", 3);
        Main.main(testInstruction);
        assertEquals(0, Main.Variable.getVariableValue("var1")); // Check the destination of CMP
    }
}