import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UnionContextTest {

    @Mock
    private EvalContext context1;
    @Mock
    private EvalContext context2;
    @Mock
    private EvalContext parentContext;
    @Mock
    private BasicNodeSet nodeSet;

    private UnionContext unionContext;

    @Before
    public void setUp() {
        when(context1.nextSet()).thenReturn(true).thenReturn(false);
        when(context1.nextNode()).thenReturn(NodePointer.create(1)).thenReturn(NodePointer.create(2));

        when(context2.nextSet()).thenReturn(true).thenReturn(false);
        when(context2.nextNode()).thenReturn(NodePointer.create(3)).thenReturn(NodePointer.create(4));

        when(parentContext.getNodeSet()).thenReturn(nodeSet);

        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
    }

    @Test
    public void testGetDocumentOrder() {
        Mockito.verify(unionContext, Mockito.times(1)).getDocumentOrder();
        Mockito.verify(unionContext, Mockito.atLeastOnce()).getDocumentOrder();
        Mockito.verify(unionContext, Mockito.atLeastOnce()).getContexts().length > 1;
        Mockito.verifyNoMoreInteractions(unionContext);
    }

    @Test
    public void testSetPosition_whenBothContextsHaveNodes_returnsTrue() {
        Mockito.verify(unionContext, Mockito.times(1)).setPosition(0);
        Mockito.verify(unionContext, Mockito.times(1)).setPosition(1);
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(NodePointer.create(1));
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(NodePointer.create(2));

        Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(context2, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context2, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context2, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verifyNoMoreInteractions(unionContext);
        Mockito.verifyNoMoreInteractions(context1);
        Mockito.verifyNoMoreInteractions(context2);
    }

    @Test
    public void testSetPosition_whenBothContextsAreEmpty_returnsFalse() {
        when(context1.nextSet()).thenReturn(false).thenReturn(false);
        when(context2.nextSet()).thenReturn(false).thenReturn(false);

        Mockito.verify(unionContext, Mockito.times(1)).setPosition(0);
        Mockito.verify(unionContext, Mockito.times(1)).setPosition(1);
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer() == null;

        Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(context2, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context2, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context2, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(unionContext, Mockito.times(1)).getDocumentOrder();
        Mockito.verify(nodeSet, Mockito.times(1)).add(ArgumentMatchers.isA(NodePointer.class));
        Mockito.verifyNoMoreInteractions(unionContext);
        Mockito.verifyNoMoreInteractions(context1);
        Mockito.verifyNoMoreInteractions(context2);
    }

    @Test
    public void testSetPosition_whenFirstContextHasNodes_returnsTrue() {
        when(context1.nextSet()).thenReturn(true).thenReturn(false);
        when(context1.nextNode()).thenReturn(NodePointer.create(1)).thenReturn(NodePointer.create(2));
        when(context2.nextSet()).thenReturn(false).thenReturn(false);

        Mockito.verify(unionContext, Mockito.times(1)).setPosition(0);
        Mockito.verify(unionContext, Mockito.times(1)).setPosition(1);
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(NodePointer.create(1));
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(NodePointer.create(2));

        Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(context2, Mockito.times(1)).nextSet();
        Mockito.verify(context2, Mockito.times(0)).nextNode();
        Mockito.verify(context2, Mockito.times(0)).getCurrentNodePointer();

        Mockito.verifyNoMoreInteractions(unionContext);
        Mockito.verifyNoMoreInteractions(context1);
        Mockito.verifyNoMoreInteractions(context2);
    }

    @Test
    public void testSetPosition_whenFirstContextIsEmpty_returnsFalse() {
        when(context1.nextSet()).thenReturn(false).thenReturn(false);
        when(context2.nextSet()).thenReturn(true).thenReturn(false);

        Mockito.verify(unionContext, Mockito.times(1)).setPosition(0);
        Mockito.verify(unionContext, Mockito.times(1)).setPosition(1);
        Mockito.verify(unionContext, Mockito.times(1)).nextNode();
        Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer() == null;

        Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(context2, Mockito.atLeastOnce()).nextSet();
        Mockito.verify(context2, Mockito.atLeastOnce()).nextNode();
        Mockito.verify(context2, Mockito.times(2)).getCurrentNodePointer();

        Mockito.verify(unionContext, Mockito.times(1)).getDocumentOrder();
        Mockito.verify(nodeSet, Mockito.times(1)).add(ArgumentMatchers.isA(NodePointer.class));
        Mockito.verifyNoMoreInteractions(unionContext);
        Mockito.verifyNoMoreInteractions(context1);
        Mockito.verifyNoMoreInteractions(context2);
    }

    @ParameterizedTest
    @MethodSource("positivePositionValuesProvider")
    public void testSetPosition_withValidPosition_setsPositionCorrectly(int position) {
        when(context1.nextSet()).thenReturn(true).thenReturn(false);
        when(context1.nextNode()).thenReturn(NodePointer.create(1)).thenReturn(NodePointer.create(2));
        when(context2.nextSet()).thenReturn(true).thenReturn(false);
        when(context2.nextNode()).thenReturn(NodePointer.create(3)).thenReturn(NodePointer.create(4));

        Mockito.verify(unionContext, Mockito.times(1)).setPosition(position);

        if (position == 0) {
            Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
            Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
            Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

            Mockito.verify(context2, Mockito.atLeastOnce()).nextSet();
            Mockito.verify(context2, Mockito.atLeastOnce()).nextNode();
            Mockito.verify(context2, Mockito.times(2)).getCurrentNodePointer();

            Mockito.verify(unionContext, Mockito.times(1)).nextNode();
            Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(position == 0 ? NodePointer.create(1) : NodePointer.create(3));
        } else if (position == 1) {
            Mockito.verify(context1, Mockito.atLeastOnce()).nextSet();
            Mockito.verify(context1, Mockito.atLeastOnce()).nextNode();
            Mockito.verify(context1, Mockito.times(2)).getCurrentNodePointer();

            Mockito.verify(context2, Mockito.atLeastOnce()).nextSet();
            Mockito.verify(context2, Mockito.atLeastOnce()).nextNode();
            Mockito.verify(context2, Mockito.times(2)).getCurrentNodePointer();

            Mockito.verify(unionContext, Mockito.times(1)).nextNode();
            Mockito.verify(unionContext, Mockito.times(1)).getCurrentNodePointer().equals(position == 0 ? NodePointer.create(2) : NodePointer.create(4));
        } else {
            Mockito.verify(unionContext, Mockito.times(1)).getDocumentOrder();
            Mockito.verify(nodeSet, Mockito.times(1)).add(ArgumentMatchers.isA(NodePointer.class));
            Mockito.verifyNoMoreInteractions(unionContext);
            Mockito.verifyNoMoreInteractions(context1);
            Mockito.verifyNoMoreInteractions(context2);
        }
    }

    private Collection<Object[]> positivePositionValuesProvider() {
        return Arrays.asList(new Object[][]{
                new Object[]{0},
                new Object[]{1},
                new Object[]{2},
                new Object[]{3},
                new Object[]{4}
        });
    }
}