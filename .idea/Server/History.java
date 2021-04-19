package Server;

import java.io.*;


public class History {

    static void writeToHistory(String message) {
        File file = new File("src/main/java/Server/History.txt");
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.append(message);
            fw.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] loadHistory() throws IOException {
        int size = getLinesLength();
        String[] data = new String[size];
        BufferedReader bufferReader = new BufferedReader(new FileReader("src/main/java/Server/History.txt"));
        String line = bufferReader.readLine();

        if (size >= 100) {

            int i = size - 100;
            while (line != null) {
                data[i] = (line + "\n");
                line = bufferReader.readLine();
                i++;

            }
        } else {

            int i = 0;
            while (line != null) {
                data[i] = (line + "\n");
                line = bufferReader.readLine();
                i++;
            }
        }
        bufferReader.close();
        return data;
    }


    public static int getLinesLength() throws IOException {
        BufferedReader bufferReader = new BufferedReader(new FileReader("src/main/java/Server/History.txt"));
        String line = bufferReader.readLine();
        int size = 0;
        while (line != null) {
            size += 1;
            line = bufferReader.readLine();
        }
        bufferReader.close();
        return size;
    }
}
