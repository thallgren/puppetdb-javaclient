/**
 * Copyright (c) 2013 Puppet Labs, Inc. and other contributors, as listed below.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *   Puppet Labs
 */
package com.puppetlabs.puppetdb.javaclient.model;

import static com.puppetlabs.puppetdb.javaclient.query.Query.field;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.puppetlabs.puppetdb.javaclient.query.Field;

/**
 * A POJO that represents a PuppetDB Resource
 */
public class Resource extends Entity {

	@SuppressWarnings("javadoc")
	public static final Field<Resource> TAG = field("tag");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> CERTNAME = field("certname");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> TYPE = field("type");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> TITLE = field("title");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> EXPORTED = field("exported");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> SOURCEFILE = field("sourcefile");

	@SuppressWarnings("javadoc")
	public static final Field<Resource> SOURCELINE = field("sourceline");

	// @fmtOff
	/**
	 * A type representing a {@link List} of {@link Resource} instances
	 */
	public static final Type LIST = new TypeToken<List<Resource>>() {}.getType();
	// @fmtOn

	private int sourceline;

	private String sourcefile;

	private boolean exported;

	private List<String> tags;

	private String title;

	private String type;

	private String certname;

	private Map<String, Object> parameters;

	/**
	 * @return the certname
	 */
	public String getCertname() {
		return certname;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @return the sourcefile
	 */
	public String getSourcefile() {
		return sourcefile;
	}

	/**
	 * @return the sourceline
	 */
	public int getSourceline() {
		return sourceline;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the exported
	 */
	public boolean isExported() {
		return exported;
	}

	/**
	 * @param certname
	 *            the certname to set
	 */
	public void setCertname(String certname) {
		this.certname = certname;
	}

	/**
	 * @param exported
	 *            the exported to set
	 */
	public void setExported(boolean exported) {
		this.exported = exported;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param sourcefile
	 *            the sourcefile to set
	 */
	public void setSourcefile(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	/**
	 * @param sourceline
	 *            the sourceline to set
	 */
	public void setSourceline(int sourceline) {
		this.sourceline = sourceline;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
