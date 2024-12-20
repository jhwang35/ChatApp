import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;



public class Client extends Thread{


	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){

		callback = call;
	}

	public void run() {

		// try to connect to server using socket
		try {
			socketClient= new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		// send one message for username

		//infinite loop that takes in messages coming from the server
		while(true) {

			try {
				Message message = (Message) in.readObject();
				callback.accept(message);
			}
			catch(Exception e) {}
		}

	}

	//sends messages that were taken in from the gui and sends them to the server
	public void send(Message data) {

		try {
			out.writeObject(data);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String read(Message message){
		return message.getMessageContent();
	}


}
