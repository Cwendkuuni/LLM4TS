import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import umd.cs.shop.JSPredicateForm;
import umd.cs.shop.JSTerm;
import umd.cs.shop.JSSubstitution;

public class JSPredicateFormTest {

 private JSPredicateForm pf1;
 private JSPredicateForm pf2;
 private JSPredicateForm pf3;
 private JSTerm t1;
 private JSTerm t2;

 @Before
 public void setup() {
 pf1 = new JSPredicateForm("(not (or a b))");
 pf2 = new JSPredicateForm("(not (or a b c))");
 pf3 = new JSPredicateForm("(not (or a b))");
 t1 = new JSTerm("(not a)");
 t2 = new JSTerm("b");
 }

 @Test
 public void testEmptyConstructor() {
 JSPredicateForm empty = new JSPredicateForm();
 assertNull(empty.elementAt(0));
 assertNull(empty.elementAt(1));
 assertNull(empty.elementAt(2));
 }

 @Test
 public void testStringConstructor() {
 assertEquals(pf1, pf1.clonePF());
 assertEquals(pf1, pf1.applySubstitutionPF(new JSSubstitution()));
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructor1() {
 new JSPredicateForm(null);
 }

 @Test(expected = JSParserError.class)
 public void testInvalidConstructor2() {
 new JSPredicateForm(new StringReader(""));
 }

 @Test
 public void testValidConstructor1() {
 assertEquals("(not (or a b))", pf1.toStr().toString());
 }

 @Test
 public void testValidConstructor2() {
 assertEquals("(not (or a b))", pf1.print().toString());
 }

 @Test
 public void testValidConstructor3() {
 assertEquals("(not (or a b))", pf1.toStr().toString());
 }

 @Test
 public void testValidConstructor4() {
 assertEquals("(not (or a b c))", pf2.toStr().toString());
 }

 @Test
 public void testEquals() {
 assertEquals(pf1, pf3);
 assertFalse(pf1.equals(pf2));
 }

 @Test
 public void testStandarizerPredicateForm() {
 assertEquals("(not (or (not a) b))", pf1.standarizerPredicateForm().toStr().toString());
 }

 @Test
 public void testMatches() {
 JSSubstitution beta = pf1.matches(pf3);
 assertEquals(new JSSubstitution(), beta);
 }

 @Test
 public void testMatches1() {
 JSSubstitution beta = pf1.matches(new JSPredicateForm("(or a b)"));
 assertFalse(beta.fail());
 }

 @Test
 public void testMatches2() {
 JSSubstitution beta = pf1.matches(new JSPredicateForm("(not (or a b))"));
 assertEquals(new JSSubstitution(), beta);
 }

 @Test
 public void testMatches3() {
 JSSubstitution beta = pf1.matches(new JSPredicateForm("(not (or a c))"));
 assertFalse(beta.fail());
 }

 @Test
 public void testMatches4() {
 JSSubstitution beta = pf1.matches(new JSPredicateForm("(not (or a b d))"));
 assertFalse(beta.fail());
 }

 @Test
 public void testMatches5() {
 JSSubstitution beta = pf1.matches(new JSPredicateForm("(or a b)"));
 assertFalse(beta.fail());
 beta.print();
 }

 @Test
 public void testAddElementNot() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("not");
 pf4.addElement(pf3);
 assertEquals("(not (or a b))", pf4.toStr().toString());
 }

 @Test
 public void testAddElementCall() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("call");
 pf4.addElement(t1);
 pf4.addElement(t2);
 assertEquals("(call (not a) b)", pf4.toStr().toString());
 }

 @Test
 public void testAddElementFunction() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("or");
 pf4.addElement(t1);
 pf4.addElement(t2);
 assertEquals("(or (not a) b)", pf4.toStr().toString());
 }

 @Test(expected = JSParserError.class)
 public void testInvalidAddElement1() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("!");
 pf4.addElement("not");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidAddElement2() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("not");
 pf4.addElement("!");
 }

 @Test(expected = JSParserError.class)
 public void testInvalidAddElement3() {
 JSPredicateForm pf4 = new JSPredicateForm();
 pf4.addElement("not");
 pf4.addElement("not");
 }

 @Test
 public void testApplySubstitutionPF() {
 JSSubstitution beta = pf1.applySubstitutionPF(new JSSubstitution());
 assertNull(beta.elements());
 }

 @Test
 public void testApplySubstitutionPF1() {
 JSSubstitution beta = pf1.applySubstitutionPF(new JSSubstitution().assign("a", "b"));
 assertEquals("(not (or b b))", pf1.applySubstitutionPF(new JSSubstitution().assign("a", "b")).toStr().toString());
 }

 @Test
 public void testApplySubstitutionPF2() {
 JSSubstitution beta = pf1.applySubstitutionPF(new JSSubstitution().assign("b", "c"));
 assertEquals("(not (or a c))", pf1.applySubstitutionPF(new JSSubstitution().assign("b", "c")).toStr().toString());
 }

 @Test
 public void testApplySubstitutionPF3() {
 JSSubstitution beta = pf1.applySubstitutionPF(new JSSubstitution().assign("a", "d").assign("b", "e"));
 assertEquals("(not (or d e))", pf1.applySubstitutionPF(new JSSubstitution().assign("a", "d").assign("b", "e")).toStr().toString());
 }

 @Test
 public void testClonePF() {
 assertEquals("(not (or a b))", pf1.clonePF().toStr().toString());
 }

 @Test
 public void testClonePF1() {
 JSSubstitution beta = new JSSubstitution().assign("a", "d").assign("b", "e");
 assertEquals("(not (or d e))", pf1.applySubstitutionPF(beta).toStr().toString());
 assertEquals("(not (or d e))", pf1.clonePF().applySubstitutionPF(beta).toStr().toString());
 }
}