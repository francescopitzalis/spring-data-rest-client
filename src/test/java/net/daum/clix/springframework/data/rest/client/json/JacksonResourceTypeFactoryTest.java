package net.daum.clix.springframework.data.rest.client.json;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.Resource;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.SimpleType;

public class JacksonResourceTypeFactoryTest {

	private JacksonResourceTypeFactory typeFactory;

	@Before
	public void setup() {
		typeFactory = new JacksonResourceTypeFactory(new ObjectMapper().getTypeFactory());
	}

	@Test
	public void testGetResourceType() throws Exception {
		final JavaType resourceType = typeFactory.getResourceType(Resource.class, Object.class);

		assertEquals(Resource.class, resourceType.getRawClass());
		assertEquals("Lorg/springframework/hateoas/Resource<Ljava/lang/Object;>;", resourceType.getGenericSignature());
	}

	@Test
	public void testGetMapResourceType() throws Exception {
		final SimpleType mapResourceType = (SimpleType) typeFactory.getMapResourceType(Resource.class, String.class,
				Object.class);

		final String expectedSignature = "Lorg/springframework/hateoas/Resource<Ljava/util/Map<Ljava/lang/String;Lorg/springframework/hateoas/Resource<Ljava/lang/Object;>;>;>;";
		assertEquals(expectedSignature, mapResourceType.getGenericSignature());
	}

}
