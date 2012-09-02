package com.akanoo

import java.util.List

import javax.servlet.http.HttpSession;



interface UserServiceInterface {
	enum PermissionConstants {
		ADMINISTRATION, WRITE, READ
	}

	String getCurrentUserName()

	def getCurrentUser()
	def getUser(String id)

	def findUserByUsername(String username)
	def findUserByEmail(String email)

	void addPermission(object, String username, PermissionConstants permission)

	void deletePermission(object, String username, PermissionConstants permission)

	/**
	 * Confirm the current user has the given role
	 * @param roleName
	 * @return
	 */
	boolean hasRole(String roleName)

	/**
	 * Confirm the current user has the given permission to the given object
	 * @param className
	 * @param id
	 * @param permission
	 * @return
	 */
	boolean hasPermission(String className, Long id, PermissionConstants permission)

	/**
	 * Delete ACL information of the given object
	 * @param object
	 */
	void deleteAcl(object)

	/**
	 * Retrieve all objects of the given domain class the given user can read
	 * @param user
	 * @param domainClassName
	 * @return
	 */
	List<Object> loadObjectsOfUser(user, String domainClassName)

	/**
	 * Get users with read access to the given domain object
	 * @param object
	 * @return
	 */
	List<?> getReadingUsers(object)

	/**
	 * Get users with any access to the given domain object
	 * @param object
	 * @return
	 */
	List<?> getUsers(object)

	/**
	 * Invite user by email to object with given title and ask to confirm email address
	 * @param email
	 * @param objectTitle
	 * @return
	 */
	def createAndAskToConfirm(String email, String objectTitle)

	/**
	 * Send share notification to given user for object with given title
	 * @param email
	 * @param objectTitle
	 * @param recipientName
	 */
	void sendShareNotification(String email, String objectTitle, String recipientName)

	/**
	 * Restore auth context from given session
	 */
	void restoreContextFromSession(HttpSession session)

	/**
	 * Reset auth context
	 */
	void resetContext()
}