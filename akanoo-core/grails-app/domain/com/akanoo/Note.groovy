package com.akanoo

import java.util.Date;

class Note {
	long id
	
	int x
	int y
	String body
	
	Long version
	Date dateCreated
	Date lastUpdated
	
	static belongsTo = [
		board: Board
	]
	
    static constraints = {
    }
    
	static mapping = {
		body type: 'text'
	}
}
