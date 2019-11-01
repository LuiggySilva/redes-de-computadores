
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.HashMap;

class UDPServer {
	public static void main(String args[]) throws Exception {

		@SuppressWarnings("resource")
		DatagramSocket serverSocket = new DatagramSocket(9876);

		byte[] receiveData = new byte[1024];
		byte[] sendData  = new byte[1024];
		HashMap<String, String> receivedRequests = new HashMap<String, String>();
		HashMap<String, Integer> receivedRequestsCouting = new HashMap<String, Integer>();
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		DatagramPacket sendPacket = null;
		
		while(true)	{
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String idPckt = sentence.split(" ")[sentence.split(" ").length-1];
			
			String ackResponse = "ACK " + idPckt;
			DatagramPacket sendACK = new DatagramPacket(ackResponse.getBytes(), ackResponse.getBytes().length, IPAddress, port);
			
			if(!receivedRequests.containsKey(idPckt)) {
				receivedRequests.put(idPckt, sentence);
				receivedRequestsCouting.put(idPckt, 1);
				// Só comentar o envio do Ack pra testar a perda de ACK do servidor
				serverSocket.send(sendACK);
				//printReceivedRequests(receivedRequests);
			}
			
			
			
			String capitalizedSentence = "";
			String op = sentence.split(" ")[0];
		    double x = Double.parseDouble(sentence.split(" ")[1]);
		    double y = Double.parseDouble(sentence.split(" ")[2]);
		    double resul = 0;
		    boolean zeroDivision = false;
		    
		    switch (op) {
			case "ADD":
				resul = x + y;
				break;
			case "SUB":
				resul = x - y;
				break;
			case "MULT":
				resul = x * y;
				break;
			case "DIV":
				if(y == 0) {
					zeroDivision = true;
				}
				else {
					resul = x / y;
				}
				break;
			case "EXP":
				resul = Math.pow(x, y);
				break;
			}
		    if(zeroDivision) {
		    	capitalizedSentence = "ERROR: ZERO DIVISION" + '\n';
		    }
		    else {
		    	capitalizedSentence = Double.toString(resul) + '\n';
		    }

			sendData = capitalizedSentence.getBytes();

			sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

			// Enviando resposta
			serverSocket.send(sendPacket);
			
			// Esperando confirmação da resposta
			while(true) {
				serverSocket.setSoTimeout(3000);
				try {
	        		serverSocket.receive(receivePacket);
	        		String receive = new String(receivePacket.getData());
	        		if(receive.startsWith("ACK")) {
	        			serverSocket.setSoTimeout(0);
	        			break;
	        		}
	        		else {
	        			// Verifico se já recebi um pacote com esse ID 3 vezes, se sim reenvio o pacote e reseto o contador 
	    				if(receivedRequestsCouting.get(receive.split(" ")[3]) == 3) {
	    					serverSocket.send(sendACK);
	    					receivedRequestsCouting.put(receive.split(" ")[3], 1);
	    				}
	    				else {
	    					int aux = receivedRequestsCouting.get(receive.split(" ")[3]);
	    					receivedRequestsCouting.put(receive.split(" ")[3], aux+1);
	    				}
	    			}
				} catch (SocketTimeoutException e) {
					 // Reenviando o pacote e resetando o timer para 3s
					 serverSocket.send(sendPacket);
				     serverSocket.setSoTimeout(3000);
				}
			}
		}
		
	}
	
	@SuppressWarnings("unused")
	private static void printReceivedRequests(HashMap<String, String> map) {
		String resul = "";
		for (String key : map.keySet()) {
			resul += key + ":" + map.get(key) + "\n";
		}
		System.out.println(resul);
	}
	
	private static void printArray(String x) {
		for (String e : x.split(" ")) {
			System.out.println(e + "\n");
		}
	}
}
