package com.avijit.server;

import com.avijit.commons.Constants;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


public class ChatServer {

    private JTextArea clientsMessage;
    private JTextArea userList;
    private ArrayList<PrintWriter> clientOutputStreams;
    private JButton sendButton;
    private JTextField smsToAll;
    private List<String> userName = new ArrayList<>();
    private List<Socket> port = new ArrayList<>();

    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket) {

            try {
                socket = clientSocket;
                InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void run() {

            String message;
            String[] parts = null;
            boolean once = false;

            try
            {
                while((message = reader.readLine()) != null)
                {
                    if(!once)
                    {
                        //Remove commented out code if not used.
                        //System.out.println(socket.getPort());
                        port.add(socket);
                        parts = message.split(":");
                        //System.out.println(parts[0]);
                        userName.add(parts[0]);
                        once = true;
                    }
                    //System.out.println("read" + message);
                    clientsMessage.append(message + "\n");
                    tellEveryone(message);
                    showingClients();
                }

                assert parts != null;
                userName.remove(parts[0]);
                port.remove(socket);
                showingClients();
                tellEveryone(parts[0].toUpperCase() + "(" + socket + ")" + "has left the conversation\n");
                clientsMessage.append(parts[0].toUpperCase() + "(" + socket + ")" + "has left the conversation\n");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    public class SendButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            clientsMessage.append("SERVER : " + smsToAll.getText() + "\n");
            tellEveryone("SERVER : " + smsToAll.getText());

            smsToAll.setText("");
            smsToAll.requestFocus();
        }

    }

    public void showingClients() {
        StringBuilder st = new StringBuilder(Constants.SHOW_MSG_VAL_CON_VAL);
        StringBuilder t = new StringBuilder(Constants.EMPTY);
        for (String s : userName) {
            st.append(s).append(",");
            t.append(s).append("\n");
        }

        System.out.println(st);

        Iterator<PrintWriter> it = clientOutputStreams.iterator();
        userList.setText(t.toString());


        //TODO extract duplicated code to a function.
        while(it.hasNext())
        {
            try
            {
                PrintWriter writer = it.next();
                writer.println(st);
                writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


    private void go()
    {
        clientOutputStreams = new ArrayList<>();

        try
        {
            ServerSocket serverSock = new ServerSocket(6666);

            //Infinite loop????
            while(true)
            {
                Socket clientSocket = serverSock.accept();
                //System.out.println(clientSocket.getPort());
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection from " + clientSocket);
                //showingClients();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }



    private void tellEveryone(String message)
    {

        Iterator<PrintWriter> it = clientOutputStreams.iterator();

        //Duplicated code
        while(it.hasNext())
        {
            try
            {
                PrintWriter writer = it.next();
                writer.println(message);
                writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void addAction()
    {
        JFrame window = new JFrame("SERVER");
        JPanel content = new JPanel();

        clientsMessage = new JTextArea(15, 50);
        clientsMessage.setLineWrap(true);
        clientsMessage.setWrapStyleWord(true);
        clientsMessage.setEditable(false);
      
        JScrollPane qScroller = new JScrollPane(clientsMessage);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        userList = new JTextArea(15, 10);
        userList.setLineWrap(true);
        userList.setWrapStyleWord(true);
        userList.setEditable(false);

        JScrollPane uScroller = new JScrollPane(userList);
        uScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        smsToAll = new JTextField(20);
      
        sendButton = new JButton("Send To All");
        sendButton.addActionListener(new SendButtonListener());

        content.add(qScroller);
        content.add(uScroller);
        content.add(smsToAll);
        content.add(sendButton);

        window.getContentPane().add(BorderLayout.CENTER, content);
        window.setSize(800, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        clientsMessage.setText(Constants.EMPTY);

    }

    public static void main(String[] args) {
        ChatServer cs = new ChatServer();
        cs.addAction();
        cs.go();
    }


}
