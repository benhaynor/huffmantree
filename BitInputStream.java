import java.io.IOException;
import java.io.InputStream;
import java.io.Closeable;
import java.io.Flushable;

public class BitInputStream implements Closeable{
	private InputStream in;
	private int bufferPosition;
	private int nextByte;

	public BitInputStream(InputStream in){
		this.in = in;	
		bufferPosition = 0;
	}

	public int readInt() throws IOException{
		int i = 0;
		for (int j = 0; j < 4; j++){
			i |= ((in.read() & 0xFF)  << (8 * (3-j)));
		}
		return i;
	}

	public int readBit() throws IOException{
		if (bufferPosition == 0){
			nextByte = in.read();
		}
		if (nextByte == -1) return -1;
		int nextBit = (nextByte & (0x1 << (7 - bufferPosition))) >> (7 - bufferPosition);	
		bufferPosition ++;
		bufferPosition %= 8;
		return nextBit;
	}

	public void close() throws IOException{
		in.close();
	}
}
