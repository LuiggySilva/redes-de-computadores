package HTTPServer;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HTTPServer {	
	
	public static void main(String[] args) throws Exception{		
		String requestMessageLine, fileName;
				
		String statusLine, contentTypeLine, entityBody, CRLF="\r\n";
		
		@SuppressWarnings("resource")
		ServerSocket listenSocket = new ServerSocket(7787);
		
		while (true) {
			Socket connectionSocket = listenSocket.accept();
		
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			requestMessageLine = inFromClient.readLine();
		
			StringTokenizer tokenizedLine = new StringTokenizer(requestMessageLine);
		
			if(tokenizedLine.nextToken().equals("GET")){
				fileName = tokenizedLine.nextToken();
				if(fileName.startsWith("/")) {
					fileName = fileName.substring(1);
					System.out.println(fileName);
				}
	
				if(fileName.equals("teste.txt")) {
					File file = new File("/home/luiggysilva/eclipse-workspace/lab02-redes/src/files/" + fileName);
		            FileInputStream txtInFile = new FileInputStream(file);
		            byte txtData[] = new byte[(int) file.length()];
		            txtInFile.read(txtData);
		            txtInFile.close();
		            
		            
					statusLine = "HTTP/1.0 200 OK" + CRLF;
					contentTypeLine = "Content-Type: text/plain" + CRLF;
					contentTypeLine += "Content-Length:" + file.length() + CRLF;
					
					outToClient.writeBytes(statusLine);
					outToClient.writeBytes(contentTypeLine);
					outToClient.writeBytes(CRLF);
					
					for (byte b : txtData) {
		            	outToClient.write(b);
					}
				}
				else if(fileName.equals("teste.html")) {
					File file = new File("/home/luiggysilva/eclipse-workspace/lab02-redes/src/files/" + fileName);
		            FileInputStream htmlInFile = new FileInputStream(file);
		            byte htmlData[] = new byte[(int) file.length()];
		            htmlInFile.read(htmlData);
		            htmlInFile.close();
					
					statusLine = "HTTP/1.0 200 OK" + CRLF;
					contentTypeLine = "Content-Type: text/html" + CRLF;
					contentTypeLine += "Content-Length:" + file.length() + CRLF;
					
					outToClient.writeBytes(statusLine);
					outToClient.writeBytes(contentTypeLine);
					outToClient.writeBytes(CRLF);
					
					for (byte b : htmlData) {
		            	outToClient.write(b);
					}
				}
				else if(fileName.equals("teste.jpg")) {
					File file = new File("/home/luiggysilva/eclipse-workspace/lab02-redes/src/files/" + fileName);
		            FileInputStream imageInFile = new FileInputStream(file);
		            byte imageData[] = new byte[(int) file.length()];
		            imageInFile.read(imageData);
		            imageInFile.close();
					
					statusLine = "HTTP/1.0 200 OK" + CRLF;
					contentTypeLine = "Content-Type: image/jpg" + CRLF;
					contentTypeLine += "Content-Length:" + file.length() + CRLF;
					
					outToClient.writeBytes(statusLine);
					outToClient.writeBytes(contentTypeLine);
					outToClient.writeBytes(CRLF);
		        
		            for (byte b : imageData) {
		            	outToClient.write(b);
					}
				}
				else if(fileName.equals("teste.json")) {
					File file = new File("/home/luiggysilva/eclipse-workspace/lab02-redes/src/files/" + fileName);
		            FileInputStream jsonInFile = new FileInputStream(file);
		            byte jsonData[] = new byte[(int) file.length()];
		            jsonInFile.read(jsonData);
		            jsonInFile.close();
					
					statusLine = "HTTP/1.0 200 OK" + CRLF;
					contentTypeLine = "Content-Type: application/json" + CRLF;
					contentTypeLine += "Content-Length:" + file.length() + CRLF;
					outToClient.writeBytes(statusLine);
					outToClient.writeBytes(contentTypeLine);
					outToClient.writeBytes(CRLF);
					
					for (byte b : jsonData) {
		            	outToClient.write(b);
					}
				}
				else {
					statusLine = "HTTP/1.0 404 Not Found" + CRLF;
					contentTypeLine = "Content-Type: text/html" + CRLF;
					entityBody = "<HTML>" + 
							"<HEAD><TITLE> Não encontrado.</TITLE></HEAD>" +
							"<BODY> Arquivo " + fileName + " não encontrado.</BODY></HTML>";
					
					outToClient.writeBytes(statusLine);
					outToClient.writeBytes(contentTypeLine);
					outToClient.writeBytes(CRLF);
					outToClient.writeBytes(entityBody);
				}
			
				connectionSocket.close();
			}		
		}
	}
}