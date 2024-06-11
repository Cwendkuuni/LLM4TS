import org.junit.Test;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;
import org.objectweb.asm.jip.ClassFile;
import org.objectweb.asm.MethodVisitor;

import static org.mockito.Mockito.*;

public class LocalVariablesSorterTest {

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", new MethodVisitor(17296) {
            @Override
            public void visitVarInsn(int opcode, int var) {
                verify(this.mockMethodVisitor, times(1)).visitVarInsn(opcode, argThat(new TypeMatcher() {
                    @Override
                    public boolean matches(Type type) {
                        return type.getSort() == ArgumentMatchers.anyInt();
                    }
                }, remap(var, Type.INT_TYPE)));
            }

            private int remap(int var, Type type) {
                LocalVariablesSorter localVariablesSorter = new LocalVariablesSorter(0, "", this);
                return localVariablesSorter.remap(var, type);
            }
        });

        Type[] types = new Type[1];
        types[0] = Type.INT_TYPE;

        ClassFile classFile = new ClassFile(0, "TestClass", null, null, null, null, null, null, null, null, null);
        MethodVisitor mv = mock(MethodVisitor.class);
        LocalVariablesSorter sorter = new LocalVariablesSorter(0, "", mv);
        sorter.visitVarInsn(21, 0); // int i
        sorter.visitVarInsn(21, 1); // int j
        sorter.visitVarInsn(22, 2); // long l
        sorter.visitVarInsn(24, 3); // float f
        sorter.visitVarInsn(23, 4); // float
        sorter.visitVarInsn(23, 4); // float
        sorter.visitVarInsn(56, 5); // float
        sorter.visitVarInsn(57, 6); // double
        sorter.visitVarInsn(55, 7); // long
        sorter.visitVarInsn(54, 8); // int
        sorter.visitVarInsn(54, 9); // int
        sorter.visitVarInsn(56, 10); // float
        sorter.visitVarInsn(56, 11); // float
        sorter.visitVarInsn(57, 12); // double
        sorter.visitVarInsn(23, 13); // float

        // Verify that the correct local variable index is remapped for each data type
        verify(mv, times(13)).visitVarInsn(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(types));
    }
}