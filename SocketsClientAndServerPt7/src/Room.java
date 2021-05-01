import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room implements AutoCloseable {
	private static SocketServer server;// used to refer to accessible server functions
	private String name;
	private final static Logger log = Logger.getLogger(Room.class.getName());

	// Commands
	private final static String COMMAND_TRIGGER = "/";
	private final static String CREATE_ROOM = "createroom";
	private final static String JOIN_ROOM = "joinroom";
	private final static String FLIP = "flip";
	private final static String ROLL = "roll";
	// private final static String MUTE = "mute";
	// private final static String UNMUTE = "unmute";

	public Room(String name) {
		this.name = name;
	}

	public static void setServer(SocketServer server) {
		Room.server = server;
	}

	public String getName() {
		return name;
	}

	private List<ServerThread> clients = new ArrayList<ServerThread>();

	protected synchronized void addClient(ServerThread client) {
		client.setCurrentRoom(this);
		if (clients.indexOf(client) > -1) {
			log.log(Level.INFO, "Attempting to add a client that already exists");
		} else {
			clients.add(client);
			if (client.getClientName() != null) {
				client.sendClearList();
				sendConnectionStatus(client, true, "joined the room " + getName());
				updateClientList(client);
			}
		}
	}

	private void updateClientList(ServerThread client) {
		Iterator<ServerThread> iter = clients.iterator();
		while (iter.hasNext()) {
			ServerThread c = iter.next();
			if (c != client) {
				boolean messageSent = client.sendConnectionStatus(c.getClientName(), true, null);
			}
		}
	}

	protected synchronized void removeClient(ServerThread client) {
		clients.remove(client);
		if (clients.size() > 0) {
			// sendMessage(client, "left the room");
			sendConnectionStatus(client, false, "left the room " + getName());
		} else {
			cleanupEmptyRoom();
		}
	}

	private void cleanupEmptyRoom() {
		// If name is null it's already been closed. And don't close the Lobby
		if (name == null || name.equalsIgnoreCase(SocketServer.LOBBY)) {
			return;
		}
		try {
			log.log(Level.INFO, "Closing empty room: " + name);
			close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void joinRoom(String room, ServerThread client) {
		server.joinRoom(room, client);
	}

	protected void joinLobby(ServerThread client) {
		server.joinLobby(client);
	}

	/***
	 * Helper function to process messages to trigger different functionality.
	 * 
	 * @param message The original message being sent
	 * @param client  The sender of the message (since they'll be the ones
	 *                triggering the actions)
	 */
	private String processCommands(String message, ServerThread client) {
		String response = null;
		try {
			if (message.indexOf(COMMAND_TRIGGER) > -1) {
				String[] comm = message.split(COMMAND_TRIGGER);
				log.log(Level.INFO, message);
				String part1 = comm[1];
				String[] comm2 = part1.split(" ");
				String command = comm2[0];
				if (command != null) {
					command = command.toLowerCase();
				}
				String roomName;
				switch (command) {
				case CREATE_ROOM:
					roomName = comm2[1];
					joinRoom(roomName, client);
					break;
				case JOIN_ROOM:
					roomName = comm2[1];
					joinRoom(roomName, client);
					break;
				case FLIP:
					Random rand = new Random();
					int value = rand.nextInt(2);
					if (value == 0) {
						response = "<font face=\"Papyrus,\" size=\"15px\" color=\"#13489E\">Flipping... " + "Tails";
					} else {
						response = "<font face=\"Papyrus,\" size=\"15px\" color=\"#13489E\">Flipping... " + "Heads";
					}
					break;
				case ROLL:
					Random rand2 = new Random();
					response = "<font face=\"Brush Script MT\" size=\"25px\" color=\"#862916\">Rolling... "
							+ Integer.toString(rand2.nextInt(Integer.parseInt(comm2[1]) + 1)) + "</font>";
					break;
				// case MUTE:

				// break;
				// case UNMUTE:

				// break;
				default:
					response = message;
					break;
				}
			} else {
				String alteredMessage = message;

				// adds a user to a list where messages would be sent privately
				if (alteredMessage.indexOf("@") > -1) {
					String[] ats = alteredMessage.split("@");
					List<String> usersPrivateMessage = new ArrayList<String>();
					for (int i = 0; i < ats.length; i++) {
						if (i % 2 != 0) {
							String[] data = ats[i].split(" ");
							String user = data[0];
							usersPrivateMessage.add(user);
						}
					}
				}

				if (alteredMessage.indexOf("*") > -1) {
					String[] s1 = alteredMessage.split("\\*");
					String m = "";
					for (int i = 0; i < s1.length; i++) {
						if (i % 2 == 0) {
							m += s1[i];
						} else {
							m += "<b>" + s1[i] + "</b>";
						}
						System.out.println(s1[i]);
					}
					alteredMessage = m;
				}
				if (alteredMessage.indexOf("[b") > -1) {
					String[] s1 = alteredMessage.split("\\[b\\]");
					String m = "";
					for (int i = 0; i < s1.length; i++) {
						if (i % 2 == 0) {
							m += s1[i];
						} else {
							m += "<font color = 'blue'>" + s1[i] + "</font>";
						}
						System.out.println(s1[i]);
					}
					alteredMessage = m;
				}
				if (alteredMessage.indexOf("[i") > -1) {
					String[] s1 = alteredMessage.split("\\[i\\]");
					String m = "";
					for (int i = 0; i < s1.length; i++) {
						if (i % 2 == 0) {
							m += s1[i];
						} else {
							m += "<i>" + s1[i] + "</i>";
						}
						System.out.println(s1[i]);
					}
					alteredMessage = m;
				}
				if (alteredMessage.indexOf("[u") > -1) {
					String[] s1 = alteredMessage.split("\\[u\\]");
					String m = "";
					for (int i = 0; i < s1.length; i++) {
						if (i % 2 == 0) {
							m += s1[i];
						} else {
							m += "<u>" + s1[i] + "</u>";
						}
						System.out.println(s1[i]);
					}
					alteredMessage = m;
				}
				if (alteredMessage.indexOf("@") > -1) {
					String[] s1 = alteredMessage.split("\\@");
					String[] s2 = s1[1].split(" ", 2);
					sendSystemMessage(s2[1], s2[0]);
					alteredMessage = null;
				}
				response = alteredMessage;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	// TODO changed from string to ServerThread
	protected void sendConnectionStatus(ServerThread client, boolean isConnect, String message) {
		Iterator<ServerThread> iter = clients.iterator();
		while (iter.hasNext()) {
			ServerThread c = iter.next();
			boolean messageSent = c.sendConnectionStatus(client.getClientName(), isConnect, message);
			if (!messageSent) {
				iter.remove();
				log.log(Level.INFO, "Removed client " + c.getId());
			}
		}
	}

	/***
	 * Takes a sender and a message and broadcasts the message to all clients in
	 * this room. Client is mostly passed for command purposes but we can also use
	 * it to extract other client info.
	 * 
	 * @param sender  The client sending the message
	 * @param message The message to broadcast inside the room
	 */
	protected void sendMessage(ServerThread sender, String message) {
		log.log(Level.INFO, getName() + ": Sending message to " + clients.size() + " clients");
		String resp = processCommands(message, sender);
		if (resp == null) {
			// it was a command, don't broadcast
			return;
		}
		message = resp;
		Iterator<ServerThread> iter = clients.iterator();
		while (iter.hasNext()) {
			ServerThread client = iter.next();
			boolean messageSent = client.send(sender.getClientName(), message);
			if (!messageSent) {
				iter.remove();
				log.log(Level.INFO, "Removed client " + client.getId());
			}
		}
	}

	protected void sendSystemMessage(String message, String target) {
		Iterator<ServerThread> iter = clients.iterator();
		while (iter.hasNext()) {
			ServerThread client = iter.next();
			if (target == null || client.getClientName().equals(target)) {
				boolean messageSent = client.send("Pssst Whisper Recieved", message);
				if (!messageSent) {
					iter.remove();
					log.log(Level.INFO, "Removed client " + client.getId());
				}
				if (target != null) {
					break;
				}
			}
		}
	}

	protected void sendSystemMessage(String message) {
		sendSystemMessage(message, null);
	}

	/***
	 * Will attempt to migrate any remaining clients to the Lobby room. Will then
	 * set references to null and should be eligible for garbage collection
	 */
	@Override
	public void close() throws Exception {
		int clientCount = clients.size();
		if (clientCount > 0) {
			log.log(Level.INFO, "Migrating " + clients.size() + " to Lobby");
			Iterator<ServerThread> iter = clients.iterator();
			Room lobby = server.getLobby();
			while (iter.hasNext()) {
				ServerThread client = iter.next();
				lobby.addClient(client);
				iter.remove();
			}
			log.log(Level.INFO, "Done Migrating " + clients.size() + " to Lobby");
		}
		server.cleanupRoom(this);
		name = null;
		// should be eligible for garbage collection now
	}

}