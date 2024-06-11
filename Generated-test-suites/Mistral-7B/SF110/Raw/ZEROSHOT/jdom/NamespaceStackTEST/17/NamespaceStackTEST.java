import org.jdom2.Namespace;
import org.jdom2.internal.ArrayCopy;
import org.jdom2.internal.EmptyIterable;
import org.jdom2.internal.NamespaceComparator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackEmpty() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertArrayEquals(NamespaceStack.EMPTY, stack.getScope());
        Assert.assertEquals(0, stack.depth);
        Assert.assertSame(NamespaceStack.EMPTYITER, stack.addedForward());
        Assert.assertSame(NamespaceStack.EMPTYITER, stack.addedReverse());
        Assert.assertSame(NamespaceStack.EMPTYITER, stack.iterator());
    }

    @Test
    public void testNamespaceStackWithDefaultSeed() {
        NamespaceStack stack = new NamespaceStack(NamespaceStack.DEFAULTSEED);
        Assert.assertArrayEquals(NamespaceStack.DEFAULTSEED, stack.getScope());
        Assert.assertEquals(1, stack.depth);
    }

    @Test
    public void testNamespaceStackPushElement() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace = new Namespace("http://example.com/test");

        Element element = new Element("test", "root");
        element.setNamespace(stackNamespace);
        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);

        Namespace[] expectedNewScope = new Namespace[]{stackNamespace, addedNamespace};
        Assert.assertArrayEquals(expectedNewScope, stack.scope[stack.depth]);
        Assert.assertArrayEquals(new Namespace[]{addedNamespace}, stack.added[stack.depth]);
        Assert.assertEquals(2, stack.depth);
    }

    @Test
    public void testNamespaceStackPushElementWithAdditionalNamespaces() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace1 = new Namespace("http://example.com/test1");
        Namespace addedNamespace2 = new Namespace("http://example.com/test2");

        Element element = new Element("test", "root");
        element.setNamespace(stackNamespace);
        element.setAdditionalNamespaces(Arrays.asList(addedNamespace1, addedNamespace2));

        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);

        Namespace[] expectedNewScope = new Namespace[]{stackNamespace, addedNamespace1, addedNamespace2};
        Assert.assertArrayEquals(expectedNewScope, stack.scope[stack.depth]);
        Assert.assertArrayEquals(new Namespace[]{addedNamespace1, addedNamespace2}, stack.added[stack.depth]);
        Assert.assertEquals(3, stack.depth);
    }

    @Test
    public void testNamespaceStackPushAttribute() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace = new Namespace("http://example.com/test");

        Attribute attribute = new Attribute("test", "attr");
        attribute.setNamespace(stackNamespace);

        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);

        Namespace[] expectedNewScope = new Namespace[]{stackNamespace, addedNamespace};
        Assert.assertArrayEquals(expectedNewScope, stack.scope[stack.depth]);
        Assert.assertArrayEquals(new Namespace[]{addedNamespace}, stack.added[stack.depth]);
        Assert.assertEquals(2, stack.depth);
    }

    @Test
    public void testNamespaceStackPop() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace = new Namespace("http://example.com/test");

        Element element = new Element("test", "root");
        element.setNamespace(stackNamespace);
        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);

        stack.pop();

        Assert.assertEquals(1, stack.depth);
        Assert.assertArrayEquals(new Namespace[]{stackNamespace}, stack.scope[stack.depth]);
        Assert.assertSame(NamespaceStack.EMPTY, stack.added[stack.depth]);
    }

    @Test(expected = IllegalStateException.class)
    public void testNamespaceStackOverpop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
        stack.pop();
    }

    @Test
    public void testNamespaceStackAddedForwardIterator() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace1 = new Namespace("http://example.com/test1");
        Namespace addedNamespace2 = new Namespace("http://example.com/test2");

        Element element = new Element("test", "root");
        element.setNamespace(stackNamespace);
        element.setAdditionalNamespaces(Arrays.asList(addedNamespace1, addedNamespace2));

        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);

        Iterator<Namespace> iterator = stack.addedForward();
        Assert.assertTrue(iterator.hasNext());
        Namespace next = iterator.next();
        Assert.assertSame(addedNamespace1, next);

        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertSame(addedNamespace2, next);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNamespaceStackAddedReverseIterator() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace1 = new Namespace("http://example.com/test1");
        Namespace addedNamespace2 = new Namespace("http://example.com/test2");

        Element element = new Element("test", "root");
        element.setNamespace(stackNamespace);
        element.setAdditionalNamespaces(Arrays.asList(addedNamespace1, addedNamespace2));

        List<Namespace> toAdd = new ArrayList<>();

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);

        Iterator<Namespace> iterator = stack.addedReverse();
        Assert.assertTrue(iterator.hasNext());
        Namespace next = iterator.next();
        Assert.assertSame(addedNamespace2, next);

        Assert.assertTrue(iterator.hasNext());
        next = iterator.next();
        Assert.assertSame(addedNamespace1, next);

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNamespaceStackIsInScope() {
        Namespace stackNamespace = new Namespace("http://example.com/");
        Namespace addedNamespace = new Namespace("http://example.com/test");

        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", "root").setNamespace(stackNamespace));
        stack.push(new Element("test", "root").setNamespace(addedNamespace));

        Assert.assertTrue(stack.isInScope(stackNamespace));
        Assert.assertTrue(stack.isInScope(addedNamespace));

        stack.pop();

        Assert.assertTrue(stack.isInScope(stackNamespace));
        Assert.assertFalse(stack.isInScope(addedNamespace));
    }
}