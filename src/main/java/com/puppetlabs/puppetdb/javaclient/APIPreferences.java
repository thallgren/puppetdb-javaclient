package com.puppetlabs.puppetdb.javaclient;

public interface APIPreferences {

	String getPassword();

	String getURL();

	String getLogin();

	void setOAuthAccessToken(String oauthToken);

	void setOAuthScopes(String scopes);

	int getConnectTimeout();

	int getReadTimeout();

}
