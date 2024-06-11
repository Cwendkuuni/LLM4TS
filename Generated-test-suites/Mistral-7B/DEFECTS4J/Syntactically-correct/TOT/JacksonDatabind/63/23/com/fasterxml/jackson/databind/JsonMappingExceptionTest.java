package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonMappingExceptionTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 /**
 * Test serialization of a simple bean to JSON, with a cyclic reference,
 * and verify that a JsonMappingException is thrown with the correct path.
 */
 @Test
 public void testCyclicSerialization() {
 A a1 = new A("a1");
 A a2 = new A("a2");
 a1.setB(a2);
 a2.setB(a1);

 try {
 JsonNode node = mapper.valueToTree(a1);
 Assert.fail();
 } catch (JsonMappingException e) {
 // Verify correct path, and that serialization context is present
 Assert.assertEquals("Root element must be an array or object: \n [{\"name\":\"a1\",\"b\":{\"name\":\"a2\",\"b\":{\"name\":\"a1\"}}}]", e.getMessage());
 Assert.assertEquals("A[\"a1\"]", e.getPathReference());
 Assert.assertNotNull(e.getProcessor());
 }
 }

 /**
 * Test deserialization of a JSON string with a cyclic reference, and
 * verify that a JsonMappingException is thrown with the correct path.
 */
 @Test
 public void testCyclicDeserialization() throws IOException {
 String json = "{\"name\":\"a1\",\"b\":{\"name\":\"a2\",\"b\":{\"name\":\"a1\"}}}";
 A a1 = null;
 A a2 = null;
 try {
 a1 = mapper.readValue(json, A.class);
 } catch (JsonMappingException e) {
 // Verify correct path, and that parser is present
 Assert.assertEquals("Cannot construct instance of \"com.fasterxml.jackson.databind.test.A\": not including a constructor\n at [Source: (String)" + json + "\n line: 1, column: 1]", e.getMessage());
 Assert.assertEquals("A[\"a1\"]", e.getPathReference());
 Assert.assertNotNull(e.getProcessor());
 a1 = mapper.readValue(json, A[].class)[0];
 }
 Assert.assertEquals("a1", a1.getName());
 Assert.assertEquals("a2", a1.getB().getName());
 Assert.assertEquals("a2", a1.getB().getB().getName());
 Assert.assertEquals("a1", a1.getB().getB().getB().getName());
 }

 /**
 * Test serialization of a complex object graph with multiple cyclic references,
 * and verify that a JsonMappingException is thrown with the correct path.
 */
 @Test
 public void testComplexCyclicSerialization() {
 B b1 = new B("b1");
 C c1 = new C("c1", b1);
 D d1 = new D("d1", c1);
 E e1 = new E("e1", d1);
 F f1 = new F("f1", e1);
 G g1 = new G("g1", f1);
 H h1 = new H("h1", g1);
 I i1 = new I("i1", h1);
 J j1 = new J("j1", i1);
 K k1 = new K("k1", j1);
 L l1 = new L("l1", k1);
 M m1 = new M("m1", l1);
 A a1 = new A("a1");
 a1.setB(b1);
 b1.setA(a1);
 c1.setD(d1);
 d1.setC(c1);
 e1.setF(f1);
 f1.setE(e1);
 g1.setH(h1);
 h1.setG(g1);
 i1.setI(i1);
 j1.setJ(j1);
 k1.setK(k1);
 l1.setL(l1);
 m1.setM(m1);
 a1.getB().getB().getB().getB().getB().setA(a1);

 try {
 JsonNode node = mapper.valueToTree(a1);
 Assert.fail();
 } catch (JsonMappingException e) {
 // Verify correct path, and that serialization context is present
 Assert.assertEquals("Root element must be an array or object: \n [{\"name\":\"a1\",\"b\":{" +
         "\"name\":\"b1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\"}}}}}}}}}]", e.getMessage());
 Assert.assertEquals("A[\"a1\"]", e.getPathReference());
 Assert.assertNotNull(e.getProcessor());
 }
 }

 /**
 * Test deserialization of a JSON string with a complex object graph and multiple
 * cyclic references, and verify that a JsonMappingException is thrown with the
 * correct path.
 */
 @Test
 public void testComplexCyclicDeserialization() throws IOException {
 String json = "" +
         "{\"a\":{\"name\":\"a1\",\"b\":{" +
         "\"name\":\"b1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\",\"d\":{" +
         "\"name\":\"d1\",\"c\":{" +
         "\"name\":\"c1\"}}}}}}}}";
 A a1 = null;
 B b1 = null;
 C c1 = null;
 D d1 = null;
 E e1 = null;
 F f1 = null;
 G g1 = null;
 H h1 = null;
 I i1 = null;
 J j1 = null;
 K k1 = null;
 L l1 = null;
 M m1 = null;
 try {
 a1 = mapper.readValue(json, A.class);
 } catch (JsonMappingException e) {
 // Verify correct path, and that parser is present
 Assert.assertEquals("Cannot construct instance of \"com.fasterxml.jackson.databind.test.A\": not including a constructor\n at [Source: (String)" + json + "\n line: 1, column: 1]", e.getMessage());
 Assert.assertEquals("A[\"a1\"]", e.getPathReference());
 Assert.assertNotNull(e.getProcessor());
 a1 = mapper.readValue(json, A[].class)[0];
 }
 Assert.assertEquals("a1", a1.getName());
 b1 = a1.getB();
 Assert.assertEquals("b1", b1.getName());
 c1 = b1.getC();
 Assert.assertEquals("c1", c1.getName());
 d1 = c1.getD();
 Assert.assertEquals("d1", d1.getName());
 e1 = d1.getC();
 Assert.assertEquals("c1", e1.getC().getName());
 f1 = e1.getF();
 Assert.assertEquals("f1", f1.getName());
 g1 = f1.getH();
 Assert.assertEquals("h1", g1.getName());
 h1 = g1.getG();
 Assert.assertEquals("g1", h1.getName());
 i1 = h1.getI();
 Assert.assertEquals("i1", i1.getName());
 j1 = i1.getJ();
 Assert.assertEquals("j1", j1.getName());
 k1 = j1.getK();
 Assert.assertEquals("k1", k1.getName());
 l1 = k1.getL();
 Assert.assertEquals("l1", l1.getName());
 m1 = l1.getM();
 Assert.assertEquals("m1", m1.getName());
 }

 /**
 * Test that a JsonMappingException is thrown when a non-serializable class is
 * encountered during serialization.
 */
 @Test(expected = JsonMappingException.class)
 public void testNonSerializableSerialization() {
 NonSerializable ns = new NonSerializable();
 try {
 mapper.writeValueAsString(ns);
 } catch (JsonMappingException e) {
 // Verify correct message
 Assert.assertEquals("FlowingSaver cannot be written to JSON", e.getMessage());
 }
 }

 /**
 * Test that a JsonMappingException is thrown when a non-serializable class is
 * encountered during deserialization.
 */
 @Test(expected = JsonMappingException.class)
 public void testNonSerializableDeserialization() throws IOException {
 String json = "{\"nonSerializable\":null}";
 NonSerializable ns = null;
 try {
 ns = mapper.readValue(json, NonSerializable.class);
 } catch (JsonMappingException e) {
 // Verify correct message
 Assert.assertEquals("NonSerializable cannot be instantiated: no constructor found", e.getMessage());
 }
 }
}

