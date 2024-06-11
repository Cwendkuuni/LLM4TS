import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import static org.junit.Assert.*;

public class LocalVariablesSorterTest {
    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(3, null));
        int originalVarIndex = 1;
        Type varType = Type.INT_TYPE;

        int remappedVarIndex = sorter.remap(originalVarIndex, varType);
        assertEquals(originalVarIndex + 1, remappedVarIndex);

        // Test with other data types
        for (int opcode = 21; opcode <= 57; opcode += 3) {
            varType = getTypeForOpcode(opcode);
            remappedVarIndex = sorter.remap(originalVarIndex, varType);
            assertEquals(originalVarIndex + 1 + (opcode >= 54 ? 1 : 0), remappedVarIndex);
        }
    }

    private Type getTypeForOpcode(int opcode) {
        switch (opcode) {
            case 21:
                return Type.INT_TYPE;
            case 22:
                return Type.LONG_TYPE;
            case 23:
                return Type.FLOAT_TYPE;
            case 24:
                return Type.DOUBLE_TYPE;
            case 54:
                return Type.INT_TYPE;
            case 55:
                return Type.LONG_TYPE;
            case 56:
                return Type.FLOAT_TYPE;
            case 57:
                return Type.DOUBLE_TYPE;
            default:
                throw new IllegalStateException("Invalid opcode: " + opcode);
        }
    }
}