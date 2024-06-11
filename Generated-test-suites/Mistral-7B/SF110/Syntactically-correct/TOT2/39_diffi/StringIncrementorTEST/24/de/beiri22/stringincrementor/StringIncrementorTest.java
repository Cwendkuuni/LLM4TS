package de.beiri22.stringincrementor.test;

import de.beiri22.stringincrementor.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijkllmopqrstuvwxyz";
        RelativeString expected = new RelativeString("i->j, 2l: 1, j->k: 2, m->n: 1, op->p: 1, q->r: 1, s->t: 1, u->v: 1, w->x: 1, y->z: 1");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected.toString(), result.toString());
    }
    
    @Test
    public void testDiffEdgeCases() {
        String a = "";
        String b = "abcdefghijklmnopqrstuvwxyz";
        RelativeString expected = new RelativeString("0->a, 11l: 11, a->b, b->c, c->d, d->e, e->f, f->g, g->h, h->i, i->j, j->k, k->l, l->m, m->n, n->o, o->p, p->q, q->r, r->s, s->t, t->u, u->v, v->w, w->x, x->y, y->z");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected.toString(), result.toString());

        String a2 = "abcdefghijklmnopqrstuvwxyz";
        String b2 = "";
        RelativeString expected2 = new RelativeString("0->a, 1->b, 2->c, 3->d, 4->e, 5->f, 6->g, 7->h, 8->i, 9->j, 10->k, 11->l, 12->m, 13->n, 14->o, 15->p, 16->q, 17->r, 18->s, 19->t, 20->u, 21->v, 22->w, 23->x, 24->y, 25->z");
        RelativeString result2 = StringIncrementor.diff(a2, b2);
        assertEquals(expected2.toString(), result2.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffExceptionHandling() {
        String a = null;
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testPatch() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String patchString = "i->j, 2l: 1, j->k: 2, m->n: 1, op->p: 1, q->r: 1, s->t: 1, u->v: 1, w->x: 1, y->z: 1";
        String expected = "abcdefghijkmnlopqrstuvwxz";
        String result = StringIncrementor.patch(a, patchString);
        assertEquals(expected, result);
    }

    @Test
    public void testPatchEdgeCases() {
        String a = "";
        String patchString = "0->a, 11l: 11, a->b, b->c, c->d, d->e, e->f, f->g, g->h, h->i, i->j, j->k, k->l, l->m, m->n, n->o, o->p, p->q, q->r, r->s, s->t, t->u, u->v, v->w, w->x, x->y, y->z";
        String expected = "abcdefghijkllmopqrstuvwxyz";
        String result = StringIncrementor.patch(a, patchString);
        assertEquals(expected, result);

        String a2 = "abcdefghijkllmopqrstuvwxyz";
        String patchString2 = "";
        String expected2 = "abcdefghijkllmopqrstuvwxyz";
        String result2 = StringIncrementor.patch(a2, patchString2);
        assertEquals(expected2, result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchExceptionHandling() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String patchString = null;
        StringIncrementor.patch(a, patchString);
    }
}