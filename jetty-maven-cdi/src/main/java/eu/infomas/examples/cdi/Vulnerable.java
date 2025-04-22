import com.google.flatbuffers.FlatBufferBuilder;
import java.util.Date;
import java.util.Vector;
import java.beans.Transient;

public class LegacyFlatBuffersExample {
    // Legacy collection (Vector is outdated, prefer ArrayList)
    private Vector<String> messages = new Vector<>();
    
    // Legacy Date class (prefer java.time API in modern Java)
    private Date creationDate;
    
    // Constructor using StringBuffer (legacy, prefer StringBuilder)
    public LegacyFlatBuffersExample() {
        this.creationDate = new Date();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Initialized at ").append(creationDate.toString());
        messages.add(buffer.toString());
    }
    
    // Method using FlatBuffers to create a simple buffer
    public byte[] createFlatBufferMessage(String message) {
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int messageOffset = builder.createString(message);
        builder.finish(messageOffset);
        return builder.sizedByteArray();
    }
    
    // Legacy method using Transient annotation (not inherently vulnerable but outdated)
    @Transient
    public String getFormattedDate() {
        return creationDate.toString();
    }
    
    // Add message to Vector
    public void addMessage(String message) {
        messages.add(message);
    }
    
    // Get messages (exposing legacy Vector)
    public Vector<String> getMessages() {
        return messages;
    }
    
    public static void main(String[] args) {
        LegacyFlatBuffersExample example = new LegacyFlatBuffersExample();
        example.addMessage("Test message");
        
        // Create a FlatBuffer
        byte[] flatBuffer = example.createFlatBufferMessage("Hello, FlatBuffers!");
        System.out.println("FlatBuffer created with size: " + flatBuffer.length);
        
        // Print legacy date and messages
        System.out.println("Creation Date: " + example.getFormattedDate());
        System.out.println("Messages: " + example.getMessages());
    }
}
