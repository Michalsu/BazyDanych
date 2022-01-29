public class ParseAnswerClient {


    public static String parseRequest(String answer) {
        if (answer == "") return "ERROR NO ANSWER";
        String[] substrings = answer.split("#");
        int parimeters = substrings.length;
        int exCode = -1;
        String response = "ERROR";
        switch (substrings[0]) {
            case "LOGIN":
                if(substrings[1]=="SUCCESSFUL")


                    if(exCode==0) response = "LOGIN#";
                    else if(exCode==-1) response = "LOGIN#WRONGPASS";
                    else if(exCode==-2) response = "LOGIN#WRONGNAME";
                    else response = "LOGIN#ERROR: "+exCode;
                break;
        }
        return "?";
    }
}

