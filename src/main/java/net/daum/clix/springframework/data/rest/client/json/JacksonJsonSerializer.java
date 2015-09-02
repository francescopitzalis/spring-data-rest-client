package net.daum.clix.springframework.data.rest.client.json;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonSerializer implements JsonSerializer {

	private final Logger LOG = LoggerFactory.getLogger(JacksonJsonSerializer.class);

	private final ObjectMapper mapper;

	private final JacksonResourceTypeFactory typeFactory;

	public JacksonJsonSerializer() {
		this.mapper = new ObjectMapper();
		this.mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// this.mapper.setAnnotationIntrospector(new
		// JpaAwareJacksonAnnotationIntrospector());

		this.typeFactory = new JacksonResourceTypeFactory(mapper.getTypeFactory());
	}

	@Override
	public byte[] serialize(Object object) {
		try {
			return mapper.writeValueAsBytes(object);
		} catch (final Exception e) {
			LOG.error("Serialization failed for " + object.getClass().getName(), e);
		}

		return null;
	}

	@Override
	public Object deserialize(byte[] jsonData, Type resourceType, Type objectType) {
		if (jsonData == null) {
			return null;
		}

		return readValue(jsonData, typeFactory.getResourceType(resourceType, objectType));
	}

	@Override
	public Object deserializeSetResource(byte[] jsonData, Type resourceType, Type valueType) {
		if (jsonData == null) {
			return null;
		}

		final JavaType setResourceType = typeFactory.getSetResourceType(resourceType, valueType);

		return readValue(jsonData, setResourceType);
	}

	@Override
	public Object deserializeMapResource(byte[] jsonData, Type resourceType, Type keyType, Type valueType) {
		if (jsonData == null) {
			return null;
		}

		final JavaType mapResourceType = typeFactory.getMapResourceType(resourceType, keyType, valueType);

		return readValue(jsonData, mapResourceType);
	}

	private Object readValue(byte[] jsonData, JavaType type) {
		try {
			return mapper.readValue(jsonData, type);
		} catch (final Exception e) {
			LOG.error("Deserialization failed for exception : ", e);
			LOG.error("Deserialization failed for json body.", new String(jsonData));
		}

		return null;
	}

}
