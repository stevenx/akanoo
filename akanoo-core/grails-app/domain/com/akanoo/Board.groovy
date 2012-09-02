package com.akanoo

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes="id")
class Board {

	long id
	
	String title
	
	long version
	Date dateCreated
	Date lastUpdated
	
	static hasMany = [
		notes: Note
	]
	
	static mapping = {
		notes cascade: 'all-delete-orphan'
	}
	
    static constraints = {
    }
}
