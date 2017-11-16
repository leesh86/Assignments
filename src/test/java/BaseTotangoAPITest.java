
/**
 * @author Lee Shoham
 *
 * Nov 15, 2017
 */
import java.util.List;

import org.apache.http.NameValuePair;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

/**
 * The Class BaseTotangoAPITest. responsible for setting up the TotangoAPIClient
 * used in the tests
 */
public class BaseTotangoAPITest {

	protected TotangoAPIClient client;

	/**
	 * Sets up the client before tests are executed.
	 *
	 * @param baseUrl
	 *            the base url
	 * @param authToken
	 *            the app token
	 */
	@Parameters({ "baseUrl" })
	@BeforeTest
	public void setUp(String baseUrl) {
		this.client = new TotangoAPIClient(baseUrl, System.getProperty("environment"));
		System.out.println(System.getProperty("environment"));
	}

	/**
	 * Execute totango get API request.
	 * validate the response status is ok, fail the test otherwise
	 *
	 * @param path the relative path
	 * @return the totango API response object
	 */
	public TotangoAPIResponse executeTotangoGetAPIRequest(String path) {

		TotangoAPIResponse response = client.executeTotangoGetAPIRequest(path);
		if (response.getStatus() == TotangoAPIResponse.Status.FAIL) {
			Assert.fail("something went wrong", response.getException());
		}
		return response;
	}

	/**
	 * Execute totango post API request.
	 * validate the response status is ok, fail the test otherwise
	 *
	 * @param path the relative path
	 * @param params the body
	 * @return the totango API response object
	 */
	public TotangoAPIResponse executeTotangoPostAPIRequest(String path, List<NameValuePair> params) {

		TotangoAPIResponse response = client.executeTotangoAPIPostRequest(path, params);
		if (response.getStatus() == TotangoAPIResponse.Status.FAIL) {
			Assert.fail("something went wrong", response.getException());
		}
		return response;
	}

}
