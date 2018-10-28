import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;


/**
 * Class for chat client.
 */
public class ChatClient {

    JTextArea incoming;
    JTextField outgoing;
    JTextArea userList;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    String username;
    String[] ps;

    /**
     * main Function.
     *
     * @param      args  The arguments
     */
    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.go();
    }


    /**
     * go function.
     */
    public void go() {

        JFrame frame = new JFrame("Client");
        JPanel mainPanel = new JPanel();

        JLabel l1, l2;
        l1 = new JLabel(Constants.MESSAGE_BOX);
        l2 = new JLabel(Constants.USER_LIST);


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

        JButton sendButton = new JButton(Constants.SEND);
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(qScroller);
        mainPanel.add(uScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);

        setUpNetworking();

        username = JOptionPane.showInputDialog(Constants.WELCOME_MESSAGE);

        frame.setTitle(username.toUpperCase());

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    /**
     * function for setting up Networking.
     */
    private void setUpNetworking() {

        try {
            sock = new Socket(Constants.HOST, Constants.PORT);

            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());

            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());

            System.out.println("Networking established");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Class for send button listener.
     * implementing the ActionListener
     */
    public class SendButtonListener implements ActionListener {


        /**
         * function for actionPerformed.
         *
         * @param      ev    is an Object of type ActionEvent.
         */
        public void actionPerformed(ActionEvent ev) {
            try {
                writer.println(username + " : " + outgoing.getText());
                writer.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }
    /**
     * Class for incoming reader.
     */
    public class IncomingReader implements Runnable {
        /**
         * function for run.
         */
        public void run() {
            String message;

            try {

                while ((message = reader.readLine()) != null) {
                    if (!messageOrList(message)) {
                        incoming.append(message + "\n");
                    } else {
                        userList.setText("");
                        for (int i = 1; i < ps.length; i++) {

                            userList.append(ps[i] + "\n");
                        }

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /**
         * function for messageOrList
         *
         * @param      ms    of type ms
         *
         * @return     true or false.
         */
        public boolean messageOrList(String ms) {
            ps = ms.split(Constants.REG_EX_ESC_PATTERN);
            return Constants.SHOW_MSG_VAL_CON_VAL.equals(ps[0]);
        }
    }



}
