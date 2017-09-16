import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;



import java.awt.*;


public class ChatServer {

	private static JTextArea clientsMessage;
	ArrayList clientOutputStreams;
	
	public class ClientHandler implements Runnable{
		
		BufferedReader reader;
		Socket sock;
		//JTextArea clientsMessage;
		
		public ClientHandler(Socket clientSocket) {
			
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
			    reader = new BufferedReader(isReader);
			    
			    
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			String message;
			try {
					while((message = reader.readLine()) != null) {
						System.out.println("read" + message);
						clientsMessage.append(message+"\n");
						tellEveryone(message);
					}
				}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}		
	public void showingClients()
	{
		for(int i = 0; i < clientOutputStreams.size() ; i++) {
			System.out.println(clientOutputStreams.get(i));
		}
			
	}
	
		public void go() {
			clientOutputStreams = new ArrayList();
			
			try {
				ServerSocket serverSock = new ServerSocket(6666);
				
				while(true) {
					Socket clientSocket = serverSock.accept();
					//System.out.println(clientSocket.getPort());
					PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
					clientOutputStreams.add(writer);
					//showingClients();
					
					Thread t = new Thread(new ClientHandler(clientSocket));
					t.start();
					System.out.println("got a connection");
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		
		
		public void tellEveryone(String message) {
			Iterator it = clientOutputStreams.iterator();
			while(it.hasNext()) {
				try {
					PrintWriter writer = (PrintWriter) it.next();
					writer.println(message);
					writer.flush();
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		
		public static void main(String[] args) {
			
			  JFrame window = new JFrame("SERVER");
		      JPanel content = new JPanel();
		      clientsMessage = new JTextArea(15,50);
		      clientsMessage.setLineWrap(true);
		      clientsMessage.setWrapStyleWord(true);
		      clientsMessage.setEditable(false);
		      JScrollPane qScroller = new JScrollPane(clientsMessage);
			  qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			  qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			  JTextField smsToAll = new JTextField(20);
			  JButton sendButton = new JButton("Send To All");
			  content.add(qScroller);
			  content.add(smsToAll);
			  content.add(sendButton);
			  window.getContentPane().add(BorderLayout.CENTER, content);
			  window.setSize(800, 400);
			  window.setVisible(true);
			  clientsMessage.setText("");

			  new ChatServer().go();
			  
			  
		}
	
	
	
}
