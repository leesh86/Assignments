/**
 * @author Lee Shoham
 *
 * Nov 15, 2017
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * The Class TotangoAPIResponse.
 * 
 * represents an API response
 */
public class TotangoAPIResponse {

	private JsonElement response;
	private Status status;
	private Exception e;

	protected enum Status {
		SUCCESS, FAIL
	};

	/**
	 * Instantiates a new totango API response.
	 *
	 * @param responseJson
	 *            the response json object
	 */
	public TotangoAPIResponse(JsonElement responseJson) {

		this.response = responseJson;
		if (responseJson instanceof JsonObject) {
			this.status = responseJson.getAsJsonObject().get("status").getAsString().equals("success") ? Status.SUCCESS
					: Status.FAIL;
		}
		else {
			this.status = Status.SUCCESS;
		}
	}

	/**
	 * Instantiates a new totango API response. used in cases of request failure
	 *
	 * @param e
	 *            the exception thrown
	 */
	public TotangoAPIResponse(Exception e) {
		this.status = Status.FAIL;
		this.e = e;
	}

	/**
	 * Gets the response.
	 *
	 * @return the response
	 */
	public JsonElement getResponse() {
		return response;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Gets the exception.
	 *
	 * @return the exception
	 */
	public Exception getException() {
		return e;
	}

}
