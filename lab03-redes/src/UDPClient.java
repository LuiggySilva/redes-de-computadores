import java.io.*;
import java.net.*;

class UDPClient {
    public static void main(String args[]) throws Exception {

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName("localhost");
        int Port = 9876;

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        
        String sentence = inFromUser.readLine();
        String idPkt = " pkt01";
        
        sentence += idPkt;
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Port);
        
        clientSocket.send(sendPacket);
        System.out.println("FROM CLIENT: Send" + idPkt);
        clientSocket.setSoTimeout(3000);
        
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        
        // Esperando ACK do pacote enviado com timer de 3s
        while(true) {
        	try {
        		clientSocket.receive(receivePacket);
        		String receive = new String(receivePacket.getData());
        		if(receive.startsWith("ACK")) {
        			System.out.println("FROM SERVER: " + receive);
        			clientSocket.setSoTimeout(0);
        			break;
        		}
            
			} catch (SocketTimeoutException e) {
				 // Reenviando o pacote e resetando o timer para 3s
				 clientSocket.send(sendPacket);
				 System.out.println("FROM CLIENT: Timeout " + idPkt);
			     clientSocket.setSoTimeout(3000);
			}
        }
        
        // Esperando a resposta do servidor
        while(true) {
        	clientSocket.receive(receivePacket);
    		String receive = new String(receivePacket.getData());
        	if (!receive.isEmpty()) {
        		System.out.println("FROM CLIENT: " + receive.split("\n")[0]);
        		String ackResponse = "ACK" + idPkt;
				DatagramPacket sendACK = new DatagramPacket(ackResponse.getBytes(), ackResponse.getBytes().length, IPAddress, Port);
				clientSocket.send(sendACK);
        		break;
        	}
        }
        clientSocket.close();
    }
} 