import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CompareToBuilderTest {

 private CompareToBuilder lhsBuilder;
 private CompareToBuilder rhsBuilder;
 private int expectedComparison;

 @Parameters
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test cases for reflectionCompare method
 new Object[]{new Integer(1), new Integer(1), 0},
 new Object[]{new Integer(1), new Integer(2), -1},
 new Object[]{new Integer(1), null, Integer.MIN_VALUE},
 new Object[]{null, new Integer(1), Integer.MAX_VALUE},
 new Object[]{new Integer(1), new Integer(1), null},
 new Object[]{new Integer[]{1, 2}, new Integer[]{1, 2}, 0},
 new Object[]{new Integer[]{1, 2}, new Integer[]{1, 3}, -1},
 new Object[]{new Integer[]{1, 2}, null, Integer.MIN_VALUE},
 new Object[]{null, new Integer[]{1, 2}, Integer.MAX_VALUE},
 new Object[]{new Integer[]{1, null}, new Integer[]{1, 2}, -1},
 // Test cases for append method for different types
 new Object[]{new Integer(1), new Integer(2), null},
 new Object[]{new Long(1), new Long(2), null},
 new Object[]{new Short("1"), new Short("2"), null},
 new Object[]{new Character('1'), new Character('2'), null},
 new Object[]{new Byte((byte)1), new Byte((byte)2), null},
 new Object[]{new Double(1.0), new Double(2.0), null},
 new Object[]{new Float(1.0f), new Float(2.0f), null},
 new Object[]{new Boolean(true), new Boolean(false), null},
 // Test cases for append method with Comparator
 new Object[]{new Integer(1), new Integer(2), Comparators.naturalOrder()},
 new Object[]{new Double(1.0), new Double(2.0), Comparators.reverseOrder()},
 new Object[]{new Character('a'), new Character('b'), Comparator.comparingInt(Character::toString)}
 });
 }

 @Before
 public void setUp() {
 lhsBuilder = new CompareToBuilder();
 rhsBuilder = new CompareToBuilder();
 }

 @Test
 public void testReflectionCompare() {
 reflectionCompareTest(CompareToBuilder.class.getMethod("reflectionCompare", Object.class, Object.class), lhsBuilder, rhsBuilder, expectedComparison);
 }

 @Test
 public void testReflectionCompareWithCompareTransients() {
 reflectionCompareTest(CompareToBuilder.class.getMethod("reflectionCompare", Object.class, Object.class, boolean.class), lhsBuilder, rhsBuilder, expectedComparison);
 }

 @Test
 public void testReflectionCompareWithExcludeFields() {
 reflectionCompareTest(CompareToBuilder.class.getMethod("reflectionCompare", Object.class, Object.class, Collection.class, String[].class), lhsBuilder, rhsBuilder, expectedComparison);
 }

 @Test
 public void testReflectionCompareWithExcludeFieldsAndCompareTransients() {
 reflectionCompareTest(CompareToBuilder.class.getMethod("reflectionCompare", Object.class, Object.class, boolean.class, Collection.class, String[].class), lhsBuilder, rhsBuilder, expectedComparison);
 }

 private void reflectionCompareTest(Method method, CompareToBuilder lhsBuilder, CompareToBuilder rhsBuilder, int expectedComparison) throws IllegalAccessException, InvocationTargetException {
 setFieldsValue(lhsBuilder, "comparison", 0);
 setFieldsValue(rhsBuilder, "comparison", 0);
 method.invoke(null, lhsBuilder, rhsBuilder);
 assertEquals(expectedComparison, lhsBuilder.toComparison());
 }

 private void setFieldsValue(Object obj, String fieldName, Object value) throws IllegalAccessException {
 Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
 field.set(obj, value);
 }
}