class A {
 private B b;

 public A() {}

 public A(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public B getB() {
 return b;
 }

 public void setB(B b) {
 this.b = b;
 }

 private String name;
}

class B {
 private C c;

 public B() {}

 public B(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public C getC() {
 return c;
 }

 public void setC(C c) {
 this.c = c;
 }

 private String name;
}

class C {
 private D d;

 public C() {}

 public C(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public D getD() {
 return d;
 }

 public void setD(D d) {
 this.d = d;
 }

 private String name;
}

class D {
 private C c;

 public D() {}

 public D(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public C getC() {
 return c;
 }

 public void setC(C c) {
 this.c = c;
 }

 private String name;
}

class E {
 private F f;

 public E() {}

 public E(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public F getF() {
 return f;
 }

 public void setF(F f) {
 this.f = f;
 }

 private String name;
}

class F {
 private G g;

 public F() {}

 public F(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public G getG() {
 return g;
 }

 public void setG(G g) {
 this.g = g;
 }

 private String name;
}

class G {
 private H h;

 public G() {}

 public G(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public H getH() {
 return h;
 }

 public void setH(H h) {
 this.h = h;
 }

 private String name;
}

class H {
 private I i;

 public H() {}

 public H(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public I getI() {
 return i;
 }

 public void setI(I i) {
 this.i = i;
 }

 private String name;
}

class I {
 private J j;

 public I() {}

 public I(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public J getJ() {
 return j;
 }

 public void setJ(J j) {
 this.j = j;
 }

 private String name;
}

class J {
 private K k;

 public J() {}

 public J(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public K getK() {
 return k;
 }

 public void setK(K k) {
 this.k = k;
 }

 private String name;
}

class K {
 private L l;

 public K() {}

 public K(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public L getL() {
 return l;
 }

 public void setL(L l) {
 this.l = l;
 }

 private String name;
}

class L {
 private M m;

 public L() {}

 public L(String name) {
 this();
 this.name = name;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public M getM() {
 return m;
 }

 public void setM(M m) {
 this.m = m;
 }

 private String name;
}

class M {
 private String m;

 public M() {}

 public M(String name) {
 this();
 this.m = name;
 }

 public String getM() {
 return m;
 }

 public void setM(String m) {
 this.m = m;
 }

 private String m;
}

class NonSerializable {
 public NonSerializable() {
 throw new RuntimeException("Cannot instantiate non-serializable class");
 }
}