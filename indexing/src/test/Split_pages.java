package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Split_pages {

	public Map<String,ArrayList<String>> split(ArrayList<String> index, String data) {
		ArrayList<String> pages = new ArrayList<String>();
		Map<String,ArrayList<String>> result=new HashMap<String, ArrayList<String>>();
		ArrayList<String> URLs = new ArrayList<String>();
		Boolean status = true;
		String status_line;
		int lenth;
		String HTTP_response;
		String HTML;
		for (int i = 0; i < index.size(); i++) {
			status_line = index.get(i);
			String delimiter = " ";
			String[] temp;
			temp = status_line.trim().split(delimiter);
			if (temp[6].trim().equals("ok")) {
				status = true;
			} else {
			}
			lenth = Integer.parseInt(temp[3].trim());
			HTTP_response = data.substring(0, lenth);
			try {
				data = data.substring(lenth);
				if (status) {
					if (HTTP_response.indexOf("\r\n\r\n") > 0) {
						HTML = HTTP_response.substring(HTTP_response
								.indexOf("\r\n\r\n"));
						pages.add(HTML);
						URLs.add(temp[0]);
					}
				} else {
				}
			} catch (NumberFormatException nfe) {
				System.out.println("NumberFormatException: " + nfe.getMessage());
			} catch (Exception e) {
				System.out.println(i);
				System.out.println(HTTP_response);
				System.out.println("Splite Exception: " + e.getMessage());
				System.err.println("Error: " + e);
				e.printStackTrace(System.err);
				continue;
			}
		}
		result.put("0", URLs);
		result.put("1", pages);
		return result;
	}

	public static void main(String[] args) {

	}
}
