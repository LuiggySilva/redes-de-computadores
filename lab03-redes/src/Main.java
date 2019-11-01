
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {
	
	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
	
	@SuppressWarnings("unused")
	private static String hash(String file) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(file.getBytes());
		byte[] bytes = md.digest();

	   StringBuilder s = new StringBuilder();
	   for (int i = 0; i < bytes.length; i++) {
	       int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
	       int parteBaixa = bytes[i] & 0xf;
	       if (parteAlta == 0) s.append('0');
	       s.append(Integer.toHexString(parteAlta | parteBaixa));
	   }
	   return s.toString();
	}
}
