package MyPage;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<Integer, String> loggedInUsers = new HashMap<>();

    // 사용자 로그인 정보 추가
    public static void setUserImage(int userNo, String userImg) {
        loggedInUsers.put(userNo, userImg);
    }

    // 사용자 로그인 정보 가져오기
    public static String getUserImage(int userNo) {
        return loggedInUsers.get(userNo);
    }
}
