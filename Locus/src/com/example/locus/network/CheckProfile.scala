import java.net._
import java.io._
import com.example.locus.entity.User
import com.example.locus.entity.Sex
import com.example.locus.core.CoreFacade

object CheckProfile {
  	def listen(port: Integer): Unit = {
		val ss = new ServerSocket(port)
		while(true){
		    println("listening...")
			val sock = ss.accept()
			actors.Actor.actor{
				val oos = new ObjectOutputStream(sock.getOutputStream())
				oos.writeObject(CoreFacade.getInstance().getCurrentUser())
				oos.flush()
			}
		}
		ss.close()
    }
	
	def connect(ip: String, port: Integer): Object = {
  		val sock = new Socket(ip, port)
		val ois = new ObjectInputStream(sock.getInputStream())
		val user = ois.readObject.asInstanceOf[User]
  		println(user.toString())
		sock.close()
		CoreFacade.getInstance().onReceiveUserProfile(user)
		return user
    }
	
	def main(args: Array[String]): Unit = {
		//listen(2222)
		//connect("localhost", 2222)
	}
}