package com.amazonaws.tvmclient;

import java.util.HashMap;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;

public class DBManager {

	public static PutItemResult insert(AmazonClientManager clientManager, String deviceid, String timestamp, 
			String latitude, String longitude, String path, String category) {
		try {
			HashMap<String, AttributeValue> item = new HashMap<String, AttributeValue>();

			item.put("deviceid", new AttributeValue().withS(deviceid));
			item.put("timestamp", new AttributeValue().withS(timestamp));
			item.put("latitude", new AttributeValue().withS(latitude));
			item.put("longitude", new AttributeValue().withS(longitude));
			item.put("path", new AttributeValue().withS(path));
			item.put("category", new AttributeValue().withS(category));

			PutItemRequest request = new PutItemRequest().withTableName(
					PropertyLoader.getInstance().getTableName())
					.withItem(item);

			return clientManager.getAmazonDBClient().putItem(request);



		} catch (AmazonServiceException ex) {
			clientManager.wipeCredentialsOnAuthError(ex);
			return null;
		}
	}
	
}
