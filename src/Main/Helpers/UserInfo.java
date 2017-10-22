package Main.Helpers;


public class UserInfo {
    public static int typeId;
    public static int accessId;

    public static int getTypeId() {
        return typeId;
    }

    public static void setTypeId(int typeId) {
        UserInfo.typeId = typeId;
    }

    public static int getAccessId() {
        return accessId;
    }

    public static void setAccessId(int accessId) {
        UserInfo.accessId = accessId;
    }
}
