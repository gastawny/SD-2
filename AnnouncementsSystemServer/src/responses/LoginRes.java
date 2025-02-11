package responses;

public class LoginRes extends Response {
    private final String token;

    public LoginRes(String response, String message, String token) {
        super(response, message);
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", response='" + response + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
