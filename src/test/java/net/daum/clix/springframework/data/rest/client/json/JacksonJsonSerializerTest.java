package net.daum.clix.springframework.data.rest.client.json;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.hateoas.Resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)
public class JacksonJsonSerializerTest {

	@Mock
	private ObjectMapper mapper;

	@Mock
	private JacksonResourceTypeFactory typeFactory;

	@InjectMocks
	private JacksonJsonSerializer serializer;

	@Test
	public void serializeSimplyDelegatesToMapper() throws Exception {
		final Object object = new Object();
		serializer.serialize(object);

		verify(mapper, times(1)).writeValueAsBytes(object);
	}

	@Test
	public void serializeReturnsNullIfAnyExceptionCatched() {
		assertNull(serializer.serialize(null));
		assertNull(serializer.serialize("{A string which is not json format"));
	}

	@Test
	public void deserializeDelegatesToMapperWithCreatedType() throws Exception {
		final byte[] jsonData = "{\"key\":\"value\"}".getBytes();
		final Type resourceType = Resource.class;
		final Type objectType = Object.class;

		final JavaType createdType = mock(JavaType.class);
		when(typeFactory.getResourceType(resourceType, objectType)).thenReturn(createdType);

		serializer.deserialize(jsonData, resourceType, objectType);

		verify(typeFactory).getResourceType(resourceType, objectType);
		verify(mapper).readValue(jsonData, createdType);
	}

	@Test
	public void deserializeMapResource() throws JsonParseException, JsonMappingException, IOException {
		final byte[] jsonData = "{\"key\":\"value\"}".getBytes();
		final Type resourceType = Resource.class;
		final Type keyType = String.class;
		final Type valueType = String.class;

		final JavaType createdType = mock(JavaType.class);
		when(typeFactory.getMapResourceType(resourceType, keyType, valueType)).thenReturn(createdType);

		serializer.deserializeMapResource(jsonData, resourceType, keyType, valueType);

		verify(typeFactory).getMapResourceType(resourceType, keyType, valueType);
		verify(mapper).readValue(jsonData, createdType);
	}

}
