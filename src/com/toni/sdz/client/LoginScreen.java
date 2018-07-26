package com.toni.sdz.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.toni.sdz.shared.User;
import com.google.gwt.user.client.Cookies;

public class LoginScreen {
	private final AppServiceAsync appService = GWT.create(AppService.class);
	private User user;
	VLayout v;

	public LoginScreen() {
		v = new VLayout();
		v.setBackgroundColor("#c3cfe2");
		DynamicForm dForm = new DynamicForm();
		dForm.setBorder("1px solid grey");
		IButton btnLogin = new IButton("Prijavi se");
		IButton btnRegister = new IButton("Registriraj se");
		HLayout buttons = new HLayout(10);
		buttons.setWidth(100);
		buttons.setHeight(100);
		buttons.addMember(btnLogin);
		buttons.addMember(btnRegister);
		v.setHeight100();
		v.setWidth100();
		dForm.setWidth(300);
		dForm.setHeight(100);
		final TextItem txtUser = new TextItem("Korisnicko ime");
		final PasswordItem txtPass = new PasswordItem("Lozinka");
		dForm.setItems(txtUser, txtPass);
		dForm.setAlign(Alignment.CENTER);
		dForm.setTitleOrientation(TitleOrientation.LEFT);
		v.setAlign(Alignment.CENTER);
		// h.setAlign(VerticalAlignment.CENTER);
		v.addMember(dForm);
		v.addMember(buttons);
		v.setDefaultLayoutAlign(Alignment.CENTER);
		v.setDefaultLayoutAlign(VerticalAlignment.CENTER);
		btnLogin.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if (txtUser.getValueAsString() == null || txtPass.getValueAsString() == null)
					SC.say("Ispunite sva polja!");
				else {
					user = new User(txtUser.getValueAsString(), txtPass.getValueAsString());

					sendNameToServer();
				}
			}
		});

		btnRegister.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				// TODO Auto-generated method stub
				v.destroy();
				new RegisterScreen();
			}

		});
		v.draw();
		txtUser.focusInItem();
	}

	private void sendNameToServer() {
		// First, we validate the input.

		appService.loginUser(user, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("Greska");
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				if (result != "" && result.length() > 8) {
					SC.say(result);
				} else {
					if (result.equals("Radnik")) {
						Cookies.setCookie("employeeSession", user.getUserName());
						new EmployeeScreen(user.getUserName());
					} else if (result.equals("Sef")) {
						Cookies.setCookie("bossSession", user.getUserName());
						new BossScreen(user.getUserName());
					} else if (result.equals("Direktor")) {
						Cookies.setCookie("directorSession", user.getUserName());
						new DirectorScreen(user.getUserName());
					} else {
						;
					}
					v.destroy();
				}

			}

		});
	}
}
