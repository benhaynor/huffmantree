import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream{
	private OutputStream out;
	private int bufferPosition;
	private int buffer;

	public BitOutputStream(OutputStream out){
		this.out = out;	
		bufferPosition = 0;
	}

	public void writeBit() throws IOException{
		if (bufferPosition == 8){
			flush();
		}
		buffer |= 0x1 << (7 - bufferPosition);
		bufferPosition ++;
	}

	public void flush() throws IOException{
		if (bufferPosition == 0){
			return;
		}
		out.write(buffer);
		bufferPosition = 0;
		buffer = 0;
	}
}
