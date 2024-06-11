import org.junit.Assert;
import org.junit.Test;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.commons.LocalVariablesSorter;

public class LocalVariablesSorterTest {

    @Test
    public void testConstructor() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        Assert.assertNotNull(sorter);
    }

    @Test
    public void testVisitVarInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        Type typeInt = Type.INT_TYPE;
        Type typeLong = Type.LONG_TYPE;
        Type typeFloat = Type.FLOAT_TYPE;
        Type typeDouble = Type.DOUBLE_TYPE;
        Type typeObject = Type.getObjectType("java/lang/Object");

        sorter.visitVarInsn(Opcodes.ILOAD, 0);
        Assert.assertEquals(0, sorter.mapping[0]);
        Assert.assertEquals(typeInt, sorter.newLocals[0]);

        sorter.visitVarInsn(Opcodes.LLOAD, 1);
        Assert.assertEquals(1, sorter.mapping[1]);
        Assert.assertEquals(typeLong, sorter.newLocals[1]);

        sorter.visitVarInsn(Opcodes.FLOAD, 2);
        Assert.assertEquals(2, sorter.mapping[2]);
        Assert.assertEquals(typeFloat, sorter.newLocals[2]);

        sorter.visitVarInsn(Opcodes.DLOAD, 3);
        Assert.assertEquals(3, sorter.mapping[3]);
        Assert.assertEquals(typeDouble, sorter.newLocals[3]);

        sorter.visitVarInsn(Opcodes.ALOAD, 4);
        Assert.assertEquals(4, sorter.mapping[4]);
        Assert.assertEquals(typeObject, sorter.newLocals[4]);

        sorter.visitVarInsn(Opcodes.ILOAD, 5);
        Assert.assertEquals(5, sorter.mapping[5]);
        Assert.assertEquals(typeInt, sorter.newLocals[5]);
    }

    @Test
    public void testVisitIincInsn() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        Type typeInt = Type.INT_TYPE;

        sorter.visitIincInsn(0, 1);
        Assert.assertEquals(1, sorter.mapping[0]);
        Assert.assertEquals(typeInt, sorter.newLocals[0]);

        sorter.visitIincInsn(1, 2);
        Assert.assertEquals(2, sorter.mapping[1]);
        Assert.assertEquals(typeInt, sorter.newLocals[1]);
    }

    @Test
    public void testVisitMaxs() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        int maxLocals = 5;

        sorter.visitMaxs(5, maxLocals);
        Assert.assertEquals(maxLocals, sorter.nextLocal);

        sorter.visitMaxs(3, maxLocals);
        Assert.assertEquals(maxLocals, sorter.nextLocal);
    }

    @Test
    public void testVisitLocalVariable() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        String name = "test";
        Type typeInt = Type.INT_TYPE;
        Type typeLong = Type.LONG_TYPE;
        Type typeFloat = Type.FLOAT_TYPE;
        Type typeDouble = Type.DOUBLE_TYPE;
        Type typeObject = Type.getObjectType("java/lang/Object");

        sorter.visitLocalVariable(name, typeInt.getDescriptor(), null, new Label(), new Label(), 0);
        Assert.assertEquals(0, sorter.mapping[0]);
        Assert.assertEquals(typeInt, sorter.newLocals[0]);

        sorter.visitLocalVariable(name, typeLong.getDescriptor(), null, new Label(), new Label(), 1);
        Assert.assertEquals(1, sorter.mapping[1]);
        Assert.assertEquals(typeLong, sorter.newLocals[1]);

        sorter.visitLocalVariable(name, typeFloat.getDescriptor(), null, new Label(), new Label(), 2);
        Assert.assertEquals(2, sorter.mapping[2]);
        Assert.assertEquals(typeFloat, sorter.newLocals[2]);

        sorter.visitLocalVariable(name, typeDouble.getDescriptor(), null, new Label(), new Label(), 3);
        Assert.assertEquals(3, sorter.mapping[3]);
        Assert.assertEquals(typeDouble, sorter.newLocals[3]);

        sorter.visitLocalVariable(name, typeObject.getInternalName(), null, new Label(), new Label(), 4);
        Assert.assertEquals(4, sorter.mapping[4]);
        Assert.assertEquals(typeObject, sorter.newLocals[4]);
    }

    @Test
    public void testVisitFrame() {
        LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
        Object[] local = new Object[10];
        Type typeInt = Type.INT_TYPE;
        Type typeLong = Type.LONG_TYPE;

        sorter.visitFrame(Opcodes.FRAME_CODE, 5, new Object[]{}, 5, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP});
        Assert.assertArrayEquals(new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, sorter.newLocals);

        for (int i = 0; i < 5
; 
i++) {
local[i] = i + 1;
sorter.visitLocalVariable("local" + i, typeInt.getDescriptor(), null, new Label(), new Label(), i);
}

sorter.visitFrame(Opcodes.FRAME_CODE, 5, local, 5, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP});
Assert.assertArrayEquals(local, sorter.newLocals);

for (int i = 0; i < 5; i++) {
int local = sorter.remap(i, typeInt);
Assert.assertEquals(i, local);
}
}

@Test
public void testNewLocal() {
LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
Type typeInt = Type.INT_TYPE;
Type typeLong = Type.LONG_TYPE;
Type typeFloat = Type.FLOAT_TYPE;
Type typeDouble = Type.DOUBLE_TYPE;

int localInt = sorter.newLocal(typeInt);
Assert.assertEquals(1, localInt);

int localLong = sorter.newLocal(typeLong);
Assert.assertEquals(2, localLong);

int localFloat = sorter.newLocal(typeFloat);
Assert.assertEquals(3, localFloat);

int localDouble = sorter.newLocal(typeDouble);
Assert.assertEquals(4, localDouble);

String localObject = "test";
int localObjectMapping = sorter.newLocalMapping(Type.getObjectType(localObject));
Assert.assertEquals(5, localObjectMapping);
}

@Test(expected = IllegalStateException.class)
public void testVisitFrameWithChangedFlag() {
LocalVariablesSorter sorter = new LocalVariablesSorter(0x0001, "()V", new MethodVisitor(Opcodes.ASM5));
sorter.changed = true;
sorter.visitFrame(Opcodes.FRAME_CODE, 5, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP}, 5, new Object[]{Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP, Opcodes.TOP});
}
}