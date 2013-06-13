import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.Flushable;

public class BitOutputStream implements Closeable, Flushable{
	private OutputStream out;
	private int bufferPosition;
	private int buffer;

	public BitOutputStream(OutputStream out){
		this.out = out;	
		bufferPosition = 0;
	}

	public void writeInt(int i) throws IOException{
		for (int j = 0; j < 4; j ++){
			out.write((0xFF << (8 * (3 - j))) & i);
		}
	}

	public void writeBit(int bit) throws IOException{
		if (bufferPosition == 8){
			flush();
		}
		buffer |= (bit << (7 - bufferPosition));
		bufferPosition ++;
	}

	public void writeBits(int[] bits) throws IOException{
		for (int i : bits){
			writeBit(i);
		}
	}

	public void flush() throws IOException{
		if (bufferPosition == 0){
			return;
		}
		out.write(buffer);
		bufferPosition = 0;
		buffer = 0;
	}
	
	public void close() throws IOException{
		flush();
		out.close();
	}

	public static void main(String[] args){
		try{
		BitOutputStream bs = new BitOutputStream(new FileOutputStream("out.dat"));	
		BitOutputStream bs2 = new BitOutputStream(new FileOutputStream("out2.dat"));	
		bs.writeInt(1);
		bs.writeInt(2);
		bs.writeInt(3);
		bs.writeInt(4);
		bs.writeInt(5);
		bs.close();
		bs2.writeInt(1);
		bs2.writeBit(0);
		bs2.writeBit(1);
		bs2.writeBit(0);
		bs2.writeBit(1);
		bs2.writeBit(0);
		bs2.writeBit(1);
		bs2.writeBit(0);
		bs2.writeBit(1);
		bs2.close();
		} catch (IOException e){}
	}
}
