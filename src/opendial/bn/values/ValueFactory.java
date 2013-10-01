// =================================================================                                                                   
// Copyright (C) 2011-2015 Pierre Lison (plison@ifi.uio.no)                                                                            
//                                                                                                                                     
// This library is free software; you can redistribute it and/or                                                                       
// modify it under the terms of the GNU Lesser General Public License                                                                  
// as published by the Free Software Foundation; either version 2.1 of                                                                 
// the License, or (at your option) any later version.                                                                                 
//                                                                                                                                     
// This library is distributed in the hope that it will be useful, but                                                                 
// WITHOUT ANY WARRANTY; without even the implied warranty of                                                                          
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU                                                                    
// Lesser General Public License for more details.                                                                                     
//                                                                                                                                     
// You should have received a copy of the GNU Lesser General Public                                                                    
// License along with this program; if not, write to the Free Software                                                                 
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA                                                                           
// 02111-1307, USA.                                                                                                                    
// =================================================================                                                                   

package opendial.bn.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opendial.arch.Logger;
import opendial.datastructs.Assignment;

/**
 * Factory for creating variable values
 *
 * @author  Pierre Lison (plison@ifi.uio.no)
 * @version $Date::                      $
 *
 */
public class ValueFactory {

	// logger
	public static Logger log = new Logger("ValueFactory", Logger.Level.DEBUG);

	// none value (no need to recreate one everytime)
	static NoneVal noneValue = new NoneVal();

	// pattern to find a double value
	static Pattern doublePattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

	// pattern to find an array of doubles
	static Pattern arrayPattern = Pattern.compile("\\[([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?,\\s*)*" +
			"([-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?)\\]");


	/**
	 * Creates a new value based on the provided string representation.
	 * If the string contains a numeric value, "true", "false", "None", 
	 * or opening and closing brackets, convert it to the appropriate
	 * values.  Else, returns a string value.
	 * 
	 * @param str the string representation for the value
	 * @return the resulting value
	 */
	public static Value create(String str) {
		str = str.trim();

		Matcher m = doublePattern.matcher(str);
		if (m.matches()) {
			return new DoubleVal(Double.parseDouble(str));
		}
		else if (str.equalsIgnoreCase("true")) {
			return new BooleanVal(true);
		}
		else if (str.equalsIgnoreCase("false")) {
			return new BooleanVal(false);
		}
		else if (str.equalsIgnoreCase("None")) {
			return none();
		}
		// adds the converted value
		else {
			Matcher m2 = arrayPattern.matcher(str);
			if (m2.matches()){
				List<Double> subVals = new ArrayList<Double>();
				for (String subVal : str.replace("[", "").replace("]", "").split(",")) {
					subVals.add(Double.parseDouble(subVal));
				}
				return new ArrayVal(subVals);
			}
			else if (str.startsWith("[") && str.endsWith("]")) {

				Set<Value> subVals = new HashSet<Value>();
				for (String subVal : str.replace("[", "").replace("]", "").split(",")) {
					if (subVal.length() > 0) {
						subVals.add(create(subVal.trim()));
					}
				}
				return new SetVal(subVals);
			}
		}

		return new StringVal(str);
	}

	/**
	 * Returns a double value given the double
	 * 
	 * @param d the double
	 * @return the value
	 */
	public static DoubleVal create(double d) {
		return new DoubleVal(d);
	}


	public static ArrayVal create(Double[] d) {
		return new ArrayVal(d);
	}

	/**
	 * Returns the boolean value given the boolean
	 * 
	 * @param b the boolean 
	 * @return the double
	 */
	public static BooleanVal create(boolean b) {
		return new BooleanVal(b);
	}

	/**
	 * Returns the set value given the values
	 * 
	 * @param vals the values
	 * @return the set value
	 */
	public static SetVal create(Value...vals) {
		return new SetVal(vals);
	}

	/**
	 * Returns the set value given the values
	 * 
	 * @param vals the values
	 * @return the set value
	 */
	public static SetVal create(Collection<Value> vals) {
		return new SetVal(vals);
	}


	/**
	 * Returns the none value
	 * 
	 * @return the none value
	 */
	public static NoneVal none() {
		return noneValue;
	}
}
