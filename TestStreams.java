import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
public class TestStreams{

	public static void main(String[] args){
		try{
			BitOutputStream bo = new BitOutputStream(new FileOutputStream("test.txt"));
			bo.writeInt(20);
			bo.writeBit(0);
			bo.writeBit(1);
			bo.writeBit(0);
			bo.writeBit(1);
			bo.close();
			BitInputStream bi = new BitInputStream(new FileInputStream("test.txt"));
			System.out.println(bi.readInt());
			System.out.println(bi.readBit());
			System.out.println(bi.readBit());
			System.out.println(bi.readBit());
			System.out.println(bi.readBit());
			bi.close();
		} catch (IOException e){}
	}
}
