package com.toni.sdz.client;

import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class DirectorScreen {
	final ListGrid list1 = new ListGrid();
	final ListGrid list2 = new ListGrid();
	private final AppServiceAsync appService = GWT.create(AppService.class);

	public DirectorScreen(String username) {
		final String usernameF = username;
		final VLayout v = new VLayout();
		HLayout nalozi = new HLayout();
		list1.setTitle("Zadani nalozi");
		ListGridField type = new ListGridField("vrsta", "");
		list1.setSortField("vrsta");
		type.setHidden(true);
		list2.setTitle("Izvrseni nalozi");
		list1.setFields(new ListGridField[] { type, new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("zadao", "Zadao"), new ListGridField("datum", "Datum"),
				new ListGridField("kome", "Kome") });
		list2.setFields(new ListGridField[] { new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("zadao", "Zadao"), new ListGridField("datum", "Datum izrade"),
				new ListGridField("radnik", "Izvrsio"), new ListGridField("komentar", "Komentar"),
				new ListGridField("status", "Datum izvrsenja") });
		list1.draw();
		list2.draw();
		nalozi.addMember(list1);
		nalozi.addMember(list2);
		nalozi.draw();
		v.setWidth100();
		v.setHeight100();
		v.setBackgroundColor("#c3cfe2");
		HLayout header = new HLayout();
		header.setAlign(Alignment.RIGHT);
		header.setHeight(30);
		appService.fetchData("", 2, "SELECT * FROM mydb.nalozi", new AsyncCallback<ArrayList<String>[]>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(ArrayList<String>[] result) {
				ArrayList<String> l1 = result[0];
				ArrayList<String> l2 = result[1];
				ListGridRecord[] records1 = new ListGridRecord[l1.size() / 5];
				for (int i = 0, j = 0; i < l1.size(); i += 5, j++) {
					ListGridRecord rec = new ListGridRecord();
					rec.setAttribute("vrsta", l1.get(i));
					rec.setAttribute("opis_naloga", l1.get(i + 1));
					rec.setAttribute("zadao", l1.get(i + 2));
					rec.setAttribute("datum", l1.get(i + 3));
					rec.setAttribute("kome", l1.get(i + 4));
					records1[j] = rec;
				}
				list1.setData(records1);

				ListGridRecord[] records2 = new ListGridRecord[l2.size() / 6];
				for (int i = 0, j = 0; i < l2.size(); i += 6, j++) {
					ListGridRecord rec = new ListGridRecord();
					rec.setAttribute("opis_naloga", l2.get(i));
					rec.setAttribute("zadao", l2.get(i + 1));
					rec.setAttribute("datum", l2.get(i + 2));
					rec.setAttribute("radnik", l2.get(i + 3));
					rec.setAttribute("komentar", l2.get(i + 4));
					rec.setAttribute("status", l2.get(i + 5));
					records2[j] = rec;
				}
				list2.setData(records2);
			}

		});
		final Label loggedUser = new Label();
		appService.getName(username, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(String result) {
				loggedUser.setContents("Prijavljeni ste kao " + "<b>" + result + "</b>");

			}

		});
		IButton logOut = new IButton("Odjavi se!");
		logOut.setHeight(30);
		logOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Cookies.removeCookie("directorSession");
				v.destroy();
				new LoginScreen();
			}

		});
		header.addMember(loggedUser);
		header.addMember(logOut);
		header.draw();

		v.addMember(header);

		v.addMember(nalozi);
		v.draw();
	}
}
