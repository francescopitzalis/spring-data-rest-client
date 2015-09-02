package net.daum.clix.springframework.data.rest.client.json;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonResourceTypeFactory {

	private final TypeFactory typeFactory;

	public JacksonResourceTypeFactory(TypeFactory typeFactory) {
		this.typeFactory = typeFactory;
	}

	public JavaType getResourceType(Type resourceType, Type objectType) {
		if (Resources.class.isAssignableFrom((Class<?>) resourceType)) {
			final JavaType wrappedType = typeFactory.constructParametrizedType(Resource.class, Resource.class,
					(Class<?>) objectType);
			return typeFactory.constructParametrizedType((Class<?>) resourceType, Resource.class, wrappedType);
		}

		return typeFactory.constructParametrizedType((Class<?>) resourceType, (Class<?>) resourceType,
				(Class<?>) objectType);
	}

	public JavaType getMapResourceType(Type resourceType, Type keyType, Type valueType) {
		final JavaType simpleKeyType = typeFactory.constructType(keyType);
		final JavaType wrappedValueType = typeFactory.constructParametrizedType(Resource.class, Resource.class,
				(Class<?>) valueType);
		final JavaType mapType = typeFactory.constructMapType(Map.class, simpleKeyType, wrappedValueType);

		return typeFactory.constructParametrizedType((Class<?>) resourceType, (Class<?>) resourceType, mapType);
	}

	public JavaType getSetResourceType(Type resourceType, Type valueType) {
		final JavaType wrappedValueType = typeFactory.constructParametrizedType(Resource.class, Resource.class,
				(Class<?>) valueType);
		final JavaType setType = typeFactory.constructParametrizedType(Set.class, Set.class, wrappedValueType);

		return typeFactory.constructParametrizedType((Class<?>) resourceType, (Class<?>) resourceType, setType);
	}

}
