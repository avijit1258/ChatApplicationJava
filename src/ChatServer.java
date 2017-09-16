import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

import javax.swing.*;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChatServer {

	JTextArea clientsMessage;
	ArrayList clientOutputStreams;
	JButton sendButton;
	JTextField smsToAll;
	ArrayList<String> uname = new ArrayList<String>();
	ArrayList<Integer> port = new ArrayList<Integer>();
	
	public class ClientHandler implements Runnable{
		
		BufferedReader reader;
		Socket sock;
		//JTextArea clientsMessage; get nullPointerException for this line wasted one day
		
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
			boolean once = false;
			
			
			try {
					while((message = reader.readLine()) != null) {
						if(!once)
						{
							System.out.println(sock.getPort());
							port.add(sock.getPort());
							String[] parts = message.split("\\:");
							System.out.println(parts[0]);
							uname.add(parts[0]);
							once = true;
						}
						System.out.println("read" + message);
						clientsMessage.append(message+"\n");
						tellEveryone(message);
						showingClients();
					}
				}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}	
	
	public  class SendButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			clientsMessage.append("SERVER : " + smsToAll.getText()+"\n");
			tellEveryone("SERVER : " + smsToAll.getText());
			
			smsToAll.setText("");
			smsToAll.requestFocus();
			
		}
		
	}
	
	
	public void showingClients()
	{
//		for(int i = 0; i < uname.size() ; i++) {
//			System.out.println(uname.get(i));
//			System.out.println(port.get(i)+ "\n");
//			
//		}
		
		for(String s : uname)
		{
			System.out.println(s);
		}
		for(Integer i: port)
		{
			System.out.println(i);
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
					
					
					Thread t = new Thread(new ClientHandler(clientSocket));
					t.start();
					System.out.println("got a connection");
					//showingClients();
				}
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		
		
		public  void tellEveryone(String message) {
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
		public  void addAction()
		{
			JFrame window = new JFrame("SERVER");
		      JPanel content = new JPanel();
		      clientsMessage = new JTextArea(15,50);
		      clientsMessage.setLineWrap(true);
		      clientsMessage.setWrapStyleWord(true);
		      clientsMessage.setEditable(false);
		      JScrollPane qScroller = new JScrollPane(clientsMessage);
			  qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			  qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			  smsToAll = new JTextField(20);
			  sendButton = new JButton("Send To All");
			  sendButton.addActionListener(new SendButtonListener());
			  content.add(qScroller);
			  content.add(smsToAll);
			  content.add(sendButton);
			  window.getContentPane().add(BorderLayout.CENTER, content);
			  window.setSize(800, 400);
			  window.setVisible(true);
			  clientsMessage.setText("");
			  
		}
		
		public static void main(String[] args) {
			
			  ChatServer cs = new ChatServer();
			  cs.addAction();
			  cs.go();
			  
			  
			  
		}
	
	
	
}
