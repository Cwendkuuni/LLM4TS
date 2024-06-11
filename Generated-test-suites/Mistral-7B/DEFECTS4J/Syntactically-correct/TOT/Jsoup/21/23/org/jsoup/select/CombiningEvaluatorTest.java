package org.jsoup.select;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.CombiningEvaluator;
import org.jsoup.select.Evaluator;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CombiningEvaluatorTest {

    @Test
    public void testAndNullInput() {
        CombiningEvaluator andEvaluator = new And((Evaluator) null);
        assertNull(andEvaluator);
    }

    @Test
    public void testAndEmptyListOfEvaluators() {
        CombiningEvaluator andEvaluator = new And(new ArrayList<>());
        assertFalse(andEvaluator.matches(Mockito.mock(Document.class), Mockito.mock(Element.class)));
    }

    @Test
    public void testOrNullInput() {
        CombiningEvaluator orEvaluator = new Or((Evaluator) null);
        assertNull(orEvaluator);
    }

    @Test
    public void testOrEmptyListOfEvaluators() {
        CombiningEvaluator orEvaluator = new Or(new ArrayList<>());
        assertFalse(orEvaluator.matches(Mockito.mock(Document.class), Mockito.mock(Element.class)));
    }

    @Test
    public void testAndMatchingElements() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(document, element)).thenReturn(true);
        Mockito.when(evaluator2.matches(document, element)).thenReturn(true);

        CombiningEvaluator andEvaluator = new And(evaluator1, evaluator2);
        assertTrue(andEvaluator.matches(document, element));
    }

    @Test
    public void testAndNoMatchingElement() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(document, element)).thenReturn(false);
        Mockito.when(evaluator2.matches(document, element)).thenReturn(true);

        CombiningEvaluator andEvaluator = new And(evaluator1, evaluator2);
        assertFalse(andEvaluator.matches(document, element));
    }

    @Test
    public void testOrMatchingElement() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(document, element)).thenReturn(true);
        Mockito.when(evaluator2.matches(document, element)).thenReturn(true);

        CombiningEvaluator orEvaluator = new Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(document, element));
    }

    @Test
    public void testOrNoMatchingElements() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(document, element)).thenReturn(false);
        Mockito.when(evaluator2.matches(document, element)).thenReturn(false);

        CombiningEvaluator orEvaluator = new Or(evaluator1, evaluator2);
        assertFalse(orEvaluator.matches(document, element));
    }

    @Test
    public void testOrOneMatchingElement() {
        Evaluator evaluator1 = Mockito.mock(Evaluator.class);
        Evaluator evaluator2 = Mockito.mock(Evaluator.class);

        Document document = Mockito.mock(Document.class);
        Element element = Mockito.mock(Element.class);

        Mockito.when(evaluator1.matches(document, element)).thenReturn(false);
        Mockito.when(evaluator2.matches(document, element)).thenReturn(true);

        CombiningEvaluator orEvaluator = new Or(evaluator1, evaluator2);
        assertTrue(orEvaluator.matches(document, element));
    }
}