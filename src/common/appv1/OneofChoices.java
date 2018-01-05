package common.appv1;

/**
 * one of question choices mapping
 * @author Barry
 *
 */
public enum OneofChoices {

	GOOD("Good", 1), NORMAL("Normal", 2), BAD("Bad", 3);
	
	private final String text;
	private final int order;
	
	private OneofChoices(String text, int order) {
		this.text = text;
		this.order = order;
	}

	public String getText() {
		return text;
	}

	public int getOrder() {
		return order;
	}
	
}
