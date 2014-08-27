package com.planauts.bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class SectionURLBean {
	public LinkedHashMap<String, String> vod;
	public LinkedHashMap<String, String> program;
	public LinkedHashMap<String, String> special;
	
	public SectionURLBean(LinkedHashMap<String, String> v, LinkedHashMap<String, String> p, LinkedHashMap<String, String> s){
		vod = v;
		program = p;
		special = s;
	}
	
	public String toString(){
		StringBuilder a = new StringBuilder();

		a.append("VOD BEGIN\n");
		for (Map.Entry<String,String> key : vod.entrySet()) {
		    a.append(key + " - " + vod.get(key) + "\n");
		}
		a.append("VOD END\n");
		a.append("PRORGRAM BEGIN\n");
		for (Map.Entry<String,String> key : program.entrySet()) {
		    a.append(key + " - " + program.get(key) + "\n");
		}
		a.append("PRORGRAM END\n");
		a.append("SPECIAL BEGIN\n");
		for (Map.Entry<String,String> key : special.entrySet()) {
		    a.append(key + " - " + special.get(key) + "\n");
		}
		a.append("SPECIAL END\n");
		
		return a.toString();
	}
}