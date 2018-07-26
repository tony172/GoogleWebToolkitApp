package com.toni.sdz.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.toni.sdz.shared.User;

@RemoteServiceRelativePath("service")
public interface AppService extends RemoteService {
	String loginUser(User u);

	boolean registerUser(User u);

	String getName(String username);

	ArrayList<String>[] fetchData(String username, int who, String query);

	boolean reserveOrder(int ID, String username);

	void unreserveOrder(int ID);

	void executeOrder(int ID, String comment);

	HashMap<String, String> updateComboBox();

	void makeOrder(String description, String username, String employee, int vrsta);
}
