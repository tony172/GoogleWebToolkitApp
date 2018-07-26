package com.toni.sdz.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class BossScreen {

	final ListGrid list1 = new ListGrid();
	final ListGrid list2 = new ListGrid();
	private final AppServiceAsync appService = GWT.create(AppService.class);

	public BossScreen(String username) {
		final String usernameF = username;
		final VLayout v = new VLayout();
		HLayout nalozi = new HLayout();
		list1.setTitle("Zadani nalozi");
		list2.setTitle("Izvrseni nalozi");
		ListGridField type = new ListGridField("vrsta", "");
		list1.setSortField("vrsta");
		type.setHidden(true);
		list1.setFields(new ListGridField[] { type, new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("datum", "Datum"), new ListGridField("kome", "Kome") });
		list2.setFields(new ListGridField[] { new ListGridField("opis_naloga", "Opis naloga"),
				new ListGridField("datum", "Datum izrade"), new ListGridField("radnik", "Izvrsio"),
				new ListGridField("komentar", "Komentar"), new ListGridField("status", "Datum izvrsenja") });
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

		appService.fetchData("", 1, "SELECT * FROM mydb.nalozi WHERE zadao='" + username + "'",
				new AsyncCallback<ArrayList<String>[]>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ArrayList<String>[] result) {
						ArrayList<String> l1 = result[0];
						ArrayList<String> l2 = result[1];
						ListGridRecord[] records1 = new ListGridRecord[l1.size() / 4];
						for (int i = 0, j = 0; i < l1.size(); i += 4, j++) {
							ListGridRecord rec = new ListGridRecord();
							rec.setAttribute("vrsta", l1.get(i));
							rec.setAttribute("opis_naloga", l1.get(i + 1));
							rec.setAttribute("datum", l1.get(i + 2));
							rec.setAttribute("kome", l1.get(i + 3));
							records1[j] = rec;
						}
						list1.setData(records1);

						ListGridRecord[] records2 = new ListGridRecord[l2.size() / 5];
						for (int i = 0, j = 0; i < l2.size(); i += 5, j++) {
							ListGridRecord rec = new ListGridRecord();
							rec.setAttribute("opis_naloga", l2.get(i));
							rec.setAttribute("datum", l2.get(i + 1));
							rec.setAttribute("radnik", l2.get(i + 2));
							rec.setAttribute("komentar", l2.get(i + 3));
							rec.setAttribute("status", l2.get(i + 4));
							records2[j] = rec;
						}
						list2.setData(records2);
					}

				});

		IButton logOut = new IButton("Odjavi se!");
		IButton createOrder = new IButton("Izradi nalog");
		logOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Cookies.removeCookie("bossSession");
				v.destroy();
				new LoginScreen();
			}

		});
		createOrder.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				drawWindow(usernameF);
			}

		});

		logOut.setHeight(30);
		createOrder.setHeight(30);
		header.addMember(loggedUser);
		header.addMember(createOrder);
		header.addMember(logOut);
		header.draw();

		v.addMember(header);

		v.addMember(nalozi);
		v.draw();

	}

	void drawWindow(String username) {
		final String usernameF = username;
		final HLayout windowLayout = new HLayout();
		windowLayout.setWidth100();
		windowLayout.setHeight100();
		Window w = new Window();
		w.setTitle("Novi nalog");
		w.setShowMinimizeButton(false);
		w.setShowCloseButton(false);
		w.setWidth("40%");
		w.setHeight("90%");
		w.setSnapTo("C");
		final IButton btnCancel = new IButton("Odustani");
		final IButton btnExecute = new IButton("Napravi nalog");
		w.addMember(btnCancel);
		w.addMember(btnExecute);
		DynamicForm wForm = new DynamicForm();
		final TextAreaItem txtDescription = new TextAreaItem("Opis naloga");
		final CheckboxItem opciNalog = new CheckboxItem("Opci nalog ");
		final ComboBoxItem radnici = new ComboBoxItem("Radnik ");
		opciNalog.setValue(false);
		ListGridField fieldID = new ListGridField("id");
		fieldID.setHidden(true);
		radnici.setPickListFields(new ListGridField("Ime i prezime"), fieldID);
		radnici.setDisplayField("Ime i prezime");
		radnici.setValueField("id");
		opciNalog.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (opciNalog.getValueAsBoolean() == false) {
					radnici.disable();
					radnici.clearValue();
				} else {
					radnici.enable();
				}

			}

		});
		appService.updateComboBox(new AsyncCallback<HashMap<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(HashMap<String, String> result) {
				radnici.setValueMap(result);

			}

		});
		txtDescription.setWidth(300);
		txtDescription.setHeight(50);
		opciNalog.setWidth(300);
		opciNalog.setHeight(30);
		radnici.setWidth(200);
		radnici.setHeight(20);
		wForm.setItems(new FormItem[] { txtDescription, opciNalog, radnici });
		VLayout formLayout = new VLayout(20);
		wForm.setTitleOrientation(TitleOrientation.LEFT);
		wForm.setHeight(15);
		wForm.setWidth(300);
		formLayout.setWidth100();
		formLayout.setHeight100();
		formLayout.setAlign(Alignment.CENTER);
		wForm.setBorder("1px solid grey");
		wForm.setAlign(Alignment.CENTER);
		formLayout.addMember(wForm);
		formLayout.setDefaultLayoutAlign(Alignment.CENTER);
		formLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		w.addItem(formLayout);
		windowLayout.addChild(w);
		windowLayout.draw();
		btnExecute.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (txtDescription.getValueAsString().equals(null)
						|| (opciNalog.getValueAsBoolean() == false && radnici.getValueAsString() == null)) {
					SC.say("Popunite sva polja!");
				} else {
					int vrsta = 0;
					if (opciNalog.getValueAsBoolean() == true)
						vrsta = 1;
					appService.makeOrder(txtDescription.getValueAsString(), usernameF, radnici.getValueAsString(),
							vrsta, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							appService.fetchData("", 1, "SELECT * FROM mydb.nalozi WHERE zadao='" + usernameF + "'",
									new AsyncCallback<ArrayList<String>[]>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(ArrayList<String>[] result) {
									ArrayList<String> l1 = result[0];
									ArrayList<String> l2 = result[1];
									ListGridRecord[] records1 = new ListGridRecord[l1.size() / 4];
									for (int i = 0, j = 0; i < l1.size(); i += 4, j++) {
										ListGridRecord rec = new ListGridRecord();
										rec.setAttribute("vrsta", l1.get(i));
										rec.setAttribute("opis_naloga", l1.get(i + 1));
										rec.setAttribute("datum", l1.get(i + 2));
										rec.setAttribute("kome", l1.get(i + 3));
										records1[j] = rec;
									}
									list1.setData(records1);

									ListGridRecord[] records2 = new ListGridRecord[l2.size() / 5];
									for (int i = 0, j = 0; i < l2.size(); i += 5, j++) {
										ListGridRecord rec = new ListGridRecord();
										rec.setAttribute("opis_naloga", l2.get(i));
										rec.setAttribute("datum", l2.get(i + 1));
										rec.setAttribute("radnik", l2.get(i + 2));
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
					windowLayout.destroy();

				}
			}

		});

		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				windowLayout.destroy();
			}

		});
	}
}
