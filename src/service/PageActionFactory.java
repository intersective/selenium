package service;


import java.util.Arrays;

import com.google.common.base.Throwables;
import common.BuildConfig;


public class PageActionFactory {

	private static class PageActionFactoryHolder {
		private static final PageActionFactory my = new PageActionFactory();
	}
	
	public static PageActionFactory getInstance() {
		return PageActionFactoryHolder.my;
	}
	
	private PageActionFactory() {
	}
	
	public PageAction build(String className) {
		String[] path = className.split("\\.");
		if (BuildConfig.mobile) {
			String header = String.join(".", Arrays.copyOfRange(path, 0, path.length - 1));
			if (BuildConfig.androidDeviceSerial != null) {
				PageAction pa = create(String.format("%s.Android%s", header, path[path.length - 1]));
				if (pa == null) {
					return create(String.format("%s.Mobile%s", header, path[path.length - 1]));
				} else {
					return pa;
				}
			} else {
				return create(String.format("%s.Mobile%s", header, path[path.length - 1]));
			}
		} else {
			return create(className);
		}
	}
	
	private PageAction create(String className) {
		try {
			Class<?> clz = Class.forName(className);
			return (PageAction) clz.newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return null;
	}
	
}
