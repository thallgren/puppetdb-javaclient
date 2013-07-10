package com.puppetlabs.puppetdb.javaclient.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.puppetlabs.puppetdb.javaclient.HttpConnector;

public class MockConnector implements HttpConnector {

	private final Gson gson;

	@Inject
	MockConnector(Gson gson) {
		this.gson = gson;
	}

	@Override
	public void abortCurrentRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void authenticate() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String urlStr) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void download(String urlStr, Map<String, String> params, OutputStream output) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public <V> V get(String urlStr, Map<String, String> params, Type type) throws IOException {
		InputStream mockResponses = getClass().getResourceAsStream("/mock_resources.json");
		assertNotNull("Unable to open 'mock_resources.json'", mockResponses);
		Object mocks;
		try {
			mocks = gson.fromJson(new InputStreamReader(mockResponses, UTF_8), Object.class);
		}
		finally {
			mockResponses.close();
		}

		assertTrue("mock_resources did not produce a Map", mocks instanceof Map);
		Object mock = ((Map<?, ?>) mocks).get(urlStr);
		if(mock == null)
			throw new HttpResponseException(HttpStatus.SC_NOT_FOUND, urlStr);

		return gson.fromJson(gson.toJson(mock), type);
	}

	@Override
	public <V> V patch(String urlStr, Object params, Class<V> type) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void post(String urlStr) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public <V> V postJSON(String urlStr, Object params, Class<V> type) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V postUpload(String urlStr, Map<String, String> stringParts, InputStream in, String mimeType,
			String fileName, long fileSize, Class<V> type) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V put(String urlStr, Object params, Class<V> type) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
