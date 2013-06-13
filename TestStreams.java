import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

public class TestStreams{

	public static void simpleIOTest(){
		try{
			BitOutputStream bo = new BitOutputStream(new FileOutputStream("test.txt"));
			bo.writeInt(20);
			bo.writeBit(0);
			bo.writeBit(1);
			bo.writeBit(0);
			bo.writeBit(1);
			bo.close();
			BitInputStream bi = new BitInputStream(new FileInputStream("test.txt"));
			assert(bi.readInt() == 20);
			assert(bi.readBit() == 0);
			assert(bi.readBit() == 1);
			assert(bi.readBit() == 0);
			assert(bi.readBit() == 1);
			System.out.println("Successfully wrote and read a bit stream.");
			bi.close();
			File bif = new File("test.txt");
			bif.delete();
		} catch (IOException e){
			System.exit(1);
		}
	}

	public static void main(String[] args){
		simpleIOTest();
	}
}
