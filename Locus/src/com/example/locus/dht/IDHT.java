
import java.util.Set;

public interface IDHT {
	    Result join();
	    Result create();
        Result put(User user);
        Set<User> getUsersByKey(User user);
        Result delete(User user);
        void leave();
}
