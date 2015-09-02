package net.daum.clix.springframework.data.rest.client.json;

import javax.persistence.Transient;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class JpaAwareJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {

	private static final long serialVersionUID = 1L;

	@Override
	protected boolean _isIgnorable(Annotated a) {
		final boolean isTransientPresent = a.getAnnotation(Transient.class) != null;
		final boolean isSpringTransientPresent = a.getAnnotation(org.springframework.data.annotation.Transient.class) != null;
		return super._isIgnorable(a) || isTransientPresent || isSpringTransientPresent;
	}
}
