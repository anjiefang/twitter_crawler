import java.io.*;


public class serialize {
	public static void serHT(Object o, String dir, String name)throws Exception{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(dir+"/"+name)));
		out.writeObject(o);
		out.flush();
		out.close();
	}
	
	public static Object deSerHT(String fileName)throws Exception{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(fileName)));
		Object o = in.readObject();
		in.close();
		return o;
	}
}
