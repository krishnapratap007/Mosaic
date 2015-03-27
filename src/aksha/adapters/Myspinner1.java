package aksha.adapters;

import java.io.Serializable;

public class Myspinner1  {
	
	
	
	        public Myspinner1( String spinnerText, String value ) {
	            this.spinnerText = spinnerText;
	            this.value = value;
	        }

	        public String getSpinnerText() {
	            return spinnerText;
	        }

	        public String getValue() {
	            return value;
	        }

	        public String toString() {
	            return spinnerText;
	        }

	        String spinnerText;
	        String value;
	   

}
