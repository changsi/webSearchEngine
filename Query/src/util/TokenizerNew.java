package util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//import java.util.Enumeration;

import java.util.StringTokenizer;
import java.util.Vector;



import org.tartarus.snowball.SnowballProgram;

public class TokenizerNew {

	private final String delimiters = " \t\n\r\f~!@#$%^&*()_+|`-=\\{}�[]:\";'<>?,./'��0123456789";

	private final String language = "english";

	/**
	 * @param source
	 * @return
	 */

	public Vector<String> split(String source) {
		Vector<String> vector = new Vector<String>();
//		 extract all uppercase word and do not need stemming
        Vector<String> vectorForAllUpperCase = new Vector<String>();
		try {

			StringTokenizer stringTokenizer = new StringTokenizer(source,
					delimiters);

			while (stringTokenizer.hasMoreTokens()) {
				String token = stringTokenizer.nextToken().trim();
				Boolean hasLetter = false;
				Boolean allLetter = true;
				int index = 0;
				while (index < token.length()) {
					char temp = token.charAt(index);
					if (('a' <= temp && temp <= 'z')
							|| ('A' <= temp && temp <= 'Z')) {
						hasLetter = true;
					}
					if (temp < 'A' || ('Z' < temp && temp < 'a') || temp > 'z') {
						allLetter = false;
					}
					index++;
				}
				if (!hasLetter) {
					continue;
				}
				
				if(token.length()==1){
					continue;
				}
		        if(allLetter){
		        	/* begin to process all uppercase word */
		            boolean allUpperCase = true;
		            for(int i=0; i<token.length(); i++) {
		                if(!Character.isUpperCase(token.charAt(i))) {
		                    allUpperCase = false;
		                }
		            }
		            if(allUpperCase) {
		                vectorForAllUpperCase.addElement(token);
		            }
		            else{
		            	/*split compound word */
		                int temp_index = 0;
		                flag1: while(temp_index < token.length()) {
		                    flag2: while(true) {
		                    	temp_index++;
		                        if((temp_index == token.length()) || !Character.isLowerCase(token.charAt(temp_index))) {
		                            break flag2;
		                        }
		                    }
		                    vector.addElement(token.substring(0, temp_index).toLowerCase());
		                    token = token.substring(temp_index);
		                    temp_index = 0;
		                    continue flag1;
		                } 
		            }
		        }
			}
			stringTokenizer=null;
			/* stemmer */
	        try {
	            Class stemClass = Class.forName("org.tartarus.snowball.ext." + language + "Stemmer");
	            SnowballProgram stemmer = (SnowballProgram)stemClass.newInstance();
	            Method stemMethod = stemClass.getMethod("stem", new Class[0]);
	            Object[] emptyArgs = new Object[0];
	            for(int i=0; i<vector.size(); i++) {
	                stemmer.setCurrent((String)vector.elementAt(i));
	                stemMethod.invoke(stemmer, emptyArgs);
	                vector.setElementAt(stemmer.getCurrent(), i);
	            }
	        } catch(ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch(InstantiationException e) {
	        	e.printStackTrace();
	        } catch(NoSuchMethodException e) {
	        	e.printStackTrace();
	        } catch(IllegalAccessException e) {
	        	e.printStackTrace();
	        } catch(InvocationTargetException e) {
	        	e.printStackTrace();
	        }
			
			for(int i=0; i<vectorForAllUpperCase.size(); i++) {
	            vector.addElement(vectorForAllUpperCase.elementAt(i).toLowerCase());
	        }
			vectorForAllUpperCase=null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}

	public static void main(String[] args) {
		TokenizerNew to = new TokenizerNew();
		StopWords sw = new StopWords();

		Vector<String> str = to.split("java_hashtable");
		str = sw.removeStopWords(str);

		for (int i = 0; i < str.size(); i++) {
			System.out.println(str.get(i));
		}
	}

}
