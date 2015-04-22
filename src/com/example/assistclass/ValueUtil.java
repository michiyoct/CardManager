package com.example.assistclass;

import java.util.ArrayList;
import java.util.List;

public class ValueUtil {
	public static String STRING_SEPERATOR = ";;";

	public static String list2string(List<String> categorys) {
		if (categorys == null)
			return null;
		int length = categorys.size();
		if (length == 0)
			return null;
		String result = "";
		for (int i = 0; i < length; i++) {
			if (i == 0)
				result += categorys.get(i);
			else
				result += STRING_SEPERATOR + categorys.get(i);
		}
		return result;
	}

	public static List<String> string2list(String original) {
		if (original == null)
			return new ArrayList<String>();
		String array[] = original.split(STRING_SEPERATOR);
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < array.length; i++)
			result.add(array[i]);
		return result;
	}

	public static int boolean2int(boolean original){
		if(original)
			return 1;
		else
			return 0;
	}
	
	public static boolean int2boolean(int original){
		return original == 1;
	}
}
