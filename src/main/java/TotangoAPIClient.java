
/**
 * @author Lee Shoham 
 *
 * Nov 15, 2017
 */

import java.net.URI;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class TotangoAPIClient.
 * 
 * responsible for executing the API requests
 */
public class TotangoAPIClient {

	private String baseUrl;
	private String authToken;

	// private static final String RESPONSE = "response";

	/**
	 * Instantiates a new totango API client.
	 *
	 * @param baseUrl
	 *            the base url
	 * @param authToken
	 *            the auth token
	 */
	public TotangoAPIClient(String baseUrl, String authToken) {

		this.baseUrl = baseUrl;
		this.authToken = authToken;
	}

	/**
	 * Execute totango API POST request.
	 *
	 * @param path
	 *            the relative path
	 * @param params
	 *            the request body
	 * @return the response as a totangoAPIResponse object
	 */
	public TotangoAPIResponse executeTotangoAPIPostRequest(String path, List<NameValuePair> params) {

		String endPoint = baseUrl + path;
		HttpResponse responseHttp = null;
		URI uri = null;

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			uri = new URI(endPoint);

			HttpPost post = new HttpPost(uri);
			post.addHeader("app-token", authToken);
			post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

			if (params != null) {
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}

			responseHttp = httpClient.execute(post);

			int statusCode = responseHttp.getStatusLine().getStatusCode();

			if (statusCode == 200) {

				// Convert response to JsonElement
				String json = EntityUtils.toString(responseHttp.getEntity());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonObject responseJson = gson.fromJson(json, JsonObject.class);

				return new TotangoAPIResponse(responseJson);
			}

			return new TotangoAPIResponse(new Exception("http error " + statusCode));

		} catch (Exception e) {
			return new TotangoAPIResponse(e);
		}
	}

	/**
	 * Execute totango GET API request.
	 *
	 * @param path
	 *            the relative path
	 * @param params
	 *            the request params
	 * @return the response as a totangoAPIResponse object
	 */
	public TotangoAPIResponse executeTotangoGetAPIRequest(String path) {

		String endPoint = baseUrl + path;
		URI uri = null;
		HttpResponse responseHttp = null;
		TotangoAPIResponse responseTotango = null;

		HttpClient httpClient = HttpClientBuilder.create().build();

		try {
			uri = new URI(endPoint);
			HttpGet get = new HttpGet(uri);
			get.addHeader("app-token", authToken);

			responseHttp = httpClient.execute(get);
			int statusCode = responseHttp.getStatusLine().getStatusCode();

			if (statusCode == 200) {

				// Convert response to JsonElement
				String json = EntityUtils.toString(responseHttp.getEntity());
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				JsonElement responseJson = gson.fromJson(json, JsonElement.class);
				responseTotango = new TotangoAPIResponse(responseJson);
			}

		} catch (Exception e) {
			responseTotango = new TotangoAPIResponse(e);
		}
		return responseTotango;
	}
}
