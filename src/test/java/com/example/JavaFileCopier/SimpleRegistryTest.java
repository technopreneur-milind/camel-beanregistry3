package com.example.JavaFileCopier;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.junit.Test;

import junit.framework.TestCase;

public class SimpleRegistryTest extends TestCase {
	private CamelContext context;
	private ProducerTemplate template;

	protected void setUp() throws Exception {
		SimpleRegistry registry = new SimpleRegistry();
		registry.put("myBean", new MyBean());
		context = new DefaultCamelContext(registry);
		template = context.createProducerTemplate();
		context.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("direct:hello").beanRef("myBean");
			}
		});
		context.start();
	}

	protected void tearDown() throws Exception {
		template.stop();
		context.stop();
	}

	@Test
	public void testHello() throws Exception {
		Object reply = template.requestBody("direct:hello", "World");
		assertEquals("Hello World", reply);
	}
}