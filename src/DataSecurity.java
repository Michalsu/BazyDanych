import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class DataSecurity {



    /**
     * Generates hash from password and salt
     * @param password to be hashed
     * @param salt introduces some variety to password
     * @return string with hashed password
     */

    public static String getHashSHA512(String password, String salt) {
        String genPass = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            genPass = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return genPass+":"+salt;
    }

    /**
     * Generates random salt for Hashing function
     *
     * @return a string containing randomly generated salt
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        StringBuilder saltVal = new StringBuilder();
        for (int i = 0; i < salt.length; i++) {
            saltVal.append(Integer.toString(salt[i] , 16).substring(1));
        }
        return saltVal.toString();
    }


    /**
     * Checks if password is correct
     * @param password to compare with database entry
     * @param salt used to hash password in database
     * @param hashedPass prom database
     * @return true if this correct , otherwise false
     */
    public static boolean checkPasswords(String password, String salt, String hashedPass) {
        if (hashedPass.equals(getHashSHA512(password, salt))) return true;
        return false;
    }

    /**
     * Checks if password meets minimal safety requirements and restrictions
     * @param password to be checked
     * @return true if password is valid, otherwise false
     */
    public static boolean passwordValid(String password){
        if(password.length()>=8){
            Pattern letter = Pattern.compile("[a-z]",Pattern.CASE_INSENSITIVE);
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?~-]");
            if(letter.matcher(password).find() &&
                    digit.matcher(password).find() &&
                    special.matcher(password).find() &&
                    !containIllegalSymbols(password)) return true;
        }
        return false;
    }

    /**
     *
     * @param text string to serch illegal symbols in
     * @return true if contains illegal symbols, otherwise false
     */
    public static boolean containIllegalSymbols(String text){
        Pattern special = Pattern.compile("[\'\"\\[\\]{}/]");
        Matcher m = special.matcher(text);
        return m.find();
    }

    /**
     * Removes illegal symbols from input
     * @param input to sanitize
     * @return sanitized String
     */
    public static String sanitizeInput(String input){
        String safeString=null;
        safeString = input.replaceAll("([\'\"\\[\\]{}/])", "");
        return safeString;
    }

    /**
     *
     * @param con connection
     * @param nickname of the user trying to login
     * @param password to check
     * @return 0 - password correct, -1 - password incorrect, -2 nickname contain illegal symbols
     *
     */
    public static int login(Connection con, String nickname, String password){
        String hash = null;
        String salt = null;
        if(containIllegalSymbols(nickname)) return -2;
        try {
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT haslo FROM klient WHERE login ='"+ nickname+"';");
            hash = rs.getString("haslo").split(":")[0];
            System.out.println(hash);
            salt = rs.getString("haslo").split(":")[1];
            System.out.println(salt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(checkPasswords(password,salt, hash)) return 0;
        return -1;
    }




    }