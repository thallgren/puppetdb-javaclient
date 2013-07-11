package com.puppetlabs.puppetdb.javaclient.model;

import static com.puppetlabs.puppetdb.javaclient.query.Query.field;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.puppetlabs.puppetdb.javaclient.query.Field;

/**
 * A POJO that represents a PuppetDB Report
 */
public class Report extends Entity {
	@SuppressWarnings("javadoc")
	public static final Field<Report> END_TIME = field("end-time");

	@SuppressWarnings("javadoc")
	public static final Field<Report> PUPPET_VERSION = field("puppet-version");

	@SuppressWarnings("javadoc")
	public static final Field<Report> RECEIVE_TIME = field("receive-time");

	@SuppressWarnings("javadoc")
	public static final Field<Report> CONFIGURATION_VERSION = field("configuration-version");

	@SuppressWarnings("javadoc")
	public static final Field<Report> START_TIME = field("start-time");

	@SuppressWarnings("javadoc")
	public static final Field<Report> HASH = field("hash");

	@SuppressWarnings("javadoc")
	public static final Field<Report> CERTNAME = field("certname");

	@SuppressWarnings("javadoc")
	public static final Field<Report> REPORT_FORMAT = field("report-format");

	// @fmtOff
	/**
	 * A type representing a {@link List} of {@link Report} instances
	 */
	public static final Type LIST = new TypeToken<List<Report>>() {}.getType();
	// @fmtOn

	@SerializedName("end-time")
	private Date endTime;

	@SerializedName("puppet-version")
	private String puppetVersion;

	@SerializedName("receive-time")
	private Date receiveTime;

	@SerializedName("configuration-version")
	private String configurationVersion;

	@SerializedName("start-time")
	private Date startTime;

	private String hash;

	private String certname;

	@SerializedName("report-format")
	private int reportFormat;

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the puppetVersion
	 */
	public String getPuppetVersion() {
		return puppetVersion;
	}

	/**
	 * @param puppetVersion
	 *            the puppetVersion to set
	 */
	public void setPuppetVersion(String puppetVersion) {
		this.puppetVersion = puppetVersion;
	}

	/**
	 * @return the receiveTime
	 */
	public Date getReceiveTime() {
		return receiveTime;
	}

	/**
	 * @param receiveTime
	 *            the receiveTime to set
	 */
	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	/**
	 * @return the configurationVersion
	 */
	public String getConfigurationVersion() {
		return configurationVersion;
	}

	/**
	 * @param configurationVersion
	 *            the configurationVersion to set
	 */
	public void setConfigurationVersion(String configurationVersion) {
		this.configurationVersion = configurationVersion;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
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
	 * @return the reportFormat
	 */
	public int getReportFormat() {
		return reportFormat;
	}

	/**
	 * @param reportFormat
	 *            the reportFormat to set
	 */
	public void setReportFormat(int reportFormat) {
		this.reportFormat = reportFormat;
	}
}
