import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual_Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest extends CoreOperationEqual_Test {

 @Test
 public void testEqualWithBooleanValues() {
 Expression arg1 = new SimpleNode(Boolean.TRUE);
 Expression arg2 = new SimpleNode(Boolean.TRUE);
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqualWithBooleanFalseAndBooleanTrue() {
 Expression arg1 = new SimpleNode(Boolean.FALSE);
 Expression arg2 = new SimpleNode(Boolean.TRUE);
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testEqualWithIntegerValues() {
 Expression arg1 = new SimpleNode(1);
 Expression arg2 = new SimpleNode(1);
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqualWithIntegerValuesDifferent() {
 Expression arg1 = new SimpleNode(1);
 Expression arg2 = new SimpleNode(2);
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test
 public void testEqualWithStringValues() {
 Expression arg1 = new SimpleNode("test");
 Expression arg2 = new SimpleNode("test");
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }

 @Test
 public void testEqualWithStringValuesDifferent() {
 Expression arg1 = new SimpleNode("test");
 Expression arg2 = new SimpleNode("different");
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 Object result = coreOpEqual.computeValue(jxpathContext);
 assertFalse(result instanceof Boolean);
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualWithNullArg1() {
 Expression arg1 = null;
 Expression arg2 = new SimpleNode(1);
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 coreOpEqual.computeValue(jxpathContext);
 // this test should throw an exception
 }

 @Test(expected = IllegalStateException.class)
 public void testEqualWithNullArg2() {
 Expression arg1 = new SimpleNode(1);
 Expression arg2 = null;
 CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
 JXPathContext jxpathContext = createContext();
 coreOpEqual.computeValue(jxpathContext);
 // this test should throw an exception
 }
}