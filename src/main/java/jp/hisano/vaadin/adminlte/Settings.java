package jp.hisano.vaadin.adminlte;

final class Settings {
	static final String THEME_NAME = "admin-lte";

	static String getThemeDirectoryPath() {
		return "VAADIN/themes/" + Settings.THEME_NAME + "/";
	}
}
