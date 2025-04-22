import com.google.flatbuffers.FlatBufferBuilder;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class VulnerableLegacyJava implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Legacy collection (Vector is deprecated, prefer ArrayList)
    private Vector<String> data = new Vector<>();
    
    // Legacy Date with SimpleDateFormat (not thread-safe)
    private Date creationDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    public VulnerableLegacyJava() {
        this.creationDate = new Date();
        data.add("Initialized on " + dateFormat.format(creationDate));
    }
    
    // Unsafe deserialization (vulnerable to RCE if untrusted input)
    public Object unsafeDeserialize(byte[] input) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(input);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object result = ois.readObject();
        ois.close();
        return result;
    }
    
    // Weak cryptography (MD5 is cryptographically broken)
    public String computeHash(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(input.getBytes());
        StringBuffer hexString = new StringBuffer();
        for (byte b : hash) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }
    
    // Use FlatBuffers (vulnerable dependency)
    public byte[] createFlatBufferMessage(String message) {
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int messageOffset = builder.createString(message);
        builder.finish(messageOffset);
        return builder.sizedByteArray();
    }
    
    // Legacy date formatting (not thread-safe)
    public String getFormattedDate() {
        return dateFormat.format(creationDate);
    }
    
    // Add data to Vector
    public void addData(String item) {
        data.add(item);
    }
    
    // Expose legacy Vector
    public Vector<String> getData() {
        return data;
    }
    
    public static void main(String[] args) {
        try {
            VulnerableLegacyJava app = new VulnerableLegacyJava();
            
            // Use FlatBuffers
            byte[] flatBuffer = app.createFlatBufferMessage("Hello, FlatBuffers!");
            System.out.println("FlatBuffer size: " + flatBuffer.length);
            
            // Compute weak hash
            String hash = app.computeHash("test");
            System.out.println("MD5 Hash: " + hash);
            
            // Use legacy date
            System.out.println("Date: " + app.getFormattedDate());
            
            // Add and print data
            app.addData("Sample data");
            System.out.println("Data: " + app.getData());
            
            // Simulate unsafe deserialization (DO NOT USE IN PRODUCTION)
            // byte[] maliciousInput = ...; // Hypothetical malicious input
            // app.unsafeDeserialize(maliciousInput);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
