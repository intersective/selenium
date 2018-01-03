package respositry;


import java.util.HashMap;


public class LocalStore {

	private HashMap<String,String> store;
	private String userPointsKey;
	private String userSpinChancesKey;
	
	private static class LocalStoreHolder {
		private static final LocalStore my = new LocalStore();
	}
	
	public static LocalStore getInstance() {
		return LocalStoreHolder.my;
	}
	
	private LocalStore() {
		store = new HashMap<String,String>();
	}
	
	public String getValue(String key) {
		return store.get(key);
	}
	
	public void addValue(String key, String value) {
		store.put(key, value);
	}

	public void addUserPoints(String value) {
		addValue(userPointsKey, value);
	}
	
	public String getUserPotins() {
		return getValue(userPointsKey);
	}
	
	public void addSpinChances(String value) {
		addValue(userSpinChancesKey, value);
	}
	
	public String getSpinChances() {
		return getValue(userSpinChancesKey);
	}

	public void setUserPointsKey(String userPointsKey) {
		this.userPointsKey = userPointsKey;
	}

	public void setUserSpinChancesKey(String userSpinChancesKey) {
		this.userSpinChancesKey = userSpinChancesKey;
	}
	
}
