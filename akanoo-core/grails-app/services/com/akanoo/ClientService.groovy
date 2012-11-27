/* Copyright 2012 Fabian Gebert and Michael Gorski.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.akanoo

import groovyx.gpars.actor.Actors;

import org.atmosphere.cpr.Broadcaster
import org.atmosphere.cpr.BroadcasterFactory
import org.atmosphere.gwt.server.GwtAtmosphereResource;

import com.akanoo.UserServiceInterface.PermissionConstants


class ClientService {
	//do not use default transaction wrapper due to performance reasons
	static transactional = false

	def grailsApplication

	UserServiceInterface userService
	
	//maps connection ids to board ids
	Map<Integer, Long> connectionBoardMap = new LinkedHashMap<Integer, Long>()
	
	Map<Integer, Object> connectionUserMap = new LinkedHashMap<Integer, Object>()

	private void addPermission(Board board, String username, permission) {
		userService.addPermission board, username, permission
	}

	private void removePermission(Board board, String username, permission) {
		userService.deletePermission board, username, permission
	}

	def createcanvas(canvasInfo,cometResource) {
		assert userService.hasRole("ROLE_USER")

		log.info "creating new canvas $canvasInfo.title"

		def board = null
		Board.withNewTransaction {
			board = new Board(title:canvasInfo.title).save(failOnError: true)
			userService.addPermission board, userService.currentUserName, PermissionConstants.ADMINISTRATION
			userService.addPermission board, userService.currentUserName, PermissionConstants.READ
			userService.addPermission board, userService.currentUserName, PermissionConstants.WRITE
		}

		assert board.id

		sendBack cometResource.broadcaster, "createcanvas", { canvas ->
			canvas.id = board.id
			canvas.title = board.title
		}

		def userBroadcaster = BroadcasterFactory.default.lookup("user${userService.currentUser.id}",true);

		//re-initialize all user's clients since a new canvas was created
		sendInitializer userBroadcaster, userService.currentUser
	}

	def renamecanvas(canvasInfo,cometResource) {
		assert userService.hasRole("ROLE_USER")

		log.info "rename canvas ${canvasInfo.id} to ${canvasInfo.title}"

		def boardId = canvasInfo.id
		def readingUsers = null

		def board = null
		// rename canvas
		Board.withNewTransaction {
			board = Board.get boardId

			board.title = canvasInfo.title
			board.save(failOnError:true, flush:true)

			//fetch all collaborators
			readingUsers = userService.getUsers (board)
		}

		//notify client about the rename
		sendBack cometResource.broadcaster,"renamecanvas", { canvas ->
			canvas.id = board.id
			canvas.title = board.title
		}

		//SEND INDIVIDUAL MESSAGE BACK TO ALL USERS THAT WERE ABLE TO ACCESS THIS CANVAS
		//notify all collaborators
		if(readingUsers) {
			Board.withNewSession {
				def userIds = readingUsers*.id
				userIds.each { userId ->
					def userBroadcaster = BroadcasterFactory.default.lookup("user${userId}",true)
	
					//re-initialize all user's clients since a canvas was renamed
					sendInitializer userBroadcaster, userService.getUser(userId)
				}
			}
		}

	}

	def deletecanvas(deleteCanvasInfo, cometResource) {
		assert userService.hasPermission(Board.class.name, deleteCanvasInfo.id, PermissionConstants.ADMINISTRATION)

		def boardId = deleteCanvasInfo.id

		def readingUsers = null

		//DELETE BOARD
		Board.withNewTransaction {
			def board = Board.get boardId

			//fetch all collaborators
			readingUsers = userService.getUsers (board)

			board.delete(flush:true)

			// Delete the ACL information as well
			userService.deleteAcl board
		}

		//SEND INDIVIDUAL MESSAGE BACK TO ALL USERS THAT WERE ALBE TO ACCESS THIS CANVAS
		//notify all collaborators
		if(readingUsers) {
			Board.withNewTransaction {
				def userIds = readingUsers*.id
				userIds.each { userId ->
					def userBroadcaster = BroadcasterFactory.default.lookup("user${userId}",true)
	
					//re-initialize all user's clients since a canvas was renamed
					sendInitializer userBroadcaster, userService.getUser(userId)
				}
			}
		}

		//no particular reply
	}


	def initialize(accountInfo, cometResource) {
		assert userService.hasRole("ROLE_USER")
		
		def userBroadcaster = BroadcasterFactory.default.lookup("user${userService.currentUser.id}",true)

		//subscribe cometResource to user broadcaster
		userBroadcaster.addAtmosphereResource cometResource.atmosphereResource
		
		//register connection
		userRegistryActor.sendAndWait([
				action: 'register',
				user: userService.currentUser,
				cometResource: cometResource
			])
		
		//re-init client
		sendInitializer cometResource.broadcaster, userService.currentUser

		//send friends
		loadfriends(null, cometResource)
		
	}

	private sendInitializer(broadcaster, user) {
		def boards = loadBoardsOfUser(user)

		//send message back to clients
		sendBack broadcaster,"initialize",{ account ->
			def canvases = []

			boards.each { Board board ->
				def canvas = [:]
				canvas.id = board.id
				canvas.title = board.title
				canvases.add canvas
			}

			def userInfo = [:]

			userInfo.uid = user.username
			userInfo.email = user.email
			userInfo.name = user.realName
			userInfo.thumbUrl = user.thumbUrl

			account.currentUser = userInfo

			account.canvases = canvases
		}
	}

	private List<Board> loadBoardsOfUser(user) {
		userService.loadObjectsOfUser(user, Board.class.name)
	}

	def loadcanvas(canvasInfo, cometResource){
		//confirm user has read permission or is admin
		assert userService.hasPermission(Board.class.name, canvasInfo.id, PermissionConstants.READ) || userService.hasRole("ROLE_ADMIN")

		def boardId = canvasInfo.id

		log.info "loading canvas $boardId"

		def board = Board.get boardId

		assert board

		def canvasBroadcaster = BroadcasterFactory.default.lookup("canvas$boardId",true);

		//update connection
		updateConnectionBoard cometResource, boardId

		//send message back to clients
		sendBack cometResource.broadcaster,"loadcanvas", { canvas ->
			def notes = []

			board.notes.each { Note boardNote ->
				def note = [:]
				note.id = boardNote.id
				note.x = boardNote.x
				note.y = boardNote.y
				note.body = boardNote.body
				note.canvasId = board.id
				notes << note
			}

			canvas.notes = notes
			canvas.id = board.id
			canvas.title = board.title
		}
	}
	
	def createnote(newNoteInfo, cometResource) {
		//confirm user has write permission
		assert userService.hasPermission(Board.class.name, newNoteInfo.canvasId, PermissionConstants.WRITE)
		
		Board board = null
		Note note = null

		Board.withNewTransaction {
			board = Board.get newNoteInfo.canvasId
			note = new Note()
			note.body = newNoteInfo.body
			note.x = newNoteInfo.x
			note.y = newNoteInfo.y

			board.addToNotes note
			board.save(failOnError:true, flush:true)
		}

		def boardId = board.id

		//send message back to clients
		sendBack "canvas${boardId}","createnote",{ noteInfo ->
			noteInfo.id = note.id
			noteInfo.x = note.x
			noteInfo.y = note.y
			noteInfo.body = note.body
			noteInfo.canvasId = board.id
		}

		//let calling client directly edit this note
		sendBack cometResource.broadcaster, "focusnote", { noteInfo ->
			noteInfo.id = note.id
			noteInfo.canvasId = board.id
		}
	}

	def movenote(moveNoteInfo, cometResource) {
		assert userService.hasPermission(Board.class.name, moveNoteInfo.canvasId, PermissionConstants.WRITE)
		
		def boardId = null
		Note note = null

		Note.withNewTransaction {
			note = Note.get moveNoteInfo.id

			boardId = note.board.id

			assert boardId == moveNoteInfo.canvasId

			note.x = moveNoteInfo.x
			note.y = moveNoteInfo.y
			note.save(failOnError: true, flush: true)
		}

		//send message back to clients
		sendBack "canvas${boardId}","movenote",{ noteInfo ->
			noteInfo.id = note.id
			noteInfo.x = note.x
			noteInfo.y = note.y
		}
	}

	def updatenote(updateNoteInfo, cometResource) {
		assert userService.hasPermission(Board.class.name, updateNoteInfo.canvasId, PermissionConstants.WRITE)
		
		def boardId = null
		Note note = null

		Note.withNewTransaction {
			note = Note.get updateNoteInfo.id

			boardId = note.board.id

			assert boardId == updateNoteInfo.canvasId

			if(updateNoteInfo.body!=null)
				note.body = updateNoteInfo.body
			if(updateNoteInfo.backBody!=null)
				note.backBody = updateNoteInfo.backBody
			if(updateNoteInfo.url!=null)
				note.url = updateNoteInfo.url
			note.save(failOnError: true, flush: true)
		}

		//send message back to clients
		sendBack "canvas${boardId}","updatenote",{
			it.id = note.id
			it.body = note.body
		}
	}

	def removenote(removeNoteInfo, cometResource) {
		assert userService.hasPermission(Board.class.name, removeNoteInfo.canvasId, PermissionConstants.WRITE)

		def boardId = null

		Board.withNewTransaction {
			Note note = Note.get removeNoteInfo.id

			//required for mongodb support
			note.delete()
			
			boardId = note.board.id

			assert boardId == removeNoteInfo.canvasId

			
			def board = Board.get boardId
			board.removeFromNotes note
			board.save(flush:true)
		}

		//send message back to clients
		sendBack "canvas${boardId}","removenote",{
			it.id = removeNoteInfo.id
		}
	}

	def loadshares(canvasInfo, cometResource){
		assert userService.hasPermission(Board.class.name, canvasInfo.id, PermissionConstants.READ)

		def boardId = canvasInfo.id

		def board = Board.get boardId

		assert board

		log.info "loading shares of canvas $boardId"

		//get reading users
		def readingUsers = userService.getReadingUsers(board)

		log.info "reading users: $readingUsers"

		sendCollaborators(cometResource.broadcaster, boardId, readingUsers)
	}

	private sendCollaborators(broadcaster, boardId, users) {
		//send a direct reply!
		sendBack broadcaster,"loadshares",{ canvas ->
			canvas.id = boardId

			def collaborators = []
			users.each { user ->
				def collaborator = [:]

				collaborator.name = user.realName ?: user.email
				collaborator.uid = user.username
				collaborator.email = user.email
				collaborator.thumbUrl = user.thumbUrl
				collaborator.enabled = user.enabled

				collaborators << collaborator
			}

			canvas.collaborators = collaborators
		}
	}

	def loadfriends(accountInfo, cometResource) {
		//load all boards this user can access
		def boards = loadBoardsOfUser userService.currentUser

		//get all users with access to these boards
		def friends = boards.collect { userService.getReadingUsers it }.flatten().unique()

		sendBack cometResource.broadcaster, "loadfriends", { account ->
			def friendInfos = []

			friends.each { friend ->
				def friendInfo = [:]

				friendInfo.name = friend.realName ?: friend.email
				friendInfo.uid = friend.username
				friendInfo.email = friend.email
				friendInfo.thumbUrl = friend.thumbUrl

				friendInfos << friendInfo
			}

			account.friends = friendInfos
		}
	}

	def sharecanvas(shareCanvasInfo, cometResource) {
		assert userService.hasPermission(Board.class.name, shareCanvasInfo.canvasId, PermissionConstants.ADMINISTRATION)
		
		
		def boardId = shareCanvasInfo.canvasId

		log.info "adjusting shares of canvas $boardId with $shareCanvasInfo.username"

		assert shareCanvasInfo.username || shareCanvasInfo.email

		def board = Board.get boardId

		def user = null

		if(shareCanvasInfo.username) {
			user = userService.findUserByUsername shareCanvasInfo.username
			if(!user) {
				throw new IllegalArgumentException("username not found")
			}
		}
		else if(shareCanvasInfo.email) {
			user = userService.findUserByEmail shareCanvasInfo.email
			if(!user) {
				user = userService.createAndAskToConfirm shareCanvasInfo.email, board.title
			}
		}

		//fetch all collaborators
		def boardUsers = userService.getUsers (board) as Set

		//send email notification if user is enabled and hasn't got access already
		if(user.enabled && !boardUsers.contains(user)) {
			//send email share notification
			userService.sendShareNotification user.email, board.title, user.realName ?: user.email
		}

		// also notify affected user (might already be in the list)
		boardUsers << user

		Board.withNewTransaction {
			userService."${shareCanvasInfo.read?'add':'delete'}Permission" board, user.username, PermissionConstants.READ
			userService."${shareCanvasInfo.write?'add':'delete'}Permission" board, user.username, PermissionConstants.WRITE
			userService."${shareCanvasInfo.admin?'add':'delete'}Permission" board, user.username, PermissionConstants.ADMINISTRATION
			log.info "Updated sharing of board #$board.id wrt user ${user.username}"
		}
		
		def userIds = boardUsers*.id
		userIds.each { userId ->
			def userBroadcaster = BroadcasterFactory.default.lookup("user${userId}",true);

			//re-initialize all user's clients since a new canvas was created
			sendInitializer userBroadcaster, userService.getUser(userId)
		}

		//no particular reply!
	}

	void sendBack (broadcaster, code, Closure objectInitializer) {
		if(!(broadcaster instanceof Broadcaster))
			broadcaster = BroadcasterFactory.default.lookup(broadcaster,true);

		def object = [:]

		objectInitializer object

		def message = [
					code: code,
					body: object
				]

		def messageString = message.encodeAsJSON()

		broadcaster.broadcast messageString
	}
	
	void onConnecting(cometResource) {
		log.info "connection opening: $cometResource.connectionID"
	}
	
	void onClose(GwtAtmosphereResource cometResource, boolean serverInitiated) {
		log.info "connection closed: $cometResource.connectionID"
		
		userRegistryActor.send([
				action: 'close',
				cometResource: cometResource
			])
	
	}
	
	def userRegistryActor = Actors.actor {
		loop {
			react { args ->
				GwtAtmosphereResource cometResource = args.cometResource
				
				if(args.action == "register") {
					connectionUserMap.put cometResource.connectionID, args.user
				}
				else if(args.action == "close") {
					
					def connectionUser = connectionUserMap[cometResource.connectionID]
					
					if(connectionUser) {
						try {
						def userBroadcaster = BroadcasterFactory.default.lookup("user${connectionUser.id}",true)
						
						//unsubscribe cometResource from user broadcaster
						userBroadcaster.removeAtmosphereResource cometResource.atmosphereResource
						
						updateConnectionBoard cometResource, null
						}
						catch (Exception ex){
							log.error "Error handling connection close async", ex
						}  
						
						connectionUserMap.remove cometResource.connectionID
					}					
				}
				
				replyIfExists null
			}
		}
	}

	/**
	 * Update the registry info on what board the given connection/tab is.
	 * Unsubscribe connection/tab from last canvas. Subscribe connection/tab to current canvas (boardId). 
	 * @param cometResource Connection resource (identifies a tab)
	 * @param boardId Current canvas, or null if connection closing
	 */
	private void updateConnectionBoard(GwtAtmosphereResource cometResource, boardId) {
		int connectionId = cometResource.connectionID
		//ignore if no change was made
		if(connectionBoardMap[connectionId] == boardId) {
			log.info "canvas of connection $connectionId was updated though canvas id $boardId didn't change"
			return
		}
		
		//save previous board
		def previousBoardId = connectionBoardMap[connectionId]
		
		if(previousBoardId) {
			//reset current board id
			connectionBoardMap.remove connectionId
			
			def previousCanvasBroadcaster = BroadcasterFactory.default.lookup("canvas$previousBoardId",true);
			
			//unsubscribe cometResource from previous canvas broadcaster
			previousCanvasBroadcaster.removeAtmosphereResource cometResource.atmosphereResource
	
			// update active users on subscribed connections
			sendBoardUsers previousBoardId
		}
		
		if(boardId) {
			//set current board id
			connectionBoardMap[connectionId] = boardId
			
			def newCanvasBroadcaster = BroadcasterFactory.default.lookup("canvas$boardId",true);
			//subscribe cometResource to new canvas broadcaster
			newCanvasBroadcaster.addAtmosphereResource cometResource.atmosphereResource
	
			// update active users on subscribed connections
			sendBoardUsers boardId
		}
	}
	
	private void sendBoardUsers(boardId) {
		def connectionIds = connectionBoardMap.findAll { it.value == boardId }.collect { it.key }
		def users = connectionIds.collect { connectionUserMap[it] }.findAll { it != null }
		
		def canvasBroadcaster = BroadcasterFactory.default.lookup("canvas$boardId",true);
		
		sendBack canvasBroadcaster, "loadactiveusers", { canvas ->
			canvas.id = boardId

			def collaborators = []
			users.each { user ->
				def collaborator = [:]

				collaborator.name = user.realName ?: user.email
				collaborator.uid = user.username
				collaborator.email = user.email
				collaborator.thumbUrl = user.thumbUrl
				collaborator.enabled = user.enabled

				collaborators << collaborator
			}

			canvas.collaborators = collaborators
		}
	}


}
