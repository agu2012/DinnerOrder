package com.bocs.dodemo.entity;

public class Employee {

	private String _id;
	private String employeeName;
	
	public Employee(){
		
	}
	
	public Employee(String _employeeName){
		employeeName = _employeeName;
	}
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	
	
	
}
