package respositry;


import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import service.Tools;

import common.ShareConfig;


public class Settings {

	private static class SettingsHolder {
		private static final Settings my = new Settings();
	}
	
	public static Settings getInstance() {
		return SettingsHolder.my;
	}
	
	private Settings() {
		HashMap<String,String> store = Tools.readProperties(String.format("%s%s%s", System.getProperty("user.dir"), File.separator, "test.properties"));
		store.putAll(Tools.readProperties(String.format("%s%s%s", System.getProperty("user.dir"), File.separator, "user.properties")));
		try {
			Class<?> cc = Class.forName("common.BuildConfig");
			Object t = cc.newInstance();
			Set<Entry<String,String>> se = store.entrySet();
			for (Entry<String,String> e : se) {
				Field f = cc.getField(e.getKey());
				if ("int".equals(f.getType().getSimpleName())) {
					f.setInt(t, Integer.parseInt(e.getValue()));
				} else if ("double".equals(f.getType().getSimpleName())) {
					f.setDouble(t, Double.parseDouble(e.getValue()));
				} else if ("boolean".equals(f.getType().getSimpleName())) {
					f.setBoolean(t, new Boolean(e.getValue()).booleanValue());
				} else {
					f.set(t, e.getValue());
				}
			}
			String localRunId = UUID.randomUUID().toString();
			System.out.println(String.format("local run id %s", localRunId));
			ShareConfig.seleniumRunId = localRunId;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | IllegalArgumentException
				| NoSuchFieldException | SecurityException e1) {
			e1.printStackTrace();
		}
	}
	
}
