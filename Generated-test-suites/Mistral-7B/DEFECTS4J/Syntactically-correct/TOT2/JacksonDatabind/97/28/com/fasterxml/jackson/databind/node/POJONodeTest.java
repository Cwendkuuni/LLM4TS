package com.fasterxml.jackson.databind.node; // import statements omitted for brevity

public class POJONodeTest {

    

    @Test
    public void testGetNodeType() {
        POJONode node = new POJONode(new Object());
        Assert.assertEquals(JsonNodeType.POJO, node.getNodeType());
    }

    @Test
    public void testAsText() {
        POJONode node = new POJONode("Hello World");
        Assert.assertEquals("Hello World", node.asText());
    }

    @Test
    public void testAsTextWithDefaultValue() {
        POJONode node = new POJONode(null);
        Assert.assertEquals("null", node.asText( "Default Value" ));
    }

    @Test
    public void testAsBoolean() {
        POJONode nodeTrue = new POJONode(true);
        POJONode nodeFalse = new POJONode(false);

        Assert.assertTrue(nodeTrue.asBoolean(false));
        Assert.assertFalse(nodeFalse.asBoolean(true));
    }

    @Test
    public void testAsInt() {
        POJONode nodeInt1 = new POJONode(1);
        POJONode nodeInt2 = new POJONode(2);
        POJONode nodeNull = new POJONode(null);

        Assert.assertEquals(1, nodeInt1.asInt(0));
        Assert.assertEquals(2, nodeInt2.asInt(0));
        Assert.assertEquals(0, nodeNull.asInt(5));
    }

    @Test
    public void testAsLong() {
        POJONode nodeLong1 = new POJONode(1L);
        POJONode nodeLong2 = new POJONode(2L);
        POJONode nodeNull = new POJONode(null);

        Assert.assertEquals(1L, nodeLong1.asLong(0L));
        Assert.assertEquals(2L, nodeLong2.asLong(0L));
        Assert.assertEquals(0L, nodeNull.asLong(10L));
    }

    @Test
    public void testAsDouble() {
        POJONode nodeDouble1 = new POJONode(1.0);
        POJONode nodeDouble2 = new POJONode(2.0);
        POJONode nodeNull = new POJONode(null);

        Assert.assertEquals(1.0, nodeDouble1.asDouble(0.0), 0.001);
        Assert.assertEquals(2.0, nodeDouble2.asDouble(0.0), 0.001);
        Assert.assertEquals(0.0, nodeNull.asDouble(3.14), 0.001);
    }

    @Test
    public void testSerialize() throws IOException {
        POJONode node = new POJONode("Test String");
        StringWriter writer = new StringWriter();
        JsonFactory factory = new JsonFactory();
        JsonGenerator gen = factory.createGenerator(writer);

        node.serialize(gen, null);
        gen.flush();

        String json = writer.toString();
        Assert.assertEquals("\"Test String\"", json);
    }

    @Test
    public void testGetPojo() {
        String testObject = "Test Object";
        POJONode node = new POJONode(testObject);

        Object pojo = node.getPojo();
        Assert.assertEquals(testObject, pojo);
    }

    /* Add edge cases and exception handling scenarios here */

    
}