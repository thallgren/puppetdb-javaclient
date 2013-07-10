package com.puppetlabs.puppetdb.javaclient.model;

import java.util.List;
import java.util.Map;

/**
 * A POJO that represents a PuppetDB Resource
 */
public class Resource extends Entity {

	private int sourceline;

	private String sourcefile;

	private boolean exported;

	private List<String> tags;

	private String title;

	private String type;

	private String certname;

	private Map<String, Object> parameters;

	/**
	 * @return the sourceline
	 */
	public int getSourceline() {
		return sourceline;
	}

	/**
	 * @param sourceline
	 *            the sourceline to set
	 */
	public void setSourceline(int sourceline) {
		this.sourceline = sourceline;
	}

	/**
	 * @return the sourcefile
	 */
	public String getSourcefile() {
		return sourcefile;
	}

	/**
	 * @param sourcefile
	 *            the sourcefile to set
	 */
	public void setSourcefile(String sourcefile) {
		this.sourcefile = sourcefile;
	}

	/**
	 * @return the exported
	 */
	public boolean isExported() {
		return exported;
	}

	/**
	 * @param exported
	 *            the exported to set
	 */
	public void setExported(boolean exported) {
		this.exported = exported;
	}

	/**
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the certname
	 */
	public String getCertname() {
		return certname;
	}

	/**
	 * @param certname
	 *            the certname to set
	 */
	public void setCertname(String certname) {
		this.certname = certname;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
}
