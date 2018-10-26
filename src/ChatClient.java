import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



// CLASS: ChatClient
public class ChatClient
{

    JTextArea incoming;
    JTextField outgoing;
    JTextArea userList;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String username;
    String[] ps;

	// FUNCTION: main
    public static void main(String[] args)
    {
        ChatClient client = new ChatClient();
        client.go();
    }

    // FUNCTION: go
    public void go()
    {

        JFrame frame = new JFrame("Client");
        JPanel mainPanel = new JPanel();

        JLabel l1, l2;
        l1 = new JLabel("Message Box");
        l2 = new JLabel("User List");

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        userList = new JTextArea(15, 10);
        userList.setLineWrap(true);
        userList.setWrapStyleWord(true);
        userList.setEditable(false);

        
        JScrollPane uScroller = new JScrollPane(userList);
        uScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        //mainPanel.add(l1);
        //mainPanel.add(l2);
        mainPanel.add(qScroller);
        mainPanel.add(uScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        setUpNetworking();

        username = JOptionPane.showInputDialog("Welcome ! Kindly say who you are ?");

        frame.setTitle(username.toUpperCase());

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        //frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    // FUNCTION: setUpNetworking
    private void setUpNetworking()
    {

        try
        {
            sock = new Socket("127.0.0.1", 6666);

            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());

            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());

            System.out.println("Networking established");

        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    // CLASS: SendButtonListener
    public class SendButtonListener implements ActionListener
    {


    	// FUNCTION: actionPerformed
        public void actionPerformed(ActionEvent ev)
        {
            try
            {
                writer.println(username + " : " + outgoing.getText());
                writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            outgoing.setText("");
            outgoing.requestFocus();
        }
    }


    // CLASS: IncomingReader
    public class IncomingReader implements Runnable
    {

    	// FUNCTION: run
        public void run()
        {
            String message;

            try
            {
                while((message = reader.readLine()) != null)
                {
                    if(!messageOrList(message))
                    {
                        //System.out.println("read" + message);
                        incoming.append( message + "\n");
                    }
                    else
                    {
                        userList.setText("");
                        for(int i = 1; i < ps.length; i++)
                        {
                            //System.out.println(ps[i]);
                            userList.append(ps[i] + "\n");
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        // FUNCTION: messageOrList
        public boolean messageOrList(String ms)
        {
            ps = ms.split("\\,");

            if(ps[0].equals("000.."))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
    }

}
