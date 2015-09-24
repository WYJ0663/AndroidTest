package mobi.shoumeng.judge;

public interface Crypto {

    public String encrypt(String key, String content) throws Exception;

    public String decrypt(String key, String content) throws Exception;

}
