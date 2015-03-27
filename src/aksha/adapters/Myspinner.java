package aksha.adapters;

import java.io.Serializable;

public class Myspinner  {
	
	
	
	        public Myspinner( String spinnerText, String value, String units ) {
	            this.spinnerText = spinnerText;
	            this.value = value;
	            this.units = units;
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

	        public String getUnits() {
				return units;
			}
	        
	        String spinnerText;
	        String value;
	        String units;
	   

}
