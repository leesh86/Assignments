
/**
 * @author Lee Shoham
 *
 * Nov 15, 2017
 */

import java.util.Arrays;
import java.util.Collections;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TotangoTest extends BaseTotangoAPITest {

	/**
	 * Paying account test to validate a secific account status
	 *
	 * @param accountNum
	 *            the account num
	 * @param paying
	 *            the status
	 */
	@Parameters({ "accountNum", "paying" })
	@Test
	public void payingAccountTest(String accountNum, String paying) {

		NameValuePair query = new BasicNameValuePair("query",
				"{\"terms\": [{\"type\": \"string\",\"term\": \"identifier\",\"eq\": \"" + accountNum
						+ "\"}],\"fields\": [{\"type\": \"string\",\"term\": \"health\"},{\"type\": \"string\",\"term\": \"status_group\",\"field_display_name\": \"Status type\"}],\"scope\": \"all\"}");
		NameValuePair dateTerm = new BasicNameValuePair("date_term",
				"{\"type\":\"date\",\"term\":\"date\",\"joker\":\"in_previous_1_day\"}");

		TotangoAPIResponse response = executeTotangoPostAPIRequest("v1/trend/accounts.json",
				Arrays.asList(query, dateTerm));

		JsonArray selectedFields = response.getResponse().getAsJsonObject().getAsJsonObject("response")
				.getAsJsonArray("accounts").get(0).getAsJsonObject().getAsJsonArray("trend").get(0).getAsJsonObject()
				.getAsJsonArray("selected_fields");
		Assert.assertEquals(selectedFields.get(1).getAsString(), paying);
	}

	/**
	 * Segments test to get list of accounts in list1 and validate they have an
	 * engagement score
	 */
	@Test
	public void segmentsTest() {

		/*
		 * 1. active-lists/folders -> get list 1 id 2. accounts/active_list.json -> get
		 * list criteria from active_list.json, using the id retrieved 3.
		 * search/accounts -> use criteria to search accounts
		 */
		TotangoAPIResponse response = executeTotangoGetAPIRequest("v1/active-lists/folders");
				
		JsonArray folders = response.getResponse().getAsJsonObject().getAsJsonObject("response")
				.getAsJsonObject("folders").getAsJsonArray("children");
		JsonObject testingFolder = getFolderByName("Testing", folders);
		JsonObject homeTestFolder = getFolderByName("home test", testingFolder.getAsJsonArray("children"));
		JsonObject list1Folder = getFolderByName("list 1", homeTestFolder.getAsJsonArray("children"));
		String id = list1Folder.get("id").getAsString();

		response = executeTotangoGetAPIRequest("v1/accounts/active_list.json");
		JsonArray lists = response.getResponse().getAsJsonObject().getAsJsonArray("active-lists");
		JsonObject list = getListById(id, lists);
		JsonArray criteria = list.getAsJsonArray("criteria");
		NameValuePair query = new BasicNameValuePair("query", "{\"scope\":\"all\",\"terms\":" + criteria.toString()
				+ ",\"fields\":[{\"type\":\"number\",\"term\":\"score\",\"field_display_name\":\"Engagement \",\"desc\":true}]}");

		response = executeTotangoPostAPIRequest("v1/search/accounts", Collections.singletonList(query));
		validateEngagement(response.getResponse().getAsJsonObject());
	}

	@Test
	public void completedTasksTest() {

		TotangoAPIResponse response = executeTotangoGetAPIRequest("v3/tasks?account_id=9092&status=closed&assignee=adi%2B777%40totango.com&assigner=amit.levi%40totango.com");
				
		Assert.assertTrue(response.getResponse().getAsJsonArray().size() >= 1);
	}

	/**
	 * Validate engagement.
	 *
	 * @param response
	 *            the json object with list of accounts
	 */
	private void validateEngagement(JsonObject response) {

		JsonArray accounts = response.getAsJsonObject("response").getAsJsonObject("accounts").getAsJsonArray("hits");
		for (JsonElement account : accounts) {

			JsonObject accountJson = account.getAsJsonObject();
			int selectedFieldsLength = accountJson.getAsJsonArray("selected_fields").size();
			Assert.assertNotEquals(0, selectedFieldsLength);
		}

	}

	/**
	 * Gets the list by id from an array of lists.
	 *
	 * @param id
	 *            the id
	 * @param lists
	 *            the lists array
	 * @return the list by id
	 */
	private JsonObject getListById(String id, JsonArray lists) {

		JsonObject listJson = null;

		for (JsonElement list : lists) {
			listJson = list.getAsJsonObject();
			if (listJson.get("id").getAsString().equals(id)) {
				return listJson;
			}
		}
		return null;
	}

	/**
	 * Gets the folder by name from an array of folders.
	 *
	 * @param folderName
	 *            the folder name
	 * @param folders
	 *            the folders array
	 * @return the folder by name
	 */
	private JsonObject getFolderByName(String folderName, JsonArray folders) {

		JsonObject folderJson = null;

		for (JsonElement folder : folders) {
			folderJson = folder.getAsJsonObject();
			if (folderJson.get("name").getAsString().equals(folderName)) {
				return folderJson;
			}
		}
		return null;
	}

}
