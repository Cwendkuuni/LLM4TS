import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.JXPathContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.loader.Compilable;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

 @Test
 public void testLessThanWithDoubleValues() {
 Compilable compilable = new Compilable() {
 @Override
 public Object compile(JXPathContext context, Path path) {
 return new CoreOperationLessThan(context.getSource().getExpression(0), context.getSource().getExpression(1));
 }
 };
 InfoSet infoSet = new InfoSet();
 NodeInfo nodeInfo = new NodeInfo("5.5");
 infoSet.addNode(nodeInfo);
 EvalContext context = new EvalContext(new JXPathContext(infoSet));
 context.setThis(nodeInfo);
 Object result = compilable.compile(context, null).computeValue(context);
 assertTrue(result instanceof Boolean);
 assertEquals(Boolean.TRUE, result);

 nodeInfo.setValue("7.5");
 context.setThis(nodeInfo);
 result = compilable.compile(context, null).computeValue(context);
 assertFalse(result instanceof Boolean);
 assertEquals(Boolean.FALSE, result);
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithMixedTypes() {
 Compilable compilable = new Compilable() {
 @Override
 public Object compile(JXPathContext context, Path path) {
 return new CoreOperationLessThan(context.getSource().getExpression(0), context.getSource().getExpression(1));
 }
 };
 InfoSet infoSet = new InfoSet();
 NodeInfo nodeInfoDouble = new NodeInfo("5.5");
 infoSet.addNode(nodeInfoDouble);
 NodeInfo nodeInfoInt = new NodeInfo("7");
 infoSet.addNode(nodeInfoInt);
 EvalContext context = new EvalContext(new JXPathContext(infoSet));
 context.setThis(nodeInfoDouble);
 Object result = compilable.compile(context, null).computeValue(context);
 fail("Expected ClassCastException");
 }

 @Test(expected = ClassCastException.class)
 public void testLessThanWithNullValues() {
 Compilable compilable = new Compilable() {
 @Override
 public Object compile(JXPathContext context, Path path) {
 return new CoreOperationLessThan(context.getSource().getExpression(0), context.getSource().getExpression(1));
 }
 };
 InfoSet infoSet = new InfoSet();
 NodeInfo nodeInfoLeft = new NodeInfo(null);
 NodeInfo nodeInfoRight = new NodeInfo(null);
 infoSet.addNode(nodeInfoLeft);
 infoSet.addNode(nodeInfoRight);
 EvalContext context = new EvalContext(new JXPathContext(infoSet));
 context.setThis(nodeInfoLeft);
 Object result = compilable.compile(context, null).computeValue(context);
 fail("Expected ClassCastException");
 }
}