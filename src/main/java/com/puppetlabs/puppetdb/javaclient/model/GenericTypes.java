package com.puppetlabs.puppetdb.javaclient.model;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

/**
 * Generic type declarations for model objects.
 */
public interface GenericTypes {
	// @fmtOff
	/**
	 * A type representing a {@link List} of {@link Resource} instances
	 */
	Type LIST_RESOURCE = new TypeToken<List<Resource>>() {}.getType();
	// @fmtOn
}
