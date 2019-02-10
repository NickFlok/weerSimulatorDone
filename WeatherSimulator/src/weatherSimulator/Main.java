package weatherSimulator;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import weatherSimulator.ErrorCorrection;

public class Main {
	
	// Queue waar de laatste 30 goede waardes in worden opgeslagen
	static Queue<String> windQueue = new LinkedList<String>();
	static Queue<String> cloudQueue = new LinkedList<String>();
    public static void main(String[] args) {

    	System.out.println("Running");
    	ServerSocket server = null;
        try {
            server = new ServerSocket(9999);
            server.setReuseAddress(true);
            // Accepteert nieuwe connecties
            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                ClientHandler clientSock = new ClientHandler(client);

                // Maakt een nieuwe tread aan voor elk weerstation dat connect
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
    	private ArrayList<String> stationListNic;
    	private ArrayList<String> stationListBol;
    	private ArrayList<String> stationListSur;
    
        public ClientHandler(Socket socket) {
        	
            this.clientSocket = socket;
            Date currentDate = new Date();
            new SimpleDateFormat("yyyy-MM-dd").format(currentDate);
            stationListNic = new ArrayList<String>();
            stationListBol = new ArrayList<String>();
            stationListSur = new ArrayList<String>();
            
        }
        
        public String split(String s){
        	
            int beginString = -1;
            int endString = -1;
            beginString = s.indexOf(">") + 1;
            endString = s.indexOf("</");
            s = s.substring(beginString,endString);
            return s;
            
        }

        @Override
        public synchronized void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            String station = " ";
            String date = " ";
            String time = " ";
            String windSpeed = " ";
            String cloudCoverage = " ";
            stationListNic.add("298690");
            stationListBol.add("947260");
            stationListSur.add("749538");
      
            try {
                out = new PrintWriter("/home/pi/data/weather.xml");
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                
                String line;
                while ((line = in.readLine()) != null) {
                    if(line.contains("<STN>")) {
                    	station = split(line);
                    }
                    if(line.contains("<DATE>")) {
                    	date = split(line);
                    }
                    if(line.contains("<TIME>")) {
                    	time = split(line);
                    }
                    if(line.contains("<WDSP>")) {
                    	windSpeed = split(line);
                    }
                    if(line.contains("<CLDC>")) {
                    	cloudCoverage = split(line);
                    }
                    if(stationListNic.contains(station)) {
                    	
                    	// zorgt dat de parseFloat methode werkt
                    	if (!windSpeed.trim().isEmpty() && windSpeed != null) {
                    		windSpeed = windSpeed.replace(",", ".");
                    		windQueue.add(windSpeed);
                    	}
                    	if (!cloudCoverage.trim().isEmpty() && windSpeed != null)  {
                    		cloudCoverage = cloudCoverage.replace(",", ".");
                    		cloudQueue.add(cloudCoverage);
                    	}
                    	
                    	
                    	//Error check
                    	windSpeed = ErrorCorrection.errorCheck(windQueue, windSpeed);
                    	cloudCoverage = ErrorCorrection.errorCheck(cloudQueue, cloudCoverage);
                    	
                    	System.out.println(station);
                        System.out.println(date);
                        System.out.println(time);
                        System.out.println(windSpeed);
                        System.out.println(cloudCoverage);
                        out.write("NN");
                        out.write(station);
                        out.write(">");
                        out.write("DN");
                        out.write(date);
                        out.write(">");
                        out.write("TN");
                        out.write(time);
                        out.write(">");
                        out.write("WN");
                        out.write("W");
                        out.write(windSpeed);
                        out.write(">");
                        out.write("CN");
                        out.write(cloudCoverage);
                        out.write(">");
                    }
                    if(stationListBol.contains(station)) {
                    	
                    	// zorgt dat de parseFloat methode werkt
                    	if (!windSpeed.trim().isEmpty() && windSpeed != null) {
                    		windSpeed = windSpeed.replace(",", ".");
                    		windQueue.add(windSpeed);
                    	}
                    	if (!cloudCoverage.trim().isEmpty() && windSpeed != null)  {
                    		cloudCoverage = cloudCoverage.replace(",", ".");
                    		cloudQueue.add(cloudCoverage);
                    	}
                    	
                    	//Error check
                    	windSpeed = ErrorCorrection.errorCheck(windQueue, windSpeed);
                    	cloudCoverage = ErrorCorrection.errorCheck(cloudQueue, cloudCoverage);
                    	
                        System.out.println(station);
                        System.out.println(date);
                        System.out.println(time);
                        System.out.println(windSpeed);
                        System.out.println(cloudCoverage);
                        out.write("BB");
                        out.write(station);
                        out.write(">");
                        out.write("DB");
                        out.write(date);
                        out.write(">");
                        out.write("TB");
                        out.write(time);
                        out.write(">");
                        out.write("WB");
                        out.write(windSpeed);
                        out.write(">");
                        out.write("CB");
                        out.write(cloudCoverage);
                        out.write(">");
                        
                    }
                    if(stationListSur.contains(station)) {
                    	
                    	// zorgt dat de parseFloat methode werkt
                    	if (!windSpeed.trim().isEmpty() && windSpeed != null) {
                    		windSpeed = windSpeed.replace(",", ".");
                    		windQueue.add(windSpeed);
                    	}
                    	if (!cloudCoverage.trim().isEmpty() && windSpeed != null)  {
                    		cloudCoverage = cloudCoverage.replace(",", ".");
                    		cloudQueue.add(cloudCoverage);
                    	}
                    	
                    	
                    	//Error check
                    	windSpeed = ErrorCorrection.errorCheck(windQueue, windSpeed);
                    	cloudCoverage = ErrorCorrection.errorCheck(cloudQueue, cloudCoverage);
                    	
                        System.out.println(station);
                        System.out.println(date);
                        System.out.println(time);
                        System.out.println(windSpeed);
                        System.out.println(cloudCoverage);
                        out.write("SS");
                        out.write(station);
                        out.write(">");
                        out.write("DS");
                        out.write(date);
                        out.write(">");
                        out.write("TS");
                        out.write(time);
                        out.write(">");
                        out.write("WS");
                        out.write(windSpeed);
                        out.write(">");
                        out.write("CS");
                        out.write(cloudCoverage);
                        out.write(">");
                     

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null)
                        in.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}