import java.io.IOException;
import java.io.InputStream;

public class BitInputStream{
	private InputStream in;
	private int bufferPosition;
	private int nextByte;

	public BitInputStream(InputStream in){
		this.in = in;	
		bufferPosition = 0;
	}

	public int readBit() throws IOException{
		if (bufferPosition == 0){
			nextByte = in.read();
			if (nextByte == -1) return -1;
		}
		int nextBit = nextByte & (0x1 << bufferPosition);	
		bufferPosition ++;
		bufferPosition %= 8;
		return nextBit;
	}
}
