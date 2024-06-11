import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.Opcodes;
import org.objectweb.asm.jip.Type;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "()V", new MethodVisitor(Opcodes.ASM5, null));

        Type intType = Type.INT_TYPE;
        Type longType = Type.LONG_TYPE;
        Type floatType = Type.FLOAT_TYPE;
        Type doubleType = Type.DOUBLE_TYPE;
        Type objectType = LocalVariablesSorter.OBJECT_TYPE;

        int index = 0;
        localVariablesSorter.visitVarInsn(Opcodes.ILOAD, index);
        int remappedIndex = localVariablesSorter.remap(index, intType);
        assertEquals(index, remappedIndex);

        index = 1;
        localVariablesSorter.visitVarInsn(Opcodes.LLOAD, index);
        long remappedIndexLong = localVariablesSorter.remap(index, longType);
        assertEquals(index, remappedIndexLong);

        index = 2;
        localVariablesSorter.visitVarInsn(Opcodes.FLOAD, index);
        float remappedIndexFloat = localVariablesSorter.remap(index, floatType);
        assertEquals(index, remappedIndexFloat);

        index = 3;
        localVariablesSorter.visitVarInsn(Opcodes.DLOAD, index);
        double remappedIndexDouble = localVariablesSorter.remap(index, doubleType);
        assertEquals(index, remappedIndexDouble);

        index = 4;
        localVariablesSorter.visitVarInsn(Opcodes.ALOAD, index);
        int remappedIndexObject = localVariablesSorter.remap(index, objectType);
        assertEquals(index, remappedIndexObject);
    }
}