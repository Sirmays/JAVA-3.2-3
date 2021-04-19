package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Optional;

public class ClientHandler {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private ChatServer chatServer;
    private String name;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new ChatServerException("Something went wrong during client establishing.", e);
        }

        new Thread(() -> {
            doAuthentication();
            listen();
        })
                .start();
    }

    public String getName() {
        return name;
    }

    private void listen() {
        receiveMessage();
    }

    private void doAuthentication() {

        sendMessage("Welcome! Please do authentication.");
        doTimer();
        while (true) {
            try {
                History history = new History();
                String message = in.readUTF();

                if (message.startsWith("-auth")) {
                    String[] credentialsStruct = message.split("\\s");
                    String login = credentialsStruct[1];
                    String password = credentialsStruct[2];

                    Optional<Entry> mayBeCredentials = Optional.ofNullable(chatServer.getAuthenticationService()
                            .findUser(login, password));

                    if (mayBeCredentials.isPresent()) {
                        Entry credentials = mayBeCredentials.get();
                        if (!chatServer.isLoggedIn(credentials.getName())) {
                            name = credentials.getName();
                            chatServer.broadcast(String.format("User[%s] entered the chat", name));
                            chatServer.subscribe(this);
                            sendMessage("You successfully authorized");

                            sendMessage(Arrays.toString(history.loadHistory()));

                            return;
                        } else {
                            sendMessage(String.format("User with name %s is already logged in", credentials.getName()));
                        }
                    } else {
                        sendMessage("Incorrect login or password.");
                    }
                } else {
                    sendMessage("Incorrect authentication message. " +
                            "Please use valid command: -auth your_login your_pass");
                }
            } catch (IOException e) {
                throw new ChatServerException("Something went wrong during client authentication.", e);
            }
        }
    }

    public void receiveMessage() {
        while (true) {
            try {
                String massage = in.readUTF();

                if (massage.startsWith("-quit")) {
                    chatServer.broadcast(String.format("User:%s go to home:)", name));
                    chatServer.unsubscribe(this);
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        throw new ChatServerException("Network error ", e);
                    }
                } else {
                    if (massage.startsWith("/w")) {
                        String[] strings = massage.split(" ");
                        String user = strings[1];
                        chatServer.directMessage(String.format("%s:%s", name, massage), user);
                    } else chatServer.broadcast(String.format("%s: %s", name, massage));
                }
            } catch (IOException e) {
                throw new ChatServerException("Something is wrong with during receive the massage", e);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new ChatServerException("Something went wrong during sending the message.", e);
        }
    }

    public void doTimer() {
        new Thread(() -> {
            try {
                Thread.sleep(12000000);
                if (name == null) {
                    out.writeUTF("The authorization time is over");
                    socket.close();
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        })
                .start();
    }
}