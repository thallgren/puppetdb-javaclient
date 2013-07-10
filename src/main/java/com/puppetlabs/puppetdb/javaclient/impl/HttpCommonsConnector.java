package com.puppetlabs.puppetdb.javaclient.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.puppetlabs.puppetdb.javaclient.APIPreferences;
import com.puppetlabs.puppetdb.javaclient.Authenticator;
import com.puppetlabs.puppetdb.javaclient.Authenticator.AuthResponse;
import com.puppetlabs.puppetdb.javaclient.HttpConnector;

/**
 * Class responsible for all HTTP request and response processing. Based on the
 * Apache {@link HttpClient}.
 */
public class HttpCommonsConnector implements HttpConnector {

	/**
	 * Create a new client that trusts self signed certificates and will allow all hostnames.
	 * If such a client cannot be created for some reason, then the default client will be used.
	 * 
	 * @return The new client
	 */
	public HttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, preferences.getConnectTimeout());
		HttpConnectionParams.setSoTimeout(params, preferences.getReadTimeout());
		HttpClient client = new DefaultHttpClient(params);
		try {
			client.getConnectionManager().getSchemeRegistry().register(
				new Scheme("https", 443, new SSLSocketFactory(new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
						return true;
					}
				}, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
		}
		catch(Exception e) {
			// let's try without that ...
		}
		return client;
	}

	static InputStream getStream(HttpEntity entity) throws IOException {
		if(entity == null)
			return null;

		return entity.getContent();
	}

	private final Gson gson;

	private final HttpClient httpClient;

	private final Authenticator authenticator;

	private final APIPreferences preferences;

	private String credentials;

	private HttpRequestBase currentRequest;

	/**
	 * <p>
	 * Creates a new HttpCommonsConnector.
	 * </p>
	 * <p>
	 * For Guice injection only. Don't use this constructor from code
	 * </p>
	 * 
	 * @param gson
	 *            The instance used when parsing or serializing JSON
	 * @param preferences
	 *            API connection preferences
	 * @param authenticator
	 */
	@Inject
	public HttpCommonsConnector(Gson gson, APIPreferences preferences, Authenticator authenticator) {
		this.gson = gson;
		this.authenticator = authenticator;
		this.preferences = preferences;
		httpClient = createHttpClient();
	}

	@Override
	public synchronized void abortCurrentRequest() {
		if(currentRequest != null) {
			currentRequest.abort();
			currentRequest = null;
		}
	}

	protected void assignJSONContent(HttpEntityEnclosingRequestBase request, Object params) {
		if(params != null) {
			request.addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON + "; charset=" + UTF_8.name()); //$NON-NLS-1$
			byte[] data = toJson(params).getBytes(UTF_8);
			request.setEntity(new ByteArrayEntity(data));
		}
	}

	@Override
	public void authenticate() throws IOException {
		if(credentials == null) {
			AuthResponse auth = authenticator.authenticate(preferences.getLogin(), preferences.getPassword());
			String oauthToken = auth.getToken();
			preferences.setOAuthAccessToken(oauthToken);
			preferences.setOAuthScopes(auth.getScopes());
			this.credentials = "Bearer " + oauthToken;
		}
	}

	protected void configureRequest(final HttpRequestBase request) {
		if(credentials != null)
			request.addHeader(HttpHeaders.AUTHORIZATION, credentials);
		else

			request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
	}

	private HttpGet createGetRequest(String urlStr, Map<String, String> params) {
		StringBuilder bld = new StringBuilder(createURI(urlStr));
		if(params != null && !params.isEmpty()) {
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			for(Map.Entry<String, String> param : params.entrySet())
				pairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			bld.append('?');
			bld.append(URLEncodedUtils.format(pairs, UTF_8.name()));
		}
		return new HttpGet(URI.create(bld.toString()));
	}

	/**
	 * Create full URI from path
	 * 
	 * @param path
	 * @return uri
	 */
	protected String createURI(final String path) {
		return preferences.getURL() + path;
	}

	@Override
	public void delete(final String uri) throws IOException {
		HttpDelete request = new HttpDelete(createURI(uri));
		configureRequest(request);
		executeRequest(request, null);
	}

	@Override
	public void download(String urlStr, Map<String, String> params, final OutputStream output) throws IOException {
		HttpGet request = createGetRequest(urlStr, params);
		configureRequest(request);
		httpClient.execute(request, new ResponseHandler<Void>() {
			@Override
			public Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				StatusLine statusLine = response.getStatusLine();
				int code = statusLine.getStatusCode();
				if(code != HttpStatus.SC_OK)
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());

				HttpEntity entity = response.getEntity();
				entity.writeTo(output);
				return null;
			}
		});
	}

	private synchronized void endRequest() {
		currentRequest = null;
	}

	protected <V> V executeRequest(final HttpRequestBase request, final Type type) throws IOException {
		startRequest(request);
		try {
			return httpClient.execute(request, new JSonResponseHandler<V>(gson, type));
		}
		finally {
			endRequest();
		}
	}

	@Override
	public <V> V get(String urlStr, Map<String, String> params, Type type) throws IOException {
		HttpGet request = createGetRequest(urlStr, params);
		configureRequest(request);
		return executeRequest(request, type);
	}

	@Override
	public <V> V patch(final String uri, final Object params, final Class<V> type) throws IOException {
		// HttpPatch is introduced in 4.2. This code is compatible with 4.1 in order to
		// play nice with Eclipse Juno and Kepler
		HttpPost request = new HttpPost(createURI(uri)) {
			@Override
			public String getMethod() {
				return "PATCH";
			}
		};

		configureRequest(request);
		assignJSONContent(request, params);
		return executeRequest(request, type);
	}

	@Override
	public void post(String uri) throws IOException {
		postJSON(uri, null, null);
	}

	@Override
	public <V> V postJSON(final String uri, final Object params, final Class<V> type) throws IOException {
		HttpPost request = new HttpPost(createURI(uri));
		configureRequest(request);
		assignJSONContent(request, params);
		return executeRequest(request, type);
	}

	@Override
	public <V> V postUpload(String uri, Map<String, String> stringParts, InputStream in, String mimeType,
			String fileName, final long fileSize, Class<V> type) throws IOException {
		HttpPost request = new HttpPost(createURI(uri));
		configureRequest(request);

		MultipartEntity entity = new MultipartEntity();
		for(Map.Entry<String, String> entry : stringParts.entrySet())
			entity.addPart(entry.getKey(), StringBody.create(entry.getValue(), "text/plain", UTF_8));

		entity.addPart("file", new InputStreamBody(in, mimeType, fileName) {
			@Override
			public long getContentLength() {
				return fileSize;
			}
		});
		request.setEntity(entity);
		return executeRequest(request, type);
	}

	@Override
	public <V> V put(final String uri, final Object params, final Class<V> type) throws IOException {
		HttpPut request = new HttpPut(createURI(uri));
		configureRequest(request);
		assignJSONContent(request, params);
		return executeRequest(request, type);
	}

	private synchronized void startRequest(HttpRequestBase request) {
		if(currentRequest != null)
			currentRequest.abort();
		currentRequest = request;
	}

	/**
	 * Convert object to a JSON string
	 * 
	 * @param object
	 * @return JSON string
	 * @throws IOException
	 */
	protected String toJson(Object object) {
		return gson.toJson(object);
	}
}
