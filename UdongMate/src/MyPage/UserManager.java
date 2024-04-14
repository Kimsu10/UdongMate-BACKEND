package MyPage;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static Map<Integer, String> loggedInUsers = new HashMap<>();

    // ����� �α��� ���� �߰�
    public static void setUserImage(int userNo, String userImg) {
        loggedInUsers.put(userNo, userImg);
    }

    // ����� �α��� ���� ��������
    public static String getUserImage(int userNo) {
        return loggedInUsers.get(userNo);
    }
}
