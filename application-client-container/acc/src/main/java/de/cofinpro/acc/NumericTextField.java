package de.cofinpro.acc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class NumericTextField extends TextField {

	public NumericTextField() {
		textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				if (isEmpty(newValue) || isInteger(newValue)) {
					setText(newValue);
				} else {
					setText(oldValue);
				}
			}

			private boolean isEmpty(String newValue) {
				return newValue == null || newValue.isEmpty();
			}

			private boolean isInteger(String newValue) {
				try {
					Integer.valueOf(newValue);
					return true;
				} catch (NumberFormatException e) {
					System.out.println("Ignoring invalid value...");
					return false;
				}
			}
		});
	}
	
}
