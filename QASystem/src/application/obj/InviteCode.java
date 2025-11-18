package application.obj;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class InviteCode {

	private final SimpleStringProperty code;
	private final SimpleBooleanProperty used;

	public InviteCode(String code, boolean hasBeenUsed) {
		this.code = new SimpleStringProperty(code);
		this.used = new SimpleBooleanProperty(hasBeenUsed);
	}

	public String getCode() {
		return this.code.get();
	}

	public boolean isUsed() {
		return this.used.get();
	}

	public void setUsed(boolean isUsed) {
		this.used.set(isUsed);
	}

	public SimpleBooleanProperty getUsedProperty() {
		return this.used;
	}
}
