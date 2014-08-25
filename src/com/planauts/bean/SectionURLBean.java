package com.planauts.bean;

public class SectionURLBean {
	public String name;
	public String url;
	
	public SectionURLBean(String n, String u){
		name = n;
		url = u;
	}
	
	public String toString(){
		return name + " " + url;
	}
}
