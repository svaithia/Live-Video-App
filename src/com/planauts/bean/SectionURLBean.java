package com.planauts.bean;

import java.util.HashMap;

public class SectionURLBean {
	public HashMap<String, String> vod;
	public HashMap<String, String> program;
	public HashMap<String, String> special;
	
	public SectionURLBean(HashMap<String, String> v, HashMap<String, String> p, HashMap<String, String> s){
		vod = v;
		program = p;
		special = s;
	}
	
	public String toString(){
		StringBuilder a = new StringBuilder();

		a.append("VOD BEGIN\n");
		for (String key : vod.keySet()) {
		    a.append(key + " - " + vod.get(key) + "\n");
		}
		a.append("VOD END\n");
		a.append("PRORGRAM BEGIN\n");
		for (String key : program.keySet()) {
		    a.append(key + " - " + program.get(key) + "\n");
		}
		a.append("PRORGRAM END\n");
		a.append("SPECIAL BEGIN\n");
		for (String key : special.keySet()) {
		    a.append(key + " - " + special.get(key) + "\n");
		}
		a.append("SPECIAL END\n");
		
		return a.toString();
	}
}
