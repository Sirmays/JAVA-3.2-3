import Server.ChatServer;
import Server.History;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class ServerApp {
    public static void main(String[] args) throws IOException {
       new ChatServer();
//        History history = new History();
//        System.out.println(Arrays.toString(history.loadHistory()));

    }




}
