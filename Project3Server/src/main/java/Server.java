import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;


public class Server{

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	ArrayList<String> usernameArr = new ArrayList<>();;


	Server(Consumer<Serializable> call){

		callback = call;
		server = new TheServer();
		server.start();
	}


	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(5555)){
				System.out.println("Server is waiting for a client!");


				while(true) {


					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept(new Message("client has connected to server: ", true));
					clients.add(c);
					c.start();

					count++;


				}
			}//end of try
			catch(Exception e) {
				callback.accept(new Message("Server socket did not launch", true));
			}
		}//end of while
	}


	class ClientThread extends Thread{


		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		String username;
		Message data;

		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;


		}

		public String getUsername(){
			return username;
		}

		public void updateClients(Message message) {
			for(int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				try {
					t.out.writeObject(message);
				}
				catch(Exception e) {}
			}
		}



		public void run() {

			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch (Exception e) {
				System.out.println("Streams not open");
			}


			while (true) {
				System.out.println("stream open");
				try {
					// check if the name is already in the arraylist, if not add it
					data = (Message) in.readObject(); // read in, check for username
					System.out.println("name " + data.getSenderUsername());
						String username = data.getSenderUsername();
						System.out.println("username " + username);
						if (!usernameArr.contains(username)) {
							System.out.println("name not in array: " + username);
							usernameArr.add(username);
							out.writeObject(new Message("OK", false));
							callback.accept(new Message("NEW USER: " + username,true));
							updateClients(new Message("NEW USER " + data.getSenderUsername() + " has joined", true));
						} else if (usernameArr.contains(username) && data.type.equals(MessageType.LOGIN)){
							// send a no to tell server we cant let em in
							System.out.println("Already in system");
							out.writeObject(new Message("NO", false));
						}
						else{
							// send message
							callback.accept(new Message("client " + data.getSenderUsername() + " sent: " + data.getMessageContent(), true)); // Pass the entire Message object to the callback
							updateClients(new Message("client " + data.getSenderUsername() + " said: " + data.getMessageContent(), true));
						}
				}
					catch(Exception e){
						callback.accept(new Message("OOOOPPs...Something wrong with the socket from client '" + data.getSenderUsername() + "'....closing down!", true));
						updateClients(new Message("Client '" + data.getSenderUsername() + "' has left the server!", true));
						clients.remove(this);
						break;
					}
            }//end of run
		}
	}
	}//end of client thread






