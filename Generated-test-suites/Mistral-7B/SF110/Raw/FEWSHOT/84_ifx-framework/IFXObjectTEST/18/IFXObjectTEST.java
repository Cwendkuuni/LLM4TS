package org.sourceforge.ifx.basetypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

@RunWith(Parameterized.class)
public class IFXObjectTest {

    private Class clazz;
    private IFXObject obj1;
    private Object obj2;
    private boolean expected;

    public IFXObjectTest(Class clazz, IFXObject obj1, Object obj2, boolean expected) {
        this.clazz = clazz;
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Object[] data() {
        return new Object[][]{
            {IFXObject.class, new IFXObject(), null, false},
            {IFXObject.class, new IFXObject(), new IFXObject(), true},
            {IFXObject.class, new IFXObject(), new Integer(0), false},
            {String.class, new String("foo"), new String("foo"), true},
            {Integer[].class, new Integer[]{1, 2}, new Integer[]{1, 2}, true},
            {Integer[].class, new Integer[]{1, 2}, new Integer[]{1, 3}, false},
            {IFXObject.class, new IFXObject(), new IFXObject(){
                @Override
                public String toString() {
                    return "bar";
                }
            }, false},
            {IFXObject.class, new IFXObject(){
                @Override
                public String toString() {
                    return "bar";
                }
            }, new IFXObject(), false},
            {IFXObject.class, new IFXObject(), new IFXObject[]{new Integer[]{1, 2}, null}, false},
            {IFXObject.class, new IFXObject[]{new Integer[]{1, 2}, null}, new IFXObject[]{new Integer[]{1, 2}, null}, true},
            {IFXObject.class, new IFXObject[]{new Integer[]{1, 2}, null}, new Object[]{new Integer[]{1, 2}, null}, false},
            {IFXObject.class, new Object[]{new Integer[]{1, 2}, null}, new IFXObject[]{new Integer[]{1, 2}, null}, true}
        };
    }

    @Test
    public void testEquals() {
        assertEquals(expected, obj1.equals(obj2));
    }

    @Test
    public void testHashCode() {
        int hashCode1 = obj1.hashCode();
        int hashCode2 = obj2.hashCode();
        if (expected) {
            assertEquals(hashCode1, hashCode2);
        } else {
            assertNotSame(hashCode1, hashCode2);
        }
    }

    @Test
    public void testToString() {
        String toString1 = obj1.toString();
        String toString2 = obj2.toString();
        if (expected) {
            assertEquals(toString1, toString2);
        } else {
            assertNotSame(toString1, toString2);
        }
    }

    @Test
    public void testStringify() throws Exception {
        String stringifiedObject1 = this.stringify(obj1, 0);
        if (obj2 != null) {
            String stringifiedObject2 = this.stringify(obj2, 0);
            if (expected) {
                assertEquals(stringifiedObject1, stringifiedObject2);
            } else {
                assertNotSame(stringifiedObject1, stringifiedObject2);
            }
        } else {
            assertNull(stringifiedObject2);
        }
    }

    private Object stringify(IFXObject obj, int level) throws Exception {
        StringBuffer buf = new StringBuffer();
        buf.append(obj.getClass().getName()).append(" = {");
        Method[] methods = obj.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (!methodName.equals("getClass") && methodName.startsWith("get")) {
                String memberName = methodName.substring(3);
                buf.append("\n").append(this.tab(level)).append(memberName).append(" = ");
                Object retObj = method.invoke(obj, (Object[])null);
                if (retObj == null) {
                    buf.append("NULL");
                } else if (retObj.getClass().isArray()) {
                    int numObjs = Array.getLength(retObj);
                    buf.append("[\n");
                    ++level;
                    for (int i = 0; i < numObjs; ++i) {
                        if (i > 0) {
                            buf.append(",\n");
                        }
                        buf.append(this.tab(level)).append("[").append(i).append("]:");
                        Object arrayObj = Array.get(retObj, i);
                        buf.append(this.stringify((IFXObject)arrayObj, level));
                    }
                    --level;
                    buf.append("\n").append(this.tab(level)).append("]");
                } else if (retObj instanceof IFXObject) {
                    buf.append(this.stringify((IFXObject)retObj, level));
                } else if (retObj instanceof IBaseType) {
                    buf.append(((IBaseType)retObj).getString());
                } else {
                    buf.append("Object");
                }
            }
        }
        --level;
        buf.append("\n").append(this.tab(level)).append("}");
        return buf.toString();
    }

    private String tab(int level) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < level; ++i) {
            buf.append("  ");
        }
        return buf.toString();
    }
}