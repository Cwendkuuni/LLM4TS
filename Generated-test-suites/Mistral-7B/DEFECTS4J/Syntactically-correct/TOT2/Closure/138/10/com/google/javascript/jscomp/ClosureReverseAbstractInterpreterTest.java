import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ChainableReverseAbstractInterpreter.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import java.util.Map;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

    @Test
    public void testGetPreciserScopeKnowingConditionOutcome() {
        ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(null, null);

        Node condition = createNode("goog.isDef(x)", 0);
        FlowScope blindScope = new FlowScope();
        boolean outcome = true;

        assertSame(interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome), interpreter.getChildFlowScope(blindScope));

        Node conditionFalse = createNode("goog.isDef(y)", 0);
        outcome = false;

        assertNull(interpreter.getPreciserScopeKnowingConditionOutcome(conditionFalse, blindScope, outcome));
    }

    private Node createNode(String nodeString, int lineNumber) {
        return new Node(LINE_COMMENT, 0, new NodeToken(NODE_EXPRESSION_START),
                new NodeToken(NAME, nodeString), new NodeToken(SEMICOLON),
                new NodeList(), new NodeList(), new NodePosition(0, 0, lineNumber));
    }
}