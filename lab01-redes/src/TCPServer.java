import java.io.*; 
import java.net.*; 

class TCPServer { 
    
    public static void main(String argv[]) throws Exception 
    { 
	String clientSentence; 
	String capitalizedSentence; 
	
	ServerSocket welcomeSocket = new ServerSocket(6796); 
	
	while(true) { 
	    
            Socket connectionSocket = welcomeSocket.accept(); 
	    
	    BufferedReader inFromClient = 
		new BufferedReader(new
		    InputStreamReader(connectionSocket.getInputStream())); 
	    
	    DataOutputStream  outToClient = 
		new DataOutputStream(connectionSocket.getOutputStream()); 
	    
	    clientSentence = inFromClient.readLine(); 
	    
	    String op = clientSentence.split(" ")[0];
	    double x = Double.parseDouble(clientSentence.split(" ")[1]);
	    double y = Double.parseDouble(clientSentence.split(" ")[2]);
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
	     
	    
	    outToClient.writeBytes(capitalizedSentence); 
        } 
    } 
} 