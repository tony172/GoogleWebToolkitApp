package com.toni.sdz.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.toni.sdz.shared.User;

public class RegisterScreen {

	public RegisterScreen() {

		final VLayout v = new VLayout();
		DynamicForm dForm = new DynamicForm();
		HLayout buttons = new HLayout();
		buttons.setHeight(30);
		buttons.setWidth(200);
		IButton btnNewUser = new IButton("Registriraj se");
		IButton btnCancel = new IButton("Odustani");
		buttons.addMember(btnNewUser);
		buttons.addMember(btnCancel);
		dForm.setHeight(300);
		dForm.setWidth(300);
		dForm.setBorder("1px solid grey");
		final TextItem txtName = new TextItem("Ime");
		final TextItem txtSurname = new TextItem("Prezime");
		final TextItem txtUsername = new TextItem("Korisnicko ime");
		final TextItem txtPassword = new TextItem("Lozinka");
		final ComboBoxItem list = new ComboBoxItem("Tip zaposlenika");
		list.setValueMap("Radnik", "Sef", "Direktor");
		dForm.setFields(new FormItem[] { txtName, txtSurname, txtUsername, txtPassword, list });
		v.setHeight100();
		v.setWidth100();
		v.setBackgroundColor("#c3cfe2");
		dForm.setAlign(Alignment.CENTER);
		dForm.setTitleOrientation(TitleOrientation.LEFT);
		v.setAlign(Alignment.CENTER);
		v.setDefaultLayoutAlign(Alignment.CENTER);
		v.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		v.addMember(dForm);
		v.addMember(buttons);
		btnNewUser.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String username = txtUsername.getValueAsString();
				String password = txtPassword.getValueAsString();
				String name = txtName.getValueAsString();
				String surname = txtSurname.getValueAsString();
				String selectedItem = list.getValueAsString();
				if (username == null || password == null || name == null || surname == null || selectedItem == null) {
					SC.say("Ispunite sva polja!");
				} else {
					User u = new User(username, password, name, surname, selectedItem);
					AppServiceAsync appService = GWT.create(AppService.class);
					appService.registerUser(u, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Boolean result) {
							if (result == true) {
								SC.say("Uspjesna registracija!");
								v.destroy();
								new LoginScreen();
							} else
								SC.say("Korisnièko ime postoji!");

						}

					});
				}

			}

		});
		
		btnCancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				v.destroy();
				new LoginScreen();
			}
			
		});
		v.draw();
		txtName.focusInItem();

	}
}
