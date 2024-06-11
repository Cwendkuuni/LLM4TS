import com.google.javascript.rhino.jstype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

 @Test
 public void testGetAlternates() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertEquals(ImmutableSet.of(StringType.getInstance(), BooleanType.getInstance()), unionType.getAlternates());
 }

 @Test
 public void testMatchesNumberContext() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertFalse(unionType.matchesNumberContext());
 }

 @Test
 public void testMatchesStringContext() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertTrue(unionType.matchesStringContext());
 }

 @Test
 public void testMatchesObjectContext() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertTrue(unionType.matchesObjectContext());
 }

 @Test
 public void testFindPropertyType() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 ObjectType.getInstance(), NumberType.getInstance()));
 assertNull(unionType.findPropertyType("foo"));
 }

 @Test
 public void testCanAssignTo() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 UnionType anotherUnionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 NumberType.getInstance(), BooleanType.getInstance()));
 assertTrue(unionType.canAssignTo(anotherUnionType));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCannotAssignToDifferentTypes() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 NumberType numberType = NumberType.getInstance();
 assertFalse(unionType.canAssignTo(numberType));
 }

 @Test
 public void testCanBeCalled() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertTrue(unionType.canBeCalled());
 }

 @Test(expected = IllegalStateException.class)
 public void testCannotBeCalledOnVoidType() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 VoidType.getInstance()));
 assertFalse(unionType.canBeCalled());
 }

 @Test
 public void testRestrictByNotNullOrUndefined() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 ObjectType.getInstance(), NullType.getInstance()));
 UnionType restrictedType = unionType.restrictByNotNullOrUndefined();
 assertEquals(NullType.getInstance(), restrictedType);
 }

 @Test
 public void testTestForEquality() {
 UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 assertEquals(TernaryValue.TRUE, unionType1.testForEquality(unionType2));
 }

 @Test
 public void testIsNullable() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 ObjectType.getInstance(), NullType.getInstance()));
 assertTrue(unionType.isNullable());
 }

 @Test
 public void testIsUnknownType() {
 UnionType unionType = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 ObjectType.getInstance(), NullType.getInstance()));
 assertFalse(unionType.isUnknownType());
 }

 @Test
 public void testGetLeastSupertype() {
 UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 BooleanType.getInstance(), StringType.getInstance()));
 UnionType leastSupertype = unionType1.getLeastSupertype(unionType2);
 assertEquals(unionType1, leastSupertype);
 }

 @Test
 public void testMeet() {
 UnionType unionType1 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), BooleanType.getInstance()));
 UnionType unionType2 = new UnionType(new JSTypeRegistry(), ImmutableSet.of(
 StringType.getInstance(), NumberType.getInstance()));
 UnionType meetResult = unionType1.meet(unionType2);
 UnionType expectedResult = new UnionType(new JSTypeRegistry(), ImmutableSet.of(StringType.getInstance()));
 assertEquals(expectedResult, meetResult);
 }
}