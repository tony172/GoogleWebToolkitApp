package com.toni.sdz.client;

import com.toni.sdz.shared.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface AppServiceAsync {
	void loginUser(User u, AsyncCallback<String> callback);

	void registerUser(User u, AsyncCallback<Boolean> callback);

	void getName(String username, AsyncCallback<String> callback);

	void fetchData(String username, int who, String query, AsyncCallback<ArrayList<String>[]> callback);

	void reserveOrder(int ID, String username, AsyncCallback<Boolean> callback);

	void unreserveOrder(int ID, AsyncCallback<Void> callback);

	void executeOrder(int ID, String comment, AsyncCallback<Void> callback);

	void updateComboBox(AsyncCallback<HashMap<String, String>> callback);

	void makeOrder(String description, String username, String employee, int vrsta, AsyncCallback<Void> callback);
}
