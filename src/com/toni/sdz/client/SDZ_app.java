package com.toni.sdz.client;

import com.toni.sdz.shared.FieldVerifier;
import com.toni.sdz.shared.User;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.google.gwt.user.client.Cookies;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SDZ_app implements EntryPoint {

	public LoginScreen loginScreen;

	public void onModuleLoad() {
		if (Cookies.getCookie("employeeSession") != null) {
			new EmployeeScreen(Cookies.getCookie("employeeSession"));
		} else if (Cookies.getCookie("bossSession") != null) {
			new BossScreen(Cookies.getCookie("bossSession"));
		} else if (Cookies.getCookie("directorSession") != null) {
			new DirectorScreen(Cookies.getCookie("directorSession"));
		} else {
			new LoginScreen();
		}

	}

}
