package com.toni.sdz.client;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mysql.jdbc.Connection;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class EmployeeScreen {
	private ListGridRecord[] records1;
	private ListGridRecord[] records2;
	final String usernameF;
	final ListGrid list1 = new ListGrid();
	final ListGrid list2 = new ListGrid();
	private final AppServiceAsync appService = GWT.create(AppService.class);

	public EmployeeScreen(String username) {
		if (Cookies.getCookie("reservation") != null) {
			appService.unreserveOrder(Integer.parseInt(Cookies.getCookie("reservation")), null);
			Cookies.removeCookie("reservation");
		}
		usernameF = username;
		final VLayout v = new VLayout();
		HLayout nalozi = new HLayout();
		list1.setTitle("Zadani nalozi");
		list1.setSortField("type");
		list2.setTitle("Izvrseni nalozi");
		ListGridField type = new ListGridField("type", "");
		type.setHidden(true);
		ListGridField ID = new ListGridField("ID", "ID");
		ID.setHidden(true);
		list1.setFields(new ListGridField[] { ID, type, new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("nalog_zadao", "Nalog zadao"), new ListGridField("datum", "Vrijeme i datum") });
		list2.setFields(new ListGridField[] { new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("nalog_zadao", "Nalog zadao"), new ListGridField("datum", "Izraden"),
				new ListGridField("komentar", "Komentar"), new ListGridField("status", "Nalog izvrsen") });
		ListGridRecord rec = new ListGridRecord();
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

		appService.fetchData(
				username, 0, "SELECT * FROM nalozi WHERE kome='" + usernameF + "' OR (vrsta=1 AND rezervirao='"
						+ usernameF + "') OR" + "(vrsta=1 AND rezervirao IS NULL)",
				new AsyncCallback<ArrayList<String>[]>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<String>[] result) {
						ArrayList<String> l1 = result[0];
						ArrayList<String> l2 = result[1];
						records1 = new ListGridRecord[l1.size() / 5];
						for (int i = 0, j = 0; i < l1.size(); i += 5, j++) {
							ListGridRecord rec = new ListGridRecord();
							rec.setAttribute("ID", l1.get(i));
							rec.setAttribute("type", l1.get(i + 1));
							rec.setAttribute("opis_naloga", l1.get(i + 2));
							rec.setAttribute("nalog_zadao", l1.get(i + 3));
							rec.setAttribute("datum", l1.get(i + 4));
							records1[j] = rec;
						}
						list1.setData(records1);
						records2 = new ListGridRecord[l2.size() / 5];
						for (int i = 0, j = 0; i < l2.size(); i += 5, j++) {
							ListGridRecord rec = new ListGridRecord();
							rec.setAttribute("opis_naloga", l2.get(i));
							rec.setAttribute("nalog_zadao", l2.get(i + 1));
							rec.setAttribute("datum", l2.get(i + 2));
							rec.setAttribute("komentar", l2.get(i + 3));
							rec.setAttribute("status", l2.get(i + 4));
							records2[j] = rec;
						}
						list2.setData(records2);
					}

				});

		IButton logOut = new IButton("Odjavi se!");
		logOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Cookies.removeCookie("employeeSession");
				v.destroy();
				new LoginScreen();
			}

		});
		list1.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				final ListGridRecord r = list1.getSelectedRecord();
				Cookies.setCookie("reservation", r.getAttribute("ID"));
				appService.reserveOrder(r.getAttributeAsInt("ID"), usernameF, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						SC.say("Došlo je do greške. Pokušajte ponovo.");
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result == false) {
							SC.say("Nalog je vec rezerviran!");
						} else
							drawWindow(r);

					}

				});

			}

		});

		logOut.setHeight(30);
		header.addMember(loggedUser);
		header.addMember(logOut);
		header.draw();

		v.addMember(header);

		v.addMember(nalozi);
		v.draw();

	}

	void drawWindow(ListGridRecord r) {
		final HLayout blur = new HLayout();
		blur.setWidth100();
		blur.setHeight100();
		blur.setBackgroundColor("#111111");
		blur.setOpacity(60);
		blur.draw();
		final HLayout windowLayout = new HLayout();
		windowLayout.setWidth100();
		windowLayout.setHeight100();
		// windowLayout.setAlign(Alignment.CENTER);
		// windowLayout.setDefaultLayoutAlign(Alignment.CENTER);
		// windowLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		Window w = new Window();
		w.setTitle("");
		w.setShowMinimizeButton(false);
		w.setShowCloseButton(false);
		w.setWidth("40%");
		w.setHeight("90%");
		w.setSnapTo("C");
		final IButton btnCancel = new IButton("Odustani");
		final IButton btnExecute = new IButton("Izvrsi nalog");
		HLayout buttons = new HLayout();
		buttons.setHeight(30);
		buttons.setWidth(200);
		buttons.addMember(btnExecute);
		buttons.addMember(btnCancel);
		w.addMember(buttons);
		Label details = new Label("<b>Opis naloga:</b> " + " " + r.getAttributeAsString("opis_naloga") + "<br/><br/>"
				+ "<b>Nalog zadao:</b> " + " " + r.getAttributeAsString("nalog_zadao") + "<br/><br/>" + "<b>Datum:<b>"
				+ " " + r.getAttributeAsString("datum"));
		details.setWidth(300);
		details.setHeight(70);
		DynamicForm wForm = new DynamicForm();
		final TextAreaItem txtComm = new TextAreaItem("Komentar");
		txtComm.setWidth(250);
		txtComm.setHeight(60);
		wForm.setItems(new FormItem[] { txtComm });
		// wForm.setBorder("1px solid grey");
		wForm.setHeight(100);
		wForm.setWidth(300);
		wForm.setAlign(Alignment.LEFT);
		VLayout formLayout = new VLayout();
		formLayout.setWidth100();
		formLayout.setHeight100();
		//formLayout.setBorder("2px solid black");
		formLayout.setAlign(Alignment.CENTER);
		wForm.setTitleOrientation(TitleOrientation.LEFT);
		formLayout.addMember(details);
		formLayout.addMember(wForm);
		formLayout.setDefaultLayoutAlign(Alignment.CENTER);
		formLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		// details.setSnapTo("C");
		// wForm.setSnapTo("C");
		w.addItem(formLayout);
		windowLayout.addChild(w);
		windowLayout.draw();
		final int ID = r.getAttributeAsInt("ID");
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appService.unreserveOrder(ID, null);
				blur.destroy();
				windowLayout.destroy();
				Cookies.removeCookie("reservation");
			}

		});
		btnExecute.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				appService.executeOrder(ID, txtComm.getValueAsString(), new AsyncCallback() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Object result) {
						appService.fetchData(usernameF, 0,
								"SELECT * FROM nalozi WHERE kome='" + usernameF + "' OR (vrsta=1 AND rezervirao='"
										+ usernameF + "') OR" + "(vrsta=1 AND rezervirao IS NULL)",
								new AsyncCallback<ArrayList<String>[]>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(ArrayList<String>[] result) {
								ArrayList<String> l1 = result[0];
								ArrayList<String> l2 = result[1];
								records1 = new ListGridRecord[l1.size() / 5];
								for (int i = 0, j = 0; i < l1.size(); i += 5, j++) {
									ListGridRecord rec = new ListGridRecord();
									rec.setAttribute("ID", l1.get(i));
									rec.setAttribute("type", l1.get(i + 1));
									rec.setAttribute("opis_naloga", l1.get(i + 2));
									rec.setAttribute("nalog_zadao", l1.get(i + 3));
									rec.setAttribute("datum", l1.get(i + 4));
									records1[j] = rec;
								}
								list1.setData(records1);
								records2 = new ListGridRecord[l2.size() / 5];
								for (int i = 0, j = 0; i < l2.size(); i += 5, j++) {
									ListGridRecord rec = new ListGridRecord();
									rec.setAttribute("opis_naloga", l2.get(i));
									rec.setAttribute("nalog_zadao", l2.get(i + 1));
									rec.setAttribute("datum", l2.get(i + 2));
									rec.setAttribute("komentar", l2.get(i + 3));
									rec.setAttribute("status", l2.get(i + 4));
									records2[j] = rec;
								}
								list2.setData(records2);
								list1.redraw();
								list2.redraw();

							}

						});

					}

				});
				blur.destroy();
				windowLayout.destroy();
				Cookies.removeCookie("reservation");
			}
		});

	}
}
